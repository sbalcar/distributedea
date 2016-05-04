package org.distributedea;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.distributedea.agents.systemagents.Agent_CentralLoger;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.Agent_DataManager;

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
	
	/**
	 *  Agent CentralManager configuration
	 */
	/** Period of Scheduler replanning */
	public static long REPLAN_PERIOD_MS = 10000;
	
	
	
	/**
	 * Provides way to system agents required to run master node
	 * @return name of the file with relative path
	 */
	public static String getConfigurationFile() {

		return "configuration" + File.separator
				+ "configuration.xml";
	}
	
	/**
	 * Provides way to system agents required to run slave node
	 * @return name of the file with relative path
	 */
	public static String getConfigurationSlaveFile() {

		return "configuration" + File.separator
				+ "configurationSlave.xml";
	}

	/**
	 * Provides way to set of Methods (Computing agents) for planning to slave nodes
	 * @return name of the file with relative path
	 */
	public static String getMethodsFile() {

		return "configuration" + File.separator
				+ "methods.xml";
	}

	/**
	 * Provides way to the instance of Problem
	 * @param name of the file with relative path
	 * @return
	 */
	public static String getInputProblemFile(String fileName) {

		return "inputs" + File.separator + fileName;
	}
	
	
	public static String getJobsDirectory() {

		return "jobqueue";
	}
	
	
	/**
	 * Provides way to the solution instance by name
	 * @param fileName
	 * @return
	 */
	public static String getSolutionFile(String fileName) {

		return "inputs" + File.separator + "solutions" +
				File.separator + fileName;
	}

	/**
	 * Provides name of directory for the centralized solution of whole Distributed Evolution
	 * @return
	 */
	public static String getResultDirectory() {

		return "result";
	}

	/**
	 * Provides way to the centralized solution
	 * @param fileName
	 * @return
	 */
	public static String getResultFile(String fileID) {
		
		return getResultDirectory() + File.separator +
				"result-" + fileID +".txt";
	}
	
	/**
	 * Provides name of directory for log files of Computing Agents
	 * @return
	 */
	public static String getComputingAgentLogDirectory() {

		return "log";
	}

	/**
	 * Provides name of directory for log files of Computing Agents
	 * @return
	 */
	public static String getComputingAgentLogResultDirectory() {

		return "log" + File.separator + "result";
	}

	public static String getComputingAgentLogImprovementOfDistributionDirectory() {

		return "log" + File.separator + "improvementOfDistribution";
	}
	
	/**
	 * Provides log file with a path for concrete Computing Agent
	 * @param computingAgentAID
	 * @return
	 */
	public static String getComputingAgentLogFile(AID computingAgentAID) {

		return getComputingAgentLogDirectory() + File.separator
				+ computingAgentAID.getLocalName() + ".log";
	}

	/**
	 * Provides log file with a path for the Best Result(Individual)
	 * @param computingAgentAID - name of file contains on their AID
	 * @return
	 */
	public static String getComputingAgentLogResultFile(AID computingAgentAID) {

		return getComputingAgentLogResultDirectory() + File.separator
				+ computingAgentAID.getLocalName() + ".rslt";
	}
	
	public static String getComputingAgentLogImprovementOfDistributionFile(AID computingAgentAID) {

		return getComputingAgentLogImprovementOfDistributionDirectory() + File.separator
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
