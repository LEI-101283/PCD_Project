package iscot.events;

import iscot.enums.Areas;

public class AreaIrrigationOrderEvent extends HighLevelOrderEvent {
    private final boolean on;

    public AreaIrrigationOrderEvent(Areas area, boolean on) {
        super(area);
        this.on = on;
    }

    public boolean isOn() { return on; }
}
