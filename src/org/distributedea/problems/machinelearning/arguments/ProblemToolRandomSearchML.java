package org.distributedea.problems.machinelearning.arguments;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.Agent_RandomSearch;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.problems.IProblemToolRandomSearch;
import org.distributedea.problems.machinelearning.AAProblemToolML;


public class ProblemToolRandomSearchML extends AAProblemToolML implements IProblemToolRandomSearch {

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
