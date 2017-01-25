package org.distributedea.agents.computingagents.computingagent.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.problemdefinition.IProblemDefinition;
import org.distributedea.structures.comparators.CmpIndividualEvaluated;

/**
 * Structure serves as model for {@link Individual}s to send
 * @author stepan
 *
 */
public class IndividualToDistributionModel {

	private int MAX_NUMBER_OF_INDIVIDUAL = 10;
	
	private List<IndividualEvaluated> individualsNoSorted = new ArrayList<>();
	
	private List<IndividualEvaluated> individualsNoSorted2 = new ArrayList<>();
	
	public void addIndividual(
			List<IndividualEvaluated> individuals, IProblemDefinition problemDef) {
		
		if (individuals == null || individuals.isEmpty()) {
			return;
		}
		
		individualsNoSorted.add(individuals.get(0));
	}
	
	public void addIndividual(IndividualEvaluated individualEval,
			IProblemDefinition problemDef) {
		
		if (individualEval == null) {
			return;
		}

		if (individualsNoSorted.size() <
				MAX_NUMBER_OF_INDIVIDUAL) {
			individualsNoSorted.add(individualEval);
			return;
		}
		
		if (individualsNoSorted2.size() <
				MAX_NUMBER_OF_INDIVIDUAL) {
			individualsNoSorted2.add(individualEval);
			return;
		}
				
	}
	
	public IndividualEvaluated getIndividual(IProblemDefinition problemDef) {
		if (individualsNoSorted.isEmpty()) {
			return null;
		}
		
		// queue contains some individuals
		if (individualsNoSorted2.size() > MAX_NUMBER_OF_INDIVIDUAL / 2) {
			
			Collections.sort(individualsNoSorted, new CmpIndividualEvaluated(problemDef));
			IndividualEvaluated result = individualsNoSorted.get(0);
			
			individualsNoSorted = individualsNoSorted2;
			individualsNoSorted2 = new ArrayList<>();
			return result;
		
		} else {
			// move first individual from second queue to the first queue
			if (! individualsNoSorted2.isEmpty()) {
				individualsNoSorted.add(
						individualsNoSorted2.remove(0));
			}
			
			Collections.sort(individualsNoSorted, new CmpIndividualEvaluated(problemDef));
			return individualsNoSorted.remove(0);
		}
	}
	
}
