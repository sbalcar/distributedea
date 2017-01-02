package org.distributedea.problems.tsp.gps.permutation.tools;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.problem.tsp.PositionGPS;

public class ToolDistanceTSPGPS {

	/**
	 * Counts distance between two GPS positions
	 * 
	 * @param position1
	 * @param position2
	 * @return
	 */
	public static double distanceBetween(PositionGPS positionGPS1,
			PositionGPS positionGPS2, IAgentLogger logger)  {
		
		double latitude1 = positionGPS1.getLatitude();
		double longitude1 = positionGPS1.getLongitude();
		
		double latitude2 = positionGPS2.getLatitude();
		double longitude2 = positionGPS2.getLongitude();

		if (latitude1 == latitude2 &&
				longitude1 == longitude2) {
			return 0;
		}	    

		//Counts distance between two points
		double xd = (latitude1 - latitude2);
		double yd = (longitude1 - longitude2);

		return Math.round(Math.sqrt((xd * xd) + (yd * yd)));
	}
}
