package org.distributedea.ontology.job;

import java.util.logging.Level;

import jade.content.Concept;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.configuration.AgentConfigurations;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.method.IMethods;
import org.distributedea.ontology.method.Methods;
import org.distributedea.ontology.pedigree.Pedigree;
import org.distributedea.ontology.problem.AProblem;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problemwrapper.ProblemStruct;
import org.distributedea.problems.IProblemTool;

/**
 * Ontology represents one run of {@link Job}
 * @author stepan
 *
 */
public class JobRun implements Concept {

	private static final long serialVersionUID = 1L;

	/**
	 * JobRun identification
	 */
	private JobID jobID;
	
	/**
	 * Turns on broadcast computed individuals to distributed agents
	 */
	private boolean individualDistribution;
	

	/**
	 * Methods
	 */
	private IMethods methods;
	
	/**
	 * Inform about type of Problem to solve
	 */
	private IProblem problemToSolveDefinition;
	
	/**
	 * Dataset
	 */
	private Dataset dataset;

	/**
	 * Specify about type of {@link Pedigree}
	 */
	private String pedigreeOfIndividualClassName;

	

	/**
	 * Constructor
	 */
	public JobRun() {}
	
	/**
	 * Copy Constructor
	 * @param jobRun
	 */
	public JobRun(JobRun jobRun) {
		
		if(jobRun == null || ! jobRun.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Given " +
					JobRun.class.getSimpleName() + " is null or invalid");
		}
		
		JobID jobIDClone = jobRun.getJobID().deepClone();
		boolean individualDistributionClone =
				jobRun.getIndividualDistribution();
		IMethods methodsClone =
				jobRun.getMethods().deepClone();
		Dataset datasetClone =
				jobRun.getDataset().deepClone();
		
		setJobID(jobIDClone);
		setIndividualDistribution(individualDistributionClone);
		setMethods(methodsClone);
		setDataset(datasetClone);
		
	}
	
	/**
	 * Returns {@link JobID} identification
	 * @return
	 */
	public JobID getJobID() {
		return jobID;
	}
	public void setJobID(JobID jobID) {
		if (jobID == null || ! jobID.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					JobID.class.getSimpleName() + " is not valid");
		}
		this.jobID = jobID;
	}
	
	
	public IMethods getMethods() {
		return methods;
	}

	public void setMethods(IMethods methods) {
		if (methods == null || ! methods.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					IMethods.class.getSimpleName() + " is not valid");
		}
		this.methods = methods;
	}
	
	/**
	 * Returns flag decides about distribution of {@link IndividualWrapper}s
	 * @return
	 */
	public boolean getIndividualDistribution() {
		return individualDistribution;
	}
	public void setIndividualDistribution(boolean individualDistribution) {
		this.individualDistribution = individualDistribution;
	}
	
	/**
	 * Returns {@link IProblem} to solve
	 * @return
	 */
	public IProblem getProblemDefinition() {
		return problemToSolveDefinition;
	}
	public void setProblemDefinition(
			IProblem problemToSolveDefinition) {
		if (problemToSolveDefinition == null ||
				! problemToSolveDefinition.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					IProblem.class.getSimpleName() + " is not valid");
		}
		this.problemToSolveDefinition = problemToSolveDefinition;
	}

	/**
	 * Returns {@link Problem} to solve
	 * @return
	 */
	public Dataset getDataset() {
		return dataset;
	}
	public void setDataset(Dataset dataset) {
		if (dataset == null || ! dataset.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Dataset.class.getSimpleName() + " is not valid");
		}
		this.dataset = dataset;
	}
	
	@Deprecated	
	public String getPedigreeOfIndividualClassName() {
		return pedigreeOfIndividualClassName;
	}
	@Deprecated
	public void setPedigreeOfIndividualClassName(String hisotyOfIndividualClassName) {
		this.pedigreeOfIndividualClassName = hisotyOfIndividualClassName;
	}

	/**
	 * Exports {@link IProblemTool} class
	 * @param logger
	 * @return
	 */
	public Class<?> exportPedigreeOfIndividual(IAgentLogger logger) {
		
		try {
			return Class.forName(pedigreeOfIndividualClassName);
		} catch (Exception e) {
		}
		return null;
	}
	/**
	 * Import {@link IProblemTool} class
	 * @param problemToSolve
	 */
	public void importPedigreeOfIndividualClassName(Class<?> pedigreeOfIndividual) {
		
		if (pedigreeOfIndividual == null) {
			this.pedigreeOfIndividualClassName = null;
			return;
		}
		this.pedigreeOfIndividualClassName = pedigreeOfIndividual.getName();
	}
	
	
	/**
	 * Exports the {@link Methods}
	 * @return
	 */
/*
	public InputMethodDescriptions exportInputAgentDescriptions() {
		return new InputMethodDescriptions(
				getAgentConfigurations(),getProblemTools());
	}
*/
	int todo;
	
	/**
	 * Exports the {@link JobRun} as {@link ProblemStruct} which
	 * contains ProblemTool in argument
	 * @param problemToolClass
	 * @return
	 */
	public ProblemStruct exportProblemStruct(Class<?> problemToolClass) {
		
		JobID jobIDClone = getJobID().deepClone();
		
		AProblem problemClone = getProblemDefinition().deepClone();
		Dataset datasetClone = getDataset().deepClone();
		
		ProblemStruct problemStruct = new ProblemStruct();
		problemStruct.setJobID(jobIDClone);
		problemStruct.setIndividualDistribution(getIndividualDistribution());
		problemStruct.setProblem(problemClone);
		problemStruct.setDataset(datasetClone);
		problemStruct.importProblemToolClass(problemToolClass);
		problemStruct.importPedigreeOfIndividualClassName(exportPedigreeOfIndividual(new TrashLogger()));
		
		return problemStruct;
	}
	
	/**
	 * Export random selected {@link MethodDescription}
	 * @return
	 */
/*
	public InputMethodDescription exportRandomSelectedAgentDescription() {
		
		if (agentConfigurations == null ||
				agentConfigurations.exportNumberOfAgentConfigurations() == 0 ||
				problemTools == null ||
				problemTools.exportNumberOfProblemTools() == 0) {
			return null;
		}
		
		InputAgentConfiguration agentToCreate =
				agentConfigurations.exportRandomAgentConfiguration();
		Class<?> problemToolClass =
				problemTools.exportRandomSelectedProblemTool();
		
		if (agentToCreate == null || problemToolClass == null) {
			return null;
		}
		
		InputMethodDescription methodToCreate =
				new InputMethodDescription(agentToCreate, problemToolClass);
		return methodToCreate;
	}
*/	
	/**
	 * Tests validation of {@link JobRun}
	 * @param logger
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		
		if (jobID == null || ! jobID.valid(logger)) {
			logger.log(Level.WARNING, JobID.class.getSimpleName() + " is not valid");
			return false;
		}
		
		if (methods == null || ! methods.valid(logger)) {
			logger.log(Level.WARNING, AgentConfigurations.class.getSimpleName() + " are not valid");
			return false;
		}
		
		if (dataset == null || ! dataset.valid(logger)) {
			logger.log(Level.WARNING, Dataset.class.getSimpleName() + " is not valid");
			return false;
		}
				
		return true;
	}
	
	/**
	 * Returns clone
	 */
	public JobRun deepClone() {
		return new JobRun(this);
	}
}