package iscot.message.highlevelorder;

import iscot.enums.Areas;

public class AreaVentilationOrderMessage extends HighLevelOrderMessage{
	@Override
	public String toString() {
		return "AreaVentilationOrderMessage [state=" + state + ", area=" + area + ", getType()=" + getType() + "]";
	}

	public final boolean state;

	public AreaVentilationOrderMessage(Areas area, boolean state) {
		super(area);
		this.state = state;
	}
}
