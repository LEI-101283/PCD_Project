package iscot.events;

import iscot.enums.Areas;

public class AreaVentilationOrderEvent extends HighLevelOrderEvent {
    private final boolean on;
    public AreaVentilationOrderEvent(Areas area, boolean on) {
        super(area);
        this.on = on;
    }
    public boolean isOn() { return on; }
    
    @Override
    public String toString(){
    	if(isOn())
    	{
    		return ("Ventilador ligado em " + super.getArea());
    	}
    	
    	return "Ventilador desligado em " + super.getArea();
    }
}
