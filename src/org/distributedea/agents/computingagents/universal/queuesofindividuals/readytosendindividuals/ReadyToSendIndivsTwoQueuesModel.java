package org.distributedea.agents.computingagents.universal.queuesofindividuals.readytosendindividuals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.distributedea.agents.computingagents.universal.queuesofindividuals.IReadyToSendIndividualsModel;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.structures.comparators.CmpIndividualEvaluated;

/**
 * Structure serves as model for {@link IndividualEvaluated}s ready to send,
 * structurally formed from two queues of {@link IndividualEvaluated}s (size=10)
 * @author stepan
 *
 */
public class ReadyToSendIndivsTwoQueuesModel implements IReadyToSendIndividualsModel {

	private int MAX_NUMBER_OF_INDIVIDUAL = 10;
	
	private List<IndividualEvaluated> individualsNoSorted = new ArrayList<>();
	
	private List<IndividualEvaluated> individualsNoSorted2 = new ArrayList<>();
	
	public void addIndividual(List<IndividualEvaluated> individualsEval,
			IProblem problem) {
		
		if (individualsEval == null || individualsEval.isEmpty()) {
			return;
		}
		
		addIndividual(individualsEval.get(0), problem);
	}

	
	public void addIndividual(IndividualEvaluated individualEval,
			IProblem problem) {
		
		if (individualEval == null) {
			return;
		}

		if (individualsNoSorted.size() <
				MAX_NUMBER_OF_INDIVIDUAL) {
			individualsNoSorted.add(individualEval);
			return;
		}
		
		if (individualsNoSorted2.size() <
				MAX_NUMBER_OF_INDIVIDUAL) {
			individualsNoSorted2.add(individualEval);
			return;
		}
				
	}
	
	public synchronized IndividualEvaluated removeIndividual(IProblem problem) {
		if (individualsNoSorted.isEmpty()) {
			return null;
		} else if (individualsNoSorted.size() == 1) {
			return individualsNoSorted.get(0);
		}
		
		// queue contains some individuals
		if (individualsNoSorted2.size() > MAX_NUMBER_OF_INDIVIDUAL / 2) {
			
			Collections.sort(individualsNoSorted, new CmpIndividualEvaluated(problem));
			IndividualEvaluated result = individualsNoSorted.get(0);
			
			individualsNoSorted = individualsNoSorted2;
			individualsNoSorted2 = new ArrayList<>();
			return result;
		
		} else {
			// move first individual from second queue to the first queue
			if (! individualsNoSorted2.isEmpty()) {
				individualsNoSorted.add(
						individualsNoSorted2.remove(0));
			}
			
			Collections.sort(individualsNoSorted, new CmpIndividualEvaluated(problem));
			return individualsNoSorted.remove(0);
		}
	}


	@Override
	public IndividualEvaluated getIndividual(IProblem problem) {
		if (individualsNoSorted.isEmpty()) {
			return null;
		}
		
		return individualsNoSorted.get(0);
	}
	
}
