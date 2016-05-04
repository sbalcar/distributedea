package org.distributedea.ontology.problem;

import java.util.List;

import org.distributedea.logging.AgentLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualPoint;
import org.distributedea.ontology.problem.continousoptimalization.Interval;

/**
 * Ontology for representation Continuous Optimization Problem
 */
public class ProblemContinousOpt extends Problem {

	private static final long serialVersionUID = 1L;
	
	/** Identification of function */
	private String functionID;
	
	/** Size of space (the number of intervals) */
	private int dimension;
	
	/** Limiting the space intervals */
	private List<Interval> intervals;

	private String problemFileName;
	
	
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
	
	@Override
	public String getProblemFileName() {
		return problemFileName;
	}
	@Override
	public void setProblemFileName(String fileName) {
		this.problemFileName = fileName;
		
	}
	
	@Override
	public boolean isMaximizationProblem() {
		return true;
	}
	
	@Override
	public boolean testIsValid(Individual individual, AgentLogger logger) {
		
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
	
}
