package org.distributedea.input.jobs;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.distributedea.agents.computingagents.Agent_BruteForce;
import org.distributedea.agents.computingagents.Agent_DifferentialEvolution;
import org.distributedea.agents.computingagents.Agent_Evolution;
import org.distributedea.agents.computingagents.Agent_HillClimbing;
import org.distributedea.agents.computingagents.Agent_RandomSearch;
import org.distributedea.agents.computingagents.Agent_SimulatedAnnealing;
import org.distributedea.agents.computingagents.Agent_TabuSearch;
import org.distributedea.agents.computingagents.computingagent.evolution.selectors.CompareTwoSelector;
import org.distributedea.agents.systemagents.centralmanager.plannerinfrastructure.endcondition.PlannerEndCondIterationCountRestriction;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationOneMethodPerCore;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.agents.systemagents.centralmanager.structures.problemtools.ProblemTools;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.ontology.arguments.Argument;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.argumentsdefinition.ArgumentDefDouble;
import org.distributedea.ontology.argumentsdefinition.ArgumentDefInteger;
import org.distributedea.ontology.argumentsdefinition.ArgumentsDef;
import org.distributedea.ontology.configurationinput.InputAgentConfiguration;
import org.distributedea.ontology.configurationinput.InputAgentConfigurations;
import org.distributedea.ontology.islandmodel.IslandModelConfiguration;
import org.distributedea.ontology.method.MethodsTwoSets;
import org.distributedea.ontology.problem.ProblemMachineLearning;
import org.distributedea.problems.machinelearning.ProblemToolMLRandomMove;

import weka.classifiers.functions.MultilayerPerceptron;

public class InputMachineLearning {

	public static Job test01() throws IOException {
		
		InputAgentConfigurations algorithms = new InputAgentConfigurations(Arrays.asList(
				new InputAgentConfiguration(Agent_HillClimbing.class, new Arguments(new Argument("numberOfNeighbors", "10"))),
				new InputAgentConfiguration(Agent_RandomSearch.class, new Arguments()),
				new InputAgentConfiguration(Agent_Evolution.class, new Arguments(new Argument("popSize", "10"), new Argument("mutationRate", "0.9"), new Argument("crossRate", "0.1"), new Argument("selector", CompareTwoSelector.class.getName()) )),
				new InputAgentConfiguration(Agent_BruteForce.class, new Arguments()),
				new InputAgentConfiguration(Agent_TabuSearch.class, new Arguments(new Argument("tabuModelSize", "50"), new Argument("numberOfNeighbors", "10") )),
				new InputAgentConfiguration(Agent_SimulatedAnnealing.class, new Arguments(new Argument("temperature", "10000"), new Argument("coolingRate", "0.002") )),
				new InputAgentConfiguration(Agent_DifferentialEvolution.class, new Arguments(new Argument("popSize", "50")) )
			));
		
		Class<?> classifier = weka.classifiers.trees.J48.class;
		Class<?> filter = weka.filters.unsupervised.instance.Randomize.class;
		ProblemMachineLearning problem = new ProblemMachineLearning(
				classifier, filter, new ArgumentsDef());
		
		Job job = new Job();
		job.setJobID("mlIris");
		job.setDescription("description");
		job.setNumberOfRuns(3);
		job.setIndividualDistribution(false);
		job.setIslandModelConfiguration(
				new IslandModelConfiguration(150000, 5000));
		job.setProblem(problem);
		job.importDatasetFile(new File(
				FileNames.getInputProblemFile("iris.arff")));
		job.setMethods(new MethodsTwoSets(
				algorithms, new ProblemTools(ProblemToolMLRandomMove.class) ));
		job.setPlanner(new PlannerInitialisationOneMethodPerCore());
		job.setPlannerEndCondition(new PlannerEndCondIterationCountRestriction(50));
		
		return job;
	}
	
	public static Job test02() throws IOException {
		
		Class<?> classifier = MultilayerPerceptron.class;
		Class<?> filter = weka.filters.unsupervised.instance.Randomize.class;
		
		ArgumentsDef argumentsDef = new ArgumentsDef();
		argumentsDef.addArgumentsDef(new ArgumentDefDouble("L", 0, 1)); // default 0.3
		argumentsDef.addArgumentsDef(new ArgumentDefDouble("M", 0, 1)); // default 0.2
		//argumentsDef.addArgumentsDef(new ArgumentDefInteger("N", 100, 500));  // default 500
		argumentsDef.addArgumentsDef(new ArgumentDefInteger("E", 20, 20));  // default 20
	    
		ProblemMachineLearning problem = new ProblemMachineLearning(classifier, filter, argumentsDef);
		
		Job job = test01();
		job.setJobID("mlZoo");
		job.setDescription("description");
		job.setNumberOfRuns(9);
		job.setIslandModelConfiguration(
				new IslandModelConfiguration(150000, 5000));
		job.importDatasetFile(new File(
				FileNames.getInputProblemFile("zoo.arff")));
		job.setProblem(problem);
		
		return job;
	}
}
