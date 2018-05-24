package org.distributedea.ontology.monitor;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individualhash.IndividualHash;

import com.thoughtworks.xstream.XStream;

import jade.content.Concept;

/**
 * Ontology represents one statistical result of method. Values are counted
 * by monitoring Individuals which is sending the current agent.
 * @author stepan
 *
 */
public class MethodStatisticResult implements Concept {

	private static final long serialVersionUID = 1L;

	private int numberOfTheBestCreatedIndividuals;
	
	private int numberOfGoodCreatedMaterial;
	
	private int numberOfIndividuals;
	
	private int numberOfTypeIndividuals;
	
	private IndividualHash bestIndividual;
	
	private double fitnessAverage;
	
	
	public int getNumberOfTheBestCreatedIndividuals() {
		return numberOfTheBestCreatedIndividuals;
	}
	public void setNumberOfTheBestCreatedIndividuals(int numberOfTheBestCreatedIndividuals) {
		this.numberOfTheBestCreatedIndividuals = numberOfTheBestCreatedIndividuals;
	}
	
	public int getNumberOfGoodCreatedMaterial() {
		return numberOfGoodCreatedMaterial;
	}
	public void setNumberOfGoodCreatedMaterial(int numberOfGoodCreatedMaterial) {
		this.numberOfGoodCreatedMaterial = numberOfGoodCreatedMaterial;
	}
	
	public int getNumberOfIndividuals() {
		return numberOfIndividuals;
	}
	public void setNumberOfIndividuals(int numberOfIndividuals) {
		this.numberOfIndividuals = numberOfIndividuals;
	}
	
	public int getNumberOfTypeIndividuals() {
		return numberOfTypeIndividuals;
	}
	public void setNumberOfTypeIndividuals(int numberOfTypeIndividuals) {
		this.numberOfTypeIndividuals = numberOfTypeIndividuals;
	}
	
	public IndividualHash getBestIndividual() {
		return bestIndividual;
	}
	public void setBestIndividual(IndividualHash bestIndividual) {
		this.bestIndividual = bestIndividual;
	}
	
	public double getFitnessAverage() {
		return fitnessAverage;
	}
	public void setFitnessAverage(double fitnessAverage) {
		this.fitnessAverage = fitnessAverage;
	}

	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (bestIndividual == null) {
			return false;
		}
		return true;
	}

	public boolean equals(Object other) {
		
	    if (!(other instanceof MethodStatisticResult)) {
	        return false;
	    }
	    
	    MethodStatisticResult methodOuther = (MethodStatisticResult)other;

	    if ((this.bestIndividual != null && methodOuther.bestIndividual == null) ||
	    		(this.bestIndividual == null && methodOuther.bestIndividual != null)) {
	    	return false;
	    }
	    
	    boolean equal =
		    this.numberOfTheBestCreatedIndividuals ==
		    	methodOuther.numberOfTheBestCreatedIndividuals &&
			this.numberOfGoodCreatedMaterial ==
				methodOuther.numberOfGoodCreatedMaterial &&
			this.numberOfIndividuals ==
				methodOuther.numberOfIndividuals &&
			this.numberOfTypeIndividuals ==
				methodOuther.numberOfTypeIndividuals;

		if (bestIndividual != null) {
			equal &= this.bestIndividual.equals(methodOuther.bestIndividual);
		}

		equal &= this.fitnessAverage == methodOuther.fitnessAverage;
	    
	    return equal;
	}

    @Override
    public int hashCode() {
    	return toString().hashCode();
    }
    
	@Override
	public String toString() {
		
		String string = "" +
				numberOfTheBestCreatedIndividuals +
				numberOfGoodCreatedMaterial +
				numberOfIndividuals +
				numberOfTypeIndividuals;
		
		if (bestIndividual != null) {
			string += bestIndividual.toString();
		}
		
		string += fitnessAverage;

		return string;
	}
	
	/**
	 * Exports structure as the XML String to the file
	 * 
	 * @throws IOException 
	 */
	public void exportXML(File methodStatisticDir) throws IOException {

		if (methodStatisticDir == null || ! methodStatisticDir.isDirectory()) {
			throw new IllegalArgumentException("Argument " +
					File.class.getSimpleName() + " is not valid");
		}
		
		String fileName = methodStatisticDir.getAbsolutePath() +
				File.separator + "statistic.xml";
		
		String xml = exportXML();

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
	 * Import the {@link MethodStatisticResult} from the file
	 * 
	 * @throws IOException
	 */
	public static MethodStatisticResult importXML(File file)
			throws IOException {

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
	 * Import the {@link MethodStatisticResult} from the String
	 */
	public static MethodStatisticResult importXML(String xml) {

		XStream xstream = new XStream();
		xstream.setMode(XStream.NO_REFERENCES);

		return (MethodStatisticResult) xstream.fromXML(xml);
	}
}
