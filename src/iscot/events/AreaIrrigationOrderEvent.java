package iscot.events;

import iscot.enums.Areas;

public class AreaIrrigationOrderEvent extends HighLevelOrderEvent {
    private final boolean turnOnWater;
    public AreaIrrigationOrderEvent(Areas area, boolean turnOnWater) {
        super(area);
        this.turnOnWater = turnOnWater;
    }
    public boolean isTurnOnWater() { return turnOnWater; }
    
    @Override
    public String toString(){
    	if(isTurnOnWater())
    	{
    		return ("Irrigação ligada em " + super.getArea() + " - " + super.getTimestamp());
    	}
    	
    	return "Irrigação desligada em " + super.getArea() + " - " + super.getTimestamp();
    }
}
