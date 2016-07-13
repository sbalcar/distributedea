package org.distributedea.input.batches;

import org.distributedea.agents.systemagents.centralmanager.planner.PlannerFollowBestResult;
import org.distributedea.agents.systemagents.centralmanager.planner.PlannerFollowup3Helpers;
import org.distributedea.agents.systemagents.centralmanager.planner.PlannerFollowupHelpers;
import org.distributedea.agents.systemagents.centralmanager.planner.PlannerMethodDescription;
import org.distributedea.agents.systemagents.centralmanager.planner.PlannerRandom;
import org.distributedea.agents.systemagents.centralmanager.planner.PlannerFollowTheGreatestQuantityOfImprovement;
import org.distributedea.agents.systemagents.centralmanager.planner.initialisation.PlannerInitialisation;
import org.distributedea.agents.systemagents.centralmanager.planner.initialisation.PlannerInitialisationState;
import org.distributedea.input.batches.jobs.InputTSP;
import org.distributedea.input.postprocessing.PostProcessing;
import org.distributedea.input.postprocessing.latex.PostProcBatchDiffTable;
import org.distributedea.input.postprocessing.latex.PostProcJobTable;
import org.distributedea.input.postprocessing.matlab.PostProcBoxplot;
import org.distributedea.input.postprocessing.matlab.PostProcComparing;
import org.distributedea.ontology.job.noontology.Batch;
import org.distributedea.ontology.job.noontology.Job;

public class BatchHeteroComparingTSP extends InputBatch {

	@Override
	public Batch batch() {
		
		Batch batch = new Batch();
		batch.setBatchID("heteroComparingTSP");
		batch.setDescription("Porovnání plánovačů v heterogenních modelech : TSP ");
		
		Job jobW0 = InputTSP.test06();
		jobW0.setJobID("onlyInit");
		jobW0.setDescription("Only Initialization");
		PlannerInitialisationState state = PlannerInitialisationState.RUN_ONE_AGENT_PER_CORE;
		jobW0.setPlanner(new PlannerInitialisation(state, true));
		
		Job jobW1 = InputTSP.test06();
		jobW1.setJobID("followBestResult");
		jobW1.setDescription("Follow Best Result");
		jobW1.setPlanner(new PlannerFollowBestResult());
		
		Job jobW2 = InputTSP.test06();
		jobW2.setJobID("followupHelpers");
		jobW2.setDescription("Follow up Helpers");
		jobW2.setPlanner(new PlannerFollowupHelpers());

		Job jobW3 = InputTSP.test06();
		jobW3.setJobID("followup3Helpers");
		jobW3.setDescription("Follow up three Helpers");
		jobW3.setPlanner(new PlannerFollowup3Helpers());

		Job jobW4 = InputTSP.test06();
		jobW4.setJobID("random");
		jobW4.setDescription("Random Kill & Random Run");
		jobW4.setPlanner(new PlannerRandom());

		Job jobW5 = InputTSP.test06();
		jobW5.setJobID("methodDescription");
		jobW5.setDescription("Method Description");
		jobW5.setPlanner(new PlannerMethodDescription());

		Job jobW6 = InputTSP.test06();
		jobW6.setJobID("theGreatestQuantityOfImprovement");
		jobW6.setDescription("The Greatest Quantity Of Improvement Statistic");
		jobW6.setPlanner(new PlannerFollowTheGreatestQuantityOfImprovement());
		
		batch.addJobWrapper(jobW0);
		batch.addJobWrapper(jobW1);
		batch.addJobWrapper(jobW2);
		batch.addJobWrapper(jobW3);
		batch.addJobWrapper(jobW4);
		batch.addJobWrapper(jobW5);
		batch.addJobWrapper(jobW6);
		
		PostProcessing psMat0 = new PostProcBoxplot();
		PostProcessing psMat1 = new PostProcComparing();
		
		batch.addPostProcessings(psMat0);
		batch.addPostProcessings(psMat1);
		
		PostProcessing psLat0 = new PostProcBatchDiffTable();
		PostProcessing psLat1 = new PostProcJobTable();
		
		batch.addPostProcessings(psLat0);
		batch.addPostProcessings(psLat1);
		
		return batch;
	}
	
}
