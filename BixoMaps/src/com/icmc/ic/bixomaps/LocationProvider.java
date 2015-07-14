package com.icmc.ic.bixomaps;
/**
 * LocationProvider
 * This class monitors the GPS or Location services provider,
 * Checking connectivity, latitude and longitude
 *
 * @author Thais Santos
 * @version 1.0
 * @since February 24, 2015
 */

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.audiofx.BassBoost.Settings;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class LocationProvider extends Service implements LocationListener {

	private final Context mContext;
	 
    // flag for GPS status
    boolean isGPSEnabled = false;
 
    // flag for network status
    boolean isNetworkEnabled = false;
 
    // flag for GPS status
    boolean canGetLocation = false;
 
    Location location; // location
    double latitude; // latitude
    double longitude; // longitude
 
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
 
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
 
    // Declaring a Location Manager
    protected LocationManager locationManager;
 
    /*	Constructor for the Location Manager
     * 	Calls get Location when the object is instantiated
     */
    public LocationProvider(Context context) {
        this.mContext = context;
        
        getLocation();
    }
 
    
    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);
 
            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
 
            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
 
            if (!isGPSEnabled && !isNetworkEnabled) {
            	
            	/*No action when the Location services are not enabled*/
            	
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                	//while(location==null){
                		locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                	//}
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                    	location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                else if (isGPSEnabled) {
                    if (location == null) {
                    	//while(location==null){
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    	//}
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }
        /* Returns the user's location
         * (latitude and longitude)*/
        return location;
    }
     
    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     * */
    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(LocationProvider.this);
        }       
    }
     
    /**
     * Function to get latitude
     * */
    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }
         
        // return latitude
        return latitude;
    }
     
    /**
     * Function to get longitude
     * */
    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }
         
        // return longitude
        return longitude;
    }
     
    /**
     * Function to check GPS/wifi enabled
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }
     
    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     * */
    public void showSettingsAlert(){
        
        Toast.makeText
        	(getApplicationContext(), "GPS is not enabled. Change settings menu", 10).show();
    }
    
   
    /*Listening for location changing*/
	@Override
	public void onLocationChanged(Location location) {
		this.location = location;
		latitude = location.getLatitude();
        longitude = location.getLongitude();
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	/*Class implemented following the tutorial 
	 *http://www.androidhive.info/2012/07/android-gps-location-manager-tutorial/*/

}
