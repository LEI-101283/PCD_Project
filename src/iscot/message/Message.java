package iscot.message;

import java.awt.geom.Area;
import java.io.Serializable;

import iscot.enums.Areas;
import iscot.enums.Type;

public abstract class Message implements Serializable {
	@Override
	public String toString() {
		return "Message [area=" + area + " type:"+ getType()+"]";
	}

	public final Areas area;
	
	public abstract Type getType();

	public Message(String area) {
		this.area = Areas.valueOf(area);
	}
}
