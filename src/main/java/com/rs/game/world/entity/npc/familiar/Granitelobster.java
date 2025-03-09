package com.rs.game.world.entity.npc.familiar;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.item.Item;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.skilling.Fishing;
import com.rs.game.world.entity.player.actions.skilling.Summoning.Pouches;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

public class Granitelobster extends Familiar {

	/**
	 * 
	 */
	private int forageTicks;
	private static final long serialVersionUID = 6059371477618091701L;

	public Granitelobster(Player owner, Pouches pouch, Position tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

    @Override
    public void processNPC() {
	super.processNPC();
	boolean isFishing = getOwner().getActionManager().getAction() != null && getOwner().getActionManager().getAction() instanceof Fishing;
	if (isFishing) {
	    forageTicks++;
	    if (forageTicks == 300)
		giveReward();
	}
    }

    private void giveReward() {
	boolean isShark = Utils.random(3) == 0;
	int foragedItem = isShark ? 383 : 371;
	if (!isShark)
	    getOwner().getSkills().addXp(Skills.FISHING, 30);
	getBob().getBeastItems().add(new Item(foragedItem, 1));
	forageTicks = 0;
    }

    @Override
    public String getSpecialName() {
	return "Crushing Claw";
    }

    @Override
    public String getSpecialDescription() {
	return "May inflict up to 140 life points of magic damage and temporarily decrease an opponent's Defence by five levels.";
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
	final Familiar npc = this;
	getOwner().setNextGraphics(new Graphics(1316));
	getOwner().animate(new Animation(7660));
	animate(new Animation(8118));
	setNextGraphics(new Graphics(1351));
	WorldTasksManager.schedule(new WorldTask() {

	    @Override
	    public void run() {

		WorldTasksManager.schedule(new WorldTask() {

		    @Override
		    public void run() {
			if (Utils.getRandom(5) == 0) {
			    if (target instanceof Player)
				((Player) target).getSkills().set(Skills.DEFENCE, ((Player) target).getSkills().getLevel(Skills.DEFENCE));
			}
			target.applyHit(new Hit(getOwner(), Utils.random(140), HitLook.MELEE_DAMAGE));
			target.setNextGraphics(new Graphics(1353));
		    }
		}, 2);
		World.sendProjectile(npc, target, 1352, 34, 16, 30, 35, 16, 0);
	    }
	});
	return true;
    }
}

