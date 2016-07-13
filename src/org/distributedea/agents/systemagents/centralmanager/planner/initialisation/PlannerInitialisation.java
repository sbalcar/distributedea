package org.distributedea.agents.systemagents.centralmanager.planner.initialisation;

import jade.core.AID;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.computingagents.computingagent.service.ComputingAgentService;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.planner.Planner;
import org.distributedea.agents.systemagents.centralmanager.planner.history.History;
import org.distributedea.agents.systemagents.centralmanager.planner.modes.Iteration;
import org.distributedea.agents.systemagents.centralmanager.planner.plan.Plan;
import org.distributedea.agents.systemagents.centralmanager.planner.plan.RePlan;
import org.distributedea.agents.systemagents.centralmanager.planner.tool.Pair;
import org.distributedea.agents.systemagents.centralmanager.planner.tool.PlannerException;
import org.distributedea.agents.systemagents.centralmanager.planner.tool.PlannerTool;
import org.distributedea.agents.systemagents.manageragent.ManagerAgentService;
import org.distributedea.configuration.AgentConfigurations;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.management.computingnode.NodeInfosWrapper;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemStruct;

public class PlannerInitialisation implements Planner {

	private Agent_CentralManager centralManager;
	private JobRun jobRun;
	private IAgentLogger logger;
	
	private PlannerInitialisationState state = PlannerInitialisationState.RUN_ONE_AGENT_PER_CORE;
	private boolean methodRepetition = true;
	

	private List<AgentDescription> nextCandidates = null;
	
	public PlannerInitialisation() {
	}
	
	public PlannerInitialisation(PlannerInitialisationState state, boolean methodRepetition) {
		this.state = state;
		this.methodRepetition = methodRepetition;
	}
	

	/**
	 * Agent state
	 * @return
	 */
	public PlannerInitialisationState getState() {
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
	public Plan agentInitialisation(Agent_CentralManager centralManager,
			Iteration iteration, JobRun job, IAgentLogger logger) throws PlannerException {
		
		this.centralManager = centralManager;
		this.jobRun = job;
		this.logger = logger;
		
		NodeInfosWrapper availableNodes =
				PlannerTool.getAvailableNodes(centralManager, logger);
		
		Schedule schedule = createScheduleForEmptyCores(availableNodes, job, logger);
				
		List<Pair<AID,AgentDescription>> planPairing = schedule.getSchedule();
		nextCandidates = schedule.getNextCandidates();
		
		return createAndRunAgents(centralManager, iteration, job, planPairing, logger);
		
	}

	private Schedule createScheduleForEmptyCores(NodeInfosWrapper availableNodes,
			JobRun job, IAgentLogger logger) throws PlannerException {
	
		List<AID> managersAID =
				availableNodes.exportManagerAIDOfEachEmptyCore();
		
		AgentConfigurations configurations = job.getAgentConfigurations();
		List<AgentDescription> descriptions =
				InitialisationTool.getCartesianProductOfConfigurationsAndTools(
						configurations, job.getProblemTools());
		
		return InitialisationTool.createPlan(this, managersAID, descriptions);
	}

	/**
	 * Initialisation - Only Create agents
	 * @param centralManager
	 * @param job
	 * @param logger
	 * @throws PlannerException
	 */
	public void agentInitialisationOnlyCreateAgents(Agent_CentralManager centralManager,
			JobRun job, IAgentLogger logger) throws PlannerException {
		
		NodeInfosWrapper availableNodes =
				PlannerTool.getAvailableNodes(centralManager, logger);
	
		Schedule plan = createScheduleForEmptyCores(availableNodes, job, logger);
				
		List<Pair<AID,AgentDescription>> planPairing = plan.getSchedule();
		nextCandidates = plan.getNextCandidates();
		
		createAgents(centralManager, planPairing, logger);
		
	}
	
	@Override
	public Pair<Plan, RePlan> replan(Iteration iteration, History history
			) throws PlannerException {
		
		// init free core by using candidate
		NodeInfosWrapper availableNodes = PlannerTool.getAvailableNodes(centralManager, logger);
		List<AID> managersAID = availableNodes.exportManagerAIDOfEachEmptyCore();
	
		Schedule scheduleInit = InitialisationTool.createPlan(this, managersAID, nextCandidates);
				
		List<Pair<AID,AgentDescription>> planPairingInit = scheduleInit.getSchedule();
		nextCandidates = scheduleInit.getNextCandidates();
		
		Plan planInit = createAndRunAgents(centralManager, iteration, jobRun, planPairingInit, logger);
		
		Plan plan = null;
		// init free cores by using all methods
		NodeInfosWrapper availableNodes2 = PlannerTool.getAvailableNodes(centralManager, logger);
		if (! availableNodes2.exportManagersAID().isEmpty()) {
			
			Schedule scheduleEmpty = createScheduleForEmptyCores(availableNodes2, jobRun, logger);
			List<Pair<AID,AgentDescription>> planPairing = scheduleEmpty.getSchedule();
			plan = createAndRunAgents(centralManager, iteration, jobRun, planPairing, logger);
		}
		
		Plan planConcat = Plan.concat(planInit, plan);
		return new Pair<Plan, RePlan>(planConcat, new RePlan(iteration));
	}

	
	@Override
	public void exit(Agent_CentralManager centralManager, IAgentLogger logger) {
		
		PlannerTool.killAllComputingAgent(centralManager, logger);
	
	}

	private Plan createAndRunAgents(Agent_CentralManager centralManager,
			Iteration iteration, JobRun job,
			List<Pair<AID,AgentDescription>> plan, IAgentLogger logger) {
		
		List<AgentDescription> createdAgents =
				createAgents(centralManager, plan, logger);
		runAgents(centralManager, createdAgents, job, logger);
		
		return new Plan(iteration, createdAgents);
	}

	private List<AgentDescription> createAgents(Agent_CentralManager centralManager,
			List<Pair<AID,AgentDescription>> plan, IAgentLogger logger) {
		
		int numberOfDescriotion = plan.size();
		
		List<AgentDescription> createdAgents = new ArrayList<>();
		//create computing agents
		for (int cpuIndex = 0; cpuIndex < numberOfDescriotion; cpuIndex++) {	
		
			Pair<AID,AgentDescription> pairI = plan.get(cpuIndex);
			
			AID managerAgentOfEmptyCoreAIDI = pairI.first;
			
			AgentDescription agentDescriptionI = pairI.second;
			Class<?> problemToolClass = agentDescriptionI.exportProblemToolClass();
			AgentConfiguration agentConfigurationI = agentDescriptionI.getAgentConfiguration();
			
			AgentConfiguration createdAgentI = ManagerAgentService.sendCreateAgent(centralManager,
					managerAgentOfEmptyCoreAIDI, agentConfigurationI, logger);
			
			AgentDescription createdAgentDescriptionI = new AgentDescription();
			createdAgentDescriptionI.setAgentConfiguration(createdAgentI);
			createdAgentDescriptionI.importProblemToolClass(problemToolClass);
			
			createdAgents.add(createdAgentDescriptionI);
		}
		
		return createdAgents;
	}
	
	private void runAgents(Agent_CentralManager centralManager,
			List<AgentDescription> createdAgents,  JobRun job, IAgentLogger logger) {
		
		for (AgentDescription agentDescriptionI : createdAgents) {

			AgentConfiguration agentConfigurationI = agentDescriptionI.getAgentConfiguration();

			Class<?> problemToolI = agentDescriptionI.exportProblemToolClass();
			
			ProblemStruct problemStructI = job.exportProblemStruct(problemToolI);
					
			ComputingAgentService.sendStartComputing(
					centralManager, agentConfigurationI.exportAgentAID(), problemStructI, logger);
		}
		
	}
	
}
