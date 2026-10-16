package iscot.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import iscot.area.Area;
import iscot.broker.Broker;

public class Server {
	private Broker broker= new Broker();
	private ServerSocket serverSocket;
	private List<Area> areas;

	public Server(int portNumber, List<Area> areas) throws IOException {
		serverSocket= new ServerSocket(portNumber);
		this.areas=areas;
	}

	/* Server that manages the broker.
	 * @params
	 *  - int portNumber: for connection requests
	 *  - list of (String, int) pairs, with name and ID of each existing area
	 */
	public static void main(String[] args) throws IOException {
		if(args.length <3 ||  args.length % 2 == 0) {
			System.err.println("Invalid arguments!");
		}
		else {
			int portNumber= Integer.parseInt(args[0]);
			List<Area> areas=new ArrayList<Area>((args.length-1)/2);
			for(int i=1; i!=args.length;i+=2)
				areas.add(new Area(args[i], Integer.parseInt(args[i+1])));
			new Server(portNumber,areas).startServing();	
		}
	}

	private void startServing() {
		while(true)
			try {
				new DealWithClient(serverSocket.accept()).start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println("Connection failed!");
				e.printStackTrace();
			}
	}

}
