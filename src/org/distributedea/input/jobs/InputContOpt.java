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
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerTheBestHelper;
import org.distributedea.agents.systemagents.centralmanager.planners.dumy.PlannerDummy;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationOneMethodPerCore;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.agents.systemagents.centralmanager.structures.problemtools.ProblemTools;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.ontology.arguments.Argument;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.configurationinput.InputAgentConfiguration;
import org.distributedea.ontology.configurationinput.InputAgentConfigurations;
import org.distributedea.ontology.islandmodel.IslandModelConfiguration;
import org.distributedea.ontology.method.MethodsTwoSets;
import org.distributedea.ontology.problem.ProblemContinuousOpt;
import org.distributedea.problems.continuousoptimization.ProblemToolCORandomMove;

/**
 * Defines a set of Continuous Optimization
 */
public class InputContOpt {

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
		
		Job job = new Job();
		job.setJobID("co01");
		job.setNumberOfRuns(1);
		job.setIslandModelConfiguration(
				new IslandModelConfiguration(false, 60000, 5000));
		job.setProblem(new ProblemContinuousOpt("f01", 2, true));
		job.importDatasetFile(new File(
				FileNames.getInputProblemFile("bbob.co")));
		job.setMethods(new MethodsTwoSets(
				algorithms, new ProblemTools(ProblemToolCORandomMove.class) ));
		job.setPlanner(new PlannerDummy());
		job.setPlannerEndCondition(new PlannerEndCondIterationCountRestriction(50));
		
		return job;
	}
	
	public static Job test02_() throws IOException {
		
		InputAgentConfigurations algorithms = test01().getMethods()
				.exportInputAgentConfigurations().deepClone();

		Job job = new Job();
		job.setJobID("co02");
		job.setNumberOfRuns(1);
		job.setIslandModelConfiguration(
				new IslandModelConfiguration(true, 60000, 5000));
		job.setProblem(new ProblemContinuousOpt("f01", 2, true));
		job.importDatasetFile(new File(
				FileNames.getInputProblemFile("bbob.co")));
		job.setMethods(new MethodsTwoSets(
				algorithms, new ProblemTools(ProblemToolCORandomMove.class)));
		
		job.setPlanner(new PlannerTheBestHelper());
		job.setPlannerEndCondition(new PlannerEndCondIterationCountRestriction(50));
		
		return job;
	}
	
	public static Job test03() throws IOException {

		InputAgentConfigurations algorithms = test01().getMethods()
				.exportInputAgentConfigurations().deepClone();

		Job job = new Job();
		job.setJobID("co03");
		job.setDescription("description");
		job.setNumberOfRuns(1);
		job.setIslandModelConfiguration(
				new IslandModelConfiguration(true, 60000, 5000));
		job.setProblem(new ProblemContinuousOpt("f01", 2, true));
		job.importDatasetFile(new File(
				FileNames.getInputProblemFile("bbob.co")));
		job.setMethods(new MethodsTwoSets(
				algorithms, new ProblemTools(ProblemToolCORandomMove.class)));
		
		job.setPlanner(new PlannerInitialisationOneMethodPerCore());
		job.setPlannerEndCondition(new PlannerEndCondIterationCountRestriction(50));

		return job;
	}
	
	public static Job test04_() throws IOException {

		InputAgentConfigurations algorithms = test01().getMethods()
				.exportInputAgentConfigurations().deepClone();

		Job job = new Job();
		job.setJobID("f2");
		job.setDescription("description");
		job.setNumberOfRuns(3);
		job.setIslandModelConfiguration(
				new IslandModelConfiguration(false, 70000, 5000));
		job.setProblem(new ProblemContinuousOpt("f2", 2, false));
		job.importDatasetFile(new File(
				FileNames.getInputProblemFile("f2.co")));
		job.setMethods(new MethodsTwoSets(
				algorithms, new ProblemTools(ProblemToolCORandomMove.class)));
		
		job.setPlanner(new PlannerDummy());
		job.setPlannerEndCondition(new PlannerEndCondIterationCountRestriction(50));
		
		return job;
	}

	public static Job test02() throws IOException {

		InputAgentConfigurations algorithms = test01().getMethods()
				.exportInputAgentConfigurations().deepClone();

		Job job = new Job();
		job.setJobID("f02");
		job.setDescription("Bbob f02");
		job.setNumberOfRuns(9);
		job.setIslandModelConfiguration(
				new IslandModelConfiguration(false, 60000, 5000));
		job.setProblem(new ProblemContinuousOpt("f02", 10, false));
		job.importDatasetFile(new File(
				FileNames.getInputProblemFile("bbob.co")));
		job.setMethods(new MethodsTwoSets(
				algorithms, new ProblemTools(ProblemToolCORandomMove.class)));
		
		job.setPlanner(new PlannerInitialisationOneMethodPerCore());
		job.setPlannerEndCondition(new PlannerEndCondIterationCountRestriction(50));
		
		return job;

	}
	
	public static Job test04() throws IOException {

		Job job = test02();
		job.setJobID("f04");
		job.setDescription("Bbob f04");
		job.setIslandModelConfiguration(
				new IslandModelConfiguration(false, 60000, 5000));
		job.setProblem(new ProblemContinuousOpt("f04", 10, false));
		
		return job;
	}

	public static Job test08() throws IOException {
		
		Job job = test02();
		job.setJobID("f08");
		job.setDescription("Bbob f08");
		job.setIslandModelConfiguration(
				new IslandModelConfiguration(false, 60000, 5000));
		job.setProblem(new ProblemContinuousOpt("f08", 10, false));
		
		return job;
	}
	
	public static Job test10() throws IOException {
		
		Job job = test02();
		job.setJobID("f10");
		job.setDescription("Bbob f10");
		job.setIslandModelConfiguration(
				new IslandModelConfiguration(false, 60000, 5000));
		job.setProblem(new ProblemContinuousOpt("f10", 10, false));
		
		return job;
	}

	public static Job test14() throws IOException {
		
		Job job = test02();
		job.setJobID("f14");
		job.setDescription("Bbob f14");
		job.setIslandModelConfiguration(
				new IslandModelConfiguration(false, 60000, 5000));
		job.setProblem(new ProblemContinuousOpt("f14", 10, false));
		
		return job;
	}

	public static Job test17() throws IOException {
		
		Job job = test02();
		job.setJobID("f17");
		job.setDescription("Bbob f17");
		job.setIslandModelConfiguration(
				new IslandModelConfiguration(false, 60000, 5000));
		job.setProblem(new ProblemContinuousOpt("f17", 10, false));
		
		return job;
	}
	
}
