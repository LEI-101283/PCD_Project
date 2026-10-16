package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class LightingSensor {
	
	/**
	 * 
	 * @param server address and port
	 * @param roomName
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws NumberFormatException 
	 */
	public static void main(String[] args) throws NumberFormatException, UnknownHostException, IOException {
		// TODO Auto-generated method stub

		Socket socket=new Socket(args[0],Integer.parseInt(args[1]));
		ObjectOutputStream out= new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream in = new ObjectInputStream(socket.getInputStream()); // useless, no incoming messages
		
		while(true) {
			
		}
	}

}
