package org.distributedea.agents.systemagents.centralmanager.plannerinfrastructure;

import java.util.logging.Level;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.Agent_Monitor;
import org.distributedea.agents.systemagents.centralmanager.plannerinfrastructure.endcondition.IPlannerEndCondition;
import org.distributedea.agents.systemagents.centralmanager.planners.IPlanner;
import org.distributedea.agents.systemagents.centralmanager.structures.history.History;
import org.distributedea.javaextension.Pair;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.IndividualArguments;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.islandmodel.IslandModelConfiguration;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.methoddescription.MethodDescriptions;
import org.distributedea.ontology.monitor.Statistic;
import org.distributedea.ontology.plan.Plan;
import org.distributedea.ontology.plan.RePlan;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.saveresult.resultofiteration.ResultOfIteration;
import org.distributedea.services.DataManagerService;
import org.distributedea.services.MonitorService;


/**
 * Abstract {@link PlannerInfrastructure} which is containing control mechanisms
 * for running specified {@link IPlanner}
 * @author stepan
 *
 */
public final class PlannerInfrastructure {
	
	private Agent_CentralManager centralManager;	
	private IAgentLogger logger;
	
	private IPlannerEndCondition endCondition;
	
	private History history;
	
	private long iterationNumI;
	
	
	protected final long getIterationNumber() {
		return iterationNumI;
	}
	
	/**
	 * Initialization
	 */
	public final void initialization(Agent_CentralManager agent,
			IPlannerEndCondition endCondition, JobID jobID, IAgentLogger logger) {
		if (logger == null) {
			throw new IllegalArgumentException("Argument " +
					IAgentLogger.class.getSimpleName() + " can not be null");
		}		
		if (agent == null) {
			throw new IllegalArgumentException("Argument " +
					Agent_CentralManager.class.getSimpleName() + " can not be null");
		}
		if (jobID == null || ! jobID.valid(logger)) {
			throw new IllegalArgumentException("Argument " +
					JobID.class.getSimpleName() + " is not valid");
		}
		
		this.centralManager = agent;
		this.logger = logger;
		
		this.endCondition = endCondition;
		this.history = new History(jobID);
		this.iterationNumI = 0;
	}
	
	/**
	 * Runs planning
	 */
	public final void run(IPlanner planner, JobRun jobRun,
			IslandModelConfiguration configuration) throws Exception {
		
		if (planner == null) {
			throw new IllegalArgumentException("Argument " +
					IPlanner.class.getSimpleName() + " can not be null");
		}
		if (configuration == null || ! configuration.valid(logger)) {
			throw new IllegalArgumentException("Argument " +
					IslandModelConfiguration.class.getSimpleName() + " is not valid");
		}
		if (jobRun == null || ! jobRun.valid(logger)) {
			throw new IllegalArgumentException("Argument " +
					JobRun.class.getSimpleName() + " is not valid");
		}
		
		JobID jobID = jobRun.getJobID();
		IProblem problem = jobRun.getProblem();
		
		
		Plan plan = planner.agentInitialisation(centralManager,
				endCondition.zeroIteration(), jobRun, configuration, logger);
		history.addNewPlan(plan);
		history.addNewRePlan(new RePlan(endCondition.zeroIteration()));
		
		while (endCondition.isContinue(iterationNumI)) {			
			
			// log information about re-planning
			iterationNumI++;
			Iteration iterationI = endCondition.iteration(iterationNumI);
			logger.log(Level.INFO, "Replanning: " +
					iterationI.getIterationNumber() + " / " +
					iterationI.getExpectetMaxIterationNumber());

			MethodDescriptions currentlyRunningAgents =
					history.exportRunningMethods();
			
			MonitorService.startsMonitoring(centralManager, jobID,
					problem, currentlyRunningAgents, logger);

			
			// sleep
			try {
				//int todoo;
				if (iterationNumI == 1)
					Thread.sleep(3000);
				Thread.sleep(configuration.getReplanPeriodMS());
			} catch (InterruptedException e) {
				logger.logThrowable("Error by waiting for replan", e);
			}
			
			// get and save statistic
			Statistic statistic = MonitorService.getStatistic(
					centralManager, jobID, logger);
			if (! statistic.containsAllAgentDescriptions(currentlyRunningAgents)) {
				throw new IllegalStateException(Agent_Monitor.class.getSimpleName() +
						" did not supply data from all methods");
			}
			
			// update history by new statistic
			history.addStatictic(statistic, iterationI);
			
			// replan by planner
			Pair<Plan,RePlan> rePlan;
			if (endCondition.isContinue(iterationNumI)) {		
				rePlan = planner.replan(iterationI, history);
			} else {
				rePlan = new Pair<Plan,RePlan>(new Plan(iterationI), new RePlan(iterationI));
			}
						
			// update history by new RePlan
			history.addNewPlan(rePlan.first);
			history.addNewRePlan(rePlan.second);
			
			ResultOfIteration resultOfIterationI = history.
					exportResultOfIteration(iterationI, logger);
			
			saveResultOfIteration(iterationI, resultOfIterationI, jobRun.getProblem());
		}

		// save model at the end of computation
		IndividualWrapper bestIndividualWrp =
				MonitorService.getBestIndividual(centralManager, logger);

		saveBestIdividualWrp(
				endCondition.iteration(iterationNumI), bestIndividualWrp);
		
		planner.exit(centralManager, logger);
		
	}
	
	private void saveResultOfIteration(Iteration iteration,
			ResultOfIteration resultOfIteration, IProblem problem) {
		if (resultOfIteration == null) {
			throw new IllegalArgumentException("Argument " +
					ResultOfIteration.class.getSimpleName() + " is not valid");
		}
						
		DataManagerService.saveResultOfIteration(centralManager, iteration,
				resultOfIteration, problem, logger);
	}

	private void saveBestIdividualWrp(Iteration iteration,
			IndividualWrapper bestIndividualWrp) {
		if (bestIndividualWrp == null) {
			throw new IllegalArgumentException("Argument " +
					IndividualWrapper.class.getSimpleName() + " is not valid");
		}
		
		IndividualEvaluated bestindividual =
				bestIndividualWrp.getIndividualEvaluated();

		logger.log(Level.INFO, "" + bestindividual.getFitness());
		if (bestindividual.getIndividual() instanceof IndividualPoint) {
			logger.log(Level.INFO, "" + bestindividual.getIndividual().toString());
		}
		if (bestindividual.getIndividual() instanceof IndividualArguments) {
			logger.log(Level.INFO, "" + bestindividual.getIndividual().toLogString());
		}
		
		DataManagerService.saveIndividualAsBestSolutionOfIteration(
				centralManager, iteration, bestIndividualWrp, logger);

	}
}
