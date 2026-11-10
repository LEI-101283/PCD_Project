package iscot.events;

import iscot.enums.Areas;

public class AreaLightingOrderEvent extends HighLevelOrderEvent {
    private final boolean on;

    public AreaLightingOrderEvent(Areas area, boolean on) {
        super(area);
        this.on = on;
    }
    public boolean isOn() { return on; }
}
