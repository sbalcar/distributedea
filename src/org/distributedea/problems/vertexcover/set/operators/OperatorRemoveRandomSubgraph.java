package org.distributedea.problems.vertexcover.set.operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetVertexCover;
import org.distributedea.ontology.dataset.vertexcover.Graph;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualSet;
import org.distributedea.ontology.problem.ProblemVertexCover;

public class OperatorRemoveRandomSubgraph {

	private static int SIZE_OF_SUBGRAPH = 5;
	
	public static Individual create(IndividualSet individualSet,
			ProblemVertexCover problemVC, DatasetVertexCover datasetVC,
			IAgentLogger logger) {

		return create(individualSet, problemVC, datasetVC,
				SIZE_OF_SUBGRAPH, logger);
	}
	
	protected static Individual create(IndividualSet individualSet,
			ProblemVertexCover problemVC, DatasetVertexCover datasetVC,
			int size, IAgentLogger logger) {

		List<Integer> cover = individualSet.getSet();
		Graph graph = datasetVC.getGraph();
		
		List<Integer> coverNew = new ArrayList<>(cover);
		
		Random rnd = new Random();
		
		List<Integer> subgraph = new ArrayList<Integer>();
		for (int i = 0; i < SIZE_OF_SUBGRAPH; i++) {
			int index = rnd.nextInt(cover.size());
			subgraph.add(cover.get(index));
			coverNew.remove(new Integer(cover.get(index)));
		}
		
		List<Integer> neighbours = graph.exportNeighboursFrom(subgraph);
		Collections.shuffle(neighbours);
		
		List<Integer> candidates = new ArrayList<>();
		candidates.addAll(neighbours);
		candidates.addAll(subgraph);
		
		Set<Integer> correctNew = graph.correctVertexCover(
				new HashSet<Integer>(coverNew), candidates);
				
		return new IndividualSet(correctNew);
	}
}
