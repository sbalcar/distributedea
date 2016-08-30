package org.distributedea.agents.systemagents.centralmanager.planners.historybased;

import java.util.Random;
import java.util.logging.Level;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.planners.Planner;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisation;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationState;
import org.distributedea.agents.systemagents.centralmanager.structures.PlannerTool;
import org.distributedea.agents.systemagents.centralmanager.structures.history.History;
import org.distributedea.agents.systemagents.centralmanager.structures.methodsstatistics.MethodsStatistics;
import org.distributedea.agents.systemagents.centralmanager.structures.plan.InputRePlan;
import org.distributedea.javaextension.Pair;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.agentdescription.inputdescription.InputAgentDescription;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.monitor.MethodStatistic;
import org.distributedea.ontology.plan.Plan;
import org.distributedea.ontology.plan.RePlan;
import org.distributedea.services.ManagerAgentService;

public class PlannerRandom implements Planner {
	
	private Agent_CentralManager centralManager;
	private JobRun jobRun;
	private IAgentLogger logger;
	
	private PlannerInitialisation plannerInit = null;
	
	Random ran = new Random();
	
	@Override
	public Plan agentInitialisation(Agent_CentralManager centralManager,
			Iteration iteration, JobRun jobRun, IAgentLogger logger) throws Exception {
		
		logger.log(Level.INFO, "Planner " + getClass().getSimpleName() + " initialization");
		
		this.centralManager = centralManager;
		this.jobRun = jobRun;
		this.logger = logger;
		
		PlannerInitialisationState state = PlannerInitialisationState.RUN_ONE_AGENT_PER_CORE;
		
		plannerInit = new PlannerInitialisation(state, true);
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
		
		History historyOfCurrentMethods = history.
				exportHistoryOfRunningMethods(iteration, 0);
		MethodsStatistics currentMethodsResults =
				historyOfCurrentMethods.exportMethodsResults(iteration);
		int todo;
/*		
		if (currentMethodsResults.getNumberOfMethodsStatistics() <= 1) {
			return new RePlan(iteration);
		}
*/
		InputAgentDescription candidateDescription = plannerInit.removeNextCandidate();
		if (candidateDescription != null) {
			
			MethodStatistic theWorstMethodStatistic = currentMethodsResults
					.exportMethodAchievedTheLeastQuantityOfImprovement();
			AgentDescription methodToKill =
					theWorstMethodStatistic.exportAgentDescriptionClone();
			
			InputAgentDescription methodToCreate =
					jobRun.exportRandomSelectedAgentDescription();
			
			return new InputRePlan(iteration, methodToKill, methodToCreate);
		}
		
		return replanRandomByRandomMethod(iteration, currentMethodsResults);
	}
	
	
	private InputRePlan replanRandomByRandomMethod(Iteration iteration,
			MethodsStatistics currentMethodsResults) throws Exception {			

		//random select agent to kill
		MethodStatistic randomMethodStatistic =
				currentMethodsResults.exportRandomMethodStatistic();

		AgentDescription methodToKill =
				randomMethodStatistic.exportAgentDescriptionClone();
		
		//random select agent to create
		InputAgentDescription methodToCreate =
				jobRun.exportRandomSelectedAgentDescription();

		
		return new InputRePlan(iteration, methodToKill, methodToCreate);
	}
	
	
	@Override
	public void exit(Agent_CentralManager centralManager, IAgentLogger logger) {
		ManagerAgentService.killAllComputingAgent(centralManager, logger);
		
	}
}
