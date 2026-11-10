package iscot.events;

import iscot.enums.Areas;

public class LightSensorEvent extends SensorEvent {
    public LightSensorEvent(Areas area, double value) {
        super(area, value);
    }
}
