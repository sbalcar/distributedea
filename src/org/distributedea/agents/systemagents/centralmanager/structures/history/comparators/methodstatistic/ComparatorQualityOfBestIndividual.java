package org.distributedea.agents.systemagents.centralmanager.structures.history.comparators.methodstatistic;

import java.util.Comparator;

import org.distributedea.agents.FitnessTool;
import org.distributedea.ontology.individualhash.IndividualHash;
import org.distributedea.ontology.monitor.MethodStatistic;
import org.distributedea.ontology.problem.IProblem;

public class ComparatorQualityOfBestIndividual implements Comparator<MethodStatistic> {

	private IProblem problem;
	
	public ComparatorQualityOfBestIndividual(IProblem problem) {
		this.problem = problem;
	}
	
	@Override
	public int compare(MethodStatistic o1, MethodStatistic o2) {
		
		IndividualHash individual1 = o1.exportBestIndividual();
		IndividualHash individual2 = o2.exportBestIndividual();
		
		if (individual1.getFitness() == individual2.getFitness()) {
			return 0;
		}
		
		boolean isFirstBetter = FitnessTool
				.isFistFitnessBetterThanSecond(
						individual1.getFitness(),
						individual2.getFitness(), problem);
		boolean isFirstWorst = !isFirstBetter;
		
		if (isFirstWorst) {
			return -1;
		} else {
			return 1;
		}
	}

}
