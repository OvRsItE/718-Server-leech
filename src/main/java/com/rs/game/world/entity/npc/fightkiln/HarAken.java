package com.rs.game.world.entity.npc.fightkiln;

import java.util.ArrayList;
import java.util.List;

import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.controller.impl.FightKiln;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;

@SuppressWarnings("serial")
public class HarAken extends NPC {

	private long time;
	private long spawnTentacleTime;
	private boolean underLava;
	private List<HarAkenTentacle> tentacles;
	
	private FightKiln controler;
	
	public void resetTimer() {
		underLava = !underLava;
		if(time == 0)
			spawnTentacleTime =  Utils.currentTimeMillis() + 9000;
		time = Utils.currentTimeMillis() + (underLava ? 45000 : 30000);
	}
	
	public HarAken(int id, Position tile, FightKiln controler) {
		super(id, tile, -1, 0, true, true);
		setForceMultiArea(true);
		this.controler = controler;
		tentacles = new ArrayList<HarAkenTentacle>();
	}
	
	
	@Override
	public void sendDeath(Entity source) {
		setNextGraphics(new Graphics(2924 + getSize()));
		if(time != 0) {
			removeTentacles();
			controler.removeNPC();
			time = 0;
		}
		super.sendDeath(source);
	}
	
	@Override
	public void processNPC() {
		if (isDead())
			return;
		cancelFaceEntityNoCheck();
	}
	
	public void process() {
		if (isDead())
			return;
		if(time != 0) {
			if(time < Utils.currentTimeMillis()) {
				if(underLava) {
					controler.showHarAken();
					resetTimer();
				}else
					controler.hideHarAken();
			}
			if(spawnTentacleTime < Utils.currentTimeMillis()) 
				spawnTentacle();
			
		}
	}
	
	public void spawnTentacle() {
		tentacles.add(new HarAkenTentacle(Utils.random(2) == 0 ? 15209 : 15210, controler.getTentacleTile(), this));
		spawnTentacleTime = Utils.currentTimeMillis() + Utils.random(15000, 25000);
	}

	public void removeTentacles() {
		for(HarAkenTentacle t : tentacles) 
			t.finish();
		tentacles.clear();
	}
	
	public void removeTentacle(HarAkenTentacle tentacle) {
		tentacles.remove(tentacle);
		
	}
	
	
}
