package iscot.events;

import iscot.enums.Areas;

public class HumiditySensorMessage extends SensorMessage {
    public HumiditySensorMessage(Areas area, double value) {
        super(area, value);
    }
}
