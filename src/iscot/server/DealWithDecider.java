package iscot.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import iscot.enums.Type;
import iscot.message.Message;
import iscot.message.sensor.LightSensorMessage;

public class DealWithDecider extends DealWithClient {

	public DealWithDecider(ObjectInputStream in, ObjectOutputStream out, Server server, String area, int id) {
		super(in, out, server, area, id);
	}

	@Override
	public void run() {
		while(true)
			try {
				// TODO: deal with other deciders
				List<Message> messages=server.getMessages(Type.SENSOR, "ALL");
				System.err.println("Decider got: "+messages.size());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
