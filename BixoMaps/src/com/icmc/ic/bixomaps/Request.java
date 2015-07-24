package com.icmc.ic.bixomaps;

/** The Request Class 
 * 
 * This class is a simple Java Class.
 * As so, it has attributes, its setters and its getters.
 * This one represents a request, with the category chosen by the user
 * The request is sent to the server to request places to be shown on the map.
 * Also, this one has a constructor that set the category requested and the preferences 
 * (if there are any)
 * 
 * 
 * @author Thais Santos
 * @version 1.0
 * @since January 27, 2015
 * 
 * */
import java.util.ArrayList;

public class Request {
	
	/*Attributes*/
	String userid;
	String date;
	Double lat;
	Double lon;
	ArrayList<String> preferences;
	String category;
	
	/*	Constructor
	 * 	set the category requested and the preferences*/	
	public Request() {
		super();
		this.category = new String();
		this.preferences = new ArrayList<String>();
	}
	
	/** Getters and Setters*/
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public Double getLon() {
		return lon;
	}
	public void setLon(Double lon) {
		this.lon = lon;
	}
	public ArrayList<String> getPreferences() {
		return preferences;
	}
	public void setPreferences(ArrayList<String> preferences) {
		this.preferences = preferences;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	
}
