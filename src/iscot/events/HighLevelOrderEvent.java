package iscot.events;
import iscot.enums.Areas;
import iscot.enums.Type;

public abstract class HighLevelOrderEvent extends Event {
    protected HighLevelOrderEvent(Areas area) {
        super(Type.DECIDER, area); // Força sempre o tópico DECIDER
    }
}