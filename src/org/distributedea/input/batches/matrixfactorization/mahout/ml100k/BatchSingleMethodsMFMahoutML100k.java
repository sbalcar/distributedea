package org.distributedea.input.batches.matrixfactorization.mahout.ml100k;

import org.distributedea.agents.computingagents.Agent_HillClimbing;
import org.distributedea.agents.systemagents.centralmanager.planners.onlyinit.PlannerInitialisationRunEachMethodOnce;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Batch;
import org.distributedea.agents.systemagents.centralmanager.structures.job.Job;
import org.distributedea.input.batches.IInputBatch;
import org.distributedea.input.jobs.InputMatrixFactorization;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescription;
import org.distributedea.ontology.methoddescriptioninput.InputMethodDescriptions;

public class BatchSingleMethodsMFMahoutML100k implements IInputBatch {

	@Override
	public Batch batch() throws Exception {
		
		Batch batch = new Batch();
		batch.setBatchID("singleMethodsMFMahoutML100k");
		batch.setDescription("Porovnání samostatných metod : MFMahoutML100k");
		
		Job jobI = InputMatrixFactorization.test04();
		jobI.setPlanner(new PlannerInitialisationRunEachMethodOnce());
		jobI.getIslandModelConfiguration().setIndividualDistribution(false);
		
		InputMethodDescription methodHillClimbing = jobI.deepClone().getMethods()
				.exportFirstInputMethodDescription(Agent_HillClimbing.class);
		
		Job job0 = jobI.deepClone();
		job0.setJobID("singleHillclimbing");
		job0.setDescription("Single-HillClimbing");
		job0.setMethods(new InputMethodDescriptions(methodHillClimbing));

		
		
		batch.addJob(job0);
				
		return batch;
		
	}

}
