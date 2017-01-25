package org.distributedea;

import java.util.Arrays;
import java.util.List;

import org.distributedea.agents.systemagents.Agent_CentralLogger;
import org.distributedea.agents.systemagents.Agent_CentralManager;
import org.distributedea.agents.systemagents.Agent_DataManager;
import org.distributedea.agents.systemagents.Agent_Monitor;

public class Configuration {

	public static final String JAVA_VERSION = "1.8";
	
	/**
	 * Char which is used as comment
	 */
	public static final String COMMENT_CHAR = "#";

	
	/**
	 * Agent ManagerAgent configuration
	 *  AID = <AgentName> + <Delimiter> + <AgentID> + <Delimiter> + <ContainerID>
	 */
	/** Agent name delimiter followed by agent ID */
	public static final char AGENT_NUMBER_PREFIX = '-';
	/** Agent name delimiter followed by container ID */
	public static final char CONTAINER_NUMBER_PREFIX = '_';

	
	public static double COUNT_OF_METHODS_ON_CORE = 0.5;
	
	/**
	 * Provides Classes of unique agents which doesn't contain suffix
	 * @return
	 */
	public static List<Class<?>> agentsWithoutSuffix() {
		
		Class<?>[] classes = new Class[] {
				Agent_CentralManager.class,
				Agent_DataManager.class,
				Agent_CentralLogger.class,
				Agent_Monitor.class
			};
		
		return Arrays.asList(classes);
	}
}
