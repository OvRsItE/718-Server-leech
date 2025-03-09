package com.rs.game.world.entity.npc.familiar;

import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.skilling.Summoning.Pouches;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

public class Smokedevil extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6059371477618091701L;

	public Smokedevil(Player owner, Pouches pouch, Position tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

    @Override
    public String getSpecialName() {
	return "Dust Cloud";
    }

    @Override
    public String getSpecialDescription() {
	return "Hit up to 80 damage to all people within 1 square of you.";
    }

    @Override
    public int getBOBSize() {
	return 0;
    }

    @Override
    public int getSpecialAmount() {
	return 6;
    }

    @Override
    public SpecialAttack getSpecialAttack() {
	return SpecialAttack.ENTITY;
    }

    @Override
    public boolean submitSpecial(Object object) {
	getOwner().setNextGraphics(new Graphics(1316));
	getOwner().animate(new Animation(7660));
	animate(new Animation(7820));
	setNextGraphics(new Graphics(1470));
	for (Entity entity : this.getPossibleTargets()) {
	    if (entity == null || entity == getOwner() || !entity.withinDistance(this, 1))
		continue;
	    entity.applyHit(new Hit(this, Utils.random(80), HitLook.MAGIC_DAMAGE));
	}
	return true;
    }
}
