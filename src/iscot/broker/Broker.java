package iscot.broker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

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
	}
	
	protected synchronized void removeMessage(Message message) {
		messages.get(message.getType()).remove(message);
		notifyAll();
	}

	public synchronized Message getMessage(Type type, String area) throws InterruptedException {
		while(messages.isEmpty() || noMessageFor(type,area))
			wait();
		return messages.get(area).getFirst();
	}

	private boolean noMessageFor(Type type, String area) {
		return !messages.get(area).isEmpty() && messages.get(area).getFirst().getType()==type;
	}
}
