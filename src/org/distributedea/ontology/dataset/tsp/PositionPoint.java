package org.distributedea.ontology.dataset.tsp;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;

/**
 * Ontology represents one TSP-Point position
 * @author stepan
 *
 */
public class PositionPoint extends Position {

	private static final long serialVersionUID = 1L;
	
	private int number;
	private double coordinateX;
	private double coordinateY;
	
	
	public PositionPoint() {
		number = -1;
		coordinateX = Double.NaN;
		coordinateY = Double.NaN;
	}
	
	/**
	 * Copy constructor
	 * @param point
	 */
	public PositionPoint(PositionPoint point) {
		if (point == null || ! point.valid(new TrashLogger())) {
			throw new IllegalArgumentException();
		}
		setNumber(point.getNumber());
		setCoordinateX(point.getCoordinateX());
		setCoordinateY(point.getCoordinateY());
	}
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	
	public double getCoordinateX() {
		return coordinateX;
	}
	public void setCoordinateX(double coordinateX) {
		this.coordinateX = coordinateX;
	}
	
	public double getCoordinateY() {
		return coordinateY;
	}
	public void setCoordinateY(double coordinateY) {
		this.coordinateY = coordinateY;
	}	
	
	/**
	 * Tests validity
	 */
	public boolean valid(IAgentLogger logger) {
		if (number < 0) {
			return false;
		}
		if (coordinateX == Double.NaN) {
			return false;
		}
		if (coordinateY == Double.NaN) {
			return false;
		}

		return true;
	}
	
	/**
	 * Returns clone
	 * @return
	 */
	public PositionPoint deepClone() {
		
		return new PositionPoint(this);
	}
}
