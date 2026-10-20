package iscot.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.message.Message;
import iscot.message.sensor.LightSensorMessage;

public class DealWithDecider extends DealWithClient {

	public DealWithDecider(ObjectInputStream in, ObjectOutputStream out, Server server, Areas area) {
		super( server, area, in, out);
	}

	@Override
	public void run() {
		while(true)
			try {
				// TODO: deal with other deciders
				Message message=server.getMessage(Type.SENSOR, getArea());
				System.err.println("Decider got: "+message);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
