package org.distributedea.ontology.individuals;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;

/**
 * Ontology represents {@link Individual} based on points
 * @author stepan
 *
 */
public class IndividualPoint  extends Individual {

	private static final long serialVersionUID = 1L;
	
	List<Double> coordinates;

	/**
	 * Constructor
	 * @param coordinates
	 */
	public IndividualPoint() {}
	
	/**
	 * Constructor
	 * @param coordinates
	 */
	public IndividualPoint(List<Double> coordinates) {
		if (coordinates == null) {
			throw new IllegalArgumentException();
		}
		this.coordinates = coordinates;
	}
	/**
	 * Copy constructor
	 * @param individual
	 */
	public IndividualPoint(IndividualPoint individual) {
		if (individual == null || ! individual.valid(new TrashLogger())) {
			throw new IllegalArgumentException();
		}
		List<Double> coordinatesNew = new ArrayList<>();
		for (Double valueI : individual.getCoordinates()) {
			coordinatesNew.add(new Double(valueI));
		}
		coordinates = coordinatesNew;
	}
	
	public List<Double> getCoordinates() {
		return coordinates;
	}
	@Deprecated
	public void setCoordinates(List<Double> coordinates) {
		this.coordinates = coordinates;
	}
	
	public double exportCoordinate(int coordinateIndex) {
		
		if (coordinateIndex < 0) {
			throw new IllegalArgumentException("Index of Coordinate can't be negative");
		}
		
		if ((coordinates == null) || (coordinates.size() < coordinateIndex)) {
			return Double.NaN;
		}
		
		return coordinates.get(coordinateIndex);
	}
	
	public void importCoordinate(int coordinateIndex, double value) {
		
		if (coordinateIndex < 0) {
			throw new IllegalArgumentException("Index of Coordinate can't be negative");
		}
		
		if (coordinates == null) {
			this.coordinates = new ArrayList<Double>();
		}
		
		if (coordinateIndex < coordinates.size()) {
			coordinates.set(coordinateIndex, value);
		} else {
			while(coordinates.size() -1 != coordinateIndex) {
				coordinates.add(null);
			}
			coordinates.set(coordinateIndex, value);
		}
		
	}
	
	@Override
	public boolean equals(Object other) {

	    if (other == null) {
	    	throw new IllegalArgumentException();
	    }

	    if (!(other instanceof IndividualPoint)) {
	        return false;
	    }
	    
	    IndividualPoint indPoint = (IndividualPoint)other;
	    //test validity of given Individual
	    if (! indPoint.valid(new TrashLogger())) {
	    	throw new IllegalArgumentException();
	    }
	    
	    if (this.coordinates.size() != indPoint.getCoordinates().size()) {
	    	return false;
	    }
	    
	    for (int coorIndex = 0; coorIndex < this.coordinates.size(); coorIndex++) {
	    	Double thisCoorI = this.getCoordinates().get(coorIndex);
	    	Double otherCoorI = indPoint.getCoordinates().get(coorIndex);
	    	
	    	if (thisCoorI != otherCoorI) {
	    		return false;
	    	}
	    }
	    
	    return true;
	}
		
    @Override
    public int hashCode() {
    	return toString().hashCode();
    }
    
	@Override
	public String toString() {
		return coordinates.toString();
	}
	
	
	@Override
	public String toLogString() {
		return coordinates.toString();
	}
	

	@Override
	public boolean valid(IAgentLogger logger) {
		
		if (coordinates == null) {
			return false;
		}
		return true;
	}
	
	/**
	 * Returns clone
	 */
	public Individual deepClone() {
		return new IndividualPoint(this);
	}
}
