package org.distributedea;

public class Configuration {

	public static String getConfigurationFile() {

		return "configuration" + System.getProperty("file.separator")
				+ "configuration.xml";
	}
	
	public static String getConfigurationSlaveFile() {

		return "configuration" + System.getProperty("file.separator")
				+ "configurationSlave.xml";
	}

	public static String getMethodsFile() {

		return "configuration" + System.getProperty("file.separator")
				+ "methods.xml";
	}

	public static String getInputFile(String fileName) {

		return "inputs" + System.getProperty("file.separator")
				+ fileName;
	}
}
