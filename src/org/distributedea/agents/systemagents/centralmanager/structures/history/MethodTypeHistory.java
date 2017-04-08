package org.distributedea.agents.systemagents.centralmanager.structures.history;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.distributedea.agents.systemagents.centralmanager.structures.history.comparators.methodhistory.ComparatorLastIteration;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.methodtype.MethodType;

public class MethodTypeHistory {

	private final MethodType methodType;
	private final List<MethodHistory> methods;

	/**
	 * Constructor
	 */
	public MethodTypeHistory(MethodType methodType) {
		if (methodType == null || ! methodType.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodType.class.getSimpleName() + " is not valid");
		}
		this.methodType = methodType;
		this.methods = new ArrayList<>();
	}

	/**
	 * Constructor
	 * @param methods
	 */
	public MethodTypeHistory(MethodType methodType,
			List<MethodHistory> methods) {
		if (methodType == null || ! methodType.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					MethodType.class.getSimpleName() + " is not valid");
		}
		this.methodType = methodType;
		this.methods = methods;
	}
	
	
	public MethodType getMethodType() {
		return this.methodType;
	}
	
	public List<MethodHistory> getMethodHistories() {
		return this.methods;
	}

	public long exportNumberOfLastIteration() {
		if (methods == null || methods.isEmpty()) {
			return 0;
		}
		
		MethodHistory maxMethodHistory =
				Collections.max(methods, new ComparatorLastIteration());
		
		return maxMethodHistory.exportNumberOfLastIteration();
	}

	public List<Integer> exportNumbersOfInstancesDuringAllIterations(int iterationCount) {
		
		List<Integer> result = new ArrayList<>();
		
		for (int i = 1; i <= iterationCount; i++) {
			
			int numberI = exportNumberOfInstancesDuringGivenIteration(i);
			result.add(numberI);
		}
		
		return result;
	}
	
	public int exportNumberOfInstancesDuringGivenIteration(int iteration) {
		if (iteration <= 0) {
			throw new IllegalArgumentException("Argument " +
					Integer.class.getSimpleName() + " is not valid");
		}
		if (methods == null || methods.isEmpty()) {
			return 0;
		}
		
		int result = 0;
		for (MethodHistory methodHistoryI : methods) {
			
			if (methodHistoryI.exportStatistic(iteration) != null) {
				result++;
			}
		}
		
		return result;
	}
	
	/**
	 * Tests validity
	 * @param logger
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		if (methodType == null || ! methodType.valid(logger)) {
			return false;
		}
		if (methods == null) {
			return false;
		}
		for (MethodHistory methodHistoryI : methods) {
			if (! methodHistoryI.valid(logger)) {
				return false;
			}
		}
		
		return true;
	}
}
