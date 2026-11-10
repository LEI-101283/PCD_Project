package iscot.client;

import iscot.client.gui.SensorControl;
import iscot.enums.Areas;
import iscot.events.LightSensorEvent;

public class LightSensor {

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

		SensorControl sensorControl = new SensorControl("☀", "Luminosidade - " + area, "%",
				0, 100, 1, 50,
				valor -> {
					// Criar o evento desta leitura e mostrá-lo na consola (fase 3)
					LightSensorEvent event = new LightSensorEvent(area, valor);
					System.out.println("GERADO (Sensor Luz): " + event);
					// TODO send the event to the server
				});

		ClientWindow.show("Luminosidade em " + area, sensorControl);
	}
}
