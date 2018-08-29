package org.distributedea.problemtools.continuousoptimization.point.operators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.distributedea.ontology.individuals.IndividualPoint;

public class OperatorUniformCross {

	public static IndividualPoint[] create(IndividualPoint individualPoint1,
			IndividualPoint individualPoint2) {
		
		List<Double> coordinatesNew1 = new ArrayList<>();
		List<Double> coordinatesNew2 = new ArrayList<>();
		
		Random ran = new Random();
		for (int i = 0; i < individualPoint1.getCoordinates().size(); i++) {
			
			double valueI1 = individualPoint1.exportCoordinate(i);
			double valueI2 = individualPoint2.exportCoordinate(i);

			if (ran.nextBoolean()) {
				coordinatesNew1.add(valueI1);
				coordinatesNew2.add(valueI2);
			} else {
				coordinatesNew1.add(valueI2);
				coordinatesNew2.add(valueI1);				
			}
		}
		
		IndividualPoint[] individuals = new IndividualPoint[2];
		individuals[0] = new IndividualPoint(coordinatesNew1);
		individuals[1] = new IndividualPoint(coordinatesNew2);
		
		return individuals;
	}
}
