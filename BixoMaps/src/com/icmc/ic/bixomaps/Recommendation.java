package com.icmc.ic.bixomaps;
/** The Recommendation class
 * 
 * @author Thais Santos
 * @version 1.0
 * @since January 27, 2015
 * 
 * */
import java.util.ArrayList;

public class Recommendation {
	/*Attributes*/
	ArrayList<Place> places;
	
	/* Constructor
	 * Initialises the array of places*/
	public Recommendation() {
		super();
		this.places = new ArrayList<Place>();
	}

	/*Getter*/
	public ArrayList<Place> getPlaces() {
		return places;
	}
	
	
}
