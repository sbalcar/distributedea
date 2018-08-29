package org.distributedea.agents.systemagents.centralmanager.planners.dumy;

import jade.core.AID;

import org.distributedea.agents.computingagents.universal.Agent_ComputingAgent;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.Agent_ManagerAgent;
import org.distributedea.agents.systemagents.centralmanager.planners.IPlanner;
import org.distributedea.agents.systemagents.centralmanager.structures.history.History;
import org.distributedea.javaextension.Pair;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.configurationinput.InputAgentConfiguration;
import org.distributedea.ontology.islandmodel.IslandModelConfiguration;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescription;
import org.distributedea.ontology.plan.Plan;
import org.distributedea.ontology.plan.RePlan;
import org.distributedea.ontology.problemtooldefinition.ProblemToolDefinition;
import org.distributedea.ontology.problemwrapper.ProblemWrapper;
import org.distributedea.services.ComputingAgentService;
import org.distributedea.services.ManagerAgentService;

/**
 * Dummy Scheduler which runs on one node
 * one Computing Agent with one ProblemTool
 *
 */
public class PlannerDummy implements IPlanner {
	
	int NODE_INDEX = 0;
	
	/** index on the method in the list of methods */
	int COMPUTING_METHOD_INDEX = 0;
		
	@Override
	public Plan agentInitialisation(Agent_CentralManager centralManager,
			Iteration iteration, JobRun jobRun, IslandModelConfiguration configuration,
			IAgentLogger logger) throws Exception {
		
		AID [] aidManagerAgents = centralManager.searchDF(
				Agent_ManagerAgent.class.getName());
		
		// chooses node
		AID managerAidI;
		try {
			managerAidI = aidManagerAgents[NODE_INDEX];
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new IllegalStateException("Manager agent to create Computing Agent not available");
		}
		
		InputMethodDescription methodI = jobRun.getMethods().get(COMPUTING_METHOD_INDEX);
		InputAgentConfiguration inputAgentConfI = methodI.getInputAgentConfiguration();
	
		ProblemToolDefinition problemToolI = methodI.getProblemToolDefinition();
		ProblemWrapper problemStructI = jobRun.exportProblemWrapper(problemToolI);

		
		AgentConfiguration createdAgent = ManagerAgentService.sendCreateAgent(
				centralManager, managerAidI, inputAgentConfI, logger);
		
			
		// assumes the existence of only one Computing Agent
		AID [] aidComputingAgents = centralManager.searchDF(
				Agent_ComputingAgent.class.getName());

		AID computingAgent = aidComputingAgents[0];
		
		boolean startOK = ComputingAgentService.sendStartComputing(centralManager,
				computingAgent, problemStructI, configuration, logger);
		if (! startOK) {
			centralManager.exit();
		}

		
		MethodDescription createdDescription = new MethodDescription(
				createdAgent, jobRun.getProblem(), problemToolI);
		return new Plan(iteration, createdDescription);
	}

	
	@Override
	public Pair<Plan,RePlan> replan(Iteration iteration, History history
			 ) throws Exception {
		
		Plan plan = new Plan(iteration);
		RePlan rePlan = new RePlan(iteration);
		return new Pair<Plan, RePlan>(plan, rePlan);
	}

	@Override
	public void exit(Agent_CentralManager centralManager, IAgentLogger logger) {
		
		ManagerAgentService.killAllComputingAgent(centralManager, logger);
		
	}


}
