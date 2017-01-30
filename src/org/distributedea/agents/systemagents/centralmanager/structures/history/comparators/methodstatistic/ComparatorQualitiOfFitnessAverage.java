package org.distributedea.agents.systemagents.centralmanager.structures.history.comparators.methodstatistic;

import java.util.Comparator;

import org.distributedea.agents.FitnessTool;
import org.distributedea.ontology.monitor.MethodStatistic;
import org.distributedea.ontology.monitor.MethodStatisticResult;
import org.distributedea.ontology.problem.IProblem;

public class ComparatorQualitiOfFitnessAverage implements Comparator<MethodStatistic>{

	private IProblem problem;
	
	public ComparatorQualitiOfFitnessAverage(IProblem problem) {
		this.problem = problem;
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
						fitnessAverage2, problem);

		if (isFirstWorse) {
			return -1;
		} else {
			return 1;
		}
	}

}
