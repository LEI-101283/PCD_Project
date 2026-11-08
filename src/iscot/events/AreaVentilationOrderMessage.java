package iscot.events;

import iscot.enums.Areas;

public class AreaVentilationOrderMessage extends HighLevelOrderMessage {
    private final boolean on;

    public AreaVentilationOrderMessage(Areas area, boolean on) {
        super(area);
        this.on = on;
    }

    public boolean isOn() { return on; }
}
