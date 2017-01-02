package org.distributedea.ontology.monitor;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.methoddescription.MethodDescriptions;
import org.distributedea.ontology.problemdefinition.IProblemDefinition;
import org.distributedea.ontology.problemdefinition.AProblemDefinition;

import jade.content.AgentAction;

/**
 * Ontology represents request for start monitoring
 * @author stepan
 *
 */
public class StartMonitoring implements AgentAction {

	private static final long serialVersionUID = 1L;
	
	private JobID jobID;
	private IProblemDefinition problemToSolve;
	private MethodDescriptions agentsToMonitor;
	
	
	@Deprecated
	public StartMonitoring() {} // only for Jade
	
	/**
	 * Constructor
	 * @param jobID
	 */
	public StartMonitoring(JobID jobID, IProblemDefinition problemToSolve,
			MethodDescriptions agentDescriptions) {
		setJobID(jobID);
		setProblemToSolve(problemToSolve);
		setAgentsToMonitor(agentDescriptions);
	}
	
	/**
	 * Copy constructor
	 * @param startMonitoring
	 */
	public StartMonitoring(StartMonitoring startMonitoring) {
		if (startMonitoring == null || ! startMonitoring.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					StartMonitoring.class.getSimpleName() + " is not valid");
		}
		JobID jobIDClone = startMonitoring.getJobID().deepClone();
		AProblemDefinition problemToSolveClone =
				startMonitoring.getProblemToSolve().deepClone();
		MethodDescriptions agentDescriptionsClone =
				startMonitoring.getAgentsToMonitor().deepClone();
		
		setJobID(jobIDClone);
		setProblemToSolve(problemToSolveClone);
		setAgentsToMonitor(agentDescriptionsClone);
		
	}
	/**
	 * Returns {@link JobRun} specification. 
	 * @return
	 */
	public JobID getJobID() {
		return jobID;
	}
	@Deprecated
	public void setJobID(JobID jobID) {
		if (jobID == null || ! jobID.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					JobID.class.getSimpleName() + " is not valid");
		}
		this.jobID = jobID;
	}
	
	/**
	 * Returns Problem to solve
	 * @return
	 */
	public IProblemDefinition getProblemToSolve() {
		return problemToSolve;
	}
	@Deprecated
	public void setProblemToSolve(IProblemDefinition problemToSolve) {
		if (problemToSolve == null || ! problemToSolve.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					AProblemDefinition.class.getSimpleName() + " is not valid");
		}
		this.problemToSolve = problemToSolve;
	}	
	
	/**
	 * Returns agents to monitor
	 * @return
	 */
	public MethodDescriptions getAgentsToMonitor() {
		return agentsToMonitor;
	}
	@Deprecated
	public void setAgentsToMonitor(MethodDescriptions agentsToMonitor) {
		if (agentsToMonitor == null || ! agentsToMonitor.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodDescriptions.class.getSimpleName() + " is not valid");
		}
		this.agentsToMonitor = agentsToMonitor;
	}

	/**
	 * Tests validation
	 * @param logger
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (jobID == null || ! jobID.valid(logger)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Returns clone
	 * @return
	 */
	public StartMonitoring deepClone() {
		
		return new StartMonitoring(this);
	}
}
