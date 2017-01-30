package org.distributedea.ontology;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;

import org.distributedea.logging.ConsoleLogger;
import org.distributedea.ontology.computing.StartComputing;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.configurationinput.InputAgentConfiguration;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetBinPacking;
import org.distributedea.ontology.dataset.DatasetContinuousOpt;
import org.distributedea.ontology.dataset.DatasetTSPGPS;
import org.distributedea.ontology.dataset.DatasetTSPPoint;
import org.distributedea.ontology.dataset.binpacking.ObjectBinPack;
import org.distributedea.ontology.dataset.continuousoptimization.Interval;
import org.distributedea.ontology.dataset.tsp.PositionGPS;
import org.distributedea.ontology.dataset.tsp.PositionPoint;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.job.JobID;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescriptionnumber.MethodDescriptionNumber;
import org.distributedea.ontology.methoddescriptionnumber.MethodDescriptionNumbers;
import org.distributedea.ontology.pedigree.Pedigree;
import org.distributedea.ontology.pedigree.tree.PedVertex;
import org.distributedea.ontology.pedigree.treefull.PedVertexFull;
import org.distributedea.ontology.problem.IProblem;
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
            add(PedVertexFull.class.getPackage().getName());
            add(PedVertex.class.getPackage().getName());
            add(MethodDescriptionNumbers.class);
            add(MethodDescriptionNumber.class);
            add(MethodDescription.class);
            
            add(MethodDescription.class);
            add(IProblem.class.getPackage().getName());
            add(InputAgentConfiguration.class.getPackage().getName());
            add(AgentConfiguration.class.getPackage().getName());
            
            add(JobID.class);
            
            
            add(IProblem.class.getPackage().getName());
            add(Dataset.class);
            
            add(DatasetTSPGPS.class);
            add(PositionGPS.class);
    
            add(DatasetTSPPoint.class);
            add(PositionPoint.class);
            
            add(DatasetBinPacking.class);
            add(ObjectBinPack.class);
            
            add(DatasetContinuousOpt.class);
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