package iscot.events;

import iscot.enums.Areas;

public class GroundMoistureSensorEvent extends SensorEvent {
    public GroundMoistureSensorEvent(Areas area, double value) { super(area, value); }
    @Override
    public String toString() {
        return super.toString() + " -> Ground: " + getValue() + "ºC";
    }
}
