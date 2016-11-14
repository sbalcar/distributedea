package org.distributedea.ontology;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;

import org.distributedea.logging.ConsoleLogger;
import org.distributedea.ontology.computing.StartComputing;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescriptioncounter.MethodDescriptionCounter;
import org.distributedea.ontology.methoddescriptioncounter.MethodDescriptionCounters;
import org.distributedea.ontology.pedigree.Pedigree;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problem.ProblemContinousOpt;
import org.distributedea.ontology.problem.ProblemTSPGPS;
import org.distributedea.ontology.problem.ProblemTSPPoint;
import org.distributedea.ontology.problem.continousoptimalization.Interval;
import org.distributedea.ontology.problem.tsp.PositionGPS;
import org.distributedea.ontology.problem.tsp.PositionPoint;
import org.distributedea.ontology.problemwrapper.ProblemWrapper;

public class ComputingOntology extends BeanOntology {

	private static final long serialVersionUID = 1L;

	private ComputingOntology() {
        super("ComputingOntology");
        
        try {
            add(StartComputing.class);
            add(ProblemWrapper.class);
            
            add(IndividualWrapper.class);
            add(IndividualEvaluated.class);
            add(Individual.class.getPackage().getName());
            
            add(Pedigree.class.getPackage().getName());
            add(MethodDescriptionCounters.class);
            add(MethodDescriptionCounter.class);
            
            add(MethodDescription.class);
            add(AgentConfiguration.class.getPackage().getName());
            
            add(JobID.class);
            
            add(Problem.class);
            
            add(ProblemTSPGPS.class);
            add(PositionGPS.class);
    
            add(ProblemTSPPoint.class);
            add(PositionPoint.class);
            
            add(ProblemContinousOpt.class);
            add(Interval.class);

        } catch (Exception e) {
        	ConsoleLogger.logThrowable("Unexpected error occured:", e);
        }
    }

    static ComputingOntology theInstance = new ComputingOntology();

    public static Ontology getInstance() {
        return theInstance;
    }
}