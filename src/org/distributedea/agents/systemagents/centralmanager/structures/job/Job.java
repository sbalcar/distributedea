package org.distributedea.agents.systemagents.centralmanager.structures.job;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.List;
import java.util.Scanner;

import javax.xml.bind.JAXBException;

import org.distributedea.agents.systemagents.centralmanager.plannerinfrastructure.PlannerInfrastructure;
import org.distributedea.agents.systemagents.centralmanager.plannerinfrastructure.endcondition.IPlannerEndCondition;
import org.distributedea.agents.systemagents.centralmanager.plannerinfrastructure.endcondition.PlannerTimeRestriction;
import org.distributedea.agents.systemagents.centralmanager.planners.IPlanner;
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerAgentInfo;
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerRandom;
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerRandomGuaranteeChance;
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerTheBestAverageOfFitness;
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerTheBestHelper;
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerTheBestResult;
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerTheGreatestQGoodMaterialImprovementFitness;
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerTheGreatestQMaterialGoodMaterialImprovement;
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerTheGreatestQuantityOfGoodMaterial;
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerTheGreatestQuantityOfImprovement;
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerTheGreatestQuantityOfMaterial;
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerThePedigree;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationOneMethodPerCore;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationRandom;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationRunEachMethodOnce;
import org.distributedea.agents.systemagents.centralmanager.structures.problemtools.ProblemTools;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.configuration.Argument;
import org.distributedea.ontology.configuration.Arguments;
import org.distributedea.ontology.configurationinput.InputAgentConfiguration;
import org.distributedea.ontology.configurationinput.InputAgentConfigurations;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.job.JobRun;
import org.distributedea.ontology.method.IMethods;
import org.distributedea.ontology.pedigree.Pedigree;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.problems.IProblemTool;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import jade.content.Concept;

/**
 * Ontology represents one input Job
 * @author stepan
 *
 */
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
	private IProblem problem;
	
	/**
	 * Defines the filename with the input {@link Dataset}
	 */
	private String datasetFileName;
	
	/**
	 * Defines the methods
	 */
	private IMethods methods;
	
	
	/**
	 * Turns on broadcast computed individuals to distributed agents
	 */
	private boolean individualDistribution;
	
	/**
	 * Declares the {@link IPlanner} class which will be used to direction of the evolution
	 */
	private IPlanner planner;

	/**
	 * Declares the {@link PlannerInfrastructure} class which will be used to direction of the evolution
	 */
	private IPlannerEndCondition plannerEndCondition;

	/**
	 * Specify about type of {@link Pedigree}
	 */
	private String pedigreeOfIndividualClassName;
	
	
	
	/**
	 * Constructor
	 */
	public Job() {
	}

	/**
	 * Copy Constructor
	 */
	public Job(Job job) {
		if (job == null || ! job.valid(new TrashLogger())) {
			job.valid(new TrashLogger());
			throw new IllegalArgumentException("Argument " + Job.class.getSimpleName() + " is not valid");
		}
		this.jobID = job.getJobID();
		this.numberOfRuns = job.getNumberOfRuns();
		this.description = job.getDescription();
		this.problem = job.getProblem();
		this.datasetFileName = job.getDatasetFileName();
		this.methods = job.getMethods().deepClone();
		this.individualDistribution = job.isIndividualDistribution();
		this.planner = job.planner;
		this.plannerEndCondition = job.plannerEndCondition;
		this.pedigreeOfIndividualClassName = job.getPedigreeOfIndividualClassName();
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
	
	public IProblem getProblem() {
		return problem;
	}
	public void setProblem(
			IProblem problemToSolveDefinition) {
		this.problem = problemToSolveDefinition;
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
	 * Exports {@link File} with {@link Dataset} assignment
	 */
	public File exportDatasetFile() {
		if (datasetFileName == null) {
			return null;
		}
		return new File(datasetFileName);
	}
	/**
	 * Imports {@link File} with {@link Dataset} assignment
	 */
	public void importDatasetFile(File datasetFile) {
		if (datasetFile == null || ! datasetFile.isFile()) {
			throw new IllegalArgumentException();
		}
		this.datasetFileName = datasetFile.getAbsolutePath();
	}


	public IMethods getMethods() {
		return this.methods;
	}
	public void setMethods(IMethods methods) {
		this.methods = methods;
	}

	public boolean isIndividualDistribution() {
		return individualDistribution;
	}
	public void setIndividualDistribution(boolean individualDistribution) {
		this.individualDistribution = individualDistribution;
	}
	
	public IPlanner getPlanner() {
		return this.planner;
	}
	public void setPlanner(IPlanner planner) {
		if (planner == null) {
			throw new IllegalArgumentException("Argument " +
					IPlanner.class.getSimpleName() + " can not be null");
		}
		this.planner = planner;	
	}

	public IPlannerEndCondition getPlannerEndCondition() {
		return this.plannerEndCondition;
	}
	
	public void setPlannerEndCondition(IPlannerEndCondition plannerEndCondition) {
		if (plannerEndCondition == null) {
			throw new IllegalArgumentException("Argument " +
					PlannerInfrastructure.class.getSimpleName() + " can not be null");
		}
		this.plannerEndCondition = plannerEndCondition;
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
		if (problem == null || ! problem.valid(logger)) {
			return false;
		}
		if (exportDatasetFile() == null) {
			return false;
		}
		if (getMethods() == null || ! getMethods().valid(logger)) {
			return false;
		}
		if (getPlannerEndCondition() == null) {
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
		
		ProblemTools problemTools = methods.exportProblemTools();
		
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
		
		File fileOfProblem = exportDatasetFile();
		Dataset dataset = problemTool.readDataset(fileOfProblem, logger);
		
		
		
		JobRun jobRun = new JobRun();
		jobRun.setIndividualDistribution(individualDistribution);
		jobRun.setMethods(methods.deepClone());
		jobRun.setProblemDefinition(problem.deepClone());
		jobRun.setDataset(dataset);
		
		jobRun.setJobID(new JobID(batchID, jobID, runNumber));
		jobRun.importPedigreeOfIndividualClassName(exportPedigreeOfIndividual(logger));

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
	public void exportXML(File jobFile) throws Exception {

		String xml = exportXML();
		
		PrintWriter file = new PrintWriter(jobFile.getAbsolutePath());
		file.println(xml);
		file.close();
		
	}
	
	/**
	 * Exports to the XML String
	 */
	public String exportXML() {

		XStream xstream = new XStream(new DomDriver());
		xstream.setMode(XStream.NO_REFERENCES);
		xstream.autodetectAnnotations(true);
		
		processAliases(xstream);
		
		return xstream.toXML(this);
	}
	
	/**
	 * Import the {@link Job} from the file
	 * 
	 * @throws FileNotFoundException
	 */
	public static Job importXML(File file)
			throws Exception {

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
		xstream.autodetectAnnotations(true);
		
		processAliases(xstream);
		
		return (Job) xstream.fromXML(xml);
	}
	
	private static void processAliases(XStream xstream) {
		
		xstream.alias("job", Job.class);
		xstream.alias("inputAgentConfigurations", InputAgentConfigurations.class);
		xstream.alias("inputAgentConfiguration", InputAgentConfiguration.class);
		xstream.alias("arguments", Arguments.class);
		xstream.alias("argument", Argument.class);
		
		xstream.alias("plannerInitialisationOneMethodPerCore", PlannerInitialisationOneMethodPerCore.class);
		xstream.alias("plannerInitialisationRandom", PlannerInitialisationRandom.class);
		xstream.alias("plannerInitialisationRunEachMethodOnce", PlannerInitialisationRunEachMethodOnce.class);
		
		xstream.alias("plannerAgentInfo", PlannerAgentInfo.class);
		xstream.alias("plannerRandom", PlannerRandom.class);
		xstream.alias("plannerRandomImpr", PlannerRandomGuaranteeChance.class);
		xstream.alias("plannerTheBestAverageOfFitness", PlannerTheBestAverageOfFitness.class);
		xstream.alias("plannerTheBestHelper", PlannerTheBestHelper.class);
		xstream.alias("plannerTheBestResult", PlannerTheBestResult.class);
		xstream.alias("plannerTheGreatestQuantityOfImprovement", PlannerTheGreatestQuantityOfImprovement.class);
		xstream.alias("plannerTheGreatestQuantityOfMaterial", PlannerTheGreatestQuantityOfMaterial.class);
		xstream.alias("plannerTheGreatestQuantityOfGoodMaterial", PlannerTheGreatestQuantityOfGoodMaterial.class);
		xstream.alias("plannerTheGreatestQuantityCombination", PlannerTheGreatestQGoodMaterialImprovementFitness.class);
		xstream.alias("plannerTheGreatestQMaterialGoodMaterialImprovement", PlannerTheGreatestQMaterialGoodMaterialImprovement.class);
		xstream.alias("plannerThePedigree", PlannerThePedigree.class);
		
		xstream.alias("plannerTimeRestriction", PlannerTimeRestriction.class);
	}
	
	/**
	 * exports Clone
	 * @return
	 */
	public Job deepClone() {
		return new Job(this);
	}
}
