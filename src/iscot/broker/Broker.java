package iscot.broker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import iscot.enums.Type;
import iscot.message.Message;

public class Broker {
	private Map<Type,List<Message>> messages =new HashMap<Type, List<Message>>();

	public void queueMessage(Message message) {
		messages.get(message.)
		
	}
	
}
