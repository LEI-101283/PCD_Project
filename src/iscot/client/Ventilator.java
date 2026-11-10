package iscot.client;

import iscot.client.gui.ActuatorPanel;
import iscot.client.gui.VentilatorIcon;
import iscot.enums.Areas;
import iscot.events.AreaVentilationOrderEvent;

public class Ventilator {

	/**
	 * @param args IP PORT Area
	 */
	public static void main(String[] args) {
		Areas area = ClientWindow.parseArea(args);
		if (area == null) {
			return;
		}

		// TODO connect to server
		// TODO register with the server
		// TODO receive and handle events from server

		VentilatorIcon ventilador = new VentilatorIcon();
		ActuatorPanel painel = new ActuatorPanel(ventilador);
		ClientWindow.show("Ventilador em " + area, painel);

		// Fase 3: ordens simuladas localmente
		new Thread(() -> {
			try {
				System.out.println("À espera de uma ordem...");
				Thread.sleep(3000);
				aplicar(new AreaVentilationOrderEvent(area, true), ventilador, painel);
				Thread.sleep(5000);
				aplicar(new AreaVentilationOrderEvent(area, false), ventilador, painel);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}).start();
	}

	private static void aplicar(AreaVentilationOrderEvent ordem, VentilatorIcon ventilador, ActuatorPanel painel) {
		System.out.println("Ordem recebida: " + ordem);
		ventilador.setLigado(ordem.isOn()); // setLigado arranca/pára a rotação das pás
		painel.registar(ordem.toString());
	}
}
