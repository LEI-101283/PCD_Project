package iscot.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.message.RegistrationMessage;
import iscot.message.sensor.LightSensorMessage;

public class DealWithSensor extends DealWithClient{

	public DealWithSensor(ObjectInputStream in, ObjectOutputStream out, Server server, Areas area) {
		super(server,area,in,out);
	}
	

	@Override
	public Type getType() {
		return Type.SENSOR;
	}


	@Override
	public void run() {
		while(true) {
			try {
				// TODO: deal with other sensors
				LightSensorMessage lightSensorMessage=(LightSensorMessage)in.readObject();
				
				System.err.println("Got lighting message:"+lightSensorMessage);
				server.queueMessage(lightSensorMessage);
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

}
