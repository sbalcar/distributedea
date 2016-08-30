package org.distributedea.agents.computingagents.computingagent.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.agentdescription.AgentDescriptionPriority;

/**
 * Structure represents model of computation-helpers
 * @author stepan
 *
 */
public class HelpersModel {

	private Map<AgentDescription, Integer> helpers = new HashMap<>();

	/**
	 * Adds helper to model
	 * @param description
	 */
	public void addHelper(AgentDescription description) {
		
		// put description to the map
		if (helpers.containsKey(description)) {
			int frequency = helpers.get(description);
			helpers.put(description, frequency+1);
		} else  {
			helpers.put(description, 1);
		}
	}
	
	/**
	 * Returns priorities of helpers
	 * @return
	 */
	public List<AgentDescriptionPriority> getPrioritiesOfHelpers() {
		
		List<AgentDescriptionPriority> helpmateList = new ArrayList<>();
		
        for (Map.Entry<AgentDescription, Integer> entryI: this.helpers.entrySet()) {
			
        	AgentDescription descriptionI = entryI.getKey();
        	Integer valueI = entryI.getValue();
        	
        	AgentDescriptionPriority wrapperI =
        			new AgentDescriptionPriority(descriptionI, valueI);
        	
        	helpmateList.add(wrapperI);
        }

		return helpmateList;
	}
	
	/**
	 * Removes everything from model. The same as to create new empty model.
	 */
	public void clean() {
		this.helpers = new HashMap<AgentDescription, Integer>();
	}
}
