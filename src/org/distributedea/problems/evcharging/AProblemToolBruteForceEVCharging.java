package org.distributedea.problems.evcharging;

import org.distributedea.problems.IProblemToolBruteForce;

public abstract class AProblemToolBruteForceEVCharging extends AAProblemToolEVCharging implements IProblemToolBruteForce {

	@Deprecated
	public AProblemToolBruteForceEVCharging() {
		super();
	}
	
	public AProblemToolBruteForceEVCharging(double normalDistMultiplicator) {
		super(normalDistMultiplicator);
	}

}
