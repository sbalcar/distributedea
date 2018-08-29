package org.distributedea.problemtools.vertexcover.set.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.distributedea.javaextension.Pair;
import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetVertexCover;
import org.distributedea.ontology.dataset.vertexcover.Graph;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualSet;
import org.distributedea.ontology.problem.ProblemVertexCover;

public class ToolGenerateIndividualVC {

	public static Individual generateIndividual(ProblemVertexCover problemVC,
			DatasetVertexCover datasetVC, IAgentLogger logger) {
		
		Graph graph = datasetVC.getGraph();

		List<Integer> vertices = graph.exportVertices();
		Collections.shuffle(vertices);
		
		Set<Integer> vertexCover =
			graph.correctVertexCover(new HashSet<Integer>(), vertices);
		
		return new IndividualSet(vertexCover);
	}
	
	public static Individual generateIndividual_(ProblemVertexCover problemVC,
			DatasetVertexCover datasetVC, IAgentLogger logger) {
		
		Graph graph = datasetVC.getGraph();

		List<Pair<Integer, Integer>> edges = graph.exportEdges();		
		Collections.shuffle(edges);

		List<Integer> vertices = graph.exportVertices();
		List<Integer> cover = new ArrayList<>();
				
		int i = 0;
		while (! vertices.isEmpty()) {
			
			Pair<Integer, Integer> edgeI = edges.get(i);
			cover.add(edgeI.first);
			cover.add(edgeI.second);
			
			vertices.remove(edgeI.first);
			vertices.remove(edgeI.second);
			
			List<Integer> neighbours1 =
					graph.exportVertex(edgeI.first).getEdges();
			List<Integer> neighbours2 =
					graph.exportVertex(edgeI.second).getEdges();
			
			vertices.removeAll(neighbours1);
			vertices.removeAll(neighbours2);
			
			i++;
		}
		
		return new IndividualSet(
				new ArrayList<Integer>(new LinkedHashSet<Integer>(cover)) );
	}
}
