package org.distributedea.logging;

import java.util.logging.Level;

public interface IAgentLogger {
	
	public void log(Level logLevel, String message);
	
	public void log(Level logLevel, String source, String message);

	public void logThrowable(String message, Throwable throwable);
}
