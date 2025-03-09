package com.rs.game.world.entity.npc.combat.impl;
/*
import com.rs.game.World;
import com.rs.game.entity.Entity;
import com.rs.game.entity.player.Player;
import com.rs.game.entity.updating.impl.Animation;
import com.rs.game.entity.updating.impl.ForceTalk;
import com.rs.game.entity.updating.impl.Graphics;
import com.rs.game.entity.updating.impl.Hit;
import com.rs.game.entity.updating.impl.Hit.HitLook;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.npc.slayer.GanodermicBeast;
import com.rs.utility.Utils;



@SuppressWarnings("unused")
public class GanodermicCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 14696, 14697 };
	}

	@Override
	public int attack(NPC n, Entity target) {
		GanodermicBeast npc = (GanodermicBeast) n;
		NPCCombatDefinitions defs = npc.getCombatDefinitions();
		Player player = (Player) target;
		if (player.withinDistance(npc, 3)) {
			delayHit(
					npc,
					1,
					target,
					getMagicHit(
							npc,
							getRandomMaxHit(npc, 400,
									NPCCombatDefinitions.MAGE, target)));
			npc.setNextAnimation(new Animation(15470));
			npc.setNextGraphics(new Graphics(2034));
			World.sendProjectile(npc, target, 2034, 10, 18, 50, 50, 0, 0);
			return defs.getAttackDelay();
		} else {
			delayHit(
					npc,
					1,
					target,
					getMagicHit(
							npc,
							getRandomMaxHit(npc, 400,
									NPCCombatDefinitions.MAGE, target)));
			npc.setNextAnimation(new Animation(15470));
			npc.setNextGraphics(new Graphics(2034));
			World.sendProjectile(npc, target, 2034, 10, 18, 50, 50, 0, 0);
		}
		return defs.getAttackDelay();
	}

}
*/