package com.rs.game.world.entity.npc.others;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;

@SuppressWarnings("serial")
public class MutatedZygomites extends ConditionalDeath {

	boolean lvl74;

	public MutatedZygomites(int id, Position tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(7421, null, false, id, tile, mapAreaNameHash, true);
		this.lvl74 = id == 3344;
	}

	@Override
	public void processNPC() {
		super.processNPC();
		if (!getCombat().process())
			resetNPC();
	}

	@Override
	public void sendDeath(final Entity source) {
		super.sendDeath(source);
		if (getHitpoints() != 1)
			resetNPC();
	}

	private void resetNPC() {
		setNPC(lvl74 ? 3344 : 3345);
		setNextNPCTransformation(lvl74 ? 3344 : 3345);
	}

	public static void transform(final Player player, final NPC npc) {
		player.animate(new Animation(2988));
		npc.setNextNPCTransformation(npc.getId() + 2);
		npc.setNPC(npc.getId() + 2);
		npc.animate(new Animation(2982));
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				npc.getCombat().setTarget(player);
				npc.setCantInteract(false);
			}
		});
	}
}
