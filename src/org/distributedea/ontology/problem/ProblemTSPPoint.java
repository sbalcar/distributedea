package org.distributedea.ontology.problem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.problem.tsp.Position;
import org.distributedea.ontology.problem.tsp.PositionPoint;

/**
 * Ontology represent one TSP 2D Point Problem
 * @author stepan
 *
 */
public class ProblemTSPPoint extends ProblemTSP {

	private static final long serialVersionUID = 1L;

	/** List of TSP points */
	private List<PositionPoint> positions;
	
	/** Problem File */
	private String problemFileName;
	
	
	
	@Deprecated
	public ProblemTSPPoint() { // only for Jade
		this.positions = new ArrayList<>();
	}
	
	/**
	 * Constructor
	 * @param positions
	 * @param problemFileName
	 */
	public ProblemTSPPoint(List<PositionPoint> positions,
			File fileOfProblem) {
		this.positions = positions;
		this.importProblemFile(fileOfProblem);
	}
	
	/**
	 * Copy Constructor
	 * @param problem
	 */
	public ProblemTSPPoint(ProblemTSPPoint problem) {
		
		List<PositionPoint> positionsClone = new ArrayList<>();
		for (PositionPoint positionI : positions) {
			PositionPoint positionCloneI =
					new PositionPoint(positionI);
			positionsClone.add(positionCloneI);
		}
		
		setPositions(positionsClone);
		setProblemFileName(problem.getProblemFileName());
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
		if (! problemFile.exists() || ! problemFile.isFile()) {
			throw new IllegalArgumentException();
		}
		this.problemFileName = problemFile.getAbsolutePath();
	}
	
	@Override
	public int numberOfPositions() {
		return positions.size();
	}

	@Override
	public boolean testIsIGivenIndividualSolutionOfTheProblem(Individual individual, IAgentLogger logger) {
		return true;
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
	public Problem deepClone() {
		
		return new ProblemTSPPoint(this);
	}
	
}
