package iscot.events;

import iscot.enums.Areas;

public class AreaLightingOrderMessage extends HighLevelOrderMessage {
    private final boolean on;

    public AreaLightingOrderMessage(Areas area, boolean on) {
        super(area);
        this.on = on;
    }
    public boolean isOn() { return on; }
}
