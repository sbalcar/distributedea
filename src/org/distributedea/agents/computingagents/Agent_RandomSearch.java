package org.distributedea.agents.computingagents;

import org.distributedea.agents.systemagents.centralmanager.structures.pedigree.PedigreeParameters;
import org.distributedea.ontology.agentinfo.AgentInfo;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problemdefinition.IProblemDefinition;
import org.distributedea.problems.IProblemTool;

/**
 * Agent represents Random Search Algorithm Method
 * @author stepan
 *
 */
public class Agent_RandomSearch extends Agent_HillClimbing {

	private static final long serialVersionUID = 1L;

	@Override
	protected AgentInfo getAgentInfo() {
		
		AgentInfo description = new AgentInfo();
		description.importComputingAgentClassName(this.getClass());
		description.setNumberOfIndividuals(1);
		description.setExploitation(true);
		description.setExploration(false);
		
		return description;
	}
	
	@Override
	protected IndividualEvaluated getNewIndividual(IndividualEvaluated individual,
			IProblemDefinition problemDef, Problem problem, IProblemTool problemTool,
			PedigreeParameters pedigreeParams) {

		return problemTool.generateIndividualEval(problemDef, problem, pedigreeParams, getLogger());
	}
	
}
