package org.distributedea.agents.computingagents.computingagent.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescriptioncounter.MethodDescriptionCounter;

/**
 * Structure represents model of computation-helpers
 * @author stepan
 *
 */
public class HelpersModel {

	private Map<MethodDescription, Integer> helpers = new HashMap<>();

	/**
	 * Adds helper to model
	 * @param description
	 */
	public void addHelper(MethodDescription description) {
		
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
	public List<MethodDescriptionCounter> getPrioritiesOfHelpers() {
		
		List<MethodDescriptionCounter> helpmateList = new ArrayList<>();
		
        for (Map.Entry<MethodDescription, Integer> entryI: this.helpers.entrySet()) {
			
        	MethodDescription descriptionI = entryI.getKey();
        	Integer valueI = entryI.getValue();
        	
        	MethodDescriptionCounter wrapperI =
        			new MethodDescriptionCounter(descriptionI, valueI);
        	
        	helpmateList.add(wrapperI);
        }

		return helpmateList;
	}
	
	/**
	 * Removes everything from model. The same as to create new empty model.
	 */
	public void clean() {
		this.helpers = new HashMap<MethodDescription, Integer>();
	}
}
