package com.rs.game.world.entity.npc.familiar;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.skilling.Summoning.Pouches;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

public class Spiritjelly extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6059371477618091701L;

	public Spiritjelly(Player owner, Pouches pouch, Position tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

    @Override
    public String getSpecialName() {
	return "Dissolve";
    }

    @Override
    public String getSpecialDescription() {
	return "A magic attack that does up to 136 magic damage and drains the target's attack stat.";
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
    public boolean submitSpecial(Object object) {// TODO get special anim
	final Entity target = (Entity) object;
	Player player = getOwner();
	final int damage = Utils.getRandom(100);
	player.animate(new Animation(7660));
	player.setNextGraphics(new Graphics(1316));
	World.sendProjectile(this, target, 1359, 34, 16, 30, 35, 16, 0);
	if (damage > 20)
	    if (target instanceof Player)
		((Player) target).getSkills().set(Skills.ATTACK, ((Player) target).getSkills().getLevel(Skills.ATTACK) - (damage / 20));
	WorldTasksManager.schedule(new WorldTask() {

	    @Override
	    public void run() {
		target.applyHit(new Hit(getOwner(), damage, HitLook.MAGIC_DAMAGE));
		target.setNextGraphics(new Graphics(1360));
	    }
	}, 2);
	return true;
    }
}
