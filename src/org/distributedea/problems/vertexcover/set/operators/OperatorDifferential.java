package org.distributedea.problems.vertexcover.set.operators;

import java.util.Set;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetVertexCover;
import org.distributedea.ontology.dataset.vertexcover.Graph;
import org.distributedea.ontology.individuals.IndividualSet;
import org.distributedea.ontology.problem.ProblemVertexCover;

public class OperatorDifferential {

	public static IndividualSet create(IndividualSet individualSet1,
			IndividualSet individualSet2, IndividualSet individualSet3,
			double differentialWeightF, ProblemVertexCover problemVC,
			DatasetVertexCover datasetVC, IAgentLogger logger) throws Exception {
		
		Graph graph = datasetVC.getGraph();
		
		Set<Integer> intersection23 = individualSet2.exportSet();
		intersection23.retainAll(individualSet3.exportSet());
		
		Set<Integer> correctedCover = graph.correctVertexCover(
				intersection23, individualSet1.getSet());
		
		return new IndividualSet(correctedCover);
	}
}
