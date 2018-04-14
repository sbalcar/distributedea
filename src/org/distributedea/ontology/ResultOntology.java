package org.distributedea.ontology;

import org.distributedea.logging.ConsoleLogger;
import org.distributedea.ontology.arguments.Argument;
import org.distributedea.ontology.argumentsdefinition.ArgumentDef;
import org.distributedea.ontology.computing.AccessesResult;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.helpmate.ReportHelpmate;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.latentfactors.LatentFactor;
import org.distributedea.ontology.data.SaveTheBestIndividual;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.iteration.Iteration;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescriptionnumber.MethodDescriptionNumber;
import org.distributedea.ontology.methoddescriptionnumber.MethodDescriptionNumbers;
import org.distributedea.ontology.methodtype.MethodInstanceDescription;
import org.distributedea.ontology.monitor.MethodStatisticResult;
import org.distributedea.ontology.pedigree.Pedigree;
import org.distributedea.ontology.pedigree.tree.PedVertex;
import org.distributedea.ontology.pedigree.treefull.PedVertexFull;
import org.distributedea.ontology.plan.Plan;
import org.distributedea.ontology.plan.RePlan;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problem.matrixfactorization.ILatFactDefinition;
import org.distributedea.ontology.saveresult.ResultOfIteration;
import org.distributedea.ontology.saveresult.ResultOfMethodInstanceIteration;
import org.distributedea.ontology.saveresult.SaveResultOfIteration;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;

public class ResultOntology extends BeanOntology {

	private static final long serialVersionUID = 1L;
	
	private ResultOntology() {
        super("ResultOntology");
        
        try {
        	add(SaveTheBestIndividual.class);
        	add(AccessesResult.class);
        	
        	add(Iteration.class);
        	
        	add(SaveResultOfIteration.class);
        	add(ResultOfIteration.class);
        	add(ResultOfMethodInstanceIteration.class);
        	add(MethodStatisticResult.class);
        	add(Plan.class);
        	add(RePlan.class);
        	
        	add(MethodInstanceDescription.class);
        	
        	add(ReportHelpmate.class.getPackage().getName());
        	
            add(IndividualWrapper.class);
            add(Individual.class.getPackage().getName());
            
            add(LatentFactor.class.getPackage().getName());
            
            add(Pedigree.class.getPackage().getName());
            add(PedVertexFull.class.getPackage().getName());
            add(PedVertex.class.getPackage().getName());
            
            add(JobID.class);

            add(MethodDescriptionNumbers.class);
            add(MethodDescriptionNumber.class);
            add(MethodDescription.class);
            
            add(MethodDescription.class.getPackage().getName());
            add(IProblem.class.getPackage().getName());
            add(ILatFactDefinition.class.getPackage().getName());
            
            add(Argument.class.getPackage().getName());
            add(ArgumentDef.class.getPackage().getName());
            add(AgentConfiguration.class.getPackage().getName());
        	
        } catch (Exception e) {
        	ConsoleLogger.logThrowable("Unexpected error occured:", e);
        }

    }

    static ResultOntology theInstance = new ResultOntology();

    public static Ontology getInstance() {
        return theInstance;
    }
}