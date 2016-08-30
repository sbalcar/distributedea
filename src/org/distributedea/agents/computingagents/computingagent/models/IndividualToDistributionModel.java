package org.distributedea.agents.computingagents.computingagent.models;

import java.util.List;

import org.distributedea.agents.FitnessTool;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualsEvaluated;
import org.distributedea.ontology.problem.Problem;

/**
 * Structure serves as model for {@link Individual}s to send
 * @author stepan
 *
 */
public class IndividualToDistributionModel {

	private int MAX_NUMBER_OF_INDIVIDUAL = 10;
	
	private IndividualsEvaluated individualsSorted =
			new IndividualsEvaluated();
	
	public synchronized void addIndividual(
			List<IndividualEvaluated> individuals, Problem problem) {
		
		if (individuals == null || individuals.isEmpty()) {
			return;
		}
		
		individualsSorted.add(individuals);
		individualsSorted =  individualsSorted.exportNBestSorted(
				MAX_NUMBER_OF_INDIVIDUAL, problem);
	}
	
	public synchronized void addIndividual(IndividualEvaluated individualEval,
			Problem problem) {
		
		if (individualEval == null) {
			return;
		}
		
		if (individualsSorted.exportNumberOfIndividuals() +1 <=
				MAX_NUMBER_OF_INDIVIDUAL) {
			individualsSorted.add(individualEval);
			individualsSorted.sortFromTheBestToTheWorst(problem);
			return;
		}
		
		IndividualEvaluated theWorstIndividualEval =
				individualsSorted.exportIndividual(
						individualsSorted.exportNumberOfIndividuals() -1);
		boolean isNewBetterThanTheWorst = FitnessTool.
				isFirstIndividualEBetterThanSecond(
				theWorstIndividualEval, individualEval, problem);
		if (! isNewBetterThanTheWorst) {
			return;
		}
		
		individualsSorted.add(individualEval);
		individualsSorted =  individualsSorted.exportNBestSorted(
				MAX_NUMBER_OF_INDIVIDUAL, problem);
		
	}
	
	public synchronized IndividualEvaluated getIndividual() {
		if (individualsSorted.exportNumberOfIndividuals() == 0) {
			return null;
		}
		return individualsSorted.remove(0);
	}
	
}
