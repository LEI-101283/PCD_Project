package iscot.events;

import iscot.enums.AirConditioningMode;
import iscot.enums.Areas;

public class AreaTemperatureOrderEvent extends HighLevelOrderEvent {
    private final double targetTemperature; // Em vez de boolean, podes enviar o alvo
    public AreaTemperatureOrderEvent(Areas area, double targetTemperature) {
        super(area);
        this.targetTemperature = targetTemperature;
    }
    public double getTargetTemperature() { return targetTemperature; }
}