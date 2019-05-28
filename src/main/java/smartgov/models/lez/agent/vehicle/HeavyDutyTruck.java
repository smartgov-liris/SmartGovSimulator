package smartgov.models.lez.agent.vehicle;

import smartgov.models.lez.copert.Copert;
import smartgov.models.lez.copert.fields.EuroNorm;
import smartgov.models.lez.copert.fields.Fuel;
import smartgov.models.lez.copert.fields.HeavyDutyTrucksSegment;
import smartgov.models.lez.copert.fields.Technology;
import smartgov.models.lez.copert.fields.VehicleCategory;

public class HeavyDutyTruck extends DeliveryVehicle {

	public HeavyDutyTruck(Fuel fuel, HeavyDutyTrucksSegment vehicleSegment, EuroNorm euroNorm, Technology technology, Copert copert) {
		super(VehicleCategory.HEAVY_DUTY_TRUCK, fuel, vehicleSegment, euroNorm, technology, copert);
	}

}
