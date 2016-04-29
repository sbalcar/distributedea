package org.distributedea.ontology.helpmate;

import jade.content.AgentAction;

public class ReportHelpmate implements AgentAction {

	private static final long serialVersionUID = 1L;

	private boolean newStatisticsForEachQuery;

	
	public boolean isNewStatisticsForEachQuery() {
		return newStatisticsForEachQuery;
	}
	public void setNewStatisticsForEachQuery(boolean newStatisticsForEachQuery) {
		this.newStatisticsForEachQuery = newStatisticsForEachQuery;
	}
	
}
