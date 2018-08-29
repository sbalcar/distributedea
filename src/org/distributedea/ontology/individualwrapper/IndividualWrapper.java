package org.distributedea.ontology.individualwrapper;

import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problemtooldefinition.ProblemToolDefinition;
import org.distributedea.problemtools.IProblemTool;

import com.thoughtworks.xstream.XStream;

import jade.content.Concept;

/**
 * Ontology for solution including a description of creator
 * and {@link Job} specification
 * @autor stepan
 * 
 */
public class IndividualWrapper implements Concept {

	private static final long serialVersionUID = 1L;

	private JobID jobID;
	
	private MethodDescription methodDescription;
	
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
		setMethodDescription(agentDescription);
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
		methodDescription = individualWrp.getMethodDescription().deepClone();
		individualEvaluated = individualWrp.getIndividualEvaluated().deepClone();
	}
	
	public JobID getJobID() {
		return jobID;
	}
	@Deprecated
	public void setJobID(JobID jobID) {
		this.jobID = jobID;
	}

	public MethodDescription getMethodDescription() {
		return methodDescription;
	}
	@Deprecated
	public void setMethodDescription(MethodDescription methodDescription) {
		this.methodDescription = methodDescription;
	}
	
	public AgentConfiguration exportAgentConfiguration() {
		return methodDescription.getAgentConfiguration();
	}
	public MethodDescription exportAgentDescriptionClone() {
		return methodDescription.deepClone();
	}
	
	/**
	 * Exports {@link IProblemTool} class
	 * @return
	 */
	public ProblemToolDefinition exportProblemToolDefinition() {
		return methodDescription.getProblemToolDefinition();
	}
	
	public IndividualEvaluated getIndividualEvaluated() {
		return individualEvaluated;
	}
	@Deprecated
	public void setIndividualEvaluated(IndividualEvaluated individualEvaluated) {
		this.individualEvaluated = individualEvaluated;
	}
	
	
	public boolean validation(IProblem problem, Dataset dataset,
			IProblemTool problemTool, IAgentLogger logger) {
		
		if (dataset == null) {
			return false;
		}
		
		return individualEvaluated.validation(problem, dataset, problemTool, logger);	
	}
	
	/**
	 * Test validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		
		if (jobID == null || (! jobID.valid(logger))) {
			return false;
		}
		if (methodDescription == null ||
				(! methodDescription.valid(logger))) {
			return false;
		}
		if (individualEvaluated == null ||
				(! individualEvaluated.valid(logger))) {
			return false;
		}
		return true;
	}
	
	/**
	 * Test quickly validity - doesn't test integrity of the {@link Individual}
	 * @return
	 */
	public boolean validQuickly(IAgentLogger logger) {
		
		if (jobID == null || (! jobID.valid(logger))) {
			return false;
		}
		if (methodDescription == null ||
				(! methodDescription.valid(logger))) {
			return false;
		}
		if (individualEvaluated == null ||
				(! individualEvaluated.validQuickly(logger))) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * Exports to the XML String
	 */
	public String exportXML() {

		XStream xstream = new XStream();
		xstream.setMode(XStream.NO_REFERENCES);

		return xstream.toXML(this);
	}

	/**
	 * Import the {@link JobID} from the String
	 */
	public static IndividualWrapper importXML(String xml) {

		XStream xstream = new XStream();
		xstream.setMode(XStream.NO_REFERENCES);

		return (IndividualWrapper) xstream.fromXML(xml);
	}
	
	/**
	 * Returns clone
	 * @return
	 */
	public IndividualWrapper deepClone() {
		return new IndividualWrapper(this);
	}
}
