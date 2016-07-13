package org.distributedea.problems.tsp.point;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problem.ProblemTSPPoint;
import org.distributedea.ontology.problem.tsp.Position;
import org.distributedea.ontology.problem.tsp.PositionPoint;
import org.distributedea.problems.tsp.ProblemTSPTool;

public abstract class ProblemTSPPointTool extends ProblemTSPTool {

	@Override
	public Class<?> problemWhichSolves() {
		return ProblemTSPPoint.class;
	}
	
	@Override
	public Problem readProblem(String inputFileName, IAgentLogger logger) {

		List<PositionPoint> positions = new ArrayList<PositionPoint>();
		for (Position positionI : readProblemTSP(inputFileName, logger)) {
			positions.add((PositionPoint) positionI);
		}
		
		ProblemTSPPoint problem = new ProblemTSPPoint();
		problem.setProblemFileName(inputFileName);
		problem.setPositions(positions);
		
		return problem;
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
