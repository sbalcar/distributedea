package org.distributedea.agents.systemagents.centralmanager.behaviours;


import java.io.IOException;
import java.util.logging.Level;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.plannerinfrastructure.PlannerInfrastructure;
import org.distributedea.agents.systemagents.centralmanager.plannerinfrastructure.endcondition.IPlannerEndCondition;
import org.distributedea.agents.systemagents.centralmanager.planners.IPlanner;
import org.distributedea.agents.systemagents.datamanager.FilesystemInitTool;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.job.JobRun;

import jade.core.behaviours.OneShotBehaviour;

/**
 * Behavior of Computation Agent ensures computation of one {@link JobRun}.
 * @author stepan
 *
 */
public class ComputeJobRunBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;
	
	private JobRun jobRun;
	private IPlanner planner;
	private IPlannerEndCondition plannerEndCondition;
	
	private IAgentLogger logger;
	private Agent_CentralManager centralManager;
	
	/**
	 * Constructor
	 * @param jobRun
	 * @param planner
	 * @param plannerEndCondition
	 * @param logger
	 */
	public ComputeJobRunBehaviour(JobRun jobRun,
			IPlannerEndCondition plannerEndCondition, IPlanner planner,
			IAgentLogger logger) {

		if (logger == null) {
			throw new IllegalArgumentException("Argument " +
					IAgentLogger.class.getSimpleName() + " is not valid");
		}
		if ((jobRun == null) || (! jobRun.valid(logger))) {
			throw new IllegalArgumentException("Argument " +
					JobRun.class.getSimpleName() + " is not valid");
		}
		if (planner == null) {
			throw new IllegalArgumentException("Argument " +
					IPlanner.class.getSimpleName() + " is not valid");
		}
		if (plannerEndCondition == null) {
			throw new IllegalArgumentException("Argument " + 
					PlannerInfrastructure.class.getSimpleName() + " is not valid");
		}
		
		this.jobRun = jobRun;
		this.planner = planner;
		this.plannerEndCondition = plannerEndCondition;
		
		this.logger = logger;
	}
	
	@Override
	public void action() {
		
		if (!(myAgent instanceof Agent_CentralManager)) {
			return;
		}
		this.centralManager = (Agent_CentralManager) myAgent;
		
		logger.log(Level.INFO, "Computing JobRun behaviour: " + jobRun.getJobID());
				
		
		JobID jobID = jobRun.getJobID();
		
		FilesystemInitTool.createLogSpaceForJobRun(jobID);
		FilesystemInitTool.createResultSpaceForJobRun(jobID);
		
		// create space in filesystem
		if (jobID.getRunNumber() == 0) {			
			try {			
				FilesystemInitTool.moveInputJobToResultDir(jobID);
			} catch (IOException e) {
				logger.logThrowable("Error by moving input file " + jobID.toString(), e);
				centralManager.exit();
				return;
			}
		}
		
		PlannerInfrastructure plannerInfr = new PlannerInfrastructure();
		try {		
			plannerInfr.initialization(centralManager, plannerEndCondition, jobID, logger);
		} catch (Exception e) {
			logger.logThrowable("Error by initialization " + jobID.toString(), e);
			centralManager.exit();
			return;
		}
		
		try {
			plannerInfr.run(planner, jobRun);
		} catch (Exception e) {
			logger.logThrowable("Error by running " + jobID.toString(), e);
			e.printStackTrace();
			centralManager.exit();
		}
		
	}

}
