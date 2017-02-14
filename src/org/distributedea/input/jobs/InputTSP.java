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
import org.distributedea.agents.systemagents.centralmanager.plannerinfrastructure.endcondition.PlannerTimeRestriction;
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerTheBestHelper;
import org.distributedea.agents.systemagents.centralmanager.planners.dumy.PlannerInitialisationRunEachMethodOnce_;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationOneMethodPerCore;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.agents.systemagents.centralmanager.structures.problemtools.ProblemTools;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.ontology.configuration.Argument;
import org.distributedea.ontology.configuration.Arguments;
import org.distributedea.ontology.configurationinput.InputAgentConfiguration;
import org.distributedea.ontology.configurationinput.InputAgentConfigurations;
import org.distributedea.ontology.method.MethodsTwoSets;
import org.distributedea.ontology.problem.ProblemTSPGPS;
import org.distributedea.ontology.problem.ProblemTSPPoint;
import org.distributedea.problems.tsp.gps.permutation.ProblemToolGPSEuc2D2opt;
import org.distributedea.problems.tsp.point.permutation.ProblemToolPoint2opt;

/**
 * Defines a set of TSP inputs
 */
public class InputTSP {
	
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
		job.setJobID("tsp01");
		job.setNumberOfRuns(1);
		job.setIndividualDistribution(true);
		job.setProblem(new ProblemTSPGPS());
		job.importDatasetFile(new File(
				FileNames.getInputProblemFile("simpleTest.tsp")));
		job.setMethods(new MethodsTwoSets(
				algorithms, new ProblemTools(ProblemToolGPSEuc2D2opt.class)));
		
		job.setPlanner(new PlannerInitialisationRunEachMethodOnce_());
		job.setPlannerEndCondition(new PlannerTimeRestriction(50));
		
		return job;
	}
	
	public static Job test02() throws IOException {
		
		InputAgentConfigurations algorithms = test01().getMethods()
				.exportInputAgentConfigurations().deepClone();
				
		Job job = new Job();
		job.setJobID("tsp02");
		job.setNumberOfRuns(1);
		job.setIndividualDistribution(true);
		job.setProblem(new ProblemTSPGPS());
		job.importDatasetFile(new File(
				FileNames.getInputProblemFile("wi29.tsp")));
		job.setMethods(new MethodsTwoSets(
				algorithms, new ProblemTools(ProblemToolGPSEuc2D2opt.class)));
				
		job.setPlanner(new PlannerInitialisationRunEachMethodOnce_());
		job.setPlannerEndCondition(new PlannerTimeRestriction(50));
		
		return job;
	}

	public static Job test03() throws IOException {

		InputAgentConfigurations algorithms = test01().getMethods()
				.exportInputAgentConfigurations().deepClone();

		Job job = new Job();
		job.setJobID("tsp03");
		job.setNumberOfRuns(1);
		job.setIndividualDistribution(true);
		job.setProblem(new ProblemTSPPoint());
		job.importDatasetFile(new File(
				FileNames.getInputProblemFile("djb2036.tsp")));
		job.setMethods(new MethodsTwoSets(
				algorithms, new ProblemTools(ProblemToolPoint2opt.class)));
		
		job.setPlanner(new PlannerInitialisationRunEachMethodOnce_());
		job.setPlannerEndCondition(new PlannerTimeRestriction(50));
		
		return job;
	}
	
	public static Job test04() throws IOException {

		InputAgentConfigurations algorithms = test01().getMethods()
				.exportInputAgentConfigurations().deepClone();

		Job job = new Job();
		job.setJobID("tsp04");
		job.setNumberOfRuns(1);
		job.setIndividualDistribution(true);
		job.setProblem(new ProblemTSPGPS());
		job.importDatasetFile(new File(
				FileNames.getInputProblemFile("xit1083.tsp")));
		job.setMethods(new MethodsTwoSets(
				algorithms, new ProblemTools(ProblemToolGPSEuc2D2opt.class)));
		
		job.setPlanner(new PlannerTheBestHelper());
		job.setPlannerEndCondition(new PlannerTimeRestriction(50));
		
		return job;
	}

	public static Job test05() throws IOException {

		InputAgentConfigurations algorithms = test01().getMethods()
				.exportInputAgentConfigurations().deepClone();

		Job job = new Job();
		job.setNumberOfRuns(3);
		job.setIndividualDistribution(true);
		job.setProblem(new ProblemTSPGPS());
		job.importDatasetFile(new File(
				FileNames.getInputProblemFile("xit1083.tsp")));
		job.setMethods(new MethodsTwoSets(
				algorithms, new ProblemTools(ProblemToolGPSEuc2D2opt.class)));
		
		job.setPlanner(new PlannerInitialisationOneMethodPerCore());
		job.setPlannerEndCondition(new PlannerTimeRestriction(50));
		
		return job;
	}

}
