package me.XP1024.LocalAdmin.Client;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Localisation {
		
	private static ResourceBundle res;
	
	private static void init(){
		if(res == null)
			load();
	}
	
	public static void load(){
		load("deutsch");
	}
	
	public static void load(String language){
		res = ResourceBundle.getBundle(language);
	}
	
	public static String getString(String key){
		init();
		try{
			return res.getString(key);	
		} catch(MissingResourceException e){
			return "#Missing translation#";
		}
			
	}

}
