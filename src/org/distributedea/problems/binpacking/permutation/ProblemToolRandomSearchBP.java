package org.distributedea.problems.binpacking.permutation;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.Agent_RandomSearch;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.problems.binpacking.AProblemToolRandomSearchBP;

public class ProblemToolRandomSearchBP extends AProblemToolRandomSearchBP {

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
