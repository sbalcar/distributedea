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
import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.agents.systemagents.centralmanager.structures.problemtools.ProblemTools;
import org.distributedea.input.batches.IInputBatch;
import org.distributedea.input.jobs.InputBinPacking;
import org.distributedea.input.postprocessing.PostProcessing;
import org.distributedea.input.postprocessing.latex.PostProcBatchDiffTable;
import org.distributedea.input.postprocessing.latex.PostProcJobRunsResultTable;
import org.distributedea.input.postprocessing.latex.PostProcJobTable;
import org.distributedea.input.postprocessing.matlab.PostProcBoxplot;
import org.distributedea.input.postprocessing.matlab.PostProcInvestigationOfMedianJobRun;
import org.distributedea.ontology.arguments.Argument;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.configurationinput.InputAgentConfiguration;
import org.distributedea.ontology.configurationinput.InputAgentConfigurations;
import org.distributedea.ontology.method.Methods;
import org.distributedea.problems.binpacking.permutation.ProblemToolBinPackingSimpleShift;

public class BatchHomoMethodsBPP1000 implements IInputBatch {

	public Batch batch() throws IOException {
		
		Batch batch = new Batch();
		batch.setBatchID("homoMethodsBPP1000");
		batch.setDescription("Porovnání homogeních modelů : BPP1000");
		
		Methods methods0 = new Methods(new InputAgentConfigurations(
				new InputAgentConfiguration(Agent_HillClimbing.class, new Arguments(new Argument("numberOfNeighbors", "10")))),
				new ProblemTools(ProblemToolBinPackingSimpleShift.class));

		Job job0 = InputBinPacking.test01();
		job0.setJobID("homoHillclimbing");
		job0.setDescription("Homo-HillClimbing");
		job0.setMethods(methods0);

		
		Methods methods1 = new Methods(new InputAgentConfigurations(
				new InputAgentConfiguration(Agent_RandomSearch.class, new Arguments())),
				new ProblemTools(ProblemToolBinPackingSimpleShift.class));
		
		Job job1 = InputBinPacking.test01();
		job1.setJobID("homoRandomsearch");
		job1.setDescription("Homo-RandomSearch");
		job1.setMethods(methods1);

		
		Methods methods2 = new Methods(new InputAgentConfigurations(
				new InputAgentConfiguration(Agent_Evolution.class, new Arguments(new Argument("popSize", "10"), new Argument("mutationRate", "0.9"), new Argument("crossRate", "0.1"), new Argument("selector", CompareTwoSelector.class.getName()) ))),
				new ProblemTools(ProblemToolBinPackingSimpleShift.class));

		Job job2 = InputBinPacking.test01();
		job2.setJobID("homoEvolution");
		job2.setDescription("Homo-Evolution");
		job2.setMethods(methods2);

		
		Methods methods3 = new Methods(new InputAgentConfigurations(
				new InputAgentConfiguration(Agent_BruteForce.class, new Arguments())),
				new ProblemTools(ProblemToolBinPackingSimpleShift.class));

		Job job3 = InputBinPacking.test01();
		job3.setJobID("homoBruteforce");
		job3.setDescription("Homo-BruteForce");
		job3.setMethods(methods3);
		
		
		Methods methods4 = new Methods(new InputAgentConfigurations(
				new InputAgentConfiguration(Agent_TabuSearch.class, new Arguments(new Argument("tabuModelSize", "50"), new Argument("numberOfNeighbors", "10")))),
				new ProblemTools(ProblemToolBinPackingSimpleShift.class));
		
		Job job4 = InputBinPacking.test01();
		job4.setJobID("homoTabusearch");
		job4.setDescription("Homo-TabuSearch");
		job4.setMethods(methods4);
		
		
		Methods methods5 = new Methods(new InputAgentConfigurations(
				new InputAgentConfiguration(Agent_SimulatedAnnealing.class, new Arguments(new Argument("temperature", "10000"), new Argument("coolingRate", "0.002")) )),
				new ProblemTools(ProblemToolBinPackingSimpleShift.class));
		
		Job job5 = InputBinPacking.test01();
		job5.setJobID("homoSimulatedannealing");
		job5.setDescription("Homo-SimulatedAnnealing");
		job5.setMethods(methods5);

		
		Methods methods6 = new Methods(new InputAgentConfigurations(
				new InputAgentConfiguration(Agent_DifferentialEvolution.class, new Arguments(new Argument("popSize", "50")) )),
				new ProblemTools(ProblemToolBinPackingSimpleShift.class));

		Job job6 = InputBinPacking.test01();
		job6.setJobID("homoDifferentialevolution");
		job6.setDescription("Homo-DifferentialEvolution");
		job6.setMethods(methods6);
		
		batch.addJob(job0);
		batch.addJob(job1);
		batch.addJob(job2);
		batch.addJob(job3);
		batch.addJob(job4);
		batch.addJob(job5);
		batch.addJob(job6);
		
		String YLABEL0 = "fitness jako počet košů";
		PostProcessing psMat0 = new PostProcBoxplot(YLABEL0);
		
		String XLABEL1 = "čas v sekundách";
		String YLABEL1 = "fitness jako počet košů";
		PostProcessing psMat1 = new PostProcInvestigationOfMedianJobRun(XLABEL1, YLABEL1);
		
		batch.addPostProcessings(psMat0);
		batch.addPostProcessings(psMat1);
		
		PostProcessing psLat0 = new PostProcJobRunsResultTable();
		PostProcessing psLat1 = new PostProcBatchDiffTable();
		PostProcessing psLat2 = new PostProcJobTable();
		
		batch.addPostProcessings(psLat0);
		batch.addPostProcessings(psLat1);
		batch.addPostProcessings(psLat2);
		
		return batch;
	}

}
