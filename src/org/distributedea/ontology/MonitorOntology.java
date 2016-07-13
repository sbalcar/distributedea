package org.distributedea.ontology;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;

import org.distributedea.logging.ConsoleLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.configuration.Argument;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.monitor.Statistic;


public class MonitorOntology extends BeanOntology {

	private static final long serialVersionUID = 1L;
	
	private MonitorOntology() {
        super("MonitorOntology");

        String statisticPackage = Statistic.class.getPackage().getName();
        String individualPackage = Individual.class.getPackage().getName();
        
        try {
            add(JobID.class);
            
            add(individualPackage);
            add(IndividualEvaluated.class);
            
            add(statisticPackage);
        	
            add(AgentDescription.class);
            add(AgentConfiguration.class);
            add(Argument.class);
            
        } catch (Exception e) {
        	ConsoleLogger.logThrowable("Unexpected error occured:", e);
        }
    }

    static MonitorOntology theInstance = new MonitorOntology();

    public static Ontology getInstance() {
        return theInstance;
    }
}