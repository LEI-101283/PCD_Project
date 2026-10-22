package iscot.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.message.RegistrationMessage;
import iscot.message.sensor.LightSensorMessage;
import iscot.message.sensor.TemperatureSensorMessage;

public class LightSensor {

	/**
	 * 
	 * @param server address and port
	 * @param roomName: string
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws NumberFormatException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws NumberFormatException, UnknownHostException, IOException, InterruptedException {
		if(args.length!=3) {
			System.err.println("Invalid arguments!");
		}
		else {
			Socket socket=new Socket(args[0],Integer.parseInt(args[1]));
			ObjectOutputStream out= new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream()); // useless, no incoming messages

			// register with the server
			Areas area=Areas.valueOf(args[2]);
			out.writeObject(new RegistrationMessage(area, Type.SENSOR));

			while(true) {
				out.writeObject(new LightSensorMessage(area,(float)(100+Math.random()*1000)));
				Thread.sleep(5000);
			}
		}
	}

}
