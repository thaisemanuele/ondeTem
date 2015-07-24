package com.icmc.ic.bixomaps;
/** The Recommendation class
 * 
 * This class is a simple Java Class.
 * As so, it has attributes, its setters and its getters.
 * This one also has a constructor to initialise the array of places
 * and is used to represent a recommendation that comes from the server
 * with the places to be displayed on the map
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
