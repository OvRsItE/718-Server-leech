package com.rs.game.world.entity.npc.others;

import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.controller.impl.RunespanControler;
import com.rs.utility.Utils;

@SuppressWarnings("serial")
public class YellowWizard extends NPC {
	
	private RunespanControler controler;
	private long spawnTime;
	public YellowWizard(Position tile, RunespanControler controler) {
		super(15430, tile, -1, 0, true, true);
		spawnTime = Utils.currentTimeMillis();
		this.controler = controler;
	}
	
	@Override
	public void processNPC() {
		if(spawnTime + 300000 < Utils.currentTimeMillis()) 
			finish();
	}
	
	@Override
	public void finish() {
		controler.removeWizard();
		super.finish();
	}
	public static void giveReward(Player player) {
		
	}
	@Override
	public boolean withinDistance(Player tile, int distance) {
		return tile == controler.getPlayer() && super.withinDistance(tile, distance);
	}

}
