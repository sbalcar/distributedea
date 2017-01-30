package org.distributedea.ontology;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;

import org.distributedea.logging.ConsoleLogger;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescription.MethodDescriptions;
import org.distributedea.ontology.methoddescriptionnumber.MethodDescriptionNumber;
import org.distributedea.ontology.methoddescriptionnumber.MethodDescriptionNumbers;
import org.distributedea.ontology.monitor.StartMonitoring;
import org.distributedea.ontology.monitor.Statistic;
import org.distributedea.ontology.pedigree.Pedigree;
import org.distributedea.ontology.pedigree.tree.PedVertex;
import org.distributedea.ontology.pedigree.treefull.PedVertexFull;
import org.distributedea.ontology.problem.IProblem;


public class MonitorOntology extends BeanOntology {

	private static final long serialVersionUID = 1L;
	
	private MonitorOntology() {
        super("MonitorOntology");

        try {
        	add(StartMonitoring.class);
            add(JobID.class);
            add(IProblem.class.getPackage().getName());
            add(MethodDescriptions.class);
            
            add(IndividualWrapper.class);
            add(IndividualEvaluated.class);
            add(Individual.class.getPackage().getName());
            
            add(Pedigree.class.getPackage().getName());
            add(PedVertexFull.class.getPackage().getName());
            add(PedVertex.class.getPackage().getName());
            
            add(MethodDescriptionNumbers.class);
            add(MethodDescriptionNumber.class);
            add(MethodDescription.class);

            add(Statistic.class.getPackage().getName());
            add(AgentConfiguration.class.getPackage().getName());
        	
        } catch (Exception e) {
        	ConsoleLogger.logThrowable("Unexpected error occured:", e);
        }
    }

    static MonitorOntology theInstance = new MonitorOntology();

    public static Ontology getInstance() {
        return theInstance;
    }
}