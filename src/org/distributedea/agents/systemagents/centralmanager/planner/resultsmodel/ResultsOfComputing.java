package org.distributedea.agents.systemagents.centralmanager.planner.resultsmodel;

import jade.content.Concept;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.ontology.agentdescription.AgentDescription;
import org.distributedea.ontology.configuration.AgentConfiguration;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.problems.ProblemToolEvaluation;

public class ResultsOfComputing implements Concept {

	private static final long serialVersionUID = 1L;
	
	private List<IndividualWrapper> resultOfComputing;

	
	public List<IndividualWrapper> getResultOfComputing() {
		return resultOfComputing;
	}
	public void setResultOfComputing(List<IndividualWrapper> resultOfComputing) {
		this.resultOfComputing = resultOfComputing;
	}
	
	public void addResultOfComputing(IndividualWrapper resultOfComputing) {
		
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
	
	public IndividualWrapper exportBestResultOfComputing(Problem problem) {
		
		if (resultOfComputing == null || resultOfComputing.isEmpty()) {
			return null;
		}
		
		IndividualWrapper bestResult = resultOfComputing.get(0);
		
		for (IndividualWrapper resultOfComputingI : resultOfComputing) {
			
			IndividualEvaluated individualEvalI =
					resultOfComputingI.getIndividualEvaluated();
			
			boolean isNewIndividualBetter = ProblemToolEvaluation.
					isFistIndividualWBetterThanSecond(individualEvalI,
							bestResult.getIndividualEvaluated(), problem);
			
			if (isNewIndividualBetter) {
				bestResult = resultOfComputingI;
			}
		}
		
		return bestResult;
	}

	public IndividualWrapper exportWorstResultOfComputing(Problem problem) {
		
		if (resultOfComputing == null || resultOfComputing.isEmpty()) {
			return null;
		}
		
		IndividualWrapper worstResult = resultOfComputing.get(0);
		
		for (IndividualWrapper resultOfComputingI : resultOfComputing) {
			
			IndividualEvaluated individualEvalI =
					resultOfComputingI.getIndividualEvaluated();
			
			boolean isNewIndividualWorse = ProblemToolEvaluation.
					isFistIndividualWWorseThanSecond(individualEvalI,
							worstResult.getIndividualEvaluated(), problem);
			if (isNewIndividualWorse) {
				worstResult = resultOfComputingI;
			}
		}
		
		return worstResult;
	}
	
	public ResultsOfComputing exportResultsOfGivenAgentType(List<Class<?>> agentType) {
		
		ResultsOfComputing resultWrp = new ResultsOfComputing();
		
		for (IndividualWrapper resultI : resultOfComputing) {
			
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
		for (IndividualWrapper resultI : resultOfComputing) {
			
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
		
		for (IndividualWrapper resultI : resultOfComputing) {
			
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
