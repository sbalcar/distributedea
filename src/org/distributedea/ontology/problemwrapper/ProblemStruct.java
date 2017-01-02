package org.distributedea.ontology.problemwrapper;

import java.io.File;

import jade.content.Concept;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.pedigree.Pedigree;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problemdefinition.IProblemDefinition;
import org.distributedea.problems.IProblemTool;
import org.distributedea.problems.ProblemTool;

/**
 * Ontology represents problem to solve with identification, parameters
 * and tools 
 * @author stepan
 *
 */
public class ProblemStruct implements Concept {

	private static final long serialVersionUID = 1L;

	/**
	 * Job identification
	 */
	private JobID jobID;
	
	/**
	 * Turns on broadcast computed individuals to distributed agents
	 */
	private boolean individualDistribution;
	
	/**
	 * Problem Tool to use for solving Problem 
	 */
	private String problemToolClass;
	
	/**
	 * Problem to solve definition
	 */
	private IProblemDefinition problemToSolveDefinition;
	
	/**
	 * Problem to solve
	 */
	private Problem problem;

	/**
	 * Specify about type of {@link Pedigree}
	 */
	private String pedigreeOfIndividualClassName;
	
	
	
	/**
	 * Constructor
	 */
	public ProblemStruct() {}
	
	/**
	 * Copy Constructor
	 * @param problemStruct
	 */
	public ProblemStruct(ProblemStruct problemStruct) {
		
		if (problemStruct == null || ! problemStruct.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					ProblemStruct.class.getSimpleName() + " is not valid");
		}
		
		JobID jobIDClone = problemStruct.getJobID().deepClone();
		boolean individualDistributionClone =
				problemStruct.getIndividualDistribution();
		Class<?> problemToolClassClone =
				problemStruct.exportProblemToolClass(new TrashLogger());
		IProblemDefinition problemDefinitionClone =
				problemStruct.getProblemDefinition().deepClone();
		Problem problemClone = problemStruct.getProblem().deepClone();
		Class<?> pedigreeOfIndividualClassClone = 
				problemStruct.exportPedigreeOfIndividual(new TrashLogger());
		
		
		this.setJobID(jobIDClone);
		this.setIndividualDistribution(individualDistributionClone);
		this.importProblemToolClass(problemToolClassClone);
		this.setProblemDefinition(problemDefinitionClone);
		this.setProblem(problemClone);
		this.importPedigreeOfIndividualClassName(pedigreeOfIndividualClassClone);
	}
	
	/**
	 * Returns {@link JobID} identification
	 * @return
	 */
	public JobID getJobID() {
		return jobID;
	}
	public void setJobID(JobID jobID) {
		this.jobID = jobID;
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
	
	@Deprecated
	public String getProblemToolClass() {
		return problemToolClass;
	}
	@Deprecated
	public void setProblemToolClass(String problemToolClass) {
		this.problemToolClass = problemToolClass;
	}
	/**
	 * Export {@link IProblemTool} class
	 * @param logger
	 * @return
	 */
	public Class<?> exportProblemToolClass(IAgentLogger logger) {
		
		try {
			return Class.forName(getProblemToolClass());
		} catch (ClassNotFoundException e) {
			logger.logThrowable("Can not find class for ProblemToll", e);
		}
		return null;
	}
	/**
	 * Export {@link IProblemTool}
	 * @param logger
	 * @return
	 */
	public IProblemTool exportProblemTool(IAgentLogger logger) {
		
		Class<?> probToolclass = exportProblemToolClass(logger);
		IProblemTool problemTool = null;
		try {
			problemTool = (IProblemTool) probToolclass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			logger.logThrowable("Can't create ProblemTool", e);
		}
		
		return problemTool;
	}
	/**
	 * Import {@link IProblemTool} class
	 * @param problemToolClass
	 */
	public void importProblemToolClass(Class<?> problemToolClass) {
		if (problemToolClass == null) {
			return;
		}
		this.problemToolClass = problemToolClass.getName();
	}
	
	
	/**
	 * Returns problem to solve definition
	 * @return
	 */
	public IProblemDefinition getProblemDefinition() {
		return problemToSolveDefinition;
	}

	public void setProblemDefinition(
			IProblemDefinition problemToSolveDefinition) {
		if (problemToSolveDefinition == null ||
				! problemToSolveDefinition.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					IProblemDefinition.class.getSimpleName() + " is not valid");
		}
		this.problemToSolveDefinition = problemToSolveDefinition;
	}

	/**
	 * Returns {@link Problem} to solve
	 * @return
	 */
	public Problem getProblem() {
		return problem;
	}
	public void setProblem(Problem problem) {
		if (problem == null ||
				! problem.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Problem.class.getSimpleName() + " is not valid");
		}
		this.problem = problem;
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
	 * Exports Problem wrapper
	 * @return
	 */
	public ProblemWrapper exportProblemWrapper() {
		if (! valid(new TrashLogger())) {
			return null;
		}
		JobID jobIDCone = jobID.deepClone();
		boolean individualDistributionClone = individualDistribution;
		IProblemDefinition problemDefClone = problemToSolveDefinition.deepClone();
		File problemFileClone = getProblem().exportProblemFile();
		Class<?> problemToolClass = exportProblemToolClass(new TrashLogger());
		
		ProblemWrapper wrapper = new ProblemWrapper();
		wrapper.setJobID(jobIDCone);
		wrapper.setIndividualDistribution(individualDistributionClone);
		wrapper.setProblemDefinition(problemDefClone);
		wrapper.importProblemFile(problemFileClone);
		wrapper.importProblemToolClass(problemToolClass);
		wrapper.importPedigreeOfIndividualClassName(exportPedigreeOfIndividual(new TrashLogger()));
		return wrapper;
	}
	
	/**
	 * Returns clone
	 * @return
	 */
	public ProblemStruct deepClone() {
		return new ProblemStruct(this);
	}
	
	/**
	 * Tests validity of this {@link ProblemStruct}
	 * @param logger
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		
		if (jobID == null || ! jobID.valid(logger)) {
			return false;
		}
		if (exportProblemToolClass(logger) == null) {
			return false;
		}
		if (problemToSolveDefinition == null || ! problemToSolveDefinition.valid(logger)) {
			return false;
		}
		if (problem == null || ! problem.valid(logger)) {
			return false;
		}
		
		IProblemTool problemTool = ProblemTool.createInstanceOfProblemTool(
				exportProblemToolClass(logger), logger);
		
		Class<?> problemClass = problemTool.problemWhichSolves();
		// if problemTool can solve the problem
		if (getProblem().getClass() != problemClass) {
			return false;
		}
		
		return true;
	}
	
}
