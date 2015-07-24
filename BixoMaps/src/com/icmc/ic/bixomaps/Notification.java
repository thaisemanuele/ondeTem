package com.icmc.ic.bixomaps;

/**The Notification Class
 * 
 * This class is a simple Java Class.
 * As so, it has attributes, its setters and its getters.
 * This one represents a notification and is used by the RequestHandler
 * when it is necessary to send a notification of the user's behaviour to the server 
 * 
 * @author Thais Santos
 * @version 1.0
 * @since January 27, 2015
 * 
 * */
public class Notification {
	
	/*Attributes*/
	private String type;
	private String userId;
	private Place  place;
	private String date;
	private Double lat;
	private Double lon;
	private Review rev;
	
	/*Setters*/
	public void setType(String type) {
		this.type = type;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public void setPlace(Place place) {
		this.place = place;
	}
	
	public void setDate(String date){
		this.date = date;
	}
	
	public void setLat(Double lat) 	{
		this.lat = lat;
	}
	
	public void setLon(Double lon) 	{
		this.lon = lon;
	}
	
	public void setRev(Review rev) 	{
		this.rev = rev;
	}

	/*Getters*/
	public String getType() {
		return type;
	}

	public String getUserId() {
		return userId;
	}

	public Place getPlace() {
		return place;
	}

	public String getDate() {
		return date;
	}

	public Double getLat() {
		return lat;
	}

	public Double getLon() {
		return lon;
	}

	public Review getRev() {
		return rev;
	}
	
}
