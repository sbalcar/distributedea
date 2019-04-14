package org.distributedea.problems.evcharging;

import org.distributedea.problems.IProblemToolHillClimbing;

public abstract class AProblemToolHillClimbingEVCharging extends AAProblemToolEVCharging implements IProblemToolHillClimbing {

	@Deprecated
	public AProblemToolHillClimbingEVCharging() {
		super();
	}

	public AProblemToolHillClimbingEVCharging(double normalDistMultiplicator) {
		super(normalDistMultiplicator);
	}

}
