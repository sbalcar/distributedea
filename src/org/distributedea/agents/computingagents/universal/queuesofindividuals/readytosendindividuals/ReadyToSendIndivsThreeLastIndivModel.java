package org.distributedea.agents.computingagents.universal.queuesofindividuals.readytosendindividuals;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import org.distributedea.agents.computingagents.universal.queuesofindividuals.IReadyToSendIndividualsModel;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.problem.IProblem;

/**
 * Structure serves as model for {@link IndividualEvaluated}s ready to send,
 * structurally formed by queue of 3 last {@link IndividualEvaluated}
 * @author stepan
 *
 */
public class ReadyToSendIndivsThreeLastIndivModel implements IReadyToSendIndividualsModel {

	private int QUEUE_SIZE = 3;
	private Queue<IndividualEvaluated> queueOfIndivs = new ArrayBlockingQueue<>(QUEUE_SIZE);
	
	
	@Override
	public void addIndividual(List<IndividualEvaluated> individualsEval,
			IProblem problem) {
		if (individualsEval == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
		}
		
		for (IndividualEvaluated indivI : individualsEval) {
			addIndividual(indivI, problem);
		}
	}

	@Override
	public void addIndividual(IndividualEvaluated individualEval,
			IProblem problem) {
		
		if (queueOfIndivs.size() == QUEUE_SIZE) {
			removeIndividual(problem);
		}
		
		queueOfIndivs.add(individualEval);
	}

	@Override
	public IndividualEvaluated getIndividual(IProblem problem) {
		
		if (queueOfIndivs.isEmpty()) {
			return null;
		}
		
		return queueOfIndivs.peek();
	}

	@Override
	public IndividualEvaluated removeIndividual(IProblem problem) {

		if (queueOfIndivs.isEmpty()) {
			return null;
		}
		
		return queueOfIndivs.poll();
	}

}
