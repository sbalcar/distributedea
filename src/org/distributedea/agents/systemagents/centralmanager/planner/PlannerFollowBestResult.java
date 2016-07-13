package org.distributedea.agents.systemagents.centralmanager.planner;

import java.util.logging.Level;

import org.distributedea.agents.computingagents.computingagent.service.ComputingAgentService;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.planner.history.History;
import org.distributedea.agents.systemagents.centralmanager.planner.initialisation.PlannerInitialisation;
import org.distributedea.agents.systemagents.centralmanager.planner.initialisation.PlannerInitialisationState;
import org.distributedea.agents.systemagents.centralmanager.planner.modes.Iteration;
import org.distributedea.agents.systemagents.centralmanager.planner.plan.Plan;
import org.distributedea.agents.systemagents.centralmanager.planner.plan.RePlan;
import org.distributedea.agents.systemagents.centralmanager.planner.resultsmodel.ResultsOfComputing;
import org.distributedea.agents.systemagents.centralmanager.planner.tool.Pair;
import org.distributedea.agents.systemagents.centralmanager.planner.tool.PlannerException;
import org.distributedea.agents.systemagents.centralmanager.planner.tool.PlannerTool;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.problem.Problem;


public class PlannerFollowBestResult implements Planner {
	
	private Agent_CentralManager centralManager;
	private JobRun jobRun;
	private IAgentLogger logger;
	
	private PlannerInitialisation plannerInit = null; 
	
	@Override
	public Plan agentInitialisation(Agent_CentralManager centralManager,
			Iteration iteration, JobRun jobRun, IAgentLogger logger) throws PlannerException {
		
		logger.log(Level.INFO, "Planner " + getClass().getSimpleName() + " initialization");
		
		this.centralManager = centralManager;
		this.jobRun = jobRun;
		this.logger = logger;
		
		PlannerInitialisationState state = PlannerInitialisationState.RUN_ONE_AGENT_PER_CORE;
		
		plannerInit = new PlannerInitialisation(state, true);
		return plannerInit.agentInitialisation(centralManager, iteration,
				jobRun, logger);
	}

	/**
	 * Nothing is changing
	 * @param centralManager
	 * @param problemTool
	 * @param logger
	 */
	@Override
	public Pair<Plan,RePlan> replan(Iteration iteration, History history
			 ) throws PlannerException {
		
		// initialization of Methods on the new containers
		Pair<Plan, RePlan> planInit = plannerInit.replan(iteration, history);
		
		// process RePlan
		RePlan rePlan = replanning(iteration, history);
		PlannerTool.processReplanning(centralManager, rePlan, jobRun, logger);
		
		return new Pair<Plan, RePlan>(planInit.first, rePlan);
	}
	
	private RePlan replanning(Iteration iteration, History history
				 ) throws PlannerException {
		
		ResultsOfComputing resultOfComputingWrapper =
				ComputingAgentService.sendAccessesResult_(centralManager, logger);
				
		printLog(centralManager, jobRun, resultOfComputingWrapper, logger);
		
		// skip killing during first iteration
		if (iteration.getIterationNumber() < 5) {
			return new RePlan(iteration);
		}
		
		if (! resultOfComputingWrapper.exportContainsMoreThanOneMethod()) {
			return new RePlan(iteration);
		}
		
		AgentDescription candidateDescription = plannerInit.removeNextCandidate();
		if (candidateDescription != null) {
			
			return killWorstAndReplaceByNewMethod(iteration,
					jobRun, resultOfComputingWrapper, candidateDescription);
		}
		
		return killWorstAndDuplicateBestMethod(iteration,
				jobRun, resultOfComputingWrapper);
	}
	
	private void printLog(
			Agent_CentralManager centralManager, JobRun job,
			ResultsOfComputing resultOfComputingWrapper,
			IAgentLogger logger) throws PlannerException {
		
		Problem problem = job.getProblem();
		
		IndividualWrapper bestResult = resultOfComputingWrapper
				.exportBestResultOfComputing(problem);
		IndividualWrapper worstResult = resultOfComputingWrapper
				.exportWorstResultOfComputing(problem);
		
		String worstResultAgentName = worstResult.getAgentDescription().getAgentConfiguration().getAgentName();
		logger.log(Level.INFO, "The worst: " + worstResultAgentName + " fitness: " + worstResult.getIndividualEvaluated().getFitness());

		String bestResultAgentName = bestResult.getAgentDescription().getAgentConfiguration().getAgentName();
		logger.log(Level.INFO, "The best : " + bestResultAgentName + " fitness: " + bestResult.getIndividualEvaluated().getFitness());
	}
	
	static RePlan killWorstAndReplaceByNewMethod(
			Iteration iteration, JobRun job,
			ResultsOfComputing resultOfComputingWrapper,
			AgentDescription newMethod) throws PlannerException {

		Problem problem = job.getProblem();
		
		IndividualWrapper worstResult = resultOfComputingWrapper
				.exportWorstResultOfComputing(problem);
		AgentDescription methodToKill =
				worstResult.getAgentDescription();
		
		return new RePlan(iteration, methodToKill, newMethod);
	}
	
	private RePlan killWorstAndDuplicateBestMethod(
			Iteration iteration, JobRun job,
			ResultsOfComputing resultOfComputingWrapper
			) throws PlannerException {

		Problem problem = job.getProblem();
		
		IndividualWrapper bestResult = resultOfComputingWrapper
				.exportBestResultOfComputing(problem);
		IndividualWrapper worstResult = resultOfComputingWrapper
				.exportWorstResultOfComputing(problem);
		
		AgentDescription bestDescription =
				bestResult.getAgentDescription();
		AgentDescription worstDescription =
				worstResult.getAgentDescription();
		
		return new RePlan(iteration, worstDescription,
				bestDescription);
	}
	
	
	@Override
	public void exit(Agent_CentralManager centralManager, IAgentLogger logger) {
		PlannerTool.killAllComputingAgent(centralManager, logger);
		
	}

}
