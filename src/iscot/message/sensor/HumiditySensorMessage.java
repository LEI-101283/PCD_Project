package iscot.message.sensor;

import iscot.enums.Areas;

public class HumiditySensorMessage extends SensorMessage{
	public final float humidity; // always in %

	public HumiditySensorMessage(Areas area, float humidity) {
		super(area);
		this.humidity = humidity;
	}

	@Override
	public String toString() {
		return "HumiditySensorMessage [light=" + humidity + ", area=" + area + ", getType()=" + getType() + "]";
	}

}
