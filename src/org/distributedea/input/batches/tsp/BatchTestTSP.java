package org.distributedea.input.batches.tsp;

import org.distributedea.agents.systemagents.centralmanager.planners.PlannerRandom;
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerTheBestHelper;
import org.distributedea.agents.systemagents.centralmanager.planners.PlannerTheGreatestQuantityOfImprovement;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.input.batches.IInputBatch;
import org.distributedea.input.jobs.InputTSP;
import org.distributedea.input.postprocessing.PostProcessing;
import org.distributedea.input.postprocessing.matlab.PostProcBoxplot;
import org.distributedea.input.postprocessing.matlab.PostProcInvestigationOfMedianJobRun;
import org.distributedea.input.postprocessing.matlab.PostProcInvestigationOfMethods;
import org.distributedea.input.postprocessing.matlab.PostProcAllottedTimeOfAgents;
import org.distributedea.input.postprocessing.matlab.PostProcAllottedTimeOfMethodTypes;
import org.distributedea.input.postprocessing.matlab.PostProcMeritsOfMethodTypes;
import org.distributedea.ontology.pedigree.PedigreeCounter;
import org.distributedea.ontology.pedigree.PedigreeTree;
import org.distributedea.ontology.pedigree.PedigreeTreeFull;


public class BatchTestTSP implements IInputBatch {

	@Override
	public Batch batch() {
		
		Batch batch = new Batch();
		batch.setBatchID("testTSP");
		batch.setDescription("Porovnání plánovačů v heterogenních modelech : TSP ");

		Job jobW2 = InputTSP.test06();
		jobW2.setJobID("followupHelpers");
		jobW2.setDescription("Follow up Helpers");
		jobW2.setPlanner(new PlannerTheBestHelper());
		jobW2.importPedigreeOfIndividualClassName(PedigreeTree.class);
		
		Job jobW4 = InputTSP.test06();
		jobW4.setJobID("random");
		jobW4.setDescription("Random Kill & Random Run");
		jobW4.setPlanner(new PlannerRandom());
		jobW4.importPedigreeOfIndividualClassName(PedigreeCounter.class);
		
		Job jobW6 = InputTSP.test06();
		jobW6.setJobID("theGreatestQuantityOfImprovement");
		jobW6.setDescription("The Greatest Quantity Of Improvement Statistic");
		jobW6.setPlanner(new PlannerTheGreatestQuantityOfImprovement());
		jobW6.importPedigreeOfIndividualClassName(PedigreeTreeFull.class);
		
		batch.addJobWrapper(jobW2);
		batch.addJobWrapper(jobW4);
		batch.addJobWrapper(jobW6);
		
		PostProcessing post1 = new PostProcInvestigationOfMedianJobRun();
		PostProcessing post2 = new PostProcBoxplot();
		PostProcessing post3 = new PostProcInvestigationOfMethods();
		PostProcessing post4 = new PostProcAllottedTimeOfMethodTypes();
		PostProcessing post5 = new PostProcAllottedTimeOfAgents();
		PostProcessing post6 = new PostProcMeritsOfMethodTypes();
		
		batch.addPostProcessings(post1);
		batch.addPostProcessings(post2);
		batch.addPostProcessings(post3);
		batch.addPostProcessings(post4);
		batch.addPostProcessings(post5);
		batch.addPostProcessings(post6);
		
		return batch;
	}
}
