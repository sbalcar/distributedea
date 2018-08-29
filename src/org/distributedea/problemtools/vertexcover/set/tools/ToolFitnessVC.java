package org.distributedea.problemtools.vertexcover.set.tools;

import java.util.HashSet;
import java.util.Set;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetVertexCover;
import org.distributedea.ontology.dataset.vertexcover.Graph;
import org.distributedea.ontology.individuals.IndividualSet;
import org.distributedea.ontology.problem.ProblemVertexCover;

public class ToolFitnessVC {

	public static double evaluate(IndividualSet individual,
			ProblemVertexCover problem, DatasetVertexCover dataset,
			IAgentLogger logger) {
		
		Graph graph = dataset.getGraph();
		
		Set<Integer> coveredVertices = new HashSet<>();
		
		for (Integer vertexIdI : individual.getSet()) {
			coveredVertices.add(vertexIdI);
			coveredVertices.addAll(
					graph.exportNeighboursTo(vertexIdI));
		}
		
		if (coveredVertices.size() == graph.numberOfVertices()) {
			return individual.getSet().size();
		} else {
			return graph.numberOfVertices();
		}
	}
}
