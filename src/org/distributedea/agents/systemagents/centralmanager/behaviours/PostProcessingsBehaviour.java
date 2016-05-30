package org.distributedea.agents.systemagents.centralmanager.behaviours;

import java.util.List;

import org.distributedea.agents.systemagents.datamanager.FilesystemTool;
import org.distributedea.input.postprocessing.PostProcessing;
import org.distributedea.ontology.job.noontology.Batch;

import jade.core.behaviours.OneShotBehaviour;

public class PostProcessingsBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;

	private Batch batch;
	
	public PostProcessingsBehaviour(Batch batch) {
		this.batch = batch;
	}
	
	@Override
	public void action() {

		List<PostProcessing> postProcessings = batch.getPostProcessings();
		
		for (PostProcessing postProcI : postProcessings) {
			postProcI.run(batch);
		}
		
		FilesystemTool.copyMatlabToResultDir(batch.getBatchID());
	}

}
