package org.distributedea.problems.tsp.point.permutation.tools;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.tsp.Position;
import org.distributedea.ontology.dataset.tsp.PositionPoint;

public class ToolDistanceTSPPoint {

	public static double distanceBetween(Position position1, Position position2,
			IAgentLogger logger) {
		
		PositionPoint positionPoint1 = (PositionPoint) position1;
		PositionPoint positionPoint2 = (PositionPoint) position2;
		
		double deltaX = Math.abs(
				positionPoint1.getCoordinateX() -
				positionPoint2.getCoordinateX() );
		double deltaY = Math.abs(
				positionPoint1.getCoordinateY() -
				positionPoint2.getCoordinateY() );
		
		double distance = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
		return Math.round(distance);
	}
}
