package org.distributedea;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.distributedea.agents.systemagents.Agent_CentralLoger;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.Agent_DataManager;
import org.distributedea.ontology.job.JobID;

import jade.core.AID;

/**
 * Constants of this project wrapper
 * @author stepan
 *
 */
public class Configuration {

	/**
	 * Char which is used as comment
	 */
	public static String COMMENT_CHAR = "#";
	
	/**
	 * Agent ComputingAgent configuration
	 */

	/** Period of sending Individual from Computing Agent to another Computing Agents */
	public static long INDIVIDUAL_BROADCAST_PERIOD_MS = 1000;
	
	
	/**
	 * Agent ManagerAgent configuration
	 *  AID = <AgentName> + <Delimiter> + <AgentID> + <Delimiter> + <ContainerID>
	 */
	/** Agent name delimiter followed by agent ID */
	public static char AGENT_NUMBER_PREFIX = '-';
	/** Agent name delimiter followed by container ID */
	public static char CONTAINER_NUMBER_PREFIX = '_';
	
	public static String JOB_SUFIX = "job";
	public static String POSTPROCESSING_SUFIX = "ps";
	
	/**
	 *  Agent CentralManager configuration
	 */
	/** Period of Scheduler replanning */
	public static long REPLAN_PERIOD_MS = 10000;
	
	
	
	private static String getDirectoryOfConfiguration() {
		
		return "configuration";
	}
	/**
	 * Provides way to system agents required to run master node
	 * @return name of the file with relative path
	 */
	public static String getConfigurationFile() {

		return getDirectoryOfConfiguration() + File.separator +
				"configuration.xml";
	}

	/**
	 * Provides way to system agents required to run slave node
	 * @return name of the file with relative path
	 */
	public static String getConfigurationSlaveFile() {

		return getDirectoryOfConfiguration() + File.separator +
				"configurationSlave.xml";
	}
	
	/**
	 * Provides way to set of Methods (Computing agents) for planning to slave nodes
	 * @return name of the file with relative path
	 */
	public static String getMethodsFile() {

		return getDirectoryOfConfiguration() + File.separator +
				"methods.xml";
	}

	/**
	 * Directory with input files
	 * @return
	 */
	public static String getDirectoryOfInputs() {
		
		return "inputs";
	}
	
	/**
	 * Provides way to the instance of Problem
	 * @param name of the file with relative path
	 * @return
	 */
	public static String getInputProblemFile(String fileName) {

		return getDirectoryOfInputs() + File.separator +
				fileName;
	}
	
	/**
	 * Provides way to the solution instance by name
	 * @param fileName
	 * @return
	 */
	public static String getSolutionFile(String fileName) {

		return getDirectoryOfInputs() + File.separator +
				"solutions" + File.separator +
				fileName;
	}
	
	/**
	 * Directory with input Batches
	 * @return
	 */
	public static String getDirectoryOfInputBatches() {

		return "jobqueue";
	}
	
	public static String getInputBatchDirectory(String batchID) {

		return getDirectoryOfInputBatches() + File.separator + batchID;
	}

	public static String getInputJobFile(String batchID, String jobID) {

		return getInputBatchDirectory(batchID) + File.separator +
				jobID + "." + Configuration.JOB_SUFIX;
	}
	
	public static String getPostProcessingFile(String batchID, String psID) {

		return getInputBatchDirectory(batchID) + File.separator +
				psID + "." + Configuration.POSTPROCESSING_SUFIX;
	}

	/**
	 * Provides name of directory for the centralized solution of whole Distributed Evolution
	 * @return
	 */
	public static String getDirectoryofResults() {

		return "result";
	}

	public static String getResultDirectory(String batchID) {

		return getDirectoryofResults() + File.separator + batchID;
	}
	
	/**
	 * Provides way to the centralized solution
	 * @param fileName
	 * @return
	 */
	public static String getResultFile(JobID jobID) {
		
		return getResultDirectory(jobID.getBatchID()) + File.separator +
				"result-" + jobID.getJobID() + "-" + jobID.getRunNumber() + ".txt";
	}
	
	
	public static String getLogDirectory() {
		
		return "log";
	}

	public static String getGeneralLogDirectoryForComputingAgent(
			AID computingAgentAID) {
		
		return getLogDirectory() + File.separator +
				computingAgentAID.getLocalName() + ".log";
	}
	
	public static String getLogBatchDirectory(String batchID) {
		
		return getLogDirectory() + File.separator + batchID;
	}
	
	/**
	 * Provides name of directory for log files of Computing Agents
	 * @return
	 */
	public static String getComputingAgentLogDirectory(JobID jobID) {

		String batchID = jobID.getBatchID();
		return getLogBatchDirectory(batchID) + File.separator +
				jobID.getJobID();
	}

	public static String getComputingAgentRunLogDirectory(JobID jobID) {

		return getComputingAgentLogDirectory(jobID) + File.separator +
				jobID.getRunNumber();
	}
	
	
	/**
	 * Provides name of directory for Solution files of Computing Agents
	 * @return
	 */
	public static String getComputingAgentLogSolutionDirectory(JobID jobID) {

		return getComputingAgentRunLogDirectory(jobID) + File.separator + "solution";
	}

	public static String getComputingAgentLogImprovementOfDistributionDirectory(
			JobID jobID) {

		return getComputingAgentRunLogDirectory(jobID) + File.separator +
				"improvementOfDistribution";
	}

	
	/**
	 * Provides log file with a path for concrete Computing Agent
	 * @param computingAgentAID
	 * @return
	 */
	public static String getComputingAgentLogFile(AID computingAgentAID,
			JobID jobID) {

		String batchID = jobID.getBatchID();
		return getLogBatchDirectory(batchID) + File.separator
				+ computingAgentAID.getLocalName() + ".log";
	}
	
	
	/**
	 * Provides log file with a path for concrete Computing Agent
	 * @param computingAgentAID
	 * @return
	 */
	public static String getComputingAgentResultFile(AID computingAgentAID, JobID jobID) {

		return getComputingAgentRunLogDirectory(jobID) + File.separator
				+ computingAgentAID.getLocalName() + ".rslt";
	}

	/**
	 * Provides log file with a path for the Best Result(Individual)
	 * @param computingAgentAID - name of file contains on their AID
	 * @return
	 */
	public static String getComputingAgentSolutionFile(AID computingAgentAID, JobID jobID) {

		return getComputingAgentLogSolutionDirectory(jobID) + File.separator
				+ computingAgentAID.getLocalName() + ".sol";
	}
	
	public static String getComputingAgentLogImprovementOfDistributionFile(AID computingAgentAID, JobID jobID) {

		return getComputingAgentLogImprovementOfDistributionDirectory(jobID) + File.separator
				+ computingAgentAID.getLocalName() + ".impr";
	}
	
	/**
	 * Provides Classes of unique agents which doesn't contain suffix
	 * @return
	 */
	public static List<Class<?>> agentsWithoutSuffix() {
		
		Class<?>[] classes = new Class[] {
				Agent_CentralManager.class,
				Agent_DataManager.class,
				Agent_CentralLoger.class
			};
		
		return Arrays.asList(classes);
	}
	
}
