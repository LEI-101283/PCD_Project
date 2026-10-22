package iscot.message.highlevelorder;

import iscot.enums.Areas;

public class AreaLightingOrderMessage extends HighLevelOrderMessage {
	public final boolean state;

	public AreaLightingOrderMessage(Areas area, boolean state) {
		super(area);
		this.state = state;
	}
}
