package org.distributedea.agents.systemagents.centralmanager.structures.history.comparators.methodtypehistory;

import java.util.Comparator;

import org.distributedea.agents.systemagents.centralmanager.structures.history.MethodTypeHistory;

public class ComparatorLastIteration implements Comparator<MethodTypeHistory> {

	@Override
	public int compare(MethodTypeHistory o1, MethodTypeHistory o2) {
		
		long iterationNumber1 = o1.exportNumberOfLastIteration();
		long iterationNumber2 = o2.exportNumberOfLastIteration();
		
		return (int) (iterationNumber1 - iterationNumber2);
	}

}
