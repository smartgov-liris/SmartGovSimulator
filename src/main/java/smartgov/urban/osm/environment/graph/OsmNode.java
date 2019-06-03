package smartgov.urban.osm.environment.graph;

import org.locationtech.jts.geom.Coordinate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import smartgov.urban.geo.environment.graph.GeoNode;

/**
 * Class used to represents OSM nodes, i.e. crossroads and line breaks.
 * 
 * @see GeoNode
 * @see OsmArc
 * 
 * @author pbreugnot
 *
 */
public class OsmNode extends GeoNode {

	@JsonIgnore
	private boolean needToSlow = true;
	
	public OsmNode(String id, Coordinate coordinate) {
		super(id, coordinate);
	}
	
	public boolean isNeedToSlow() {
		return needToSlow;
	}
	
	public void setNeedToSlow(boolean needToSlow) {
		this.needToSlow = needToSlow;
	}

}
