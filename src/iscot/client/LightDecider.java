package iscot.client;


import java.awt.GridLayout;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import iscot.client.gui.IconeLuz;
import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.message.Message;
import iscot.message.RegistrationMessage;
import iscot.message.highlevelorder.AreaLightingOrderMessage;
import iscot.message.sensor.LightSensorMessage;

public class LightDecider {

	/**
	 * 
	 * @param server address and port
	 * @param roomName: string
	 * @param lightThreshold: int
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws NumberFormatException 
	 * @throws InterruptedException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws NumberFormatException, UnknownHostException, IOException, InterruptedException, ClassNotFoundException {
		if(args.length!=4) {
			System.err.println("Invalid arguments!");
		}
		else {
			Socket socket=new Socket(args[0],Integer.parseInt(args[1]));
			ObjectOutputStream out= new ObjectOutputStream(socket.getOutputStream()); 
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

			// register with the server
			Areas area=Areas.valueOf(args[2]);
			out.writeObject(new RegistrationMessage(area, Type.SENSOR));

			int lightThreshold = Integer.parseInt(args[3]);

			while(true) {
				Message message=(Message) in.readObject();
				if( message instanceof LightSensorMessage) {
					LightSensorMessage lightSensorMessage=(LightSensorMessage)message;
					if(lightSensorMessage.area == area)
						if(lightSensorMessage.light<lightThreshold) {
							out.writeObject(new AreaLightingOrderMessage(area, true));
							System.err.println("Ordering light on at:"+area);
						}
						else {
							out.writeObject(new AreaLightingOrderMessage(area, false));
							System.err.println("Ordering light off at:"+area);
						}
					else
						System.err.println("Message for unexpected area!");
				}
				else
					System.err.println("Unexpected message in Light:"+message);

			}
		}
	}
}
