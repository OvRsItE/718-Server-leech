package com.rs.game.world.entity.npc.familiar;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.item.Item;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.skilling.Fishing;
import com.rs.game.world.entity.player.actions.skilling.Summoning.Pouches;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;

public class Ibis extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6059371477618091701L;
	private int forageTicks;

	public Ibis(Player owner, Pouches pouch, Position tile,
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
		boolean isSwordFish = Utils.random(3) == 0;
		int foragedItem = isSwordFish ? 371 : 359;
		if (isSwordFish)
		    getOwner().getSkills().addXp(Skills.FISHING, 10);
		getBob().getBeastItems().add(new Item(foragedItem, 1));
		forageTicks = 0;
	    }

	    @Override
	    public String getSpecialName() {
		return "Fish rain";
	    }

	    @Override
	    public String getSpecialDescription() {
		return "Makes fish (raw shrimp, bass, cod, and mackerel) fall out of the sky.";
	    }

	    @Override
	    public int getBOBSize() {
		return 10;
	    }

	    @Override
	    public int getSpecialAmount() {
		return 12;
	    }

	    @Override
	    public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	    }

	    @Override
	    public boolean submitSpecial(Object object) {
		final Player player = (Player) object;
		getOwner().setNextGraphics(new Graphics(1300));
		getOwner().animate(new Animation(7660));
		animate(new Animation(8201));
		final Position firstTile = new Position(player.getX() + 1, player.getY() + 1, player.getZ());
		final Position secondTile = new Position(player.getX() - 1, player.getY() - 1, player.getZ());
		World.sendGraphics(player, new Graphics(1337), firstTile);
		World.sendGraphics(player, new Graphics(1337), secondTile);
		WorldTasksManager.schedule(new WorldTask() {

		    @Override
		    public void run() {
			World.addGroundItem(new Item(1, 1), firstTile, player, true, 180, false);
			World.addGroundItem(new Item(1, 1), secondTile, player, true, 180, false);
		    }
		}, 2);
		return true;
	    }
}
