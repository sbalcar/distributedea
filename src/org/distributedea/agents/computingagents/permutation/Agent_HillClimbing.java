package org.distributedea.agents.computingagents.permutation;

import java.util.logging.Level;

import org.distributedea.agents.computingagents.Agent_CompPermutation;
import org.distributedea.ontology.individuals.IndividualPermutation;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problem.ProblemTSP;
import org.distributedea.problems.ProblemTool;


public class Agent_HillClimbing  extends Agent_CompPermutation {

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
			logger.logThrowable(
					"Recieved problem is not TSP Problem",
					new IllegalStateException());
		}
		
		ProblemTool problemTool = instanceProblemTool(
				problemTSP.getProblemToolClass());
		
		IndividualPermutation individual = (IndividualPermutation)
				problemTool.generateIndividual(problemTSP, logger);
		
		long generation = 0;
		
		while (true) {
			logger.log(Level.INFO, "Generation: " + generation);
			
			double fitness =
					problemTool.fitness(individual, problemTSP, logger);
			logger.log(Level.INFO, "Finess: " + fitness);
			
			IndividualPermutation individualNew = (IndividualPermutation)
					problemTool.improveIndividual(individual, problemTSP, logger);
			
			double fitnessNew =
					problemTool.fitness(individualNew, problemTSP, logger);
			
			if (fitnessNew < fitness) {
				logger.log(Level.INFO, "JUMP");
				fitness = fitnessNew;
				individual = individualNew;
			}
			
			generation++;
		}
	}

}
