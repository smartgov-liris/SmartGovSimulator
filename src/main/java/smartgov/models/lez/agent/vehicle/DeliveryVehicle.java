package smartgov.models.lez.agent.vehicle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import smartgov.models.lez.copert.Copert;
import smartgov.models.lez.copert.CopertParameters;
import smartgov.models.lez.copert.fields.EuroNorm;
import smartgov.models.lez.copert.fields.Fuel;
import smartgov.models.lez.copert.fields.Pollutant;
import smartgov.models.lez.copert.fields.Technology;
import smartgov.models.lez.copert.fields.VehicleCategory;
import smartgov.models.lez.copert.fields.VehicleSegment;

/**
 * A vehicle, that can be associated to an OsmAgentBody.
 * @author pbreugnot
 *
 */
@JsonIgnoreProperties({"copert", "emissions"})
public class DeliveryVehicle {

	private VehicleCategory category;
	private Fuel fuel;
	private VehicleSegment vehicleSegment;
	private EuroNorm euroNorm;
	private Technology technology;
	private Copert copert;
	
	public DeliveryVehicle(VehicleCategory category, Fuel fuel, VehicleSegment vehicleSegment,  EuroNorm euroNorm, Technology technology, Copert copert) {
		this.category = category;
		this.fuel = fuel;
		this.vehicleSegment = vehicleSegment;
		this.technology = technology;
		this.euroNorm = euroNorm;
		this.copert = copert;
	}
	
	public VehicleCategory getCategory() {
		return category;
	}
	
	public Fuel getFuel() {
		return fuel;
	}
	
	public VehicleSegment getVehicleSegment() {
		return vehicleSegment;
	}

	public EuroNorm getEuroNorm() {
		return euroNorm;
	}
	
	public Technology getTechnology() {
		return technology;
	}

	public Copert getCopert() {
		return copert;
	}



	/**
	 * Compute emissions in g according to the COPERT model.
	 * 
	 * @param meanSpeed Mean speed of the vehicle.
	 * @param distance Traveled distance.
	 * @return
	 */
	public double getEmissions(Pollutant pollutant, double meanSpeed, double distance) {
		CopertParameters copertParameters = copert.getCopertParameters(pollutant);
		if (copertParameters != null) {
			return copertParameters.emissions(meanSpeed) * distance;
		}
		return 0;
	}
}
