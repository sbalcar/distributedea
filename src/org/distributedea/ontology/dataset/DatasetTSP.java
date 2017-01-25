package org.distributedea.ontology.dataset;

import java.util.List;

import org.distributedea.ontology.dataset.tsp.Position;

/**
 * Ontology represents abstract class for TSP {@link Dataset}
 * @author stepan
 *
 */
public abstract class DatasetTSP extends Dataset {

	private static final long serialVersionUID = 1L;

	public abstract List<Position> exportPositions();
	public abstract Position exportPosition(int itemNumber);
	public abstract int numberOfPositions();
	
	/**
	 * Minimum of position numbers
	 * @return
	 */
	public int minOfPositionNumbers() {
		
		int minValue = Integer.MAX_VALUE;
		
		for (Position positionI : exportPositions()) {
			
			if (positionI.getNumber() < minValue) {
				minValue = positionI.getNumber();
			}
		}
		
		// permutation is empty
		if (minValue == Integer.MAX_VALUE) {
			return -1;
		}
		
		return minValue;
	}
	
}
