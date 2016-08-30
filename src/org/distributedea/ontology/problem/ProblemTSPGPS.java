package org.distributedea.ontology.problem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.problem.tsp.Position;
import org.distributedea.ontology.problem.tsp.PositionGPS;

/**
 * Ontology represents TSP-GPS {@link Problem}
 * @author stepan
 *
 */
public class ProblemTSPGPS extends ProblemTSP {

	private static final long serialVersionUID = 1L;

	/** List of TSP-GPS points */
	private List<PositionGPS> positions;
	
	/** Problem File */
	private String problemFileName;


	@Deprecated
	public ProblemTSPGPS() { // only for Jade
		this.positions = new ArrayList<PositionGPS>();
	}
	
	/**
	 * Constructor
	 * @param positions
	 * @param problemFileName
	 */
	public ProblemTSPGPS(List<PositionGPS> positions, File problemFile) {
		this.positions = positions;
		this.importProblemFile(problemFile);
	}
	
	/**
	 * Copy Constructor
	 * @param problem
	 */
	public ProblemTSPGPS(ProblemTSPGPS problem) {
		
		if (problem == null) {
			throw new IllegalArgumentException();
		}
		
		List<PositionGPS> positionsClone = new ArrayList<>();
		for (PositionGPS positionI : problem.getPositions()) {
			PositionGPS positionCloneI =
					new PositionGPS(positionI);
			positionsClone.add(positionCloneI);
		}
		setPositions(positionsClone);
		
		setProblemFileName(problem.getProblemFileName());
	}
	
	public List<PositionGPS> getPositions() {
		return positions;
	}
	@Deprecated
	public void setPositions(List<PositionGPS> positions) {
		this.positions = positions;
	}

	@Override
	public List<Position> exportPositions() {
		
		List<Position> positionsList = new ArrayList<Position>();
		for (PositionGPS positionGPSI : positions) {
			positionsList.add(positionGPSI);
		}
		
		return positionsList;
	}
	
	public PositionGPS exportPosition(int itemNumber) {
		
		for (PositionGPS positionI : positions) {
			if (positionI.getNumber() == itemNumber) {
				return positionI;
			}
		}
		
		return null;
	}
	
	@Deprecated
	public String getProblemFileName() {
		File file = exportProblemFile();
		if (file == null) {
			return null;
		}
		return file.getAbsolutePath();
	}
	@Deprecated
	public void setProblemFileName(String fileName) {
		try {
			importProblemFile(new File(fileName));
		} catch(Exception e) {
			throw new IllegalArgumentException();
		}
	}
	/**
	 * Exports File with {@link Problem} assignment
	 */
	@Override
	public File exportProblemFile() {
		if (problemFileName == null) {
			return null;
		}
		return new File(problemFileName);
	}
	/**
	 * Imports File with {@link Problem} assignment
	 */
	@Override
	public void importProblemFile(File problemFile) {
		if (problemFile == null) {
			throw new IllegalArgumentException();
		}
		if ((! problemFile.exists()) || (! problemFile.isFile())) {
			throw new IllegalArgumentException();
		}
		this.problemFileName = problemFile.getAbsolutePath();
	}
	
	public int numberOfPositions() {
		return positions.size();
	}

	@Override
	public boolean testIsIGivenIndividualSolutionOfTheProblem(
			Individual individual, IAgentLogger logger) {
		return true;
	}
	
	/**
	 * Tests validity
	 */
	public boolean valid(IAgentLogger logger) {
		if (positions == null || positions.isEmpty()) {
			return false;
		}
		for (PositionGPS positionI : positions) {
			if (! positionI.valid(logger)) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public Problem deepClone() {
		return new ProblemTSPGPS(this);
	}

}
