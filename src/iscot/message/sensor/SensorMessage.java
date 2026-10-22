package iscot.message.sensor;

import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.message.Message;

public abstract class SensorMessage extends Message {

	@Override
	public Type getType() {
		return Type.SENSOR;
	}

	public SensorMessage(Areas area) {
		super(area);
	}

}
