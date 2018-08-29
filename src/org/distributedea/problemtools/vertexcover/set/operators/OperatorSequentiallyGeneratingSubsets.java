package org.distributedea.problemtools.vertexcover.set.operators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetVertexCover;
import org.distributedea.ontology.dataset.vertexcover.Graph;
import org.distributedea.ontology.individuals.Individual;
import org.distributedea.ontology.individuals.IndividualSet;
import org.distributedea.ontology.problem.ProblemVertexCover;

public class OperatorSequentiallyGeneratingSubsets {

	private List<Integer> coverLast;
	
	public Individual generateNextIndividual(IndividualSet individualVC,
			ProblemVertexCover problemVC, DatasetVertexCover datasetVC,
			IAgentLogger logger) {
		
		Graph graph = datasetVC.getGraph();
		
		List<Integer> coverNew;
		
		if (coverLast == null) {
			
			List<Integer> vertices = graph.exportVertices();
			int min = Collections.min(vertices);
			
			coverNew = Arrays.asList(min);
		
		} else {

			Collections.sort(coverLast);
			
			List<Integer> vertices = graph.exportVertices();
			Collections.sort(vertices);
			
			List<Integer> lastK = vertices.subList(
					vertices.size() - coverLast.size(), vertices.size());
			
			List<Integer> coverWithoutLastK = new ArrayList<>(coverLast);
			coverWithoutLastK.removeAll(lastK);
			
			if (coverWithoutLastK.isEmpty()) {
				int min = Collections.min(vertices);
				
				coverNew = new ArrayList<>(coverLast);
				coverNew.add(new Integer(min));
				
			} else {
				int candidate = Collections.max(coverWithoutLastK);
				
				coverNew = new ArrayList<>(coverLast);
				coverNew.remove(new Integer(candidate));
				coverNew.add(new Integer(candidate +1));
				
			}
		}
	
		this.coverLast = coverNew;
		
		Set<Integer> correctedCover = graph.correctVertexCover(
				new HashSet<>(coverNew), graph.exportVerticesInRandomOrder());
		
		return new IndividualSet(correctedCover);
	}
}
