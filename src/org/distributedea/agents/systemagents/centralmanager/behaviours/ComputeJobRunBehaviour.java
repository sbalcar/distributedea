package org.distributedea.agents.systemagents.centralmanager.behaviours;


import java.io.IOException;
import java.util.logging.Level;

import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.planners.Planner;
import org.distributedea.agents.systemagents.centralmanager.plannertype.PlannerType;
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
	private Planner planner;
	private PlannerType plannerType;
	
	private IAgentLogger logger;
	private Agent_CentralManager centralManager;
	
	/**
	 * Constructor
	 * @param jobRun
	 * @param planner
	 * @param plannerType
	 * @param logger
	 */
	public ComputeJobRunBehaviour(JobRun jobRun, Planner planner,
			PlannerType plannerType, IAgentLogger logger) {

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
					Planner.class.getSimpleName() + " is not valid");
		}
		if (plannerType == null) {
			throw new IllegalArgumentException("Argument " + 
					PlannerType.class.getSimpleName() + " is not valid");
		}
		
		this.jobRun = jobRun;
		this.planner = planner;
		this.plannerType = plannerType;
		
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
		
		try {		
			plannerType.initialization(centralManager, jobID, logger);
		} catch (Exception e) {
			logger.logThrowable("Error by initialization " + jobID.toString(), e);
			centralManager.exit();
			return;
		}
		
		try {
			plannerType.run(planner, jobRun);
		} catch (Exception e) {
			logger.logThrowable("Error by running " + jobID.toString(), e);
			centralManager.exit();
		}
		
	}

}
