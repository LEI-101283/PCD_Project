package iscot.message.sensor;

import iscot.enums.Areas;

public class TemperatureSensorMessage extends SensorMessage {
	public final float temperature; // always celsius

	public TemperatureSensorMessage(Areas area, float temperature) {
		super(area);
		this.temperature = temperature;
	}
}
