package org.distributedea.problems.tsp.point.permutation;

import java.io.File;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.dataset.DatasetTSPPoint;
import org.distributedea.ontology.dataset.tsp.Position;
import org.distributedea.ontology.dataset.tsp.PositionPoint;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.IProblem;
import org.distributedea.problems.ProblemTool;
import org.distributedea.problems.tsp.gps.permutation.IProblemTSPPermutationTool;
import org.distributedea.problems.tsp.gps.permutation.tools.ToolNextPermutationTSPGPS;
import org.distributedea.problems.tsp.point.ProblemTSPPointTool;
import org.distributedea.problems.tsp.point.permutation.tools.ToolDistanceTSPPoint;
import org.distributedea.problems.tsp.point.permutation.tools.ToolFitnessTSPPoint;
import org.distributedea.problems.tsp.point.permutation.tools.ToolGenerateFirstIndividualTSPPoint;
import org.distributedea.problems.tsp.point.permutation.tools.ToolGenerateIndividualForTSPPoint;
import org.distributedea.problems.tsp.point.permutation.tools.ToolReadProblemTSPPoint;
import org.distributedea.problems.tsp.point.permutation.tools.ToolReadSolutionTSPPoints;


/**
 * Abstract {@link ProblemTool} for {@link ProblemTSPPoint} using permutation
 * representation of {@link Individual}s
 * @author stepan
 *
 */
public abstract class AProblemToolTSPPointPermutation extends ProblemTSPPointTool implements IProblemTSPPermutationTool {

	@Override
	public Class<?> datasetReprezentation() {

		return DatasetTSPPoint.class;
	}
	
	@Override
	public Class<?> reprezentationWhichUses() {

		return IndividualPermutation.class;
	}
	
	@Override
	public Individual generateFirstIndividual(IProblem proble,
			Dataset dataset, IAgentLogger logger) {
		
		DatasetTSPPoint datasetTSPPoint = (DatasetTSPPoint)dataset;
		
		return ToolGenerateFirstIndividualTSPPoint.generate(datasetTSPPoint, logger);
	}
	
	@Override
	public Individual generateIndividual(IProblem problem,
			Dataset dataset, IAgentLogger logger) {
		
		DatasetTSPPoint datasetTSPPoint = (DatasetTSPPoint) dataset;
		
		return ToolGenerateIndividualForTSPPoint.generate(datasetTSPPoint);

	}
	
	@Override
	public Individual generateNextIndividual(IProblem problem,
			Dataset dataset, Individual individual, long neighborIndex,
			IAgentLogger logger) {
		
		IndividualPermutation individualPerm = (IndividualPermutation) individual;
		List<Integer> perm = individualPerm.getPermutation();
		
		List<Integer> permNew = ToolNextPermutationTSPGPS.nextPermutation(perm);
		
		return new IndividualPermutation(permNew);
	}
	
	@Override
	public double fitness(Individual individual, IProblem problem,
			Dataset dataset, IAgentLogger logger) {
		
		IndividualPermutation individualPermutation =
				(IndividualPermutation) individual;
		DatasetTSPPoint datasetTSPPoint = (DatasetTSPPoint) dataset;
		
		return ToolFitnessTSPPoint.fitness(individualPermutation, datasetTSPPoint, this, logger);
	}
	
	@Override
	public Dataset readDataset(File datasetFile, IAgentLogger logger) {

		return ToolReadProblemTSPPoint.readDataset(datasetFile, logger);
	}
	
	@Override
	public Individual readSolution(File fileOfSolution, Dataset dataset,
			IAgentLogger logger) {
		
		return ToolReadSolutionTSPPoints.readSolutionTSP(fileOfSolution, logger);		
	}
	
	@Override
	public Individual getNeighbor(Individual individual,
			IProblem problem, Dataset dataset,
			long neighborIndex, IAgentLogger logger) throws Exception {
		
		DatasetTSPPoint datasetTSPPoint = (DatasetTSPPoint) dataset;
		
		return ToolGenerateIndividualForTSPPoint.generate(datasetTSPPoint);
	}
	
	@Override
	public double distanceBetween(Position position1, Position position2,
			IAgentLogger logger) {
		
		PositionPoint positionPoint1 = (PositionPoint) position1;
		PositionPoint positionPoint2 = (PositionPoint) position2;
		
		return ToolDistanceTSPPoint.distanceBetween(positionPoint1, positionPoint2, logger);
	}
	
}
