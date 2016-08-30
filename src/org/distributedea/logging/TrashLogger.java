package org.distributedea.logging;

import java.util.logging.Level;

/**
 * Logger which throw away everything
 * @author stepan
 *
 */
public class TrashLogger implements IAgentLogger {

	@Override
	public void log(Level logLevel, String message) {
	}

	@Override
	public void log(Level logLevel, String source, String message) {
	}

	@Override
	public void logThrowable(String message, Throwable throwable) {
	}

}
