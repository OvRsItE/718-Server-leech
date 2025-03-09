package com.rs.game.world.entity.player.actions.CitadelActions;

import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.game.item.Item;
import com.rs.game.map.WorldObject;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.Action;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.player.content.social.citadel.Citadel;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.utility.Logger;

public final class CitadelSummoning extends Action {

	public static enum PouchDefinitions {

		Golden_Object(100, 20585, 20), Iron_Object(50, 20118, 10);

		private int delay;
		private int activatedId;
        private int xp;
		
		private PouchDefinitions(int delay, int activatedId, int xp) {
			this.delay = delay;
			this.activatedId = activatedId;
			this.xp = xp;
		}
	}

	private int pouchId;
	private WorldObject obelisk;
	private WorldObject temp;
	private PouchDefinitions def;

	public CitadelSummoning(WorldObject obelisk,
			int pouchId, PouchDefinitions def) {
		this.obelisk = obelisk;
		this.def = def;
		this.pouchId = pouchId;
	}

	@Override
	public boolean start(final Player player) {
		temp = new WorldObject(def.activatedId, obelisk.getType(), obelisk.getRotation(), obelisk.getX(), obelisk.getY(), obelisk.getZ());
		if (!checkAll(player))
			return false;
		if(World.getObject(temp) != temp)
		World.spawnTemporaryObject(temp, def.delay * 600, obelisk);
		player.savePouchId = pouchId;
		player.obelisk = obelisk;
		player.getInventory().deleteItem(new Item(pouchId));
		player.lockSwitch = true;
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					Citadel cit = new Citadel();
					cit.removeObjects.add(obelisk);
					temp = null;
					player.lockSwitch = false;	
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}

		}, def.delay * 600, TimeUnit.MILLISECONDS);
		return true;
	}
	
	private boolean checkAll(Player player) {
		if (!player.getInventory().hasFreeSlots()) {
			return false;
		}
		if (temp == null)
			return false;
		return true;
	}

	@Override
	public boolean process(Player player) {
		return checkAll(player);
	}

	@Override
	public int processWithDelay(Player player) {
		player.animate(new Animation(645));
		player.getSkills().addXp(Skills.SUMMONING, def.xp);
		player.getInventory().addItemMoneyPouch(new Item(pouchId));
		player.getPackets().sendGameMessage("You reach into the abyss and return the pouch you sacrificed.");
		return 5;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);
	}

}
