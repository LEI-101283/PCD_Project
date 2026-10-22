package iscot.message.sensor;

import iscot.enums.Areas;

public class LightSensorMessage extends SensorMessage {
	public final float light; // always in lux

	public LightSensorMessage(Areas area, float light) {
		super(area);
		this.light = light;
	}

	@Override
	public String toString() {
		return "LightSensorMessage [light=" + light + ", " + super.toString() + "]";
	}
	
}
