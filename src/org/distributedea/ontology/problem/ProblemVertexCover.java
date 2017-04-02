package org.distributedea.ontology.problem;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetVertexCover;

/**
 * Ontology for definition vertex cover problem
 * @author stepan
 *
 */
public class ProblemVertexCover extends AProblem {

	private static final long serialVersionUID = 1L;

	@Override
	public boolean exportIsMaximizationProblem() {
		return false;
	}

	@Override
	public Class<?> exportDatasetClass() {
		return DatasetVertexCover.class;
	}

	@Override
	public boolean valid(IAgentLogger logger) {
		return true;
	}

	@Override
	public AProblem deepClone() {
		return new ProblemVertexCover();
	}

}
