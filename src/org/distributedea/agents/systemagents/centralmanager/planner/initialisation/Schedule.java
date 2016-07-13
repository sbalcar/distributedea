package org.distributedea.agents.systemagents.centralmanager.planner.initialisation;

import jade.core.AID;

import java.util.List;

import org.distributedea.agents.systemagents.centralmanager.planner.tool.Pair;
import org.distributedea.ontology.agentdescription.AgentDescription;

public class Schedule {
	
	private List<Pair<AID,AgentDescription>> schedule;
	
	private List<AgentDescription> nextCandidates;

	public Schedule() {
	}

	public Schedule (List<Pair<AID,AgentDescription>> schedule, List<AgentDescription> nextCandidates) {
		this.schedule = schedule;
		this.nextCandidates = nextCandidates;
	}

	public List<Pair<AID, AgentDescription>> getSchedule() {
		return schedule;
	}
	public void setPlan(List<Pair<AID, AgentDescription>> plan) {
		this.schedule = plan;
	}

	public List<AgentDescription> getNextCandidates() {
		return nextCandidates;
	}
	public void setNextCandidates(List<AgentDescription> nextCandidates) {
		this.nextCandidates = nextCandidates;
	}
	
}
