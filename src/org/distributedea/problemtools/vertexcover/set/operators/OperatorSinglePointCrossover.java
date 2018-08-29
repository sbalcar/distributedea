package org.distributedea.problemtools.vertexcover.set.operators;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.distributedea.ontology.dataset.DatasetVertexCover;
import org.distributedea.ontology.dataset.vertexcover.Graph;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualSet;
import org.distributedea.ontology.problem.ProblemVertexCover;

public class OperatorSinglePointCrossover {

	public static Individual[] crossover(IndividualSet individual1,
			IndividualSet individual2, ProblemVertexCover problemVC,
			DatasetVertexCover datasetVC) {
		
		Graph graph = datasetVC.getGraph();
		List<Integer> vertices = graph.exportVertices();
		
		Collections.sort(vertices);
		int medianVertexID = vertices.get(vertices.size() / 2);
		
		
		Set<Integer> coverNew = new HashSet<>();
		
		for (int idI : individual1.getSet()) {
			if (idI <= medianVertexID) {
				coverNew.add(idI);
			}
		}
		for (int idI : individual2.getSet()) {
			if (idI >= medianVertexID) {
				coverNew.add(idI);
			}
		}
		
		Set<Integer> correctedCover = graph.correctVertexCover(coverNew,
				graph.exportVerticesInRandomOrder());
				
		Individual[] result = new Individual[2];
		result[0] = new IndividualSet(correctedCover);
		result[1] = individual1;
		
		return result;
	}
}
