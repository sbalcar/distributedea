package org.distributedea.agents.systemagents.centralmanager.planners.onlyinit;

import jade.core.AID;

import java.util.List;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.planners.Planner;
import org.distributedea.agents.systemagents.centralmanager.structures.PlannerTool;
import org.distributedea.agents.systemagents.centralmanager.structures.history.History;
import org.distributedea.agents.systemagents.centralmanager.structures.plan.InputPlan;
import org.distributedea.agents.systemagents.centralmanager.structures.schedule.InitialisationTool;
import org.distributedea.agents.systemagents.centralmanager.structures.schedule.Schedule;
import org.distributedea.javaextension.Pair;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.agentdescription.inputdescription.InputAgentDescription;
import org.distributedea.ontology.agentdescription.inputdescription.InputAgentDescriptions;
import org.distributedea.ontology.configuration.inputconfiguration.InputAgentConfigurations;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.management.computingnode.NodeInfosWrapper;
import org.distributedea.ontology.plan.Plan;
import org.distributedea.ontology.plan.RePlan;
import org.distributedea.services.ManagerAgentService;

/**
 * Planer Initialisation
 * @author stepan
 *
 */
public class PlannerInitialisation implements Planner {

	private Agent_CentralManager centralManager;
	private JobRun jobRun;
	private IAgentLogger logger;
	
	private PlannerInitialisationState state;
	private boolean methodRepetition;
	

	private InputAgentDescriptions nextCandidates = null;
	
	
	@Deprecated
	public PlannerInitialisation() {
		this.state = PlannerInitialisationState.RUN_ONE_AGENT_PER_CORE;
		this.methodRepetition = true;
	}
	
	/**
	 * Constructor
	 * @param state
	 * @param methodRepetition
	 */
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
	public InputAgentDescriptions getNextCandidates() {
		return this.nextCandidates;
	}
	
	/**
	 * removes ant returns next candidate
	 * @return
	 */
	public InputAgentDescription removeNextCandidate() {
		
		if (nextCandidates == null || nextCandidates.isEmpty()) {
			return null;
		}
		return nextCandidates.remove(0);
	}
	
	
	@Override
	public Plan agentInitialisation(Agent_CentralManager centralManager,
			Iteration iteration, JobRun jobRun, IAgentLogger logger
			) throws Exception {
		
		this.centralManager = centralManager;
		this.jobRun = jobRun;
		this.logger = logger;
		
		NodeInfosWrapper availableNodes =
				ManagerAgentService.getAvailableNodes(centralManager, logger);
		
		Schedule schedule = createScheduleForEmptyCores(availableNodes,
				iteration, jobRun, logger);
		InputPlan inputPlan = schedule.getInputPlan();
		
		nextCandidates = schedule.getNextCandidates();
		
		return PlannerTool.createAndRunAgents(centralManager, iteration,
				jobRun, inputPlan, logger);
	}

	private Schedule createScheduleForEmptyCores(NodeInfosWrapper availableNodes,
			Iteration iteration, JobRun job, IAgentLogger logger) throws Exception {
	
		List<AID> managersAID =
				availableNodes.exportManagerAIDOfEachEmptyCore();
		
		InputAgentConfigurations configurations = job.getAgentConfigurations();
		List<InputAgentDescription> descriptions =
				InitialisationTool.getCartesianProductOfConfigurationsAndTools(
						configurations, job.getProblemTools());
		
		return InitialisationTool.createSchedule(this, iteration, managersAID,
				descriptions);
	}

	/**
	 * Initialisation - Only Create agents
	 * @param centralManager
	 * @param job
	 * @param logger
	 * @throws PlannerException
	 */
	public void agentInitialisationOnlyCreateAgents(Agent_CentralManager centralManager,
			Iteration iteration, JobRun job, IAgentLogger logger) throws Exception {
		
		NodeInfosWrapper availableNodes =
				ManagerAgentService.getAvailableNodes(centralManager, logger);
	
		Schedule schedule = createScheduleForEmptyCores(availableNodes,
				iteration, job, logger);
				
		InputPlan planPairing = schedule.getInputPlan();
		nextCandidates = schedule.getNextCandidates();

		PlannerTool.createAgents(centralManager, iteration,
				planPairing, logger);
	}
	
	@Override
	public Pair<Plan, RePlan> replan(Iteration iteration, History history
			) throws Exception {
		
		// init free core by using candidate
		NodeInfosWrapper availableNodes =
				ManagerAgentService.getAvailableNodes(centralManager, logger);
		List<AID> managersAID = availableNodes.exportManagerAIDOfEachEmptyCore();
	
		Schedule scheduleInit = InitialisationTool.createSchedule(this,
				iteration, managersAID, nextCandidates.getAgentDescriptions());
				
		InputPlan planPairingInit = scheduleInit.getInputPlan();
		nextCandidates = scheduleInit.getNextCandidates();
		
		Plan planInit = PlannerTool.createAndRunAgents(centralManager,
				iteration, jobRun, planPairingInit, logger);
		
		Plan plan = null;
		// init free cores by using all methods
		NodeInfosWrapper availableNodes2 =
				ManagerAgentService.getAvailableNodes(centralManager, logger);
		if (! availableNodes2.exportManagersAID().isEmpty()) {
			
			Schedule scheduleEmpty = createScheduleForEmptyCores(availableNodes2,
					iteration, jobRun, logger);
			InputPlan planPairing = scheduleEmpty.getInputPlan();
			plan = PlannerTool.createAndRunAgents(centralManager, iteration,
					jobRun, planPairing, logger);
		}
		
		Plan planConcat = Plan.concat(planInit, plan);
		return new Pair<Plan, RePlan>(planConcat, new RePlan(iteration));
	}

	
	@Override
	public void exit(Agent_CentralManager centralManager, IAgentLogger logger) {
		
		ManagerAgentService.killAllComputingAgent(centralManager, logger);
	}
	
}
