package iscot.events;

import iscot.enums.Areas;

public class TemperatureSensorEvent extends SensorEvent {
    public TemperatureSensorEvent(Areas area, double value) { super(area, value); }
    @Override
    public String toString() {
        return super.toString() + " -> Temp: " + getValue() + "ºC";
    }
}
