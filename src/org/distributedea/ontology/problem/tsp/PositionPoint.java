package org.distributedea.ontology.problem.tsp;

public class PositionPoint extends Position {

	private static final long serialVersionUID = 1L;
	
	private int number;
	private double coordinateX;
	private double coordinateY;
	
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
	
}
