package com.rs.game.world.entity.player.content;

import com.rs.game.world.entity.player.Player;

public class Starter {

        public static final int MAX_STARTER_COUNT = 2;

    	public static void appendStarter(Player player) {
        		player.getHintIconsManager().removeUnsavedHintIcon();
                player.getMusicsManager().reset();
                player.getCombatDefinitions().setAutoRelatie(false);
                /*player.getInventory().addItem(385, 1);
    			player.getInventory().addItem(1856, 1); // Information Book
    			player.getInventory().addItem(6099, 1); // Teleport Crystal
    			player.getInventory().addItem(995, 10000000); // 10M Coins
    			player.getInventory().addItem(1153, 1); // Iron Helm
    			player.getInventory().addItem(1115, 1); // Iron Platebody
    			player.getInventory().addItem(1067, 1); // Iron Platelegs
    			player.getInventory().addItem(1323, 1); // Iron Scimitar
    			player.getInventory().addItem(1333, 1); // Rune Scimitar
    			player.getInventory().addItem(4587, 1); // Dragon Scimitar
    			player.getInventory().addItem(1007, 1); // Cape
    			player.getInventory().addItem(841, 1); // Shortbow
    			player.getInventory().addItem(861, 1); // Magic Shortbow
    			player.getInventory().addItem(884, 1000); // Iron Arrow
    			player.getInventory().addItem(556, 1000); // Air Rune
    			player.getInventory().addItem(558, 500); // Mind Rune
    			player.getInventory().addItem(562, 200);*/ // Chaos Rune
                //player.getHintIconsManager().addHintIcon(2592, 5605, 1, 100, 0, 0,-1, false);
                player.getCombatDefinitions().refreshAutoRelatie();
                player.starter = 1;
                player.starterstage = 1;
				
    	}
}