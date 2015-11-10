package org.distributedea.ontology;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;

import org.distributedea.logging.ConsoleLogger;
import org.distributedea.ontology.computing.StartComputing;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problem.ProblemTSPGPS;
import org.distributedea.ontology.problem.ProblemTSPPoint;
import org.distributedea.ontology.problem.tsp.PositionGPS;
import org.distributedea.ontology.problem.tsp.PositionPoint;

public class ComputingOntology extends BeanOntology {

	private static final long serialVersionUID = 1L;

	private ComputingOntology() {
        super("ComputingOntology");

        String individualPackage = Individual.class.getPackage().getName();
        
        try {
            add(StartComputing.class);
            add(individualPackage);
            
            add(Problem.class);
            
            add(ProblemTSPGPS.class);
            add(PositionGPS.class);
    
            add(ProblemTSPPoint.class);
            add(PositionPoint.class);

        } catch (Exception e) {
        	ConsoleLogger.logThrowable("Unexpected error occured:", e);
        }
    }

    static ComputingOntology theInstance = new ComputingOntology();

    public static Ontology getInstance() {
        return theInstance;
    }
}