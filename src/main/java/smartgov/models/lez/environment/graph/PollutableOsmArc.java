package smartgov.models.lez.environment.graph;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import smartgov.models.lez.copert.fields.Pollutant;
import smartgov.models.lez.environment.pollution.Pollution;
import smartgov.models.lez.output.PollutionSerializer;
import smartgov.urban.osm.environment.graph.OsmArc;
import smartgov.urban.osm.environment.graph.OsmNode;
import smartgov.urban.osm.environment.graph.Road;

/**
 * An OsmArc that can be polluted with some particles.
 * 
 * @author pbreugnot
 *
 */
public class PollutableOsmArc extends OsmArc {
	
	@JsonSerialize(using=PollutionSerializer.class)
	private Pollution pollution;

	public PollutableOsmArc(
			String id,
			Road road,
			OsmNode startNode,
			OsmNode targetNode,
			int lanes,
			String type) {
		super(id, road, startNode, targetNode, lanes, type);
		pollution = new Pollution();
	}
	
	public void increasePollution(Pollutant pollutant, double increment) {
		pollution.get(pollutant).increasePollution(increment);
	}
	
	public Pollution getPollution() {
		return pollution;
	}
}
