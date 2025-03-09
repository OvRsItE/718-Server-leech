package com.rs.game.world.entity.npc.familiar;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.skilling.Summoning.Pouches;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

public class Prayingmantis extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6059371477618091701L;

	public Prayingmantis(Player owner, Pouches pouch, Position tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

    @Override
    public String getSpecialName() {
	return "Mantis Strike";
    }

    @Override
    public String getSpecialDescription() {
	return "Uses a magic based attack (max hit 170) which always drains the opponent's prayer and binds if it deals any damage.";
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
	final Entity target = (Entity) object;
	getOwner().setNextGraphics(new Graphics(1316));
	getOwner().animate(new Animation(7660));
	animate(new Animation(8071));
	setNextGraphics(new Graphics(1422));
	final int hitDamage = Utils.random(170);
	if (hitDamage > 0) {
	    if (target instanceof Player)
		((Player) target).getPrayer().drainPrayer(hitDamage);
	}
	WorldTasksManager.schedule(new WorldTask() {

	    @Override
	    public void run() {
		target.setNextGraphics(new Graphics(1423));
		target.applyHit(new Hit(getOwner(), hitDamage, HitLook.MAGIC_DAMAGE));
	    }
	}, 2);
	return true;
    }
}
