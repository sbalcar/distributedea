package org.distributedea.ontology.dataset.tsp;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;

/**
 * Ontology represents one TSP-GPS position
 * @author stepan
 *
 */
public class PositionGPS extends Position {

	private static final long serialVersionUID = 1L;
	
	private int number;
	private double latitude;
	private double longitude;
	
	
	public PositionGPS() {
		number = -1;
		latitude = Double.NaN;
		longitude = Double.NaN;
	}
	
	/**
	 * Copy constructor
	 * @param position
	 */
	public PositionGPS(PositionGPS position) {
		if (position == null || ! position.valid(new TrashLogger())) {
			throw new IllegalArgumentException();
		}
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
	
	/**
	 * Tests validity
	 */
	public boolean valid(IAgentLogger logger) {
		if (number < 0) {
			return false;
		}
		if (latitude == Double.NaN) {
			return false;
		}
		if (longitude == Double.NaN) {
			return false;
		}

		return true;
	}
	
	/**
	 * Returns clone
	 * @return
	 */
	public PositionGPS deepClone() {
		return new PositionGPS(this);
	}
}
