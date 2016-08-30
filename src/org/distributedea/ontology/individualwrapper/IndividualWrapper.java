package org.distributedea.ontology.individualwrapper;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.problems.IProblemTool;

import jade.content.Concept;

/**
 * Ontology for solution including a description of creator
 * and {@link Job} specification
 * @autor stepan
 * 
 */
public class IndividualWrapper implements Concept {

	private static final long serialVersionUID = -3883073526379640439L;

	private JobID jobID;
	
	private AgentDescription agentDescription;
	
	private IndividualEvaluated individualEvaluated;

	@Deprecated
	public IndividualWrapper() {} // only for Jade
	
	/**
	 * Constructor
	 * @param jobID
	 * @param agentDescription
	 * @param individualEvaluated
	 */
	public IndividualWrapper(JobID jobID, AgentDescription agentDescription,
			IndividualEvaluated individualEvaluated) {
		setJobID(jobID);
		setAgentDescription(agentDescription);
		setIndividualEvaluated(individualEvaluated);
	}
	
	/**
	 * Copy constructor
	 * @param individualWrp
	 */
	public IndividualWrapper(IndividualWrapper individualWrp) {
		if (individualWrp == null || ! individualWrp.valid(new TrashLogger())) {
			throw new IllegalArgumentException();
		}
			
		jobID = individualWrp.getJobID().deepClone();
		agentDescription = individualWrp.getAgentDescription().deepClone();
		individualEvaluated = individualWrp.getIndividualEvaluated().deepClone();
	}
	
	public JobID getJobID() {
		return jobID;
	}
	@Deprecated
	public void setJobID(JobID jobID) {
		this.jobID = jobID;
	}

	public AgentDescription getAgentDescription() {
		return agentDescription;
	}
	@Deprecated
	public void setAgentDescription(AgentDescription agentDescription) {
		this.agentDescription = agentDescription;
	}
	
	public AgentConfiguration exportAgentConfiguration() {
		return agentDescription.getAgentConfiguration();
	}
	public AgentDescription exportAgentDescriptionClone() {
		return agentDescription.deepClone();
	}
	
	/**
	 * Exports {@link IProblemTool} class
	 * @return
	 */
	public Class<?> exportProblemToolClass() {
		return agentDescription.exportProblemToolClass();
	}
	
	public IndividualEvaluated getIndividualEvaluated() {
		return individualEvaluated;
	}
	@Deprecated
	public void setIndividualEvaluated(IndividualEvaluated individualEvaluated) {
		this.individualEvaluated = individualEvaluated;
	}
	
	
	public boolean validation(Problem problem, IProblemTool problemTool, IAgentLogger logger) {
		
		if (problem == null) {
			return false;
		}
		
		return individualEvaluated.validation(problem, problemTool, logger);	
	}
	
	/**
	 * Test validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		
		if (jobID == null || ! jobID.valid(logger)) {
			return false;
		}
		if (agentDescription == null || ! agentDescription.valid(logger)) {
			return false;
		}
		if (individualEvaluated == null || ! individualEvaluated.valid(logger)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Returns clone
	 * @return
	 */
	public IndividualWrapper deepClone() {
		return new IndividualWrapper(this);
	}
}
