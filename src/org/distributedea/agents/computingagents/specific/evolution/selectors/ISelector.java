package org.distributedea.agents.computingagents.specific.evolution.selectors;

import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.problem.IProblem;

public interface ISelector {

	public IndividualEvaluated select( IndividualEvaluated[] individuals, IProblem problem);
}
