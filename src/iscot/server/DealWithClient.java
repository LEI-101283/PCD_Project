package iscot.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import iscot.enums.Type;
import iscot.message.RegistrationMessage;

public class DealWithClient extends Thread{
	protected ObjectInputStream in;
	private ObjectOutputStream out;
	protected Server server;
	private String area;
	public String getArea() {
		return area;
	}
	public int getClientId() {
		return id;
	}
	private int id=-1;
	public DealWithClient(Socket accept,Server server) throws IOException {
		this.server=server;
		in=new ObjectInputStream(accept.getInputStream());
		out=new ObjectOutputStream(accept.getOutputStream());
	}
	public DealWithClient(ObjectInputStream in, ObjectOutputStream out,Server server, String area, int id) {
		this.in=in;
		this.out=out;
		this.server=server;
		this.area=area;
		this.id=id;
	}
	public Type getType() {
		return Type.SENSOR;
	}
	@Override
	public void run() {
		// get registration
		try {
			RegistrationMessage registrationMessage=(RegistrationMessage)in.readObject();
			
			switch (registrationMessage.type) {
			case SENSOR:
					DealWithSensor dealWithSensor=new DealWithSensor(in, out,server,registrationMessage.area,registrationMessage.id);
					server.register(dealWithSensor);
					dealWithSensor.start();
				break;

			default:
				break;
			}
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
