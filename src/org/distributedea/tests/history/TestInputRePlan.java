package org.distributedea.tests.history;

import org.distributedea.agents.computingagents.Agent_HillClimbing;
import org.distributedea.agents.systemagents.centralmanager.structures.plan.InputRePlan;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.configuration.Arguments;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescription;
import org.distributedea.problems.tsp.gps.permutation.ProblemToolGPSEuc2D2opt;

public class TestInputRePlan {

	public static void main(String [] args) {
		
		Iteration iteration = new Iteration(5, 10);
		
		AgentConfiguration agentConfiguration = new AgentConfiguration(
				Agent_HillClimbing.class.getSimpleName(), Agent_HillClimbing.class, new Arguments());
		
		MethodDescription agentToKill = new MethodDescription(agentConfiguration, ProblemToolGPSEuc2D2opt.class);
		
		InputMethodDescription agentToCreate = agentToKill.exportInputAgentDescription();
		
		
		InputRePlan inputRePlan = new InputRePlan(iteration,
				agentToKill, agentToCreate);
		
		@SuppressWarnings("unused")
		InputRePlan inputRePlanOpt = inputRePlan.exportOptimalizedInpuRePlan();
		
	}
}
