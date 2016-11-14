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
import org.distributedea.ontology.methoddescriptioncounter.MethodDescriptionCounter;
import org.distributedea.ontology.methoddescriptioncounter.MethodDescriptionCounters;
import org.distributedea.ontology.monitor.Statistic;
import org.distributedea.ontology.pedigree.PedigreeCounter;


public class MonitorOntology extends BeanOntology {

	private static final long serialVersionUID = 1L;
	
	private MonitorOntology() {
        super("MonitorOntology");

        try {
            add(JobID.class);
            
            add(IndividualWrapper.class);
            add(IndividualEvaluated.class);
            add(Individual.class.getPackage().getName());
            
            add(PedigreeCounter.class.getPackage().getName());
            add(MethodDescriptionCounters.class);
            add(MethodDescriptionCounter.class);
            add(MethodDescription.class);
            		
            add(AgentConfiguration.class.getPackage().getName());

            add(Statistic.class.getPackage().getName());
            add(MethodDescriptions.class);
        	
        } catch (Exception e) {
        	ConsoleLogger.logThrowable("Unexpected error occured:", e);
        }
    }

    static MonitorOntology theInstance = new MonitorOntology();

    public static Ontology getInstance() {
        return theInstance;
    }
}