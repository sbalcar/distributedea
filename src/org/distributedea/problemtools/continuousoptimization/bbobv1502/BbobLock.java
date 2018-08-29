package org.distributedea.problemtools.continuousoptimization.bbobv1502;

public class BbobLock {

	private static BbobLock bInstance;
	
	// generates ID of Instance
	private int counter = 0;
	
	private BbobLock() {}
	
	public static BbobLock getInstance() {
		
		if (bInstance == null) {
			bInstance = new BbobLock();
		}
		
		return bInstance;
	}
	
	public synchronized int getID() {
		
		int id = counter;
		
		this.counter++;
		
		return id;
	}
}
