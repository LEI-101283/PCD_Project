package iscot.events;

import iscot.enums.AirConditioningMode;
import iscot.enums.Areas;

public class AreaTemperatureOrderEvent extends HighLevelOrderEvent {
    private final double targetTemperature; // Em vez de boolean, podes enviar o valor
    public AreaTemperatureOrderEvent(Areas area, double targetTemperature) {
        super(area);
        this.targetTemperature = targetTemperature;
    }
    public double getTargetTemperature() { return targetTemperature; }
    
    @Override
    public String toString(){
    	if(targetTemperature > 25)
    	{
    		return ("Ar condicionado no frio ligado em " + super.getArea());
    	}
    	
    	else if(targetTemperature < 20) {
    		return ("Ar condicionado no quente ligado em " + super.getArea());
    	}
    	
    	return "Ar condicionado desligado em " + super.getArea();
    }
}
