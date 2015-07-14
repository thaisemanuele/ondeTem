package com.icmc.ic.bixomaps;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;

/**
 * List of categories that are supposed to show on the drawer menu
 * Each name should be listed at res/values/strings.xml for the English Menu
 * and at res/values-pt-rBr/strings.xml for the Portuguese Menu
 *
 * @author Thais Santos
 * @version 1.0
 * @since February 03, 2015
 */


public class DrawerMenu {
	
	public ArrayList<String> getOptions(Context context){
		ArrayList<String> list = new ArrayList<String>();
		list.add(context.getString(R.string.category_car));
		list.add(context.getString(R.string.category_bank));
		list.add(context.getString(R.string.category_education));
		list.add(context.getString(R.string.category_medical));
		list.add(context.getString(R.string.category_emergency));
		list.add(context.getString(R.string.category_culture));
		list.add(context.getString(R.string.category_food));
		list.add(context.getString(R.string.category_government));
		list.add(context.getString(R.string.category_lodging));
		list.add(context.getString(R.string.category_recreation));
		list.add(context.getString(R.string.category_publicservices));
		list.add(context.getString(R.string.category_shops));
		list.add(context.getString(R.string.category_transport));
		list.add(context.getString(R.string.category_worship));
		list.add(context.getString(R.string.category_home));
		list.add(context.getString(R.string.category_beauty));
		list.add(context.getString(R.string.category_administrative));
		list.add(context.getString(R.string.category_animal));
		
		/*Sorting the list to display the elements in alphabetical order*/
		Collections.sort(list);
		return list;
	}

}
