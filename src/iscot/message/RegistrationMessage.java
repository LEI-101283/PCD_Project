package iscot.message;

import iscot.enums.Areas;
import iscot.enums.Type;

public class RegistrationMessage extends Message {
	public final Type type;
	public RegistrationMessage(Areas area, Type type) {
		super(area);
		this.type = type;
	}
	@Override
	public Type getType() {
		return type;
	}
	
}
