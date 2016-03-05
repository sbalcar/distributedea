package org.distributedea.ontology.problem;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.ontology.problem.tsp.Position;
import org.distributedea.ontology.problem.tsp.PositionPoint;

public class ProblemTSPPoint extends ProblemTSP {

	private static final long serialVersionUID = 1L;

	private List<PositionPoint> positions;

	@Override
	public List<Position> exportPositions() {
		
		List<Position> positionsList = new ArrayList<Position>();
		for (PositionPoint positionPointI : positions) {
			positionsList.add(positionPointI);
		}
		
		return positionsList;
	}
	
	public List<PositionPoint> getPositions() {
		return positions;
	}

	public void setPositions(List<PositionPoint> positions) {
		this.positions = positions;
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
	
}
