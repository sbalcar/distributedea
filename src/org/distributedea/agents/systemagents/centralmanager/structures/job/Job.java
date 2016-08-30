package org.distributedea.agents.systemagents.centralmanager.structures.job;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlRootElement;

import org.distributedea.agents.systemagents.centralmanager.planners.Planner;
import org.distributedea.agents.systemagents.centralmanager.plannertype.PlannerType;
import org.distributedea.agents.systemagents.centralmanager.structures.problemtools.ProblemTools;
import org.distributedea.agents.systemagents.initiator.XmlConfigurationProvider;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.configuration.inputconfiguration.InputAgentConfigurations;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.problems.IProblemTool;

import com.thoughtworks.xstream.XStream;

import jade.content.Concept;

/**
 * Ontology represents one input Job
 * @author stepan
 *
 */
@XmlRootElement (name="jobs")
public class Job implements Concept, Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Declares Job IDentification
	 */
	private String jobID;
	
	/**
	 * Declares the number of job repetitions
	 */
	private int numberOfRuns;
	
	/**
	 * Job Description
	 */
	private String description;
	
	/**
	 * Inform about type of Problem to solve
	 */
	private String problemToSolveClassName;
	
	/**
	 * Defines the filename with the input Problem
	 */
	private String problemFileName;
	
	/**
	 * Defines the filename with method(agent types)
	 */
	private String methodsFileName;
	
	/**
	 * Declares the set of available {@link ProblemTools} for computation
	 */
	private ProblemTools problemTools;
	
	/**
	 * Turns on broadcast computed individuals to distributed agents
	 */
	private boolean individualDistribution;
	
	/**
	 * Declares the {@link Planner} class which will be used to direction of the evolution
	 */
	private List<Planner> planners;	// warning planner is wrapped in list because it is necessary for XML serialization

	/**
	 * Declares the {@link PlannerType} class which will be used to direction of the evolution
	 */
	private List<PlannerType> plannerTypes;	// warning plannerType is wrapped in list because it is necessary for XML serialization

	
	
	/**
	 * Constructor
	 */
	public Job() {
	}
	
	public String getJobID() {
		return jobID;
	}
	public void setJobID(String jobID) {
		this.jobID = jobID;
	}
	
	public int getNumberOfRuns() {
		return numberOfRuns;
	}
	public void setNumberOfRuns(int numberOfRuns) {
		this.numberOfRuns = numberOfRuns;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getProblemToSolveClassName() {
		return problemToSolveClassName;
	}
	public void setProblemToSolveClassName(String problemToSolveClassName) {
		this.problemToSolveClassName = problemToSolveClassName;
	}
	/**
	 * Exports {@link IProblemTool} class
	 * @param logger
	 * @return
	 */
	public Class<?> exportProblemToSolve(IAgentLogger logger) {
		
		try {
			return Class.forName(problemToSolveClassName);
		} catch (ClassNotFoundException e) {
			logger.logThrowable("Can not find class for ProblemToll", e);
		}
		return null;
	}
	/**
	 * Import {@link IProblemTool} class
	 * @param problemToSolve
	 */
	public void importProblemToSolve(Class<?> problemToSolve) {
		
		if (problemToSolve == null) {
			throw new IllegalArgumentException();
		}
		this.problemToSolveClassName = problemToSolve.getName();
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
	 * Exports {@link File} with {@link Problem} assignment
	 */
	public File exportProblemFile() {
		if (problemFileName == null) {
			return null;
		}
		return new File(problemFileName);
	}
	/**
	 * Imports {@link File} with {@link Problem} assignment
	 */
	public void importProblemFile(File problemFile) {
		if (problemFile == null || ! problemFile.isFile()) {
			throw new IllegalArgumentException();
		}
		this.problemFileName = problemFile.getAbsolutePath();
	}

	/**
	 * Exports {@link File} with methods assignment
	 */
	public File exportMethodsFile() {
		if (methodsFileName == null) {
			return null;
		}
		return new File(methodsFileName);
	}
	/**
	 * Imports {@link File} with methods assignment
	 */
	public void importMethodsFile(File methodsFile) {
		if (methodsFile == null || ! methodsFile.isFile()) {
			throw new IllegalArgumentException();
		}
		this.methodsFileName = methodsFile.getAbsolutePath();
	}
	@Deprecated
	public String getMethodsFileName() {
		File file = exportMethodsFile();
		if (file == null) {
			return null;
		}
		return file.getAbsolutePath();
	}
	@Deprecated
	public void setMethodsFileName(String methodsFileName) {
		try {
			importMethodsFile(new File(methodsFileName));
		} catch(Exception e) {
			throw new IllegalArgumentException();
		}
	}
	
	public ProblemTools getProblemTools() {
		return problemTools;
	}
	public void setProblemTools(ProblemTools problemTools) {
		this.problemTools = problemTools;
	}

	public boolean isIndividualDistribution() {
		return individualDistribution;
	}
	public void setIndividualDistribution(boolean individualDistribution) {
		this.individualDistribution = individualDistribution;
	}
	
	public Planner getPlanner() {
		if (this.planners == null || this.planners.isEmpty()) {
			return null;
		}
		return this.planners.get(0);
	}
	public void setPlanner(Planner planner) {
		if (planner == null) {
			throw new IllegalArgumentException("Argument " +
					Planner.class.getSimpleName() + " can not be null");
		}
		
		if (this.planners == null) {
			this.planners = new ArrayList<>();
		}
		this.planners.clear();
		this.planners.add(planner);
	}

	public PlannerType getPlannerType() {
		if (this.plannerTypes == null || this.plannerTypes.isEmpty()) {
			return null;
		}
		return this.plannerTypes.get(0);
	}
	
	public void setPlannerType(PlannerType plannerType) {
		if (plannerType == null) {
			throw new IllegalArgumentException("Argument " +
					PlannerType.class.getSimpleName() + " can not be null");
		}
		
		if (this.plannerTypes == null) {
			this.plannerTypes = new ArrayList<>();
		}
		this.plannerTypes.clear();
		this.plannerTypes.add(plannerType);
	}
	
	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		
		if (jobID == null) {
			return false;
		}
		if (numberOfRuns <= 0) {
			return false;
		}
		if (description == null) {
			return false;
		}
		Class<?> problemToSolve = exportProblemToSolve(logger);
		if (problemToSolve == null) {
			return false;
		}
		if (exportProblemFile() == null) {
			return false;
		}
		if (exportMethodsFile() == null) {
			return false;
		}
		if (problemTools == null ||
				! problemTools.valid(problemToSolve, logger)) {
			return false;
		}
		if (getPlannerType() == null) {
			return false;
		}
		if (getPlanner() == null) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Exports {@link JobRun}. In case of wrong returns null.
	 * @param batchID
	 * @param runNumber
	 * @param logger
	 * @return
	 */
	public JobRun exportJobRun(String batchID, int runNumber, IAgentLogger logger) {
				
		// AgentConfigurations - Methods reading
		InputAgentConfigurations agentConfigurations =
				XmlConfigurationProvider.getConfiguration(exportMethodsFile(), logger);
		if (agentConfigurations == null) {
			Exception throwable = new IOException("Can not read AgetConfigurations");
			logger.logThrowable("Error by exporting JobRun", throwable);
			return null;
		}
				
		// Problem reading and testing
		List<Class<?>> problemToolClasses = problemTools.getProblemTools();
		if (problemToolClasses == null || problemToolClasses.isEmpty()) {
			return null;
		}
		
		Class<?> problemToolClass0 = problemToolClasses.get(0);
		if (problemToolClass0 == null) {
			return null;
		}
		
		IProblemTool problemTool = ProblemTools.exportProblemTool(problemToolClass0, logger);
		if (problemTool == null) {
			return null;
		}
		
		File fileOfProblem = exportProblemFile();
		Problem problem = problemTool.readProblem(fileOfProblem, logger);
		
		
		
		JobRun jobRun = new JobRun();
		jobRun.setIndividualDistribution(individualDistribution);
		jobRun.setAgentConfigurations(agentConfigurations);
		jobRun.setProblem(problem);
		jobRun.setProblemTools(problemTools);
		
		jobRun.setJobID(new JobID(batchID, jobID, runNumber));

		// test validity of JobRun
		if (jobRun.valid(logger)) {
			return jobRun;
		} else {
			return null;
		}
	}
	
	
	
	/**
	 * Exports structure as the XML String to the file
	 * 
	 * @throws FileNotFoundException
	 * @throws JAXBException 
	 */
	public void exportXML(File jobFile) throws IOException {

		String xml = exportXML();

		PrintWriter file = new PrintWriter(jobFile.getAbsolutePath());
		file.println(xml);
		file.close();
		
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
	 * Import the {@link Job} from the file
	 * 
	 * @throws FileNotFoundException
	 */
	public static Job importXML(File file)
			throws FileNotFoundException {

		Scanner scanner = new Scanner(file);
		String xml = scanner.useDelimiter("\\Z").next();
		scanner.close();

		return importXML(xml);
		
	}

	/**
	 * Import the {@link Job} from the String
	 */
	public static Job importXML(String xml) {

		XStream xstream = new XStream();
		xstream.setMode(XStream.NO_REFERENCES);

		return (Job) xstream.fromXML(xml);
	}
	
}
