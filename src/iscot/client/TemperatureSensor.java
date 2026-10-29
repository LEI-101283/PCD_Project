package iscot.client;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import iscot.client.gui.LightIcon;
import iscot.client.gui.SensorControl;
import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.message.RegistrationMessage;
import iscot.message.sensor.LightSensorMessage;
import iscot.message.sensor.TemperatureSensorMessage;

public class TemperatureSensor {

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

			JFrame janela = new JFrame("Temperatura em "+area);
			janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			
			// Criar os componentes gráficos
			JPanel painel=new JPanel();
			painel.setLayout(new BorderLayout());
			SensorControl sensorControl= new SensorControl(
				    "🌡", "Temperatura da sala", "°C",
				    -10, 50, 0.5, 22.5,
				    valor -> {
				        // TODO enviar mensagem
				        System.out.println("Nova temperatura: " + valor);
				    }
				);
			painel.add(sensorControl,BorderLayout.CENTER);
			janela.setContentPane(painel);
			janela.setLocation(600, 400);
			janela.setMinimumSize(janela.getSize());
			janela.pack();
			janela.setVisible(true);
			
			// TODO
			// Send message with initial value
			out.writeObject(new LightSensorMessage(area, (float)sensorControl.getValor()));
		}
	}

}
