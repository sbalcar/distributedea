package org.distributedea.input.postprocessing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.xml.bind.JAXBException;

import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.logging.IAgentLogger;

import com.thoughtworks.xstream.XStream;

public abstract class PostProcessing {

	/**
	 * Runs {@link PostProcessing}
	 * @param batch
	 * @param logger
	 */
	public abstract void run(Batch batch) throws Exception;
	
	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		return true;
	}
	
	/**
	 * Exports structure as the XML String to the file
	 * 
	 * @throws FileNotFoundException
	 * @throws JAXBException 
	 */
	public void exportXML(File postProcFile) throws IOException {

		String xml = exportXML();
		
		PrintWriter file = new PrintWriter(postProcFile.getAbsolutePath());
		file.println(xml);
		file.close();
		
	}
	
	/**
	 * Exports to the XML String
	 */
	public String exportXML() {

		XStream xstream = new XStream();
		xstream.setMode(XStream.NO_REFERENCES);
		
		return xstream.toXML(this);
	}
	
	/**
	 * Import the {@link Job} from the file
	 * 
	 * @throws FileNotFoundException
	 */
	public static PostProcessing importXML(File file)
			throws FileNotFoundException {

		Scanner scanner = new Scanner(file);
		String xml = scanner.useDelimiter("\\Z").next();
		scanner.close();

		return importXML(xml);
		
	}

	/**
	 * Import the {@link Job} from the String
	 */
	public static PostProcessing importXML(String xml) {

		XStream xstream = new XStream();
		xstream.setMode(XStream.NO_REFERENCES);

		return (PostProcessing) xstream.fromXML(xml);
	}
}
