package org.distributedea.logging;

import java.util.logging.Level;

import org.distributedea.agents.Agent_DistributedEA;

public class AgentLogger {
	
	private Agent_DistributedEA agent;
	
	public AgentLogger(Agent_DistributedEA agent) {
		this.agent = agent;
	}

	public void logThrowable(String message, Throwable throwable) {
		//:TODO send info to Agent_Logger
		ConsoleLogger.logThrowable(message, throwable);
	}
	
	public void log(Level logLevel, String message) {
		//:TODO send info to Agent_Logger
		ConsoleLogger.log(logLevel, message);
	}
	
	public void log(Level logLevel, String source, String message) {
		//:TODO send info to Agent_Logger
		ConsoleLogger.log(logLevel, source, message);
	}
	
}
