package com.icmc.ic.bixomaps;
/**
 * RatingActivity
 * This is the activity for adding a review to a place
 *
 * @author Thais Santos
 * @version 1.0
 * @since February 24, 2015
 */
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.R.color;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class RatingActivity extends Activity{
	
	String placeName;
	String language = new String("pt");
	static Place p = new Place();
	static String notifyXML;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		
		/*Setting the content view for this activity*/
		setContentView(R.layout.activity_rating);
		
		/*getting the place name from the extras added on the Main activity*/
		placeName = extras.getString("mk");
		
		/*Getting the components*/
		TextView title = (TextView)findViewById(R.id.review_title);
		final TextView address = (TextView)findViewById(R.id.rev_address);
		TextView phone = (TextView)findViewById(R.id.rev_phone);
		
		/*Setting the components values*/
		title.setText(placeName);
		address.setText(MainActivity.getPlaceAdress(placeName));
		phone.setText(MainActivity.getPlacePhone(placeName));
		
		/*Getting the components*/
		final EditText comment = (EditText)findViewById(R.id.review_comment);
		final RatingBar rb = (RatingBar)findViewById(R.id.review_ratingBar);
		final Button submit = (Button)findViewById(R.id.review_submit);
		final Button cancel = (Button)findViewById(R.id.review_cancel);
		
		/*Setting the rating bar*/
		rb.setRating(1.0f);
		rb.setStepSize(1.0f);
		
		EditText editReview = (EditText)findViewById(R.id.review_comment);
		editReview.setHorizontallyScrolling(false);
		editReview.setMaxLines(8);
		/*Changing the Keyboard confirm button to work as a submit button */
		editReview.setOnEditorActionListener(new OnEditorActionListener() {
		
			@Override
			public boolean onEditorAction(TextView arg0, int actionId, KeyEvent arg2) {
				if(actionId==EditorInfo.IME_ACTION_GO){
					submit.performClick(); /* Force a click on the submit button*/
					return true;
				}
				return false;
			}
		});
		
		/*listener for the submit button*/
		submit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				final Review rev = new Review();
				rev.setLanguage(language);
				/*TODO
				 * Change Overall rating according to rating bar
				 * */
				rev.setOverallRating(rb.getRating());
				Calendar calendar = Calendar.getInstance();
				Date d = calendar.getTime();
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
				rev.setTime(sdf.format(d));
				rev.setComment(comment.getText().toString());
				
				p = MainActivity.getPlace(placeName);
				/*Displaying alert of confirmation*/
				new AlertDialog.Builder(RatingActivity.this)
			    .setTitle(RatingActivity.this.getString(R.string.confirm_submit))
			    .setMessage(rev.getComment())
			    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) {
			        	RatingActivity.notifyXML = RequestHandler.getEventNotification(p, "review", MainActivity.myLocation.getLatitude(), MainActivity.myLocation.getLatitude(), rev, getApplicationContext());
			        	new ReviewEventSend().execute(RatingActivity.this);
			        	
			        	hideSoftKeyboard(getWindow().getDecorView().findViewById(android.R.id.content));
			            finish();
			        }
			     })
			    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			            // do nothing
			        }
			     })
			    .setIcon(chooseIcon(rb.getRating()))
			     .show();
			}
			
		});
		
		/*Listener for the cancel button*/
		cancel.setOnClickListener(new OnClickListener(){
			/*Hide the keyboard and finish the activity*/
			@Override
			public void onClick(View v) {
				hideSoftKeyboard(getWindow().getDecorView().findViewById(android.R.id.content));
	            finish();	
			}
			
		});//END of the cancel button listener
		
	}//END of the onCreate Method

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.rating, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
	    if(id==R.id.open_about){
        	openAbout();
            return true;
	    }
	    
	    //TODO allow settings to the user
	   /** if(id==R.id.open_settings){
	    	showSettings();
        	return true;
	    }*/
	    return super.onOptionsItemSelected(item);
	}
	
	/*hide the soft keyboard*/
	public void hideSoftKeyboard(View view){
		  InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		  imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
	
	/*Chose an icon according to the rating value*/
	public int chooseIcon(Float f){
		if(f==1.0f){ return getResources().getIdentifier("ic_rating1", "drawable", "com.icmc.ic.bixomaps");}
		if(f==2.0f){ return getResources().getIdentifier("ic_rating2", "drawable", "com.icmc.ic.bixomaps");}
		if(f==3.0f){ return getResources().getIdentifier("ic_rating3", "drawable", "com.icmc.ic.bixomaps");}
		if(f==4.0f){ return getResources().getIdentifier("ic_rating4", "drawable", "com.icmc.ic.bixomaps");}
		if(f==5.0f){ return getResources().getIdentifier("ic_rating5", "drawable", "com.icmc.ic.bixomaps");}
		return android.R.drawable.ic_dialog_alert;
	}
	
	/*Open the about dialog*/
	public void openAbout(){
		AlertDialog about = new AlertDialog.Builder(RatingActivity.this)
	    .setTitle(RatingActivity.this.getString(R.string.action_about))
	    .setMessage(""+RatingActivity.this.getString(R.string.about_message))
	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) {
	        	// do nothing
	        }
	     })
	    .create();
	    if(!about.isShowing()){
	    	about.show();
	    }
	}
}
