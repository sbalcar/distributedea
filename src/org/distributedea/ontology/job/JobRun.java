package org.distributedea.ontology.job;

import java.util.logging.Level;

import jade.content.Concept;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.datasetdescription.IDatasetDescription;
import org.distributedea.ontology.method.IMethods;
import org.distributedea.ontology.pedigree.Pedigree;
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
	 * Methods
	 */
	private IMethods methods;
	
	/**
	 * Inform about type of Problem to solve
	 */
	private IProblem problem;
	
	/**
	 * Defines the filename with the input {@link Dataset}
	 */
	private IDatasetDescription datasetDescription;

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
		IMethods methodsClone =
				jobRun.getMethods().deepClone();
		IProblem problemClone =
				jobRun.getProblem().deepClone();
		Dataset datasetClone =
				jobRun.getDataset().deepClone();
		
		setJobID(jobIDClone);
		setMethods(methodsClone);
		setProblem(problemClone);
		setDataset(datasetClone);
		setPedigreeOfIndividualClassName(
				jobRun.getPedigreeOfIndividualClassName());
		
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
	 * Returns {@link IProblem} to solve
	 * @return
	 */
	public IProblem getProblem() {
		return problem;
	}
	public void setProblem(IProblem problem) {
		if (problem == null ||
				! problem.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					IProblem.class.getSimpleName() + " is not valid");
		}
		this.problem = problem;
	}
	
	

	public IDatasetDescription getDatasetDescription() {
		return datasetDescription;
	}

	public void setDatasetDescription(IDatasetDescription datasetDescription) {
		if (datasetDescription == null ||
				! datasetDescription.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					IDatasetDescription.class.getSimpleName() + " is not valid");
		}
		this.datasetDescription = datasetDescription;
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
	 * Exports the {@link JobRun} as {@link ProblemStruct} which
	 * contains ProblemTool in argument
	 * @param problemToolClass
	 * @return
	 */
	public ProblemStruct exportProblemStruct(Class<?> problemToolClass) {
		
		JobID jobIDClone = getJobID().deepClone();
		
		IProblem problemClone = getProblem().deepClone();
		Dataset datasetClone = getDataset().deepClone();
		IDatasetDescription datasetDescrClone =
				getDatasetDescription().deepClone();
		
		ProblemStruct problemStruct = new ProblemStruct();
		problemStruct.setJobID(jobIDClone);
		problemStruct.setProblem(problemClone);
		problemStruct.setDatasetDescription(datasetDescrClone);
		problemStruct.setDataset(datasetClone);
		problemStruct.importProblemToolClass(problemToolClass);
		problemStruct.importPedigreeOfIndividualClassName(exportPedigreeOfIndividual(new TrashLogger()));
		
		return problemStruct;
	}
	
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
			logger.log(Level.WARNING, IMethods.class.getSimpleName() + " are not valid");
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
