package org.distributedea.input.batches.tsp.cities1083;

import java.io.IOException;

import org.distributedea.agents.computingagents.Agent_HillClimbing;
import org.distributedea.agents.computingagents.Agent_TabuSearch;
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerAgentInfo;
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerRandom;
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerRandomGuaranteeChance;
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerTheBestAverageOfFitness;
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerTheBestHelper;
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerTheBestResult;
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerTheGreatestQGoodMaterialImprovementFitness;
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerTheGreatestQMaterialGoodMaterialImprovement;
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerTheGreatestQuantityOfGoodMaterial;
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerTheGreatestQuantityOfImprovement;
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerTheGreatestQuantityOfMaterial;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationConcretePlan;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationOneMethodPerCore;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationRunEachMethodOnce;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.input.batches.IInputBatch;
import org.distributedea.input.jobs.InputTSP;
import org.distributedea.input.postprocessing.PostProcessing;
import org.distributedea.input.postprocessing.latex.PostProcBatchDiffTable;
import org.distributedea.input.postprocessing.latex.PostProcJobRunsResultTable;
import org.distributedea.input.postprocessing.latex.PostProcJobTable;
import org.distributedea.input.postprocessing.matlab.PostProcBoxplot;
import org.distributedea.input.postprocessing.matlab.PostProcInvestigationOfMedianJobRun;
import org.distributedea.ontology.arguments.Argument;
import org.distributedea.ontology.arguments.Arguments;
import org.distributedea.ontology.configurationinput.InputAgentConfiguration;
import org.distributedea.ontology.method.Methods;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescription;
import org.distributedea.problems.tsp.gps.permutation.ProblemToolGPSEuc2D2opt;

public class BatchHeteroMethodsTSP1083 implements IInputBatch {

	@Override
	public Batch batch() throws IOException {
		
		Batch batch = new Batch();
		batch.setBatchID("heteroMethodsTSP1083");
		batch.setDescription("Porovnání plánovačů v heterogenních modelech : TSP1083");
		
		Job job0 = InputTSP.test05();
		job0.setJobID("initialisationOneMethodPerCore");
		job0.setDescription("Initialisation One Method Per Core");
		job0.setPlanner(new PlannerInitialisationOneMethodPerCore());
		
		Job job1 = InputTSP.test05();
		job1.setJobID("initialisationRunEachMethodOnce");
		job1.setDescription("Initialisation Run Each Method Once");
		job1.setPlanner(new PlannerInitialisationRunEachMethodOnce());
		
		Job job2 = InputTSP.test05();
		job2.setJobID("agentInfo");
		job2.setDescription("Based on exploatation/exploration Agent Info");
		job2.setPlanner(new PlannerAgentInfo());
		
		Job job3 = InputTSP.test05();
		job3.setJobID("random");
		job3.setDescription("Random Init, Random Kill & Random Run");
		job3.setPlanner(new PlannerRandom());
		
		Job job4 = InputTSP.test05();
		job4.setJobID("randomGuaranteeChance");
		job4.setDescription("Random Kill & Random Run");
		job4.setPlanner(new PlannerRandomGuaranteeChance());
		
		Job job5 = InputTSP.test05();
		job5.setJobID("theBestAverageOfFitness");
		job5.setDescription("The Best Average Of Fitness");
		job5.setPlanner(new PlannerTheBestAverageOfFitness());
		
		Job job6 = InputTSP.test05();
		job6.setJobID("theBestHelper");
		job6.setDescription("Follow up Helpers");
		job6.setPlanner(new PlannerTheBestHelper());
		
		Job job7 = InputTSP.test05();
		job7.setJobID("theBestResult");
		job7.setDescription("The Best Result Quantity Of Improvement Statistic");
		job7.setPlanner(new PlannerTheBestResult());
		
		Job job8 = InputTSP.test05();
		job8.setJobID("theGreatestQuantityOfGoodMaterial");
		job8.setDescription("The Greatest Quantity Of Good Genetic Material");
		job8.setPlanner(new PlannerTheGreatestQuantityOfGoodMaterial());
		
		Job job9 = InputTSP.test05();
		job9.setJobID("theGreatestQuantityOfImprovement");
		job9.setDescription("The Greatest Quantity Of Improvement Statistic");
		job9.setPlanner(new PlannerTheGreatestQuantityOfImprovement());
		
		Job job10 = InputTSP.test05();
		job10.setJobID("theGreatestQuantityOfMaterial");
		job10.setDescription("The Greatest Quantity Of Genetic Material");
		job10.setPlanner(new PlannerTheGreatestQuantityOfMaterial());

		Job job11 = InputTSP.test05();
		job11.setJobID("theGreatestQMaterialGoodMaterialImprovement");
		job11.setDescription("The Combination of Greatest Quantity of Material, Good Material and Improvement");
		job11.setPlanner(new PlannerTheGreatestQMaterialGoodMaterialImprovement());
		
		Job job12 = InputTSP.test05();
		job12.setJobID("theGreatestQGoodMaterialImprovementFitness");
		job12.setDescription("The Combination of Greatest Quantity Good Material, Improvement and Fitness");
		job12.setPlanner(new PlannerTheGreatestQGoodMaterialImprovementFitness());
		
		
		Methods algorithms = new Methods();
		algorithms.addAgentDescriptions(new InputMethodDescription(
				new InputAgentConfiguration(Agent_HillClimbing.class, new Arguments(new Argument("numberOfNeighbors", "10"))),
				ProblemToolGPSEuc2D2opt.class), 15);
		algorithms.addAgentDescriptions(new InputMethodDescription(
				new InputAgentConfiguration(Agent_TabuSearch.class, new Arguments(new Argument("tabuModelSize", "50"), new Argument("numberOfNeighbors", "10") )),
				ProblemToolGPSEuc2D2opt.class), 1);

		Job job13 = InputTSP.test05();
		job13.setJobID("onlyInitHillClimbingAndTabuSearch");
		job13.setDescription("Only initialization 15x Hillclimbing and 1x Tabu search");
		job13.setPlanner(new PlannerInitialisationConcretePlan(algorithms));

		
		batch.addJob(job0);
		batch.addJob(job1);
		batch.addJob(job2);
		batch.addJob(job3);
		batch.addJob(job4);
		batch.addJob(job5);
		batch.addJob(job6);
		batch.addJob(job7);
		batch.addJob(job8);
		batch.addJob(job9);
		batch.addJob(job10);
		batch.addJob(job11);
		batch.addJob(job12);
		batch.addJob(job13);
		
		String YLABEL0 = "hodnota fitness v kilometrech";
		PostProcessing psMat0 = new PostProcBoxplot(YLABEL0);
		
		String XLABEL1 = "čas v sekundách";
		String YLABEL1 = "hodnota fitness v kilometrech";
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
