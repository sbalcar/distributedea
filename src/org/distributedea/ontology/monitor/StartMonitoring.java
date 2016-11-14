package org.distributedea.ontology.monitor;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.methoddescription.MethodDescriptions;

import jade.content.AgentAction;

/**
 * Ontology represents request for start monitoring
 * @author stepan
 *
 */
public class StartMonitoring implements AgentAction {

	private static final long serialVersionUID = 1L;
	
	private JobID jobID;
	private String problemToSolveClassName;
	private MethodDescriptions agentsToMonitor;
	
	
	@Deprecated
	public StartMonitoring() {} // only for Jade
	
	/**
	 * Constructor
	 * @param jobID
	 */
	public StartMonitoring(JobID jobID, Class<?> problemToSolveClass,
			MethodDescriptions agentDescriptions) {
		
		if (jobID == null || ! jobID.valid(new TrashLogger())) {
			throw new IllegalArgumentException();
		}
		if (problemToSolveClass == null) {
			throw new IllegalArgumentException();
		}
		if (agentDescriptions == null ||
				! agentDescriptions.valid(new TrashLogger())) {
			throw new IllegalArgumentException();
		}
		
		this.jobID = jobID;
		importProblemToSolveClass(problemToSolveClass);
		this.agentsToMonitor = agentDescriptions;
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
			throw new IllegalArgumentException();
		}
		this.jobID = jobID;
	}
	
	@Deprecated
	public String getProblemToSolveClassName() {
		return problemToSolveClassName;
	}
	@Deprecated
	public void setProblemToSolveClassName(String problemToSolveClassName) {
		this.problemToSolveClassName = problemToSolveClassName;
	}
	
	public Class<?> exportProblemToSolveClass() {		
		try {
			return Class.forName(problemToSolveClassName);
		} catch (ClassNotFoundException e) {
		}
		return null;
	}
	public void importProblemToSolveClass(Class<?> problemToSolveClass) {
		if (problemToSolveClass == null) {
			throw new IllegalArgumentException();
		}
		this.problemToSolveClassName = problemToSolveClass.getName();
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
}
