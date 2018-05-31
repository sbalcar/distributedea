package org.distributedea.ontology.dataset;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.tsp.Position;
import org.distributedea.ontology.dataset.tsp.PositionPoint;
/**
 * Ontology represent one TSP 2D Point Problem
 * @author stepan
 *
 */
public class DatasetTSPPoint extends DatasetTSP {

	private static final long serialVersionUID = 1L;

	/** List of TSP points */
	private List<PositionPoint> positions;
	
	
	@Deprecated
	public DatasetTSPPoint() { // only for Jade
		this.positions = new ArrayList<>();
	}
	
	/**
	 * Constructor
	 * @param positions
	 * @param problemFileName
	 */
	public DatasetTSPPoint(List<PositionPoint> positions,
			File fileOfProblem) {
		this.positions = positions;
	}
	
	/**
	 * Copy Constructor
	 * @param problem
	 */
	public DatasetTSPPoint(DatasetTSPPoint dataset) {
		
		List<PositionPoint> positionsClone = new ArrayList<>();
		for (PositionPoint positionI : positions) {
			PositionPoint positionCloneI =
					new PositionPoint(positionI);
			positionsClone.add(positionCloneI);
		}
		
		setPositions(positionsClone);
	}
	
	public List<PositionPoint> getPositions() {
		return positions;
	}
	public void setPositions(List<PositionPoint> positions) {
		this.positions = positions;
	}

	@Override
	public List<Position> exportPositions() {
		
		List<Position> positionsList = new ArrayList<Position>();
		for (PositionPoint positionPointI : positions) {
			positionsList.add(positionPointI);
		}
		
		return positionsList;
	}
	
	@Override
	public Position exportPosition(int itemNumber) {
		
		for (PositionPoint positionI : positions) {
			if (positionI.getNumber() == itemNumber) {
				return positionI;
			}
		}
		return null;
	}
	
	@Override
	public int numberOfPositions() {
		return positions.size();
	}


	/**
	 * Tests validity
	 */
	public boolean valid(IAgentLogger logger) {
		if (positions == null || positions.isEmpty()) {
			return false;
		}
		for (PositionPoint positionI : positions) {
			if (! positionI.valid(logger)) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public DatasetTSPPoint deepClone() {
		
		return new DatasetTSPPoint(this);
	}
	
}
