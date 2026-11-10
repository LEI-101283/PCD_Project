package iscot.events;

import iscot.enums.Areas;

public class AreaLightingOrderEvent extends HighLevelOrderEvent {
    private final boolean turnOnLights;
    public AreaLightingOrderEvent(Areas area, boolean turnOnLights) {
        super(area);
        this.turnOnLights = turnOnLights;
    }
    public boolean isTurnOnLights() { return turnOnLights; }
    
    @Override
    public String toString(){
    	if(isTurnOnLights())
    	{
    		return ("Luz ligada em " + super.getArea()) + " - " + super.getTimestamp();
    	}
    	
    	return "Luz desligada em " + super.getArea() + " - " + super.getTimestamp();
    }
}
