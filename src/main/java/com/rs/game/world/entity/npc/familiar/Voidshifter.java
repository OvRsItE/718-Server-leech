package com.rs.game.world.entity.npc.familiar;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.skilling.Summoning.Pouches;
import com.rs.game.world.entity.player.content.skills.magic.Magic;

public class Voidshifter extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2825822265261250357L;

	public Voidshifter(Player owner, Pouches pouch, Position tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

    @Override
    public String getSpecialName() {
	return "Call To Arms";
    }

    @Override
    public String getSpecialDescription() {
	return "Teleports the player to Void Outpost.";
    }

    @Override
    public int getBOBSize() {
	return 0;
    }

    @Override
    public int getSpecialAmount() {
	return 4;
    }

    @Override
    public SpecialAttack getSpecialAttack() {
	return SpecialAttack.CLICK;
    }

    @Override
    public boolean submitSpecial(Object object) {
	Magic.sendTeleportSpell((Player) object, 14388, -1, 1503, 1502, 0, 0, new Position(2662, 2649, 0), 3, true, Magic.OBJECT_TELEPORT);
	return true;
    }
}