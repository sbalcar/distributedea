package org.distributedea.ontology.dataset.matrixfactorization;

import java.util.List;

import org.distributedea.ontology.dataset.matrixfactorization.content.IItemContent;

/**
 * Interface for Item content model for matrix factorization dataset
 * @author stepan
 *
 */
public interface IItemContentModel {

	public List<IItemContent> exportIItemContents();
}
