package org.distributedea.agents.systemagents.centralmanager.structures.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;
import org.distributedea.javaextension.Pair;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.agentdescription.AgentDescriptionPriority;
import org.distributedea.ontology.helpmate.StatisticOfHelpmates;

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

	public int exportNumberConcreteAgentDescription(AgentDescription description) {
		
		List<AgentDescription> allDescriptions = exportAgentDescription();
		
		int numberOfLelement = 0;
		
		for (AgentDescription descriptionI : allDescriptions) {
			
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
		
		List<AgentDescription> descriptions = exportAgentDescription();
		
		// removing duplicates
		Set<AgentDescription> hs = new HashSet<>();
		hs.addAll(descriptions);

		return hs.size();
	}
	
	/**
	 * Exports all Agent Description with duplicates
	 * @return
	 */
	private List<AgentDescription> exportAgentDescription() {
		
		List<AgentDescription> descriptions = new ArrayList<>();
		
		for (StatisticOfHelpmates helperI : helpmates) {
			
			AgentDescription descriptionI = helperI.getAgentDescription();
			descriptions.add(descriptionI);
		}
		
		return descriptions;
	}
	
	
	/**
	 * Export the AgentDescription with the minimal priority
	 * @return
	 */
	public Pair<AgentDescription, Integer> exportMinPrioritizedDescription() {
		
		// initialization of data structure
		Map<AgentDescription, Integer> helpmateMapI = exportAgentPriority();
		
		int minPriority = Integer.MAX_VALUE;
		AgentDescription minPriorityDescription = null;
		
		for (Map.Entry<AgentDescription, Integer> entry : helpmateMapI.entrySet()) {
			
			AgentDescription descriptionI = entry.getKey();
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
	public Pair<AgentDescription, Integer> exportMaxPrioritizedDescription() {
		
		// initialization of data structure
		Map<AgentDescription, Integer> helpmateMapI = exportAgentPriority();

		int maxPriority = Integer.MIN_VALUE;
		AgentDescription maxPriorityDescription = null;
		
		for (Map.Entry<AgentDescription, Integer> entry : helpmateMapI.entrySet()) {
			
			AgentDescription descriptionI = entry.getKey();
		    int priorityI = entry.getValue();

		    if (priorityI > maxPriority) {
		    	maxPriority = priorityI;
		    	maxPriorityDescription = descriptionI;
		    }		    
		}
		return new Pair<>(maxPriorityDescription, maxPriority);
	}
	
	public Map<AgentDescription, Integer> exportAgentPriority() {

		// initialization of data structure
		Map<AgentDescription, Integer> helpmateMapI = new HashMap<AgentDescription, Integer>();
		for (StatisticOfHelpmates helpmateListI: getHelpers()) {
			helpmateMapI.put(helpmateListI.getAgentDescription(), 0);
		}
		
		// count priority in global
		for (StatisticOfHelpmates helpmateListI: getHelpers()) {
			List<AgentDescriptionPriority> wrappers = helpmateListI.getHelpersDescriptions();

			// going through all results regarding their helpers
			for (AgentDescriptionPriority wrapperI : wrappers) {
				
				AgentDescription descriptionI = wrapperI.getDescription();
				int priorityI = wrapperI.getPriority();
				
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
