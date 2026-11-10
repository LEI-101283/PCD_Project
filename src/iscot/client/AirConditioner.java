package iscot.client;

import iscot.client.gui.ACIcon;
import iscot.client.gui.ActuatorPanel;
import iscot.enums.AirConditioningMode;
import iscot.enums.Areas;
import iscot.events.AreaTemperatureOrderEvent;

public class AirConditioner {

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

		ACIcon ar = new ACIcon();
		ActuatorPanel painel = new ActuatorPanel(ar);
		ClientWindow.show("Ar condicionado em " + area, painel);

		// Fase 3: ordens simuladas localmente
		new Thread(() -> {
			try {
				System.out.println("À espera de uma ordem...");
				Thread.sleep(3000);
				aplicar(new AreaTemperatureOrderEvent(area, AirConditioningMode.Aquecimento, 22), ar, painel);
				Thread.sleep(3000);
				aplicar(new AreaTemperatureOrderEvent(area, AirConditioningMode.Frio, 18), ar, painel);
				Thread.sleep(3000);
				aplicar(new AreaTemperatureOrderEvent(area, AirConditioningMode.Desligado, 0), ar, painel);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}).start();
	}

	private static void aplicar(AreaTemperatureOrderEvent ordem, ACIcon ar, ActuatorPanel painel) {
		System.out.println("Ordem recebida: " + ordem);
		if (ordem.getMode() != AirConditioningMode.Desligado) {
			ar.setTemperatura(ordem.getTargetTemperature()); // o ACIcon só aceita 16 a 30 ºC
		}
		ar.setModo(ordem.getMode());
		painel.registar(ordem.toString());
	}
}
