/**
 * 
 */
package com.rs.mysql.impl;

import com.rs.mysql.Database;

/**
 * @author ReverendDread
 * Sep 28, 2018
 */
public class Donations implements Runnable {

	private static final String HOST = "https://www.valius.net/", USERNAME = "", PASSWORD = "", DATABASE = "";
	
	@Override
	public void run() {
		
		/**
		 * Create the database object.
		 */
		Database db = new Database(HOST, USERNAME, PASSWORD, DATABASE);
		
		try {
			
			/**
			 * Attempt to connect to the database.
			 */
			if (!db.init())
				return;
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

}
