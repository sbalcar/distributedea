package org.distributedea.ontology.computing.result;

import jade.content.Concept;

import java.util.ArrayList;
import java.util.List;

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
		
	public ResultOfComputing exportBestResultOfComputing(Problem problem) {
		
		if (resultOfComputing == null || resultOfComputing.isEmpty()) {
			return null;
		}
		
		ResultOfComputing bestResult = resultOfComputing.get(0);
		
		for (ResultOfComputing resultOfComputingI : resultOfComputing) {
			
			double fitnessValueI = resultOfComputingI.getFitnessValue();
			
			boolean isBetter = ProblemToolEvaluation.isFistFitnessBetterThanSecond(
					fitnessValueI, bestResult.getFitnessValue(), problem);
			if (isBetter) {
				bestResult = resultOfComputingI;
			}
		}
		
		return bestResult;
	}
	
}
