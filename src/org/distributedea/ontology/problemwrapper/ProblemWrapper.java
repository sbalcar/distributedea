package org.distributedea.ontology.problemwrapper;

import java.io.File;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.pedigree.Pedigree;
import org.distributedea.ontology.problemdefinition.IProblemDefinition;
import org.distributedea.problems.IProblemTool;
import org.distributedea.problems.ProblemTool;

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
	 * Turns on broadcast computed individuals to distributed agents
	 */
	private boolean individualDistribution;
	
	/**
	 * Problem definition
	 */
	private IProblemDefinition problemDefinition;
	
	/**
	 * File with problem
	 */
	private String problemFileName;
	
	/**
	 * Problem Tool to use for solving Problem 
	 */
	private String problemToolClass;
	
	/**
	 * Specify about type of {@link Pedigree}
	 */
	private String pedigreeOfIndividualClassName;

	
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
		jobID = problemWrapper.getJobID().deepClone();
		individualDistribution = problemWrapper.isIndividualDistribution();
		setProblemDefinition(problemWrapper.getProblemDefinition().deepClone());
		importProblemFile(problemWrapper.exportProblemFile());
		importProblemToolClass(problemWrapper.exportProblemToolClass());
		importPedigreeOfIndividualClassName(
				problemWrapper.exportPedigreeOfIndividual(new TrashLogger()));
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
	 * Returns flag decides about distribution of {@link IndividualWrapper}s
	 * @return
	 */
	public boolean isIndividualDistribution() {
		return individualDistribution;
	}
	/**
	 * Sets distribution of {@link Individual}s
	 * @param individualDistribution
	 */
	public void setIndividualDistribution(boolean individualDistribution) {
		this.individualDistribution = individualDistribution;
	}
	
	
	/**
	 * Returns definition of Problem to solve
	 * @return
	 */
	public IProblemDefinition getProblemDefinition() {
		return problemDefinition;
	}

	public void setProblemDefinition(IProblemDefinition problemDefinition) {
		if (problemDefinition == null || ! problemDefinition.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					IProblemDefinition.class.getSimpleName() + " is not valid");
		}
		this.problemDefinition = problemDefinition;
	}

	@Deprecated
	public String getProblemFileName() {
		File file = exportProblemFile();
		if (file == null) {
			return null;
		}
		return file.getAbsolutePath();
	}
	@Deprecated
	public void setProblemFileName(String fileName) {
		try {
			importProblemFile(new File(fileName));
		} catch(Exception e) {
			throw new IllegalArgumentException();
		}
	}
	/**
	 * Exports File with {@link Problem} assignment
	 */
	public File exportProblemFile() {
		if (problemFileName == null) {
			return null;
		}
		return new File(problemFileName);
	}
	/**
	 * Imports File with {@link Problem} assignment
	 */
	public void importProblemFile(File problemFile) {
		if (problemFile == null) {
			throw new IllegalArgumentException();
		}
		if (! problemFile.exists() || ! problemFile.isFile()) {
			throw new IllegalArgumentException();
		}
		this.problemFileName = problemFile.getAbsolutePath();
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
	 * Import {@link IProblemTool} class
	 * @param problemToolClass
	 */
	public void importProblemToolClass(Class<?> problemToolClass) {
		this.problemToolClass = problemToolClass.getName();
	}
	/**
	 * Export {@link IProblemTool} class
	 * @return
	 */
	public Class<?> exportProblemToolClass() {
		try {
			return Class.forName(problemToolClass);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	
	/**
	 * Export Problem from from given problem file
	 * @param logger
	 * @return
	 */
	public Dataset exportDataset(IAgentLogger logger) {
		
		if (! valid(logger)) {
			return null;
		}
		
		IProblemTool problemTool = ProblemTool.createInstanceOfProblemTool(
				exportProblemToolClass(), logger);
		
		return problemTool.readDataset(exportProblemFile(), logger);
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
	 * Exports {@link ProblemStruct}
	 * @param logger
	 * @return
	 */
	public ProblemStruct exportProblemStruct(IAgentLogger logger) {
		if (! valid(logger)) {
			return null;
		}
		
		ProblemStruct struct = new ProblemStruct();
		struct.setJobID(getJobID());
		struct.setIndividualDistribution(individualDistribution);
		struct.setProblemDefinition(problemDefinition.deepClone());
		struct.setDataset(exportDataset(logger));
		struct.importProblemToolClass(exportProblemToolClass());
		struct.importPedigreeOfIndividualClassName(exportPedigreeOfIndividual(logger));
		return struct;
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
		if (exportProblemToolClass() == null) {
			return false;
		}
		File problemFile = exportProblemFile();
		if (problemFile == null || ! problemFile.isFile()) {
			return false;
		}
		return true;
	}

	/**
	 * Returns clone
	 * @return
	 */
	public ProblemWrapper deepClone() {
		return new ProblemWrapper(this);
	}
}
