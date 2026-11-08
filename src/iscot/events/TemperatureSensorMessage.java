package iscot.events;

import iscot.enums.Areas;

public class TemperatureSensorMessage extends SensorMessage {
	
    public TemperatureSensorMessage(Areas area, double value) {
        super(area, value);
    }
    
}
