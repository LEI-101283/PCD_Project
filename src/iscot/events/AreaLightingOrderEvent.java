package iscot.events;

import iscot.enums.Areas;

public class AreaLightingOrderEvent extends HighLevelOrderEvent {
    private final boolean turnOnLights;
    public AreaLightingOrderEvent(Areas area, boolean turnOnLights) {
        super(area);
        this.turnOnLights = turnOnLights;
    }
    public boolean isTurnOnLights() { return turnOnLights; }
}
