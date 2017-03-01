package org.distributedea.ontology.dataset.continuousoptimization;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.xml.bind.JAXBException;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.IndividualPoint;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public abstract class DomainDefinition {

	public abstract Interval exportRestriction(int index);
	public abstract boolean valid(IAgentLogger logger);
	public abstract DomainDefinition deepClone();
	
	public boolean valueIsCorrect(IndividualPoint individual) {
		if (individual == null) {
			throw new IllegalArgumentException("Arguent " +
					IndividualPoint.class.getSimpleName() + " is not valid");
		}
		
		for (int i = 0; i < individual.getCoordinates().size(); i++) {
			
			Interval intervalI = exportRestriction(i);
			double coordI = individual.exportCoordinate(i);
			if (! intervalI.contain(coordI)) {
				return false;
			}
		}
		
		return true;
	}
	
	
	/**
	 * Exports structure as the XML String to the file
	 * 
	 * @throws FileNotFoundException
	 * @throws JAXBException 
	 */
	public void exportXML(File file) throws Exception {

		String xml = exportXML();
		
		PrintWriter writer = new PrintWriter(file.getAbsolutePath());
		writer.println(xml);
		writer.close();
		
	}
	
	/**
	 * Exports to the XML String
	 */
	public String exportXML() {

		XStream xstream = new XStream(new DomDriver());
		xstream.setMode(XStream.NO_REFERENCES);
		xstream.autodetectAnnotations(true);
		
		processAliases(xstream);
		
		return xstream.toXML(this);
	}
	
	/**
	 * Import the {@link DomainDefinition} from the file
	 * 
	 * @throws FileNotFoundException
	 */
	public static DomainDefinition importXML(File file)
			throws Exception {

		Scanner scanner = new Scanner(file);
		String xml = scanner.useDelimiter("\\Z").next();
		scanner.close();
		
		return importXML(xml);
	}
	
	/**
	 * Import the {@link DomainDefinition} from the String
	 */
	public static DomainDefinition importXML(String xml) {

		XStream xstream = new XStream();
		xstream.setMode(XStream.NO_REFERENCES);
		xstream.autodetectAnnotations(true);
		
		processAliases(xstream);
		
		return (DomainDefinition) xstream.fromXML(xml);
	}
	
	private static void processAliases(XStream xstream) {
		
		xstream.alias("domainDefinition", DomainDefinition.class);
		xstream.alias("dimensionRestriction", DimensionRestriction.class);

		xstream.alias("allDimensionsRestriction", AllDimensionsRestriction.class);
		xstream.alias("concreteDimensionsRestriction", ConcreteDimensionsRestriction.class);
		xstream.alias("interval", Interval.class);
	}	
}
