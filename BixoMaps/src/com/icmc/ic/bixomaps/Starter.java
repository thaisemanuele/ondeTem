package com.icmc.ic.bixomaps;

import java.sql.Time;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

/**
 * Starter Class
 * 
 * This class works asynchronously within the Start Screen activity
 * and tries to get the user's location before a certain amount of seconds
 * 
 * @author Thais Santos
 * @version 1.0
 * @since March 19, 2015
 */


public class Starter extends AsyncTask<String, Void, Double[]> {
	
	AsyncResponse ready;
	Time timeProvider = new Time(System.currentTimeMillis());
	Long startTime;
	Long endTime;
	Context context;
	 private static Location location;
	LocationProvider loc;
	
	/*The class' constructor
	 * ready, of type AsyncResponse is used when the process is ready to present results*/
	public Starter(Context context, AsyncResponse ready){
		super();
		this.context = context;
		this.ready = ready;
		this.loc = new LocationProvider(context); 
	}
	
	/*This are the tasks to be done in background,
	 * asynchronously, at the same time the StartScreenActivity is running */
	@Override
	protected Double[] doInBackground(String... url) {
		Double[] result = new Double[3];
		
		
		Location location = loc.getLocation();
		startTime = timeProvider.getTime();
		endTime = timeProvider.getTime();
		/*Hard coded 6 seconds*/
		while(location == null && (endTime - startTime)<6000){
			timeProvider = new Time(System.currentTimeMillis());
			endTime = timeProvider.getTime();
			location = loc.getLocation();
		}
		/*Setting results to 0 if can't find location*/
		if(location == null){
			result[0] = (double) 0;
			result[1] = (double) 0;
			result[2] = (double) 0;
			
		}else{
			/*Set users latitude and longitude,
			 * the result[0] is set to one to indicate that the location was found*/
			result[0] = (double) 1;
			result[1] = location.getLatitude();
			result[2] = location.getLongitude();
		}
		return result;
	}
	
	
	/* Returns the location(0 if not found) to the start screen*/
	@Override
    protected void onPostExecute(Double[] result) {
		String[] ans = new String[3];
		ans[0] = result[0].toString();
		ans[1] = result[1].toString();
		ans[2] = result[2].toString();
		ready.processFinish(ans);
    }

}
