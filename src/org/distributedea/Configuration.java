package org.distributedea;

import java.util.Arrays;
import java.util.List;

import org.distributedea.agents.systemagents.Agent_CentralLoger;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.Agent_DataManager;
import org.distributedea.agents.systemagents.Agent_Monitor;

public class Configuration {

	/**
	 * Char which is used as comment
	 */
	public static String COMMENT_CHAR = "#";

	/**
	 * Agent CentralManager configuration
	 */
	/** Period of Scheduler replanning */
	public static long REPLAN_PERIOD_MS = 70000;
	
	
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

	
	/**
	 * Provides Classes of unique agents which doesn't contain suffix
	 * @return
	 */
	public static List<Class<?>> agentsWithoutSuffix() {
		
		Class<?>[] classes = new Class[] {
				Agent_CentralManager.class,
				Agent_DataManager.class,
				Agent_CentralLoger.class,
				Agent_Monitor.class
			};
		
		return Arrays.asList(classes);
	}
}
