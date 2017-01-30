package org.distributedea.problems.continuousoptimization.point.operators;

import java.util.Random;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individuals.IndividualPoint;

public class OperatorDifferential {

	/**
	 * 
	 * @param individualP1
	 * @param individualP2
	 * @param individualP3
	 * @param differentialWeight [0,2]
	 * @param dataset
	 * @param logger
	 * @return
	 * @throws Exception
	 */
	public static IndividualPoint create(IndividualPoint individualP1,
			IndividualPoint individualP2, IndividualPoint individualP3,
			double differentialWeightF, Dataset dataset, IAgentLogger logger)
			throws Exception {
		
		int dimension = individualP1.getCoordinates().size();
		
		Random random = new Random();
		int index = random.nextInt(dimension);
		
		IndividualPoint newIndividual = individualP1.deepClone();
		
		double newValue = individualP1.exportCoordinate(index) + differentialWeightF * (
				individualP2.exportCoordinate(index) - individualP3.exportCoordinate(index)); 
		
		newIndividual.importCoordinate(index, newValue);
		
		return newIndividual;
	}
}
