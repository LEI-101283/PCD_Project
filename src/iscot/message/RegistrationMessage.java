package iscot.message;

import iscot.enums.Type;

public class RegistrationMessage extends Message {
	public final Type type;
	public RegistrationMessage(String area, Type type) {
		super(area);
		this.type = type;
	}
	@Override
	public Type getType() {
		return type;
	}
	
}
