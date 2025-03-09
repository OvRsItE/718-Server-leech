package com.rs.game.world.entity.npc.ents;


import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.updating.impl.Animation;


public class EntsNPC extends NPC {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1698918474885907284L;

	
	public EntsNPC(int id, Position tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, 0, canBeAttackFromOutOfArea, spawned);
	}
	
	
	@Override
	public void sendDeath(final Entity source) {
		NPCCombatDefinitions defs = getCombatDefinitions();
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					animate(new Animation(defs.getDeathEmote()));
				} else if (loop >= defs.getDeathDelay() && getId() != 26595) {
					transformIntoNPC(26595);
					setNextFaceEntity(null);
				} else if (loop >= 300) {
					finish();
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}
	
}
