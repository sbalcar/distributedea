package org.distributedea.agents.systemagents.centralmanager.planner.initialization;

import jade.core.AID;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.computingagent.service.ComputingAgentService;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.planner.Planner;
import org.distributedea.agents.systemagents.centralmanager.planner.modes.Iteration;
import org.distributedea.agents.systemagents.centralmanager.planner.modes.ReceivedData;
import org.distributedea.agents.systemagents.centralmanager.planner.tool.Pair;
import org.distributedea.agents.systemagents.centralmanager.planner.tool.PlannerException;
import org.distributedea.agents.systemagents.centralmanager.planner.tool.PlannerTool;
import org.distributedea.agents.systemagents.manageragent.ManagerAgentService;
import org.distributedea.configuration.AgentConfigurations;
import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.management.computingnode.NodeInfosWrapper;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemStruct;

public class PlannerInitialization implements Planner {

	private PlannerInitializationState state = PlannerInitializationState.RUN_ONE_AGENT_PER_CORE;
	private boolean methodRepetition = true;
	

	private List<AgentDescription> nextCandidates = null;
	
	public PlannerInitialization() {
	}
	
	public PlannerInitialization(PlannerInitializationState state, boolean methodRepetition) {
		this.state = state;
		this.methodRepetition = methodRepetition;
	}
	

	/**
	 * Agent state
	 * @return
	 */
	public PlannerInitializationState getState() {
		return state;
	}
	
	/**
	 * Method repetition
	 * @return
	 */
	public boolean isMethodRepetition() {
		return this.methodRepetition;
	}

	
	/**
	 * Returns candidates for next re-planing
	 * @return
	 */
	public List<AgentDescription> getNextCandidates() {
		return this.nextCandidates;
	}
	
	/**
	 * removes ant returns next candidate
	 * @return
	 */
	public AgentDescription removeNextCandidate() {
		
		if (nextCandidates == null || nextCandidates.isEmpty()) {
			return null;
		}
		return nextCandidates.remove(0);
	}
	
	
	@Override
	public void agentInitialization(Agent_CentralManager centralManager,
			JobRun job, AgentLogger logger) throws PlannerException {
		
		NodeInfosWrapper availableNodes =
				PlannerTool.getAvailableNodes(centralManager, logger);
		
		Plan plan = createPlanForEmptyCores(availableNodes, job, logger);
				
		List<Pair<AID,AgentDescription>> planPairing = plan.getPlan();
		nextCandidates = plan.getNextCandidates();
		
		createAndRunAgents(centralManager, job, planPairing, logger);
		
	}

	public Plan createPlanForEmptyCores(NodeInfosWrapper availableNodes,
			JobRun job, AgentLogger logger) throws PlannerException {
	
		List<AID> managersAID =
				availableNodes.exportManagerAIDOfEachEmptyCore();
		
		AgentConfigurations configurations = job.getAgentConfigurations();
		List<AgentDescription> descriptions =
				InitializationTool.getCartesianProductOfConfigurationsAndTools(
						configurations,job.getProblemTools());
		
		return InitializationTool.createPlan(this, managersAID, descriptions);
	}

	public void agentInitializationOnlyCreateAgents(Agent_CentralManager centralManager,
			JobRun job, AgentLogger logger) throws PlannerException {
		
		NodeInfosWrapper availableNodes =
				PlannerTool.getAvailableNodes(centralManager, logger);
	
		Plan plan = createPlanForEmptyCores(availableNodes, job, logger);
				
		List<Pair<AID,AgentDescription>> planPairing = plan.getPlan();
		nextCandidates = plan.getNextCandidates();
		
		createAgents(centralManager, job, planPairing, logger);
		
	}
	
	@Override
	public void replan(Agent_CentralManager centralManager, JobRun job,
			Iteration iteration, ReceivedData receivedData, AgentLogger logger
			) throws PlannerException {
		
		// init free core by using candidate
		NodeInfosWrapper availableNodes = PlannerTool.getAvailableNodes(centralManager, logger);
		List<AID> managersAID = availableNodes.exportManagerAIDOfEachEmptyCore();
	
		Plan plan =  InitializationTool.createPlan(this, managersAID, nextCandidates);
				
		List<Pair<AID,AgentDescription>> planPairing = plan.getPlan();
		nextCandidates = plan.getNextCandidates();
		
		createAndRunAgents(centralManager, job, planPairing, logger);
		
		
		// init free cores by using all methods
		NodeInfosWrapper availableNodes2 = PlannerTool.getAvailableNodes(centralManager, logger);
		if (! availableNodes2.exportManagersAID().isEmpty()) {
			
			Plan plan2 = createPlanForEmptyCores(availableNodes2, job, logger);
			createAgents(centralManager, job, plan2.getPlan(), logger);
		}
	}

	
	@Override
	public void exit(Agent_CentralManager centralManager, AgentLogger logger) {
		
		PlannerTool.killAllComputingAgent(centralManager, logger);
	
	}

	private void createAndRunAgents(Agent_CentralManager centralManager, JobRun job,
			List<Pair<AID,AgentDescription>> plan, AgentLogger logger) {
		
		List<AID> createdAgents =
				createAgents(centralManager, job, plan, logger);
		runAgents(centralManager, job, plan, createdAgents, logger);
	}

	private List<AID> createAgents(Agent_CentralManager centralManager, JobRun job,
			List<Pair<AID,AgentDescription>> plan, AgentLogger logger) {
		
		int numberOfDescriotion = plan.size();
		
		List<AID> createdAgents = new ArrayList<>();
		//create computing agents
		for (int cpuIndex = 0; cpuIndex < numberOfDescriotion; cpuIndex++) {	
		
			Pair<AID,AgentDescription> pairI = plan.get(cpuIndex);
			
			AID managerAgentOfEmptyCoreAIDI = pairI.first;
			
			AgentDescription agentDescriptionI = pairI.second;
			AgentConfiguration agentConfigurationI = agentDescriptionI.getAgentConfiguration();
			
			AID createdAgentI = ManagerAgentService.sendCreateAgent(centralManager,
					managerAgentOfEmptyCoreAIDI, agentConfigurationI, logger);
			
			createdAgents.add(createdAgentI);
		}
		
		return createdAgents;
	}
	
	private void runAgents(Agent_CentralManager centralManager, JobRun job,
			List<Pair<AID,AgentDescription>> plan, List<AID> createdAgents, AgentLogger logger) {
		
		int numberOfDescriotion = plan.size();
		
		//start computing agent
		for (int cpuIndex = 0; cpuIndex < numberOfDescriotion; cpuIndex++) {

			Pair<AID,AgentDescription> pairI = plan.get(cpuIndex);
			
			AgentDescription agentDescriptionI = pairI.second;
			Class<?> problemToolI = agentDescriptionI.exportProblemToolClass();
			
			ProblemStruct problemStructI = job.exportProblemStruct(problemToolI);
			
			AID createdAgentI = createdAgents.get(cpuIndex);
			
			ComputingAgentService.sendStartComputing(
					centralManager, createdAgentI, problemStructI, logger);
		}
		
	}
	
}
