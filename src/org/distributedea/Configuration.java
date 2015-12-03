package org.distributedea;

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
	 * Agent ComputingAgent configuration
	 */
	/** Period of sending result from Computing Agent to DataManager Agent */
	public static long LOG_PERIOD_MS = 1000;
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

		return "configuration" + System.getProperty("file.separator")
				+ "configuration.xml";
	}
	
	/**
	 * Provides way to system agents required to run slave node
	 * @return name of the file with relative path
	 */
	public static String getConfigurationSlaveFile() {

		return "configuration" + System.getProperty("file.separator")
				+ "configurationSlave.xml";
	}

	/**
	 * Provides way to set of Methods (Computing agents) for planning to slave nodes
	 * @return name of the file with relative path
	 */
	public static String getMethodsFile() {

		return "configuration" + System.getProperty("file.separator")
				+ "methods.xml";
	}

	/**
	 * Provides way to the instance of Problem
	 * @return
	 */
	public static String getInputProblemFile() {

		return "inputs" + System.getProperty("file.separator")
				+ InputConfiguration.inputProblemFileName;
	}
	/**
	 * Provides way to the instance of Problem
	 * @param name of the file with relative path
	 * @return
	 */
	public static String getInputFile(String fileName) {

		return "inputs" + System.getProperty("file.separator")
				+ fileName;
	}
	
	/**
	 * Provides way to the solution instance by name
	 * @param fileName
	 * @return
	 */
	public static String getSolutionFile(String fileName) {

		return "inputs" + System.getProperty("file.separator")
				+ "solutions" + System.getProperty("file.separator")
				+ fileName;
	}

	/**
	 * Provides way to the centralized solution of whole Distributed Evolution
	 * @return
	 */
	public static String getResultFile() {

		return "results.txt";
	}

	/**
	 * Provides name of directory for log files of Computing Agents
	 * @return
	 */
	public static String getComputingAgentLogDirectory() {

		return "log";

	}
	
	/**
	 * Provides log file with path for concrete Computing Agent
	 * @param computingAgentAID
	 * @return
	 */
	public static String getComputingAgentLogFile(AID computingAgentAID) {

		return getComputingAgentLogDirectory() + System.getProperty("file.separator")
				+ computingAgentAID.getLocalName() + ".log";
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
