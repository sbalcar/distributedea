package org.distributedea.agents.systemagents.centralmanager.planners.dumy;

import java.util.logging.Level;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.planners.Planner;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisation;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationState;
import org.distributedea.agents.systemagents.centralmanager.structures.PlannerTool;
import org.distributedea.agents.systemagents.centralmanager.structures.history.History;
import org.distributedea.agents.systemagents.centralmanager.structures.plan.InputRePlan;
import org.distributedea.javaextension.Pair;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.agentdescription.inputdescription.InputAgentDescription;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.individualwrapper.IndividualsWrappers;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.plan.Plan;
import org.distributedea.ontology.plan.RePlan;
import org.distributedea.services.ComputingAgentService;
import org.distributedea.services.ManagerAgentService;


public class PlannerFollowNaiveAskingForBestResult implements Planner {
	
	private Agent_CentralManager centralManager;
	private JobRun jobRun;
	private IAgentLogger logger;
	
	private PlannerInitialisation plannerInit = null; 
	
	@Override
	public Plan agentInitialisation(Agent_CentralManager centralManager,
			Iteration iteration, JobRun jobRun, IAgentLogger logger) throws Exception {
		
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
			 ) throws Exception {
		
		// initialization of Methods on the new containers
		Pair<Plan, RePlan> planInit = plannerInit.replan(iteration, history);
		
		// process RePlan
		InputRePlan rePlan = replanning(iteration, history);
		RePlan rePlanUpdated = PlannerTool.processReplanning(centralManager,
				rePlan, jobRun, logger);
		
		return new Pair<Plan, RePlan>(planInit.first, rePlanUpdated);
	}
	
	private InputRePlan replanning(Iteration iteration, History history
				 ) throws Exception {
	
		IndividualsWrappers resultOfComputing =
				ComputingAgentService.sendAccessesResult_(centralManager, logger);
		
		IndividualWrapper theBestIndivWrp = resultOfComputing
				.exportBestResultOfComputing(jobRun.getProblem());
		IndividualWrapper theWorstIndivWrp = resultOfComputing
				.exportWorstResultOfComputing(jobRun.getProblem());

		printLog(theBestIndivWrp, theWorstIndivWrp, logger);
		
		// skip killing during first iteration
		if (iteration.getIterationNumber() < 5) {
			return new InputRePlan(iteration);
		}
		
		if (! resultOfComputing.exportContainsMoreThanOneMethod()) {
			return new InputRePlan(iteration);
		}
		
		InputAgentDescription candidateDescrip = plannerInit.removeNextCandidate();
		if (candidateDescrip != null) {
			
			//kill the worst and replace by new Method
			AgentDescription methodToKill =
					theWorstIndivWrp.exportAgentDescriptionClone();
			return new InputRePlan(iteration, methodToKill, candidateDescrip);
		}
		
		//kill the worst and duplicate best Method
		AgentDescription theBestDescription =
				theBestIndivWrp.exportAgentDescriptionClone();
		AgentDescription theWorstDescription =
				theWorstIndivWrp.exportAgentDescriptionClone();
		return new InputRePlan(iteration, theWorstDescription,
				theBestDescription.exportInputAgentDescription());
	}
	
	private void printLog(
			IndividualWrapper theBestIndivWrp,
			IndividualWrapper theWorstIndivWrp,
			IAgentLogger logger) throws Exception {
		
		String theWorstIndivAgentName = theWorstIndivWrp.
				exportAgentConfiguration().getAgentName();
		double theWorstIndivFitness = theWorstIndivWrp.
				getIndividualEvaluated().getFitness();
		
		logger.log(Level.INFO, "The worst: " + theWorstIndivAgentName +
				" fitness: " + theWorstIndivFitness);

		String theBestIndivAgentName = theBestIndivWrp.
				exportAgentConfiguration().getAgentName();
		double theBestIndivFitness = theBestIndivWrp.
				getIndividualEvaluated().getFitness();
		
		logger.log(Level.INFO, "The best : " + theBestIndivAgentName +
				" fitness: " + theBestIndivFitness);
	}
	
	
	@Override
	public void exit(Agent_CentralManager centralManager, IAgentLogger logger) {
		ManagerAgentService.killAllComputingAgent(centralManager, logger);
		
	}

}
