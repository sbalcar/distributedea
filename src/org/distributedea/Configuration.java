package org.distributedea;

/**
 * Constants of this project wrapper
 * @author stepan
 *
 */
public class Configuration {

	/**
	 * Provides way to system agents required to run master node
	 * @return name of the file with relative path
	 */
	public static String getConfigurationFile() {

		return "configuration" + System.getProperty("file.separator")
				+ "configuration.xml";
	}
	
	/**
	 * Provides way to system agents required to run slave node
	 * @return name of the file with relative path
	 */
	public static String getConfigurationSlaveFile() {

		return "configuration" + System.getProperty("file.separator")
				+ "configurationSlave.xml";
	}

	/**
	 * Provides way to set of Methods (Computing agents) for planning to slave nodes
	 * @return name of the file with relative path
	 */
	public static String getMethodsFile() {

		return "configuration" + System.getProperty("file.separator")
				+ "methods.xml";
	}

	/**
	 * Provides way to the instance of Problem
	 * @param name of the file with relative path
	 * @return
	 */
	public static String getInputFile(String fileName) {

		return "inputs" + System.getProperty("file.separator")
				+ fileName;
	}
	
	/**
	 * Provides way to the solution instance by name
	 * @param fileName
	 * @return
	 */
	public static String getSolutionFile(String fileName) {

		return "inputs" + System.getProperty("file.separator")
				+ "solutions" + System.getProperty("file.separator")
				+ fileName;
	}

	/**
	 * Provides way to the solution instance by name
	 * @param fileName
	 * @return
	 */
	public static String getResultFile() {

		return "results.txt";
	}
}
