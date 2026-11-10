package iscot.client;

import iscot.client.gui.ActuatorPanel;
import iscot.client.gui.LightIcon;
import iscot.enums.Areas;
import iscot.events.AreaLightingOrderEvent;

public class LightActuator {

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

		LightIcon luz = new LightIcon();
		ActuatorPanel painel = new ActuatorPanel(luz);
		ClientWindow.show("Luz em " + area, painel);

		// Fase 3: ordens simuladas localmente
		new Thread(() -> {
			try {
				System.out.println("À espera de uma ordem...");
				Thread.sleep(3000);
				aplicar(new AreaLightingOrderEvent(area, true), luz, painel);
				Thread.sleep(3000);
				aplicar(new AreaLightingOrderEvent(area, false), luz, painel);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}).start();
	}

	private static void aplicar(AreaLightingOrderEvent ordem, LightIcon luz, ActuatorPanel painel) {
		System.out.println("Ordem recebida: " + ordem);
		luz.setLigada(ordem.isTurnOnLights());
		painel.registar(ordem.toString());
	}
}
