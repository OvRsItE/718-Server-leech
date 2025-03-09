package com.rs.game.world.entity.updating.impl;

import com.rs.Constants;

public final class Animation {

	private int[] ids;
	private int delay;

	public Animation(int id) { 
		this(id, 0);
	}

	public Animation(int id, int speed) {
		this(id, id, id, id, speed);
	}

	public Animation(int id1, int id2, int id3, int id4, int speed) {
		this.ids = new int[] { id1, id2, id3, id4 };
		this.delay = speed;
	}
	
	public static Animation createOSRS(int id) {
		return createOSRS(id, 0);
	}
	
	public static Animation createOSRS(int id, int speed) {
		return new Animation(id == -1 ? -1 : id + Constants.OSRS_SEQ_OFFSET, speed);
	}

	public int[] getIds() {
		return ids;
	}

	public int getId() {
		return ids[0];
	}

	public int getDelay() {
		return delay;
	}
}
