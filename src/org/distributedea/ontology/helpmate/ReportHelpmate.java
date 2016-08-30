package org.distributedea.ontology.helpmate;

import org.distributedea.agents.computingagents.computingagent.Agent_ComputingAgent;

import jade.content.AgentAction;

/**
 * Ontology for Request for helpmates of one {@link Agent_ComputingAgent}
 * @author stepan
 *
 */
public class ReportHelpmate implements AgentAction {

	private static final long serialVersionUID = 1L;

	private boolean newStatisticsForEachQuery;

	@Deprecated
	public ReportHelpmate() {} //only for Jade
	
	/**
	 * Constructor
	 * @param newStatisticsForEachQuery
	 */
	public ReportHelpmate(boolean newStatisticsForEachQuery) {
		this.newStatisticsForEachQuery = newStatisticsForEachQuery;
	}
	
	public boolean isNewStatisticsForEachQuery() {
		return newStatisticsForEachQuery;
	}
	public void setNewStatisticsForEachQuery(boolean newStatisticsForEachQuery) {
		this.newStatisticsForEachQuery = newStatisticsForEachQuery;
	}
	
}
