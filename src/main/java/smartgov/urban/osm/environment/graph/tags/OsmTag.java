package smartgov.urban.osm.environment.graph.tags;

/**
 * Interface that enumerations corresponding to OSM tags
 * can implement.
 *
 */
public interface OsmTag {

	/**
	 * Osm representation of the key of the tag.
	 * 
	 * @return osm tag
	 */
	public String getOsmTag();
	
}
