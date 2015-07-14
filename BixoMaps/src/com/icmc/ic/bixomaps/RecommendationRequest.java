package com.icmc.ic.bixomaps;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * This class connects to the POI Web Server and sends xml request file and gets a
 * xml recommendation file.
 * This class uses background threads to perform long running network IO operations, 
 * so that the main UI thread is not locked up.
 *
 * @author Marcos Domingues
 * @version 1.0
 * @since January 27, 2015
 */

public class RecommendationRequest {

	public static final String WebServerUri = "http://143.107.183.246:8080/POIbroker/Adapt";
	private int NetworkConnectionTimeout_ms = 5000;
	private static MainActivity _activity;
	private String result = new String();
	private ProgressBar progress;

	
	public void execute(MainActivity activity) {

		_activity = activity;

		// allows non-"edt" thread to be re-inserted into the "edt" queue
		final Handler uiThreadCallback = new Handler();

		// performs rendering in the "edt" thread, after background operation is complete
		final Runnable runInUIThread = new Runnable() {
			public void run() {
				_showInUI();
				
			}
		};

		new Thread() {
			@Override public void run() {
				_doInBackgroundPost();
				uiThreadCallback.post(runInUIThread);
			}
		}.start();

		//Toast.makeText(_activity, "Getting data from servlet", Toast.LENGTH_LONG).show();
	}

	/** this method is called in a non-"edt" thread */
	private void _doInBackgroundPost() {
		Log.i(getClass().getSimpleName(), "background task - start");

		try {
			HttpParams params = new BasicHttpParams();

			// set params for connection...
			HttpConnectionParams.setStaleCheckingEnabled(params, false);
			HttpConnectionParams.setConnectionTimeout(params, NetworkConnectionTimeout_ms);
			HttpConnectionParams.setSoTimeout(params, NetworkConnectionTimeout_ms);
			
			DefaultHttpClient httpClient = new DefaultHttpClient(params);

			// create post method
			HttpPost postMethod = new HttpPost(WebServerUri);

			// create request entity for binary data
			/*ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(_activity.xml);
			ByteArrayEntity req_entity = new ByteArrayEntity(baos.toByteArray());
			req_entity.setContentType(MIMETypeConstantsIF.BINARY_TYPE);
			postMethod.setEntity(req_entity); // associating entity with method*/

			// create request entity for xml data
			StringEntity se = new StringEntity(_activity.requestXML,"UTF-8");
			postMethod.setEntity(se); // associating entity with method
    
			// execute the http post call and get the response
			httpClient.execute(postMethod, new ResponseHandler<Void>() {
				public Void handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
					HttpEntity resp_entity = response.getEntity();
					
					if (resp_entity != null) {
						try {
							//byte[] data = EntityUtils.toByteArray(resp_entity);
							//ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
							//dataFromServlet = (Hashtable<DataKeys, Serializable>) ois.readObject();
							//Log.i(getClass().getSimpleName(), "data size from servlet=" + data.toString());
							//Log.i(getClass().getSimpleName(), "data hashtable from servlet=" + dataFromServlet.toString());

							result = EntityUtils.toString(resp_entity,"latin1");
							
							// store the recommendation in the mobile phone sdcard
							XmlHandler handler = new XmlHandler();
							File replyXML = new File(handler.getDir().getPath()+File.separator+"reply.xml");
					        FileWriter wr = new FileWriter(replyXML);
					        wr.append(result);
					        wr.close();
							
							Log.i(getClass().getSimpleName(), result);
						} catch (Exception e) {
							Log.e(getClass().getSimpleName(), "problem processing post response", e);
						}
					} else {
						throw new IOException(new StringBuffer().append("HTTP response : ").append(response.getStatusLine()).toString());
					}
					
					return null;
				}
			});
			
		} catch (Exception e) {
			Log.e(getClass().getSimpleName(), "problem encountered", e);
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			Log.e(getClass().getSimpleName(), sw.getBuffer().toString(), e);
		}

		Log.i(getClass().getSimpleName(), "background task - end");
	}

	/** this method is called in the "edt" */
	private void _showInUI() {

		XmlHandler handler = new XmlHandler();
		_activity.rec = handler.getRecommendationFromResponseXMLdata(result);
		Log.i(getClass().getSimpleName(), result);
		
		_activity.googleMap.clear();
	
		if( _activity.myLocation!=null) _activity.point1 = new LatLng(_activity.myLocation.getLatitude(), _activity.myLocation.getLongitude());
		if( _activity.myLocation!=null) _activity.point2 = new LatLng(_activity.myLocation.getLatitude(), _activity.myLocation.getLongitude());
		
		for(Place p: _activity.rec.getPlaces()){
			Double lat = p.getLatitude();
			Double lon = p.getLongitude();
			String name = p.getName();
			MarkerOptions m = new MarkerOptions().position(new LatLng(lat,lon)).title(name);
			_activity.googleMap.addMarker(m);
		}
		mapReposition();
		
		//_activity.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(_activity.point1, 15)); // Marcos: Ver Thais se preciso disso
		
		/*
		Log.i(getClass().getSimpleName(), "Updating the screen ...");
		
		if (data != null)
			Toast.makeText(_activity, "Got data from service: " + data.toString(), Toast.LENGTH_SHORT).show();
		
		if (ex != null)
			Toast.makeText(_activity, ex.getMessage() == null ? "Error" : "Error - " + ex.getMessage(), Toast.LENGTH_SHORT).show();
		*/
		//  Toast.makeText(_activity,
		//  "completed background task, rejoining \"edt\"",
		
		// _activity._displayUserProfile(dataFromServlet);
	}
	
	public void mapReposition(){
		
		LatLng mPoint = new LatLng(_activity.myLocation.getLatitude(),_activity.myLocation.getLongitude());
		ZoomControl zControl = new ZoomControl(_activity.rec.places, mPoint);
		progress = (ProgressBar)_activity.findViewById(R.id.search_progressBar);
		progress.setIndeterminate(true);
		progress.setVisibility(View.GONE);
		if(!_activity.rec.places.isEmpty()){
			
			/*Atributing the right name to what is being displayed*/
			_activity.getActionBar().setTitle(_activity.getSelectedName());
			_activity.googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(zControl.zoomMap(), 100));
			
		}
		else{
			Toast.makeText(_activity, R.string.no_results, Toast.LENGTH_LONG).show();
			_activity.getActionBar().setTitle(_activity.getString(R.string.app_name));
		}
			
	}

	
}

