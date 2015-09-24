package org.distributedea.ontology.problem;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.ontology.problem.tsp.PositionGPS;

public class ProblemTSP extends Problem {

	private static final long serialVersionUID = 1L;

	private List<PositionGPS> positions;

	public ProblemTSP() {
		this.positions = new ArrayList<PositionGPS>();
	}
	
	public List<PositionGPS> getPositions() {
		return positions;
	}

	public void setPositions(List<PositionGPS> positions) {
		this.positions = positions;
	}

	public PositionGPS exportPositionGPS(int itemNumber) {
		
		for (PositionGPS positionI : positions) {
			if (positionI.getNumber() == itemNumber) {
				return positionI;
			}
		}
		
		return null;
	}
	
	public int numberOfPositions() {
		return positions.size();
	}
	
}
