package com.rs.game.world.entity.npc.others;

import java.util.Random;

import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;

@SuppressWarnings("serial")
public class Wolverine extends NPC {

	public Wolverine(Player target, int id, Position tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		setCombatLevel(target.getSkills().getCombatLevel() + new Random().nextInt(100) + 100);
		int hitpoints = 1000 + this.getCombatLevel() + target.getSkills().getCombatLevel() / 2 + new Random().nextInt(10);
		super.getCombatDefinitions().setHitpoints(hitpoints);
		setHitpoints(hitpoints);
		setRandomWalk(true);
		setForceAgressive(true);
		setAttackedBy(target);
		setAtMultiArea(true);
		faceEntity(target);
	}
}