package org.distributedea.agents.systemagents.centralmanager.structures.history.comparators.methodstatistic;

import java.util.Comparator;

import org.distributedea.ontology.monitor.MethodStatistic;
import org.distributedea.ontology.monitor.MethodStatisticResult;

public class ComparatorQuantitiOfMaterial implements Comparator<MethodStatistic> {

	@Override
	public int compare(MethodStatistic o1, MethodStatistic o2) {
		
		MethodStatisticResult statisticRslt1 =
				o1.getMethodStatisticResult();
		MethodStatisticResult statisticRslt2 =
				o2.getMethodStatisticResult();
		
		double numberOfTheBest1 =
				statisticRslt1.getNumberOfTypeIndividuals();
		double numberOfTheBest2 =
				statisticRslt2.getNumberOfTypeIndividuals();
		
		if (numberOfTheBest1 < numberOfTheBest2) {
			return -1;
		} else if (numberOfTheBest1 == numberOfTheBest2) {
			return 0;
		} else {
			return 1;
		}
		
	}

}
