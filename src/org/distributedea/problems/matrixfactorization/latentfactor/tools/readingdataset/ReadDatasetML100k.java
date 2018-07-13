package org.distributedea.problems.matrixfactorization.latentfactor.tools.readingdataset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.distributedea.ontology.dataset.matrixfactorization.content.MovieContent;
import org.distributedea.ontology.dataset.matrixfactorization.content.IItemContent;

/**
 * Reader for dataset ML-100k
 * @author stepan
 *
 */
public class ReadDatasetML100k implements IReadDatasetML {

	private String DELIMITER = "\\|";
	
	private boolean DEBUG = false;
	
	public IItemContent parseFilmContentLine(String line) {
				
		// 1|Toy Story (1995)|01-Jan-1995||http://us.imdb.com/M/title-exact?Toy%20Story%20(1995)|0|0|0|1|1|1|0|0|0|0|0|0|0|0|0|0|0|0|0
		// unknown | Action | Adventure | Animation | Children's | Comedy | Crime | Documentary | Drama | Fantasy | Film-Noir | Horror | Musical | Mystery | Romance | Sci-Fi | Thriller | War | Western |
		// 1unknown | 2Action | 3Adventure | 4Animation | 5Children's | 6Comedy | 7Crime | 8Documentary | 9Drama | 10Fantasy | 11Film-Noir | 12Horror | 13Musical | 14Mystery | 15Romance | 16Sci-Fi | 17Thriller | 18War | 19Western |
		// |1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19
		
		// 267|unknown| <rok> |  | <imdb> |1-1|2-0|3-0|4-0|5-0|6-0|7-0|8-0|9-0|10-0|11-0|12-0|13-0|14-0|15-0|16-0|17-0|18-0|19-0
		//
		
		String[] tokensInArray = line.split(DELIMITER);
		List<String> tokens = new ArrayList<String>(Arrays.asList(tokensInArray));		
		
		int movieId = Integer.parseInt(tokens.remove(0));
		String movieTitle = tokens.remove(0);
		String releaseDate = tokens.remove(0);
		String videoReleaseDate = tokens.remove(0);
		String imdbURL = tokens.remove(0);
	
		if (DEBUG) {
			System.out.println("Line: " + line);

			System.out.println("movieId: " + movieId);
			System.out.println("movieTitle: " + movieTitle);
			System.out.println("releaseDate: " + releaseDate);
			System.out.println("videoReleaseDate" + videoReleaseDate);
			System.out.println("IMDBurl: " + imdbURL);
			System.out.println();
		}
		
		MovieContent content = new MovieContent();
		content.setMovieId(movieId);
		
		String isUnknown = tokens.remove(0);
		if (isUnknown.equals("1")) {
			content.setUnknown(true);	
		}
		
		String isAction = tokens.remove(0);
		if (isAction.equals("1")) {
			content.setAction(true);	
		}
		
		String isAdventure = tokens.remove(0);
		if (isAdventure.equals("1")) {
			content.setAdventure(true);
		}
		
		String isAnimation = tokens.remove(0);
		if (isAnimation.equals("1")) {
			content.setAnimation(true);
		}
		
		String isChildrens = tokens.remove(0);
		if (isChildrens.equals("1")) {
			content.setChildrens(true);	
		}
		
		String isComedy = tokens.remove(0);
		if (isComedy.equals("1")) {
			content.setComedy(true);
		}
		
		String isCrime = tokens.remove(0);
		if (isCrime.equals("1")) {
			content.setCrime(true);	
		}
		
		String isDocumentary = tokens.remove(0);
		if (isDocumentary.equals("1")) {
			content.setDocumentary(true);
		}
		
		String isDrama = tokens.remove(0);
		if (isDrama.equals("1")) {
			content.setDrama(true);
		}
		
		String isFantasy = tokens.remove(0);
		if (isFantasy.equals("1")) {
			content.setFantasy(true);	
		}
		
		String isFilmNoir = tokens.remove(0);
		if (isFilmNoir.equals("1")) {
			content.setFilmNoir(true);
		}
		
		String isHorror = tokens.remove(0);
		if (isHorror.equals("1")) {
			content.setHorror(true);
		}
		
		String isMusical = tokens.remove(0);
		if (isMusical.equals("1")) {
			content.setMusical(true);
		}
		
		String isMystery = tokens.remove(0);
		if (isMystery.equals("1")) {
			content.setMystery(true);	
		}
		
		String isRomance = tokens.remove(0);
		if (isRomance.equals("1")) {
			content.setRomance(true);	
		}
		
		String isSciFi = tokens.remove(0);
		if (isSciFi.equals("1")) {
			content.setSciFi(true);
		}
		
		String isThriller = tokens.remove(0);
		if (isThriller.equals("1")) {
			content.setThriller(true);
		}

		String isWar = tokens.remove(0);
		if (isWar.equals("1")) {
			content.setWar(true);
		}
		
		String isWestern = tokens.remove(0);		
		if (isWestern.equals("1")) {
			content.setWestern(true);
		}
		
		return content;
	}
}
