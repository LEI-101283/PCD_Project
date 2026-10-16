package iscot.message.sensor;

public class TemperatureSensorMessage extends SensorMessage {
	public final float temperature; // always celsius

	public TemperatureSensorMessage(String area, float temperature) {
		super(area);
		this.temperature = temperature;
	}
}
