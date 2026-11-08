package iscot.events;

import iscot.enums.Areas;

public class LightSensorMessage extends SensorMessage {
    public LightSensorMessage(Areas area, double value) {
        super(area, value);
    }
}
