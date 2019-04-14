package org.distributedea.tests.history;

import org.distributedea.agents.computingagents.Agent_HillClimbing;
import org.distributedea.agents.systemagents.centralmanager.structures.plan.InputRePlan;
import org.distributedea.ontology.agentconfiguration.AgentConfiguration;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescription;
import org.distributedea.ontology.methoddesriptionsplanned.MethodIDs;
import org.distributedea.ontology.problem.ProblemTSPGPS;
import org.distributedea.ontology.problemtooldefinition.ProblemToolDefinition;
import org.distributedea.problems.continuousoptimization.point.ProblemToolBruteForceCO;

public class TestInputRePlan {

	public static void main(String [] args) {
		
		Iteration iteration = new Iteration(5, 10);
		
		AgentConfiguration agentConf = new AgentConfiguration(
				Agent_HillClimbing.class.getSimpleName(), Agent_HillClimbing.class, new Arguments());
		
		ProblemTSPGPS problem = new ProblemTSPGPS();
		
		ProblemToolDefinition probToolDef =
				new ProblemToolDefinition(new ProblemToolBruteForceCO(0.005));
		MethodDescription agentToKill =
				new MethodDescription(agentConf, new MethodIDs(1), problem, probToolDef);
		
		InputMethodDescription agentToCreate = agentToKill.exportInputMethodDescription();
		
		
		InputRePlan inputRePlan = new InputRePlan(iteration,
				agentToKill, agentToCreate);
		
		@SuppressWarnings("unused")
		InputRePlan inputRePlanOpt = inputRePlan.exportOptimalizedInpuRePlan();
		
	}
}
