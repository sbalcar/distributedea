package org.distributedea.input.batches.binpacking.objects1000;

import java.io.IOException;

import org.distributedea.agents.computingagents.Agent_BruteForce;
import org.distributedea.agents.computingagents.Agent_DifferentialEvolution;
import org.distributedea.agents.computingagents.Agent_Evolution;
import org.distributedea.agents.computingagents.Agent_HillClimbing;
import org.distributedea.agents.computingagents.Agent_RandomSearch;
import org.distributedea.agents.computingagents.Agent_SimulatedAnnealing;
import org.distributedea.agents.computingagents.Agent_TabuSearch;
import org.distributedea.agents.computingagents.computingagent.evolution.selectors.CompareTwoSelector;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationRunEachMethodOnce;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.agents.systemagents.centralmanager.structures.problemtools.ProblemTools;
import org.distributedea.input.batches.IInputBatch;
import org.distributedea.input.jobs.InputBinPacking;
import org.distributedea.input.postprocessing.PostProcessing;
import org.distributedea.input.postprocessing.latex.PostProcJobRunsResultTable;
import org.distributedea.input.postprocessing.matlab.PostProcBoxplot;
import org.distributedea.input.postprocessing.matlab.PostProcInvestigationOfMedianJobRun;
import org.distributedea.ontology.configuration.Argument;
import org.distributedea.ontology.configuration.Arguments;
import org.distributedea.ontology.configurationinput.InputAgentConfiguration;
import org.distributedea.ontology.configurationinput.InputAgentConfigurations;
import org.distributedea.ontology.method.Methods;
import org.distributedea.problems.binpacking.permutation.ProblemToolBinPackingSimpleShift;

public class BatchSingleMethodsBPP1000 implements IInputBatch {

	@Override
	public Batch batch() throws IOException {
		
		Batch batch = new Batch();
		batch.setBatchID("singleMethodsBPP1000");
		batch.setDescription("Porovnání samostatných metod : BPP1000");
		
		Job jobI = InputBinPacking.test01();
		jobI.setJobID("0");
		jobI.setDescription("clone");
		jobI.setPlanner(new PlannerInitialisationRunEachMethodOnce());
		
		Methods methods0 = new Methods(new InputAgentConfigurations(
				new InputAgentConfiguration(Agent_HillClimbing.class, new Arguments(new Argument("numberOfNeighbors", "10")))),
				new ProblemTools(ProblemToolBinPackingSimpleShift.class));
		
		Job job0 = jobI.deepClone();
		job0.setJobID("singleHillclimbing");
		job0.setDescription("Single-HillClimbing");
		job0.setMethods(methods0);
		
		
		Methods methods1 = new Methods(new InputAgentConfigurations(
				new InputAgentConfiguration(Agent_RandomSearch.class, new Arguments())),
				new ProblemTools(ProblemToolBinPackingSimpleShift.class));
		
		Job job1 = jobI.deepClone();
		job1.setJobID("singleRandomsearch");
		job1.setDescription("Single-RandomSearch");
		job1.setMethods(methods1);

		
		Methods methods2 = new Methods(new InputAgentConfigurations(
				new InputAgentConfiguration(Agent_Evolution.class, new Arguments(new Argument("popSize", "10"), new Argument("mutationRate", "0.9"), new Argument("crossRate", "0.1"), new Argument("selector", CompareTwoSelector.class.getName()) ))),
				new ProblemTools(ProblemToolBinPackingSimpleShift.class));

		Job job2 = jobI.deepClone();
		job2.setJobID("singleEvolution");
		job2.setDescription("Single-Evolution");
		job2.setMethods(methods2);
		

		Methods methods3 = new Methods(new InputAgentConfigurations(
				new InputAgentConfiguration(Agent_BruteForce.class, new Arguments())),
				new ProblemTools(ProblemToolBinPackingSimpleShift.class));

		Job job3 = jobI.deepClone();
		job3.setJobID("singleBruteforce");
		job3.setDescription("Single-BruteForce");
		job3.setMethods(methods3);

		
		Methods methods4 = new Methods(new InputAgentConfigurations(
				new InputAgentConfiguration(Agent_TabuSearch.class, new Arguments(new Argument("tabuModelSize", "50"), new Argument("numberOfNeighbors", "10")))),
				new ProblemTools(ProblemToolBinPackingSimpleShift.class));
		
		Job job4 = jobI.deepClone();
		job4.setJobID("singleTabusearch");
		job4.setDescription("Single-TabuSearch");
		job4.setMethods(methods4);
		

		Methods methods5 = new Methods(new InputAgentConfigurations(
				new InputAgentConfiguration(Agent_SimulatedAnnealing.class, new Arguments(new Argument("temperature", "10000"), new Argument("coolingRate", "0.002")))),
				new ProblemTools(ProblemToolBinPackingSimpleShift.class));

		Job job5 = jobI.deepClone();
		job5.setJobID("singleSimulatedannealing");
		job5.setDescription("Single-SimulatedAnnealing");
		job5.setMethods(methods5);

		
		Methods methods6 = new Methods(new InputAgentConfigurations(
				new InputAgentConfiguration(Agent_DifferentialEvolution.class, new Arguments(new Argument("popSize", "50")))),
				new ProblemTools(ProblemToolBinPackingSimpleShift.class));

		Job job6 = jobI.deepClone();
		job6.setJobID("singleDifferentialevolution");
		job6.setDescription("Single-DifferentialEvolution");
		job6.setMethods(methods6);
		

		String YLABEL0 = "fitness jako počet košů";
		PostProcessing ps0 = new PostProcBoxplot(YLABEL0);
		
		String XLABEL1 = "čas v sekundách";
		String YLABEL1 = "fitness jako počet košů";
		PostProcessing ps1 = new PostProcInvestigationOfMedianJobRun(XLABEL1, YLABEL1);

		PostProcessing psLat0 = new PostProcJobRunsResultTable();
		
		batch.addJobWrapper(job0);
		batch.addJobWrapper(job1);
		batch.addJobWrapper(job2);
		batch.addJobWrapper(job3);
		batch.addJobWrapper(job4);
		batch.addJobWrapper(job5);
		batch.addJobWrapper(job6);

		batch.addPostProcessings(ps0);
		batch.addPostProcessings(ps1);
		
		batch.addPostProcessings(psLat0);
		
		return batch;
	}
}
