package org.distributedea.input.jobs;

import java.io.File;
import java.io.IOException;

import org.distributedea.agents.computingagents.Agent_BruteForce;
import org.distributedea.agents.computingagents.Agent_DifferentialEvolution;
import org.distributedea.agents.computingagents.Agent_Evolution;
import org.distributedea.agents.computingagents.Agent_HillClimbing;
import org.distributedea.agents.computingagents.Agent_RandomSearch;
import org.distributedea.agents.computingagents.Agent_SimulatedAnnealing;
import org.distributedea.agents.computingagents.Agent_TabuSearch;
import org.distributedea.agents.computingagents.specific.evolution.selectors.CompareTwoSelector;
import org.distributedea.agents.computingagents.universal.queuesofindividuals.readytosendindividuals.ReadyToSendIndivsTwoQueuesModel;
import org.distributedea.agents.computingagents.universal.queuesofindividuals.receivedindividuals.ReceivedIndivsOneQueueModel;
import org.distributedea.agents.computingagents.universal.queuesofindividualsselectors.readytosendindividual.ReadyToSendIndividualsOnlyOneInserter;
import org.distributedea.agents.computingagents.universal.queuesofindividualsselectors.receivedindividual.ReceivedIndivRemoveOneSelector;
import org.distributedea.agents.systemagents.centralmanager.plannerinfrastructure.endcondition.PlannerEndCondIterationCountRestriction;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationOneMethodPerCore;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.agents.systemagents.datamanager.FileNames;
import org.distributedea.ontology.arguments.Argument;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.configurationinput.InputAgentConfiguration;
import org.distributedea.ontology.datasetdescription.DatasetDescription;
import org.distributedea.ontology.islandmodel.IslandModelConfiguration;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescription;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescriptions;
import org.distributedea.ontology.pedigreedefinition.PedigreeDefinition;
import org.distributedea.ontology.problem.ProblemVertexCover;
import org.distributedea.ontology.problemtooldefinition.ProblemToolDefinition;
import org.distributedea.problems.vertexcover.set.ProblemToolBruteForceVC;
import org.distributedea.problems.vertexcover.set.ProblemToolDifferentialEvolutionVC;
import org.distributedea.problems.vertexcover.set.ProblemToolEvolutionVCCompleteByTheSeconCrossRemove3Mutation;
import org.distributedea.problems.vertexcover.set.ProblemToolHillClimbingVCRemoveRandomSubgraph;
import org.distributedea.problems.vertexcover.set.ProblemToolRandomSearchVC;
import org.distributedea.problems.vertexcover.set.ProblemToolSimulatedAnnealingVCRemoveRandomSubgraph;
import org.distributedea.problems.vertexcover.set.ProblemToolTabuSearchVCRemoveRandomSubgraph;

/**
 * Defines a set of Vertex Cover {@link Job}
 * @author stepan
 *
 */
public class InputVC {

	public static Job test01() throws IOException {


		InputMethodDescription methodHillClimbing = new InputMethodDescription(
				new InputAgentConfiguration(Agent_HillClimbing.class, new Arguments(new Argument("numberOfNeighbors", "10"))),
				new ProblemToolDefinition(new ProblemToolHillClimbingVCRemoveRandomSubgraph())
				);

		InputMethodDescription methodRandomSearch = new InputMethodDescription(
				new InputAgentConfiguration(Agent_RandomSearch.class, new Arguments()),
				new ProblemToolDefinition(new ProblemToolRandomSearchVC())
				);

		InputMethodDescription methodEvolution = new InputMethodDescription(
				new InputAgentConfiguration(Agent_Evolution.class, new Arguments(new Argument("popSize", "10"), new Argument("mutationRate", "0.9"), new Argument("crossRate", "0.1"), new Argument("selector", CompareTwoSelector.class.getName()))),
				new ProblemToolDefinition(new ProblemToolEvolutionVCCompleteByTheSeconCrossRemove3Mutation())
				);

		InputMethodDescription methodBruteForce = new InputMethodDescription(
				new InputAgentConfiguration(Agent_BruteForce.class, new Arguments()),
				new ProblemToolDefinition(new ProblemToolBruteForceVC())
				);

		InputMethodDescription methodTabuSearch = new InputMethodDescription(
				new InputAgentConfiguration(Agent_TabuSearch.class, new Arguments(new Argument("tabuModelSize", "50"), new Argument("numberOfNeighbors", "10"))),
				new ProblemToolDefinition(new ProblemToolTabuSearchVCRemoveRandomSubgraph())
				);

		InputMethodDescription methodSimulatedAnnealing = new InputMethodDescription(
				new InputAgentConfiguration(Agent_SimulatedAnnealing.class, new Arguments(new Argument("temperature", "10000"), new Argument("coolingRate", "0.002"))),
				new ProblemToolDefinition(new ProblemToolSimulatedAnnealingVCRemoveRandomSubgraph())
				);

		InputMethodDescription methodDifferentialEvolution = new InputMethodDescription(
				new InputAgentConfiguration(Agent_DifferentialEvolution.class, new Arguments(new Argument("popSize", "50"), new Argument("crossRate", "0.0"))),
				new ProblemToolDefinition(new ProblemToolDifferentialEvolutionVC())
				);

		InputMethodDescriptions methods = new InputMethodDescriptions();
		methods.addInputMethodDescr(methodHillClimbing);
		methods.addInputMethodDescr(methodRandomSearch);
		methods.addInputMethodDescr(methodEvolution);
		methods.addInputMethodDescr(methodBruteForce);
		methods.addInputMethodDescr(methodTabuSearch);
		methods.addInputMethodDescr(methodSimulatedAnnealing);
		methods.addInputMethodDescr(methodDifferentialEvolution);
		
		
		IslandModelConfiguration islandModelConf = new IslandModelConfiguration();
		islandModelConf.setIndividualDistribution(true);
		islandModelConf.setReplanPeriodMS(60000);
		islandModelConf.setIndividualBroadcastPeriodMS(5000);
		islandModelConf.importReadyToSendIndividualInserterClass(
				ReadyToSendIndividualsOnlyOneInserter.class);
		islandModelConf.importReceivedIndividualSelectorClass(
				ReceivedIndivRemoveOneSelector.class);
		islandModelConf.importReadyToSendIndividualsModelClass(
				ReadyToSendIndivsTwoQueuesModel.class);
		islandModelConf.importReceivedIndividualsModelClass(
				ReceivedIndivsOneQueueModel.class);
		
		Job job = new Job();
		job.setJobID("frb59265");
		job.setDescription("description");
		job.setNumberOfRuns(9);
		job.setIslandModelConfiguration(islandModelConf);
		job.setProblem(new ProblemVertexCover());
		job.setDatasetDescription(new DatasetDescription(
				new File(FileNames.getInputProblemFile("frb59-26-5.mis"))));
		job.setMethods(methods);
		
		job.setPlanner(new PlannerInitialisationOneMethodPerCore());
		job.setPlannerEndCondition(new PlannerEndCondIterationCountRestriction(50));
		job.setPedigreeDefinition(new PedigreeDefinition((Class<?>) null));
		
		return job;
	}

	public static Job test02() throws IOException {

		Job job = test01();
		job.setJobID("frb10040");
		job.setDescription("description");
		job.setDatasetDescription(new DatasetDescription(
				new File(FileNames.getInputProblemFile("frb100-40.mis"))));
		
		return job;
	}

}
