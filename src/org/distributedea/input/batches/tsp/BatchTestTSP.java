package org.distributedea.input.batches.tsp;

import java.io.IOException;

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
import org.distributedea.input.postprocessing.matlab.PostProcInvestigationOfInstPresenceOfMethods;
import org.distributedea.input.postprocessing.matlab.PostProcCountsOfAllottedTimeOfMethods;
import org.distributedea.input.postprocessing.matlab.PostProcCountsOfAllottedTimeOfMethodTypes;
import org.distributedea.input.postprocessing.matlab.PostProcCountsOfMeritsOfMethodTypes;
import org.distributedea.ontology.pedigree.PedigreeCounter;
import org.distributedea.ontology.pedigree.PedigreeTree;
import org.distributedea.ontology.pedigree.PedigreeTreeFull;


public class BatchTestTSP implements IInputBatch {

	@Override
	public Batch batch() throws IOException {
		
		Batch batch = new Batch();
		batch.setBatchID("testTSP");
		batch.setDescription("Porovnání plánovačů v heterogenních modelech : TSP ");

		Job jobW2 = InputTSP.test05();
		jobW2.setJobID("followupHelpers");
		jobW2.setDescription("Follow up Helpers");
		jobW2.setPlanner(new PlannerTheBestHelper());
		jobW2.importPedigreeOfIndividualClassName(PedigreeTree.class);
		
		Job jobW4 = InputTSP.test05();
		jobW4.setJobID("random");
		jobW4.setDescription("Random Kill & Random Run");
		jobW4.setPlanner(new PlannerRandom());
		jobW4.importPedigreeOfIndividualClassName(PedigreeCounter.class);
		
		Job jobW6 = InputTSP.test05();
		jobW6.setJobID("theGreatestQuantityOfImprovement");
		jobW6.setDescription("The Greatest Quantity Of Improvement Statistic");
		jobW6.setPlanner(new PlannerTheGreatestQuantityOfImprovement());
		jobW6.importPedigreeOfIndividualClassName(PedigreeTreeFull.class);
		
		batch.addJob(jobW2);
		batch.addJob(jobW4);
		batch.addJob(jobW6);
		
		String YLABEL1 = "hodnota fitness v kilometrech";
		PostProcessing post1 = new PostProcInvestigationOfMedianJobRun(YLABEL1);
		
		String YLABEL2 = "hodnota fitness v kilometrech";
		PostProcessing post2 = new PostProcBoxplot(YLABEL2);
		
		PostProcessing post3 = new PostProcInvestigationOfInstPresenceOfMethods();
		PostProcessing post4 = new PostProcCountsOfAllottedTimeOfMethodTypes(false, false);
		PostProcessing post5 = new PostProcCountsOfAllottedTimeOfMethods();
		PostProcessing post6 = new PostProcCountsOfMeritsOfMethodTypes(false, false);
		
		batch.addPostProcessings(post1);
		batch.addPostProcessings(post2);
		batch.addPostProcessings(post3);
		batch.addPostProcessings(post4);
		batch.addPostProcessings(post5);
		batch.addPostProcessings(post6);
		
		return batch;
	}
}
