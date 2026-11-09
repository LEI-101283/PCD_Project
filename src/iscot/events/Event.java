package iscot.events;

import java.io.Serializable;
import java.time.LocalDateTime;

import iscot.enums.Areas;
import iscot.enums.Type;

public abstract class Event implements Serializable{
	
	private final Type type;
    private final Areas area;
    private final LocalDateTime timestamp = LocalDateTime.now();

    protected Event(Type type, Areas area) {
        this.type = type;
        this.area = area;
    }

    public Type getType() { return type; }
    public Areas getArea() { return area; }
    public LocalDateTime getTimestamp() { return timestamp; }
    
    @Override
    public String toString() {
        return "Event{" +
               "type=" + getType() +
               ", area=" + getArea() +
               ", timestamp=" + getTimestamp() +
               '}';
    }
    
}
