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
import org.distributedea.ontology.configurationinput.InputAgentConfiguration;
import org.distributedea.ontology.configurationinput.InputAgentConfigurations;
import org.distributedea.ontology.islandmodel.IslandModelConfiguration;
import org.distributedea.ontology.method.MethodsTwoSets;
import org.distributedea.ontology.problem.ProblemVertexCover;
import org.distributedea.problems.vertexcover.ProblemToolVC;

/**
 * Defines a set of vertex cover inputs
 */
public class InputVC {

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
		job.setJobID("frb59265");
		job.setDescription("description");
		job.setNumberOfRuns(9);
		job.setIndividualDistribution(true);
		job.setIslandModelConfiguration(
				new IslandModelConfiguration(60000, 5000));
		job.setProblem(new ProblemVertexCover());
		job.importDatasetFile(new File(
				FileNames.getInputProblemFile("frb59-26-5.mis")));
		job.setMethods(new MethodsTwoSets(
				algorithms, new ProblemTools(ProblemToolVC.class)));
		
		job.setPlanner(new PlannerInitialisationOneMethodPerCore());
		job.setPlannerEndCondition(new PlannerEndCondIterationCountRestriction(50));
		
		return job;
	}

	public static Job test02() throws IOException {

		Job job = test01();
		job.setJobID("frb10040");
		job.setDescription("description");
		job.importDatasetFile(new File(
				FileNames.getInputProblemFile("frb100-40.mis")));
		
		return job;
	}

}
