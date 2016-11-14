package org.distributedea.input.batches;

import org.distributedea.agents.systemagents.centralmanager.planners.PlannerAgentInfo;
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerRandom;
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerRandomImpr;
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerTheBestAverageOfFitness;
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerTheBestHelper;
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerTheBestResult;
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerTheGreatestQGoodMaterialImprovementFitness;
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerTheGreatestQMaterialGoodMaterialImprovement;
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerTheGreatestQuantityOfGoodMaterial;
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerTheGreatestQuantityOfImprovement;
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerTheGreatestQuantityOfMaterial;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationOneMethodPerCore;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationRunEachMethodOnce;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.input.batches.jobs.InputTSP;
import org.distributedea.input.postprocessing.PostProcessing;
import org.distributedea.input.postprocessing.latex.PostProcBatchDiffTable;
import org.distributedea.input.postprocessing.latex.PostProcJobTable;
import org.distributedea.input.postprocessing.matlab.PostProcBoxplot;
import org.distributedea.input.postprocessing.matlab.PostProcInvestigationOfMedianJobRun;

public class BatchHeteroComparingTSP implements IInputBatch {

	@Override
	public Batch batch() {
		
		Batch batch = new Batch();
		batch.setBatchID("heteroComparingTSP");
		batch.setDescription("Porovnání plánovačů v heterogenních modelech : TSP ");
		
		Job jobW0 = InputTSP.test06();
		jobW0.setJobID("initialisationOneMethodPerCore");
		jobW0.setDescription("Initialisation One Method Per Core");
		jobW0.setPlanner(new PlannerInitialisationOneMethodPerCore());
		
		Job jobW1 = InputTSP.test06();
		jobW1.setJobID("initialisationRunEachMethodOnce");
		jobW1.setDescription("Initialisation Run Each Method Once");
		jobW1.setPlanner(new PlannerInitialisationRunEachMethodOnce());
		
		Job jobW2 = InputTSP.test06();
		jobW2.setJobID("agentInfo");
		jobW2.setDescription("Based on exploatation/exploration Agent Info");
		jobW2.setPlanner(new PlannerAgentInfo());
		
		Job jobW3 = InputTSP.test06();
		jobW3.setJobID("random");
		jobW3.setDescription("Random Init, Random Kill & Random Run");
		jobW3.setPlanner(new PlannerRandom());
		
		Job jobW4 = InputTSP.test06();
		jobW4.setJobID("randomImproved");
		jobW4.setDescription("Random Kill & Random Run");
		jobW4.setPlanner(new PlannerRandomImpr());
		
		Job jobW5 = InputTSP.test06();
		jobW5.setJobID("theBestAverageOfFitness");
		jobW5.setDescription("The Best Average Of Fitness");
		jobW5.setPlanner(new PlannerTheBestAverageOfFitness());
		
		Job jobW6 = InputTSP.test06();
		jobW6.setJobID("theBestHelper");
		jobW6.setDescription("Follow up Helpers");
		jobW6.setPlanner(new PlannerTheBestHelper());
		
		Job jobW7 = InputTSP.test06();
		jobW7.setJobID("theBestResult");
		jobW7.setDescription("The Best Result Quantity Of Improvement Statistic");
		jobW7.setPlanner(new PlannerTheBestResult());
		
		Job jobW8 = InputTSP.test06();
		jobW8.setJobID("theGreatestQuantityOGoodMaterial");
		jobW8.setDescription("The Greatest Quantity Of Good Genetic Material");
		jobW8.setPlanner(new PlannerTheGreatestQuantityOfGoodMaterial());
		
		Job jobW9 = InputTSP.test06();
		jobW9.setJobID("theGreatestQuantityOfImprovement");
		jobW9.setDescription("The Greatest Quantity Of Improvement Statistic");
		jobW9.setPlanner(new PlannerTheGreatestQuantityOfImprovement());
		
		Job jobW10 = InputTSP.test06();
		jobW10.setJobID("theGreatestQuantityOfMaterial");
		jobW10.setDescription("The Greatest Quantity Of Genetic Material");
		jobW10.setPlanner(new PlannerTheGreatestQuantityOfMaterial());

		Job jobW11 = InputTSP.test06();
		jobW11.setJobID("theGreatestQMaterialGoodMaterialImprovement");
		jobW11.setDescription("The Combination of Greatest Quantity of Material, Good Material and Improvement");
		jobW11.setPlanner(new PlannerTheGreatestQMaterialGoodMaterialImprovement());
		
		Job jobW12 = InputTSP.test06();
		jobW12.setJobID("theGreatestQGoodMaterialImprovementFitness");
		jobW12.setDescription("The Combination of Greatest Quantity Good Material, Improvement and Fitness");
		jobW12.setPlanner(new PlannerTheGreatestQGoodMaterialImprovementFitness());
				
		batch.addJobWrapper(jobW0);
		batch.addJobWrapper(jobW1);
		batch.addJobWrapper(jobW2);
		batch.addJobWrapper(jobW3);
		batch.addJobWrapper(jobW4);
		batch.addJobWrapper(jobW5);
		batch.addJobWrapper(jobW6);
		batch.addJobWrapper(jobW7);
		batch.addJobWrapper(jobW8);
		batch.addJobWrapper(jobW9);
		batch.addJobWrapper(jobW10);
		batch.addJobWrapper(jobW11);
		batch.addJobWrapper(jobW12);
		
		PostProcessing psMat0 = new PostProcBoxplot();
		PostProcessing psMat1 = new PostProcInvestigationOfMedianJobRun();
		
		batch.addPostProcessings(psMat0);
		batch.addPostProcessings(psMat1);
		
		PostProcessing psLat0 = new PostProcBatchDiffTable();
		PostProcessing psLat1 = new PostProcJobTable();
		
		batch.addPostProcessings(psLat0);
		batch.addPostProcessings(psLat1);
		
		return batch;
	}
	
}
