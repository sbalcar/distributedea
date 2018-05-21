package org.distributedea.ontology.problemwrapper;

import java.io.File;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.pedigree.Pedigree;
import org.distributedea.ontology.problem.IProblem;
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
	 * Problem definition
	 */
	private IProblem problem;
	
	/**
	 * Data set
	 */
	private String datasetFileName;
	
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
		setProblem(problemWrapper.getProblem().deepClone());
		importDatasetFile(problemWrapper.exportDatasetFile());
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

	@Deprecated
	public String getDatasetFileName() {
		File file = exportDatasetFile();
		if (file == null) {
			return null;
		}
		return file.getAbsolutePath();
	}
	@Deprecated
	public void setDatasetFileName(String fileName) {
		try {
			importDatasetFile(new File(fileName));
		} catch(Exception e) {
			throw new IllegalArgumentException();
		}
	}
	/**
	 * Exports File with {@link Dataset} assignment
	 */
	public File exportDatasetFile() {
		if (datasetFileName == null) {
			return null;
		}
		return new File(datasetFileName);
	}
	/**
	 * Imports File with {@link Dataset} assignment
	 */
	public void importDatasetFile(File datasetFile) {
		if (datasetFile == null) {
			throw new IllegalArgumentException();
		}
		if (! datasetFile.exists() || ! datasetFile.isFile()) {
			throw new IllegalArgumentException();
		}
		this.datasetFileName = datasetFile.getAbsolutePath();
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
		
		return problemTool.readDataset(exportDatasetFile(), getProblem(), logger);
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
		struct.setProblem(problem.deepClone());
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
		File problemFile = exportDatasetFile();
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
