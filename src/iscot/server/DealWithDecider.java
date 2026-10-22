package iscot.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.message.Message;
import iscot.message.highlevelorder.HighLevelOrderMessage;
import iscot.message.sensor.LightSensorMessage;

public class DealWithDecider extends DealWithClient {

	public DealWithDecider(ObjectInputStream in, ObjectOutputStream out, Server server, Areas area) {
		super( server, area, in, out);
	}

	@Override
	public void run() {
		// Create thread to send relevant messages
		new Thread( new Runnable() {

			@Override
			public void run() {

				HighLevelOrderMessage highLevelOrderMessage;
				while(true) {
					try {
						highLevelOrderMessage = (HighLevelOrderMessage)in.readObject();
						System.err.println("Got HighLevelOrderMessage:"+highLevelOrderMessage);
						server.queueMessage(highLevelOrderMessage);
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}).start();
		while(true)
			try {
				// TODO: deal with other deciders
				Message message=server.getMessage(Type.SENSOR, getArea(),getClientId());
				System.err.println("Decider got: "+message);
				out.writeObject(message);
			} catch (InterruptedException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	@Override
	public Type getType() {
		return Type.DECIDER;
	}

	@Override
	public String toString() {
		return "DealWithDecider [getType()=" + getType() + ", getArea()=" + getArea() + ", getClientId()="
				+ getClientId() + "]";
	}
}
