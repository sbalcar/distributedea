package org.distributedea.problems.tsp.gps.permutation;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.problem.tsp.Position;

public interface IProblemTSPPermutationTool {

	public double distanceBetween(Position position1, Position position2,
			IAgentLogger logger);

}
