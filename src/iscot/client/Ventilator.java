package iscot.client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import iscot.client.gui.VentilatorIcon;
import iscot.enums.Areas;
import iscot.events.AreaVentilationOrderEvent;

public class Ventilator {

	/**
	 * 
	 * @param server    address and port
	 * @param roomName: string
	 * @throws IOException
	 * @throws UnknownHostException
	 * @throws NumberFormatException
	 * @throws InterruptedException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws NumberFormatException, UnknownHostException, IOException,
			InterruptedException, ClassNotFoundException {
		if (args.length != 3) {
			System.err.println("Invalid arguments!");
		} else {
			// TODO connect to server

			// TODO register with the server

			// TODO: for first phase: create an appropriate event hardcoded locally and
			// change the GUI accordingly
			
			// TODO: receive and handle messages from server
			
			// TODO: for first phase: create an appropriate event hardcoded locally and
			// change the GUI accordingly

			Areas area = Areas.valueOf(args[2]);

			JFrame janela = new JFrame("Ventilador em " + area);
			janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			// Criar os componentes gráficos
			JPanel painel = new JPanel();
			painel.setLayout(new BorderLayout());
			VentilatorIcon ventilator = new VentilatorIcon();
			painel.add(ventilator, BorderLayout.CENTER);
			janela.setContentPane(painel);
			janela.setLocation(600, 400);
			janela.setMinimumSize(janela.getSize());
			janela.pack();
			janela.setVisible(true);

			// 1. Vamos criar uma Thread temporária só para simular um atraso na rede
			new Thread(() -> {
				try {
					System.out.println("À espera de uma ordem...");
					Thread.sleep(3000); // Espera 3 segundos para veres a janela fechada primeiro

					// 2. Criar o evento simulado ("hardcoded") da ordem do Decisor
					AreaVentilationOrderEvent ordemLigar = new AreaVentilationOrderEvent(area, true);
					System.out.println("Ordem recebida: " + ordemLigar);

					// 3. Alterar a interface gráfica com base no evento
					ventilator.setAtivo(ordemLigar.isOn());

					// Vamos testar desligar passado uns segundos
					Thread.sleep(3000);
					AreaVentilationOrderEvent ordemDesligar = new AreaVentilationOrderEvent(area, false);
					System.out.println("Ordem recebida: " + ordemDesligar);
					ventilator.setAtivo(ordemDesligar.isOn());

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}).start();

			

			
		}
	}
}