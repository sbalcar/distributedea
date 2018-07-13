package org.distributedea.ontology.dataset.matrixfactorization.content;

import java.util.ArrayList;
import java.util.List;

import org.distributedea.ontology.dataset.matrixfactorization.IItemContentModel;

/**
 * Model represents Items-Content informations for Movie Lens dataset
 * @author stepan
 *
 */
public class ContentMLMoviesModel implements IItemContentModel {

	private MovieContents moviesContentAll;

	// duplicated information
	private MovieContents moviesGenreUnknow;
	private MovieContents moviesGenreAction;
	private MovieContents moviesGenreAdventure;
	private MovieContents moviesGenreAnimation;
	private MovieContents moviesGenreChildrens;
	private MovieContents moviesGenreComedy;
	private MovieContents moviesGenreCrime;
	private MovieContents moviesGenreDocumentary;
	private MovieContents moviesGenreDrama;
	private MovieContents moviesGenreFantasy;
	private MovieContents moviesGenreFilmNoir;
	private MovieContents moviesGenreHorror;
	private MovieContents moviesGenreMusical;
	private MovieContents moviesGenreMystery;
	private MovieContents moviesGenreRomance;
	private MovieContents moviesGenreSciFi;
	private MovieContents moviesGenreThriller;
	private MovieContents moviesGenreWar;
	private MovieContents moviesGenreWestern;
	
	/**
	 * Constructor
	 * @param filmsContent
	 */
	public ContentMLMoviesModel (List<IItemContent> filmsContent) {
		if (filmsContent == null) {
			throw new IllegalArgumentException("Argument " +
					List.class.getSimpleName() + " is null");
		}
		List<MovieContent> filmsContentConv = new ArrayList<>();
		
		for (IItemContent itemContentI : filmsContent) {
			if (!(itemContentI instanceof MovieContent)) {
				throw new IllegalArgumentException("Argument " +
						"is not istance of " + MovieContent.class.getSimpleName());
			}
			filmsContentConv.add((MovieContent) itemContentI);
		}
		setMoviesContent(filmsContentConv);
	}

	public MovieContents getMoviesContent() {
		return moviesContentAll;
	}
	@Deprecated
	public void setMoviesContent(List<MovieContent> movieContentsList) {
		
		MovieContents movieContents = new MovieContents(movieContentsList);
		
		this.moviesGenreUnknow = new MovieContents();
		this.moviesGenreAction = new MovieContents();
		this.moviesGenreAdventure = new MovieContents();
		this.moviesGenreAnimation = new MovieContents();
		this.moviesGenreChildrens = new MovieContents();
		this.moviesGenreComedy = new MovieContents();
		this.moviesGenreCrime = new MovieContents();
		this.moviesGenreDocumentary = new MovieContents();
		this.moviesGenreDrama = new MovieContents();
		this.moviesGenreFantasy = new MovieContents();
		this.moviesGenreFilmNoir = new MovieContents();
		this.moviesGenreHorror = new MovieContents();
		this.moviesGenreMusical = new MovieContents();
		this.moviesGenreMystery = new MovieContents();
		this.moviesGenreRomance = new MovieContents();
		this.moviesGenreSciFi = new MovieContents();
		this.moviesGenreThriller = new MovieContents();
		this.moviesGenreWar = new MovieContents();
		this.moviesGenreWestern = new MovieContents();

		this.moviesContentAll = movieContents;
		
		for (MovieContent movieContentI : movieContentsList) {
			
			if (movieContentI.isUnknown()) {
				this.moviesGenreUnknow.add(movieContentI);
				
			} else if (movieContentI.isAction()) {
				this.moviesGenreAction.add(movieContentI);
				
			} else if (movieContentI.isAdventure()) {
				this.moviesGenreAdventure.add(movieContentI);
				
			} else if (movieContentI.isAnimation()) {
				this.moviesGenreAnimation.add(movieContentI);
				
			} else if (movieContentI.isChildrens()) {
				this.moviesGenreChildrens.add(movieContentI);
				
			} else if (movieContentI.isComedy()) {
				this.moviesGenreComedy.add(movieContentI);
				
			} else if (movieContentI.isCrime()) {
				this.moviesGenreCrime.add(movieContentI);
				
			} else if (movieContentI.isDocumentary()) {
				this.moviesGenreDocumentary.add(movieContentI);
				
			} else if (movieContentI.isDrama()) {
				this.moviesGenreDrama.add(movieContentI);
				
			} else if (movieContentI.isFantasy()) {
				this.moviesGenreFantasy.add(movieContentI);
				
			} else if (movieContentI.isFilmNoir()) {
				this.moviesGenreFilmNoir.add(movieContentI);
				
			} else if (movieContentI.isHorror()) {
				this.moviesGenreHorror.add(movieContentI);
				
			} else if (movieContentI.isMusical()) {
				this.moviesGenreMusical.add(movieContentI);
				
			} else if (movieContentI.isMystery()) {
				this.moviesGenreMystery.add(movieContentI);
				
			} else if (movieContentI.isRomance()) {
				this.moviesGenreRomance.add(movieContentI);
				
			} else if (movieContentI.isSciFi()) {
				this.moviesGenreSciFi.add(movieContentI);
				
			} else if (movieContentI.isThriller()) {
				this.moviesGenreThriller.add(movieContentI);
				
			} else if (movieContentI.isWar()) {
				this.moviesGenreWar.add(movieContentI);
				
			} else if (movieContentI.isWestern()) {
				this.moviesGenreWestern.add(movieContentI);
			}
			
		}
		
	}
	
	public List<IItemContent> exportIItemContents() {
		List<IItemContent> itemContents = new ArrayList<>();
		for (MovieContent filmContentI : this.moviesContentAll.getMovieContents()) {
			itemContents.add(filmContentI);
		}
		return itemContents;
	}
	
	public MovieContent exportMovieContent(int movieId) {
		
		// tests if the movie's IDs does not match the index of list
		if (movieId < this.moviesContentAll.size()) {
			MovieContent filmContent = this.moviesContentAll.export(movieId);
			if (filmContent.getMovieId() == movieId) {
				return filmContent;
			}
		}

		// tests if the movie's IDs -1 does not match the index of list
		if (0 < movieId && movieId < this.moviesContentAll.size()) {
			MovieContent filmContent = this.moviesContentAll.export(movieId -1);
			if (filmContent.getMovieId() == movieId) {
				return filmContent;
			}
		}

		// we will go through all
		for (MovieContent filmContentI : this.moviesContentAll.getMovieContents()) {
			if (filmContentI.getMovieId() == movieId) {
				return filmContentI;
			}
		}
		
		return null;
	}
	
	
	public MovieContents exportMoviesGenreUnknow() {
		return moviesGenreUnknow;
	}	
	public MovieContents exportMoviesGenreAction() {
		return moviesGenreAction;
	}
	public MovieContents exportMoviesGenreAdventure() {
		return moviesGenreAdventure;
	}
	public MovieContents exportMoviesGenreAnimation() {
		return moviesGenreAnimation;
	}
	public MovieContents exportMoviesGenreChildrens() {
		return moviesGenreChildrens;
	}	
	public MovieContents exportMoviesGenreComedy() {
		return moviesGenreComedy;
	}
	public MovieContents exportMoviesGenreCrime() {
		return moviesGenreCrime;
	}
	public MovieContents exportMoviesGenreDocumentary() {
		return moviesGenreDocumentary;
	}
	public MovieContents exportMoviesGenreDrama() {
		return moviesGenreDrama;
	}
	public MovieContents exportMoviesGenreFantasy() {
		return moviesGenreFantasy;
	}
	public MovieContents exportMoviesGenreFilmNoir() {
		return moviesGenreFilmNoir;
	}
	public MovieContents exportMoviesGenreHorror() {
		return moviesGenreHorror;
	}
	public MovieContents exportMoviesGenreMusical() {
		return moviesGenreMusical;
	}
	public MovieContents exportMoviesGenreMystery() {
		return moviesGenreMystery;
	}
	public MovieContents exportMoviesGenreRomance() {
		return moviesGenreRomance;
	}
	public MovieContents exportMoviesGenreSciFi() {
		return moviesGenreSciFi;
	}
	public MovieContents exportMoviesGenreThriller() {
		return moviesGenreThriller;
	}
	public MovieContents exportMoviesGenreWar() {
		return moviesGenreWar;
	}
	public MovieContents exportMoviesGenreWestern() {
		return moviesGenreWestern;
	}
	
	
	
	public int size() {
		if (moviesContentAll == null) {
			return 0;
		}
		return moviesContentAll.size();
	}
	
}
