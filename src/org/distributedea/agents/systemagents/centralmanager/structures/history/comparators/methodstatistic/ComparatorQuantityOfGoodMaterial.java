package org.distributedea.agents.systemagents.centralmanager.structures.history.comparators.methodstatistic;

import java.util.Comparator;

import org.distributedea.ontology.monitor.MethodStatistic;
import org.distributedea.ontology.monitor.MethodStatisticResult;

public class ComparatorQuantityOfGoodMaterial implements Comparator<MethodStatistic> {

	@Override
	public int compare(MethodStatistic o1, MethodStatistic o2) {
		
		MethodStatisticResult statisticRslt1 =
				o1.getMethodStatisticResult();
		MethodStatisticResult statisticRslt2 =
				o2.getMethodStatisticResult();
		
		int numberOfGoodMaterial11 = statisticRslt1.getNumberOfGoodCreatedMaterial();
		int numberOfGoodMaterial12 = statisticRslt2.getNumberOfGoodCreatedMaterial();
		
		if (numberOfGoodMaterial11 < numberOfGoodMaterial12) {
			return -1;
		} else if (numberOfGoodMaterial11 == numberOfGoodMaterial12) {
			return 0;
		} else {
			return 1;
		}

	}

}
