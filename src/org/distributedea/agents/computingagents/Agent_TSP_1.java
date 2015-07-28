package org.distributedea.agents.computingagents;

import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problem.ProblemTSP;


public class Agent_TSP_1  extends Agent_TSP {

	private static final long serialVersionUID = 1L;

	@Override
	public void prepareToDie() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void startComputing(Problem problem) {
		
		ProblemTSP problemTSP = null;
		
		if (problem instanceof ProblemTSP) {
			
			problemTSP = (ProblemTSP) problem;
		} else {
			logException(
					"Recieved problem is not TSP Problem",
					new IllegalStateException());
		}
		
		IndividualPermutation individual = generateIndividual(problemTSP);
		
		long generation = 0;
		
		while (true) {
			logInfo("Generation: " + generation);
			
			double fitness = fitness(individual, problemTSP);
			logInfo("Finess: " + fitness);
			
			IndividualPermutation individualNew = mutation(individual);
			double fitnessNew = fitness(individualNew, problemTSP);
			
			if (fitnessNew < fitness) {
				logInfo("JUMP");
				fitness = fitnessNew;
				individual = individualNew;
			}
			
			generation++;
		}
	}

}
