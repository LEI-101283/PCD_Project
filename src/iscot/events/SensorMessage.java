package iscot.events;

import iscot.enums.Areas;
import iscot.enums.Type;

public abstract class SensorMessage extends Event {
    private final double value;

    protected SensorMessage(Areas area, double value) {
        super(Type.SENSOR, area);
        this.value = value;
    }
    
	public double getValue() { return value; }
}


