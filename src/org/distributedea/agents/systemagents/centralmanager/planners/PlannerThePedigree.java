package org.distributedea.agents.systemagents.centralmanager.planners;

import java.util.List;
import java.util.logging.Level;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationOneMethodPerCore;
import org.distributedea.agents.systemagents.centralmanager.structures.PlannerTool;
import org.distributedea.agents.systemagents.centralmanager.structures.history.History;
import org.distributedea.agents.systemagents.centralmanager.structures.methodsstatistics.MethodsStatistics;
import org.distributedea.agents.systemagents.centralmanager.structures.plan.InputRePlan;
import org.distributedea.javaextension.Pair;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescription;
import org.distributedea.ontology.methoddescriptionnumber.MethodDescriptionNumber;
import org.distributedea.ontology.methoddescriptionnumber.MethodDescriptionNumbers;
import org.distributedea.ontology.methodtypenumber.MethodTypeNumbers;
import org.distributedea.ontology.pedigree.Pedigree;
import org.distributedea.ontology.plan.Plan;
import org.distributedea.ontology.plan.RePlan;
import org.distributedea.services.ManagerAgentService;

public class PlannerThePedigree implements IPlanner {

	private Agent_CentralManager centralManager;
	private JobRun jobRun;
	private IAgentLogger logger;
	
	private IPlanner plannerInit = null;
	
	@Override
	public Plan agentInitialisation(Agent_CentralManager centralManager,
			Iteration iteration, JobRun jobRun, IAgentLogger logger)
			throws Exception {
		
		logger.log(Level.INFO, "Planner " + getClass().getSimpleName() + " initialization");
		
		this.centralManager = centralManager;
		this.jobRun = jobRun;
		this.logger = logger;
		
		plannerInit = new PlannerInitialisationOneMethodPerCore();
		return plannerInit.agentInitialisation(centralManager, iteration, jobRun, logger);
	}

	
	@Override
	public Pair<Plan, RePlan> replan(Iteration iteration, History history
			 ) throws Exception {
		
		// initialization of Methods on the new containers
		Pair<Plan, RePlan> rePlanInit = plannerInit.replan(iteration, history);
		
		// process RePlan
		InputRePlan rePlan = replanning(iteration, history);
		RePlan rePlanUpdated = PlannerTool.processReplanning(centralManager,
				rePlan, jobRun, logger);
		
		return new Pair<Plan, RePlan>(rePlanInit.first, rePlanUpdated);
	}
	
	private InputRePlan replanning(Iteration iteration, History history
			 ) throws Exception {
			
		MethodsStatistics methodsStatistics = history.exportMethodsResults(iteration);
		IndividualWrapper theBestIndiwWrp = methodsStatistics.exportTheBestIndividualWrp();
		Pedigree pedigree = theBestIndiwWrp.getIndividualEvaluated().getPedigree();
		
		// pring info
		printLog(centralManager, theBestIndiwWrp, logger);

		// skip killing during first iteration
		if (iteration.getIterationNumber() < 5) {
			return new InputRePlan(iteration);
		}

		
		MethodDescriptionNumbers pedigreeNumbers = pedigree.exportCreditsOfMethodDescriptions();
		List<MethodDescriptionNumber> runningPedigreeNumbers =
				pedigreeNumbers.exportMethodDescriptionNumbersOfGiven(
				history.exportRunningMethods());
		
		MethodDescriptionNumbers numbers =
				new MethodDescriptionNumbers(runningPedigreeNumbers);
		
		MethodTypeNumbers typeNumbers = numbers.exportMethodTypeNumbers();
		
		MethodDescription methodToKill = history
				.exportRunningMethods().exportRandomAgentDescription();
		InputMethodDescription methodToCreate = typeNumbers
				.exportMethodTypeNumberWithMaxNumber().getMethodType().exportInputAgentDescription();
		
		return new InputRePlan(iteration, methodToKill, methodToCreate).
				exportOptimalizedInpuRePlan();
	}

	private void printLog(Agent_CentralManager centralManager,
			IndividualWrapper theBestIndiwWrp, IAgentLogger logger) {
		
	}
	
	@Override
	public void exit(Agent_CentralManager centralManager, IAgentLogger logger) {
		
		ManagerAgentService.killAllComputingAgent(centralManager, logger);
	}

}
