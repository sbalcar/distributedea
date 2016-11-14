package org.distributedea.ontology.job;

import java.util.logging.Level;

import jade.content.Concept;

import org.distributedea.agents.systemagents.centralmanager.structures.problemtools.ProblemTools;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.configuration.AgentConfigurations;
import org.distributedea.ontology.configurationinput.InputAgentConfiguration;
import org.distributedea.ontology.configurationinput.InputAgentConfigurations;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescription;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescriptions;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problemwrapper.ProblemStruct;

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
	 * Agent configurations - available for problem solving
	 */
	private InputAgentConfigurations agentConfigurations;
	
	/**
	 * Problem Tools to use for solving Problem 
	 */
	private ProblemTools problemTools;
	
	/**
	 * Problem to solve
	 */
	private Problem problem;


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
		InputAgentConfigurations agentConfigurationsClone =
				jobRun.getAgentConfigurations().deepClone();
		ProblemTools problemToolsClone =
				jobRun.getProblemTools().deepClone();
		Problem problemClone =
				jobRun.getProblem().deepClone();
		
		setJobID(jobIDClone);
		setIndividualDistribution(individualDistributionClone);
		setAgentConfigurations(agentConfigurationsClone);
		setProblemTools(problemToolsClone);
		setProblem(problemClone);
		
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
	
	/**
	 * Returns {@link AgentConfiguration} as agent identification
	 * @return
	 */
	public InputAgentConfigurations getAgentConfigurations() {
		return agentConfigurations;
	}
	public void setAgentConfigurations(InputAgentConfigurations agentConfigurations) {
		if (agentConfigurations == null ||
				! agentConfigurations.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					InputAgentConfigurations.class.getSimpleName() + " is not valid");
		}
		this.agentConfigurations = agentConfigurations;
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
	 * Returns {@link ProblemTools} available to solve {@link Problem} 
	 * @return
	 */
	public ProblemTools getProblemTools() {
		return problemTools;
	}
	public void setProblemTools(ProblemTools problemTools) {
		if (problemTools == null ||
				! problemTools.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					ProblemTools.class.getSimpleName() + " is not valid");
		}
		this.problemTools = problemTools;
	}

	/**
	 * Returns {@link Problem} to solve
	 * @return
	 */
	public Problem getProblem() {
		return problem;
	}
	public void setProblem(Problem problem) {
		if (problem == null || ! problem.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					Problem.class.getSimpleName() + " is not valid");
		}
		this.problem = problem;
	}
	
	/**
	 * Exports the {@link InputMethodDescriptions}
	 * @return
	 */
	public InputMethodDescriptions exportInputAgentDescriptions() {
		return new InputMethodDescriptions(
				getAgentConfigurations(),getProblemTools());
	}
	
	/**
	 * Exports the {@link JobRun} as {@link ProblemStruct} which
	 * contains ProblemTool in argument
	 * @param problemToolClass
	 * @return
	 */
	public ProblemStruct exportProblemStruct(Class<?> problemToolClass) {
		
		JobID jobIDClone = getJobID().deepClone();
		
		Problem problemClone = getProblem().deepClone();
		
		ProblemStruct problemStruct = new ProblemStruct();
		problemStruct.setJobID(jobIDClone);
		problemStruct.setIndividualDistribution(getIndividualDistribution());
		problemStruct.setProblem(problemClone);
		problemStruct.importProblemToolClass(problemToolClass);
		
		return problemStruct;
	}
	
	/**
	 * Export random selected {@link MethodDescription}
	 * @return
	 */
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
		
		if (agentConfigurations == null || ! agentConfigurations.valid(logger)) {
			logger.log(Level.WARNING, AgentConfigurations.class.getSimpleName() + " are not valid");
			return false;
		}
		
		if (problem == null || ! problem.valid(logger)) {
			logger.log(Level.WARNING, Problem.class.getSimpleName() + " is not valid");
			return false;
		}
		
		if (problemTools == null || ! problemTools.valid(problem.getClass(), logger)) {
			logger.log(Level.WARNING, ProblemTools.class.getSimpleName() + " are not valid");
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
