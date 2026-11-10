package iscot.events;

import iscot.enums.Areas;

public class HumiditySensorEvent extends SensorEvent {
    public HumiditySensorEvent(Areas area, double value) {
        super(area, value);
    }
}
