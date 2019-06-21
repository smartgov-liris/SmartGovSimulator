package smartgov.urban.geo.agent.mover;

import smartgov.urban.geo.agent.GeoAgentBody;
import smartgov.urban.geo.environment.graph.GeoArc;

public class TestGeoMover extends BasicGeoMover {

	@Override
	protected void handleArcChanged(GeoArc oldArc, GeoArc newArc) {
		// Nothing special to do in our case, just follow the next arc
		
	}

	@Override
	protected void updateAgentSpeed(GeoAgentBody agentBody) {
		// For now, let's keep the speed constant
		
	}

}
