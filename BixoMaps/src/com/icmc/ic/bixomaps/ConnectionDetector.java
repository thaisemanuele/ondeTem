package com.icmc.ic.bixomaps;
/**
 * This class returns true when the device is anyway connected to the web,
 * returns false otherwise
 *
 * @author Thais Santos
 * @version 1.0
 * @since January 23, 2015
 */
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {
	
	private Context context;
	
	public ConnectionDetector(Context context){
		this.context = context;
	}
	
	public Boolean isConnected(){
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(!(cm==null)){
			NetworkInfo[] netInfo = cm.getAllNetworkInfo();
			if(!(netInfo==null)){
				for(NetworkInfo n:netInfo){
					if(n.getState()==NetworkInfo.State.CONNECTED)	return true;
				}
			}
		}
		return false;
	}

}
