package org.distributedea.agents.systemagents.centralmanager.structures.history;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.distributedea.agents.systemagents.centralmanager.structures.history.comparators.methodtypehistory.ComparatorLastIteration;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.methodtype.MethodType;

/**
 * Structure represents {@link List} of {@link MethodTypeHistory}
 * @author stepan
 *
 */
public class MethodTypeHistories {

	private final List<MethodTypeHistory> methods;
	
	/**
	 * Constructor
	 */
	public MethodTypeHistories() {
		this.methods = new ArrayList<>();
	}
	
	/**
	 * Constructor
	 * @param methods
	 */
	public MethodTypeHistories(List<MethodTypeHistory> methods) {
		if (methods == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " can not be null");
		}
		for (MethodTypeHistory methodTypeHistoryI : methods) {
			if (! methodTypeHistoryI.valid(new TrashLogger())) {
				throw new IllegalArgumentException("Argument " +
						MethodTypeHistory.class.getSimpleName() +
						"is not valid");
			}
		}
		this.methods = methods;
	}
	
	/**
	 * Adds {@link MethodType}s which have never run
	 * @param methodTypes
	 */
	public void addMethodWhichHaveNeverRun(List<MethodType> methodTypes) {
	
		for (MethodType methodTypeI : methodTypes) {
			
			methods.add(new MethodTypeHistory(methodTypeI));
		}
	}
	
	/**
	 * Exports {@link MethodTypeHistories} of given {@link MethodType}s
	 * @param methodTypes
	 * @return
	 */
	public MethodTypeHistories exportMethodTypeHistoriesOf(
			List<MethodType> methodTypes) {
		if (methodTypes == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
		}
		
		List<MethodTypeHistory> methodSelected = new ArrayList<>();
		
		for (MethodTypeHistory methodTypeHistoryI : methods) {
			MethodType methodTypeI =
					methodTypeHistoryI.getMethodType();
			if (methodTypes.contains(methodTypeI)) {
				methodSelected.add(methodTypeHistoryI);
			}
		}
		
		return new MethodTypeHistories(methodSelected);
	}
	
	public MethodType methodTypeWhichDidntRunForTheLongestTime() {
		
		MethodTypeHistory maxMethodTypeHistory =
				Collections.min(methods, new ComparatorLastIteration());
		
		return maxMethodTypeHistory.getMethodType();
	}
}
