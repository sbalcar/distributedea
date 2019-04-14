package org.distributedea.problems.tsp.point.permutation;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.Agent_RandomSearch;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.problems.tsp.point.AProblemToolRandomSearchTSPPoint;

public class ProblemToolRandomSearchTSPPoint extends AProblemToolRandomSearchTSPPoint {

	@Override
	public List<Class<?>> belongsToAgent() {
		
		List<Class<?>> agents = new ArrayList<>();
		agents.add(Agent_RandomSearch.class);
		return agents;
	}

	@Override
	public Arguments exportArguments() {
		return new Arguments();
	}

	@Override
	public void importArguments(Arguments arguments) {
	}
	
}
