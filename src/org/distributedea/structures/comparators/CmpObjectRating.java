package org.distributedea.structures.comparators;

import java.util.Comparator;

import org.distributedea.ontology.dataset.matrixfactorization.objectrating.ObjectRating;

public class CmpObjectRating implements Comparator<ObjectRating> {

	@Override
	public int compare(ObjectRating oRating0, ObjectRating oRating1) {

		double rating0 = oRating0.getRaiting();
		double rating1 = oRating1.getRaiting();
		
		if (rating0 == rating1) {
			return 0;
		}
		
		if (rating0 < rating1) {
			return -1;
		} else {
			return 1;
		}
	}

}
