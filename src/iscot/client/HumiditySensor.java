package iscot.client;

import iscot.client.gui.SensorControl;
import iscot.enums.Areas;
import iscot.events.HumiditySensorEvent;

public class HumiditySensor {

	/**
	 * @param args IP PORT Area
	 */
	public static void main(String[] args) {
		Areas area = ClientWindow.parseArea(args);
		if (area == null) {
			return;
		}

		// TODO connect to the server
		// TODO register with the server

		SensorControl sensorControl = new SensorControl("💧", "Humidade - " + area, "%",
				0, 100, 1, 50,
				valor -> {
					// Criar o evento desta leitura e mostrá-lo na consola (fase 3)
					HumiditySensorEvent event = new HumiditySensorEvent(area, valor);
					System.out.println("GERADO (Sensor Humidade): " + event);
					// TODO send the event to the server
				});

		ClientWindow.show("Humidade em " + area, sensorControl);
	}
}
