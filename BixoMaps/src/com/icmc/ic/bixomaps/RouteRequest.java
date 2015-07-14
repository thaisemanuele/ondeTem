package com.icmc.ic.bixomaps;

/**
 * RouteRequest
 * 
 * This class works asynchronously within the main activity
 * and replies once the request is ready
 * 
 * @author Thais Santos
 * @version 1.0
 * @since March 19, 2015
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.android.gms.maps.model.LatLng;

import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

public class RouteRequest extends AsyncTask<String, Void, String[]> {
	
	ArrayList<LatLng> list;
	String duration = new String();
	String warnings = new String();
	String copyrights = new String();
	AsyncResponse deliver;
	
	public RouteRequest(AsyncResponse deliver) {
		super();
		this.deliver = deliver;
	}
	/*
	 *	gets a Document with the route properties according to the URL given  
	 */
	public Document requestRoute(String url){
	  
	  InputStream is = null;
      try {
           
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(url);
            
            HttpResponse httpResponse = httpClient.execute(httpPost, localContext);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
            try {
            	
            	DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document doc = builder.parse(is);
                return doc;
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
      return null;
	}

	/*
	 * builds the URL to be sent as a request
	 * Origin latitude, origin longitude, destination latitude, destination longitude, language
	 */
	public String urlBuilder (double originLat, double originLon, double destinLat, double destinLon, String lang, String mode){
        
		//StringBuilder url = new StringBuilder();
		String url = null;
		try {
			if(mode.equals("walking")){
				url = new String("https://maps.googleapis.com/maps/api/directions/xml?origin="
						+URLEncoder.encode(Double.toString(originLat),"UTF-8")+","
						+URLEncoder.encode(Double.toString(originLon),"UTF-8")+"&destination="
						+URLEncoder.encode(Double.toString(destinLat),"UTF-8")+","
						+URLEncoder.encode(Double.toString(destinLon),"UTF-8")+"&language="
						+URLEncoder.encode(lang,"UTF-8")+"&mode="
						+URLEncoder.encode(mode,"UTF-8")+"&avoid=highways"
						+URLEncoder.encode("&key=AIzaSyCOC6HIHZLzLDbOJI7-N432dp4UBv0GJcM","UTF-8"));
			}
			else{
				url = new String("https://maps.googleapis.com/maps/api/directions/xml?origin="
						+URLEncoder.encode(Double.toString(originLat),"UTF-8")+","
						+URLEncoder.encode(Double.toString(originLon),"UTF-8")+"&destination="
						+URLEncoder.encode(Double.toString(destinLat),"UTF-8")+","
						+URLEncoder.encode(Double.toString(destinLon),"UTF-8")+"&language="
						+URLEncoder.encode(lang,"UTF-8")+"&mode="
						+URLEncoder.encode(mode,"UTF-8")
						+URLEncoder.encode("&key=AIzaSyCOC6HIHZLzLDbOJI7-N432dp4UBv0GJcM","UTF-8"));
			}
					
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
        return url;
    }
	
	/*Get the request copyrights*/
	public String getCopyRights(Document doc) {
	    try {
	        NodeList nodeList = doc.getElementsByTagName("copyrights");
	        Node node = nodeList.item(0);
	        Log.i("CopyRights", node.getTextContent());
	        return node.getTextContent();
	    } catch (Exception e) {
	    	return "";
	    }
	}
	
	/*Get the request duration*/
	public String getDuration(Document doc) {
		Node node = null;
		NodeList lNode = null;
		String result = new String();
	    
		try {
	        NodeList nodeList = doc.getElementsByTagName("duration");
	        node = nodeList.item( nodeList.getLength()-1);
	        lNode = node.getChildNodes();
	        for(int i = 0;i<lNode.getLength();i++){
	        	Node n = lNode.item(i);
	        	if(n.getNodeName().equalsIgnoreCase("text")){
	        		result = n.getTextContent();
	        	}
	        	Log.i("Duration "+i+" "+n.getNodeName(), result);
	   		 
	        }
	       	
	        return result;
	    } catch (Exception e) {
	    	return "";
	    }
	}
	
	/*Get the request warnings*/
	public String getWarnings(Document doc) {
	    try {
	        NodeList nodeList = doc.getElementsByTagName("warning");
	        Node node = nodeList.item(0);
	        return node.getTextContent();
	    } catch (Exception e) {
	    	return "";
	    }
	}
	
	/*Get the request steps to be drawn*/
	public ArrayList<LatLng> getSteps(Document doc) {
	    NodeList nl1, nl2, nl3;
	    ArrayList<LatLng> listGeopoints = new ArrayList<LatLng>();
	    nl1 = doc.getElementsByTagName("step");
	    if (nl1.getLength() > 0) {
	        for (int i = 0; i < nl1.getLength(); i++) {
	            Node node1 = nl1.item(i);
	            nl2 = node1.getChildNodes();

	            Node locationNode = nl2
	                    .item(getNodeIndex(nl2, "start_location"));
	            nl3 = locationNode.getChildNodes();
	            Node latNode = nl3.item(getNodeIndex(nl3, "lat"));
	            double lat = Double.parseDouble(latNode.getTextContent());
	            Node lngNode = nl3.item(getNodeIndex(nl3, "lng"));
	            double lng = Double.parseDouble(lngNode.getTextContent());
	            listGeopoints.add(new LatLng(lat, lng));

	            locationNode = nl2.item(getNodeIndex(nl2, "polyline"));
	            nl3 = locationNode.getChildNodes();
	            latNode = nl3.item(getNodeIndex(nl3, "points"));
	            ArrayList<LatLng> arr = decodePoly(latNode.getTextContent());
	            for (int j = 0; j < arr.size(); j++) {
	                listGeopoints.add(new LatLng(arr.get(j).latitude, arr
	                        .get(j).longitude));
	            }

	            locationNode = nl2.item(getNodeIndex(nl2, "end_location"));
	            nl3 = locationNode.getChildNodes();
	            latNode = nl3.item(getNodeIndex(nl3, "lat"));
	            lat = Double.parseDouble(latNode.getTextContent());
	            lngNode = nl3.item(getNodeIndex(nl3, "lng"));
	            lng = Double.parseDouble(lngNode.getTextContent());
	            listGeopoints.add(new LatLng(lat, lng));
	        }
	    }
	    return listGeopoints;
	}
	
	private int getNodeIndex(NodeList nl, String nodename) {
	    for (int i = 0; i < nl.getLength(); i++) {
	        if (nl.item(i).getNodeName().equals(nodename))
	            return i;
	    }
	    return -1;
	}
	
	private ArrayList<LatLng> decodePoly(String encoded) {
	    ArrayList<LatLng> poly = new ArrayList<LatLng>();
	    int index = 0, len = encoded.length();
	    int lat = 0, lng = 0;
	    while (index < len) {
	        int b, shift = 0, result = 0;
	        do {
	            b = encoded.charAt(index++) - 63;
	            result |= (b & 0x1f) << shift;
	            shift += 5;
	        } while (b >= 0x20);
	        int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
	        lat += dlat;
	        shift = 0;
	        result = 0;
	        do {
	            b = encoded.charAt(index++) - 63;
	            result |= (b & 0x1f) << shift;
	            shift += 5;
	        } while (b >= 0x20);
	        int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
	        lng += dlng;

	        LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
	        poly.add(position);
	    }
	    return poly;
	}

	/*The Method called to make this class asynchronous*/
	@Override
	protected String[] doInBackground(String... url) {
		InputStream is = null;
		String[] result = new String[3];
	      try {
	           
	            DefaultHttpClient httpClient = new DefaultHttpClient();
	            HttpContext localContext = new BasicHttpContext();
	            HttpPost httpPost = new HttpPost(url[0]);
	            
	            HttpResponse httpResponse = httpClient.execute(httpPost, localContext);
	            HttpEntity httpEntity = httpResponse.getEntity();
	            is = httpEntity.getContent();
	            try {
	            	
	            	DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	                Document doc = builder.parse(is);
	                list = getSteps(doc);
	                copyrights = getCopyRights(doc);
	                duration = getDuration(doc);
	                warnings = getWarnings(doc);
	                /*Setting the results to the result array*/
	                result[0] = duration;
	        		result[1] = warnings;
	        		result[2] = copyrights;
	               
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        } catch (ClientProtocolException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	      return result;

	}
	
	/*Replies to main activity*/
	@Override
    protected void onPostExecute(String[] result) {
		MainActivity.drawRoute(list);
		deliver.processFinish(result);
    }
	
	
	
}
