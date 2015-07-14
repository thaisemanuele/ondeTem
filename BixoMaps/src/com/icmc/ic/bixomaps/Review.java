package com.icmc.ic.bixomaps;

/** The Review Class
 * 
 * @author Thais Santos
 * @version 1.0
 * @since January 27, 2015
 * 
 * */
public class Review {
	/*Attributes*/
	String id;
	String time;
	String language;
	Float overallRating;
	String comment;
	
	
	
	
	/**
	 * Getters and Setters
	 */
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getTime() {
		return time;
	}
	
	public void setTime(String time) {
		this.time = time;
	}
	
	public String getLanguage() {
		return language;
	}
	
	public void setLanguage(String language) {
		this.language = language;
	}
	
	public Float getOverallRating() {
		return overallRating;
	}
	
	public void setOverallRating(Float overallRating) {
		this.overallRating = overallRating;
	}
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
