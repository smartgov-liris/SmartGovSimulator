package smartgov.models.lez.environment.pollution;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import smartgov.SmartGov;
import smartgov.models.lez.copert.fields.Pollutant;
import smartgov.models.lez.output.PollutionRateSerializer;

@JsonSerialize(using = PollutionRateSerializer.class)
public class PollutionRate {

	private Pollutant pollutant;
	private double pollution = 0; // Pollution in g
	
	public PollutionRate(Pollutant pollutant) {
		this.pollutant = pollutant;
	}
	
	public void increasePollution(double pollution) {
		this.pollution += pollution;
		// TODO : improve performances
		if (Pollution.pollutionRatePeeks.get(pollutant) != this) {
			if(getValue() > Pollution.pollutionRatePeeks.get(pollutant).getValue()) {
				Pollution.pollutionRatePeeks.put(pollutant, this);
			}
		}
	}
	
	public double getAbsValue() {
		return pollution;
	}
	
	public double getValue() {
		return pollution / (SmartGov.getRuntime().getTickCount() * SmartGov.getRuntime().getTickDuration());
	}
}
