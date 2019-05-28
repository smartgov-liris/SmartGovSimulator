package smartgov.models.lez.agent.vehicle;

import smartgov.models.lez.copert.Copert;
import smartgov.models.lez.copert.fields.EuroNorm;
import smartgov.models.lez.copert.fields.Fuel;
import smartgov.models.lez.copert.fields.LightWeightVehicleSegment;
import smartgov.models.lez.copert.fields.Technology;
import smartgov.models.lez.copert.fields.VehicleCategory;

public class LightCommercialVehicle extends DeliveryVehicle {

	public LightCommercialVehicle(Fuel fuel, LightWeightVehicleSegment vehicleSegment, EuroNorm euroNorm, Technology technology, Copert copert) {
		super(VehicleCategory.LIGHT_WEIGHT, fuel, vehicleSegment, euroNorm, technology, copert);
	}


}
