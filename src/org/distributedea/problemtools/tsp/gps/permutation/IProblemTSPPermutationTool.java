package org.distributedea.problemtools.tsp.gps.permutation;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.tsp.Position;

public interface IProblemTSPPermutationTool {

	public double distanceBetween(Position position1, Position position2,
			IAgentLogger logger);

}
