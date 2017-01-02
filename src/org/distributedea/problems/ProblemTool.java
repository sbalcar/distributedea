package org.distributedea.problems;

import org.distributedea.agents.systemagents.centralmanager.structures.pedigree.PedigreeParameters;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.pedigree.Pedigree;
import org.distributedea.ontology.problem.Problem;
import org.distributedea.ontology.problemdefinition.IProblemDefinition;

/**
 * Abstract class for {@link ProblemTool}
 * @author stepan
 *
 */
public abstract class ProblemTool implements IProblemTool {

	
	protected abstract Individual generateIndividual(IProblemDefinition problemDef,
			Problem problem, IAgentLogger logger);
	
	protected abstract Individual generateFirstIndividual(IProblemDefinition problemDef,
			Problem problem, IAgentLogger logger);
	
	protected abstract Individual generateNextIndividual(IProblemDefinition problemDef,
			Problem problem, Individual individual, IAgentLogger logger);
	
	protected abstract Individual improveIndividual(Individual individual,
			IProblemDefinition problemDef, IAgentLogger logger) throws Exception;

	protected abstract Individual getNeighbor(Individual individual,
			IProblemDefinition problemDef, Problem problem, long neighborIndex,
			IAgentLogger logger) throws Exception;
	
	protected abstract Individual[] createNewIndividual(Individual individual1,
			Individual individual2, IProblemDefinition problemDef,
			Problem problem, IAgentLogger logger)
			throws Exception;
	
	protected abstract Individual[] createNewIndividual(Individual individual1,
			Individual individual2, Individual individual3,
			IProblemDefinition problemDef, Problem problem, IAgentLogger logger)
			throws Exception;
	
	
	
	public final IndividualEvaluated generateIndividualEval(
			IProblemDefinition problemDef, Problem problem,
			PedigreeParameters pedigreeParams, IAgentLogger logger) {
		
		Individual individualNew = generateIndividual(problemDef, problem, logger);
		double fitness = fitness(individualNew, problemDef, problem, logger);
		
		Pedigree pedigree = Pedigree.create(pedigreeParams);
		
		return new IndividualEvaluated(individualNew, fitness, pedigree);
	}
	
	public final IndividualEvaluated generateFirstIndividualEval(
			IProblemDefinition problemDef, Problem problem,
			PedigreeParameters pedigreeParams, IAgentLogger logger) {
		
		Individual individualNew = generateFirstIndividual(problemDef, problem, logger);
		double fitness = fitness(individualNew, problemDef, problem, logger);
		
		Pedigree pedigree = Pedigree.create(pedigreeParams);
		
		return new IndividualEvaluated(individualNew, fitness, pedigree);
	}
	
	public final IndividualEvaluated generateNextIndividualEval(
			IProblemDefinition problemDef, Problem problem,
			IndividualEvaluated individual, PedigreeParameters pedigreeParams,
			IAgentLogger logger) {
		
		Individual individualNew = generateNextIndividual(problemDef, problem, individual.getIndividual(), logger);
		double fitness = fitness(individualNew, problemDef, problem, logger);
		
		Pedigree pedigree = Pedigree.create(pedigreeParams);
		
		return new IndividualEvaluated(individualNew, fitness, pedigree);
	}
	
	public final IndividualEvaluated getNeighborEval(IndividualEvaluated individual,
			IProblemDefinition problemDef, Problem problem, long neighborIndex,
			PedigreeParameters pedigreeParams, IAgentLogger logger) throws Exception {
		
		Individual individualNew = getNeighbor(individual.getIndividual(),
				problemDef, problem, neighborIndex, logger);
		double fitness = fitness(individualNew, problemDef, problem, logger);
		
		Pedigree pedigree = Pedigree.create(pedigreeParams);
		
		return new IndividualEvaluated(individualNew, fitness, pedigree);
	}
	
	public final IndividualEvaluated[] createNewIndividual(IndividualEvaluated individualEval1,
			IndividualEvaluated individualEval2, IProblemDefinition problemDef,
			Problem problem, PedigreeParameters pedigreeParams, IAgentLogger logger)
			throws Exception {
		
		Individual individual1 = individualEval1.getIndividual();
		Pedigree pedigree1 = individualEval1.getPedigree();
				
		Individual individual2 = individualEval2.getIndividual();
		Pedigree pedigree2 = individualEval2.getPedigree();
		
		
		Individual[] individualNew = createNewIndividual(individual1,
				individual2, problemDef, problem, logger);
		Pedigree pedigreeNew = Pedigree.update(pedigree1, pedigree2, pedigreeParams);
		
		
		IndividualEvaluated[] list = new IndividualEvaluated[individualNew.length];
		for (int i = 0; i < individualNew.length; i++) {
			
			Individual individualNewI = individualNew[i];
			double fitnessI = fitness(individualNewI, problemDef, problem, logger);
			list[i] = new IndividualEvaluated(individualNewI, fitnessI, pedigreeNew);
		}
		return list;
	}
			
	public final IndividualEvaluated[] createNewIndividualEval(IndividualEvaluated individualEval1, 
			IndividualEvaluated individualEval2, IndividualEvaluated individualEval3,
			IProblemDefinition problemDef, Problem problem, PedigreeParameters
			pedigreeParams, IAgentLogger logger) throws Exception {
		
		Individual individual1 = individualEval1.getIndividual();
		Pedigree pedigree1 = individualEval1.getPedigree();
		
		Individual individual2 = individualEval2.getIndividual();
		Pedigree pedigree2 = individualEval2.getPedigree();
		
		Individual individual3 = individualEval3.getIndividual();
		Pedigree pedigree3 = individualEval3.getPedigree();
		
		
		Individual[] individualNew = createNewIndividual(individual1, 
				individual2, individual3, problemDef, problem, logger);
		Pedigree pedigreeNew = Pedigree.update(pedigree1, pedigree2, pedigree3,
				pedigreeParams);
		
		IndividualEvaluated[] list = new IndividualEvaluated[individualNew.length];
		for (int i = 0; i < individualNew.length; i++) {
			
			Individual individualNewI = individualNew[i];
			double fitnessI = fitness(individualNewI, problemDef, problem, logger);
			list[i] = new IndividualEvaluated(individualNewI, fitnessI, pedigreeNew);
		}
		return list;
	}
	
	
	public final IndividualEvaluated improveIndividualEval(IndividualEvaluated individualEval1,
			IProblemDefinition problemDef, Problem problem, PedigreeParameters pedigreeParams,
			IAgentLogger logger) throws Exception {
		
		Individual individual1 = individualEval1.getIndividual();
		Pedigree pedigree1 = individualEval1.getPedigree();
		
		Individual individualNew = improveIndividual(individual1, problemDef, logger);
		double fitness = fitness(individualNew, problemDef, problem, logger);
		
		Pedigree pedigreeNew = Pedigree.update(pedigree1, pedigreeParams);
		
		return new IndividualEvaluated(individualNew, fitness, pedigreeNew);
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
