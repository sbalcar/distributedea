package org.distributedea.ontology.job;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import jade.content.Concept;

import org.distributedea.javaextension.Pair;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.datasetdescription.IDatasetDescription;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescription;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescriptions;
import org.distributedea.ontology.pedigree.Pedigree;
import org.distributedea.ontology.pedigreedefinition.PedigreeDefinition;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problemtooldefinition.ProblemToolDefinition;
import org.distributedea.ontology.problemwrapper.ProblemWrapper;

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
	private InputMethodDescriptions methods;
	
	/**
	 * Inform about type of Problem to solve
	 */
	private IProblem problem;
	
	/**
	 * Defines the filename with the input {@link Dataset}
	 */
	private IDatasetDescription datasetDescription;

	/**
	 * Specify about type of {@link Pedigree}
	 */
	private PedigreeDefinition pedigreeDefinition;

	

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
		InputMethodDescriptions methodsClone =
				jobRun.getMethods().deepClone();
		IProblem problemClone =
				jobRun.getProblem().deepClone();
		PedigreeDefinition pedigreeDefinitionClone =
				jobRun.getPedigreeDefinition();
		
		setJobID(jobIDClone);
		setMethods(methodsClone);
		setProblem(problemClone);
		setPedigreeDefinition(pedigreeDefinitionClone);
		
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
	
	
	public InputMethodDescriptions getMethods() {
		return methods;
	}
	public void setMethods(InputMethodDescriptions methods) {
		if (methods == null || ! methods.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					InputMethodDescriptions.class.getSimpleName() + " is not valid");
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
	

	
	public PedigreeDefinition getPedigreeDefinition() {
		return pedigreeDefinition;
	}

	public void setPedigreeDefinition(PedigreeDefinition pedigreeDefinition) {
		if (pedigreeDefinition == null || ! pedigreeDefinition.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					PedigreeDefinition.class.getSimpleName() + " is not valid");
		}
		this.pedigreeDefinition = pedigreeDefinition;
	}


	public ProblemWrapper exportProblemWrapper(ProblemToolDefinition problemToolDef) {
				
		ProblemWrapper problemWrp = new ProblemWrapper();
		problemWrp.setJobID(getJobID().deepClone());
		problemWrp.setProblem(getProblem().deepClone());
		problemWrp.setProblemToolDefinition(problemToolDef);
		problemWrp.setDatasetDescription(getDatasetDescription().deepClone());
		problemWrp.setPedigreeDefinition(getPedigreeDefinition().deepClone());
		
		return problemWrp;
	}

	public Pair<InputMethodDescription,ProblemWrapper> exportProblemWrapper(int problemIndex) {
		
		InputMethodDescription methodI = getMethods().get(problemIndex);
		
		ProblemWrapper problemWrp = new ProblemWrapper();
		problemWrp.setJobID(getJobID().deepClone());
		problemWrp.setProblem(getProblem().deepClone());
		problemWrp.setProblemToolDefinition(
				methodI.getProblemToolDefinition().deepClone());
		problemWrp.setDatasetDescription(getDatasetDescription().deepClone());
		problemWrp.setPedigreeDefinition(getPedigreeDefinition().deepClone());
		
		return new Pair<InputMethodDescription,ProblemWrapper>(methodI, problemWrp);
	}
	
	public List<Pair<InputMethodDescription,ProblemWrapper>> exportProblemWrappers() {
		
		List<Pair<InputMethodDescription,ProblemWrapper>> prbWrpsList =
				new ArrayList<Pair<InputMethodDescription,ProblemWrapper>>();
		
		for (int metodIndex = 0; metodIndex < getMethods().size(); metodIndex++) {
			
			prbWrpsList.add(exportProblemWrapper(metodIndex));
		}
		
		return prbWrpsList;
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
			logger.log(Level.WARNING, InputMethodDescriptions.class.getSimpleName() + " are not valid");
			return false;
		}
		
		if (pedigreeDefinition == null || ! pedigreeDefinition.valid(logger)) {
			logger.log(Level.WARNING, PedigreeDefinition.class.getSimpleName() + " is not valid");
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
