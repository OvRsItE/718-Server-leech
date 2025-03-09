package com.rs.game.world.entity.npc.familiar;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.skilling.Summoning.Pouches;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

public class Arcticbear extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6059371477618091701L;

	public Arcticbear(Player owner, Pouches pouch, Position tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	   @Override
	    public String getSpecialName() {
		return "Arctic Blast";
	    }

	    @Override
	    public String getSpecialDescription() {
		return "Can hit a maximum of 150 damage, with a chance of stunning the opponent.";
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
	    public boolean submitSpecial(Object object) { // TODO find real animation of
							  // bear
		final Entity target = (Entity) object;
		final Familiar npc = this;
		getOwner().setNextGraphics(new Graphics(1316));
		getOwner().animate(new Animation(7660));
		animate(new Animation(4929));
		setNextGraphics(new Graphics(1405));
		WorldTasksManager.schedule(new WorldTask() {

		    @Override
		    public void run() {
			animate(new Animation(-1));
			World.sendProjectile(npc, target, 1406, 34, 16, 30, 35, 16, 0);
			WorldTasksManager.schedule(new WorldTask() {

			    @Override
			    public void run() {
				target.applyHit(new Hit(getOwner(), Utils.random(150), HitLook.MAGIC_DAMAGE));
				target.setNextGraphics(new Graphics(1407));
			    }
			}, 2);
		    }
		});
		return true;
	    }
}
