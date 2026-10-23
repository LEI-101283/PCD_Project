package iscot.client;

import java.awt.BorderLayout;
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
import iscot.client.gui.IconeVentilador;
import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.message.Message;
import iscot.message.RegistrationMessage;
import iscot.message.highlevelorder.AreaLightingOrderMessage;
import iscot.message.sensor.LightSensorMessage;

public class Ventilator {

	/**
	 * 
	 * @param server address and port
	 * @param roomName: string
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws NumberFormatException 
	 * @throws InterruptedException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws NumberFormatException, UnknownHostException, IOException, InterruptedException, ClassNotFoundException {
		if(args.length!=3) {
			System.err.println("Invalid arguments!");
		}
		else {
			Socket socket=new Socket(args[0],Integer.parseInt(args[1]));
			ObjectOutputStream out= new ObjectOutputStream(socket.getOutputStream()); // useless, no outgoing messages
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

			// register with the server
			Areas area=Areas.valueOf(args[2]);
			out.writeObject(new RegistrationMessage(area, Type.ACTUATOR));

			JFrame janela = new JFrame("Ventilador em "+area);
			janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			
			// Criar os componentes gráficos
			JPanel painel=new JPanel();
			painel.setLayout(new BorderLayout());
			IconeVentilador luz = new IconeVentilador();
			painel.add(luz,BorderLayout.CENTER);
			janela.setContentPane(painel);
			janela.setLocation(600, 400);
			janela.setMinimumSize(janela.getSize());
			janela.pack();
			janela.setVisible(true);

			while(true) {
				Message message=(Message) in.readObject();
				// TODO deal with appropriate messages
				if( message instanceof AreaLightingOrderMessage) {
					AreaLightingOrderMessage areaLightingOrderMessage=(AreaLightingOrderMessage)message;
					if(areaLightingOrderMessage.area == area) {
						luz.setAtivo(areaLightingOrderMessage.state);
						System.err.println("Altering light  at:"+area+" to;"+areaLightingOrderMessage.state);
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
