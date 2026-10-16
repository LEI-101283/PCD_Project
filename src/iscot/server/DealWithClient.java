package iscot.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class DealWithClient extends Thread{
	private ObjectInputStream in;
	private ObjectOutputStream out;
	public DealWithClient(Socket accept) throws IOException {
		in=new ObjectInputStream(accept.getInputStream());
		out=new ObjectOutputStream(accept.getOutputStream());
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
	}

}
