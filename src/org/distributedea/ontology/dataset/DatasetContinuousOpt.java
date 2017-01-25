package org.distributedea.ontology.dataset;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.dataset.continuousoptimization.Interval;

/**
 * Ontology represents Continuous Optimization Dataset
 * @author stepan
 * 
 */
public class DatasetContinuousOpt extends Dataset {

	private static final long serialVersionUID = 1L;
	
	/** Identification of function */
	private String functionID;
	
	/** Size of space (the number of intervals) */
	private int dimension;
	
	/** Limiting the space intervals */
	private List<Interval> intervals;

	/** Problem File name */
	private String problemFileName;
	
	
	public DatasetContinuousOpt() {
		this.intervals = new ArrayList<>();
	}
	
	/**
	 * Copy Constructor
	 * @param problem
	 */
	public DatasetContinuousOpt(DatasetContinuousOpt problem) {
		if (problem == null || ! problem.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					DatasetContinuousOpt.class.getSimpleName() + " is not valid");
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
	public Dataset deepClone() {
		return new DatasetContinuousOpt(this);
	}
	
}
