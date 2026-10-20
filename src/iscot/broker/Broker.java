package iscot.broker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

import iscot.area.Area;
import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.message.Message;
import iscot.server.Server;

public class Broker {
	private Map<Type,List<Message>> messages =new HashMap<Type, List<Message>>();
	private Map<Message,CountDownLatch> cdls=new HashMap<Message,CountDownLatch>();
	private Server server;
	public Broker(Server server) {
		this.server = server;
		for(Type type:Type.values())
			messages.put(type, new ArrayList<Message>());
	}

	public synchronized void queueMessage(Message message) {
		messages.get(message.getType()).add(message);
		CountDownLatch cdl=new CountDownLatch(server.countClientsFor(message.getType(), message.area));
		cdls.put(message, cdl);
		new Thread(new Runnable() {
			@Override
			public void run() {				
				try {
					cdl.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				removeMessage(message);
			}
		}).start();
		notifyAll();
//		System.err.println("Broker: message queued!");
	}
	
	protected synchronized void removeMessage(Message message) {
		messages.get(message.getType()).remove(message);
		notifyAll();
	}

	public synchronized Message getMessage(Type type, Areas area) throws InterruptedException {
		while(messages.isEmpty() || noMessageFor(type,area)) {
			System.err.println(" Wait in getMessage!:"+messages+ "Asked for:"+type+area);
			wait();
		}
		return messages.get(type).get(0);
	}

	private boolean noMessageFor(Type type, Areas area) {
//		System.err.println("Checking messages for:"+area+" Type:"+type);
		return messages.get(type).isEmpty() ||  messages.get(type).get(0).area!=area;
	}

	// Just returns the queue, without any consequence in queue state, no remocals
	public List<Message> checkMessageQueue() {
		List<Message> returnedMessages=new ArrayList<>();
		Collection<List<Message>> allLists=messages.values();
//		for(List<Message> list:allLists)
		returnedMessages.addAll(returnedMessages);
		return returnedMessages;
	}
}
