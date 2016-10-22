package org.distributedea.agents.computingagents.computingagent.evolution.selectors;



public abstract class Selector implements ISelector {

	public static ISelector createInstance(Class<?> selectorClass) {
		
		try {
			return (ISelector) selectorClass.newInstance();
			
		} catch (InstantiationException | IllegalAccessException e) {
			return null;
		}
	}
}
