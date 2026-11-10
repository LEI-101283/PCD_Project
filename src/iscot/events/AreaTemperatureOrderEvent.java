package iscot.events;

import iscot.enums.AirConditioningMode;
import iscot.enums.Areas;

public class AreaTemperatureOrderEvent extends HighLevelOrderEvent {
    private final AirConditioningMode mode;

    public AreaTemperatureOrderEvent(Areas area, AirConditioningMode mode) {
        super(area);
        this.mode = mode;
    }

    public AirConditioningMode getMode() { return mode; }
}
