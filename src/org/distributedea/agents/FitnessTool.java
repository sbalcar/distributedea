package org.distributedea.agents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.problem.IProblem;

/**
 * Tool for comparing fitness of {@link Individual}s. This tool
 * doesn't validity of Individuals. Tool expect, that you work with
 * valid {@link Individual}s.
 * @author stepan
 *
 */
public class FitnessTool {

	public static double getTheBestFitnessValueFrom(List<Double> fitnessValues, IProblem problem) {
	
		List<Double> listSorted = new ArrayList<Double>(fitnessValues);
		Collections.sort(listSorted);
		
		if (problem.exportIsMaximizationProblem()) {
			return listSorted.get(listSorted.size() -1);
		} else {
			return listSorted.get(0);
		}
	}
	
	public static boolean isFistIndividualWBetterThanSecond(IndividualWrapper individual1,
			IndividualWrapper individual2, IProblem problem) {
		
		if (individual1 == null) {
			return false;
		} else if (individual2 == null) {
			return true;
		}
		
		IndividualEvaluated indivEv1 = individual1.getIndividualEvaluated();
		IndividualEvaluated indivEv2 = individual2.getIndividualEvaluated();
		
		return isFirstIndividualEBetterThanSecond(indivEv1, indivEv2, problem);
	}
	
	public static boolean isFistIndividualWBetterThanSecond(IndividualWrapper individualWrp1,
			IndividualEvaluated individualEval2, IProblem problem) {
		
		if (individualWrp1 == null) {
			return false;
		}
		IndividualEvaluated individualEval1 = individualWrp1.getIndividualEvaluated();
		
		return isFirstIndividualEBetterThanSecond(individualEval1,
				individualEval2, problem);
	}
	
	public static boolean isFirstIndividualWBetterThanSecond(IndividualWrapper individual1,
			double fitness2, IProblem problem) {
		
		if (individual1 == null) {
			return false;
		}
		
		IndividualEvaluated individualE1 = individual1.getIndividualEvaluated();
		
		return isFistFitnessBetterThanSecond(
				individualE1.getFitness(), fitness2, problem);
	}
	
	public static boolean isFistIndividualEBetterThanSecond(IndividualEvaluated individualEval1,
			IndividualEvaluated individualEval2, IProblem problem) {
		
		if (individualEval1 == null) {
			return false;
			
		} else if (individualEval2 == null) {
			return true;
		}

		return isFirstIndividualEBetterThanSecond(individualEval1,
				individualEval2, problem);
	}
	
	public static boolean isFirstIndividualEBetterThanSecond(IndividualEvaluated individualEval1,
			IndividualEvaluated individualEval2, IProblem problem) {
		
		if (individualEval1 == null) {
			return false;
		
		} else if (individualEval2 == null) {
			return true;
		}
		
		double fitness1 = individualEval1.getFitness();
		double fitness2 = individualEval2.getFitness();
		
		return isFistFitnessBetterThanSecond(fitness1, fitness2, problem);
	}
	
	public static boolean isFirstIndividualEWorseThanSecond(IndividualEvaluated individualEval1,
			IndividualEvaluated individualEval2, IProblem problem) {
		
		if (individualEval1 == null) {
			return false;
			
		} else if (individualEval2 == null) {
			return true;
		}
		
		double fitness1 = individualEval1.getFitness();
		double fitness2 = individualEval2.getFitness();
		
		return isFistFitnessWorseThanSecond(fitness1, fitness2, problem);
	}
	
	public static boolean isFistFitnessWorseThanSecond(double fitness1,
			double fitness2,  IProblem problem) {
		
		boolean isBetter = isFistFitnessBetterThanSecond(fitness1,
				fitness2, problem);
		boolean isEqual = isFistFitnessEqualToSecond(fitness1,
				fitness2, problem);
		
		if ( (! isBetter) && (! isEqual) ) {
			return true;
		} else {
			return false;
		}
	}
	
	private static boolean isFistFitnessEqualToSecond(double fintess1,
			double fitness2, IProblem problem) {
		return (fintess1 == fitness2);
	}
	
	public static boolean isFistFitnessBetterThanSecond(double fitness1,
			double fitness2, IProblem problem) {
		
		if (problem.exportIsMaximizationProblem()) {
			if (fitness1 > fitness2) {
				return true;
			}
		} else {
			if (fitness1 < fitness2) {
				return true;
			}
		}
		
		return false;
	}
}
