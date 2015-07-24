package com.icmc.ic.bixomaps;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import android.os.Environment;
import android.util.Xml;

/**
 * XmlHandler
 * This class creates a folder on the user's SD card,
 * Get reviews from a given XML, and write XML files
 * 
 * @author Thais Santos
 * @version 1.0
 * @since February 24, 2015
 */
public class XmlHandler {
	
	/*Folder's name on SD CARD */
	public static String BM_TEST = "OndeTem";
	
	
	/*Get information from Xml*/
	public Recommendation getInformation(){
		Document document = null;
		Recommendation rec = new Recommendation();
		
		try {
			
		    DocumentBuilderFactory factory = 
		    DocumentBuilderFactory.newInstance();
		    DocumentBuilder builder = factory.newDocumentBuilder();

		    File reply = new File(getDir().getPath()+File.separator+"reply.xml");
		    
		    document = builder.parse(reply);
		    NodeList listOfRec = document.getElementsByTagName("place");
		   /*Create a Place object and fill it with with the attributes from the node*/
		    for(int i=0; i<listOfRec.getLength();i++){
		    	Node placeNode = listOfRec.item(i);
				Element e = (Element) placeNode;
				Place p = new Place();
				p.setAddress(e.getAttribute("address"));
				p.setCategory(e.getAttribute("category"));
				p.setIcon(e.getAttribute("icon"));
				p.setId(e.getAttribute("id"));
				p.setPhone(e.getAttribute("phone"));
				Double lat = Double.parseDouble(e.getAttribute("lat"));
				p.setLatitude(lat);
				Double lon = Double.parseDouble(e.getAttribute("long"));
				p.setLongitude(lon);
				p.setName(e.getAttribute("name"));
				Float rating = Float.parseFloat(e.getAttribute("rating"));
				p.setRating(rating);
				p.setUrl(e.getAttribute("url"));
				p.setWebsite(e.getAttribute("website"));
				/*Fill up the arrayList of Reviews for that place*/
				p.setReviews(getReviewsFromDoc(placeNode)); 
					rec.places.add(p);
				}

		    if(listOfRec.getLength()==0){
		    	return null;
		    }
		}
		catch (FactoryConfigurationError e) {
		    // unable to get a document builder factory
		} 
		catch (ParserConfigurationException e) {
		    // parser was unable to be configured
			}
		catch (SAXException e) {
		    // parsing error
		} 
		catch (IOException e) {
		    // i/o error
		}
		return rec;
		
	}
	
	/*Getting the External storage address*/
	public File getDir() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            String path = Environment.getExternalStorageDirectory().getPath() + File.separator + BM_TEST;
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdir();
            }
            return dir;
        }
        return null;
    }
	
	/*Get the reviews from an XML Document*/
	private ArrayList<Review> getReviewsFromDoc(Node node){
		String test = new String();
		ArrayList<Review> rev = new ArrayList<Review>();
		NodeList gchildren = null;
		NodeList children = node.getChildNodes();
		for(int i=0; i<children.getLength();i++){
			test = children.item(i).getNodeName();
			if(test.equalsIgnoreCase("reviews")){
				gchildren = children.item(i).getChildNodes();
				break;
			}
			
		}
		if(gchildren!=null){
			for(int i=0; i<gchildren.getLength();i++){
				test = gchildren.item(i).getNodeName();
				if(test.equals("review")){
					Node reviewNode = gchildren.item(i);
					Element e = (Element) reviewNode;
					Review r = new Review();
					r.setId(e.getAttribute("id"));
					r.setTime(e.getAttribute("time"));
					r.setLanguage(e.getAttribute("language"));
					r.setComment(e.getTextContent());
					r.setOverallRating(Float.parseFloat(e.getAttribute("overall_rating")));
					rev.add(r);
				}
			}
		}
				
		return rev;
	}
	
	/*Writes a recommendation to an XML*/
	protected  void writeRecommendation(Request req){
		XmlSerializer serializer = Xml.newSerializer();
	    StringWriter writer = new StringWriter();
	    try {
	        serializer.setOutput(writer);
	        serializer.startDocument("UTF-8",null);
	        serializer.startTag("", "message");
	        serializer.startTag("", "recommend");
	        serializer.startTag("", "user");
	        serializer.attribute("", "id", req.getUserid().toString());
	        serializer.attribute("", "date", req.getDate().toString());
	        serializer.attribute("", "lat", req.getLat().toString());
	        serializer.attribute("", "long", req.getLon().toString());
	        serializer.startTag("", "profile");
	        serializer.startTag("", "preference");
	        serializer.text("");
	        serializer.endTag("", "preference");
	        serializer.endTag("", "profile");
	        serializer.endTag("", "user");
	        serializer.startTag("", "place");
	        serializer.attribute("", "category", req.getCategory());
	        serializer.endTag("", "place");
	        serializer.endTag("", "recommend");
	        serializer.endTag("", "message");
	        serializer.endDocument();
	        File request = new File(getDir().getPath()+File.separator+"recommend.xml");
	        FileWriter wr = new FileWriter(request);
	        wr.append(writer.toString());
	        wr.close();
	        
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    } 
	}
	
	/*Writes a notification to an XML*/
	public void writeNotification(Notification note){

		XmlSerializer serializer = Xml.newSerializer();
	    StringWriter writer = new StringWriter();
	    try {
	        serializer.setOutput(writer);
	        serializer.startDocument("UTF-8",null);
	        serializer.startTag("", "message");
	        serializer.startTag("", "notify");
	        serializer.startTag("", "event");
	        serializer.attribute("", "type", note.getType());
	        serializer.endTag("", "event");
	        serializer.startTag("", "user");
	        serializer.attribute("", "id", note.getUserId().toString());
	        serializer.attribute("", "date", note.getDate().toString());
	        serializer.attribute("", "lat", note.getLat().toString());
	        serializer.attribute("", "lon", note.getLon().toString());
	        serializer.endTag("","user");
	        serializer.startTag("", "place");
	        serializer.attribute("", "id", note.getPlace().getId());
	        serializer.attribute("", "category", note.getPlace().getCategory());
	        serializer.startTag("", "review");
	        serializer.attribute("", "language", note.getRev().getLanguage());
	        serializer.attribute("", "rating",note.getRev().getOverallRating().toString());
	        serializer.text(note.getRev().getComment());
	        serializer.endTag("", "review");
	        serializer.endTag("", "place");
	        serializer.endTag("", "notify");
	        serializer.endTag("", "message");
	        serializer.endDocument();
	        File request = new File(getDir().getPath()+File.separator+"notify.xml");
	        FileWriter wr = new FileWriter(request);
	        wr.append(writer.toString());
	        wr.close();
	        
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    } 

	}
	
	/*Add to the XML the values according to the user's settings */
	protected  void writeSettings(String place, String num){
		XmlSerializer serializer = Xml.newSerializer();
	    StringWriter writer = new StringWriter();
	    try {
	        serializer.setOutput(writer);
	        serializer.startDocument("UTF-8",null);
	        serializer.startTag("", "config");
	        serializer.startTag("", "options");
	        serializer.startTag("", "option");
	        serializer.attribute("", "name", "city");
	        serializer.attribute("", "value", place);
	        serializer.endTag("", "option");
	        serializer.startTag("", "option");
	        serializer.attribute("", "name", "number");
	        serializer.attribute("", "value", num);
	        serializer.endTag("", "option");
	        serializer.endDocument();
	        File request = new File(getDir().getPath()+File.separator+"config.xml");
	        FileWriter wr = new FileWriter(request);
	        wr.append(writer.toString());
	        wr.close();
	        
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    } 
	}

			
	/*Creates a new XML document*/
	private static Document getDocument(String fileName){
		
		try {
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringComments(true);
			factory.setIgnoringElementContentWhitespace(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			return builder.parse(new InputSource(fileName));
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	//Get the XML data for the recommendation request
	public String createRecommendationRequestXMLdata(Request req){
		
		XmlSerializer serializer = Xml.newSerializer();
	    StringWriter writer = new StringWriter();
	    try {
	        serializer.setOutput(writer);
	        serializer.startDocument("UTF-8",null);
	        serializer.startTag("", "message");
	        serializer.startTag("", "recommend");
	        serializer.startTag("", "user");
	        serializer.attribute("", "id", req.getUserid().toString());
	        serializer.attribute("", "date", req.getDate().toString());
	        serializer.attribute("", "lat", req.getLat().toString());
	        serializer.attribute("", "long", req.getLon().toString());
	        serializer.startTag("", "profile");
	        serializer.startTag("", "preference");
	        serializer.text("");
	        serializer.endTag("", "preference");
	        serializer.endTag("", "profile");
	        serializer.endTag("", "user");
	        serializer.startTag("", "place");
	        serializer.attribute("", "category", req.getCategory());
	        serializer.endTag("", "place");
	        serializer.endTag("", "recommend");
	        serializer.endTag("", "message");
	        serializer.endDocument();
	        
	        return writer.toString(); 
	        
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    } 
	}
	
	//Get the XML data for the recommendation response
	public Recommendation getRecommendationFromResponseXMLdata(String xml){
		
		Document document = null;
		Recommendation rec = new Recommendation();

		try {
			
		    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder builder = factory.newDocumentBuilder();
		    InputSource is = new InputSource(new StringReader(xml));
		    document = builder.parse(is);

		    NodeList listOfRec = document.getElementsByTagName("place");
		    //NodeList listOfRev = document.getChildNodes();
		    
		   /*Create a Place object and fill it with with the attributes from the node*/
		    for(int i=0; i<listOfRec.getLength();i++){
		    	Node placeNode = listOfRec.item(i);
				Element e = (Element) placeNode;
				Place p = new Place();
				p.setAddress(e.getAttribute("address"));
				p.setCategory(e.getAttribute("category"));
				p.setIcon(e.getAttribute("icon"));
				p.setId(e.getAttribute("id"));
				p.setPhone(e.getAttribute("phone"));
				Double lat = Double.parseDouble(e.getAttribute("lat"));
				p.setLatitude(lat);
				Double lon = Double.parseDouble(e.getAttribute("long"));
				p.setLongitude(lon);
				p.setName(e.getAttribute("name"));
				Float rating = Float.parseFloat(e.getAttribute("rating"));
				p.setRating(rating);
				p.setUrl(e.getAttribute("url"));
				p.setWebsite(e.getAttribute("website"));
				/*Fill up the arrayList of Reviews for that place*/
				p.setReviews(getReviewsFromDoc(placeNode));
					rec.places.add(p);
			}
		} catch (FactoryConfigurationError e) {
		    // unable to get a document builder factory
		} catch (ParserConfigurationException e) {
		    // parser was unable to be configured
		} catch (SAXException e) {
		    // parsing error
		} catch (IOException e) {
		    // i/o error
		}
		
		return rec;
	}

	//Get the XML data for the event notification
	public String createEventNotificationXMLdata(Notification note){

		XmlSerializer serializer = Xml.newSerializer();
	    StringWriter writer = new StringWriter();
	    try {
	        serializer.setOutput(writer);
	        serializer.startDocument("UTF-8",null);
	        serializer.startTag("", "message");
	        serializer.startTag("", "notify");
	        serializer.startTag("", "event");
	        serializer.attribute("", "type", note.getType());
	        serializer.endTag("", "event");
	        serializer.startTag("", "user");
	        serializer.attribute("", "id", note.getUserId().toString());
	        serializer.attribute("", "date", note.getDate().toString());
	        serializer.attribute("", "lat", note.getLat().toString());
	        serializer.attribute("", "long", note.getLon().toString());
	        serializer.endTag("","user");
	        serializer.startTag("", "place");
	        serializer.attribute("", "id", note.getPlace().getId());
	        serializer.attribute("", "category", note.getPlace().getCategory());
	        serializer.startTag("", "review");
	        serializer.attribute("", "language", note.getRev().getLanguage());
	        serializer.attribute("", "rating",note.getRev().getOverallRating().toString());
	        serializer.text(note.getRev().getComment());
	        serializer.endTag("", "review");
	        serializer.endTag("", "place");
	        serializer.endTag("", "notify");
	        serializer.endTag("", "message");
	        serializer.endDocument();
	        
	        return writer.toString(); 
	        
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    } 

	}
}

