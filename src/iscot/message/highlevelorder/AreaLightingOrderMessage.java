package iscot.message.highlevelorder;

public class AreaLightingOrderMessage extends HighLevelOrderMessage {
	public final boolean state;

	public AreaLightingOrderMessage(String area, boolean state) {
		super(area);
		this.state = state;
	}
}
