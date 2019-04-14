package org.distributedea.problems.evcharging;

import org.distributedea.problems.IProblemToolTabuSearch;

public abstract class AProblemToolTabuSearchEVCharging extends AAProblemToolEVCharging implements IProblemToolTabuSearch {

	@Deprecated
	public AProblemToolTabuSearchEVCharging() {
		super();
	}

	public AProblemToolTabuSearchEVCharging(double normalDistMultiplicator) {
		super(normalDistMultiplicator);
	}

}
