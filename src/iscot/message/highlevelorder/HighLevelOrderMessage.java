package iscot.message.highlevelorder;

import iscot.enums.Type;
import iscot.message.Message;

public abstract class HighLevelOrderMessage extends Message {

	public HighLevelOrderMessage(String area) {
		super(area);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return Type.DECIDER;
	}

}
