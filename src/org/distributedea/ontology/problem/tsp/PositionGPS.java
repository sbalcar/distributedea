package org.distributedea.ontology.problem.tsp;


public class PositionGPS extends Position {

	private static final long serialVersionUID = 1L;
	
	private int number;
	private double latitude;
	private double longitude;
	
	public PositionGPS() {}
	
	public PositionGPS(PositionGPS position) {
		
		setNumber(position.getNumber());
		setLatitude(position.getLatitude());
		setLongitude(position.getLongitude());
	}
	
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
	
	public PositionGPS deepClone() {
		return new PositionGPS(this);
	}
}
