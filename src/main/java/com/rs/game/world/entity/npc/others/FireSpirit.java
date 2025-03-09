package com.rs.game.world.entity.npc.others;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.item.Item;
import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.utility.Utils;

@SuppressWarnings("serial")
public class FireSpirit extends NPC {

	private Player target;
	private long createTime;
	
	public FireSpirit(Position tile, Player target) {
		super(15451, tile, -1, 0, true, true);
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
		player.animate(new Animation(16705));
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.unlock();
				player.getInventory().addItem(new Item(12158, Utils.random(1, 6)));
				player.getInventory().addItem(new Item(12159, Utils.random(1, 6)));
				player.getInventory().addItem(new Item(12160, Utils.random(1, 6)));
				player.getInventory().addItem(new Item(12163, Utils.random(1, 6)));
				player.getPackets().sendGameMessage("The fire spirit gives you a reward to say thank you for freeing it, before disappearing.");
				finish();
				
			}
			
		}, 2);
	}
	
	@Override
	public boolean withinDistance(Player tile, int distance) {
		return tile == target && super.withinDistance(tile, distance);
	}

}
