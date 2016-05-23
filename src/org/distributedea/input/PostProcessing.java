package org.distributedea.input;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.xml.bind.JAXBException;

import org.distributedea.ontology.job.noontology.Batch;
import org.distributedea.ontology.job.noontology.JobWrapper;

import com.thoughtworks.xstream.XStream;

public abstract class PostProcessing {

	public abstract void run(Batch batch);
	
	/**
	 * Exports structure as the XML String to the file
	 * 
	 * @throws FileNotFoundException
	 * @throws JAXBException 
	 */
	public void exportXML(String fileName) throws FileNotFoundException, JAXBException {

		String xml = exportXML();
		System.out.println(xml);
		PrintWriter file = new PrintWriter(fileName);
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
	 * Import the {@link JobWrapper} from the file
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
	 * Import the {@link JobWrapper} from the String
	 */
	public static PostProcessing importXML(String xml) {

		XStream xstream = new XStream();
		xstream.setMode(XStream.NO_REFERENCES);

		xstream.aliasAttribute("type", "class");

		return (PostProcessing) xstream.fromXML(xml);
	}
}
