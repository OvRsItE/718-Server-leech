package com.rs.game.world.entity.player.content;

import java.util.Calendar;

public class DoubleXpManager {

	public static int dayOfWeek() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DAY_OF_WEEK);
	}
	
	public static boolean isWeekend() {
		return dayOfWeek() == 1 ? true: 
			   dayOfWeek() == 6 ? true:
			   dayOfWeek() == 7 ? true: false;
	}
	
	public static boolean isDoubleDrops() {
		return 
			   dayOfWeek() == 5 ? true:
			   dayOfWeek() == 6 ? true: false;
	}
	
	public static boolean Monday() {
		return 
			   dayOfWeek() == 2 ? true: false;
	}
	
	public static boolean Tuesday() {
		return 
			   dayOfWeek() == 3 ? true: false;
	}
	
	public static boolean Wednesday() {
		return 
			   dayOfWeek() == 4 ? true: false;
	}
	
	public static boolean Thursday() {
		return 
			   dayOfWeek() == 5 ? true: false;
	}
	
	public static boolean Friday() {
		return 
			   dayOfWeek() == 6 ? true: false;
	}
	
	public static boolean Saturday() {
		return 
			   dayOfWeek() == 7 ? true: false;
	}
	
	public static boolean Sunday() {
		return 
			   dayOfWeek() == 1 ? true: false;
	}
	
	
}
