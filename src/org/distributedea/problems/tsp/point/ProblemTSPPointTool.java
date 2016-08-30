package org.distributedea.problems.tsp.point;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problem.ProblemTSPPoint;
import org.distributedea.ontology.problem.tsp.Position;
import org.distributedea.ontology.problem.tsp.PositionPoint;
import org.distributedea.problems.tsp.ProblemTSPTool;

/**
 * Ontology represents abstract {@link IProblemTool}  for
 * problem {@link ProblemTSPPoint}
 * @author stepan
 *
 */
public abstract class ProblemTSPPointTool extends ProblemTSPTool {

	@Override
	public Class<?> problemWhichSolves() {
		return ProblemTSPPoint.class;
	}
	
	@Override
	public Problem readProblem(File fileOfProblem, IAgentLogger logger) {

		List<Position> positionsInput = readProblemTSP(fileOfProblem, logger);
		
		List<PositionPoint> positions = new ArrayList<PositionPoint>();
		for (Position positionI : positionsInput) {
			positions.add((PositionPoint) positionI);
		}
		
		return new ProblemTSPPoint(positions, fileOfProblem);
	}
	

	@Override
	public Position convertToPosition(int number, double firstValue,
			double secondValue) {
		
		PositionPoint position = new PositionPoint();
		position.setNumber(number);
		position.setCoordinateX(firstValue);
		position.setCoordinateY(secondValue);
		
		return position;
	}
	
}
