package org.distributedea.agents.systemagents.centralmanager.planners.dumy;

import java.util.logging.Level;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.planners.IPlanner;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationOneMethodPerCore;
import org.distributedea.agents.systemagents.centralmanager.structures.PlannerTool;
import org.distributedea.agents.systemagents.centralmanager.structures.history.History;
import org.distributedea.agents.systemagents.centralmanager.structures.plan.InputRePlan;
import org.distributedea.javaextension.Pair;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.individualwrapper.IndividualsWrappers;
import org.distributedea.ontology.islandmodel.IslandModelConfiguration;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.method.Methods;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescription;
import org.distributedea.ontology.plan.Plan;
import org.distributedea.ontology.plan.RePlan;
import org.distributedea.services.ComputingAgentService;
import org.distributedea.services.ManagerAgentService;


public class PlannerFollowNaiveAskingForBestResult implements IPlanner {
	
	private Agent_CentralManager centralManager;
	private JobRun jobRun;
	private IslandModelConfiguration configuration;
	private IAgentLogger logger;
	
	private IPlanner plannerInit = null; 
	
	@Override
	public Plan agentInitialisation(Agent_CentralManager centralManager,
			Iteration iteration, JobRun jobRun, IslandModelConfiguration configuration,
			IAgentLogger logger) throws Exception {
		
		logger.log(Level.INFO, "Planner " + getClass().getSimpleName() + " initialization");
		
		this.centralManager = centralManager;
		this.jobRun = jobRun;
		this.configuration = configuration;
		this.logger = logger;
		
		
		plannerInit = new PlannerInitialisationOneMethodPerCore();
		return plannerInit.agentInitialisation(centralManager, iteration,
				jobRun, configuration, logger);
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
				rePlan, jobRun, configuration, logger);
		
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
		
		Methods agentDescriptions =
				jobRun.getMethods().exportInputMethodDescriptions();

		Methods methodsWhichHaveNeverRun =  history
				.getMethodHistories().exportsMethodsWhichHaveNeverRun(agentDescriptions);
		
		InputMethodDescription candidateDescrip = methodsWhichHaveNeverRun.exportRandomMethodDescription();
		
		if (candidateDescrip != null) {
			
			//kill the worst and replace by new Method
			MethodDescription methodToKill =
					theWorstIndivWrp.exportAgentDescriptionClone();
			return new InputRePlan(iteration, methodToKill, candidateDescrip);
		}
		
		//kill the worst and duplicate best Method
		MethodDescription theBestDescription =
				theBestIndivWrp.exportAgentDescriptionClone();
		MethodDescription theWorstDescription =
				theWorstIndivWrp.exportAgentDescriptionClone();
		return new InputRePlan(iteration, theWorstDescription,
				theBestDescription.exportInputMethodDescription());
	}
	
	private void printLog(
			IndividualWrapper theBestIndivWrp,
			IndividualWrapper theWorstIndivWrp,
			IAgentLogger logger) throws Exception {
		
		String theWorstIndivAgentName = theWorstIndivWrp.
				exportAgentConfiguration().exportAgentname();
		double theWorstIndivFitness = theWorstIndivWrp.
				getIndividualEvaluated().getFitness();
		
		logger.log(Level.INFO, "The worst: " + theWorstIndivAgentName +
				" fitness: " + theWorstIndivFitness);

		String theBestIndivAgentName = theBestIndivWrp.
				exportAgentConfiguration().exportAgentname();
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
