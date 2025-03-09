package com.rs.game.world.entity.npc.others;

import java.util.concurrent.TimeUnit;

import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.controller.impl.DarkInvasion;
import com.rs.utility.ChatColors;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2012-10-26
 */
@SuppressWarnings("serial")
public class DIZombie extends NPC {

	public DIZombie(int id, Position tile, int mapAreaNameHash,
                    boolean canBeAttackFromOutOfArea) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		this.setForceMultiArea(true);
		this.setSpawned(true);
		setBonuses(new int[10]);
		for (int i = 0; i < getBonuses().length; i++) {
			this.getBonuses()[i] = this.getCombatLevel();
		}
	}

	@Override
	public void sendDeath(Entity source) {
		super.sendDeath(source);
		if (source instanceof Player) {
			Player killer = (Player) source;
			if (killer.getControlerManager().getControler() instanceof DarkInvasion) {
				((DarkInvasion) killer.getControlerManager().getControler()).getMonsters().remove(this);
				((DarkInvasion) killer.getControlerManager().getControler()).addKills();
				((DarkInvasion) killer.getControlerManager().getControler()).addTime(TimeUnit.SECONDS.toMillis(5));
				killer.sendMessage("<col=" + ChatColors.BLUE + ">One of " + ((DarkInvasion) killer.getControlerManager().getControler()).getZombieCount() + " zombies have died and you gain 1 second on the timer.");
			}
		}
	}

}
