package iscot.events;

import iscot.enums.Areas;
import iscot.enums.Type;

//HighLevelOrderMessage.java
public abstract class HighLevelOrderMessage extends Event {
 protected HighLevelOrderMessage(Areas area) {
     super(Type.DECIDER, area);
 }
}