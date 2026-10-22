package iscot.message.highlevelorder;

import iscot.enums.Areas;

public class AreaLightingOrderMessage extends HighLevelOrderMessage {
	@Override
	public String toString() {
		return "AreaLightingOrderMessage [state=" + state + ", area=" + area + ", getType()=" + getType() + "]";
	}

	public final boolean state;

	public AreaLightingOrderMessage(Areas area, boolean state) {
		super(area);
		this.state = state;
	}
}
