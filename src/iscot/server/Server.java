package iscot.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import iscot.area.Area;
import iscot.broker.Broker;
import iscot.enums.Type;
import iscot.message.Message;
import iscot.message.sensor.LightSensorMessage;

public class Server {
	private Broker broker= new Broker(this);
	private ServerSocket serverSocket;
	private Map<String,Area> areas= new HashMap<>();

	public Server(int portNumber, List<Area> areas) throws IOException {
		serverSocket= new ServerSocket(portNumber);
		for(Area area:areas)
			this.areas.put(area.name, area);
	}

	/* Server that manages the broker.
	 * @params
	 *  - int portNumber: for connection requests
	 *  - list of String, with names of all existing areas
	 */
	public static void main(String[] args) throws IOException {
		if(args.length <3) {
			System.err.println("Invalid arguments!");
		}
		else {
			int portNumber= Integer.parseInt(args[0]);
			List<Area> areas=new ArrayList<Area>((args.length-1)/2);
			for(int i=1; i!=args.length;i++)
				areas.add(new Area(args[i]));
			new Server(portNumber,areas).startServing();	
		}
	}

	private void startServing() {
		while(true)
			try {
				new DealWithClient(serverSocket.accept(),this).start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println("Connection failed!");
				e.printStackTrace();
			}
	}

	public void register(DealWithClient dealWithClient) {
		if(isAlreadyRegistered(dealWithClient))
			throw new IllegalArgumentException("Sensor already registered!");
		if(!areaExists(dealWithClient.getArea()))
			throw new IllegalArgumentException("Invalid Area!");
		// if all areas...
		if(dealWithClient.getArea().equals("all"))
			for(String areaName:areas.keySet())
				areas.get(areaName).getClients().add(dealWithClient);
		else
			areas.get(dealWithClient.getArea()).getClients().add(dealWithClient);
		
	}

	private boolean areaExists(String area) {
		return areas.containsKey(area);
	}

	private boolean isAlreadyRegistered(DealWithClient dealWithClient) {
		for(DealWithClient client:areas.get(dealWithClient.getArea()).getClients())
			if(client.getClientId()==dealWithClient.getClientId())
				return true;
		return false;
	}

	public void queueMessage(Message message) {
		broker.queueMessage(message);
		
	}

	public int countClientsFor(Type type, String area) {
		int count=0;
		for(DealWithClient client:areas.get(area).getClients())
			if(client.getType()==Type.DECIDER && type==Type.SENSOR)
				count++;
			else if(client.getType()==Type.ACTUATOR && type==Type.DECIDER)
				count++;
		return count;
	}

}
