package org.distributedea.ontology.helpmate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.distributedea.agents.systemagents.centralmanager.scheduler.tool.Pair;
import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.agentdescription.AgentDescriptionWrapper;

public class HelpmatesWrapper {

	private List<HelpmateList> helpers;

	
	public List<HelpmateList> getHelpers() {
		return helpers;
	}
	public void setHelpers(List<HelpmateList> helpers) {
		this.helpers = helpers;
	}
	public void addHelper(HelpmateList helper) {
		if (this.helpers == null) {
			this.helpers = new ArrayList<>();
		}
		this.helpers.add(helper);
	}
	
	
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
		for (HelpmateList helpmateListI: getHelpers()) {
			helpmateMapI.put(helpmateListI.getDescription(), 0);
		}
		
		// count priority in global
		for (HelpmateList helpmateListI: getHelpers()) {
			List<AgentDescriptionWrapper> wrappers = helpmateListI.getDescriptions();

			// going through all results regarding their helpers
			for (AgentDescriptionWrapper wrapperI : wrappers) {
				
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
