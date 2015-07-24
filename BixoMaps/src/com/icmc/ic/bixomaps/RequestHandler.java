package com.icmc.ic.bixomaps;
/**
 * RequestHandler
 * This class handles the creation of requests and notifications,
 * Get recommendation requests and event notifications,
 * And change the category string to be added to the request 
 *
 * @author Thais Santos
 * @version 1.0
 * @since February 24, 2015
 */
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.text.format.Time;

public class RequestHandler {

	/*Uses the category chosen, the user's position and the context (the Activity) to create a request
	 * and write it to a xml*/
	public static void createRequest(String category, Double lat, Double lon, Context c){
		XmlHandler xml= new XmlHandler();
		Request request = new Request();
		request.userid = android.os.Build.SERIAL; 
		Calendar calendar = Calendar.getInstance();
		Date d = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
		request.date =  sdf.format(d);
		request.lat = lat;
		request.lon = lon;
		category = formatString(category,c);
		request.setCategory(category);
		/*Add preferences here*/
		request.preferences.add("");
		xml.writeRecommendation(request);
	}
	
	/*Uses the place , the notification type, the user's position, the review added and the context (the Activity)
	 * to create a notification
	 * and write it to a xml*/
	public static void createNotification(Place p, String type,  Double lat, Double lon, Review rev, Context c){
		
		XmlHandler xml= new XmlHandler();
		Notification note = new Notification();
		note.setType(type);
		note.setUserId(android.os.Build.SERIAL);
		Calendar calendar = Calendar.getInstance();
		Date d = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
		note.setDate(sdf.format(d));
		note.setLat(lat);
		note.setLon(lon);
		p.setCategory(formatString(p.getCategory(),c));
		note.setPlace(p);
		if(rev != null){
			note.setRev(rev);
		}
		else{
			Review r = new Review();
			Float f = (float) 0.0;
			r.setComment("");
			r.setLanguage("");
			r.setOverallRating(f);
			note.setRev(r);
		}
		xml.writeNotification(note);
		
	}
	
	//Get the recommendation request with the data that will be send to the web server
		public static String getRecommendationRequest(String category, Double lat, Double lon, Context c){
			XmlHandler xml= new XmlHandler();
			Request request = new Request();
			request.userid = android.os.Build.SERIAL; 
			Calendar calendar = Calendar.getInstance();
			Date d = calendar.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
			request.date =  sdf.format(d);
			request.lat = lat;
			request.lon = lon;
			category = formatString(category,c);
			request.setCategory(category);
			/*Add preferences here*/
			request.preferences.add("");
			return xml.createRecommendationRequestXMLdata(request);
		}
		
		// Get the event notification with the data that will be send to the web server
		public static String getEventNotification(Place p, String type,  Double lat, Double lon, Review rev, Context c){
			
			XmlHandler xml= new XmlHandler();
			Notification note = new Notification();
			note.setType(type);
			note.setUserId(android.os.Build.SERIAL);
			Calendar calendar = Calendar.getInstance();
			Date d = calendar.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
			note.setDate(sdf.format(d));
			note.setLat(lat);
			note.setLon(lon);
			p.setCategory(formatString(p.getCategory(),c));
			note.setPlace(p);
			
			if(rev != null) {
				note.setRev(rev);
			} else{
				Review r = new Review();
				Float f = (float) 0.0;
				r.setComment("");
				r.setLanguage("");
				r.setOverallRating(f);
				note.setRev(r);
			}
			
			return xml.createEventNotificationXMLdata(note);
			
		}

	/*Change the string in entry to be added to the request with the correct name*/
	public static  String formatString(String entry, Context context){
		if(entry.equalsIgnoreCase(context.getString(R.string.category_car))) entry = "car_services";
		else if(entry.equalsIgnoreCase(context.getString(R.string.category_bank))) entry = "bank";
		else if(entry.equalsIgnoreCase(context.getString(R.string.category_education))) entry = "education";
		else if(entry.equalsIgnoreCase(context.getString(R.string.category_medical))) entry = "medical_care";
		else if(entry.equalsIgnoreCase(context.getString(R.string.category_emergency))) entry = "emergency_services";
		else if(entry.equalsIgnoreCase(context.getString(R.string.category_culture))) entry = "culture_entertainment";
		else if(entry.equalsIgnoreCase(context.getString(R.string.category_food))) entry = "food_drink";
		else if(entry.equalsIgnoreCase(context.getString(R.string.category_government))) entry = "government";
		else if(entry.equalsIgnoreCase(context.getString(R.string.category_lodging))) entry = "lodging";
		else if(entry.equalsIgnoreCase(context.getString(R.string.category_recreation))) entry = "recreation";
		else if(entry.equalsIgnoreCase(context.getString(R.string.category_publicservices))) entry = "public_services";
		else if(entry.equalsIgnoreCase(context.getString(R.string.category_shops))) entry = "service_shops_stores";
		else if(entry.equalsIgnoreCase(context.getString(R.string.category_transport))) entry = "public_transport";
		else if(entry.equalsIgnoreCase(context.getString(R.string.category_worship))) entry = "places_worship";
		else if(entry.equalsIgnoreCase(context.getString(R.string.category_home))) entry = "basic_home_services";
		else if(entry.equalsIgnoreCase(context.getString(R.string.category_beauty))) entry = "beauty_care";
		else if(entry.equalsIgnoreCase(context.getString(R.string.category_administrative))) entry = "administrative_services";
		else if(entry.equalsIgnoreCase(context.getString(R.string.category_animal))) entry = "animal_care";
		else if(entry.equalsIgnoreCase(context.getString(R.string.category_notary))) entry = "notary_courier";
		return entry;
		
		}

}

