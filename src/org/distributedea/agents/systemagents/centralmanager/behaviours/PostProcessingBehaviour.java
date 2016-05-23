package org.distributedea.agents.systemagents.centralmanager.behaviours;

import org.distributedea.input.PostProcessing;
import org.distributedea.ontology.job.noontology.Batch;

import jade.core.behaviours.OneShotBehaviour;

public class PostProcessingBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;

	private PostProcessing postProcessing;
	private Batch batch;
	
	public PostProcessingBehaviour(PostProcessing postProcessing, Batch batch) {
		this.postProcessing = postProcessing;
		this.batch = batch;
	}
	
	@Override
	public void action() {
		
		postProcessing.run(batch);
		
	}

}
