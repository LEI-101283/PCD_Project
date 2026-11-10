package iscot.events;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import iscot.enums.Areas;
import iscot.enums.Type;

public abstract class Event implements Serializable{
	
	private final Type type;
    private final Areas area;
    DateTimeFormatter formato = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final String timestamp = LocalTime.now().format(formato);

    protected Event(Type type, Areas area) {
        this.type = type;
        this.area = area;
    }

    public Type getType() { return type; }
    public Areas getArea() { return area; }
    public String getTimestamp() { return timestamp; }
    
    @Override
    public String toString() {
        return "Event{" +
               "type=" + getType() +
               ", area=" + getArea() +
               ", timestamp=" + getTimestamp() +
               '}';
    }
    
}
