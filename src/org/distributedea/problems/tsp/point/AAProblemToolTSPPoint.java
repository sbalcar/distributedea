package org.distributedea.problems.tsp.point;

import java.io.File;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.agentconfiguration.AgentConfiguration;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetTSPPoint;
import org.distributedea.ontology.dataset.tsp.Position;
import org.distributedea.ontology.dataset.tsp.PositionGPS;
import org.distributedea.ontology.datasetdescription.DatasetDescription;
import org.distributedea.ontology.datasetdescription.IDatasetDescription;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.methoddesriptionsplanned.MethodIDs;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.ontology.problem.ProblemTSPPoint;
import org.distributedea.problems.AProblemTool;
import org.distributedea.problems.tsp.point.permutation.tools.ToolFitnessTSPPoint;
import org.distributedea.problems.tsp.point.permutation.tools.ToolGenerateIndividualForTSPPoint;
import org.distributedea.problems.tsp.point.permutation.tools.ToolReadProblemTSPPoint;
import org.distributedea.problems.tsp.point.permutation.tools.ToolReadSolutionTSPPoints;

/**
 * Abstract Problem Tool for TSP Problem for general Individual representation
 * @author stepan
 *
 */
public abstract class AAProblemToolTSPPoint extends AProblemTool {
	
	@Override
	public Class<?> problemReprezentation() {
		return ProblemTSPPoint.class;
	}

	@Override
	public Class<?> datasetReprezentation() {

		return DatasetTSPPoint.class;
	}
	
	@Override
	public Class<?> reprezentationWhichUses() {

		return IndividualPermutation.class;
	}
	
	
	@Override
	public void initialization(IProblem problem, Dataset dataset,
			AgentConfiguration agentConf, MethodIDs methodIDs, IAgentLogger logger) {
	}

	@Override
	public void exit() {
	}
	
	@Override
	public Individual generateIndividual(IProblem problem,
			Dataset dataset, IAgentLogger logger) {
		
		DatasetTSPPoint datasetTSPPoint = (DatasetTSPPoint) dataset;
		
		return ToolGenerateIndividualForTSPPoint.generate(datasetTSPPoint);

	}

	
	@Override
	public double fitness(Individual individual, IProblem problem,
			Dataset dataset, IAgentLogger logger) {
		
		IndividualPermutation individualPermutation =
				(IndividualPermutation) individual;
		DatasetTSPPoint datasetTSPPoint = (DatasetTSPPoint) dataset;
		
		return ToolFitnessTSPPoint.fitness(individualPermutation, datasetTSPPoint, logger);
	}
	
	@Override
	public Dataset readDataset(IDatasetDescription datasetDescription,
			IProblem problem, IAgentLogger logger) {

		DatasetDescription datasetDescr = (DatasetDescription) datasetDescription;
		return ToolReadProblemTSPPoint.readDataset(datasetDescr, problem, logger);
	}
	
	@Override
	public Individual readSolution(File fileOfSolution, Dataset dataset,
			IAgentLogger logger) {
		
		return ToolReadSolutionTSPPoints.readSolutionTSP(fileOfSolution, logger);		
	}
	
	
	
	/**
	 * Converts to Position
	 * 
	 * @param number
	 * @param firstValue
	 * @param secondValue
	 * @return
	 */
	public Position convertToPosition(int number, double firstValue,
			double secondValue) {
		
		PositionGPS positionI = new PositionGPS();
		positionI.setNumber(number);
		positionI.setLatitude(firstValue);
		positionI.setLongitude(secondValue);
		
		return positionI;
	}
	
}
