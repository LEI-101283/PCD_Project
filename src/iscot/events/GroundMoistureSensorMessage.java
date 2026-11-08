package iscot.events;

import iscot.enums.Areas;

public class GroundMoistureSensorMessage extends SensorMessage {
    public GroundMoistureSensorMessage(Areas area, double value) {
        super(area, value);
    }
}
