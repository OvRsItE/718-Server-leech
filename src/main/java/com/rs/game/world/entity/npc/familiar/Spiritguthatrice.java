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
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

public class Spiritguthatrice extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6059371477618091701L;
	private int chocoTriceEgg;

	public Spiritguthatrice(Player owner, Pouches pouch, Position tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	   @Override
	    public void processNPC() {
		super.processNPC();
		chocoTriceEgg++;
		if (chocoTriceEgg == 500)
		    addChocolateEgg();
	    }

	    private void addChocolateEgg() {
		getBob().getBeastItems().add(new Item(12109, 1));
		chocoTriceEgg = 0;
	    }

	    @Override
	    public String getSpecialName() {
		return "Petrifying Gaze";
	    }

	    @Override
	    public String getSpecialDescription() {
		return "Inflicts damage and drains a combat stat, which varies according to the type of cockatrice.";
	    }

	    @Override
	    public int getBOBSize() {
		return 30;
	    }

	    @Override
	    public int getSpecialAmount() {
		return 5;
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
		animate(new Animation(7766));
		setNextGraphics(new Graphics(1467));
		World.sendProjectile(this, target, 1468, 34, 16, 30, 35, 16, 0);
		if (target instanceof Player) {
		    Player playerTarget = (Player) target;
		    int level = playerTarget.getSkills().getLevelForXp(Skills.ATTACK);
		    int drained = 3;
		    if (level - drained > 0)
			drained = level;
		    playerTarget.getSkills().drainLevel(Skills.ATTACK, drained);
		}
		WorldTasksManager.schedule(new WorldTask() {

		    @Override
		    public void run() {
			target.applyHit(new Hit(getOwner(), Utils.random(100), HitLook.MELEE_DAMAGE));
		    }
		}, 2);
		return true;
	    }

	}
