package iscot.message;

import iscot.enums.Type;

public class RegistrationMessage extends Message {
	public final Type type;
	public RegistrationMessage(String area, Type type, int id) {
		super(area);
		this.type = type;
		this.id = id;
	}
	public final int id;
	@Override
	public Type getType() {
		return type;
	}
	
}
