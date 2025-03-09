package com.rs.game.world.entity.npc.familiar;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.skilling.Summoning.Pouches;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;

public class Abyssaltitan extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7635947578932404484L;

	public Abyssaltitan(Player owner, Pouches pouch, Position tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
	super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Essence Shipment";
	}

	@Override
	public String getSpecialDescription() {
		return "Sends all your inventory and beast's essence to your bank.";
	}

	@Override
	public int getBOBSize() {
		return 7;
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
		if (getOwner().getBank().hasBankSpace()) {
			if (getBob().getBeastItems().getUsedSlots() == 0) {
				getOwner().getPackets().sendGameMessage(
						"You clearly have no essence.");
				return false;
			}
			getOwner().getBank().depositAllBob(false);
			getOwner().setNextGraphics(new Graphics(1316));
			getOwner().animate(new Animation(7660));
			return true;
		}
		return false;
	}
}
