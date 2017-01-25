package org.distributedea.ontology.individualwrapper;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.problemdefinition.IProblemDefinition;
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
	
	private MethodDescription agentDescription;
	
	private IndividualEvaluated individualEvaluated;

	@Deprecated
	public IndividualWrapper() {} // only for Jade
	
	/**
	 * Constructor
	 * @param jobID
	 * @param agentDescription
	 * @param individualEvaluated
	 */
	public IndividualWrapper(JobID jobID, MethodDescription agentDescription,
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

	public MethodDescription getAgentDescription() {
		return agentDescription;
	}
	@Deprecated
	public void setAgentDescription(MethodDescription agentDescription) {
		this.agentDescription = agentDescription;
	}
	
	public AgentConfiguration exportAgentConfiguration() {
		return agentDescription.getAgentConfiguration();
	}
	public MethodDescription exportAgentDescriptionClone() {
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
	
	
	public boolean validation(IProblemDefinition problemDef, Dataset dataset,
			IProblemTool problemTool, IAgentLogger logger) {
		
		if (dataset == null) {
			return false;
		}
		
		return individualEvaluated.validation(problemDef, dataset, problemTool, logger);	
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
