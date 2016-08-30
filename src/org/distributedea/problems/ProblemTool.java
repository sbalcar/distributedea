package org.distributedea.problems;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.problem.Problem;

/**
 * Abstract class for {@link ProblemTool}
 * @author stepan
 *
 */
public abstract class ProblemTool implements IProblemTool {

	public IndividualEvaluated generateIndividualEval(Problem problem, IAgentLogger logger) {
		
		Individual individualNew = generateIndividual(problem, logger);
		double fitness = fitness(individualNew, problem, logger);
		
		return new IndividualEvaluated(individualNew, fitness);
	}
	
	public IndividualEvaluated generateFirstIndividualEval(Problem problem, IAgentLogger logger) {
		
		Individual individualNew = generateFirstIndividual(problem, logger);
		double fitness = fitness(individualNew, problem, logger);
		
		return new IndividualEvaluated(individualNew, fitness);
	}
	
	public IndividualEvaluated generateNextIndividualEval(Problem problem,
			Individual individual, IAgentLogger logger) {
		
		Individual individualNew = generateNextIndividual(problem, individual, logger);
		double fitness = fitness(individualNew, problem, logger);
		
		return new IndividualEvaluated(individualNew, fitness);
	}
	
	public IndividualEvaluated getNeighborEval(Individual individual, Problem problem,
			long neighborIndex, IAgentLogger logger) throws ProblemToolException {
		
		Individual individualNew = getNeighbor(individual, problem,
				neighborIndex, logger);
		double fitness = fitness(individualNew, problem, logger);
		
		return new IndividualEvaluated(individualNew, fitness);
	}
	
	
	public IndividualEvaluated[] createNewIndividualEval(Individual individual1, 
			Individual individual2, Individual individual3,
			Problem problem, IAgentLogger logger)
			throws ProblemToolException {
		
		Individual[] individualNew = createNewIndividual(individual1, 
				individual2, individual3, problem, logger);
		
		IndividualEvaluated[] list = new IndividualEvaluated[individualNew.length];
		for (int i = 0; i < individualNew.length; i++) {
			
			Individual individualNewI = individualNew[i];
			double fitnessI = fitness(individualNewI, problem, logger);
			list[i] = new IndividualEvaluated(individualNewI, fitnessI);
		}
		return list;
	}
	
	
	public IndividualEvaluated improveIndividualEval(Individual individual, Problem problem,
			IAgentLogger logger) throws ProblemToolException {
		
		Individual individualNew = improveIndividual(individual, problem, logger);
		double fitness = fitness(individualNew, problem, logger);
		
		return new IndividualEvaluated(individualNew, fitness);
	}
	
	/**
	 * Creates instance of {@link ProblemTool} from class
	 * @param className
	 * @param logger
	 * @return
	 */
	public static IProblemTool createInstanceOfProblemTool(Class<?> toolClass,
			IAgentLogger logger) {
		
		IProblemTool problemTool = null;
		try {
			problemTool = (IProblemTool) toolClass.newInstance();
		} catch (InstantiationException e) {
			logger.logThrowable("Class of " +
					ProblemTool.class.getSimpleName() +
					" can't be instanced", e);
		} catch (IllegalAccessException e) {
			logger.logThrowable("Class of " +
					ProblemTool.class.getSimpleName() +
					" can't be instanced", e);
		}

		return problemTool;
	}
}
