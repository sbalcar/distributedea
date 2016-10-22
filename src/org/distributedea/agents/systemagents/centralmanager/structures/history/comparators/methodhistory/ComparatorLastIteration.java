package org.distributedea.agents.systemagents.centralmanager.structures.history.comparators.methodhistory;

import java.util.Comparator;

import org.distributedea.agents.systemagents.centralmanager.structures.history.MethodHistory;

public class ComparatorLastIteration implements Comparator<MethodHistory> {

	@Override
	public int compare(MethodHistory o1, MethodHistory o2) {
		
		long iterationNumber1 = o1.exportNumberOfLastIteration();
		long iterationNumber2 = o2.exportNumberOfLastIteration();
		
		return (int) (iterationNumber1 - iterationNumber2);
	}

}
