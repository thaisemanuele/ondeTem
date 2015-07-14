package com.icmc.ic.bixomaps;

/**
 * SettingsActivity
 * This is the activity for displaying the app settings
 *
 * @author Thais Santos
 * @version 1.0
 * @since April 06, 2015
 */


import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

public class SettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_settings);
		final Spinner locSpin = (Spinner)findViewById(R.id.settings_location);
		final Spinner numSpin = (Spinner)findViewById(R.id.settings_number);
		
		//List of Places where the App is available
		//Add more cities later
		ArrayList<String> places = new ArrayList<String>();
		places.add(getString(R.string.city_saocarlos));
		
		//number of recommendations
		ArrayList<String> nRec = new ArrayList<String>();
		nRec.add("15");
		//Adapters for the spinners
		ArrayAdapter adapter =  new ArrayAdapter(this,  android.R.layout.simple_spinner_item, places);
		ArrayAdapter nAdapter =  new ArrayAdapter(this,  android.R.layout.simple_spinner_item, nRec);
		
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    locSpin.setAdapter(adapter);
	    numSpin.setAdapter(nAdapter);
	    
	    Button save = (Button)findViewById(R.id.settings_confirm);
	    /*Save button listener*/
	    save.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				String place = locSpin.getSelectedItem().toString();
				String num = numSpin.getSelectedItem().toString();
				XmlHandler settingsXml = new XmlHandler();
				settingsXml.writeSettings(place, num);
				Toast toast = Toast.makeText(getApplicationContext(), R.string.settings_done, 10);
				toast.show();
				finish();
			}
	    	
	    });
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
