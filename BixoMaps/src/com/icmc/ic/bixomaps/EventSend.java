package com.icmc.ic.bixomaps;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.os.Handler;
import android.util.Log;

/**
	 * This class connects to the POI Web Server and sends xml notify file with the events
	 * (CLICK, DIRECTION, CHECKIN) carried out on the apk.
	 * This class uses background threads to perform long running network IO operations, 
	 * so that the main UI thread is not locked up.
	 *
	 * @author Marcos Domingues
	 * @version 1.0
	 * @since January 27, 2015
	 */

	public class EventSend {

		public static final String WebServerUri = "http://143.107.183.246:8080/POIbroker/RegisterActions";
		private int NetworkConnectionTimeout_ms = 5000;
		private static MainActivity _activity;
		
		public void execute(MainActivity activity) {

			_activity = activity;

			// allows non-"edt" thread to be re-inserted into the "edt" queue
			final Handler uiThreadCallback = new Handler();

			// performs rendering in the "edt" thread, after background operation is complete
			final Runnable runInUIThread = new Runnable() {
				public void run() {
					//_showInUI();
				}
			};

			new Thread() {
				@Override public void run() {
					_doInBackgroundPost();
					uiThreadCallback.post(runInUIThread);
				}
			}.start();

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

				// create request entity for xml data
				StringEntity se = new StringEntity(_activity.notifyXML,"UTF-8");
				postMethod.setEntity(se); // associating entity with method
	    
				Log.i(getClass().getSimpleName(), _activity.notifyXML);
				
				// execute the http post call and get the response
				httpClient.execute(postMethod, new ResponseHandler<Void>() {
					public Void handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
						Log.i(getClass().getSimpleName(), response.getStatusLine().toString());
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
		}
		

}
