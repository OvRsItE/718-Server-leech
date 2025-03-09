package com.rs.game.world.entity.npc;

public class TimerBar {

	private int timeToShow;
	private int startHp;
	private int endHp;

	public TimerBar(int timeToShow, int startHp, int endHp) {
		this.timeToShow = timeToShow;
		this.startHp = startHp;
		this.endHp = endHp;
	}

	public TimerBar(int time) {
		this.timeToShow = 5;
		this.startHp = 1;
		this.endHp = time;
	}

	public int getEndHp() {
		return endHp;
	}

	public void setEndHp(int endHp) {
		this.endHp = endHp;
	}

	public int getStartHp() {
		return startHp;
	}

	public void setStartHp(int startHp) {
		this.startHp = startHp;
	}

	public int getTimeToShow() {
		return timeToShow;
	}

	public void setTimeToShow(int timeToShow) {
		this.timeToShow = timeToShow;
	}

}
