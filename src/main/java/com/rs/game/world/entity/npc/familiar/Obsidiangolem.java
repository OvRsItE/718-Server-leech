package com.rs.game.world.entity.npc.familiar;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.skilling.Summoning.Pouches;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;

public class Obsidiangolem extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6059371477618091701L;

	public Obsidiangolem(Player owner, Pouches pouch, Position tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

    @Override
    public String getSpecialName() {
	return "Volcanic Strength";
    }

    @Override
    public String getSpecialDescription() {
	return "Gives +9 strength levels to the player.";
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
	return SpecialAttack.CLICK;
    }

    @Override
    public boolean submitSpecial(Object object) {
	Player player = (Player) object;
	player.getSkills().set(Skills.STRENGTH, player.getSkills().getLevelForXp(Skills.STRENGTH) + 9);
	player.animate(new Animation(7660));
	player.setNextGraphics(new Graphics(1300));
	setNextGraphics(new Graphics(1465));
	animate(new Animation(8053));
	return true;
    }
}

