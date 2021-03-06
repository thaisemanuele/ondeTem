package com.icmc.ic.bixomaps;

import java.util.ArrayList;

/** The Place class
 * 
 * This class is a simple Java Class.
 * As so, it has attributes, its setters and its getters.
 * This one represents a place, like a cafeteria, a museum or a school
 * Those are the places the user is going to check in to. 
 * 
 * @author Thais Santos
 * @version 1.0
 * @since January 27, 2015
 * 
 * */
public class Place {
	
	/*Attributes*/
	private String id;
	private String name;
	private String address;
	private String phone;
	private Double latitude;
	private Double longitude;
	private String icon;
	private String url;
	private String website;
	private Float rating;
	private String category;
	private ArrayList<Review> reviews;
	private Boolean checkinAvailable;
	
	/*	Constructor
	 * 	Add reviews to a place,
	 * 	Set the check in availability*/
	public Place() {
		super();
		this.reviews = new ArrayList<Review>();	
		this.checkinAvailable = true;
	}
	
	/*Verify check in availability*/
	public Boolean isCheckinAvailable() {
		return checkinAvailable;
	}

	/*Set check in availability*/
	public void setCheckinAvailable(Boolean checkinAvailable) {
		this.checkinAvailable = checkinAvailable;
	}

	/**
	 * Getters and Setters
	 */
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public Double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
	public Double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	public String getIcon() {
		return icon;
	}
	
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getWebsite() {
		return website;
	}
	
	public void setWebsite(String website) {
		this.website = website;
	}
	
	public Float getRating() {
		/*Place rating should consider the average of the ratings in the 
		 * reviews, and consider the overall rating when there are no reviews*/
		float counter = 0.0f;
		int divBy = 0;
		for(Review r: reviews){
			counter += r.overallRating;
			divBy++;
		}
		if(divBy!=0){
			return counter/divBy;
		}else return rating;
	}
	
	public void setRating(Float rating) {
		this.rating = rating;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	public ArrayList<Review> getReviews() {
		return reviews;
	}
	
	public void setReviews(ArrayList<Review> reviews) {
		this.reviews = reviews;
	}
	
}
