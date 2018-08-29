package org.distributedea.input.batches.tsp.cities1083;

import java.io.IOException;

import org.distributedea.agents.computingagents.Agent_HillClimbing;
import org.distributedea.agents.computingagents.Agent_TabuSearch;
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerAgentInfo;
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerLazyQuantityOfImprovement;
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
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerThePedigree;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationConcretePlan;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationRunEachMethodOnce;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationRunEachMethodTwice;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.input.batches.IInputBatch;
import org.distributedea.input.jobs.InputTSP;
import org.distributedea.input.postprocessing.PostProcessing;
import org.distributedea.input.postprocessing.general.latex.PostProcTableOfJob;
import org.distributedea.input.postprocessing.general.latex.PostProcTableOfJobRunResults;
import org.distributedea.input.postprocessing.general.latex.PostProcTableSuccessOfPlanners;
import org.distributedea.input.postprocessing.general.matlab.PostProcBoxplot;
import org.distributedea.input.postprocessing.general.matlab.PostProcCountsOfAllottedTimeOfMethodTypes;
import org.distributedea.input.postprocessing.general.matlab.PostProcInvestigationOfInstCountOfMethodTypes;
import org.distributedea.input.postprocessing.general.matlab.PostProcInvestigationOfMedianJobRun;
import org.distributedea.input.postprocessing.general.matlab.PostProcInvestigationOfMeritsOfMethodTypes;
import org.distributedea.ontology.method.Methods;
import org.distributedea.ontology.pedigree.PedigreeCounter;
import org.distributedea.ontology.pedigreedefinition.PedigreeDefinition;

public class BatchHeteroMethodsTSP1083 implements IInputBatch {

	@Override
	public Batch batch() throws IOException {
		
		Batch batch = new Batch();
		batch.setBatchID("heteroMethodsTSP1083");
		batch.setDescription("Porovnání plánovačů v heterogenních modelech : TSP1083");
		
		Job jobI = InputTSP.test05();
		jobI.getIslandModelConfiguration().setIndividualDistribution(true);
		
		Job job0 = jobI.deepClone();
		job0.setJobID("withoutReplanning1xAll");
		job0.setDescription("Hetero without replanning all methods");
		job0.setPlanner(new PlannerInitialisationRunEachMethodOnce());
		
		Job job1 = jobI.deepClone();
		job1.setJobID("withoutReplanning2xAll");
		job1.setDescription("Hetero without replanning 2x all methods");
		job1.setPlanner(new PlannerInitialisationRunEachMethodTwice());
		
		Job job2 = jobI.deepClone();
		job2.setJobID("agentInfo");
		job2.setDescription("Based on exploatation/exploration Agent Info");
		job2.setPlanner(new PlannerAgentInfo());
		
		Job job3 = jobI.deepClone();
		job3.setJobID("random");
		job3.setDescription("Random Init, Random Kill & Random Run");
		job3.setPlanner(new PlannerRandom());
		
		Job job4 = jobI.deepClone();
		job4.setJobID("randomGuaranteeChance");
		job4.setDescription("Random Kill & Random Run");
		job4.setPlanner(new PlannerRandomGuaranteeChance());
		
		Job job5 = jobI.deepClone();
		job5.setJobID("theBestAverageOfFitness");
		job5.setDescription("The Best Average Of Fitness");
		job5.setPlanner(new PlannerTheBestAverageOfFitness());
		
		Job job6 = jobI.deepClone();
		job6.setJobID("theBestHelper");
		job6.setDescription("Follow up Helpers");
		job6.setPlanner(new PlannerTheBestHelper());
		
		Job job7 = jobI.deepClone();
		job7.setJobID("theBestResult");
		job7.setDescription("The Best Result Quantity Of Improvement Statistic");
		job7.setPlanner(new PlannerTheBestResult());
		
		Job job8 = jobI.deepClone();
		job8.setJobID("theGreatestQuantityOfGoodMaterial");
		job8.setDescription("The Greatest Quantity Of Good Genetic Material");
		job8.setPlanner(new PlannerTheGreatestQuantityOfGoodMaterial());
		
		Job job9 = jobI.deepClone();
		job9.setJobID("theGreatestQuantityOfImprovement");
		job9.setDescription("The Greatest Quantity Of Improvement Statistic");
		job9.setPlanner(new PlannerTheGreatestQuantityOfImprovement());
		
		Job job9_ = jobI.deepClone();
		job9_.setJobID("lazyQuantityOfImprovement");
		job9_.setDescription("Lazy impl. of the Greatest Quantity Of Improvement Statistic");
		job9_.setPlanner(new PlannerLazyQuantityOfImprovement());
		
		Job job10 = jobI.deepClone();
		job10.setJobID("theGreatestQuantityOfMaterial");
		job10.setDescription("The Greatest Quantity Of Genetic Material");
		job10.setPlanner(new PlannerTheGreatestQuantityOfMaterial());

		Job job11 = jobI.deepClone();
		job11.setJobID("theGreatestQMaterialGoodMaterialImprovement");
		job11.setDescription("The Combination of Greatest Quantity of Material, Good Material and Improvement");
		job11.setPlanner(new PlannerTheGreatestQMaterialGoodMaterialImprovement());
		
		Job job12 = jobI.deepClone();
		job12.setJobID("theGreatestQGoodMaterialImprovementFitness");
		job12.setDescription("The Combination of Greatest Quantity Good Material, Improvement and Fitness");
		job12.setPlanner(new PlannerTheGreatestQGoodMaterialImprovementFitness());

		Job job12_ = jobI.deepClone();
		job12_.setJobID("thePedigree");
		job12_.setDescription("The Pedigree");
		job12_.setPlanner(new PlannerThePedigree());
		job12_.setPedigreeDefinition(new PedigreeDefinition(PedigreeCounter.class));

		
		Methods algorithms = new Methods();
		algorithms.addInputMethodDescriptions(
				jobI.getMethods().exportFirstInputMethodDescription(Agent_HillClimbing.class), 15);
		algorithms.addInputMethodDescriptions(
				jobI.getMethods().exportFirstInputMethodDescription(Agent_TabuSearch.class), 1);

		Job job13 = jobI.deepClone();
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
		batch.addJob(job9_);
		batch.addJob(job10);
		batch.addJob(job11);
		batch.addJob(job12);
		batch.addJob(job12_);
		batch.addJob(job13);
		
		PostProcessing psLat0 = new PostProcTableOfJob();
		PostProcessing psLat1 = new PostProcTableOfJobRunResults(10);
		
		batch.addPostProcessings(psLat0);
		batch.addPostProcessings(psLat1);
		
		
		String YLABEL0 = "hodnota fitness v kilometrech";
		PostProcessing psMat0 = new PostProcBoxplot(YLABEL0);
		
		String YLABEL1 = "hodnota fitness v kilometrech";
		PostProcessing psMat1 = new PostProcInvestigationOfMedianJobRun(YLABEL1);

		PostProcessing psMat2 = new PostProcCountsOfAllottedTimeOfMethodTypes(false, false);

		PostProcessing psMat3 = new PostProcInvestigationOfMeritsOfMethodTypes(false, false);
		
		PostProcessing psMat4 = new PostProcInvestigationOfInstCountOfMethodTypes(false, false);
		
		PostProcessing psMat5 = new PostProcTableSuccessOfPlanners();
		
		batch.addPostProcessings(psMat0);
		batch.addPostProcessings(psMat1);
		batch.addPostProcessings(psMat2);
		batch.addPostProcessings(psMat3);
		batch.addPostProcessings(psMat4);
		batch.addPostProcessings(psMat5);
		
		return batch;
	}
	
}
