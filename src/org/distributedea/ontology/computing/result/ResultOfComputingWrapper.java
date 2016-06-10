package org.distributedea.ontology.computing.result;

import jade.content.Concept;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.problems.ProblemToolEvaluation;

public class ResultOfComputingWrapper implements Concept {

	private static final long serialVersionUID = 1L;
	
	private List<ResultOfComputing> resultOfComputing;

	
	public List<ResultOfComputing> getResultOfComputing() {
		return resultOfComputing;
	}
	public void setResultOfComputing(List<ResultOfComputing> resultOfComputing) {
		this.resultOfComputing = resultOfComputing;
	}
	
	public void addResultOfComputing(ResultOfComputing resultOfComputing) {
		
		if (this.resultOfComputing == null) {
			this.resultOfComputing = new ArrayList<>();
		}
		
		this.resultOfComputing.add(resultOfComputing);
	}

	public boolean exportContainsMoreThanOneMethod() {
		
		if (this.resultOfComputing == null ||
				this.resultOfComputing.size() <= 1) {
			return false;
		}
		return true;
	}
	
	public ResultOfComputing exportBestResultOfComputing(Problem problem) {
		
		if (resultOfComputing == null || resultOfComputing.isEmpty()) {
			return null;
		}
		
		ResultOfComputing bestResult = resultOfComputing.get(0);
		
		for (ResultOfComputing resultOfComputingI : resultOfComputing) {
			
			double fitnessValueI = resultOfComputingI.getFitnessValue();
			
			boolean isNewIndividualBetter = ProblemToolEvaluation.isFistFitnessBetterThanSecond(
					fitnessValueI, bestResult.getFitnessValue(), problem);
			if (isNewIndividualBetter) {
				bestResult = resultOfComputingI;
			}
		}
		
		return bestResult;
	}

	public ResultOfComputing exportWorstResultOfComputing(Problem problem) {
		
		if (resultOfComputing == null || resultOfComputing.isEmpty()) {
			return null;
		}
		
		ResultOfComputing worstResult = resultOfComputing.get(0);
		
		for (ResultOfComputing resultOfComputingI : resultOfComputing) {
			
			double fitnessValueI = resultOfComputingI.getFitnessValue();
			
			boolean isNewIndividualWorse =
					ProblemToolEvaluation.isFistFitnessWorseThanSecond(
							fitnessValueI, worstResult.getFitnessValue(), problem);
			if (isNewIndividualWorse) {
				worstResult = resultOfComputingI;
			}
		}
		
		return worstResult;
	}
	
	public ResultOfComputingWrapper exportResultsOfGivenAgentType(List<Class<?>> agentType) {
		
		ResultOfComputingWrapper resultWrp = new ResultOfComputingWrapper();
		
		for (ResultOfComputing resultI : resultOfComputing) {
			
			AgentDescription agentDescriptionI = resultI.getAgentDescription();
			AgentConfiguration agentConfigurationI = agentDescriptionI.getAgentConfiguration();
			Class<?> agentTypeI = agentConfigurationI.exportAgentType();
			
			if (agentType.contains(agentTypeI)) {
				resultWrp.addResultOfComputing(resultI);
			}
		}
		
		return resultWrp;
	}
	
	public int exportAgentNumberOfType(List<Class<?>> agentType) {
	
		int counter = 0;
		for (ResultOfComputing resultI : resultOfComputing) {
			
			AgentDescription agentDescriptionI = resultI.getAgentDescription();
			AgentConfiguration agentConfigurationI = agentDescriptionI.getAgentConfiguration();
			Class<?> agentTypeI = agentConfigurationI.exportAgentType();
			
			if (agentType.contains(agentTypeI)) {
				counter++;
			}
		}
		
		return counter;
	}

	public boolean containsAgentTypes(Class<?> agentType) {
		
		for (ResultOfComputing resultI : resultOfComputing) {
			
			AgentDescription agentDescriptionI = resultI.getAgentDescription();
			AgentConfiguration agentConfigurationI = agentDescriptionI.getAgentConfiguration();
			Class<?> agentTypeI = agentConfigurationI.exportAgentType();
			
			if (agentType == agentTypeI) {
				return true;
			}
		}
		return false;
	}
	
	public List<Class<?>> exportAgentTypesWhichDontContains(List<Class<?>> agentTypes) {
		
		List<Class<?>> agentTypesSupplement = new ArrayList<>();
		
		for (Class<?> agentTypeI : agentTypes) {
			
			if (! containsAgentTypes(agentTypeI)) {
				if (! agentTypesSupplement.contains(agentTypeI)) {
					
					agentTypesSupplement.add(agentTypeI);
				}
			}
		}
		return agentTypesSupplement;
	}
	
}
