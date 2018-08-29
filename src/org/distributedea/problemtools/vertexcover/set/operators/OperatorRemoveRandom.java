package org.distributedea.problemtools.vertexcover.set.operators;

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

public class OperatorRemoveRandom {

	public static Individual create(IndividualSet individualSet,
			ProblemVertexCover problemVC, DatasetVertexCover datasetVC,
			IAgentLogger logger) {
		
		List<Integer> cover = individualSet.getSet();
		Graph graph = datasetVC.getGraph();
		
		Random rnd = new Random();
		int index = rnd.nextInt(cover.size());
		int randomVertexID = cover.get(index);
		
		List<Integer> coverNew = new ArrayList<>(cover);
		coverNew.remove(new Integer(randomVertexID));
		
		List<Integer> neighbours = graph.exportNeighboursFrom(randomVertexID);
		Collections.shuffle(neighbours);
		
		List<Integer> candidates = new ArrayList<>();
		candidates.addAll(neighbours);
		candidates.add(randomVertexID);
		
		Set<Integer> correctedNew = graph.correctVertexCover(
				new HashSet<Integer>(coverNew), candidates);
				
		return new IndividualSet(correctedNew);
	}
}
