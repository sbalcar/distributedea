package org.distributedea.problemtools.matrixfactorization.latentfactor.operators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.distributedea.logging.IAgentLogger;
import org.distributedea.ontology.dataset.DatasetMF;
import org.distributedea.ontology.dataset.matrixfactorization.RatingModel;
import org.distributedea.ontology.dataset.matrixfactorization.content.ContentMLMoviesModel;
import org.distributedea.ontology.dataset.matrixfactorization.content.MovieContent;
import org.distributedea.ontology.dataset.matrixfactorization.content.MovieContents;
import org.distributedea.ontology.dataset.matrixfactorization.objectrating.ObjectRating;
import org.distributedea.ontology.dataset.matrixfactorization.objectrating.ObjectRatingList;
import org.distributedea.ontology.individuals.IndividualLatentFactors;
import org.distributedea.ontology.individuals.latentfactors.LatentFactor;
import org.distributedea.ontology.individuals.latentfactors.LatentFactorVector;
import org.distributedea.ontology.problem.ProblemMatrixFactorization;

/**
 * Operator combines two randomly selected movie-vectors which are same genre.  
 * @author stepan
 *
 */
public class OperatorAdulterateOfLatentVectorBySameGenreLatentVector {

	private static double ALFA = 0.01;
	private static Random randomGenerator = new Random();
	
	public static IndividualLatentFactors improve(
			IndividualLatentFactors individual,
			ProblemMatrixFactorization problemMF, DatasetMF datasetMF,
			IAgentLogger logger) throws Exception {
		
		
		RatingModel ratingModel = datasetMF.exportTrainingRatingModel();
		ObjectRatingList objRatList = ratingModel.exportObjectRaitingList();

		// random selected rating
		ObjectRating objectRatingRnd = objRatList.exportRandomObjectRaiting();
		int movieID = objectRatingRnd.getItemID();
		
		ContentMLMoviesModel moviesModel = (ContentMLMoviesModel)
				datasetMF.exportItemContentModel();
		MovieContent movieContentSelected =
				moviesModel.exportMovieContent(movieID);
		
		MovieContent imovieContentSimilar =
				selectSimilarMovie(movieContentSelected, moviesModel);

		//movieContentSelected.print();
		//imovieContentSimilar.print();
		

		return adulterate(individual, movieContentSelected, imovieContentSimilar, problemMF, datasetMF);
	}
	
	private static MovieContent selectSimilarMovie(MovieContent movieConten, ContentMLMoviesModel moviesModel) {
		
		List<MovieContents> models = new ArrayList<>();
		
		if (movieConten.isUnknown()) {
			models.add(moviesModel.exportMoviesGenreUnknow());
		}
		if (movieConten.isAction()) {
			models.add(moviesModel.exportMoviesGenreAction());
		}
		if (movieConten.isAdventure()) {
			models.add(moviesModel.exportMoviesGenreAdventure());
		}
		if (movieConten.isAnimation()) {
			models.add(moviesModel.exportMoviesGenreAnimation());
		}
		if (movieConten.isChildrens()) {
			models.add(moviesModel.exportMoviesGenreChildrens());
		}
		if (movieConten.isComedy()) {
			models.add(moviesModel.exportMoviesGenreComedy());
		}
		if (movieConten.isCrime()) {
			models.add(moviesModel.exportMoviesGenreCrime());
		}
		if (movieConten.isDocumentary()) {
			models.add(moviesModel.exportMoviesGenreDocumentary());
		}
		if (movieConten.isDrama()) {
			models.add(moviesModel.exportMoviesGenreDrama());
		}
		if (movieConten.isFantasy()) {
			models.add(moviesModel.exportMoviesGenreFantasy());
		}
		if (movieConten.isFilmNoir()) {
			models.add(moviesModel.exportMoviesGenreFilmNoir());
		}
		if (movieConten.isHorror()) {
			models.add(moviesModel.exportMoviesGenreHorror());
		}
		if (movieConten.isMusical()) {
			models.add(moviesModel.exportMoviesGenreMusical());
		}
		if (movieConten.isMystery()) {
			models.add(moviesModel.exportMoviesGenreMystery());
		}
		if (movieConten.isRomance()) {
			models.add(moviesModel.exportMoviesGenreRomance());
		}
		if (movieConten.isSciFi()) {
			models.add(moviesModel.exportMoviesGenreSciFi());
		}
		if (movieConten.isThriller()) {
			models.add(moviesModel.exportMoviesGenreThriller());
		}
		if (movieConten.isWar()) {
			models.add(moviesModel.exportMoviesGenreWar());
		}
		if (movieConten.isWestern()) {
			models.add(moviesModel.exportMoviesGenreWestern());
		}

		List<MovieContent> movies = new ArrayList<MovieContent>();
		for (MovieContents modelI : models) {
			movies.addAll(modelI.getMovieContents());
		}
		
		int index = randomGenerator.nextInt(movies.size());
		
		return movies.get(index);
	}
	
	private static IndividualLatentFactors adulterate(IndividualLatentFactors individual, MovieContent movieContentSelected,
			MovieContent imovieContentSimilar, ProblemMatrixFactorization problemMF, DatasetMF datasetMF) {

		IndividualLatentFactors idividualClone =
				(IndividualLatentFactors) individual.deepClone();

		int movieSelcID = movieContentSelected.getMovieId();
		int movieSimlID = imovieContentSimilar.getMovieId();
		
		RatingModel model = datasetMF.exportTrainingRatingModel();
		int movieSelcIndex = model.exportIndexOfItem(movieSelcID);
		int movieSimlIndex = model.exportIndexOfItem(movieSimlID);
		
		LatentFactor latentFactorMovies = idividualClone.getLatentFactorX();
		LatentFactorVector latVectSelc =
				latentFactorMovies.exportLatentFactorVector(movieSelcIndex);
		LatentFactorVector latVectSiml =
				latentFactorMovies.exportLatentFactorVector(movieSimlIndex);
			
		LatentFactorVector latVectorStep =
				latVectSiml.multiply(ALFA * randomGenerator.nextDouble());
		
		LatentFactorVector newLatVector = latVectSelc.deepClone();
		
		if (randomGenerator.nextBoolean()) {
			newLatVector = newLatVector.minus(latVectorStep);
		} else {
			newLatVector = newLatVector.plus(latVectorStep);
		}
		
		latentFactorMovies.importLatentFactorVector(movieSelcIndex, newLatVector);
		
		return idividualClone;
	}
	

}
