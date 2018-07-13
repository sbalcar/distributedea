package org.distributedea.ontology.dataset.matrixfactorization.content;

/**
 * Class represents item-based content informations for Movie Lens data sets
 * @author stepan
 *
 */
public class MovieContent implements IItemContent {
	
	private static final long serialVersionUID = 1L;
	
	private int movieId;
	
	private boolean unknown;
	private boolean action;
	private boolean adventure;
	private boolean animation;
	private boolean childrens;
	private boolean comedy;
	private boolean crime;
	private boolean documentary;
	private boolean drama;
	private boolean fantasy;
	private boolean filmNoir;
	private boolean horror;
	private boolean musical;
	private boolean mystery;
	private boolean romance;
	private boolean sciFi;
	private boolean thriller;
	private boolean war;
	private boolean western;
	
	
	public int getMovieId() {
		return movieId;
	}
	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}
	
	
	public boolean isUnknown() {
		return unknown;
	}
	public void setUnknown(boolean unknown) {
		this.unknown = unknown;
	}
	
	public boolean isAction() {
		return action;
	}
	public void setAction(boolean action) {
		this.action = action;
	}
	
	public boolean isAdventure() {
		return adventure;
	}
	public void setAdventure(boolean adventure) {
		this.adventure = adventure;
	}
	
	public boolean isAnimation() {
		return animation;
	}
	public void setAnimation(boolean animation) {
		this.animation = animation;
	}
	
	public boolean isChildrens() {
		return childrens;
	}
	public void setChildrens(boolean childrens) {
		this.childrens = childrens;
	}
	
	public boolean isComedy() {
		return comedy;
	}
	public void setComedy(boolean comedy) {
		this.comedy = comedy;
	}
	
	public boolean isCrime() {
		return crime;
	}
	public void setCrime(boolean crime) {
		this.crime = crime;
	}
	
	public boolean isDocumentary() {
		return documentary;
	}
	public void setDocumentary(boolean documentary) {
		this.documentary = documentary;
	}
	
	public boolean isDrama() {
		return drama;
	}
	public void setDrama(boolean drama) {
		this.drama = drama;
	}
	
	public boolean isFantasy() {
		return fantasy;
	}
	public void setFantasy(boolean fantasy) {
		this.fantasy = fantasy;
	}
	
	public boolean isFilmNoir() {
		return filmNoir;
	}
	public void setFilmNoir(boolean filmNoir) {
		this.filmNoir = filmNoir;
	}
	
	public boolean isHorror() {
		return horror;
	}
	public void setHorror(boolean horror) {
		this.horror = horror;
	}
	
	public boolean isMusical() {
		return musical;
	}
	public void setMusical(boolean musical) {
		this.musical = musical;
	}
	
	public boolean isMystery() {
		return mystery;
	}
	public void setMystery(boolean mystery) {
		this.mystery = mystery;
	}
	
	public boolean isRomance() {
		return romance;
	}
	public void setRomance(boolean romance) {
		this.romance = romance;
	}
	
	public boolean isSciFi() {
		return sciFi;
	}
	public void setSciFi(boolean sciFi) {
		this.sciFi = sciFi;
	}
	
	public boolean isThriller() {
		return thriller;
	}
	public void setThriller(boolean thriller) {
		this.thriller = thriller;
	}
	
	public boolean isWar() {
		return war;
	}
	public void setWar(boolean war) {
		this.war = war;
	}
	
	public boolean isWestern() {
		return western;
	}
	public void setWestern(boolean western) {
		this.western = western;
	}

	
	public void print() {
		
		String NL = "\n";
		
		System.out.println("movieId " + movieId + NL +
				"unknown " + unknown + NL +
				"action " + action + NL +
				"adventure " + adventure + NL +
				"animation " + animation + NL +
				"childrens " + childrens + NL +
				"comedy " + comedy + NL +
				"crime " + crime + NL +
				"documentary " + documentary + NL +
				"drama " + drama + NL +
				"fantasy " + fantasy + NL +
				"filmNoir " + filmNoir + NL +
				"horror " + horror + NL +
				"musical " + musical + NL +
				"mystery " + mystery + NL +
				"romance " + romance + NL +
				"sciFi " + sciFi + NL +
				"thriller " + thriller + NL +
				"war " + war + NL +
				"western " + western);

	}
	
}
