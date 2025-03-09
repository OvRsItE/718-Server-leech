package com.rs.game.world.entity.npc.familiar;

import com.rs.game.item.Item;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.skilling.Summoning.Pouches;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;

public class Fruitbat extends Familiar {

	/**
	 * 
	 */
	
    private static final transient int[] FRUITS = new int[] { 5972, 5974, 2102, 2120, 1963, 2108, 5982 };

    private int fruitTicks;
    
	private static final long serialVersionUID = 6059371477618091701L;

	public Fruitbat(Player owner, Pouches pouch, Position tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

    @Override
    public void processNPC() {
	super.processNPC();
	fruitTicks++;
	if (fruitTicks == 500)
	    addFruitReward();
    }

    private void addFruitReward() {
	getBob().getBeastItems().add(new Item(FRUITS[Utils.random(FRUITS.length)], 1));
	fruitTicks = 0;
    }

    @Override
    public String getSpecialName() {
	return "Fruitfall";
    }

    @Override
    public String getSpecialDescription() {
	return "Bat can get up to eight fruit and drop them on the ground around the player.";
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
	return SpecialAttack.CLICK;
    }

    @Override
    public boolean submitSpecial(Object object) {
	Player player = (Player) object;
	player.animate(new Animation(7660));
	player.setNextGraphics(new Graphics(1316));
	Position tile = this;
	for (int trycount = 0; trycount < Utils.getRandom(8); trycount++) {
	    tile = new Position(this, 2);
	    World.sendGraphics(player, new Graphics(1331), tile);
	    World.addGroundItem(new Item(FRUITS[Utils.random(FRUITS.length)], 1), tile, player, true, 120, false);
	}
	return false;
    }
}