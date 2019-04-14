package org.distributedea.problems.evcharging.point;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.Agent_RandomSearch;
import org.distributedea.problems.evcharging.AProblemToolRandomSearchEVCharging;

public class ProblemToolRandomSearchEVCharging extends AProblemToolRandomSearchEVCharging {
	
	@Deprecated
	public ProblemToolRandomSearchEVCharging() {
		super();
	}

	/**
	 * Constructor
	 * @param normalDistMultiplicator
	 */
	public ProblemToolRandomSearchEVCharging(double normalDistMultiplicator) {
		super(normalDistMultiplicator);
	}

	@Override
	public List<Class<?>> belongsToAgent() {
		
		List<Class<?>> agents = new ArrayList<>();
		agents.add(Agent_RandomSearch.class);
		return agents;
	}	
}
