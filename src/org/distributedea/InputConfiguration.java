package org.distributedea;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.input.postprocessing.PostProcessing;

import com.thoughtworks.xstream.XStream;


/**
 * Contains parameters settings for the current way of solving.
 * @author stepan
 *
 */
public class InputConfiguration {

	/**
	 * Allows automatic skipping machine, offering the option of framework,
	 * in the case of value is True straight starts computing
	 */
	public boolean automaticStart;
	
	/**
	 * Allows to kill all containers after finishing the last input {@link Batch}
	 */
	public boolean automaticExit;
	
	
	/**
	 * Allows to clean results of old jobs
	 */
	public boolean automaticOldResultsRemoving;
	
	/**
	 * Allows the {@link Batch} run available {@link PostProcessing}
	 */
	//public boolean runPostProcessing;
	
	
	
	private static InputConfiguration singleton;
	
	private InputConfiguration() {
		
	}
	
	public static InputConfiguration getConf() throws Exception {
		if (singleton != null) {
			return singleton;
		}
		
		File configurationFile = new File(FileNames.getInputConfigurationFile());

		singleton = InputConfiguration.importXML(configurationFile);

		return singleton;
	}

	/**
	 * Exports structure as the XML String to the file
	 * @param dir
	 * @throws Exception
	 */
	public void exportXML(File dir) throws Exception {

		if (dir == null || ! dir.isDirectory()) {
			throw new IllegalArgumentException("Argument " +
					File.class.getSimpleName() + " is not valid");
		}
		
		String jobIDFile = dir.getAbsolutePath() + File.separator + "inputConfiguration.xml";
		File file = new File(jobIDFile);
		file.getParentFile().mkdirs(); 
		
		String xml = exportXML();
		
		PrintWriter fileWr = new PrintWriter(jobIDFile);
		fileWr.println(xml);
		fileWr.close();
	}
	
	/**
	 * Exports to the XML String
	 * @return
	 */
	private String exportXML() {

		XStream xstream = new XStream();
		xstream.setMode(XStream.NO_REFERENCES);
		xstream.alias("inputConfiguration", InputConfiguration.class);
		
		return xstream.toXML(this);
	}
	
	/**
	 * Import the {@link InputConfiguration} from the file
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static InputConfiguration importXML(File file)
			throws Exception {

		if (file == null || ! file.isFile()) {
			throw new IllegalArgumentException("Argument " +
					File.class.getSimpleName() + " is not valid");
		}

		Scanner scanner = new Scanner(file);
		String xml = scanner.useDelimiter("\\Z").next();
		scanner.close();

		return importXML(xml);
	}

	/**
	 * Import the {@link InputConfiguration} from the String
	 * @param xml
	 * @return
	 */
	private static InputConfiguration importXML(String xml) {

		XStream xstream = new XStream();
		xstream.setMode(XStream.NO_REFERENCES);
		xstream.alias("inputConfiguration", InputConfiguration.class);

		return (InputConfiguration) xstream.fromXML(xml);
	}
	
	
	public static void main(String [] args) throws Exception {
		
		InputConfiguration input = new InputConfiguration();
		input.exportXML(new File("configuration" + File.separator));
		
		//InputConfiguration conf = InputConfiguration.importXML(
		//		new File("configuration/inputConfiguration.xml"));
	}
	
}
