package com.rs.game.world.entity.player.content;

import java.io.Serializable;

import com.rs.game.item.Item;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.HomeTeleport;
import com.rs.game.world.entity.updating.impl.Graphics;

public class LodeStone implements Serializable {

	private static final long serialVersionUID = -2414976654365223059L;

	private static final int[] CONFIG_IDS = new int[] { 10900, 10901, 10902,
			10903, 10904, 10905, 10906, 10907, 10908, 10909, 10910, 10911,
			10912, 2448, 358 };

	private transient Player player;

	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * Handles the interface of the lodestone network. Checks if the player is
	 * able to teleport to the selected lodestone.
	 * 
	 * @param componentId
	 */
	public void handleButtons(int componentId) {
		player.stopAll();
		Position stoneTile = null;
		switch (componentId) {
		case 7:// Bandit Camp
			stoneTile = HomeTeleport.BANDIT_CAMP_LODE_STONE;
			break;
		case 39:// Lunar Isle
			stoneTile = HomeTeleport.LUNAR_ISLE_LODE_STONE;
			break;
		case 40:// AlKarid
			stoneTile = HomeTeleport.ALKARID_LODE_STONE;
			break;
		case 41:// Ardougne
			stoneTile = HomeTeleport.ARDOUGNE_LODE_STONE;
			break;
		case 42:// Burthorpe
			stoneTile = HomeTeleport.BURTHORPE_LODE_STONE;
			break;
		case 43:// Catherby
			stoneTile = HomeTeleport.CATHERBAY_LODE_STONE;
			break;
		case 44:// Draynor
			stoneTile = HomeTeleport.DRAYNOR_VILLAGE_LODE_STONE;
			break;
		case 45:// Edgeville
			stoneTile = HomeTeleport.EDGEVILLE_LODE_STONE;
			break;
		case 46:// Falador
			stoneTile = HomeTeleport.FALADOR_LODE_STONE;
			break;
		case 47:// Lumbridge
			stoneTile = HomeTeleport.LUMBRIDGE_LODE_STONE;
			break;
		case 48:// Port Sarim
			stoneTile = HomeTeleport.PORT_SARIM_LODE_STONE;
			break;
		case 49:// Seers Village
			stoneTile = HomeTeleport.SEERS_VILLAGE_LODE_STONE;
			break;
		case 50:// Taverly
			stoneTile = HomeTeleport.TAVERLY_LODE_STONE;
			break;
		case 51:// Varrock
			stoneTile = HomeTeleport.VARROCK_LODE_STONE;
			break;
		case 52:// Yanille
			stoneTile = HomeTeleport.YANILLE_LODE_STONE;
			break;
		}
		if (stoneTile != null) {
			player.getActionManager().setAction(new HomeTeleport(stoneTile));
		}
	}

	/**
	 * Checks the object id then sends the necessary config. Activates the
	 * lodestone for the player.
	 * 
	 * @param object
	 */
	public void activateLodestone(WorldObject object) {
		switch (object.getId()) {
		case 69827:// Bandit Camp
			sendReward();
			player.getPackets().sendConfigByFile(CONFIG_IDS[14], 190);
			player.getPackets().sendGraphics(new Graphics(3019), object);
			player.getActivatedLodestones()[14] = true;
			break;
		case 69828:// Lunar Isle
			sendReward();
			player.getPackets().sendConfigByFile(CONFIG_IDS[13], 190);
			player.getPackets().sendGraphics(new Graphics(3019), object);
			player.getActivatedLodestones()[13] = true;
			break;
		case 69829:// AlKarid
			sendReward();
			player.getPackets().sendConfigByFile(CONFIG_IDS[0], 1);
			player.getPackets().sendGraphics(new Graphics(3019), object);
			player.getActivatedLodestones()[0] = true;
			break;
		case 69830:// Ardougne
			sendReward();
			player.getPackets().sendConfigByFile(CONFIG_IDS[1], 1);
			player.getPackets().sendGraphics(new Graphics(3019), object);
			player.getActivatedLodestones()[1] = true;
			break;
		case 69831:// Burthorpe
			sendReward();
			player.getPackets().sendConfigByFile(CONFIG_IDS[2], 1);
			player.getPackets().sendGraphics(new Graphics(3019), object);
			player.getActivatedLodestones()[2] = true;
			break;
		case 69832:// Catherby
			sendReward();
			player.getPackets().sendConfigByFile(CONFIG_IDS[3], 1);
			player.getPackets().sendGraphics(new Graphics(3019), object);
			player.getActivatedLodestones()[3] = true;
			break;
		case 69833:// Draynor
			sendReward();
			player.getPackets().sendConfigByFile(CONFIG_IDS[4], 1);
			player.getPackets().sendGraphics(new Graphics(3019), object);
			player.getActivatedLodestones()[4] = true;
			break;
		case 69834:// Edgeville
			sendReward();
			player.getPackets().sendConfigByFile(CONFIG_IDS[5], 1);
			player.getPackets().sendGraphics(new Graphics(3019), object);
			player.getActivatedLodestones()[5] = true;
			break;
		case 69835:// Falador
			sendReward();
			player.getPackets().sendConfigByFile(CONFIG_IDS[6], 1);
			player.getPackets().sendGraphics(new Graphics(3019), object);
			player.getActivatedLodestones()[6] = true;
			break;
		case 69837:// Port Sarim
			sendReward();
			player.getPackets().sendConfigByFile(CONFIG_IDS[8], 1);
			player.getPackets().sendGraphics(new Graphics(3019), object);
			player.getActivatedLodestones()[8] = true;
			break;
		case 69838:// Seers Village
			sendReward();
			player.getPackets().sendConfigByFile(CONFIG_IDS[9], 1);
			player.getPackets().sendGraphics(new Graphics(3019), object);
			player.getActivatedLodestones()[9] = true;
			break;
		case 69839:// Taverly
			sendReward();
			player.getPackets().sendConfigByFile(CONFIG_IDS[10], 1);
			player.getPackets().sendGraphics(new Graphics(3019), object);
			player.getActivatedLodestones()[10] = true;
			break;
		case 69840:// Varrock
			sendReward();
			player.getPackets().sendConfigByFile(CONFIG_IDS[11], 1);
			player.getPackets().sendGraphics(new Graphics(3019), object);
			player.getActivatedLodestones()[11] = true;
			break;
		case 69841:// Yanille
			sendReward();
			player.getPackets().sendConfigByFile(CONFIG_IDS[12], 1);
			player.getPackets().sendGraphics(new Graphics(3019), object);
			player.getActivatedLodestones()[12] = true;
			break;
		}
	}

	/**
	 * Sends the player their reward for activating the lodestone.
	 * 
	 */
	private void sendReward() {
		if (player.getInventory().getFreeSlots() < 1) {
			player.getPackets()
					.sendGameMessage(
							"You have no free spaces in your inventory. Your reward is on the ground.");
			World.addGroundItem(new Item(995, 375), player, player, true, 180);
			return;
		}
		player.getInventory().addItemMoneyPouch(995, 375);
	}

	/**
	 * Checks if the player has unlocked the lodestone during login.
	 */
	public void checkActivation() {
		player.getPackets().sendConfigByFile(10907, 1);
		for (int x = 0; x <= 12; x++) {
			player.getPackets().sendConfigByFile(CONFIG_IDS[x], 1);
		}
		player.getPackets().sendConfigByFile(CONFIG_IDS[13], 190);
		player.getPackets().sendConfigByFile(CONFIG_IDS[14], 15);
	}

}
