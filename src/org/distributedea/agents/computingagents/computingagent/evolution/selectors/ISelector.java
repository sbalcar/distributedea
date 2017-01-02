package org.distributedea.agents.computingagents.computingagent.evolution.selectors;

import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualsEvaluated;
import org.distributedea.ontology.problemdefinition.IProblemDefinition;

public interface ISelector {

	public IndividualEvaluated select(IndividualsEvaluated individuals, IProblemDefinition problemDef);
}
