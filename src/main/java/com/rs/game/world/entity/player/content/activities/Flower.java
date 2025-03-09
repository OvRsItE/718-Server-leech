package com.rs.game.world.entity.player.content.activities;

import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.activities.dueling.DuelArena;
import com.rs.game.world.entity.player.content.activities.dueling.DuelControler;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.utility.Utils;


public class Flower {
	
	private final static int[] FLOWERS = {2980, 2986, 2987, 2988, 2981, 2981, 2981, 2981, 2981, 2981, 2981, 2981, 2982, 2982, 2982, 2982, 2982, 2982, 2982, 2982,
	2983, 2983, 2983, 2983, 2983, 2983, 2983, 2983, 2984, 2984, 2984, 2984, 2984, 2984, 2984, 2984, 2985, 2985, 2985, 2985, 2985, 2985, 2985, 2985, 2981, 2981, 2981, 2981, 2981, 2981, 2981, 2981, 2982, 2982, 2982, 2982, 2982, 2982, 2982, 2982,
	2983, 2983, 2983, 2983, 2983, 2983, 2983, 2983, 2984, 2984, 2984, 2984, 2984, 2984, 2984, 2984, 2985, 2985, 2985, 2985, 2985, 2985, 2985, 2985  };
	
	public static void PlantFlower(Player player) {
		final Position tile = new Position(player);
		 WorldObject Flower4 = new WorldObject(FLOWERS[Utils.random(FLOWERS.length)], 10, 0, player);
		if (!World.canMoveNPC(player.getZ(), player.getX(), player.getY(),1)
						|| player.getControlerManager().getControler() instanceof DuelArena || player.getControlerManager().getControler() instanceof DuelControler) { // contains
			player.getPackets().sendGameMessage("You can't Plant a flower here.");
		} else {
	if (!player.addWalkSteps(player.getX() - 1, player.getY(), 1))
		if (!player.addWalkSteps(player.getX() + 1, player.getY(), 1))
			if (!player.addWalkSteps(player.getX(), player.getY() + 1, 1))
				player.addWalkSteps(player.getX(), player.getY() - 1, 1);
	player.getInventory().deleteItem(299, 1);
	World.spawnObject(Flower4, false);
	player.getDialogueManager().startDialogue("FlowerGame");
	player.animate(new Animation(827));
		}
	}

	public static void PickFlowers() {
		// TODO Auto-generated method stub
		
	}

}