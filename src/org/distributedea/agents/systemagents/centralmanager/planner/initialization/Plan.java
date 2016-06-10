package org.distributedea.agents.systemagents.centralmanager.planner.initialization;

import jade.core.AID;

import java.util.List;

import org.distributedea.agents.systemagents.centralmanager.planner.tool.Pair;
import org.distributedea.ontology.agentdescription.AgentDescription;

public class Plan {
	
	private List<Pair<AID,AgentDescription>> plan;
	
	private List<AgentDescription> nextCandidates;

	public Plan() {
	}

	public Plan (List<Pair<AID,AgentDescription>> plan, List<AgentDescription> nextCandidates) {
		this.plan = plan;
		this.nextCandidates = nextCandidates;
	}

	public List<Pair<AID, AgentDescription>> getPlan() {
		return plan;
	}
	public void setPlan(List<Pair<AID, AgentDescription>> plan) {
		this.plan = plan;
	}

	public List<AgentDescription> getNextCandidates() {
		return nextCandidates;
	}
	public void setNextCandidates(List<AgentDescription> nextCandidates) {
		this.nextCandidates = nextCandidates;
	}
	
}
