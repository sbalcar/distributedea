package org.distributedea.agents.systemagents.centralmanager.plannertype;

import java.util.logging.Level;

import org.distributedea.Configuration;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.planner.Planner;
import org.distributedea.agents.systemagents.centralmanager.planner.history.History;
import org.distributedea.agents.systemagents.centralmanager.planner.modes.Iteration;
import org.distributedea.agents.systemagents.centralmanager.planner.plan.Plan;
import org.distributedea.agents.systemagents.centralmanager.planner.plan.RePlan;
import org.distributedea.agents.systemagents.centralmanager.planner.tool.Pair;
import org.distributedea.agents.systemagents.centralmanager.planner.tool.PlannerException;
import org.distributedea.agents.systemagents.datamanager.DataManagerService;
import org.distributedea.agents.systemagents.datamanager.FilesystemTool;
import org.distributedea.agents.systemagents.monitor.MonitorService;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.monitor.Statistic;
import org.distributedea.ontology.saveresult.ResultOfIteration;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

public abstract class PlannerType implements IPlannerType {
	
	private Agent_CentralManager centralManager;
	private History history;
	
	protected IAgentLogger logger;
	
	@XStreamOmitField
	private long iterationNumI;
		
	protected abstract boolean isContinue();
	
	protected abstract Iteration zeroIteration();
	protected abstract Iteration iteration();
	
	protected final long getIterationNumber() {
		return iterationNumI;
	}
	
	public final void initialization(Agent_CentralManager agent,
			JobID jobID, IAgentLogger logger) {
		
		this.centralManager = agent;
		this.history = new History(jobID);
		this.logger = logger;
		this.iterationNumI = 0;
	}
	
	public final void run(Planner planner, JobRun jobRun) throws PlannerException {
		
		// tests if plannerType was initialised
		if (centralManager == null || logger == null) {
			throw new PlannerException();
		}
		
		JobID jobID = jobRun.getJobID();
		
		// creating space in filesystem
		FilesystemTool.createLogSpaceForJobRun(jobID);
		
		if (jobID.getRunNumber() == 0) {
			FilesystemTool.moveInputJobToResultDir(jobID);
		}
		
		Plan plan = planner.agentInitialisation(centralManager,
				zeroIteration(), jobRun, logger);
		history.addNewPlan(plan);
		
		while (isContinue()) {			
			
			// log information about re-planning
			iterationNumI++;
			Iteration iterationStructI = iteration();
			
			// sleep
			try {
				Thread.sleep(Configuration.REPLAN_PERIOD_MS);
			} catch (InterruptedException e) {
				logger.logThrowable("Error by waiting for replan", e);
			}

			// re-planning
			// get and save statistic
			Statistic statistic = MonitorService.sendGetStatistic(
					centralManager, logger);
			
			IndividualWrapper bestIndividualWrapper =
					statistic.exportBestIndividualWrapper();
			
			// update history by new statistic
			history.addStatictic(statistic, iterationStructI);
			
			ResultOfIteration resultOfIterationI = history.
					exportResultOfIteration(iterationStructI);
			saveResult(iterationStructI, bestIndividualWrapper,
					resultOfIterationI);
			
			// replan by planner
			Pair<Plan,RePlan> rePlan = planner.replan(iterationStructI, history);
			
			// update history by new RePlan
			history.addNewPlan(rePlan.first);
			history.addNewRePlan(rePlan.second);
			
//			String monitoringDirName = Configuration.getResultDirectoryMonitoringDirectory(jobID);
//			try {
//				history.exportToXML(new File(monitoringDirName));
//			} catch (FileNotFoundException | JAXBException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
		}
		
		planner.exit(centralManager, logger);
		
	}
	
	private void saveResult(Iteration iteration, IndividualWrapper bestIndividualWrapper, ResultOfIteration resultOfIteration) {
				
		if (bestIndividualWrapper != null) {
			
			IndividualEvaluated bestindividual = bestIndividualWrapper.
					getIndividualEvaluated();

			logger.log(Level.INFO, "" + bestindividual.getFitness());
		}
		
		DataManagerService.sendSaveBestIndividual(centralManager, iteration,
				bestIndividualWrapper, logger);
		
		DataManagerService.sendResultOfIteration(centralManager, iteration,
				resultOfIteration, logger);
	}
	
}
