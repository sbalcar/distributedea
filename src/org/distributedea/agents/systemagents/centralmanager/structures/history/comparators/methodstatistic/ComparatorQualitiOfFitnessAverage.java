package org.distributedea.agents.systemagents.centralmanager.structures.history.comparators.methodstatistic;

import java.util.Comparator;

import org.distributedea.agents.FitnessTool;
import org.distributedea.ontology.monitor.MethodStatistic;
import org.distributedea.ontology.monitor.MethodStatisticResult;
import org.distributedea.ontology.problemdefinition.IProblemDefinition;

public class ComparatorQualitiOfFitnessAverage implements Comparator<MethodStatistic>{

	private IProblemDefinition problemDef;
	
	public ComparatorQualitiOfFitnessAverage(IProblemDefinition problemDef) {
		this.problemDef = problemDef;
	}
	
	@Override
	public int compare(MethodStatistic o1, MethodStatistic o2) {
		
		MethodStatisticResult statisticResult1 =
				o1.getMethodStatisticResult();
		MethodStatisticResult statisticResult2 =
				o2.getMethodStatisticResult();
		
		double fitnessAverage1 = statisticResult1.getFitnessAverage();
		double fitnessAverage2 = statisticResult2.getFitnessAverage();
		
		if (fitnessAverage1 == fitnessAverage2) {
			return 0;
		}
		
		boolean isFirstWorse = FitnessTool
				.isFistFitnessWorseThanSecond(fitnessAverage1,
						fitnessAverage2, problemDef);

		if (isFirstWorse) {
			return -1;
		} else {
			return 1;
		}
	}

}
