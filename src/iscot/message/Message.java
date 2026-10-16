package iscot.message;

import java.io.Serializable;

import iscot.enums.Type;

public abstract class Message implements Serializable {
	@Override
	public String toString() {
		return "Message [area=" + area + "]";
	}

	public final String area;
	
	public abstract Type getType();

	public Message(String area) {
		this.area = area;
	}
}
