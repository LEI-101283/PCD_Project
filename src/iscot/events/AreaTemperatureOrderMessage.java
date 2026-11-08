package iscot.events;

import iscot.enums.AirConditioningMode;
import iscot.enums.Areas;

public class AreaTemperatureOrderMessage extends HighLevelOrderMessage {
    private final AirConditioningMode mode;

    public AreaTemperatureOrderMessage(Areas area, AirConditioningMode mode) {
        super(area);
        this.mode = mode;
    }

    public AirConditioningMode getMode() { return mode; }
}
