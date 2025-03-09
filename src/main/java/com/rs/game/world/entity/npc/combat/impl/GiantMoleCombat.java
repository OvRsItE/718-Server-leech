package com.rs.game.world.entity.npc.combat.impl;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;

public class GiantMoleCombat extends CombatScript {

	private static final Position[] COORDS =  {
		new Position(1737, 5228, 0),
		new Position(1751, 5233, 0),
		new Position(1778, 5237, 0),
		new Position(1736, 5227, 0),
		new Position(1780, 5152, 0),
		new Position(1758, 5162, 0),
		new Position(1745, 5169, 0),
		new Position(1760, 5183, 0)
	};
	@Override
	public Object[] getKeys() {
		return new Object[] { 3340 };
	}

	@Override
	public int attack(final NPC npc, Entity target) {
		NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if(Utils.random(5) == 0 && npc.getHitpoints() < 1000) { //bury
			npc.animate(new Animation(3314));
			npc.setCantInteract(true);
			npc.getCombat().removeTarget();
			final Player player = (Player) (target instanceof  Player ? target : null);
			if(player != null)
				player.getInterfaceManager().sendTab(player.getInterfaceManager().hasRezizableScreen() ? 1 : 11, 226);		
			final Position middle = npc.getMiddleWorldTile();
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					if(player != null)
						player.getPackets().closeInterface(player.getInterfaceManager().hasRezizableScreen() ? 1 : 11);
					npc.setCantInteract(false);
					if(npc.isDead())
						return;
					World.sendGraphics(npc, new Graphics(572), middle);
					World.sendGraphics(npc, new Graphics(571), new Position(middle.getX(), middle.getY()-1, middle.getZ()));
					World.sendGraphics(npc, new Graphics(571), new Position(middle.getX(), middle.getY()+1, middle.getZ()));
					World.sendGraphics(npc, new Graphics(571), new Position(middle.getX()-1, middle.getY()-1, middle.getZ()));
					World.sendGraphics(npc, new Graphics(571), new Position(middle.getX()-1, middle.getY()+1, middle.getZ()));
					World.sendGraphics(npc, new Graphics(571), new Position(middle.getX()+1, middle.getY()-1, middle.getZ()));
					World.sendGraphics(npc, new Graphics(571), new Position(middle.getX()+1, middle.getY()+1, middle.getZ()));
					World.sendGraphics(npc, new Graphics(571), new Position(middle.getX()-1, middle.getY(), middle.getZ()));
					World.sendGraphics(npc, new Graphics(571), new Position(middle.getX()+1, middle.getY(), middle.getZ()));
					npc.setNextPosition(new Position(COORDS[Utils.random(COORDS.length)]));
					npc.animate(new Animation(3315));
					
				}
				
			}, 2);

		}else{
			npc.animate(new Animation(defs.getAttackEmote()));
			delayHit(npc, 0, target, getMeleeHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MELEE, target)));
		}
		return defs.getAttackDelay();
	}

}
