package org.distributedea.agents.systemagents.datamanager;

import java.io.File;

import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Batches;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.job.JobRun;

import jade.core.AID;

/**
 * Constants of this project wrapper
 * @author stepan
 *
 */
public class FileNames {

	public static String mainControllerIP;
	
	public final static String JOB_SUFIX = "job";
	public final static String POSTPROCESSING_SUFIX = "ps";
	
	
	/**
	 *  Directory with {@link Agent_CentralManager} configuration
	 */
	public static String getDirectoryOfConfiguration() {
		
		return "configuration";
	}
	
	public static String getInputConfigurationFile() {

		return getDirectoryOfConfiguration() + File.separator +
				"inputConfiguration.xml";
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
	public static String getMethodsFile(String methodsFileName) {

		return getDirectoryOfConfiguration() + File.separator +
				methodsFileName;
	}

	/**
	 * Directory with input files
	 * @return
	 */
	public static String getDirectoryOfInputs() {
		
		return "inputs";
	}
	
	/**
	 * Provides way to the instance of {@link Problem}
	 * @param name of the file with relative path
	 * @return
	 */
	public static String getInputProblemFile(String fileName) {

		return getDirectoryOfInputs() + File.separator + fileName;
	}
	
	
	
	
	/**
	 * Directory with input {@link Batches} (input queue)
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
				jobID + "." + FileNames.JOB_SUFIX;
	}
	
	
	
	
	
	/**
	 * Directory with exported {@link Batches}
	 * @return
	 */
	public static String getDirectoryOfBatches() {

		return "batches";
	}

	
	

	
	/**
	 * Provides name of directory for the centralized solution
	 * @return
	 */
	public static String getDirectoryofResults() {

		return "result";
	}

	public static String getResultDirectory(String batchID) {

		return getDirectoryofResults() + File.separator + batchID;
	}
	
	public static String getResultDirectoryForJobs(String batchID) {
		
		return getResultDirectory(batchID) + File.separator + "jobs";
	}
	public static String getResultDirectoryForJob(JobID jobID) {
		
		return getResultDirectoryForJobs(jobID.getBatchID()) +
				File.separator + jobID.getJobID();
	}
	
	public static String getResultDirectoryForJobRun(JobID jobID) {
		
		return getResultDirectoryForJob(jobID)  + File.separator +
				jobID.getRunNumber();
	}
	
	public static String getResultDirectoryMonitoringDirectory(JobID jobID) {
		
		return getResultDirectoryForJobRun(jobID) + File.separator +
				"monitoring";
	}
	
	/**
	 * Provides way to the centralized solution for one {@link JobRun}
	 * @param fileName
	 * @return
	 */
	public static String getResultFile(JobID jobID) {
	
		return getResultDirectoryForJobRun(jobID) + File.separator +
				"result-" + jobID.getJobID() + "-" + jobID.getRunNumber() + ".txt";
	}	
	
	public static String getResultDirectoryForMatlab(String batchID) {
		
		return getResultDirectory(batchID) + File.separator + "matlab";
	}

	/**
	 * 
	 * @param batchID
	 * @return
	 */
	public static String getResultDirectoryWithCopyOfInputParameters(String batchID) {

		return getResultDirectory(batchID) + File.separator + "input";
	}
	public static String getResultDirectoryWithCopyOfInputBatch(String batchID) {

		return getResultDirectoryWithCopyOfInputParameters(batchID) + File.separator +
				batchID;
	}
	
	
	
	
	
	
	/**
	 * Provides way to centralized log directory
	 * @return
	 */
	public static String getGlobalLogDirectory() {
		
		return "log";
	}
	
	public static String getLogDirectory() {
		
		return getGlobalLogDirectory() + File.separator +
				"central-" + mainControllerIP;
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
	 * Provides name of directory for log files of {@link Agent_ComputingAgent}
	 * @return
	 */
	public static String getComputingAgentLogDirectory(JobID jobID) {

		return getLogBatchDirectory(jobID.getBatchID()) + File.separator +
				jobID.getJobID();
	}

	public static String getComputingAgentRunLogDirectory(JobID jobID) {

		return getComputingAgentLogDirectory(jobID) + File.separator +
				jobID.getRunNumber();
	}
	
	
	/**
	 * Provides name of directory for Solution files of {@link Agent_ComputingAgent}
	 * @return
	 */
	public static String getComputingAgentLogSolutionDirectory(JobID jobID) {

		return getComputingAgentRunLogDirectory(jobID) + File.separator +
				"solution";
	}

	public static String getComputingAgentLogImprovementOfDistributionDirectory(
			JobID jobID) {

		return getComputingAgentRunLogDirectory(jobID) + File.separator +
				"improvementOfDistribution";
	}
	
}
