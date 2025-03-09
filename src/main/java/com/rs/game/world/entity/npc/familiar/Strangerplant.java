package com.rs.game.world.entity.npc.familiar;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.item.Item;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.skilling.Summoning.Pouches;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.player.content.toxin.ToxinType;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

public class Strangerplant extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6059371477618091701L;
	private int forageTicks;

	public Strangerplant(Player owner, Pouches pouch, Position tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		int currentLevel = owner.getSkills().getLevelForXp(Skills.FARMING);
		owner.getSkills().set(Skills.FARMING, (int) ((1 + (currentLevel * .04)) + currentLevel));
		owner.getPackets().sendGameMessage("You feel a sudden urge to plant flowers.");
	    }

	    @Override
	    public void processNPC() {
		super.processNPC();
		forageTicks++;
		if (forageTicks == 750)
		    addStrangeFruit();
	    }

	    private void addStrangeFruit() {
		getBob().getBeastItems().add(new Item(464, 1));
		forageTicks = 0;
	    }

	    @Override
	    public String getSpecialName() {
		return "Poisonous Blast";
	    }

	    @Override
	    public String getSpecialDescription() {
		return "Attack with 50% chance of poisoning your opponent and inflicting 20 damage";
	    }

	    @Override
	    public int getBOBSize() {
		return 30;
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
		animate(new Animation(8211));
		World.sendProjectile(this, target, 1508, 34, 16, 30, 35, 16, 0);
		WorldTasksManager.schedule(new WorldTask() {

		    @Override
		    public void run() {
			target.applyHit(new Hit(getOwner(), Utils.random(20), HitLook.MAGIC_DAMAGE));
			if (Utils.random(1) == 0)
				target.getToxin().applyToxin(ToxinType.POISON, 60);
			target.setNextGraphics(new Graphics(1511));
		    }
		}, 2);
		return true;
	    }
	}