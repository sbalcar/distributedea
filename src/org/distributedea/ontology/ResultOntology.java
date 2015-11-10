package org.distributedea.ontology;

import org.distributedea.logging.ConsoleLogger;
import org.distributedea.ontology.computing.AccessesResult;
import org.distributedea.ontology.computing.result.ResultOfComputing;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.results.PartResult;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;

public class ResultOntology extends BeanOntology {

	private static final long serialVersionUID = 1L;
	
	private ResultOntology() {
        super("ResultOntology");

        String individualPackage = Individual.class.getPackage().getName();
        
        try {
        	add(PartResult.class);
        	add(AccessesResult.class);
        	add(ResultOfComputing.class);
        	
            add(individualPackage);
        	
        } catch (Exception e) {
        	ConsoleLogger.logThrowable("Unexpected error occured:", e);
        }
    }

    static ResultOntology theInstance = new ResultOntology();

    public static Ontology getInstance() {
        return theInstance;
    }
}