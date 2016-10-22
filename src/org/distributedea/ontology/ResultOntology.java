package org.distributedea.ontology;

import org.distributedea.logging.ConsoleLogger;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.computing.AccessesResult;
import org.distributedea.ontology.helpmate.ReportHelpmate;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.SaveBestIndividual;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.methodtype.MethodInstanceDescription;
import org.distributedea.ontology.monitor.MethodStatisticResult;
import org.distributedea.ontology.plan.Plan;
import org.distributedea.ontology.plan.RePlan;
import org.distributedea.ontology.saveresult.ResultOfIteration;
import org.distributedea.ontology.saveresult.ResultOfMethodInstanceIteration;
import org.distributedea.ontology.saveresult.SaveResultOfIteration;

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
        	add(SaveBestIndividual.class);
        	add(AccessesResult.class);
        	
        	add(Iteration.class);
        	
        	add(SaveResultOfIteration.class);
        	add(ResultOfIteration.class);
        	add(ResultOfMethodInstanceIteration.class);
        	add(MethodStatisticResult.class);
        	add(Plan.class);
        	add(RePlan.class);
        	
        	add(MethodInstanceDescription.class);
        	
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