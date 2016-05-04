package org.distributedea.ontology.problem;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.problem.tsp.Position;
import org.distributedea.ontology.problem.tsp.PositionGPS;

public class ProblemTSPGPS extends ProblemTSP {

	private static final long serialVersionUID = 1L;

	private List<PositionGPS> positions;
	private String problemFileName;

	@Override
	public List<Position> exportPositions() {
		
		List<Position> positionsList = new ArrayList<Position>();
		for (PositionGPS positionGPSI : positions) {
			positionsList.add(positionGPSI);
		}
		
		return positionsList;
	}
	
	public ProblemTSPGPS() {
		this.positions = new ArrayList<PositionGPS>();
	}
	
	public List<PositionGPS> getPositions() {
		return positions;
	}

	public void setPositions(List<PositionGPS> positions) {
		this.positions = positions;
	}

	public PositionGPS exportPosition(int itemNumber) {
		
		for (PositionGPS positionI : positions) {
			if (positionI.getNumber() == itemNumber) {
				return positionI;
			}
		}
		
		return null;
	}
	
	
	@Override
	public String getProblemFileName() {
		return problemFileName;
	}
	@Override
	public void setProblemFileName(String fileName) {
		this.problemFileName = fileName;
	}
	
	
	public int numberOfPositions() {
		return positions.size();
	}

	@Override
	public boolean testIsValid(Individual individual, AgentLogger logger) {
		return true;
	}
	
}
