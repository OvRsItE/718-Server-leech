package com.rs.game.world.entity.npc.familiar;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.skilling.Summoning.Pouches;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;

public class Hydra extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6059371477618091701L;

	public Hydra(Player owner, Pouches pouch, Position tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	   @Override
	    public String getSpecialName() {
		return "Regrowth";
	    }

	    @Override
	    public String getSpecialDescription() {
		return "Automatically regrows the associated tree without delay.";
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
		return SpecialAttack.CLICK;
	    }

	    @Override
	    public boolean submitSpecial(Object object) {
		Player player = (Player) object;
		setNextGraphics(new Graphics(1487));
		animate(new Animation(7939));
		return false;
	    }
}
