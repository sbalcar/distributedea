package org.distributedea.ontology.monitor;

import jade.content.Concept;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.distributedea.agents.systemagents.centralmanager.structures.history.comparators.methodstatistic.ComparatorQualityOfBestIndividual;
import org.distributedea.agents.systemagents.centralmanager.structures.history.comparators.methodstatistic.ComparatorQuantityOfImprovement;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.logging.TrashLogger;
import org.distributedea.ontology.individualhash.IndividualHash;
import org.distributedea.ontology.methoddescription.MethodDescription;
import org.distributedea.ontology.methoddescription.MethodDescriptions;
import org.distributedea.ontology.problem.IProblem;

/**
 * Ontology represents statistics of computational methods.
 * @author stepan
 *
 */
public class MethodStatistics implements Concept {

	private static final long serialVersionUID = 1L;
	
	private List<MethodStatistic> methodStatistics;
	
	@Deprecated // only for Jade
	public MethodStatistics() {
		this.methodStatistics = new ArrayList<>();
	}
	
	/**
	 * Constructor
	 * @param methodStatistics
	 */
	public MethodStatistics(List<MethodStatistic> methodStatistics) {
		this.setStatistics(methodStatistics);
	}
	
	
	public List<MethodStatistic> getStatistics() {
		return methodStatistics;
	}
	@Deprecated
	public void setStatistics(List<MethodStatistic> statistics) {
		if (statistics == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is not valid");
		}
		for (MethodStatistic methStatisI : statistics) {
			if (methStatisI == null ||
					! methStatisI.valid(new TrashLogger())) {
				throw new IllegalArgumentException("Argument " +
						MethodStatistic.class.getSimpleName() +
						" is not valid");
			}
		}
		this.methodStatistics = statistics;
	}
	
	public void addMethodStatistic(MethodStatistic methodStatistic) {
		if (methodStatistics == null) {
		    methodStatistics = new ArrayList<>();
		}
		methodStatistics.add(methodStatistic);
	}
	
	public long exportNumberOfMethods() {
		if (methodStatistics == null) {
			return 0;
		}
		return methodStatistics.size();
	}
	
	public MethodStatistic exportRandomMethodStatistic() {
		Random ran = new Random();
		int index = ran.nextInt(methodStatistics.size());
		return methodStatistics.get(index);
	}
	
	public long exportNumberOfImprovementsAchievedByAllMethods() {
		if (methodStatistics == null) {
			return 0;
		}
		
		long numberOfImprovement = 0;
		for (MethodStatistic statisticI : methodStatistics) {
			
			MethodStatisticResult statisticResultI =
					statisticI.getMethodStatisticResult(); 
			numberOfImprovement += statisticResultI
					.getNumberOfTheBestCreatedIndividuals();
		}
		return numberOfImprovement;
	}

	public long exportNumberOfGoodMaterialAchievedByAllMethods() {
		if (methodStatistics == null) {
			return 0;
		}
		
		long numberOfGoodMaterial = 0;
		for (MethodStatistic statisticI : methodStatistics) {
			
			MethodStatisticResult statisticResultI =
					statisticI.getMethodStatisticResult(); 			
			numberOfGoodMaterial += statisticResultI
					.getNumberOfGoodCreatedMaterial();
		}
		return numberOfGoodMaterial;
	}

	public MethodStatistic exportMethodAchievedTheGreatestQuantityOfImprovement() {
		
		if (this.methodStatistics == null || this.methodStatistics.isEmpty()) {
			return null;
		}
		
		Collections.sort(methodStatistics, new ComparatorQuantityOfImprovement());
		return methodStatistics.get(methodStatistics.size() -1);
	}

	public MethodStatistic exportMethodAchievedTheLeastQuantityOfImprovement() {
		
		if (this.methodStatistics == null || this.methodStatistics.isEmpty()) {
			return null;
		}
		
		Collections.sort(methodStatistics, new ComparatorQuantityOfImprovement());
		return methodStatistics.get(0);
	}
	
	public IndividualHash exportBestIndividual() {
		
		if (this.methodStatistics == null || this.methodStatistics.isEmpty()) {
			return null;
		}
		
		MethodStatistic method0 = this.methodStatistics.get(0);
		MethodDescription agentDescription = method0.getMethodDescription();
		
		IProblem problem = agentDescription.getProblem();
		
		Collections.sort(methodStatistics,
				new ComparatorQualityOfBestIndividual(problem));
		MethodStatistic methodWithBestIdividual =
				methodStatistics.get(methodStatistics.size() -1);
		
		return methodWithBestIdividual.exportBestIndividual();
	}
	
	/**
	 * Exports {@link MethodDescriptions} clones
	 * @return
	 */
	public MethodDescriptions exportAgentDescriptions() {
		
		List<MethodDescription> agentDescriptions = new ArrayList<>();
		for (MethodStatistic methodStatisticI : this.methodStatistics) {
			MethodDescription agentDescriptionClonesI =
					methodStatisticI.exportMethodDescriptionClone();
			agentDescriptions.add(agentDescriptionClonesI);
		}
		return new MethodDescriptions(agentDescriptions);
	}
	
	/**
	 * Tests if this {@link Statistic} contains all {@link MethodDescription}
	 * from given {@link MethodDescriptions}.
	 * @param agentDescriptions
	 * @return
	 */
	public boolean containsAllAgentDescriptions(MethodDescriptions agentDescriptions) {
		if (agentDescriptions == null ||
				! agentDescriptions.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					agentDescriptions.getClass().getSimpleName() + " is not valid.");
		}
		
		for (MethodDescription agentDescrI :
				agentDescriptions.getAgentDescriptions()) {
			if (! containsAgentDescriptions(agentDescrI)) {
				containsAgentDescriptions(agentDescrI);
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Tests if this {@link Statistic} contains given {@link MethodDescription}.
	 * @param agentDescription
	 * @return
	 */
	public boolean containsAgentDescriptions(MethodDescription agentDescription) {
		if (agentDescription == null ||
				! agentDescription.valid(new TrashLogger())) {
			throw new IllegalArgumentException("Argument " +
					agentDescription.getClass().getSimpleName() + " is not valid.");
		}
		for (MethodStatistic methodStatisticI : this.methodStatistics) {
			MethodDescription agentDescriptionI =
					methodStatisticI.getMethodDescription();
			if (agentDescriptionI.equals(agentDescription)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Tests validity
	 * @param logger
	 * @return
	 */
	public boolean valid(IAgentLogger logger) {
		
		if (methodStatistics == null) {
			return false;
		}
		for (MethodStatistic methodStatisticI : methodStatistics) {
			if (methodStatisticI == null || ! methodStatisticI.valid(logger)) {
				return false;
			}
		}
		
		return true;
	}
}
