package org.distributedea.agents.systemagents.monitor.statisticmodel.methodmodel;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.monitor.MethodStatistic;
import org.distributedea.ontology.monitor.MethodStatistics;
import org.distributedea.ontology.monitor.Statistic;

/**
 * Model for methods statistics
 * @author stepan
 *
 */
public class MethodStatisticsModel {

	private List<MethodStatisticModel> methods = new ArrayList<>();	

	
	/**
	 * Add {@link MethodStatisticModel}
	 * @param model
	 */
	public void add(MethodStatisticModel model) {
		methods.add(model);
	}
	
	/**
	 * Get {@link MethodStatisticModel}
	 * @param description
	 * @return
	 */
	public MethodStatisticModel getMethodStatisticModel(MethodDescription description) {
		
		for (MethodStatisticModel modelI : methods) {
			
			MethodDescription descriptionI = modelI.getAgentDescription();
			if (descriptionI.equals(description)) {
				return modelI;
			}
		}
		
		return null;
	}
	
	/**
	 * Exports {@link Statistic}
	 * @return
	 */
	public MethodStatistics exportMethodStatistics() {
		
		List<MethodStatistic> methodStatistics = new ArrayList<>();
		
		for (MethodStatisticModel methodStatModI : methods) {
			methodStatistics.add(
					methodStatModI.exportMethodStatistic());
		}
		
		return new MethodStatistics(methodStatistics);
	}
	
}
