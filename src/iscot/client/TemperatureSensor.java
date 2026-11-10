package iscot.client;

import iscot.client.gui.SensorControl;
import iscot.enums.Areas;
import iscot.events.TemperatureSensorEvent;

public class TemperatureSensor {

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

		SensorControl sensorControl = new SensorControl("🌡", "Temperatura - " + area, "ºC",
				0, 40, 1, 20,
				valor -> {
					// Criar o evento desta leitura e mostrá-lo na consola (fase 3)
					TemperatureSensorEvent event = new TemperatureSensorEvent(area, valor);
					System.out.println("GERADO (Sensor Temperatura): " + event);
					// TODO send the event to the server
				});

		ClientWindow.show("Temperatura em " + area, sensorControl);
	}
}
