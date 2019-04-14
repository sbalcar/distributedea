package org.distributedea.problems.evcharging;

import org.distributedea.problems.IProblemToolEvolution;

public abstract class AProblemToolEvolutionEVCharging extends AAProblemToolEVCharging implements IProblemToolEvolution {

	@Deprecated
	public AProblemToolEvolutionEVCharging() {
		super();
	}

	public AProblemToolEvolutionEVCharging(double normalDistMultiplicator) {
		super(normalDistMultiplicator);
	}

}
