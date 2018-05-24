package org.distributedea.agents.systemagents.monitor.statisticmodel.individualhashesmodel;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.agents.FitnessTool;
import org.distributedea.ontology.individualhash.IndividualHash;
import org.distributedea.ontology.problem.IProblem;

public class IndividualHashesModel {

	private int MAX_SIZE_OF_MATERIAL = 5;
	
	private List<IndividualHash> bestGeneticMaterial = new ArrayList<>();
	
	public boolean update(IndividualHash individual, IProblem problem) {
		
		if (bestGeneticMaterial.size() < MAX_SIZE_OF_MATERIAL) {
			bestGeneticMaterial.add(individual);
			return true;
		}
		
		IndividualHash exportTheWorstIndividual =
				exportTheWorstIndividual(problem);
		
		boolean isBetter = FitnessTool.isFistFitnessBetterThanSecond(
				individual.getFitness(), exportTheWorstIndividual.getFitness(), problem);
		
		if (isBetter) {
			bestGeneticMaterial.remove(exportTheWorstIndividual);
			bestGeneticMaterial.add(individual);
		}
		
		return isBetter;
	}
	
	
	private IndividualHash exportTheWorstIndividual(IProblem problem) {
		
		if (bestGeneticMaterial.isEmpty()) {
			return null;
		}
		
		IndividualHash individualTheWorst = bestGeneticMaterial.get(0);
		for (IndividualHash individualI : bestGeneticMaterial) {
			
			boolean isWore = FitnessTool.isFistFitnessWorseThanSecond(
					individualI.getFitness(), individualTheWorst.getFitness(), problem);
			if (isWore) {
				individualTheWorst = individualI;
			}
		}
		
		return individualTheWorst;
	}
}
