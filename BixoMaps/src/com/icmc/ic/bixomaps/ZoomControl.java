package com.icmc.ic.bixomaps;

/**
 * ZoomControl
 * 
 * This class separates the places in quadrants and find the correct zoom
 * in order to all the places to be displayed on the map,
 * including the user's location 
 * 
 * @author Thais Santos
 * @version 1.0
 * @since May 20, 2015
 */

import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class ZoomControl {
	
	private ArrayList<Place> places;
	private LatLng max;
	private LatLng min;
	private LatLng position;
	private boolean isAvailable;
	private Double margin;
	
	/*	Constructor 
	 * 	Set the given list of places and the give user's position*/
	public ZoomControl(ArrayList<Place> places, LatLng position){
		this.places = new ArrayList<Place>();
		this.places = places;
		this.max = new LatLng(0f,0f);
		this.min = new LatLng(0f,0f);
		this.position = new LatLng(position.latitude, position.longitude);
		this.margin = 0.0;
		this.isAvailable = false;
	}
	
	public LatLngBounds zoomMap(){
		
		ArrayList<LatLng> firstQuadrant;
		ArrayList<LatLng> secondQuadrant;
		ArrayList<LatLng> thirdQuadrant;
		ArrayList<LatLng> fourthQuadrant;
		
		/*Separates each place according to its latitude and longitude values*/
		firstQuadrant = findQuadrant(1);
		secondQuadrant = findQuadrant(2);
		thirdQuadrant = findQuadrant(3);
		fourthQuadrant = findQuadrant(4);
		
		/*Determines the farthest points on each quadrant*/
		LatLng max1 = findFarthest(firstQuadrant);
		LatLng max2 = findFarthest(secondQuadrant);
		LatLng max3 = findFarthest(thirdQuadrant);
		LatLng max4 = findFarthest(fourthQuadrant);
		
		
		/*Defines the map's boundaries*/
		LatLngBounds bounds = findBoundaries(max1, max2, max3, max4);
		return bounds;
	}
	
	/*Defines the quadrant for each point*/
	private ArrayList<LatLng> findQuadrant(int q){
		
		ArrayList<LatLng> points = new ArrayList<LatLng>();
		if(q == 1){
				/*if the latitude is greater than or equal user's position and 
				 * longitude is greater than or equal user's position
				 * the point is in the first quadrant*/
				for(Place p: places){
					if(p.getLatitude()>= position.latitude
							&& p.getLongitude()>= position.longitude){
						LatLng l = new LatLng(p.getLatitude(),p.getLongitude());
						points.add(l);
					}
				}
		}
				
		if(q == 2){
				/*if the latitude is greater than or equal user's position and 
				 * longitude is less than user's position
				 * the point is in the second quadrant*/
				for(Place p: places){
					if(p.getLatitude()>= position.latitude
							&& p.getLongitude()< position.longitude){
						LatLng l = new LatLng(p.getLatitude(),p.getLongitude());
						points.add(l);
					}
				}
		}
				
		if(q == 3){
				/*if the latitude is less than user's position and 
				 * longitude is less than user's position
				 * the point is in the third quadrant*/
				for(Place p: places){
					if(p.getLatitude()< position.latitude
							&& p.getLongitude()< position.longitude){
						LatLng l = new LatLng(p.getLatitude(),p.getLongitude());
						points.add(l);
					}
				}
		}
				
		if(q == 4){
				/*if the latitude is less than user's position and 
				 * longitude is greater than user's position
				 * the point is in the fourth quadrant*/
				for(Place p: places){
					if(p.getLatitude()< position.latitude
							&& p.getLongitude()> position.longitude){
						LatLng l = new LatLng(p.getLatitude(),p.getLongitude());
						points.add(l);
					}
				}
		}
		
		return points;
	}
	
	/*Finds the highest point and the farthest point from user's location*/
	private LatLng findFarthest(ArrayList<LatLng> qPoints){
		//Double maxDist = -1.0;
		Double maxHeight = -1.0;
		Double maxWidth = -1.0;
		LatLng max;
		if(!qPoints.isEmpty()){
			Double maxLat = null;
			Double maxLon = null;
			for(LatLng p:qPoints){
				//Double dist = findDistance(p);
				//if(dist > maxDist){
				Double hDist = Math.abs(p.latitude - position.latitude);
				Double wDist = Math.abs(p.longitude - position.longitude);
				if(hDist > maxHeight){
					maxHeight = hDist;
					maxLat = p.latitude;
				}
				if(wDist > maxWidth){
					maxWidth = wDist;
					maxLon = p.longitude;
				}
			}
			max = new LatLng(maxLat, maxLon);
			return max;
		}
		
		return null;
	}
	
	/*Calculates the distance between the user and a given point*/
	private Double findDistance(LatLng p){
		Double a;
		Double b;
		Double c;
		
		b = Math.abs(p.longitude - position.longitude);
		c = Math.abs(p.latitude - position.latitude);
		/*a^2 = b^2 + c^2*/
		a = Math.sqrt((b*b)+(c*c));
		
		return a;
	}
	
	/*Defines the map's boundaries*/
	private LatLngBounds findBoundaries(LatLng max1, LatLng max2, LatLng max3, LatLng max4){
		LatLng ne = null;
		LatLng sw = null;
		Double newMaxLat_r = null;
		Double newMaxLon_r = null;
		Double newMaxLat_l = null;
		Double newMaxLon_l = null;
		/*Comparing first and fourth quadrants*/
		if(max1!=null){
			newMaxLat_r = max1.latitude + margin;
			newMaxLon_r = max1.longitude + margin;
			
			if(max4==null || (max4.longitude<=max1.longitude)){
				ne = new LatLng(newMaxLat_r,newMaxLon_r);
			}
			else if(max4.longitude>max1.longitude){
				newMaxLon_r = max4.longitude + margin;
				ne = new LatLng(newMaxLat_r,newMaxLon_r);
			}
		}
		/*Comparing third and second quadrants*/
		if(max3!=null){
			newMaxLat_l = max3.latitude - margin;
			newMaxLon_l = max3.longitude - margin;
			
			if(max2==null || (max2.longitude>=max3.longitude)){
				sw = new LatLng(newMaxLat_l,newMaxLon_l);
			}
			else if(max2.longitude<max3.longitude){
				newMaxLon_l = max2.longitude - margin;
				sw = new LatLng(newMaxLat_l,newMaxLon_l);
			}
		}
		
		if(max1==null){
			/*Defining latitude if there are no points in the first quadrant*/
			if(max2!=null){
				newMaxLat_r = max2.latitude +margin;
			}else{
				newMaxLat_r = position.latitude +margin;
			}
			/*Defining longitude if there are no points in the first quadrant*/
			if(max4!=null){
				newMaxLon_r = max4.longitude +margin;
			}else{
				newMaxLon_r = position.longitude +margin;
			}
			ne = new LatLng(newMaxLat_r,newMaxLon_r);
		}
		
		if(max3==null){
			/*Defining latitude if there are no points in the third quadrant*/
			if(max4!=null){
				newMaxLat_l = max4.latitude -margin;
			}else{
				newMaxLat_l = position.latitude -margin;
			}
			/*Defining longitude if there are no points in the third quadrant*/
			if(max2!=null){
				newMaxLon_l = max2.longitude -margin;
			}else{
				newMaxLon_l = position.longitude -margin;
			}
			sw = new LatLng(newMaxLat_l,newMaxLon_l);
		}
		
		if(max1==null&&max2==null&&max3==null&&max4==null){
			ne = new LatLng(position.latitude+0.005,position.latitude+0.005);
			sw = new LatLng(position.latitude-0.005,position.latitude-0.005);
		}
		
		LatLngBounds bounds = new LatLngBounds(sw,ne);
		
		return bounds;
	}

}
