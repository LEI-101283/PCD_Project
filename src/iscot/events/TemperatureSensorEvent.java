package iscot.events;

import iscot.enums.Areas;

public class TemperatureSensorEvent extends SensorEvent {
	
    public TemperatureSensorEvent(Areas area, double value) {
        super(area, value);
    }
    
}
