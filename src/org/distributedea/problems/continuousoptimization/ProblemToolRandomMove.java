package org.distributedea.problems.continuousoptimization;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problem.ProblemContinousOpt;
import org.distributedea.ontology.problem.continousoptimalization.Interval;
import org.distributedea.problems.exceptions.ProblemToolException;

public class ProblemToolRandomMove extends ProblemContinuousOptTool {

	private double maxStep = 0.005;
	
	@Override
	public Individual generateFirstIndividual(Problem problem,
			IAgentLogger logger) {
		
		return generateIndividual(problem, logger);
	}

	@Override
	public Individual generateNextIndividual(Problem problem,
			Individual individual, IAgentLogger logger) {
		
		ProblemContinousOpt problemCO = (ProblemContinousOpt) problem;
		IndividualPoint individualP = (IndividualPoint) individual;
		
		List<Interval> intervals = problemCO.getIntervals();
		List<Double> coordinates = individualP.getCoordinates();
				
		List<Double> coordinatesNew = new ArrayList<Double>();
		
		boolean isTheLastIndividual = true;
		for (int i = 0; i < intervals.size(); i++) {
		
			Interval intervalI = intervals.get(i);
			double coordinateI = coordinates.get(i);
			
			if (coordinateI + maxStep < intervalI.getMax()) {
				
				isTheLastIndividual = false;
				
				int sequenceNumber = (int) (coordinateI / maxStep);
				double coordinateNewI =
						maxStep*(sequenceNumber +1) + maxStep*Math.random();
				coordinatesNew.add(coordinateNewI);
			} else {
				coordinatesNew.add(coordinateI);
			}
		}

		if (isTheLastIndividual) {
			return null;
		}
		
		IndividualPoint individualNew = new IndividualPoint();
		individualNew.setCoordinates(coordinatesNew);

		return individualNew;
	}

	@Override
	public Individual improveIndividual(Individual individual, Problem problem,
			IAgentLogger logger) throws ProblemToolException {
		
		IndividualPoint individualPoint = (IndividualPoint) individual;
		
		IndividualPoint individualNew = new IndividualPoint();

		for (int i = 0; i < individualPoint.getCoordinates().size(); i++) {
			
			double coordinateI = individualPoint.exportCoordinate(i);
			double coordinateNewI = coordinateI + maxStep*Math.random() - maxStep/2;
		
			individualNew.importCoordinate(i, coordinateNewI);
		}
		return individualNew;
	}

	@Override
	public Individual getNeighbor(Individual individual, Problem problem,
			long neighborIndex, IAgentLogger logger) throws ProblemToolException {

		return improveIndividual(individual, problem, logger);
	}

	@Override
	public Individual[] createNewIndividual(Individual individual1,
			Individual individual2, Problem problem, IAgentLogger logger)
			throws ProblemToolException {
		
		IndividualPoint individualP1 = (IndividualPoint) individual1;
		IndividualPoint individualP2 = (IndividualPoint) individual2;
		
		if (individualP1.getCoordinates().size() !=
				individualP2.getCoordinates().size()) {
			throw new ProblemToolException("Individuals doesn't have the same dimension");
		}

		IndividualPoint individualPNew1 = new IndividualPoint();
		IndividualPoint individualPNew2 = new IndividualPoint();
		
		for (int i = 0; i < individualP1.getCoordinates().size(); i++) {
			
			double coordinate1 = individualP1.exportCoordinate(i);
			double coordinate2 = individualP2.exportCoordinate(i);
			
			double coordinatemin = Math.min(coordinate1, coordinate2);
			double distance = Math.abs(coordinate1 -coordinate2);
			
			// somewhere between
			double coordinateNew1 = coordinatemin + Math.random()*distance;
			individualPNew1.importCoordinate(i, coordinateNew1);
			
			double coordinateNew2 = coordinatemin + Math.random()*distance;
			individualPNew2.importCoordinate(i, coordinateNew2);
		}
		
		Individual[] individuals = {individualPNew1, individualPNew2};
		return individuals;
	}

	@Override
	public Individual[] createNewIndividual(Individual individual1,
			Individual individual2, Individual individual3,
			Problem problem, IAgentLogger logger)
			throws ProblemToolException {
		
		double F = 1.0;
		
		IndividualPoint individualP1 = (IndividualPoint) individual1;
		IndividualPoint individualP2 = (IndividualPoint) individual2;
		IndividualPoint individualP3 = (IndividualPoint) individual3;
		
		ProblemContinousOpt problemCO = (ProblemContinousOpt) problem;
		
		List<Double> coordinates = new ArrayList<Double>();
		for (int i = 0; i < individualP1.getCoordinates().size(); i++) {
			
			double indACoorI = individualP1.exportCoordinate(i);
			double indBCoorI = individualP2.exportCoordinate(i);
			double indCCoorI = individualP3.exportCoordinate(i);
			
			double valueNew = indACoorI+F*(indBCoorI-indCCoorI);
			
			Interval intervalI = problemCO.getIntervals().get(i);
			
			// corrects the new computed value
			double intervalSize = intervalI.getMax() - intervalI.getMin();
			if (valueNew < intervalI.getMin()) {
				valueNew += intervalSize;
			}
			if (intervalI.getMax() < valueNew) {
				valueNew -= intervalSize;
			}
			
			coordinates.add(valueNew);
		}
		
		IndividualPoint individualNew = new IndividualPoint();
		individualNew.setCoordinates(coordinates);
		
		Individual[] individuals = {individualNew};
		return individuals;
	}

}
