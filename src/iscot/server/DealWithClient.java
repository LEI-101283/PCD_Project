package iscot.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.message.RegistrationMessage;

public class DealWithClient extends Client{
	public DealWithClient(Server server, Areas area, ObjectInputStream in, ObjectOutputStream out) {
		super(server, area);
		
		this.in=in;
		this.out=out;
	}
	protected ObjectInputStream in;
	private ObjectOutputStream out;
	
	
	@Override
	public void run() {
		// get registration
		try {
			RegistrationMessage registrationMessage=(RegistrationMessage)in.readObject();
			
			switch (registrationMessage.type) {
			case SENSOR:
					DealWithSensor dealWithSensor=new DealWithSensor(in, out,server,registrationMessage.area);
					server.register(dealWithSensor);
					dealWithSensor.start();
				break;
			case DECIDER:
				DealWithDecider dealWithDecider=new DealWithDecider(in, out,server,registrationMessage.area);
				server.register(dealWithDecider);
				dealWithDecider.start();
			default:
				break;
			}
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public Type getType() {
		return Type.DWC;
	}

}
