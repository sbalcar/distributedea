package org.distributedea.problemtools.vertexcover.set.operators;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetVertexCover;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualSet;
import org.distributedea.ontology.problem.ProblemVertexCover;

public class OperatorRemoveRandom3 extends OperatorRemoveRandomSubgraph {

	private static int SIZE_OF_SUBGRAPH = 3;
	
	public static Individual create(IndividualSet individualSet,
			ProblemVertexCover problemVC, DatasetVertexCover datasetVC,
			IAgentLogger logger) {

		return create(individualSet, problemVC, datasetVC,
				SIZE_OF_SUBGRAPH, logger);
	}

}
