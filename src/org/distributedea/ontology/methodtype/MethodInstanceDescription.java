package org.distributedea.ontology.methodtype;

import jade.content.Concept;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;

import com.thoughtworks.xstream.XStream;

/**
 * Ontology represents one instance of method
 * @author stepan
 *
 */
public class MethodInstanceDescription implements Concept {

	private static final long serialVersionUID = 1L;
	
	private MethodType methodType;
		
	private int instanceNumber;
	
	@Deprecated
	public MethodInstanceDescription() {} // only for Jade

	/**
	 * Constructor
	 * @param methodType
	 * @param instanceNumber
	 */
	public MethodInstanceDescription(MethodType methodType, int instanceNumber) {
		if (methodType == null || ! methodType.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodType.class.getSimpleName() + " is not valid");
		}
		if (instanceNumber < 0) {
			throw new IllegalArgumentException("Argument " +
					Integer.class.getSimpleName() + " is not valid");
		}
		this.methodType = methodType;
		this.instanceNumber = instanceNumber;
	}
	
	
	public MethodType getMethodType() {
		return methodType;
	}
	@Deprecated
	public void setMethodType(MethodType methodType) {
		this.methodType = methodType;
	}

	public Class<?> exportAgentClass() {
		return methodType.exportAgentClass();
	}
	public Class<?> exportProblemToolClass() {
		return methodType.exportProblemToolClass();
	}
	
	
	public int getInstanceNumber() {
		return instanceNumber;
	}
	@Deprecated
	public void setInstanceNumber(int instanceNumber) {
		this.instanceNumber = instanceNumber;
	}

	public String exportInstanceName() {
		return "" + exportAgentClass().getSimpleName() + "-" +
				exportProblemToolClass().getSimpleName() + "-" +
				instanceNumber;
	}
	
	public boolean exportAreTheSameType(MethodType methodType) {
		
		return methodType.exportAgentClass() == exportAgentClass() &&
				methodType.exportProblemToolClass() == exportProblemToolClass();
	}
	
	public boolean exportAreTheSameType(AgentDescription agentDescription) {
		if (agentDescription == null) {
			return false;
		}
		Class<?> agentClass = agentDescription.getAgentConfiguration().exportAgentClass();
		Class<?> problemToolClass = agentDescription.exportProblemToolClass();
		
		return exportAgentClass() == agentClass &&
				exportProblemToolClass() == problemToolClass;
	}
	
	/**
	 * Test validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (methodType == null || ! methodType.valid(logger)) {
			return false;
		}
		if (instanceNumber < 0) {
			return false;
		}
		return true;
	}
	
	public boolean equals(Object other) {
		
	    if (!(other instanceof MethodInstanceDescription)) {
	        return false;
	    }
	    
	    MethodInstanceDescription methodInstOuther = (MethodInstanceDescription)other;
	    
	    return this.toString().equals(methodInstOuther);
	}
	
    @Override
    public int hashCode() {
    	return toString().hashCode();
    }
    
	@Override
	public String toString() {
		
		return methodType.toString() + instanceNumber;
	}
	
	public MethodInstanceDescription exportClone() {
		
		MethodInstanceDescription clone = new MethodInstanceDescription();
		clone.setMethodType(methodType.exportClone());
		clone.setInstanceNumber(instanceNumber);
		return clone;
	}
	
	/**
	 * Exports structure as the XML String to the file
	 * 
	 * @throws IOException
	 */
	public void exportXML(File dir) throws IOException {		
		
		if (dir == null || ! dir.isDirectory()) {
			throw new IllegalArgumentException("Argument " +
					File.class.getSimpleName() + " is not valid");
		}
		
		String fileName = dir.getAbsolutePath() + File.separator + "instance.xml";
		
		String xml = exportXML();

		PrintWriter fileWr = new PrintWriter(fileName);
		fileWr.println(xml);
		fileWr.close();
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
	 * Import the {@link MethodInstanceDescription} from the file
	 * 
	 * @throws FileNotFoundException
	 */
	public static MethodInstanceDescription importXML(File file)
			throws FileNotFoundException {
		
		if (file == null || (!file.exists()) || (!file.isFile())) {
			return null;
		}
		
		Scanner scanner = new Scanner(file);
		String xml = scanner.useDelimiter("\\Z").next();
		scanner.close();

		return importXML(xml);
		
	}

	/**
	 * Import the {@link MethodInstanceDescription} from the String
	 */
	public static MethodInstanceDescription importXML(String xml) {

		XStream xstream = new XStream();
		xstream.setMode(XStream.NO_REFERENCES);

		return (MethodInstanceDescription) xstream.fromXML(xml);
	}

}
