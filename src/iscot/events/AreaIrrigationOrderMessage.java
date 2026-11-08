package iscot.events;

import iscot.enums.Areas;

public class AreaIrrigationOrderMessage extends HighLevelOrderMessage {
    private final boolean on;

    public AreaIrrigationOrderMessage(Areas area, boolean on) {
        super(area);
        this.on = on;
    }

    public boolean isOn() { return on; }
}
