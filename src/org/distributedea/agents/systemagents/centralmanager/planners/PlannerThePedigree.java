package org.distributedea.agents.systemagents.centralmanager.planners;

import java.util.logging.Level;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationOneMethodPerCore;
import org.distributedea.agents.systemagents.centralmanager.structures.PlannerTool;
import org.distributedea.agents.systemagents.centralmanager.structures.history.History;
import org.distributedea.agents.systemagents.centralmanager.structures.methodsstatistics.MethodsStatistics;
import org.distributedea.agents.systemagents.centralmanager.structures.plan.InputRePlan;
import org.distributedea.javaextension.Pair;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individualhash.IndividualHash;
import org.distributedea.ontology.islandmodel.IslandModelConfiguration;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescription;
import org.distributedea.ontology.methoddescriptionnumber.MethodDescriptionNumber;
import org.distributedea.ontology.methoddescriptionnumber.MethodDescriptionNumbers;
import org.distributedea.ontology.methodtypenumber.MethodTypeNumber;
import org.distributedea.ontology.methodtypenumber.MethodTypeNumbers;
import org.distributedea.ontology.pedigree.Pedigree;
import org.distributedea.ontology.plan.Plan;
import org.distributedea.ontology.plan.RePlan;
import org.distributedea.services.ManagerAgentService;

public class PlannerThePedigree implements IPlanner {

	private int SKIPPED_ITERATIONS_COUNT = 5;
	
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

	
	@Override
	public Pair<Plan, RePlan> replan(Iteration iteration, History history
			 ) throws Exception {
		
		// initialization of Methods on the new containers
		Pair<Plan, RePlan> rePlanInit = plannerInit.replan(iteration, history);
		
		// process RePlan
		InputRePlan rePlan = replanning(iteration, history);
		RePlan rePlanUpdated = PlannerTool.processReplanning(centralManager,
				rePlan, jobRun, configuration, logger);
		
		return new Pair<Plan, RePlan>(rePlanInit.first, rePlanUpdated);
	}
	
	private InputRePlan replanning(Iteration iteration, History history
			 ) throws Exception {
			
		MethodsStatistics methodsStatistics = history.exportMethodsResults(iteration);
		IndividualHash theBestIndiwHash = methodsStatistics.exportTheBestIndividualWrp();
		Pedigree pedigree = theBestIndiwHash.getPedigree();
		
		// pring info
		printLog(centralManager, theBestIndiwHash, logger);

		// skip killing during first iteration
		if (iteration.getIterationNumber() < SKIPPED_ITERATIONS_COUNT) {
			return new InputRePlan(iteration);
		}

		
		MethodDescriptionNumbers pedigreeNumbers = pedigree.exportCreditsOfMethodDescriptions();
		MethodDescriptionNumbers numbers =
				pedigreeNumbers.exportMethodDescriptionNumbersOfGiven(
						history.exportRunningMethods());
		
		
		MethodDescription methodToKill = history
				.exportRunningMethods().exportRandomMethodDescription();
		
		MethodTypeNumbers typeNumbers = numbers.exportMethodTypeNumbers();
		MethodTypeNumber methodTypeNumberWithMax =
				typeNumbers.exportMethodTypeNumberWithMaxNumber();
		
		InputMethodDescription methodToCreate = methodTypeNumberWithMax
				.getMethodType().exportInputMethodDescription();
		
		return new InputRePlan(iteration, methodToKill, methodToCreate).
				exportOptimalizedInpuRePlan();
	}

	private void printLog(Agent_CentralManager centralManager,
			IndividualHash theBestIndiwHash, IAgentLogger logger) {
		
		Pedigree pedigree = theBestIndiwHash.getPedigree();
		
		MethodDescriptionNumbers methodDescriptionNumbers =
				pedigree.exportCreditsOfMethodDescriptions();
		for (MethodDescriptionNumber numI :
			methodDescriptionNumbers.getMethDescNumbers()) {
			System.out.println("  " + numI.getDescription().exportMethodName() + " " + numI.getNumber());
		}
	}
	
	@Override
	public void exit(Agent_CentralManager centralManager, IAgentLogger logger) {
		
		ManagerAgentService.killAllComputingAgent(centralManager, logger);
	}

}
