package org.distributedea.agents.computingagents.computingagent.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.distributedea.agents.FitnessTool;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescriptionnumber.MethodDescriptionNumber;
import org.distributedea.ontology.problem.IProblem;

/**
 * Structure represents model of computation-helpers
 * @author stepan
 *
 */
public class HelpersModel {

	private Map<MethodDescription, Integer> helpers = new HashMap<>();

	/**
	 * Updates model
	 * @param receivedIndivWrp
	 * @param theBestIndiv
	 * @param problem
	 */
	public void processReceivedIndiv(IndividualWrapper receivedIndivWrp,
			IndividualWrapper theBestIndiv, IProblem problem) {
		
		if (FitnessTool.isFistIndividualWBetterThanSecond(
				receivedIndivWrp, theBestIndiv, problem)) {
			
			addHelper(receivedIndivWrp.getMethodDescription());
		}
	}
	
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
	public List<MethodDescriptionNumber> getPrioritiesOfHelpers() {
		
		List<MethodDescriptionNumber> helpmateList = new ArrayList<>();
		
        for (Map.Entry<MethodDescription, Integer> entryI: this.helpers.entrySet()) {
			
        	MethodDescription descriptionI = entryI.getKey();
        	Integer valueI = entryI.getValue();
        	
        	MethodDescriptionNumber wrapperI =
        			new MethodDescriptionNumber(descriptionI, valueI);
        	
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
