package org.distributedea.ontology.individualwrapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.xml.bind.JAXBException;

import jade.content.Concept;

import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.pedigree.Pedigree;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.problems.IProblemTool;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * Ontology which contains one {@link Individual} and fitness value
 * @author stepan
 *
 */
public class IndividualEvaluated implements Concept {

	private static final long serialVersionUID = 1L;

	private Individual individual;
	
	private double fitness;

	private Pedigree pedigree;
	
	
	@Deprecated
	public IndividualEvaluated() {} // only for Jade
	
	/**
	 * Constructor
	 * @param individual
	 * @param fitness
	 */
	public IndividualEvaluated(Individual individual, double fitness, Pedigree pedigree) {
		this.individual = individual;
		this.fitness = fitness;
		this.pedigree = pedigree;
	}

	/**
	 * Copy constructor
	 * @param individualEval
	 */
	public IndividualEvaluated(IndividualEvaluated individualEval) {
		if (individualEval == null || ! individualEval.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					IndividualEvaluated.class.getSimpleName() + " is not valid");
		}
		this.individual = individualEval.getIndividual().deepClone();
		this.fitness = individualEval.getFitness();
		
		if (individualEval.getPedigree() != null) {
			this.pedigree = individualEval.getPedigree().deepClone();
		}
	}
	
	public Individual getIndividual() {
		return individual;
	}
	@Deprecated
	public void setIndividual(Individual individual) {
		this.individual = individual;
	}

	public double getFitness() {
		return fitness;
	}
	@Deprecated
	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public Pedigree getPedigree() {
		return pedigree;
	}
	public void setPedigree(Pedigree pedigree) {
		this.pedigree = pedigree;
	}
	
	public boolean validation(IProblem problem, Dataset dataset,
			IProblemTool problemTool, IAgentLogger logger) {

		if (! individual.valid(logger)) {
			return false;
		}

		try {
			problemTool.initialization(problem, dataset, null, null, logger);
		} catch (Exception e) {
			return false;
		}
		double fitnessValue = problemTool
				.fitness(individual, problem, dataset, logger);
		
		return fitness == fitnessValue;
	}
	
	
	
	@Override
	public boolean equals(Object other) {
		
	    if (other == null || !(other instanceof IndividualEvaluated)) {
	        return false;
	    }
	    
	    IndividualEvaluated ieOuther = (IndividualEvaluated)other;
	    
	    boolean areFitnessEqual =
	    		getFitness() == ieOuther.getFitness();
	    if (! areFitnessEqual) {
	    	return false;
	    }
	    	
	    boolean areIndividualsEqual =
	    		getIndividual().equals(ieOuther.getIndividual());	    
	    return areIndividualsEqual && areFitnessEqual;
	}
	
    @Override
    public int hashCode() {
    	return toString().hashCode();
    }
    
	@Override
	public String toString() {
		
		return "" + getIndividual().toString() +
				getFitness();
	}
	
	
	/**
	 * Exports structure as the XML String to the file
	 * 
	 * @throws FileNotFoundException
	 * @throws JAXBException 
	 */
	public void exportXML(File jobFile) throws Exception {

		String xml = exportXML();
		
		PrintWriter file = new PrintWriter(jobFile.getAbsolutePath());
		file.println(xml);
		file.close();
		
	}
	
	/**
	 * Exports to the XML String
	 */
	public String exportXML() {

		XStream xstream = new XStream(new DomDriver());
		xstream.setMode(XStream.NO_REFERENCES);
		xstream.autodetectAnnotations(true);
		
		return xstream.toXML(this);
	}
	
	/**
	 * Import the {@link Job} from the file
	 * 
	 * @throws FileNotFoundException
	 */
	public static IndividualEvaluated importXML(File file)
			throws Exception {

		Scanner scanner = new Scanner(file);
		String xml = scanner.useDelimiter("\\Z").next();
		scanner.close();
		
		return importXML(xml);
	}
	
	/**
	 * Import the {@link Job} from the String
	 */
	public static IndividualEvaluated importXML(String xml) {

		XStream xstream = new XStream();
		xstream.setMode(XStream.NO_REFERENCES);
		xstream.autodetectAnnotations(true);
				
		return (IndividualEvaluated) xstream.fromXML(xml);
	}
	
	/**
	 * Tests validity
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (individual == null ||
				(! individual.valid(logger))) {
			return false;
		}
		if (pedigree != null &&
				(! pedigree.valid(logger))) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Test quickly validity - doesn't test integrity of the {@link Individual}
	 * @return
	 */
	public boolean validQuickly(IAgentLogger logger) {
		if (individual == null) {
			return false;
		}
		if (pedigree != null &&
				(! pedigree.valid(logger))) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Returns clone
	 * @return
	 */
	public IndividualEvaluated deepClone() {
		return new IndividualEvaluated(this);
	}
}
