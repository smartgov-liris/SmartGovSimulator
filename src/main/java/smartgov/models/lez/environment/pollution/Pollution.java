package smartgov.models.lez.environment.pollution;

import java.util.HashMap;

import smartgov.models.lez.copert.fields.Pollutant;

public class Pollution extends HashMap<Pollutant, PollutionRate>{

	private static final long serialVersionUID = 1L;
	
	public static Pollution pollutionRatePeeks = new Pollution();

	public Pollution() {
		for(Pollutant pollutant : Pollutant.values()) {
			put(pollutant, new PollutionRate(pollutant));
		}
	}
}
