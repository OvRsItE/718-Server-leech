package com.rs.game.world.entity.player.actions.skilling.mining;

import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.ShootingStars;
import com.rs.game.world.entity.player.content.ShootingStars.StarStages;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.utility.Utils;

public class StarMining extends MiningBase {
	
	public static String getFirstMessage(int id) {
		return "Congratulations, you were the first to find this star! You recieve " + Utils.getFormattedNumber(getXpForFinding(id) / 10) + " Mining XP as a reward.";
	}
	private static int getXpForFinding(int id) {
		switch (id) {
		case 38660:
			return 19313;
		case 38661:
			return 21393;
		case 38662:
			return 23848;
		case 38663:
			return 25384;
		case 38664:
			return 26382;
		case 38665:
			return 27393;
		case 38666:
			return 29303;
		case 38667:
			return 31238;
		case 38668: 
			return 34895;
		default:
			return 23924;
		}
	}

	private final WorldObject star;
	
	private final StarStages starDef;
	
	public StarMining(WorldObject star, StarStages starDef) {
		this.star = star;
		this.starDef = starDef;
	}
	
	private boolean checkAll(Player player) {
		if(!ShootingStars.found) {
			player.getDialogueManager().startDialogue("SimpleMessage", getFirstMessage(star.getId()));
			ShootingStars.found = true;
			player.getSkills().addXp(Skills.MINING, getXpForFinding(star.getId())/10);
		    return false;
		}
		if (!setPickaxe(player)) {
			player.getPackets().sendGameMessage(
					"You need a pickaxe to mine the star.");
			return false;
		}
		if (!hasPickaxe(player)) {
			player.getPackets().sendGameMessage(
					"You dont have the required level to use this pickaxe.");
			return false;
		}
		if (!hasMiningLevel(player))
			return false;
		if (!player.getInventory().hasFreeSlots()) {
			player.getPackets().sendGameMessage(
					"Not enough space in your inventory.");
			return false;
		}
		if(!checkRock(player)) {
			return false;
		}
		return true;
	}	

	private boolean checkRock(Player player) {
		return World.getObjectWithId(new Position(star.getX(), star.getY(), star.getZ()), star.getId()) != null;
	}
	
	private int getMiningDelay(Player player) {
		int oreBaseTime = 50;
		int oreRandomTime = 20;
		int mineTimer = oreBaseTime
				- player.getSkills().getLevel(Skills.MINING)
				- Utils.getRandom(pickaxeTime);
		if (mineTimer < 1 + oreRandomTime)
			mineTimer = 1 + Utils.getRandom(oreRandomTime);
		mineTimer /= player.getAuraManager().getMininingAccurayMultiplier();
		return mineTimer;
	}
	
	private boolean hasMiningLevel(Player player) {
		if (starDef.getLevel() > player.getSkills().getLevel(Skills.MINING)) {
			player.getPackets().sendGameMessage(
					"You need a mining level of "+starDef.getLevel()+" to mine this rock.");
			return false;
		}
		return true;
	}
	
	@Override
	public boolean process(Player player) {
		player.animate(new Animation(emoteId));
		return checkRock(player);
	}
	
	@Override
	public int processWithDelay(Player player) {
		if (!player.getInventory().hasFreeSlots()) {
			player.animate(new Animation(-1));
			player.getPackets().sendGameMessage(
					"Not enough space in your inventory.");
			return -1;
		}
		int randomStarDust = Utils.random(5);
		if(Utils.random(3) == 1) {
		   player.getSkills().addXp(Skills.MINING, starDef.getXP());
		   ShootingStars.starDust += randomStarDust;
		   ShootingStars.upgradeStar(player);
		   int currentStarDust = player.getInventory().getNumberOf(13727) + (player.getBank().getItem(13727) == null ? 0 : player.getBank().getItem(13727).getAmount());
		   if(currentStarDust < 200)
			  if(currentStarDust + randomStarDust <= 200)// 199 + 2 = 201 
		      player.getInventory().addItem(13727, randomStarDust);
			  else 
			  player.getInventory().addItem(13727, 200 - currentStarDust);
			  
		}
		return getMiningDelay(player);
	}

	@Override
	public boolean start(Player player) {
		if (!checkAll(player))
			return false;
		setActionDelay(player, getMiningDelay(player));
		return true;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, -1);
	}

}
