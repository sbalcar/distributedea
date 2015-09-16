package org.distributedea.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;

public class ConsoleLogger {

	public static void logThrowable(String message, Throwable throwable) {
		System.out.println("Log: " + message);
		System.out.println(throwableToStackTrace(throwable));
	}

	public static void log(Level logLevel, String message) {
		System.out.println("Log: " + "LogLevel: " + logLevel.intValue() + " " + message);
	}

	public static void log(Level logLevel, String source, String message) {
		System.out.println("Log: " + "LogLevel: " + logLevel.intValue() + " " + message);
		System.out.println(message);
	}
	
	private static String throwableToStackTrace(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		Throwable ttI = t;
		while (ttI != null) {
			ttI.printStackTrace(pw);
			ttI = ttI.getCause();
			if (ttI != null) {
				pw.print("caused by: ");
			}
		}
		return sw.toString();
	}
}