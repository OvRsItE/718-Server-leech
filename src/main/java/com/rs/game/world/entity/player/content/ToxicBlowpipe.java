package com.rs.game.world.entity.player.content;

import java.io.Serializable;
import java.text.DecimalFormat;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.world.entity.player.CombatDefinitions;
import com.rs.game.world.entity.player.Equipment;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.utility.ItemBonuses;
import com.rs.utility.Utils;

import lombok.Getter;

public class ToxicBlowpipe implements Serializable {
	
	private static final long serialVersionUID = -7310748623048656676L;
	
	private static final DecimalFormat FORMATTER = new DecimalFormat("#.#");
	
	private transient Player player;
	@Getter private Item darts;
	@Getter private int scales;
	
	private static final int MAX_SCALES = 16_383;
	private static final int SCALES_ID = 29898;
	private static final int CHARGED_BLOWPIPE = 29966;
	private static final int UNCHARGED_BLOWPIPE = 29841;
	private static final int TANZANITE_FANG = 42922;
	private static final int BLOWPIPE_STRENGTH = 60;
	
	/**
	 * Handles item on item for blowpipe.
	 * @param blowpipe
	 * @param blowpipeSlot
	 * @param used
	 * @return
	 */
	public boolean handleItemOnItem(Item blowpipe, int blowpipeSlot, Item used) {
		if (blowpipe.getId() == TANZANITE_FANG && used.getId() == 1755 
				|| used.getId() == TANZANITE_FANG && blowpipe.getId() == 1755) {
			createBlowpipe();
			return true;
		}
		if (blowpipe.getId() == CHARGED_BLOWPIPE || blowpipe.getId() == UNCHARGED_BLOWPIPE) {	
			switch (used.getId()) {	
				case 806:
				case 807:
				case 808:
				case 809:
				case 810:
				case 811:
				case 11230:
					if (darts == null) {
						darts = used;
						player.getInventory().deleteItem(used);
					} else if (darts.getId() == used.getId()) {
						player.getInventory().deleteItem(used);
						darts.setAmount(darts.getAmount() + used.getAmount());
					} else {
						player.sendMessage("The blowpipe contains a different sort of darts.");
					}
					break;
				case SCALES_ID:
					int added = 0;
					if (scales / 3 == MAX_SCALES) {
						player.sendMessage("The blowpipe is already fully charged with scales.");
						break;
					}
					if (scales + used.getAmount() / 3 > MAX_SCALES) {
						added = MAX_SCALES - scales / 3;
					} else {
						added = used.getAmount();
					}
					scales += added * 3;
					player.getInventory().deleteItem(SCALES_ID, added);				
					break;	
				default:
					player.sendMessage("You can't charge the blowpipe with this.");
					return false;
			}
			if (blowpipe.getId() == UNCHARGED_BLOWPIPE) {
				blowpipe.setId(CHARGED_BLOWPIPE);
				player.getInventory().refresh(blowpipeSlot);
			}
			check();
			return true;
		}
		return false;
	}
	
	private void createBlowpipe() {
		if (player.getSkills().getLevel(Skills.FLETCHING) >= 53) {
			player.getInventory().deleteItem(TANZANITE_FANG, 1);
			player.getInventory().addItem(UNCHARGED_BLOWPIPE, 1);
			player.getDialogueManager().startDialogue("ItemMessage", "You create a toxic blowpipe from your tanzanite fang.", UNCHARGED_BLOWPIPE);
		} else {
			player.getDialogueManager().startDialogue("SimpleMessage", "You need at least level 53 fletching to do this.");
		}
	}

	/**
	 * Opens uncharging dialogue for blowpipe.
	 * @param slot
	 * 			the slot blowpipe is in.
	 */
	public void unchargeDialogue(int slot) {
		player.getDialogueManager().startDialogue(new Dialogue() {

			int slot;
			
			@Override
			public void start() {
				slot = (Integer) parameters[0];
				sendOptionsDialogue("Are you sure you want to uncharge it? All scales and darts will fallout. ", "Yes", "No");
			}

			@Override
			public void run(int interfaceId, int componentId) {
				if (componentId == OPTION_1)
					player.getToxicBlowpipe().uncharge(slot);		
				end();
			}

			@Override
			public void finish() {
				
			}
			
		}, slot);	
	}
	
	/**
	 * Uncharges the blowpipe of scales and darts.
	 */
	public void uncharge(int slot) {
		if (scales <= 0) {
			player.sendMessage("The blowpipe has no scales charging it.");
			return;
		} else {
			if (player.getInventory().containsItem(SCALES_ID, 1) || player.getInventory().getFreeSlots() > 1) {
				player.getInventory().addItem(SCALES_ID, scales / 3);
				scales = 0;
				if (unload()) {
					player.getInventory().getItem(slot).setId(UNCHARGED_BLOWPIPE);
					player.getInventory().refresh(slot);
				}
			} else {
				player.sendMessage("You don't have enough inventory space to uncharge the blowpipe.");
			}
		}
	}
	
	/**
	 * Unloads the blowpipe.
	 */
	public boolean unload() {
		if (darts == null) {
			player.sendMessage("The blowpipe has no darts in it.");
			return true;
		}
		if (player.getInventory().containsItem(darts.getId(), 1) || player.getInventory().hasFreeSlots()) {
			player.getInventory().addItem(darts.getId(), darts.getAmount());
			darts = null;
			return true;
		} else {
			player.sendMessage("You don't have enough inventory space to do this.");
		}
		return false;
	}
	
	/**
	 * Called to degrade the blowpipe.
	 * @return
	 */
	public boolean degrade() {
		if (scales <= 0 || darts == null) {
			if (scales <= 0 && darts == null) {
				player.getEquipment().getItem(Equipment.SLOT_WEAPON).setId(UNCHARGED_BLOWPIPE);
				player.sendMessage("Your blowpipe needs to be charged with Zulrah's scales and darts to be used.");
				return false;
			}
			player.sendMessage("Your blowpipe is out of " + (scales <= 0 ? "scales" : "darts") + ".");
			return false;
		}
		scales -= 2;
		darts.setAmount(darts.getAmount() - 1);
		if (darts.getAmount() <= 0)
			darts = null;	
		return true;
	}
	
	/**
	 * Gets the ranged strength of the current dart type.
	 * @return
	 * 			the ranged strength.
	 */
	public int getDartsStrength() {
		return ItemBonuses.getItemBonuses(darts.getId())[CombatDefinitions.RANGED_STR_BONUS] + BLOWPIPE_STRENGTH;
	}
	
	/**
	 * Shows the information amount the blowpipe.
	 */
	public void check() {
		String ammo = "None";
		if (darts != null)
			ammo = ItemDefinitions.forId(darts.getId()).getName() + " x " + Utils.formatNumber(darts.getAmount());	
		String scales = FORMATTER.format((getScales() / 3.0) * 100.0 / (double) MAX_SCALES) + "%";
		player.sendMessage("Darts: <col=007F00>" + ammo + "</col>. Scales: <col=007F00>" + scales + "</col>");
	}
	
	/**
	 * Checks if player is wearing blowpipe.
	 * @return
	 */
	public boolean wearing() {
		return player.getEquipment().getWeaponId() == CHARGED_BLOWPIPE;
	}
	
	/**
	 * Checks if an item is a blowpipe.
	 * @param itemId
	 * @return
	 */
	public static boolean isBlowpipe(int itemId) {
		return itemId == CHARGED_BLOWPIPE || itemId == UNCHARGED_BLOWPIPE;
	}
	
	/**
	 * Sets the player.
	 * @param player
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}
	
}
