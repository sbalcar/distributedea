package org.distributedea.ontology.problem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.problem.continuousoptimization.Interval;

/**
 * Ontology represents Continuous Optimization Problem
 * @author stepan
 * 
 */
public class ProblemContinuousOpt extends Problem {

	private static final long serialVersionUID = 1L;
	
	/** Identification of function */
	private String functionID;
	
	/** Size of space (the number of intervals) */
	private int dimension;
	
	/** Limiting the space intervals */
	private List<Interval> intervals;

	/** Problem File name */
	private String problemFileName;
	
	
	public ProblemContinuousOpt() {
		this.intervals = new ArrayList<>();
	}
	
	/**
	 * Copy Constructor
	 * @param problem
	 */
	public ProblemContinuousOpt(ProblemContinuousOpt problem) {
		if (problem == null || ! problem.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					ProblemContinuousOpt.class.getSimpleName() + " is not valid");
		}
		
		setFunctionID(problem.getFunctionID());
		setDimension(problem.getDimension());
		
		List<Interval> intervalsClone = new ArrayList<>();
		for (Interval intervalI : problem.getIntervals()) {
			intervalsClone.add(intervalI.deepClone());
		}
		
		setIntervals(intervalsClone);
		setProblemFileName(problem.getProblemFileName());
	}
	
	public String getFunctionID() {
		return functionID;
	}
	public void setFunctionID(String functionID) {
		this.functionID = functionID;
	}

	public int getDimension() {
		return dimension;
	}
	public void setDimension(int dimension) {
		this.dimension = dimension;
	}
	
	public List<Interval> getIntervals() {
		return intervals;
	}
	public void setIntervals(List<Interval> intervals) {
		this.intervals = intervals;
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
	public boolean testIsIGivenIndividualSolutionOfTheProblem(
			Individual individual, IAgentLogger logger) {
		
		if (! (individual instanceof IndividualPoint)) {
			return false;
		}
		
		IndividualPoint individualPoint = (IndividualPoint)individual;
		List<Double> coordinates = individualPoint.getCoordinates();
		
		for (int i = 0; i < coordinates.size(); i++) {
			
			Double coordinateI = coordinates.get(i);
			Interval intervalI = intervals.get(i);
			
			boolean isCoordinateValid = 
					intervalI.getMin() <= coordinateI &&
					coordinateI <= intervalI.getMax();
			if (! isCoordinateValid) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Tests validity
	 */
	public boolean valid(IAgentLogger logger) {
		
		if (functionID == null) {
			return false;
		}
		if (dimension < 1) {
			return false;
		}
		if (intervals == null || intervals.size() != dimension) {
			return false;
		}
		return true;
	}
	
	@Override
	public Problem deepClone() {
		return new ProblemContinuousOpt(this);
	}
	
}
