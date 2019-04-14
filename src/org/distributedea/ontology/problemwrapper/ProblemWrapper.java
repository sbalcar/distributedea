package org.distributedea.ontology.problemwrapper;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.datasetdescription.IDatasetDescription;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.pedigree.Pedigree;
import org.distributedea.ontology.pedigreedefinition.PedigreeDefinition;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problemtooldefinition.ProblemToolDefinition;

import jade.content.Concept;

/**
 * Ontology represents {@link Problem} with parameters
 * @author stepan
 *
 */
public class ProblemWrapper implements Concept {

	private static final long serialVersionUID = 1L;

	/**
	 * Job identification
	 */
	private JobID jobID;
	
	
	/**
	 * Problem definition
	 */
	private IProblem problem;

	/**
	 * Problem Tool to use for solving Problem 
	 */
	private ProblemToolDefinition problemToolDefinition;

	/**
	 * Data set
	 */
	private IDatasetDescription datasetDescription;
		
	/**
	 * Specify about type of {@link Pedigree}
	 */
	private PedigreeDefinition pedigreeDefinition;

	
	/**
	 * Constructor
	 */
	public ProblemWrapper() {}
	
	/**
	 * Copy constructor
	 * @param problemWrapper
	 */
	public ProblemWrapper(ProblemWrapper problemWrapper) {
		if (problemWrapper == null || ! problemWrapper.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					ProblemWrapper.class.getSimpleName() + " is not valid");
		}
		setJobID(problemWrapper.getJobID().deepClone());
		setProblem(problemWrapper.getProblem().deepClone());
		setDatasetDescription(problemWrapper.getDatasetDescription().deepClone());
		setProblemToolDefinition(problemWrapper.getProblemToolDefinition().deepClone());		
		setPedigreeDefinition(problemWrapper.getPedigreeDefinition().deepClone());
	}
	
	/**
	 * Returns Problem IDentification
	 * @return
	 */
	public JobID getJobID() {
		return jobID;
	}
	/**
	 * set Problem IDentification
	 * @param problemID
	 */
	public void setJobID(JobID jobID) {
		this.jobID = jobID;
	}	
	
	/**
	 * Returns definition of Problem to solve
	 * @return
	 */
	public IProblem getProblem() {
		return problem;
	}

	public void setProblem(IProblem problem) {
		if (problem == null || ! problem.valid(new TrashLogger())) {
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
					IProblem.class.getSimpleName() + " is not valid");
		}

		this.datasetDescription = datasetDescription;
	}

	public ProblemToolDefinition getProblemToolDefinition() {
		return problemToolDefinition;
	}
	public void setProblemToolDefinition(ProblemToolDefinition problemToolDefinition) {
		if (problemToolDefinition == null ||
				! problemToolDefinition.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					ProblemToolDefinition.class.getSimpleName() + " is not valid");
		}
		this.problemToolDefinition = problemToolDefinition;
	}
	
	
	public PedigreeDefinition getPedigreeDefinition() {
		return pedigreeDefinition;
	}
	public void setPedigreeDefinition(PedigreeDefinition pedigreeDefinition) {
		if (pedigreeDefinition == null ||
				! pedigreeDefinition.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					PedigreeDefinition.class.getSimpleName() + " is not valid");
		}
		this.pedigreeDefinition = pedigreeDefinition;
	}

	
	/**
	 * Test validity
	 * @param logger
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		
		if (jobID == null || ! jobID.valid(logger)) {
			return false;
		}

		if (getDatasetDescription() == null ||
				! getDatasetDescription().valid(logger)) {
			return false;
		}
		return true;
	}

	public String toLogString() {
		return "jobID=" + jobID.toString() + " " +
				"problem=" + problem.toLogString() + " " +
				"problemTool=" + problemToolDefinition.toLogString();
	}
	
	/**
	 * Returns clone
	 * @return
	 */
	public ProblemWrapper deepClone() {
		return new ProblemWrapper(this);
	}
}
