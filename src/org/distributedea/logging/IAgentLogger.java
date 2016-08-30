package org.distributedea.logging;

import java.util.logging.Level;

/**
 * Interface for universal logger
 * @author stepan
 *
 */
public interface IAgentLogger {
	
	public void log(Level logLevel, String message);
	
	public void log(Level logLevel, String source, String message);

	public void logThrowable(String message, Throwable throwable);
}
