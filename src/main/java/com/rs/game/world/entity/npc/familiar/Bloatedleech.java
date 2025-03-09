package com.rs.game.world.entity.npc.familiar;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.skilling.Summoning.Pouches;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

public class Bloatedleech extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6059371477618091701L;

	public Bloatedleech(Player owner, Pouches pouch, Position tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

    @Override
    public String getSpecialName() {
	return "Blood Drain";
    }

    @Override
    public String getSpecialDescription() {
	return "Heals stat damage, poison, and disease but sacrifices some life points.";
    }

    @Override
    public int getBOBSize() {
	return 0;
    }

    @Override
    public int getSpecialAmount() {
	return 5;
    }

    @Override
    public SpecialAttack getSpecialAttack() {
	return SpecialAttack.CLICK;
    }

    @Override
    public boolean submitSpecial(Object object) {
	Player player = (Player) object;
	final int damage = Utils.random(100) + 50;
	if (player.getHitpoints() - damage <= 0) {
	    player.getPackets().sendGameMessage("You don't have enough life points to use this special.");
	    return false;
	}
	if (player.getToxin().poisoned())
	    player.getToxin().reset();
	player.getSkills().restoreSkills();
	player.applyHit(new Hit(player, damage, HitLook.DESEASE_DAMAGE));
	setNextGraphics(new Graphics(1419));
	player.setNextGraphics(new Graphics(1420));
	return true;
    }
}
