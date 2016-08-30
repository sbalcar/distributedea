package org.distributedea.logging;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;

import org.distributedea.agents.Agent_DistributedEA;
import org.distributedea.agents.systemagents.datamanager.FileNames;

public class FileLogger implements IAgentLogger {

	String fileName;
	
	public FileLogger(Agent_DistributedEA agent) {
		this.fileName = FileNames.
				getGeneralLogDirectoryForComputingAgent(agent.getAID());
	}
	
	public void logThrowable(String message, Throwable throwable) {
		
		String line = message + " " + throwable;
		writeToFile(line);
	}
	
	public void log(Level logLevel, String message) {
		
		String line = logLevel + " " + message;
		writeToFile(line);
	}
	
	public void log(Level logLevel, String source, String message) {
		
		String line = logLevel + " " + source + " " + message;
		writeToFile(line);
	}

	private void writeToFile(String line) {
		
		try {
			Writer writer = new BufferedWriter(new FileWriter(fileName, true));
			writer.append(line + "\n");
			writer.close();
		} catch (IOException e) {
			ConsoleLogger.logThrowable("Log message can't be logged", e);
		}
	}

}
