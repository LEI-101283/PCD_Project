package iscot.events;
import iscot.enums.Areas;
import iscot.enums.Type;

public class RegistrationEvent extends Event {
    // Aqui não precisas do deviceId porque o servidor é que o vai gerar
    public RegistrationEvent(Type type, Areas area) {
        super(type, area);
    }
}