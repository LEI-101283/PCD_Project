package iscot.message.sensor;

public class LightSensorMessage extends SensorMessage {
	public final float light; // always in lux

	public LightSensorMessage(String area, float light) {
		super(area);
		this.light = light;
	}

	@Override
	public String toString() {
		return "LightSensorMessage [light=" + light + ", " + super.toString() + "]";
	}
	
}
