package org.distributedea.ontology;

import org.distributedea.logging.ConsoleLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.computing.AccessesResult;
import org.distributedea.ontology.computing.result.ResultOfComputing;
import org.distributedea.ontology.helpmate.ReportHelpmate;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.saveresult.SaveResultOfComputing;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;

public class ResultOntology extends BeanOntology {

	private static final long serialVersionUID = 1L;
	
	private ResultOntology() {
        super("ResultOntology");

        String individualPackage = Individual.class.getPackage().getName();
        String helpmatePackage = ReportHelpmate.class.getPackage().getName();
        String agentDescriptionPackage = AgentDescription.class.getPackage().getName();
        
        try {
        	add(SaveResultOfComputing.class);
        	add(AccessesResult.class);
        	add(ResultOfComputing.class);
        	
        	add(helpmatePackage);
        	
            add(IndividualWrapper.class);
            add(individualPackage);
            add(JobID.class);
            
            add(agentDescriptionPackage);
        	
        } catch (Exception e) {
        	ConsoleLogger.logThrowable("Unexpected error occured:", e);
        }
    }

    static ResultOntology theInstance = new ResultOntology();

    public static Ontology getInstance() {
        return theInstance;
    }
}