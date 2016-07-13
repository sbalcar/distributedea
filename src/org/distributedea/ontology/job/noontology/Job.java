package org.distributedea.ontology.job.noontology;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlRootElement;

import org.distributedea.agents.systemagents.centralmanager.planner.Planner;
import org.distributedea.agents.systemagents.centralmanager.planner.tool.PlannerException;
import org.distributedea.agents.systemagents.centralmanager.plannertype.PlannerType;
import org.distributedea.configuration.AgentConfigurations;
import org.distributedea.configuration.XmlConfigurationProvider;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problemwrapper.noontologie.ProblemTools;
import org.distributedea.problems.ProblemTool;

import com.thoughtworks.xstream.XStream;

import jade.content.Concept;


@XmlRootElement (name="jobs")
public class Job implements Concept, Serializable {

	private static final long serialVersionUID = 1L;

	public Job() {
	}
	
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
	private Class<?> problemToSolve;
	
	/**
	 * Defines the filename with the input Problem
	 */
	private String problemFileName;
	
	/**
	 * Defines the filename with method(agent types)
	 */
	private String methodsFileName;
	
	/**
	 * Declares the set of available ProblemTools for Computing Agents
	 */
	private ProblemTools problemTools;
	
	
	/**
	 * Turns on broadcast computed individuals to distributed agents
	 */
	private boolean individualDistribution;
	
	
	/**
	 * Declares the Planner Class which will be used to direction of the evolution
	 */
	private List<Planner> planners;	// warning planner is wrapped in list because it is necessary for XML serialization

	/**
	 * Declares the PlannerType Class which will be used to direction of the evolution
	 */
	private List<PlannerType> plannerTypes;	// warning plannerType is wrapped in list because it is necessary for XML serialization

	
	
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
	
	public Class<?> getProblemToSolve() {
		return problemToSolve;
	}
	public void setProblemToSolve(Class<?> problemToSolve) {
		this.problemToSolve = problemToSolve;
	}
	
	public String getProblemFileName() {
		return problemFileName;
	}
	public void setProblemFileName(String problemFileName) {
		this.problemFileName = problemFileName;
	}
	
	public String getMethodsFileName() {
		return methodsFileName;
	}
	public void setMethodsFileName(String methodsFileName) {
		this.methodsFileName = methodsFileName;
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
		if (this.plannerTypes == null) {
			this.plannerTypes = new ArrayList<>();
		}
		this.plannerTypes.clear();
		this.plannerTypes.add(plannerType);
	}
	
	public JobRun exportJobRun(String batchID, int runNumber, IAgentLogger logger) {
				
		// AgentConfigurations - Methods reading
		AgentConfigurations agentConfigurations =
				XmlConfigurationProvider.getConfiguration(methodsFileName, logger);
		if (agentConfigurations == null) {
			Exception throwable = new PlannerException("Can not read AgetConfigurations");
			logger.logThrowable("Error by exporting JobRun", throwable);
			return null;
		}
		
		boolean areAgentConfigurationsValid = agentConfigurations.valid(logger);
		if (! areAgentConfigurationsValid) {
			Exception throwable = new PlannerException("AgentConfiguration aren't valid");
			logger.logThrowable("Error by exporting JobRun", throwable);
			return null;
		}
		
		// Problem reading and testing
		ProblemTool problemTool = problemTools.exportProblemTool(0, logger);
		Problem problem = problemTool.readProblem(problemFileName, logger);
		
		
		
		JobRun jobRun = new JobRun();
		jobRun.setIndividualDistribution(individualDistribution);
		jobRun.setAgentConfigurations(agentConfigurations);
		jobRun.setProblem(problem);
		jobRun.setProblemTools(problemTools);
		
		jobRun.setJobID(new JobID(batchID, jobID, runNumber));

		return jobRun;
	}
	
	
	
	/**
	 * Exports structure as the XML String to the file
	 * 
	 * @throws FileNotFoundException
	 * @throws JAXBException 
	 */
	public void exportXML(String fileName) throws FileNotFoundException, JAXBException {

		String xml = exportXML();

		PrintWriter file = new PrintWriter(fileName);
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
