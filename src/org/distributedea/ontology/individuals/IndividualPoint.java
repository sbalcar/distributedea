package org.distributedea.ontology.individuals;

import java.util.ArrayList;
import java.util.List;

public class IndividualPoint  extends Individual {

	private static final long serialVersionUID = 1L;
	
	List<Double> coordinates;

	public List<Double> getCoordinates() {
		return coordinates;
	}

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
	public boolean validation() {
		
		if (coordinates == null) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean equals(Object other) {
		
	    if (!(other instanceof IndividualPoint)) {
	        return false;
	    }
	    
	    IndividualPoint indPoint = (IndividualPoint)other;
	    
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
	public String toLogString() {
		return coordinates.toString();
	}
	
}
