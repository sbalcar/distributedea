package org.distributedea.problems.continuousoptimization.point.operators;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.IndividualPoint;

public class OperatorDifferentialAllDimensions {
	
	/**
	 * 
	 * @param individualP1
	 * @param individualP2
	 * @param individualP3
	 * @param differentialWeight [0,2]
	 * @param logger
	 * @return
	 * @throws Exception
	 */
	public static IndividualPoint create(IndividualPoint individualP1,
			IndividualPoint individualP2, IndividualPoint individualP3,
			double differentialWeightF, IAgentLogger logger)
			throws Exception {
		
		int dimension = individualP1.getCoordinates().size();
				
		List<Double> coordinates = new ArrayList<>();
		
		for (int indexI = 0; indexI < dimension; indexI++) {
			
			double coordinate1 = individualP1.exportCoordinate(indexI);
			double coordinate2 = individualP2.exportCoordinate(indexI);
			double coordinate3 = individualP3.exportCoordinate(indexI);
			
			double newValueI = coordinate1 + Math.random() * differentialWeightF
					* (coordinate2 - coordinate3);
			
			coordinates.add(newValueI);
		}
		
		return new IndividualPoint(coordinates);
	}
}
