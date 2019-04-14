package org.distributedea.problems.continuousoptimization;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.Agent_Evolution;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.problems.IProblemToolEvolution;

public abstract class AProblemToolEvolutionCO extends AAProblemToolCO implements IProblemToolEvolution {

	@Override
	public List<Class<?>> belongsToAgent() {
		
		List<Class<?>> agents = new ArrayList<>();
		agents.add(Agent_Evolution.class);
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
