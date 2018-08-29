package org.distributedea.input.batches.vc.frb59265;

import org.distributedea.agents.computingagents.Agent_BruteForce;
import org.distributedea.agents.computingagents.Agent_DifferentialEvolution;
import org.distributedea.agents.computingagents.Agent_Evolution;
import org.distributedea.agents.computingagents.Agent_HillClimbing;
import org.distributedea.agents.computingagents.Agent_RandomSearch;
import org.distributedea.agents.computingagents.Agent_SimulatedAnnealing;
import org.distributedea.agents.computingagents.Agent_TabuSearch;
import org.distributedea.agents.computingagents.specific.evolution.selectors.CompareTwoSelector;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationRunEachMethodOnce;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.input.batches.IInputBatch;
import org.distributedea.input.jobs.InputVC;
import org.distributedea.input.postprocessing.PostProcessing;
import org.distributedea.input.postprocessing.general.latex.PostProcTableOfJob;
import org.distributedea.input.postprocessing.general.latex.PostProcTableOfJobRunResults;
import org.distributedea.input.postprocessing.general.matlab.PostProcBoxplot;
import org.distributedea.input.postprocessing.general.matlab.PostProcInvestigationOfMedianJobRun;
import org.distributedea.ontology.arguments.Argument;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.configurationinput.InputAgentConfiguration;
import org.distributedea.ontology.method.Methods;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescription;
import org.distributedea.ontology.problemtooldefinition.ProblemToolDefinition;
import org.distributedea.problemtools.vertexcover.set.ProblemToolVC;

public class BatchSingleMethodsVCfrb59265 implements IInputBatch {

	@Override
	public Batch batch() throws Exception {
		
		Batch batch = new Batch();
		batch.setBatchID("singleMethodsVCfrb59265");
		batch.setDescription("Porovnání samostatných metod : VCfrb59265");
		
		Job jobI = InputVC.test01();
		jobI.setPlanner(new PlannerInitialisationRunEachMethodOnce());
		jobI.getIslandModelConfiguration().setIndividualDistribution(false);
		
		InputMethodDescription method0 = new InputMethodDescription(
				new InputAgentConfiguration(Agent_HillClimbing.class, new Arguments(new Argument("numberOfNeighbors", "10"))),
				new ProblemToolDefinition(new ProblemToolVC()));
		
		Job job0 = jobI.deepClone();
		job0.setJobID("singleHillclimbing");
		job0.setDescription("Single-HillClimbing");
		job0.setMethods(new Methods(method0));

		
		InputMethodDescription method1 = new InputMethodDescription(
				new InputAgentConfiguration(Agent_RandomSearch.class, new Arguments()),
				new ProblemToolDefinition(new ProblemToolVC()));

		Job job1 = jobI.deepClone();
		job1.setJobID("singleRandomsearch");
		job1.setDescription("Single-RandomSearch");
		job1.setMethods(new Methods(method1));

		
		InputMethodDescription method2 = new InputMethodDescription(
				new InputAgentConfiguration(Agent_Evolution.class, new Arguments(new Argument("popSize", "10"), new Argument("mutationRate", "0.9"), new Argument("crossRate", "0.1"), new Argument("selector", CompareTwoSelector.class.getName()))),
				new ProblemToolDefinition(new ProblemToolVC()));

		Job job2 = jobI.deepClone();
		job2.setJobID("singleEvolution");
		job2.setDescription("Single-Evolution");
		job2.setMethods(new Methods(method2));

		
		InputMethodDescription method3 = new InputMethodDescription(
				new InputAgentConfiguration(Agent_BruteForce.class, new Arguments()),
				new ProblemToolDefinition(new ProblemToolVC()));

		Job job3 = jobI.deepClone();
		job3.setJobID("singleBruteforce");
		job3.setDescription("Single-BruteForce");
		job3.setMethods(new Methods(method3));
		

		InputMethodDescription method4 = new InputMethodDescription(
				new InputAgentConfiguration(Agent_TabuSearch.class, new Arguments(new Argument("tabuModelSize", "50"), new Argument("numberOfNeighbors", "10"))),
				new ProblemToolDefinition(new ProblemToolVC()));

		Job job4 = jobI.deepClone();
		job4.setJobID("singleTabusearch");
		job4.setDescription("Single-TabuSearch");
		job4.setMethods(new Methods(method4));

		
		InputMethodDescription method5 = new InputMethodDescription(
				new InputAgentConfiguration(Agent_SimulatedAnnealing.class, new Arguments(new Argument("temperature", "10000"), new Argument("coolingRate", "0.002"))),
				new ProblemToolDefinition(new ProblemToolVC()));

		Job job5 = jobI.deepClone();
		job5.setJobID("singleSimulatedannealing");
		job5.setDescription("Single-SimulatedAnnealing");
		job5.setMethods(new Methods(method5));

		
		InputMethodDescription method6 = new InputMethodDescription(
				new InputAgentConfiguration(Agent_DifferentialEvolution.class, new Arguments(new Argument("popSize", "50"))),
				new ProblemToolDefinition(new ProblemToolVC()));

		Job job6 = jobI.deepClone();
		job6.setJobID("singleDifferentialevolution");
		job6.setDescription("Single-DifferentialEvolution");
		job6.setMethods(new Methods(method6));
		
		batch.addJob(job0);
		batch.addJob(job1);
		batch.addJob(job2);
		batch.addJob(job3);
		batch.addJob(job4);
		batch.addJob(job5);
		batch.addJob(job6);
		
		
		PostProcessing psLat0 = new PostProcTableOfJob();
		PostProcessing psLat1 = new PostProcTableOfJobRunResults(10);

		batch.addPostProcessings(psLat0);
		batch.addPostProcessings(psLat1);
		
		
		String YLABEL0 = "velikost vrcholového pokrytí";
		PostProcessing psMat0 = new PostProcBoxplot(YLABEL0);
		
		String YLABEL1 = "velikost vrcholového pokrytí";
		PostProcessing psMat1 = new PostProcInvestigationOfMedianJobRun(YLABEL1);

		batch.addPostProcessings(psMat0);
		batch.addPostProcessings(psMat1);
		
		return batch;
	}


}
