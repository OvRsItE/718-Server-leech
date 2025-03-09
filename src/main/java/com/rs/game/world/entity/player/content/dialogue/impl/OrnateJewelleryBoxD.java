/**
 * 
 */
package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.Magic;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

/**
 * @author ReverendDread
 * Sep 21, 2018
 */
public class OrnateJewelleryBoxD extends Dialogue {

	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.player.content.dialogue.Dialogue#start()
	 */
	@Override
	public void start() {
		sendOptionsDialogue("Choose a piece of jewellery.", "Ring of dueling", "Games necklance", "Combat bracelet", "Skills necklace", "Next Page");
	}

	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.player.content.dialogue.Dialogue#run(int, int)
	 */
	@Override
	public void run(int interfaceId, int componentId) {
		switch (stage) {
			case -1:
				if (componentId == OPTION_1) {
					sendOptionsDialogue("Choose a teleport.", "Al Kharid Duel Arena", "Castle Wars Arena", "Clan Wars Arena");
					stage = 0;
				}
				else if (componentId == OPTION_2) {
					sendOptionsDialogue("Choose a teleport.", "Burthorpe", "Barbarian Outpost", "Corporeal Beast", "Chasm of Tears", "Wintertodt Camp");
					stage = 1;
				}
				else if (componentId == OPTION_3) {
					sendOptionsDialogue("Choose a teleport.", "Warrior's Guild", "Champion's Guild", "Edgeville Monastery", "Ranging Guild");
					stage = 2;
				}
				else if (componentId == OPTION_4) {
					sendOptionsDialogue("Choose a teleport.", "Fishing Guild", "Mining Guild", "Crafting Guild", "Cooking Guild", "Woodcutting Guild");
					stage = 3;
				}
				else if (componentId == OPTION_5) {
					sendOptionsDialogue("Choose a piece of jewellery.", "Ring of wealth", "Amulet of glory", "Back");
					stage = 4;
				}
				break;
			case 0: //Ring of dueling
				if (componentId == OPTION_1) { //Duel arena
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3319, 3235, 0));
				}
				else if (componentId == OPTION_2) { //Castle wars
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2442, 3090, 0));
				}
				else if (componentId == OPTION_3) { //Clan wars
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2994, 9679, 0));
				}
				end();
				break;
			case 1: //Games necklace
				if (componentId == OPTION_1) { //Burthrope
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2896, 3548, 0));
				}
				else if (componentId == OPTION_2) { //Barbarian outpost
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2518, 3571, 0));
				}
				else if (componentId == OPTION_3) { //Corporeal beast
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2968, 4384, 2));
				}
				else if (componentId == OPTION_4) { //Chasm of tears
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3244, 9500, 2));
				}
				else if (componentId == OPTION_5) { //Wintertodt camp
					player.sm("Teleport unavaliable.");
				}
				end();
				break;
			case 2:
				if (componentId == OPTION_1) { //Warriors guild
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2880, 3542, 0));
				}
				else if (componentId == OPTION_2) { //Champions guild
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3191, 3367, 0));
				}
				else if (componentId == OPTION_3) { //Edgeville monestery
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3052, 3502, 0));
				}
				else if (componentId == OPTION_4) { //Ranging guild
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3656, 3440, 0));
				}
				end();
				break;
			case 3:
				if (componentId == OPTION_1) { //Fishing guild
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2613, 3384, 0));
				}
				else if (componentId == OPTION_2) { //Mining guild
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3021, 3338, 0));
				}
				else if (componentId == OPTION_3) { //Crafting guild
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2933, 3291, 0));
				}
				else if (componentId == OPTION_4) { //Cooking guild
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3143, 3440, 0));
				}
				else if (componentId == OPTION_5) { //Woodcutting guild
					player.sm("Teleport unavailable.");
				}
				end();
				break;
			case 4:
				if (componentId == OPTION_1) {
					sendOptionsDialogue("Choose a teleport.", "Miscellania", "Grand Exchange", "Falador Park", "Dondakan's Rock");
					stage = 5;
				}
				else if (componentId == OPTION_2) {
					sendOptionsDialogue("Choose a teleport.", "Edgeville", "Karamja", "Draynor Village", "Al Kharid");
					stage = 6;
				}
				else if (componentId == OPTION_3) {
					sendOptionsDialogue("Choose a piece of jewellery.", "Ring of dueling", "Games necklance", "Combat bracelet", "Skills necklace", "Next Page");
					stage = -1;
				}
				break;
			case 5:
				if (componentId == OPTION_1) { //Miscellania
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2503, 3860, 1));
				}
				else if (componentId == OPTION_2) { //Grand exchange
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3165, 3460, 0));
				}
				else if (componentId == OPTION_3) { //Falador Park
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2966, 3378, 0));
				}
				else if (componentId == OPTION_4) { //Dondakan's Rock
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2825, 10170, 0));
				}
				end();
				break;
			case 6:
				if (componentId == OPTION_1) { //Edgeville
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3087, 3496, 0));
				}
				else if (componentId == OPTION_2) { //Karmaja
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2920, 3173, 0));
				}
				else if (componentId == OPTION_3) { //Draynor village
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3103, 3250, 0));
				}
				else if (componentId == OPTION_4) { //Al Kharid
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3293, 3183, 0));
				}
				end();
				break;
		}
	}

	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.player.content.dialogue.Dialogue#finish()
	 */
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
	}

}
