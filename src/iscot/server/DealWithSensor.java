package iscot.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.message.Message;
import iscot.message.RegistrationMessage;
import iscot.message.sensor.HumiditySensorMessage;
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
				Message message=(Message) in.readObject();
				if(message instanceof LightSensorMessage) {
					LightSensorMessage lightSensorMessage=(LightSensorMessage)message;
					System.err.println("Got lighting message:"+lightSensorMessage);
					server.queueMessage(lightSensorMessage);
				}
				else if(message instanceof HumiditySensorMessage) {
					HumiditySensorMessage humiditySensorMessage=(HumiditySensorMessage)message;
					System.err.println("Got HumiditySensorMessage message:"+humiditySensorMessage);
					server.queueMessage(humiditySensorMessage);
				}
				else
					System.err.println( "Unexpcted message at DealWithSensor:"+message);
				
				// TODO: deal with other sensors

			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}


	@Override
	public String toString() {
		return "DealWithSensor [getType()=" + getType() + ", getArea()=" + getArea() + ", getClientId()="
				+ getClientId() + "]";
	}

}
