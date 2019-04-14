package org.distributedea.problems.evcharging;

import org.distributedea.problems.IProblemToolDifferentialEvolution;

public abstract class AProblemToolDifferentialEvolutionEVCharging extends AAProblemToolEVCharging implements IProblemToolDifferentialEvolution {

	@Deprecated
	public AProblemToolDifferentialEvolutionEVCharging() {
		super();
	}

	public AProblemToolDifferentialEvolutionEVCharging(double normalDistMultiplicator) {
		super(normalDistMultiplicator);
	}

}
