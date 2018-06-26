package org.distributedea.agents.computingagents.universal.queuesofindividuals.receivedindividuals;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import org.distributedea.agents.computingagents.universal.queuesofindividuals.IReceivedIndividualsModel;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.problem.IProblem;

/**
 * Model for a set of received {@link IndividualWrapper} from distribution,
 * structurally formed from tree last {@link IndividualWrapper}s
 * @author stepan
 *
 */
public class ReceivedIndivsThreeLastIndivModel implements IReceivedIndividualsModel {

	private int QUEUE_SIZE = 3;
	private Queue<IndividualWrapper> queueOfIndivs = new ArrayBlockingQueue<>(QUEUE_SIZE);
	
	@Override
	public void addIndividual(IndividualWrapper individualW, IProblem problem,
			Dataset dataset, IAgentLogger logger) {

		if (queueOfIndivs.size() == QUEUE_SIZE) {
			removeIndividual(problem);
		}
		
		queueOfIndivs.add(individualW);
	}

	@Override
	public IndividualWrapper getIndividual(IProblem problem) {
		
		if (queueOfIndivs.isEmpty()) {
			return null;
		}
		
		return queueOfIndivs.peek();
	}

	@Override
	public IndividualWrapper removeIndividual(IProblem problem) {
		
		if (queueOfIndivs.isEmpty()) {
			return null;
		}
		
		return queueOfIndivs.poll();
	}

}
