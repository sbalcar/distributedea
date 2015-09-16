package org.distributedea.ontology.problem.tsp;

import jade.content.Concept;

public class PositionGPS implements Concept {

	private static final long serialVersionUID = 1L;
	
	private int number;
	private double latitude;
	private double longitude;
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
}