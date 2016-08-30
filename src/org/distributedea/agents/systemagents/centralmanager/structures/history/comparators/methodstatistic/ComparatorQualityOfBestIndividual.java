package org.distributedea.agents.systemagents.centralmanager.structures.history.comparators.methodstatistic;

import java.util.Comparator;

import org.distributedea.agents.FitnessTool;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.monitor.MethodStatistic;

public class ComparatorQualityOfBestIndividual implements Comparator<MethodStatistic> {

	private Class<?> problemClass;
	
	public ComparatorQualityOfBestIndividual(Class<?> problemClass) {
		this.problemClass = problemClass;
	}
	
	@Override
	public int compare(MethodStatistic o1, MethodStatistic o2) {
		
		IndividualEvaluated individual1 = o1.exportBestIndividual();
		IndividualEvaluated individual2 = o2.exportBestIndividual();
		
		if (individual1.getFitness() == individual2.getFitness()) {
			return 0;
		}
		
		boolean isFirstBetter = FitnessTool
				.isFistIndividualEBetterThanSecond(individual1,
						individual2, problemClass);
		boolean isFirstWorst = !isFirstBetter;
		
		if (isFirstWorst) {
			return -1;
		} else {
			return 1;
		}
	}

}
