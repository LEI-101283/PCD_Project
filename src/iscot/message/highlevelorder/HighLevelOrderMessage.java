package iscot.message.highlevelorder;

import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.message.Message;

public abstract class HighLevelOrderMessage extends Message {

	public HighLevelOrderMessage(Areas area) {
		super(area);
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return Type.DECIDER;
	}

}
