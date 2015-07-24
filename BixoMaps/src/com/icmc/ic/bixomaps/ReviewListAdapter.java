package com.icmc.ic.bixomaps;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * ReviewListAdapter
 * This class works as an adapter for the Review List,
 * changing the list items for an image followed by a comment
 *
 * @author Thais Santos
 * @version 1.0
 * @since March 24, 2015
 */
public class ReviewListAdapter extends ArrayAdapter<String> {

	private final Context context;
	private final ArrayList<String> list;
	private List<Review> revList = new ArrayList<Review>();
	

	/*This is the adapter constructor
	 * the lt contains the reviews, and the objects controls how many objects will be show*/
	public ReviewListAdapter(Context context, int resource, int textViewResourceId,
			List<Review> lt, List<String> objects) {
		super(context, resource, textViewResourceId, objects);
		this.context = context;
		this.list = (ArrayList<String>) objects;
		this.revList =  lt;
	}

	/*Need to Override the getView method to create a personalised view*/
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    View rowView = inflater.inflate(R.layout.review_row, parent, false);
		    TextView textView = (TextView) rowView.findViewById(R.id.rev_comment);
		    ImageView imageview = (ImageView)rowView.findViewById(R.id.star_image);
		    LinearLayout revRow = (LinearLayout)rowView.findViewById(R.id.rev_row);
		    String comm = new String();
		    /*if the review list is empty, display the no reviews message*/
		    if((revList.isEmpty())){
		    	comm = context.getString(R.string.no_reviews);
		    	textView.setText(comm);
			    chooseImage(imageview,-1.0f);
			}
		    /*Otherwise, display the comment*/
		    else{
		    	comm = revList.get(position).comment;
				   
			    String newLine = new String("\n");
			    if(comm.contains("\n"))	comm = comm.replace("\n", "");
			    if(comm.contains("\t"))	comm = comm.replace("\t", "");
			    comm = newLine.concat(comm);
			    textView.setText(comm);
			    chooseImage(imageview,revList.get(position).overallRating);
			   
		    }
		     return rowView;
	}	
	
	/*Choose the image to display within the review*/
	public static void chooseImage(ImageView imageView,Float f){
	
		if (f==1.0) {imageView.setImageResource(R.drawable.ic_rating1);
	    }else if (f==2.0) {imageView.setImageResource(R.drawable.ic_rating2);
	    }else if (f==3.0) {imageView.setImageResource(R.drawable.ic_rating3);
	    }else if (f==4.0) {imageView.setImageResource(R.drawable.ic_rating4);
	    }else if (f==5.0) {imageView.setImageResource(R.drawable.ic_rating5);
	    }else if (f==-1.0) {imageView.setImageResource(R.drawable.ic_sad_star);}
	
	}
	
}

