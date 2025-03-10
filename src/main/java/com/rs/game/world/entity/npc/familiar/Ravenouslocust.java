package com.rs.game.world.entity.npc.familiar;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.item.Item;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.skilling.Summoning.Pouches;
import com.rs.game.world.entity.player.content.Foods.Food;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;

public class Ravenouslocust extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6059371477618091701L;

	public Ravenouslocust(Player owner, Pouches pouch, Position tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	   @Override
	    public String getSpecialName() {
		return "Famine";
	    }

	    @Override
	    public String getSpecialDescription() {
		return "Eats a peice of an opponent's food.";
	    }

	    @Override
	    public int getBOBSize() {
		return 0;
	    }

	    @Override
	    public int getSpecialAmount() {
		return 12;
	    }

	    @Override
	    public SpecialAttack getSpecialAttack() {
		return SpecialAttack.ENTITY;
	    }

	    @Override
	    public boolean submitSpecial(Object object) {
		final Entity target = (Entity) object;
		final Familiar npc = this;
		setNextGraphics(new Graphics(1346));
		animate(new Animation(7998));
		WorldTasksManager.schedule(new WorldTask() {

		    @Override
		    public void run() {
			World.sendProjectile(npc, target, 1347, 34, 16, 30, 35, 16, 0);
			WorldTasksManager.schedule(new WorldTask() {

			    @Override
			    public void run() {
				target.setNextGraphics(new Graphics(1348));
				if (target instanceof Player) {
				    Player playerTarget = (Player) target;
				    itemLoop: for (Item item : playerTarget.getInventory().getItems().getItems()) {
					if (item == null)
					    continue;
					Food food = Food.forId(item.getId());
					if (food == null)
					    continue;
					playerTarget.getInventory().deleteItem(item);
					break itemLoop;
				    }
				}
			    }
			}, 2);
		    }
		});
		return true;
	    }
}
