package org.distributedea.agents.systemagents.centralmanager.plannerinfrastructure;

import java.util.logging.Level;

import org.distributedea.InputConfiguration;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.Agent_Monitor;
import org.distributedea.agents.systemagents.centralmanager.plannerinfrastructure.endcondition.IPlannerEndCondition;
import org.distributedea.agents.systemagents.centralmanager.planners.IPlanner;
import org.distributedea.agents.systemagents.centralmanager.structures.history.History;
import org.distributedea.javaextension.Pair;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.methoddescription.MethodDescriptions;
import org.distributedea.ontology.monitor.Statistic;
import org.distributedea.ontology.plan.Plan;
import org.distributedea.ontology.plan.RePlan;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.saveresult.ResultOfIteration;
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
	public final void run(IPlanner planner, JobRun jobRun) throws Exception {
		
		if (planner == null) {
			throw new IllegalArgumentException("Argument " +
					IPlanner.class.getSimpleName() + " can not be null");
		}
		if (jobRun == null || ! jobRun.valid(logger)) {
			throw new IllegalArgumentException("Argument " +
					JobRun.class.getSimpleName() + " is not valid");
		}
		
		JobID jobID = jobRun.getJobID();
		IProblem problem = jobRun.getProblemDefinition();
		
		
		Plan plan = planner.agentInitialisation(centralManager,
				endCondition.zeroIteration(), jobRun, logger);
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
				Thread.sleep(InputConfiguration.getConf().REPLAN_PERIOD_MS);
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
			
			IndividualWrapper bestIndividualWrapper =
					statistic.exportBestIndividualWrapper();
			
			// update history by new statistic
			history.addStatictic(statistic, iterationI);
			
			// replan by planner
			Pair<Plan,RePlan> rePlan =
					planner.replan(iterationI, history);
						
			// update history by new RePlan
			history.addNewPlan(rePlan.first);
			history.addNewRePlan(rePlan.second);
			
			ResultOfIteration resultOfIterationI = history.
					exportResultOfIteration(iterationI, logger);
			saveResult(iterationI, bestIndividualWrapper,
					resultOfIterationI);
		}
		
		planner.exit(centralManager, logger);
		
	}
	
	private void saveResult(Iteration iteration,
			IndividualWrapper bestIndividualWrapper,
			ResultOfIteration resultOfIteration) {
				
		if (bestIndividualWrapper != null) {
			
			IndividualEvaluated bestindividual = bestIndividualWrapper.
					getIndividualEvaluated();

			logger.log(Level.INFO, "" + bestindividual.getFitness());
			if (bestindividual.getIndividual() instanceof IndividualPoint) {
				logger.log(Level.INFO, "" + bestindividual.getIndividual().toString());
			}
		}
		
		DataManagerService.saveIndividualAsBestSolutionOfIteration(
				centralManager, iteration, bestIndividualWrapper, logger);
		
		DataManagerService.saveResultOfIteration(centralManager, iteration,
				resultOfIteration, logger);
	}
	
}