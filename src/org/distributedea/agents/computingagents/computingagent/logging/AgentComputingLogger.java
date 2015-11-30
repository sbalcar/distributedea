package org.distributedea.agents.computingagents.computingagent.logging;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;

import org.distributedea.Configuration;
import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.logging.AgentLogger;
import org.distributedea.logging.ConsoleLogger;

public class AgentComputingLogger extends AgentLogger {

	public AgentComputingLogger(Agent_DistributedEA agent) {
		super(agent);
	}
	
	public void logThrowable(String message, Throwable throwable) {
		
		String line = message + " " + throwable;
		writeToFile(line);
		//ConsoleLogger.logThrowable(message, throwable);
	}
	
	public void log(Level logLevel, String message) {
		
		String line = logLevel + " " + message;
		writeToFile(line);
		//ConsoleLogger.log(logLevel, message);
	}
	
	public void log(Level logLevel, String source, String message) {
		
		String line = logLevel + " " + source + " " + message;
		writeToFile(line);
		//ConsoleLogger.log(logLevel, source, message);
	}
	
	private void writeToFile(String line) {

		String fileName = Configuration.getComputingAgentLogFile(agent.getAID());
		try {
			Writer writer = new BufferedWriter(new FileWriter(fileName, true));
			writer.append(line + "\n");
			writer.close();
		} catch (IOException e) {
			ConsoleLogger.logThrowable("Log message in Computing agent " +
					agent.getAID() + "can't be logged", e);
		}
	}

	
}
