package iscot.events;

import iscot.enums.Areas;

public class LightSensorEvent extends SensorEvent {
    public LightSensorEvent(Areas area, double value) { super(area, value); }
    @Override
    public String toString() {
        return super.toString() + " -> Luz: " + getValue() + "ºC";
    }
}
