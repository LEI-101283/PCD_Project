package iscot.events;

import iscot.enums.Areas;

public class AreaIrrigationOrderEvent extends HighLevelOrderEvent {
    private final boolean turnOnWater;
    public AreaIrrigationOrderEvent(Areas area, boolean turnOnWater) {
        super(area);
        this.turnOnWater = turnOnWater;
    }
    public boolean isTurnOnWater() { return turnOnWater; }
}
