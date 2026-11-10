package iscot.events;

import iscot.enums.Areas;
import iscot.enums.Type;

//HighLevelOrderMessage.java
public abstract class HighLevelOrderEvent extends Event {
 protected HighLevelOrderEvent(Areas area) {
     super(Type.DECIDER, area);
 }
}