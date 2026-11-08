package iscot.events;

import iscot.enums.Areas;
import iscot.enums.Type;

public class RegistrationEvent extends Event {
    private final String deviceType; // validado pelo servidor mais tarde

    public RegistrationEvent(Type type, Areas area, String deviceType) {
        super(type, area);
        this.deviceType = deviceType;
    }
    public String getDeviceType() { return deviceType; }
}
