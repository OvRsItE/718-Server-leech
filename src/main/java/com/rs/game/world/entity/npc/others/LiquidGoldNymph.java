package com.rs.game.world.entity.npc.others;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.utility.Utils;

@SuppressWarnings("serial")
public class LiquidGoldNymph extends NPC {
	
	private static final int[] GOLD = { 20787, 20788, 20789, 20790, 20791};

	private Player target;
	private long createTime;
	
	public LiquidGoldNymph(Position tile, Player target) {
		super(14, tile, -1, 0, true, true);
		this.target = target;
		createTime = Utils.currentTimeMillis();
	}
	
	@Override
	public void processNPC() {
		if(target.isFinished() || createTime + 60000 < Utils.currentTimeMillis()) 
			finish();
	}
	
	public void giveReward(final Player player) {
		if(player != target || player.isLocked())
			return;
		player.lock();
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.unlock();
			    player.getInventory().addItem(GOLD[Utils.random(GOLD.length - 1)], 1);
				player.getPackets().sendGameMessage("The Liquid Gold Nymph gave you a reward to say thank you.");
				finish();
			}
			
		}, 1);
	}
	
	@Override
	public boolean withinDistance(Player tile, int distance) {
		return tile == target && super.withinDistance(tile, distance);
	}

}
