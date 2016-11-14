package org.distributedea.agents.systemagents.centralmanager.structures.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.javaextension.Pair;
import org.distributedea.ontology.helpmate.StatisticOfHelpmates;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescriptioncounter.MethodDescriptionCounter;

/**
 * Data structure of helpmates statistics of all running instances
 * of {@link Agent_ComputingAgent}
 * @author stepan
 *
 */
public class ModelOfHelpmates {

	private List<StatisticOfHelpmates> helpmates;

	public ModelOfHelpmates(List<StatisticOfHelpmates> helpmates) {
		this.helpmates = helpmates;
	}
	
	public List<StatisticOfHelpmates> getHelpers() {
		return helpmates;
	}

	public int exportNumberConcreteAgentDescription(MethodDescription description) {
		
		List<MethodDescription> allDescriptions = exportAgentDescription();
		
		int numberOfLelement = 0;
		
		for (MethodDescription descriptionI : allDescriptions) {
			
			if (descriptionI.equals(description)) {
				numberOfLelement++;
			}
		}
		
		return numberOfLelement;
	}
	
	/**
	 * Returns number of Different Agent Description
	 * @return
	 */
	public int exportNumberOfDifferentAgentDescription() {
		
		List<MethodDescription> descriptions = exportAgentDescription();
		
		// removing duplicates
		Set<MethodDescription> hs = new HashSet<>();
		hs.addAll(descriptions);

		return hs.size();
	}
	
	/**
	 * Exports all Agent Description with duplicates
	 * @return
	 */
	private List<MethodDescription> exportAgentDescription() {
		
		List<MethodDescription> descriptions = new ArrayList<>();
		
		for (StatisticOfHelpmates helperI : helpmates) {
			
			MethodDescription descriptionI = helperI.getAgentDescription();
			descriptions.add(descriptionI);
		}
		
		return descriptions;
	}
	
	
	/**
	 * Export the AgentDescription with the minimal priority
	 * @return
	 */
	public Pair<MethodDescription, Integer> exportMinPrioritizedDescription() {
		
		// initialization of data structure
		Map<MethodDescription, Integer> helpmatesMap = exportAgentPriority();
		
		int minPriority = Integer.MAX_VALUE;
		MethodDescription minPriorityDescription = null;
		
		for (Map.Entry<MethodDescription, Integer> entry : helpmatesMap.entrySet()) {
			
			MethodDescription descriptionI = entry.getKey();
		    int priorityI = entry.getValue();
		    
		    if (priorityI < minPriority) {
		    	minPriority = priorityI;
		    	minPriorityDescription = descriptionI;
		    }    
		}
		return new Pair<>(minPriorityDescription, minPriority);
	}
	
	/**
	 * Export the AgentDescription with the maximal priority
	 * @return
	 */
	public Pair<MethodDescription, Integer> exportMaxPrioritizedDescription() {
		
		// initialization of data structure
		Map<MethodDescription, Integer> helpmateMapI = exportAgentPriority();

		int maxPriority = Integer.MIN_VALUE;
		MethodDescription maxPriorityDescription = null;
		
		for (Map.Entry<MethodDescription, Integer> entry : helpmateMapI.entrySet()) {
			
			MethodDescription descriptionI = entry.getKey();
		    int priorityI = entry.getValue();

		    if (priorityI > maxPriority) {
		    	maxPriority = priorityI;
		    	maxPriorityDescription = descriptionI;
		    }		    
		}
		return new Pair<>(maxPriorityDescription, maxPriority);
	}
	
	public Map<MethodDescription, Integer> exportAgentPriority() {

		// initialization of data structure
		Map<MethodDescription, Integer> helpmateMapI = new HashMap<MethodDescription, Integer>();
		for (StatisticOfHelpmates helpmateListI: getHelpers()) {
			helpmateMapI.put(helpmateListI.getAgentDescription(), 0);
		}
		
		// count priority in global
		for (StatisticOfHelpmates helpmateListI: getHelpers()) {
			List<MethodDescriptionCounter> wrappers = helpmateListI.getHelpersDescriptions();

			// going through all results regarding their helpers
			for (MethodDescriptionCounter wrapperI : wrappers) {
				
				MethodDescription descriptionI = wrapperI.getDescription();
				int priorityI = wrapperI.getCounter();
				
				if (helpmateMapI.containsKey(descriptionI)) {
					int freqeuency = helpmateMapI.get(descriptionI);
					helpmateMapI.put(descriptionI, freqeuency+priorityI);
				} else {
					// throws out information about agent which still doesn't exist
				}
			}
			
		}
		return helpmateMapI;
	}
}
