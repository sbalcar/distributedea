package org.distributedea.ontology.dataset.matrixfactorization.content;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class Represents List of {@link MovieContent}.
 * @author stepan
 *
 */
public class MovieContents implements IItemContent {
	
	private static final long serialVersionUID = 1L;

	private List<MovieContent> movieContents;
	
	private Random randomGenerator;

	/**
	 * Constructor
	 * @param movieContents
	 */
	public MovieContents() {
		setMovieContents(new ArrayList<MovieContent>());  // Only for JADE
		this.randomGenerator = new Random();
	}

	
	/**
	 * Constructor
	 * @param movieContents
	 */
	public MovieContents(List<MovieContent> movieContents) {
		setMovieContents(movieContents);
		this.randomGenerator = new Random();
	}

	public List<MovieContent> getMovieContents() {
		return movieContents;
	}
	@Deprecated
	public void setMovieContents(List<MovieContent> movieContents) {
		if (movieContents == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is null");
		}
		for (MovieContent movieContentI : movieContents) {
			if (movieContentI == null) {
				throw new IllegalArgumentException("Argument " +
						MovieContent.class.getSimpleName() + " is null");
			}
		}
		this.movieContents = movieContents;
	}
	
	public MovieContent export(int index) {
		if (movieContents == null) {
			return null;
		}
		return movieContents.get(index);
	}

	public void add(MovieContent movieContent) {
		movieContents.add(movieContent);
	}

	public MovieContent exportRandomMovieContent() {
		
		int index = randomGenerator.nextInt(size());
		return movieContents.get(index);
	}
	
	public int size() {
		if (movieContents == null) {
			return 0;
		}
		return movieContents.size();
	}
	
}
