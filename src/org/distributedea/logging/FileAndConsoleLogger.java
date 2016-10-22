package org.distributedea.logging;

import java.util.logging.Level;

import org.distributedea.agents.Agent_DistributedEA;

public class FileAndConsoleLogger extends FileLogger {

	public FileAndConsoleLogger(Agent_DistributedEA agent) {
		super(agent);
	}
	
	public void logThrowable(String message, Throwable throwable) {
		
		String line = message + " " + throwable;
		System.out.println(line);
		writeToFile(line);
	}
	
	public void log(Level logLevel, String message) {
		
		String line = logLevel + " " + message;
		System.out.println(line);
		writeToFile(line);
	}
	
	public void log(Level logLevel, String source, String message) {
		
		String line = logLevel + " " + source + " " + message;
		System.out.println(line);
		writeToFile(line);
	}
	
}
