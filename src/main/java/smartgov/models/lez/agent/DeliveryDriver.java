package smartgov.models.lez.agent;

import smartgov.models.lez.agent.actuator.PollutantCarMover;
import smartgov.models.lez.agent.vehicle.DeliveryVehicle;
import smartgov.urban.osm.agent.OsmAgentBody;
import smartgov.urban.osm.environment.OsmContext;

public class DeliveryDriver extends OsmAgentBody {
	
	private DeliveryVehicle vehicle;

	public DeliveryDriver(
			String id,
			DeliveryVehicle vehicle,
			OsmContext environment) {
		super(id, new PollutantCarMover(), environment);
		this.vehicle = vehicle;
	}
	
	public DeliveryVehicle getVehicle() {
		return vehicle;
	}

}
