package com.rs.game.world.entity.player.content.presetsmanager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import com.rs.game.item.Item;


import com.rs.game.world.entity.player.Equipment;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.TemporaryAttributes;
import com.rs.game.world.entity.player.content.presetsmanager.PresetInterfaceConfigs.CONFIGS;
import com.rs.game.world.entity.player.content.skills.Skills;


/**
 * Coded for Venomite PS.
 * @author Luke
 */

public class PresetManager implements Serializable {
	private static final long serialVersionUID = -703572544198525607L;

	public static final short INTERFACE_ID = 3126;

	private Player player;
	private List<PresetObject> playerPresets;
	private PresetObject viewingPreset;
	private boolean savingPreset;
	private boolean deletingPreset;
	
	private void deletePreset(int index) {
		if(playerPresets.size() > index)
			playerPresets.remove(index);
		player.getDialogueManager().startDialogue("SimpleMessage", "You've deleted preset #"+(index+1));
	}
	
	private void sendDeletePreset() {
		this.deletingPreset = true;
		player.getDialogueManager().startDialogue("SimpleMessage", "Click the preset which you wish to delete.");
	}
	
	private void savePreset(int index) {
		if(playerPresets.size() > index)
			playerPresets.set(index, viewingPreset);
		else
			playerPresets.add(index, viewingPreset);
		player.getDialogueManager().startDialogue("SimpleMessage", "Your setup has been saved to preset #"+(index+1));
	}
	
	private void sendSavePreset() {
		this.savingPreset = true;
		player.getDialogueManager().startDialogue("SimpleMessage", "Click the preset which you wish to save to.");
	}
	
	public void startInterface() {
		player.getInterfaceManager().sendInterface(3126);
		sendPresetSetup(getCurrentPresetObject());
		sendPresetInventory(viewingPreset);
	}
	
	private void gearUpPreset() {
		player.getBank().depositAllEquipment(true);
		player.getBank().depositAllInventory(true);
		PresetObject preset = this.viewingPreset;
		for(Item item : preset.getEquipment()) {
			if(item != null) {
				if (player.getBank().containsItem(item.getId(), item.getAmount())) {
					int[] BankSlot = player.getBank().getItemSlot(item.getId());
					int slot = Equipment.getItemSlot(item.getId());
					player.getEquipment().getItems().set(slot, item);
					player.getBank().removeItem(BankSlot, item.getAmount(), true, true);
				} else {
						player.sendMessage("<col=ff0000> Couldn't withdraw everything in preset missing item " + item.getName());
				}
			}
		}
		for(Item item : preset.getInventory()) {
			if(item != null)
			if (player.getBank().containsItem(item.getId(), item.getAmount())) {
				int[] BankSlot = player.getBank().getItemSlot(item.getId());
				player.getInventory().addItem(item);
				player.getBank().removeItem(BankSlot, item.getAmount(), true, true);
			} else {
				player.sendMessage("<col=ff0000> Couldn't withdraw everything in preset missing item " + item.getName());
			}
		}
//		player.getSkills().set(PresetObject.ATTACK_INDEX, preset.getSkills()[PresetObject.ATTACK_INDEX], true);
//		player.getSkills().set(PresetObject.STRENGTH_INDEX, preset.getSkills()[PresetObject.STRENGTH_INDEX], true);
//		player.getSkills().set(PresetObject.DEFENCE_INDEX, preset.getSkills()[PresetObject.DEFENCE_INDEX], true);
//		player.getSkills().set(PresetObject.RANGE_INDEX, preset.getSkills()[PresetObject.RANGE_INDEX], true);
//		player.getSkills().set(PresetObject.MAGIC_INDEX, preset.getSkills()[PresetObject.MAGIC_INDEX], true);
//		player.getSkills().set(PresetObject.HITPOINTS_INDEX, preset.getSkills()[PresetObject.HITPOINTS_INDEX], true);
//		player.getSkills().set(PresetObject.PRAYER_INDEX, preset.getSkills()[PresetObject.PRAYER_INDEX], true);
		player.getCombatDefinitions().setSpellBook(preset.getSpellBook());
		player.getPrayer().setPrayerBook(preset.isPrayer());
		player.getAppearence().generateAppearenceData();
		player.getEquipment().init();
		player.closeInterfaces();
	}
	
	private void sendPresetMagicAndPrayer(PresetObject preset) {
		String prayer = "Normal";
		if(preset.isPrayer()) {
			prayer = "Curses";
		}
		String magic = "Moderns";
		if(preset.getSpellBook() == 1) {
			magic = "Ancients";
		} else if(preset.getSpellBook() == 2) {
			magic = "Lunars";
		}
		player.getPackets().sendIComponentText(INTERFACE_ID, 46, prayer);
		player.getPackets().sendIComponentText(INTERFACE_ID, 50, magic);
	}

	private void sendPresetSpawnList(PresetObject preset) {
		player.getPackets().sendItems(96, player.getInventory().getItems().getItemsCopy());
		player.getPackets().sendUnlockIComponentOptionSlots(INTERFACE_ID, 118, 0, 90, 0, 1, 2);
	}
	
	private void sendPresetSkills(PresetObject preset) {
		player.getPackets().sendIComponentText(INTERFACE_ID, CONFIGS.ATTACK.textComponents[0], preset.getSkills()[PresetObject.ATTACK_INDEX]+"");
		player.getPackets().sendIComponentText(INTERFACE_ID, CONFIGS.ATTACK.textComponents[1], preset.getSkills()[PresetObject.ATTACK_INDEX]+"");
		player.getPackets().sendIComponentText(INTERFACE_ID, CONFIGS.STRENGTH.textComponents[0], preset.getSkills()[PresetObject.STRENGTH_INDEX]+"");
			player.getPackets().sendIComponentText(INTERFACE_ID, CONFIGS.STRENGTH.textComponents[1], preset.getSkills()[PresetObject.STRENGTH_INDEX]+"");
		player.getPackets().sendIComponentText(INTERFACE_ID, CONFIGS.DEFENCE.textComponents[0], preset.getSkills()[PresetObject.DEFENCE_INDEX]+"");
			player.getPackets().sendIComponentText(INTERFACE_ID, CONFIGS.DEFENCE.textComponents[1], preset.getSkills()[PresetObject.DEFENCE_INDEX]+"");
		player.getPackets().sendIComponentText(INTERFACE_ID, CONFIGS.HITPOINTS.textComponents[0], preset.getSkills()[PresetObject.HITPOINTS_INDEX]+"");
			player.getPackets().sendIComponentText(INTERFACE_ID, CONFIGS.HITPOINTS.textComponents[1], preset.getSkills()[PresetObject.HITPOINTS_INDEX]+"");
		player.getPackets().sendIComponentText(INTERFACE_ID, CONFIGS.RANGE.textComponents[0], preset.getSkills()[PresetObject.RANGE_INDEX]+"");
			player.getPackets().sendIComponentText(INTERFACE_ID, CONFIGS.RANGE.textComponents[1], preset.getSkills()[PresetObject.RANGE_INDEX]+"");
		player.getPackets().sendIComponentText(INTERFACE_ID, CONFIGS.MAGIC.textComponents[0], preset.getSkills()[PresetObject.MAGIC_INDEX]+"");
			player.getPackets().sendIComponentText(INTERFACE_ID, CONFIGS.MAGIC.textComponents[1], preset.getSkills()[PresetObject.MAGIC_INDEX]+"");
		player.getPackets().sendIComponentText(INTERFACE_ID, CONFIGS.PRAYER.textComponents[0], preset.getSkills()[PresetObject.PRAYER_INDEX]+"");
			player.getPackets().sendIComponentText(INTERFACE_ID, CONFIGS.PRAYER.textComponents[1], preset.getSkills()[PresetObject.PRAYER_INDEX]+"");	
	}
	
	private void sendPresetEquipment(PresetObject preset) {
		player.getPackets().sendHideIComponent(INTERFACE_ID, CONFIGS.EQUIP_ARROW_LEFT.buttonId, true);
		player.getPackets().sendHideIComponent(INTERFACE_ID, CONFIGS.EQUIP_ARROW_RIGHT.buttonId, false);
		player.getPackets().sendIComponentText(INTERFACE_ID, 160, "Equipment");
		player.getPackets().sendHideIComponent(INTERFACE_ID, 170, false);
		player.getPackets().sendHideIComponent(INTERFACE_ID, 210, false);
		Item slot = null;
		slot = preset.getEquipment()[0];if(slot != null)player.getPackets().sendItemOnIComponent(INTERFACE_ID, 177, slot.getId(), slot.getAmount());
		slot = preset.getEquipment()[1];if(slot != null)player.getPackets().sendItemOnIComponent(INTERFACE_ID, 180, slot.getId(), slot.getAmount());
		slot = preset.getEquipment()[2];if(slot != null)player.getPackets().sendItemOnIComponent(INTERFACE_ID, 183, slot.getId(), slot.getAmount());
		slot = preset.getEquipment()[3];if(slot != null)player.getPackets().sendItemOnIComponent(INTERFACE_ID, 189, slot.getId(), slot.getAmount());
		slot = preset.getEquipment()[4];if(slot != null)player.getPackets().sendItemOnIComponent(INTERFACE_ID, 192, slot.getId(), slot.getAmount());
		slot = preset.getEquipment()[5];if(slot != null)player.getPackets().sendItemOnIComponent(INTERFACE_ID, 195, slot.getId(), slot.getAmount());
		slot = preset.getEquipment()[7];if(slot != null)player.getPackets().sendItemOnIComponent(INTERFACE_ID, 198, slot.getId(), slot.getAmount());
		slot = preset.getEquipment()[9];if(slot != null)player.getPackets().sendItemOnIComponent(INTERFACE_ID, 201, slot.getId(), slot.getAmount());
		slot = preset.getEquipment()[10];if(slot != null)player.getPackets().sendItemOnIComponent(INTERFACE_ID, 204, slot.getId(), slot.getAmount());
		slot = preset.getEquipment()[12];if(slot != null)player.getPackets().sendItemOnIComponent(INTERFACE_ID, 207, slot.getId(), slot.getAmount());
		slot = preset.getEquipment()[13];if(slot != null)player.getPackets().sendItemOnIComponent(INTERFACE_ID, 186, slot.getId(), slot.getAmount());
		slot = preset.getEquipment()[14];if(slot != null)player.getPackets().sendItemOnIComponent(INTERFACE_ID, 212, slot.getId(), slot.getAmount());
	}
	
	private void sendPresetInventory(PresetObject preset) {
		player.getPackets().sendIComponentText(INTERFACE_ID, 119, "Inventory");
		player.getPackets().sendHideIComponent(INTERFACE_ID, 224, false);
		player.getPackets().sendItems(95, preset.getInventory());
		player.getPackets().sendInterSetItemsOptionsScript(INTERFACE_ID, 224, 95, 4, 7, "Examine");
	}
	
	private void sendPresetSetup(PresetObject preset) {
		this.viewingPreset = preset;
		this.savingPreset = false;
		this.deletingPreset = false;
		sendPresetSkills(preset);
		sendPresetEquipment(preset);
		sendPresetMagicAndPrayer(preset);
		sendPresetSpawnList(preset);
		player.getInterfaceManager().sendInterface(INTERFACE_ID);
	}
	
	public void handleButtons(int button) {
		System.out.println("button: "+button);
		if(matchesButton(button, CONFIGS.RESPAWN_LOCATION)) {
			player.getPackets().sendGameMessage("This feature is currently unavailable.");
			return;
		}
		if(this.savingPreset) {
			this.savingPreset = false;
			int index = PresetInterfaceConfigs.getSaveIndexForButton(button);
			if(index != -1) {
				savePreset(index);
				return;
			}
		}
		if(this.deletingPreset) {
			this.deletingPreset = false;
			int index = PresetInterfaceConfigs.getSaveIndexForButton(button);
			if(index != -1) {
				deletePreset(index);
				return;
			}
		}
		if(matchesButton(button, CONFIGS.CURRENT_SETUP)) {
			sendPresetSetup(getCurrentPresetObject());
			sendPresetInventory(viewingPreset);
		} else if(matchesButton(button, CONFIGS.ATTACK)) {
			sendChangePresetSkillInput(PresetObject.ATTACK_INDEX);
		} else if(matchesButton(button, CONFIGS.STRENGTH)) {
			sendChangePresetSkillInput(PresetObject.STRENGTH_INDEX);
		} else if(matchesButton(button, CONFIGS.DEFENCE)) {
			sendChangePresetSkillInput(PresetObject.DEFENCE_INDEX);
		} else if(matchesButton(button, CONFIGS.HITPOINTS)) {
			sendChangePresetSkillInput(PresetObject.HITPOINTS_INDEX);
		} else if(matchesButton(button, CONFIGS.RANGE)) {
			sendChangePresetSkillInput(PresetObject.RANGE_INDEX);
		} else if(matchesButton(button, CONFIGS.MAGIC)) {
			sendChangePresetSkillInput(PresetObject.MAGIC_INDEX);
		} else if(matchesButton(button, CONFIGS.PRAYER)) {
			sendChangePresetSkillInput(PresetObject.PRAYER_INDEX);
		} else if(matchesButton(button, CONFIGS.EQUIP_ARROW_LEFT)) {
			sendPresetEquipment(viewingPreset);
		} else if(matchesButton(button, CONFIGS.SAVE)) {
			sendSavePreset();
		} else if(matchesButton(button, CONFIGS.DELETE)) {
			sendDeletePreset();
		} else if(matchesButton(button, CONFIGS.SPELL_BOOK)) {
			int currentSpellbook = viewingPreset.getSpellBook();
			if(currentSpellbook >= 2)
				currentSpellbook = 0;
			else
				currentSpellbook++;
			System.out.println("spellbook: "+currentSpellbook);
			this.viewingPreset.setSpellBook(currentSpellbook);
			sendPresetMagicAndPrayer(viewingPreset);
		} else if(matchesButton(button, CONFIGS.PRAYER_BOOK)) {
			viewingPreset.setPrayer(!viewingPreset.isPrayer());
			sendPresetMagicAndPrayer(viewingPreset);
		} else if(matchesButton(button, CONFIGS.GEAR_UP)) {
			gearUpPreset();
		} else {
			int index = PresetInterfaceConfigs.getSaveIndexForButton(button);
			if(index != -1) {
				if(playerPresets.size() > index) {
					PresetObject preset = playerPresets.get(index);
					if(preset != null)
						sendPresetSetup(preset);
					    sendPresetInventory(preset);
				}
				
			}
		}
	}
	
	public void sendChangePresetSkillInput(int index) {
		player.getTemporaryAttributtes().put(TemporaryAttributes.Key.CHANGE_PRESET_SKILL, index);
		player.getPackets().sendRunScript(108, new Object[] { "Enter level for "+Skills.SKILL_NAME[index].toLowerCase()+"..." });
	}
	
	public void changePresetSkill(int value, int skill) {
		if(value > 99)
			value = 99;
		if(value < 1 && skill != PresetObject.HITPOINTS_INDEX)
			value = 1;
		if(value < 10 && skill == PresetObject.HITPOINTS_INDEX)
			value = 10;
		if(this.viewingPreset != null) {
			this.viewingPreset.getSkills()[skill] = value;
			sendPresetSkills(viewingPreset);
		}
	}
	
	private PresetObject getCurrentPresetObject() {
		int skills[] = new int[7];
		skills[PresetObject.ATTACK_INDEX] = player.getSkills().getLevel(Skills.ATTACK);
		skills[PresetObject.STRENGTH_INDEX] = player.getSkills().getLevel(Skills.STRENGTH);
		skills[PresetObject.DEFENCE_INDEX] = player.getSkills().getLevel(Skills.DEFENCE);
		skills[PresetObject.RANGE_INDEX] = player.getSkills().getLevel(Skills.RANGE);
		skills[PresetObject.MAGIC_INDEX] = player.getSkills().getLevel(Skills.MAGIC);
		skills[PresetObject.PRAYER_INDEX] = player.getSkills().getLevel(Skills.PRAYER);
		skills[PresetObject.HITPOINTS_INDEX] = player.getSkills().getLevel(Skills.HITPOINTS);
		Item[] equipment = player.getEquipment().getItems().getItemsCopy();
		Item[] inventory = player.getInventory().getItems().getItemsCopy();
		boolean prayer = player.getPrayer().isAncientCurses();
		int spellbook = player.getCombatDefinitions().getSpellBook();
		PresetObject preset = new PresetObject(skills, equipment, inventory, prayer, spellbook);
		return preset;
	}
	
	public boolean matchesButton(int button, CONFIGS config) {
		return button == config.buttonId;
	}
	
	public PresetManager(Player player) {
		this.player = player;
		this.playerPresets = new ArrayList<PresetObject>(7);
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public List<PresetObject> getPlayerPresets() {
		return playerPresets;
	}

	public void setPlayerPresets(List<PresetObject> playerPresets) {
		this.playerPresets = playerPresets;
	}
	
}
