package iscot.client;

import iscot.client.gui.ActuatorPanel;
import iscot.client.gui.IrrigationIcon;
import iscot.enums.Areas;
import iscot.events.AreaIrrigationOrderEvent;

public class IrrigationActuator {

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

		IrrigationIcon rega = new IrrigationIcon();
		ActuatorPanel painel = new ActuatorPanel(rega);
		ClientWindow.show("Rega em " + area, painel);

		// Fase 3: ordens simuladas localmente
		new Thread(() -> {
			try {
				System.out.println("À espera de uma ordem...");
				Thread.sleep(3000);
				aplicar(new AreaIrrigationOrderEvent(area, true), rega, painel);
				Thread.sleep(3000);
				aplicar(new AreaIrrigationOrderEvent(area, false), rega, painel);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}).start();
	}

	private static void aplicar(AreaIrrigationOrderEvent ordem, IrrigationIcon rega, ActuatorPanel painel) {
		System.out.println("Ordem recebida: " + ordem);
		rega.setAtiva(ordem.isTurnOnWater());
		painel.registar(ordem.toString());
	}
}
