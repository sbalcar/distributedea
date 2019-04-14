package org.distributedea.problems.matrixfactorization.latentfactor.tools.mahout.mahout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericPreference;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.Preference;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.distributedea.ontology.dataset.matrixfactorization.RatingModel;
import org.distributedea.ontology.dataset.matrixfactorization.objectrating.ObjectRating;
import org.distributedea.ontology.dataset.matrixfactorization.objectrating.ObjectRatingList;

public class ReadDatasetExample {

	public static RatingModel read(File dataset) throws IOException {
		
		// read model
		BufferedReader br = new BufferedReader(new FileReader(dataset));
		List<ObjectRating> list = new ArrayList<>();
		
		String line = br.readLine();
		while (line != null) {
			String[] tokens = line.split(",");
			int userID = Integer.parseInt(tokens[0]);
			int itemID = Integer.parseInt(tokens[1]);
			double raiting = Double.parseDouble(tokens[2]);
			
			ObjectRating oI = new ObjectRating(userID, itemID, raiting);
			list.add(oI);
			
			line = br.readLine();
		}
		br.close();

		return new RatingModel(list);
	}
	
	public static DataModel convertToDataModel(RatingModel rModel) {
		
		FastByIDMap<PreferenceArray> userMap = new FastByIDMap<>();
		
		for (int userIdI : rModel.exportUserIDs()) {
			
			ObjectRatingList orListI = rModel.exportRaitingsOfUser(userIdI);
			List<Preference> pList = convertToListOfPreferences(orListI);
			
			GenericUserPreferenceArray gpArray = new GenericUserPreferenceArray(pList);
			userMap.put(userIdI, gpArray);
		}
		
		return new GenericDataModel(userMap);
	}
	
	private static List<Preference> convertToListOfPreferences(ObjectRatingList listOfUsers) {
		
		List<Preference> pList = new ArrayList<>();
		for (ObjectRating oRatingI : listOfUsers.getRaitings()) {
			
			Preference pI = new GenericPreference(oRatingI.getUserID(),
					oRatingI.getItemID(), (float) oRatingI.getRaiting());
			pList.add(pI);
		}
		
		return pList;
	}
}
