package iscot.events;

import iscot.enums.AirConditioningMode;
import iscot.enums.Areas;

public class AreaTemperatureOrderEvent extends HighLevelOrderEvent {
    private final AirConditioningMode mode;
    private final double targetTemperature;

    public AreaTemperatureOrderEvent(Areas area, AirConditioningMode mode, double targetTemperature) {
        super(area);
        this.mode = mode;
        this.targetTemperature = targetTemperature;
    }

    public AirConditioningMode getMode() { return mode; }
    public double getTargetTemperature() { return targetTemperature; }

    @Override
    public String toString() {
        switch (mode) {
        case Frio:
            return "Ar condicionado no frio ligado em " + getArea() + " - " + super.getTimestamp();
        case Aquecimento:
            return "Ar condicionado no quente ligado em " + getArea() + " - " + super.getTimestamp();
        case Ventilação:
            return "Ar condicionado em ventilação em " + getArea() + " - " + super.getTimestamp();
        default:
            return "Ar condicionado desligado em " + getArea() + " - " + super.getTimestamp();
        }
    }
}
