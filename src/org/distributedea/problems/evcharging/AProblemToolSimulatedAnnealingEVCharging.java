package org.distributedea.problems.evcharging;

import org.distributedea.problems.IProblemToolSimulatedAnnealing;

public abstract class AProblemToolSimulatedAnnealingEVCharging extends AAProblemToolEVCharging implements IProblemToolSimulatedAnnealing {

	@Deprecated
	public AProblemToolSimulatedAnnealingEVCharging() {
		super();
	}

	public AProblemToolSimulatedAnnealingEVCharging(double normalDistMultiplicator) {
		super(normalDistMultiplicator);
	}

}
