package com.icmc.ic.bixomaps;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * This class works as an adapter for the InfoWindow, a feature of the Google Map
 * that displays the name of the place when the marker is clicked.
 * The default behaviour of the InfoWindow is to display the place name.
 * This adapter changes the behaviour to display the name and the address of 
 * a place when the marker is clicked
 *
 * @author Thais Santos
 * @version 1.0
 * @since March 26, 2015
 */

public class InfoAdapter implements InfoWindowAdapter{

	private static MainActivity _activity;
	public InfoAdapter(MainActivity activity){
		this._activity = activity;
	}
	@Override
	public View getInfoContents(Marker m) {
		 View view = _activity.getLayoutInflater().inflate(R.layout.info_adapter, null);
		 /*Getting the views declared on the xml file*/
         TextView title = (TextView) view.findViewById(R.id.info_window_title);
         TextView address = (TextView) view.findViewById(R.id.info_window_address);
         
         /*setting the views with the information obtained from the marker*/
         title.setText(m.getTitle());
         address.setText(MainActivity.getPlaceAdress(m.getTitle()));
         
         /*Returning the new view*/
         return view;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		return null;
	}

}
