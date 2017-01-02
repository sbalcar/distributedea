package org.distributedea.problems.tsp;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problem.tsp.Position;
import org.distributedea.ontology.problem.tsp.PositionGPS;
import org.distributedea.problems.ProblemTool;

/**
 * Abstract Problem Tool for TSP Problem for general Individual representation
 * @author stepan
 *
 */
public abstract class ProblemTSPTool extends ProblemTool {
	
	@Override
	public void initialization(Problem problem, AgentConfiguration agentConf, IAgentLogger logger) {
	}

	@Override
	public void exit() {
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
