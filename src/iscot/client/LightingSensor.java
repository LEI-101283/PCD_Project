package iscot.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import iscot.enums.Type;
import iscot.message.RegistrationMessage;
import iscot.message.sensor.LightSensorMessage;
import iscot.message.sensor.TemperatureSensorMessage;

public class LightingSensor {

	/**
	 * 
	 * @param server address and port
	 * @param roomName: string
	 * @param id: int
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws NumberFormatException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws NumberFormatException, UnknownHostException, IOException, InterruptedException {
		if(args.length!=4) {
			System.err.println("Invalid arguments!");
		}
		else {
			Socket socket=new Socket(args[0],Integer.parseInt(args[1]));
			ObjectOutputStream out= new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream()); // useless, no incoming messages

			// register with the server
			String area=args[2];
			out.writeObject(new RegistrationMessage(area, Type.SENSOR,Integer.parseInt(args[3])));

			while(true) {
				out.writeObject(new LightSensorMessage(area,(float)(17+Math.random()*6)));
				Thread.sleep(5000);
			}
		}
	}

}
