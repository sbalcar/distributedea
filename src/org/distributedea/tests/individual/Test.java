package org.distributedea.tests.individual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.problemdefinition.IProblemDefinition;
import org.distributedea.ontology.problemdefinition.ProblemBinPackingDef;
import org.distributedea.ontology.problemdefinition.ProblemContinuousOptDef;
import org.distributedea.structures.comparators.CmpIndividualWrapper;

public class Test {

	@SuppressWarnings("deprecation")
	public static void main(String [ ] args) {
		
		IndividualEvaluated indE0 = new IndividualEvaluated();
		indE0.setFitness(100);		
		IndividualWrapper indW0 = new IndividualWrapper();
		indW0.setIndividualEvaluated(indE0);

		IndividualEvaluated indE1 = new IndividualEvaluated();
		indE1.setFitness(101);		
		IndividualWrapper indW1 = new IndividualWrapper();
		indW1.setIndividualEvaluated(indE1);

		IndividualEvaluated indE2 = new IndividualEvaluated();
		indE2.setFitness(102);		
		IndividualWrapper indW2 = new IndividualWrapper();
		indW2.setIndividualEvaluated(indE2);

		IndividualEvaluated indE3 = new IndividualEvaluated();
		indE3.setFitness(103);		
		IndividualWrapper indW3 = new IndividualWrapper();
		indW3.setIndividualEvaluated(indE3);
		
		List<IndividualWrapper> individuals = new ArrayList<>();
		individuals.add(indW1);
		individuals.add(indW3);
		individuals.add(indW0);
		individuals.add(indW2);
		
		IProblemDefinition problemDefBP = new ProblemBinPackingDef(1);
		Collections.sort(individuals, new CmpIndividualWrapper(problemDefBP));

		IndividualWrapper bestBP = individuals.get(0);
		if (! bestBP.equals(indW0)) {
			System.out.println("Error");
		}

		IProblemDefinition problemDefCO = new ProblemContinuousOptDef(true);
		Collections.sort(individuals, new CmpIndividualWrapper(problemDefCO));

		IndividualWrapper bestCO = individuals.get(0);
		if (! bestCO.equals(indW3)) {
			System.out.println("Error");
		}
		
		System.out.println("OK");
	}
}
