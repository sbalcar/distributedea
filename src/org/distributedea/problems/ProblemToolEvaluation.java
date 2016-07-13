package org.distributedea.problems;

import org.distributedea.agents.FitnessTool;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.individualwrapper.IndividualWrapper;
import org.distributedea.ontology.problem.Problem;

public class ProblemToolEvaluation {

	public static boolean isFistIndividualWBetterThanSecond(IndividualEvaluated individualEval1,
			IndividualEvaluated individualEval2, Class<?> problemClass) {

		Problem problem = null;
		try {
			problem = (Problem)problemClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (individualEval1 == null) {
			return false;
			
		} else if (individualEval2 == null) {
			return true;
		}

		return isFistIndividualWBetterThanSecond(individualEval1,
				individualEval2, problem);
	}
	
	public static boolean isFistIndividualWBetterThanSecond(IndividualEvaluated individualEval1,
			IndividualEvaluated individualEval2, Problem problem) {
		
		double fitness1 = individualEval1.getFitness();
		double fitness2 = individualEval2.getFitness();
		return isFistFitnessBetterThanSecond(fitness1,
				fitness2, problem);
	}
	public static boolean isFistIndividualWWorseThanSecond(IndividualEvaluated individualEval1,
			IndividualEvaluated individualEval2, Problem problem) {
		
		double fitness1 = individualEval1.getFitness();
		double fitness2 = individualEval2.getFitness();
		return isFistFitnessWorseThanSecond(fitness1,
				fitness2, problem);
	}
	public static boolean isFistIndividualWBetterThanSecond(IndividualWrapper individual1,
			double fitness2, Problem problem) {
		
		if (individual1 == null) {
			return false;
		}
		
		IndividualEvaluated individualE1 = individual1.getIndividualEvaluated();
		
		if (individualE1 == null) {
			return false;
		}
		
		return isFistFitnessBetterThanSecond(
				individualE1.getFitness(), fitness2, problem);
	}
	
	public static boolean isFistIndividualWBetterThanSecond(IndividualWrapper individual1,
			IndividualWrapper individual2, Problem problem) {
		
		IndividualEvaluated individualE1 = individual1.getIndividualEvaluated();
		IndividualEvaluated individualE2 = individual2.getIndividualEvaluated();
		
		return isFistIndividualBetterThanSecond(individualE1, individualE2, problem);
	}
	
	public static boolean isFistIndividualBetterThanSecond(IndividualEvaluated individual1,
			IndividualEvaluated individual2, Problem problem) {
		
		return isFistFitnessBetterThanSecond(
				individual1.getFitness(), individual2.getFitness(), problem);
	}
	
	public static boolean isFistFitnessBetterThanSecond(double fintess1,
			double fitness2, Problem problem) {
		
		return FitnessTool.isFistFitnessBetterThanSecond(fintess1,
				fitness2, problem);
	}

	public static boolean isFistFitnessWorseThanSecond(double fintess1,
			double fitness2,  Problem problem) {
		
		boolean isBetter = isFistFitnessBetterThanSecond(fintess1,
				fitness2, problem);
		boolean isEqual = isFistFitnessEqualToSecond(fintess1,
				fitness2, problem);
		
		if ( (! isBetter) && (! isEqual) ) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isFistFitnessEqualToSecond(double fintess1,
			double fitness2, Problem problem) {
		return (fintess1 == fitness2);
	}
	
	public static Class<?> getProblemClassFromProblemTool(Class<?> problemToolClass) {
		
		ProblemTool tool = null;
		try {
			tool = (ProblemTool) problemToolClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return tool.problemWhichSolves();
	}
	
}
