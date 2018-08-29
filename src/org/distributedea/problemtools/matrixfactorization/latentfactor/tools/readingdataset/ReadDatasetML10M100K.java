package org.distributedea.problemtools.matrixfactorization.latentfactor.tools.readingdataset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.distributedea.ontology.dataset.matrixfactorization.content.MovieContent;
import org.distributedea.ontology.dataset.matrixfactorization.content.IItemContent;

/**
 * Reader for dataset ML-10M
 * @author stepan
 *
 */
public class ReadDatasetML10M100K implements IReadDatasetML {

	private String DELIMITER = "::";
	private String DELIMITER_OF_GENRES = "\\|";
	
	private boolean DEBUG = false;
	
	private String ACTION = "Action";
	private String ADVENTURE = "Adventure";
	private String ANIMATION = "Animation";
	private String CHILDRENS = "Children";  // no "Children's"
	private String COMEDY = "Comedy";
	private String CRIME = "Crime";
	private String DOCUMENTARY = "Documentary";
	private String DRAMA = "Drama";
	private String FANTASY = "Fantasy";
	private String FILMNOIR = "Film-Noir";
	private String HOROR = "Horror";
	private String MUSICAL = "Musical";
	private String MYSTERY = "Mystery";
	private String ROMANCE = "Romance";
	private String SCIFI = "Sci-Fi";
	private String TRILLER = "Thriller";
	private String WAR = "War";
	private String WESTERN = "Western";
	@SuppressWarnings("unused")
	private String IMAX = "IMAX"; // added
	
	public IItemContent parseFilmContentLine(String line) {
		
		// 1::Toy Story (1995)::Adventure|Animation|Children|Comedy|Fantasy
		
		MovieContent content = new MovieContent();
		
		String[] tokensInArray = line.split(DELIMITER);
		List<String> tokens = new ArrayList<String>(Arrays.asList(tokensInArray));

		
		int movieID = Integer.parseInt(tokens.remove(0));
		content.setMovieId(movieID);
		
		String title = tokens.remove(0);
		String genres = tokens.remove(0);
		
		if (DEBUG) {
			System.out.println("movieID: " + movieID);
			System.out.println("title: " + title);
			System.out.println("genres: " + genres);
		}

		String[] tokensGenresInArray = genres.split(DELIMITER_OF_GENRES);
		List<String> tokensGenres = new ArrayList<String>(Arrays.asList(tokensGenresInArray));
		
		while (! tokensGenres.isEmpty()) {
			
			String genreI = tokensGenres.remove(0);
			
			if (genreI.equals(ACTION)) {
				content.setAction(true);
				
			} else if (genreI.equals(ADVENTURE)) {
				content.setAdventure(true);
				
			} else if (genreI.equals(ANIMATION)) {
				content.setAnimation(true);
				
			} else if (genreI.equals(CHILDRENS)) {
				content.setChildrens(true);
				
			} else if (genreI.equals(COMEDY)) {
				content.setComedy(true);
				
			} else if (genreI.equals(CRIME)) {
				content.setCrime(true);
				
			} else if (genreI.equals(DOCUMENTARY)) {
				content.setDocumentary(true);
				
			} else if (genreI.equals(DRAMA)) {
				content.setDrama(true);
				
			} else if (genreI.equals(FANTASY)) {
				content.setFantasy(true);
				
			} else if (genreI.equals(FILMNOIR)) {
				content.setFilmNoir(true);
				
			} else if (genreI.equals(HOROR)) {
				content.setHorror(true);
				
			} else if (genreI.equals(MUSICAL)) {
				content.setMusical(true);
				
			} else if (genreI.equals(MYSTERY)) {
				content.setMystery(true);
				
			} else if (genreI.equals(ROMANCE)) {
				content.setRomance(true);
				
			} else if (genreI.equals(SCIFI)) {
				content.setSciFi(true);
				
			} else if (genreI.equals(TRILLER)) {
				content.setThriller(true);
				
			} else if (genreI.equals(WAR)) {
				content.setWar(true);
				
			} else if (genreI.equals(WESTERN)) {
				content.setWestern(true);
			}
		}
		
		return content;
	}
	
}
