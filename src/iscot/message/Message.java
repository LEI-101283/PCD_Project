package iscot.message;

import java.io.Serializable;

public abstract class Message implements Serializable {
	public final String area;

	public Message(String area) {
		this.area = area;
	}
}
