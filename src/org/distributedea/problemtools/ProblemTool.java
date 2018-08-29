package org.distributedea.problemtools;

import org.distributedea.agents.systemagents.centralmanager.structures.pedigree.PedigreeParameters;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.Dataset;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individualwrapper.IndividualEvaluated;
import org.distributedea.ontology.pedigree.Pedigree;
import org.distributedea.ontology.pedigreedefinition.PedigreeDefinition;
import org.distributedea.ontology.problem.IProblem;

/**
 * Abstract class for {@link ProblemTool}
 * @author stepan
 *
 */
public abstract class ProblemTool implements IProblemTool {

	
	protected abstract Individual generateIndividual(IProblem problem,
			Dataset dataset, IAgentLogger logger);
	
	protected abstract Individual generateFirstIndividual(IProblem problem,
			Dataset dataset, IAgentLogger logger);
	
	protected abstract Individual generateNextIndividual(IProblem problem,
			Dataset dataset, Individual individual, long neighborIndex,
			IAgentLogger logger);
	
	protected abstract Individual improveIndividual(Individual individual,
			IProblem problem, Dataset dataset, IAgentLogger logger) throws Exception;

	protected abstract Individual getNeighbor(Individual individual,
			IProblem problem, Dataset dataset, long neighborIndex,
			IAgentLogger logger) throws Exception;
	
	protected abstract Individual[] createNewIndividual(Individual individual1,
			Individual individual2, IProblem problem,
			Dataset dataset, IAgentLogger logger)
			throws Exception;
	
	protected abstract Individual createNewIndividual(Individual individual1,
			Individual individual2, Individual individual3,
			IProblem problem, Dataset dataset, IAgentLogger logger)
			throws Exception;
	
	
	public final IndividualEvaluated generateIndividualEval(
			IProblem problem, Dataset dataset,
			PedigreeParameters pedigreeParams, IAgentLogger logger) {
		
		Individual individualNew = generateIndividual(problem, dataset, logger);
		double fitness = fitness(individualNew, problem, dataset, logger);
		
		PedigreeDefinition pedigreeDef = pedigreeParams.pedigreeDefinition;
		Pedigree pedigree = pedigreeDef.create(pedigreeParams, logger);
		
		return new IndividualEvaluated(individualNew, fitness, pedigree);
	}
	
	public final IndividualEvaluated generateFirstIndividualEval(
			IProblem problem, Dataset dataset,
			PedigreeParameters pedigreeParams, IAgentLogger logger) {
		
		Individual individualNew = generateFirstIndividual(problem, dataset, logger);
		double fitness = fitness(individualNew, problem, dataset, logger);
		
		PedigreeDefinition pedigreeDef = pedigreeParams.pedigreeDefinition;
		Pedigree pedigree = pedigreeDef.create(pedigreeParams, logger);
		
		return new IndividualEvaluated(individualNew, fitness, pedigree);
	}
	
	public final IndividualEvaluated generateNextIndividualEval(
			IProblem problem, Dataset dataset,
			IndividualEvaluated individualEval, long neighborIndex,
			PedigreeParameters pedigreeParams, IAgentLogger logger) {
		
		Individual individual = individualEval.getIndividual();
		Pedigree pedigree = individualEval.getPedigree();
		
		Individual individualNew = generateNextIndividual(problem, dataset,
				individual, neighborIndex, logger);
		double fitness = fitness(individualNew, problem, dataset, logger);

		PedigreeDefinition pedigreeDef = pedigreeParams.pedigreeDefinition;
		Pedigree pedigreeNew = pedigreeDef.create(pedigree, pedigreeParams, logger);
		
		return new IndividualEvaluated(individualNew, fitness, pedigreeNew);
	}
	
	public final IndividualEvaluated getNeighborEval(IndividualEvaluated individualEval,
			IProblem problem, Dataset dataset, long neighborIndex,
			PedigreeParameters pedigreeParams, IAgentLogger logger) throws Exception {
		
		Individual individual = individualEval.getIndividual();
		Pedigree pedigree = individualEval.getPedigree();
		
		Individual individualNew = getNeighbor(individual, problem, dataset,
				neighborIndex, logger);
		double fitness = fitness(individualNew, problem, dataset, logger);

		PedigreeDefinition pedigreeDef = pedigreeParams.pedigreeDefinition;
		Pedigree pedigreeNew = pedigreeDef.create(pedigree, pedigreeParams, logger);
		
		return new IndividualEvaluated(individualNew, fitness, pedigreeNew);
	}
	
	public final IndividualEvaluated[] createNewIndividual(IndividualEvaluated individualEval1,
			IndividualEvaluated individualEval2, IProblem problem,
			Dataset dataset, PedigreeParameters pedigreeParams, IAgentLogger logger)
			throws Exception {
		
		Individual individual1 = individualEval1.getIndividual();
		Pedigree pedigree1 = individualEval1.getPedigree();
				
		Individual individual2 = individualEval2.getIndividual();
		Pedigree pedigree2 = individualEval2.getPedigree();
		
		Individual[] individualNew = createNewIndividual(individual1,
				individual2, problem, dataset, logger);

		PedigreeDefinition pedigreeDef = pedigreeParams.pedigreeDefinition;
		Pedigree pedigreeNew = pedigreeDef.create(pedigree1, pedigree2, pedigreeParams, logger);
				
		IndividualEvaluated[] list = new IndividualEvaluated[individualNew.length];
		for (int i = 0; i < individualNew.length; i++) {
			
			Individual individualNewI = individualNew[i];
			double fitnessI = fitness(individualNewI, problem, dataset, logger);
			list[i] = new IndividualEvaluated(individualNewI, fitnessI, pedigreeNew);
		}
		return list;
	}
			
	public final IndividualEvaluated createNewIndividualEval(IndividualEvaluated individualEval1, 
			IndividualEvaluated individualEval2, IndividualEvaluated individualEval3,
			IProblem problem, Dataset dataset, PedigreeParameters
			pedigreeParams, IAgentLogger logger) throws Exception {
		
		Individual individual1 = individualEval1.getIndividual();
		Pedigree pedigree1 = individualEval1.getPedigree();
		
		Individual individual2 = individualEval2.getIndividual();
		Pedigree pedigree2 = individualEval2.getPedigree();
		
		Individual individual3 = individualEval3.getIndividual();
		Pedigree pedigree3 = individualEval3.getPedigree();
		
		
		Individual individualNew = createNewIndividual(individual1, 
				individual2, individual3, problem, dataset, logger);
		
		PedigreeDefinition pedigreeDef = pedigreeParams.pedigreeDefinition;
		Pedigree pedigreeNew = pedigreeDef.create(pedigree1, pedigree2,
				pedigree3, pedigreeParams, logger);
		
		double fitness = fitness(individualNew, problem, dataset, logger);
		return new IndividualEvaluated(individualNew, fitness, pedigreeNew);

	}
	
	
	public final IndividualEvaluated improveIndividualEval(IndividualEvaluated individualEval1,
			IProblem problem, Dataset dataset, PedigreeParameters pedigreeParams,
			IAgentLogger logger) throws Exception {
		
		Individual individual1 = individualEval1.getIndividual();
		Pedigree pedigree1 = individualEval1.getPedigree();
		
		Individual individualNew = improveIndividual(individual1, problem, dataset, logger);
		double fitness = fitness(individualNew, problem, dataset, logger);
		
		PedigreeDefinition pedigreeDef = pedigreeParams.pedigreeDefinition;
		Pedigree pedigreeNew = pedigreeDef.create(pedigree1, pedigreeParams, logger);
		
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
