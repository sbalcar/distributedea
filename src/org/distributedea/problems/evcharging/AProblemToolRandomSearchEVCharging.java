package org.distributedea.problems.evcharging;

import org.distributedea.problems.IProblemToolRandomSearch;

public abstract class AProblemToolRandomSearchEVCharging extends AAProblemToolEVCharging implements IProblemToolRandomSearch {

	@Deprecated
	public AProblemToolRandomSearchEVCharging() {
		super();
	}
	
	public AProblemToolRandomSearchEVCharging(double normalDistMultiplicator) {
		super(normalDistMultiplicator);
	}

}
