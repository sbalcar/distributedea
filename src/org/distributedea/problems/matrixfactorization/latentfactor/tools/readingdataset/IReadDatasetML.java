package org.distributedea.problems.matrixfactorization.latentfactor.tools.readingdataset;

import org.distributedea.ontology.dataset.matrixfactorization.content.IItemContent;

public interface IReadDatasetML {

	public IItemContent parseFilmContentLine(String line);
	
}
