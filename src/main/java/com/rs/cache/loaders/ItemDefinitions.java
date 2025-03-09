package com.rs.cache.loaders;

import com.rs.Constants;
import com.rs.cache.Cache;
import com.rs.game.item.Item;
import com.rs.game.world.entity.player.Equipment;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.network.io.InputStream;
import com.rs.utility.Misc;
import com.rs.utility.Utils;

import java.util.Arrays;
import java.util.HashMap;

@SuppressWarnings("unused")
public final class ItemDefinitions {

	public static final ItemDefinitions[] itemsDefinitions;
	public short[] originalTextureIds;

	static { // that's why this is here
		itemsDefinitions = new ItemDefinitions[Utils.getItemDefinitionsSize()];
	}

	public int id;
	public boolean loaded;

	public int modelId;
	public String name;

	// model size information
	public int modelZoom;
	public int modelRotation1;
	public int modelRotation2;
	public int modelOffset1;
	public int modelOffset2;

	// extra information
	public int stackable;
	public int value;
	public boolean membersOnly;

	// wearing model information
	public int maleEquip1;
	public int femaleEquip1;
	public int maleEquip2;
	public int femaleEquip2;

	// options
	public String[] groundOptions;
	public String[] inventoryOptions;
	private short[] modifiedTextureIds;
	// model information
	public int[] originalModelColors;
	public int[] modifiedModelColors;
	public short[] originalTextureColors;
	public short[] modifiedTextureColors;
	public byte[] unknownArray1;
	public byte[] unknownArray3;
	public int[] unknownArray2;
	// extra information, not used for newer items
	public boolean grandExchange;

	public int maleEquipModelId3;
	public int femaleEquipModelId3;
	public int unknownInt1;
	public int unknownInt2;
	public int unknownInt3;
	public int unknownInt4;
	public int unknownInt5;
	public int unknownInt6;
	public int certId;
	public int certTemplateId;
	public int[] stackIds;
	public int[] stackAmounts;
	public int unknownInt7;
	public int unknownInt8;
	public int unknownInt9;
	public int unknownInt10;
	public int unknownInt11;
	public int teamId;
	public int lendId;
	public int lendTemplateId;
	public int unknownInt12;
	public int unknownInt13;
	public int unknownInt14;
	public int unknownInt15;
	public int unknownInt16;
	public int unknownInt17;
	public int unknownInt18;
	public int unknownInt19;
	public int unknownInt20;
	public int unknownInt21;
	public int unknownInt22;
	public int unknownInt23;
	public int equipSlot;
	public int equipType;

	// extra added
	public boolean noted;
	public boolean lended;

	public HashMap<Integer, Object> parameters;
	public HashMap<Integer, Integer> itemRequiriments;
	public int[] unknownArray5;
	public int[] unknownArray4;
	public byte[] unknownArray6;
	private int boughtId;
	private int boughtTemplateId;
	private int placeholderId;
	private int placeholderTemplateId;

	public boolean cox;

	public static final ItemDefinitions getItemDefinitions(int itemId) {
		if (itemId < 0 || itemId >= itemsDefinitions.length)
			itemId = 0;
		ItemDefinitions def = itemsDefinitions[itemId];
		if (def == null)
			itemsDefinitions[itemId] = def = new ItemDefinitions(itemId);
		return def;
	}

	public static final void clearItemsDefinitions() {
		for (int i = 0; i < itemsDefinitions.length; i++)
			itemsDefinitions[i] = null;
	}

	/*
	 * public static ItemDefinitions forName(String name) { ItemDefinitions def =
	 * ItemDefinitions.getItemDefinitions(7774); def.name = "RealityX Points (300)";
	 * 
	 * for (ItemDefinitions definition : itemsDefinitions) {
	 * 
	 * if (definition.name.equalsIgnoreCase(name)) { return definition;
	 * 
	 * } } return null; }
	 */

	public ItemDefinitions(int id) {
		this.id = id;
		setDefaultsVariableValues();
		setDefaultOptions();
		loadItemDefinitions();
	}

	public boolean isLoaded() {
		return loaded;
	}

	public final void loadItemDefinitions() {
		byte[] data = Cache.STORE.getIndexes()[19].getFile(getArchiveId(), getFileId());
		if (data == null) {
			// System.out.println("Failed loading Item " + id+".");
			return;
		}
		readOpcodeValues(new InputStream(data));
		if (certTemplateId != -1)
			toNote();
		if (lendTemplateId != -1)
			toLend();
		if (unknownValue1 != -1)
			toBind();
		if (getId() >= com.rs.Constants.OSRS_ITEMS_OFFSET)
			applyEquipData();
		loaded = true;
	}

	public void toNote() {
		// ItemDefinitions noteItem; //certTemplateId
		ItemDefinitions realItem = getItemDefinitions(certId);
		membersOnly = realItem.membersOnly;
		value = realItem.value;
		name = realItem.name;
		stackable = 1;
		noted = true;
	}

	public void toBind() {
		// ItemDefinitions lendItem; //lendTemplateId
		ItemDefinitions realItem = getItemDefinitions(unknownValue2);
		originalModelColors = realItem.originalModelColors;
		maleEquipModelId3 = realItem.maleEquipModelId3;
		femaleEquipModelId3 = realItem.femaleEquipModelId3;
		teamId = realItem.teamId;
		value = 0;
		membersOnly = realItem.membersOnly;
		name = realItem.name;
		inventoryOptions = new String[5];
		groundOptions = realItem.groundOptions;
		if (realItem.inventoryOptions != null)
			for (int optionIndex = 0; optionIndex < 4; optionIndex++)
				inventoryOptions[optionIndex] = realItem.inventoryOptions[optionIndex];
		inventoryOptions[4] = "Discard";
		maleEquip1 = realItem.maleEquip1;
		maleEquip2 = realItem.maleEquip2;
		femaleEquip1 = realItem.femaleEquip1;
		femaleEquip2 = realItem.femaleEquip2;
		parameters = realItem.parameters;
		equipSlot = realItem.equipSlot;
		equipType = realItem.equipType;
	}

	public void toLend() {
		// ItemDefinitions lendItem; //lendTemplateId
		ItemDefinitions realItem = getItemDefinitions(lendId);
		originalModelColors = realItem.originalModelColors;
		maleEquipModelId3 = realItem.maleEquipModelId3;
		femaleEquipModelId3 = realItem.femaleEquipModelId3;
		teamId = realItem.teamId;
		value = 0;
		membersOnly = realItem.membersOnly;
		name = realItem.name;
		inventoryOptions = new String[5];
		groundOptions = realItem.groundOptions;
		if (realItem.inventoryOptions != null)
			for (int optionIndex = 0; optionIndex < 4; optionIndex++)
				inventoryOptions[optionIndex] = realItem.inventoryOptions[optionIndex];
		inventoryOptions[4] = "Discard";
		maleEquip1 = realItem.maleEquip1;
		maleEquip2 = realItem.maleEquip2;
		femaleEquip1 = realItem.femaleEquip1;
		femaleEquip2 = realItem.femaleEquip2;
		parameters = realItem.parameters;
		equipSlot = realItem.equipSlot;
		equipType = realItem.equipType;
		lended = true;
	}

	public int getArchiveId() {
		return getId() >>> 8;
	}

	public int getFileId() {
		return 0xff & getId();
	}

	public boolean isDestroyItem() {
		if (inventoryOptions == null)
			return false;
		for (String option : inventoryOptions) {
			if (option == null)
				continue;
			if (option.equalsIgnoreCase("destroy"))
				return true;
		}
		return false;
	}

	public boolean containsOption(int i, String option) {
		if (inventoryOptions == null || inventoryOptions[i] == null || inventoryOptions.length <= i)
			return false;
		return inventoryOptions[i].equals(option);
	}

	public boolean containsOption(String option) {
		if (inventoryOptions == null)
			return false;
		for (String o : inventoryOptions) {
			if (o == null || !o.equals(option))
				continue;
			return true;
		}
		return false;
	}

	public boolean isWearItem() {
		return equipSlot != -1;
	}

	public boolean isWearItem(boolean male) {
		if (equipSlot < Equipment.SLOT_RING && (male ? getMaleWornModelId1() == -1 : getFemaleWornModelId1() == -1))
			return false;
		return equipSlot != -1;
	}

	public boolean hasSpecialBar() {
		if (parameters == null)
			return false;
		Object specialBar = parameters.get(686);
		if (specialBar != null && specialBar instanceof Integer)
			return (Integer) specialBar == 1;
		return false;
	}

	public int getAttackSpeed() {
		if (parameters == null)
			return 4;
		Object attackSpeed = parameters.get(14);
		if (attackSpeed != null && attackSpeed instanceof Integer)
			return (int) attackSpeed;
		return 4;
	}

	public int getStabAttack() {
		if (parameters == null)
			return 0;
		Object value = parameters.get(0);
		if (value != null && value instanceof Integer)
			return (int) value;
		return 0;
	}

	public int getSlashAttack() {
		if (parameters == null)
			return 0;
		Object value = parameters.get(1);
		if (value != null && value instanceof Integer)
			return (int) value;
		return 0;
	}

	public int getCrushAttack() {
		if (parameters == null)
			return 0;
		Object value = parameters.get(2);
		if (value != null && value instanceof Integer)
			return (int) value;
		return 0;
	}

	public int getMagicAttack() {
		if (parameters == null)
			return 0;
		Object value = parameters.get(3);
		if (value != null && value instanceof Integer)
			return (int) value;
		return 0;
	}

	public int getRangeAttack() {
		if (parameters == null)
			return 0;
		Object value = parameters.get(4);
		if (value != null && value instanceof Integer)
			return (int) value;
		return 0;
	}

	public int getStabDef() {
		if (parameters == null)
			return 0;
		Object value = parameters.get(5);
		if (value != null && value instanceof Integer)
			return (int) value;
		return 0;
	}

	public int getSlashDef() {
		if (parameters == null)
			return 0;
		Object value = parameters.get(6);
		if (value != null && value instanceof Integer)
			return (int) value;
		return 0;
	}

	public int getCrushDef() {
		if (parameters == null)
			return 0;
		Object value = parameters.get(7);
		if (value != null && value instanceof Integer)
			return (int) value;
		return 0;
	}

	public static ItemDefinitions forName(String name) {
		for (ItemDefinitions definition : itemsDefinitions) {
			if (definition.name.equalsIgnoreCase(name)) {
				return definition;
			}
		}
		return null;
	}

	public int getMagicDef() {
		if (parameters == null)
			return 0;
		Object value = parameters.get(8);
		if (value != null && value instanceof Integer)
			return (int) value;
		return 0;
	}

	public int getRangeDef() {
		if (parameters == null)
			return 0;
		Object value = parameters.get(9);
		if (value != null && value instanceof Integer)
			return (int) value;
		return 0;
	}

	public int getSummoningDef() {
		if (parameters == null)
			return 0;
		Object value = parameters.get(417);
		if (value != null && value instanceof Integer)
			return (int) value;
		return 0;
	}

	public int getAbsorveMeleeBonus() {
		if (parameters == null)
			return 0;
		Object value = parameters.get(967);
		if (value != null && value instanceof Integer)
			return (int) value;
		return 0;
	}

	public int getAbsorveMageBonus() {
		if (parameters == null)
			return 0;
		Object value = parameters.get(969);
		if (value != null && value instanceof Integer)
			return (int) value;
		return 0;
	}

	public int getAbsorveRangeBonus() {
		if (parameters == null)
			return 0;
		Object value = parameters.get(968);
		if (value != null && value instanceof Integer)
			return (int) value;
		return 0;
	}

	public int getStrengthBonus() {
		if (parameters == null)
			return 0;
		Object value = parameters.get(641);
		if (value != null && value instanceof Integer)
			return (int) value / 10;
		return 0;
	}

	public int getRangedStrBonus() {
		if (parameters == null)
			return 0;
		Object value = parameters.get(643);
		if (value != null && value instanceof Integer)
			return (int) value;
		return 0;
	}

	public int getMagicDamage() {
		if (parameters == null)
			return 0;
		Object value = parameters.get(685);
		if (value != null && value instanceof Integer)
			return (int) value;
		return 0;
	}

	public int getPrayerBonus() {
		if (parameters == null)
			return 0;
		Object value = parameters.get(11);
		if (value != null && value instanceof Integer)
			return (int) value;
		return 0;
	}

	public int getRenderAnimId() {
		if (parameters == null)
			return 1426;
		Object animId = parameters.get(644);
		if (animId != null && animId instanceof Integer)
			return (Integer) animId;
		return 1426;
	}

	public int getModelZoom() {
		return modelZoom;
	}

	public int getModelOffset1() {
		return modelOffset1;
	}

	public int getModelOffset2() {
		return modelOffset2;
	}

	public int getQuestId() {
		if (parameters == null)
			return -1;
		Object questId = parameters.get(861);
		if (questId != null && questId instanceof Integer)
			return (Integer) questId;
		return -1;
	}

	public HashMap<Integer, Integer> getCreateItemRequirements() {
		if (parameters == null)
			return null;
		HashMap<Integer, Integer> items = new HashMap<Integer, Integer>();
		int requiredId = -1;
		int requiredAmount = -1;
		for (int key : parameters.keySet()) {
			Object value = parameters.get(key);
			if (value instanceof String)
				continue;
			if (key >= 538 && key <= 770) {
				if (key % 2 == 0)
					requiredId = (Integer) value;
				else
					requiredAmount = (Integer) value;
				if (requiredId != -1 && requiredAmount != -1) {
					if (requiredId == 1)
						items.put(requiredId, requiredAmount);
					// return items;
					// else
					// items.put(requiredAmount, requiredId);
					requiredId = -1;
					requiredAmount = -1;
				}
			}
		}
		return items;
	}

	public HashMap<Integer, Object> getClientScriptData() {
		return parameters;
	}

	public HashMap<Integer, Integer> getWearingSkillRequiriments() {
		if (parameters == null)
			return null;
		if (itemRequiriments == null) {
			HashMap<Integer, Integer> skills = new HashMap<Integer, Integer>();
			for (int i = 0; i < 10; i++) {
				Integer skill = (Integer) parameters.get(749 + (i * 2));
				if (skill != null) {
					Integer level = (Integer) parameters.get(750 + (i * 2));
					if (level != null)
						skills.put(skill, level);
				}
			}
			Integer maxedSkill = (Integer) parameters.get(277);
			if (maxedSkill != null)
				skills.put(maxedSkill, getId() == 19709 ? 120 : 99);
			itemRequiriments = skills;

			if (getId() == 7462) {
				itemRequiriments.put(Skills.DEFENCE, 40);
			} if (getId() == 19784 || getId() == 22401 || getId() == 19780) {
				itemRequiriments.put(Skills.ATTACK, 78);
				itemRequiriments.put(Skills.STRENGTH, 78);
			} if (name.contains("Inferno adze")) {
				itemRequiriments.put(Skills.FIREMAKING, 92);
			}else if (getId() == 20822 || getId() == 20823 || getId() == 20824 || getId() == 20825 || getId() == 20826) {
				itemRequiriments.put(Skills.DEFENCE, 99);
			}else if (name.equals("Dragon defender")) {
				itemRequiriments.put(Skills.ATTACK, 60);
				itemRequiriments.put(Skills.DEFENCE, 60);
			}else if (getId() == 29965) {
				itemRequiriments.put(Skills.ATTACK, 75);
			}else if (getId() == 29953) {
				itemRequiriments.put(Skills.MAGIC, 75);
			}else if (getId() == 29951) {
				itemRequiriments.put(Skills.MAGIC, 70);
			}else if (getId() == 29943) {
				itemRequiriments.put(Skills.RANGE, 75);
				//custom
			}else if (getId() == 29966) {
				itemRequiriments.put(Skills.RANGE, 75);

			}else if (getId() == 29949) {
				itemRequiriments.put(Skills.MAGIC, 75);

			}else if (getId() == 29945) {
				itemRequiriments.put(Skills.ATTACK, 75);

			}else if (getId() == 29947) {
				itemRequiriments.put(Skills.RANGE, 87);

			}else if (getId() == 29939) {
				itemRequiriments.put(Skills.STRENGTH, 60);

			}else if (getId() == 29927) {
				itemRequiriments.put(Skills.DEFENCE, 75);

			}else if (getId() == 29926) {
				itemRequiriments.put(Skills.DEFENCE, 75);

			}else if (getId() == 29929) {
				itemRequiriments.put(Skills.RANGE, 65);

			}else if (getId() == 29895) {
				itemRequiriments.put(Skills.ATTACK, 87);

			}else if (getId() == 29893) {
				itemRequiriments.put(Skills.MAGIC, 75);
				itemRequiriments.put(Skills.ATTACK, 75);

			}else if (getId() == 29889) {
				itemRequiriments.put(Skills.MAGIC, 75);
				itemRequiriments.put(Skills.ATTACK, 75);

			}else if (getId() == 29881 || getId() == 28654) {
				itemRequiriments.put(Skills.ATTACK, 85);

			}else if (getId() == 29877) {
				itemRequiriments.put(Skills.ATTACK, 85);
				itemRequiriments.put(Skills.MAGIC, 85);

			}else if (getId() == 29879) {
				itemRequiriments.put(Skills.ATTACK, 85);
				itemRequiriments.put(Skills.RANGE, 85);

			}else if (getId() == 29874) {
				itemRequiriments.put(Skills.DEFENCE, 75);
				itemRequiriments.put(Skills.MAGIC, 75);

			}else if (getId() == 29872) {
				itemRequiriments.put(Skills.ATTACK, 75);

			}else if (getId() == 29866) {
				itemRequiriments.put(Skills.ATTACK, 87);
				itemRequiriments.put(Skills.MAGIC, 87);

			}else if (getId() == 29864) {
				itemRequiriments.put(Skills.RANGE, 75);

			}else if (getId() == 29788) {
				itemRequiriments.put(Skills.ATTACK, 87);

			}else if (getId() == 29790) {
				itemRequiriments.put(Skills.ATTACK, 87);
				itemRequiriments.put(Skills.MAGIC, 87);

			}else if (getId() == 29784) {
				itemRequiriments.put(Skills.ATTACK, 87);
				itemRequiriments.put(Skills.MAGIC, 87);

			}else if (getId() == 29786) {
				itemRequiriments.put(Skills.ATTACK, 87);

			}else if (getId() == 29760) {
				itemRequiriments.put(Skills.DEFENCE, 87);

			}else if (getId() == 29756) {
				itemRequiriments.put(Skills.DEFENCE, 87);

			}else if (getId() == 29758) {
				itemRequiriments.put(Skills.DEFENCE, 87);

			}else if (getId() == 29750) {
				itemRequiriments.put(Skills.ATTACK, 87);

			}else if (getId() == 29743) {
				itemRequiriments.put(Skills.DEFENCE, 75);

			}else if (getId() == 29741) {
				itemRequiriments.put(Skills.ATTACK, 60);
				itemRequiriments.put(Skills.FISHING, 60);

			}else if (getId() == 29739) {
				itemRequiriments.put(Skills.ATTACK, 60);

			}else if (getId() == 29736) {
				itemRequiriments.put(Skills.DEFENCE, 75);

			}else if (getId() == 29726) {
				itemRequiriments.put(Skills.ATTACK, 75);

			}else if (getId() == 29724) {
				itemRequiriments.put(Skills.RANGE, 75);

			}else if (getId() == 29722) {
				itemRequiriments.put(Skills.MAGIC, 75);

			}else if (getId() == 29720) {
				itemRequiriments.put(Skills.RANGE, 65);

			}else if (getId() == 29718) {
				itemRequiriments.put(Skills.MAGIC, 65);

			}else if (getId() == 29706) {
				itemRequiriments.put(Skills.ATTACK, 75);

			}else if (getId() == 29704) {
				itemRequiriments.put(Skills.ATTACK, 75);

			}else if (getId() == 29663) {
				itemRequiriments.put(Skills.ATTACK, 75);

			}else if (getId() == 29659) {
				itemRequiriments.put(Skills.ATTACK, 75);

			}else if (getId() == 29639) {
				itemRequiriments.put(Skills.ATTACK, 60);

			}else if (getId() == 29521) {
				itemRequiriments.put(Skills.DEFENCE, 80);

			}else if (getId() == 29493) {
				itemRequiriments.put(Skills.DEFENCE, 87);
				itemRequiriments.put(Skills.MAGIC, 87);

			}else if (getId() == 29491) {
				itemRequiriments.put(Skills.DEFENCE, 87);
				itemRequiriments.put(Skills.MAGIC, 87);

			}else if (getId() == 29489) {
				itemRequiriments.put(Skills.DEFENCE, 87);
				itemRequiriments.put(Skills.MAGIC, 87);

			}else if (getId() == 29235) {
				itemRequiriments.put(Skills.DEFENCE, 87);
				itemRequiriments.put(Skills.STRENGTH, 87);

			}else if (getId() == 29233) {
				itemRequiriments.put(Skills.DEFENCE, 87);
				itemRequiriments.put(Skills.STRENGTH, 87);

			}else if (getId() == 29231) {
				itemRequiriments.put(Skills.DEFENCE, 87);
				itemRequiriments.put(Skills.STRENGTH, 87);

			}else if (getId() == 29225) {
				itemRequiriments.put(Skills.DEFENCE, 87);
				itemRequiriments.put(Skills.ATTACK, 87);

			}else if (getId() == 29223) {
				itemRequiriments.put(Skills.DEFENCE, 87);
				itemRequiriments.put(Skills.ATTACK, 87);

			}else if (getId() == 29221) {
				itemRequiriments.put(Skills.DEFENCE, 87);
				itemRequiriments.put(Skills.ATTACK, 87);

			}else if (getId() == 29216) {
				itemRequiriments.put(Skills.DEFENCE, 87);
				itemRequiriments.put(Skills.ATTACK, 87);

			}else if (getId() == 29214) {
				itemRequiriments.put(Skills.DEFENCE, 87);
				itemRequiriments.put(Skills.ATTACK, 87);

			}else if (getId() == 29218) {
				itemRequiriments.put(Skills.DEFENCE, 87);
				itemRequiriments.put(Skills.ATTACK, 87);

			}else if (getId() == 29197) {
				itemRequiriments.put(Skills.DEFENCE, 87);
				itemRequiriments.put(Skills.MAGIC, 87);

			}else if (getId() == 29195) {
				itemRequiriments.put(Skills.DEFENCE, 87);
				itemRequiriments.put(Skills.MAGIC, 87);

			}else if (getId() == 29199) {
				itemRequiriments.put(Skills.DEFENCE, 87);
				itemRequiriments.put(Skills.MAGIC, 87);

			}else if (getId() == 29115) {
				itemRequiriments.put(Skills.DEFENCE, 87);

			}else if (getId() == 28997) {
				itemRequiriments.put(Skills.ATTACK, 87);

			}else if (getId() == 28995) {
				itemRequiriments.put(Skills.ATTACK, 87);
				itemRequiriments.put(Skills.MAGIC, 87);

			}else if (getId() == 28929) {
				itemRequiriments.put(Skills.ATTACK, 80);

			}else if (getId() == 28987) {
				itemRequiriments.put(Skills.ATTACK, 87);

			}else if (getId() == 28979) {
				itemRequiriments.put(Skills.ATTACK, 87);

			}else if (getId() == 28975) {
				itemRequiriments.put(Skills.ATTACK, 87);

			}else if (getId() == 28973) {
				itemRequiriments.put(Skills.ATTACK, 85);

			}else if (getId() == 28971) {
				itemRequiriments.put(Skills.ATTACK, 87);
				itemRequiriments.put(Skills.MAGIC, 87);

			}else if (getId() == 28963) {
				itemRequiriments.put(Skills.ATTACK, 87);
				itemRequiriments.put(Skills.MAGIC, 87);

			}else if (getId() == 28967) {
				itemRequiriments.put(Skills.ATTACK, 87);

			}else if (getId() == 28991) {
				itemRequiriments.put(Skills.ATTACK, 87);

			}else if (getId() == 28931) {
				itemRequiriments.put(Skills.ATTACK, 85);
				itemRequiriments.put(Skills.RANGE, 85);

			}else if (getId() == 28906) {
				itemRequiriments.put(Skills.ATTACK, 87);
				itemRequiriments.put(Skills.RANGE, 87);

			}else if (getId() == 28904) {
				itemRequiriments.put(Skills.ATTACK, 87);
				itemRequiriments.put(Skills.RANGE, 87);

			}else if (getId() == 28902) {
				itemRequiriments.put(Skills.ATTACK, 87);
				itemRequiriments.put(Skills.RANGE, 87);

			}else if (getId() == 28900) {
				itemRequiriments.put(Skills.ATTACK, 87);
				itemRequiriments.put(Skills.RANGE, 87);

			}else if (getId() == 28898) {
				itemRequiriments.put(Skills.ATTACK, 87);
				itemRequiriments.put(Skills.RANGE, 87);

			}else if (getId() == 28894) {
				itemRequiriments.put(Skills.ATTACK, 87);

			}else if (getId() == 28898) {
				itemRequiriments.put(Skills.ATTACK, 87);
				itemRequiriments.put(Skills.MAGIC, 87);

			}else if (getId() == 28892) {
				itemRequiriments.put(Skills.RANGE, 87);

			}else if (getId() == 28867) {
				itemRequiriments.put(Skills.DEFENCE, 87);
				itemRequiriments.put(Skills.MAGIC, 87);

			}else if (getId() == 28865) {
				itemRequiriments.put(Skills.DEFENCE, 87);
				itemRequiriments.put(Skills.MAGIC, 87);

			}else if (getId() == 28863) {
				itemRequiriments.put(Skills.DEFENCE, 87);
				itemRequiriments.put(Skills.MAGIC, 87);

			}else if (getId() == 28861) {
				itemRequiriments.put(Skills.ATTACK, 87);
				itemRequiriments.put(Skills.RANGE, 87);

			}else if (getId() == 28816) {
				itemRequiriments.put(Skills.ATTACK, 87);
				itemRequiriments.put(Skills.RANGE, 87);

			}else if (getId() == 28852) {
				itemRequiriments.put(Skills.ATTACK, 80);

			}else if (getId() == 28850) {
				itemRequiriments.put(Skills.ATTACK, 80);
				itemRequiriments.put(Skills.MAGIC, 80);

			}else if (getId() == 28854) {
				itemRequiriments.put(Skills.ATTACK, 80);
				itemRequiriments.put(Skills.MAGIC, 80);

			}else if (getId() == 28845) {
				itemRequiriments.put(Skills.RANGE, 80);

			}else if (getId() == 28843) {
				itemRequiriments.put(Skills.RANGE, 80);

			}else if (getId() == 28847) {
				itemRequiriments.put(Skills.RANGE, 80);

			}else if (getId() == 28828) {
				itemRequiriments.put(Skills.ATTACK, 87);
				itemRequiriments.put(Skills.MAGIC, 87);

			}else if (getId() == 28826) {
				itemRequiriments.put(Skills.ATTACK, 87);

			}else if (getId() == 28835) {
				itemRequiriments.put(Skills.DEFENCE, 80);

			}else if (getId() == 28814) {
				itemRequiriments.put(Skills.ATTACK, 87);

			}else if (getId() == 28813) {
				itemRequiriments.put(Skills.ATTACK, 87);
				itemRequiriments.put(Skills.MAGIC, 87);

			}else if (getId() == 28810) {
				itemRequiriments.put(Skills.DEFENCE, 87);

			}else if (getId() == 28808) {
				itemRequiriments.put(Skills.DEFENCE, 87);

			}else if (getId() == 28806) {
				itemRequiriments.put(Skills.DEFENCE, 87);

			}else if (getId() == 28810) {
				itemRequiriments.put(Skills.DEFENCE, 87);

			}else if (getId() == 28808) {
				itemRequiriments.put(Skills.DEFENCE, 87);

			}else if (getId() == 28806) {
				itemRequiriments.put(Skills.DEFENCE, 87);

			}else if (getId() == 28772) {
				itemRequiriments.put(Skills.ATTACK, 87);
				itemRequiriments.put(Skills.MAGIC, 87);

			}else if (getId() == 28768) {
				itemRequiriments.put(Skills.ATTACK, 87);
				itemRequiriments.put(Skills.MAGIC, 87);

			}else if (getId() == 28770) {
				itemRequiriments.put(Skills.ATTACK, 87);
				itemRequiriments.put(Skills.MAGIC, 87);

			}else if (getId() == 29236) {
				itemRequiriments.put(Skills.DEFENCE, 87);

			}else if (getId() == 29238) {
				itemRequiriments.put(Skills.DEFENCE, 87);

			}else if (getId() == 28791) {
				itemRequiriments.put(Skills.RANGE, 87);

			}else if (getId() == 28791) {
				itemRequiriments.put(Skills.RANGE, 87);

			}else if (getId() == 28876) {
				itemRequiriments.put(Skills.ATTACK, 90);

				//custom end
			}else if (getId() == 29939) {
				itemRequiriments.put(Skills.ATTACK, 60);	
			}else if (getId() == 29931) {
				itemRequiriments.put(Skills.DEFENCE, 75);	
			}else if (getId() == 29929) {
				itemRequiriments.put(Skills.DEFENCE, 75);	
			}else if (getId() == 29927) {
				itemRequiriments.put(Skills.DEFENCE, 75);	
			}else if (getId() == 29926) {
				itemRequiriments.put(Skills.DEFENCE, 75);	
			}else if (getId() == 29925) {
				itemRequiriments.put(Skills.MAGIC, 75);	
			}else if (getId() == 29899) {
				itemRequiriments.put(Skills.MAGIC, 75);	
			}else if (getId() == 29897) {
				itemRequiriments.put(Skills.ATTACK, 60);
				itemRequiriments.put(Skills.RANGE, 60);
			}else if (getId() == 29895) {
				itemRequiriments.put(Skills.ATTACK, 75);
				itemRequiriments.put(Skills.STRENGTH, 75);
			}else if (getId() == 28670) {
				itemRequiriments.put(Skills.MAGIC, 75);
			}else if (getId() == 28668) {
				itemRequiriments.put(Skills.ATTACK, 75);
			}else if (getId() == 29893) {
				itemRequiriments.put(Skills.ATTACK, 75);
				itemRequiriments.put(Skills.MAGIC, 75);	
			}else if (getId() == 29891) {
				itemRequiriments.put(Skills.ATTACK, 75);
				itemRequiriments.put(Skills.MAGIC, 75);	
			}else if (getId() == 29889) {
				itemRequiriments.put(Skills.ATTACK, 75);
				itemRequiriments.put(Skills.MAGIC, 75);	
			}else if (getId() == 29885) {
				itemRequiriments.put(Skills.ATTACK, 90);
				itemRequiriments.put(Skills.MAGIC, 90);	
			}else if (getId() == 29883) {
				itemRequiriments.put(Skills.ATTACK, 90);
				itemRequiriments.put(Skills.MAGIC, 90);	
			}if (getId() == 29881) {
				itemRequiriments.put(Skills.ATTACK, 90);
				itemRequiriments.put(Skills.STRENGTH, 90);	
			}else if (getId() == 29879) {
				itemRequiriments.put(Skills.ATTACK, 90);
				itemRequiriments.put(Skills.RANGE, 90);	
			}else if (getId() == 29877) {
				itemRequiriments.put(Skills.ATTACK, 90);	
				itemRequiriments.put(Skills.MAGIC, 90);	
			}else if (getId() == 29874) {
				itemRequiriments.put(Skills.DEFENCE, 75);	
				itemRequiriments.put(Skills.MAGIC, 75);		
			}else if (getId() == 29872) {
				itemRequiriments.put(Skills.ATTACK, 65);		
			}else if (getId() == 29870) {
				itemRequiriments.put(Skills.ATTACK, 60);	
			}else if (getId() == 29866) {
				itemRequiriments.put(Skills.ATTACK, 75);	
				itemRequiriments.put(Skills.MAGIC, 75);
			}else if (getId() == 29864) {
				itemRequiriments.put(Skills.DEFENCE, 75);	
				itemRequiriments.put(Skills.RANGE, 75);
			}else if (getId() == 29915) {
				itemRequiriments.put(Skills.MINING, 120);
			}else if (getId() == 29904) {
				itemRequiriments.put(Skills.AGILITY, 120);
			}else if (getId() == 29901) {
				itemRequiriments.put(Skills.ATTACK, 120);
			}else if (getId() == 29902) {
				itemRequiriments.put(Skills.CONSTRUCTION, 120);
			}else if (getId() == 29905){
				itemRequiriments.put(Skills.COOKING, 120);
			}else if (getId() == 29906){
				itemRequiriments.put(Skills.CRAFTING, 120);
			}else if (getId() == 29907){
				itemRequiriments.put(Skills.DEFENCE, 120);
			}else if (getId() == 29908){
				itemRequiriments.put(Skills.FARMING, 120);
			}else if (getId() == 29909){
				itemRequiriments.put(Skills.FIREMAKING, 120);
			}else if (getId() == 29910){
				itemRequiriments.put(Skills.FISHING, 120);
			}else if (getId() == 29911){
				itemRequiriments.put(Skills.FLETCHING, 120);
			}else if (getId() == 29912){
				itemRequiriments.put(Skills.HERBLORE, 120);
			}else if (getId() == 29900){
				itemRequiriments.put(Skills.HITPOINTS, 120);
			}else if (getId() == 29913){
				itemRequiriments.put(Skills.HUNTER, 120);
			}else if (getId() == 29914){
				itemRequiriments.put(Skills.MAGIC, 120);
			}else if (getId() == 29916){
				itemRequiriments.put(Skills.PRAYER, 120);
			}else if (getId() == 29918){
				itemRequiriments.put(Skills.RANGE, 120);
			}else if (getId() == 29919){
				itemRequiriments.put(Skills.RUNECRAFTING, 120);
			}else if (getId() == 29920){
				itemRequiriments.put(Skills.SLAYER, 120);
			}else if (getId() == 29921){
				itemRequiriments.put(Skills.SMITHING, 120);
			}else if (getId() == 29903){
				itemRequiriments.put(Skills.STRENGTH, 120);
			}else if (getId() == 29922){
				itemRequiriments.put(Skills.SUMMONING, 120);
			}else if (getId() == 29923){
				itemRequiriments.put(Skills.THIEVING, 120);
			}else if (getId() == 28906){
				itemRequiriments.put(Skills.WOODCUTTING, 120);
			}else if (getId() == 28907){
				itemRequiriments.put(Skills.THIEVING, 120);
			}

		}
		return itemRequiriments;
	}

	public void setDefaultOptions() {
		groundOptions = new String[] { null, null, "take", null, null };
		inventoryOptions = new String[] { null, null, null, null, "drop" };
	}

	public void setDefaultsVariableValues() {
		name = "null";
		maleEquip1 = -1;
		maleEquip2 = -1;
		femaleEquip1 = -1;
		femaleEquip2 = -1;
		modelZoom = 2000;
		lendId = -1;
		lendTemplateId = -1;
		certId = -1;
		certTemplateId = -1;
		unknownInt9 = 128;
		value = 1;
		maleEquipModelId3 = -1;
		femaleEquipModelId3 = -1;
		unknownValue1 = -1;
		unknownValue2 = -1;
		teamId = 0;
		equipSlot = -1;
		equipType = -1;
	}

	public final void readValues(InputStream stream, int opcode) {
		if (opcode == 1)
			modelId = stream.readBigSmart();
		else if (opcode == 2)
			name = stream.readString();
		else if (opcode == 4)
			modelZoom = stream.readUnsignedShort();
		else if (opcode == 5)
			modelRotation1 = stream.readUnsignedShort();
		else if (opcode == 6)
			modelRotation2 = stream.readUnsignedShort();
		else if (opcode == 7) {
			modelOffset1 = stream.readUnsignedShort();
			if (modelOffset1 > 32767)
				modelOffset1 -= 65536;
			modelOffset1 <<= 0;
		} else if (opcode == 8) {
			modelOffset2 = stream.readUnsignedShort();
			if (modelOffset2 > 32767)
				modelOffset2 -= 65536;
			modelOffset2 <<= 0;
		} else if (opcode == 11)
			stackable = 1;
		else if (opcode == 12)
			value = stream.readInt();
		else if (opcode == 13) {
			equipSlot = stream.readUnsignedByte();
		} else if (opcode == 14) {
			equipType = stream.readUnsignedByte();
		} else if (opcode == 16)
			membersOnly = true;
		else if (opcode == 18) { // added
			stream.readUnsignedShort();
		} else if (opcode == 23)
			maleEquip1 = stream.readBigSmart();
		else if (opcode == 24)
			maleEquip2 = stream.readBigSmart();
		else if (opcode == 25)
			femaleEquip1 = stream.readBigSmart();
		else if (opcode == 26)
			femaleEquip2 = stream.readBigSmart();
		else if (opcode == 27)
			stream.readUnsignedByte();
		else if (opcode >= 30 && opcode < 35)
			groundOptions[opcode - 30] = stream.readString();
		else if (opcode >= 35 && opcode < 40)
			inventoryOptions[opcode - 35] = stream.readString();
		else if (opcode == 40) {
			int length = stream.readUnsignedByte();
			originalModelColors = new int[length];
			modifiedModelColors = new int[length];
			for (int index = 0; index < length; index++) {
				originalModelColors[index] = stream.readUnsignedShort();
				modifiedModelColors[index] = stream.readUnsignedShort();
			}
		} else if (opcode == 41) {
			int length = stream.readUnsignedByte();
			originalTextureIds = new short[length];
			modifiedTextureIds = new short[length];
			for (int index = 0; index < length; index++) {
				originalTextureIds[index] = (short) stream.readUnsignedShort();
				modifiedTextureIds[index] = (short) stream.readUnsignedShort();
			}
		} else if (opcode == 42) {
			int length = stream.readUnsignedByte();
			unknownArray1 = new byte[length];
			for (int index = 0; index < length; index++)
				unknownArray1[index] = (byte) stream.readByte();
		} else if (opcode == 44) {
			int length = stream.readUnsignedShort();
			int arraySize = 0;
			for (int modifier = 0; modifier > 0; modifier++) {
				arraySize++;
				unknownArray3 = new byte[arraySize];
				byte offset = 0;
				for (int index = 0; index < arraySize; index++) {
					if ((length & 1 << index) > 0) {
						unknownArray3[index] = offset;
					} else {
						unknownArray3[index] = -1;
					}
				}
			}
		} else if (45 == opcode) {
			int i_97_ = (short) stream.readUnsignedShort();
			int i_98_ = 0;
			for (int i_99_ = i_97_; i_99_ > 0; i_99_ >>= 1)
				i_98_++;
			unknownArray6 = new byte[i_98_];
			byte i_100_ = 0;
			for (int i_101_ = 0; i_101_ < i_98_; i_101_++) {
				if ((i_97_ & 1 << i_101_) > 0) {
					unknownArray6[i_101_] = i_100_;
					i_100_++;
				} else
					unknownArray6[i_101_] = (byte) -1;
			}
		} else if (opcode == 65)
			grandExchange = true;
		else if (opcode == 78)
			maleEquipModelId3 = stream.readBigSmart();
		else if (opcode == 79)
			femaleEquipModelId3 = stream.readBigSmart();
		else if (opcode == 90)
			unknownInt1 = stream.readBigSmart();
		else if (opcode == 91)
			unknownInt2 = stream.readBigSmart();
		else if (opcode == 92)
			unknownInt3 = stream.readBigSmart();
		else if (opcode == 93)
			unknownInt4 = stream.readBigSmart();
		else if (opcode == 95)
			unknownInt5 = stream.readUnsignedShort();
		else if (opcode == 96)
			unknownInt6 = stream.readUnsignedByte();
		else if (opcode == 97)
			certId = stream.readUnsignedShort();
		else if (opcode == 98)
			certTemplateId = stream.readUnsignedShort();
		else if (opcode >= 100 && opcode < 110) {
			if (stackIds == null) {
				stackIds = new int[10];
				stackAmounts = new int[10];
			}
			stackIds[opcode - 100] = stream.readUnsignedShort();
			stackAmounts[opcode - 100] = stream.readUnsignedShort();
		} else if (opcode == 110)
			unknownInt7 = stream.readUnsignedShort();
		else if (opcode == 111)
			unknownInt8 = stream.readUnsignedShort();
		else if (opcode == 112)
			unknownInt9 = stream.readUnsignedShort();
		else if (opcode == 113)
			unknownInt10 = stream.readByte();
		else if (opcode == 114)
			unknownInt11 = stream.readByte() * 5;
		else if (opcode == 115)
			teamId = stream.readUnsignedByte();
		else if (opcode == 121)
			lendId = stream.readUnsignedShort();
		else if (opcode == 122)
			lendTemplateId = stream.readUnsignedShort();
		else if (opcode == 125) {
			unknownInt12 = stream.readByte() << 0;
			unknownInt13 = stream.readByte() << 0;
			unknownInt14 = stream.readByte() << 0;
		} else if (opcode == 126) {
			unknownInt15 = stream.readByte() << 0;
			unknownInt16 = stream.readByte() << 0;
			unknownInt17 = stream.readByte() << 0;
		} else if (opcode == 127) {
			unknownInt18 = stream.readUnsignedByte();
			unknownInt19 = stream.readUnsignedShort();
		} else if (opcode == 128) {
			unknownInt20 = stream.readUnsignedByte();
			unknownInt21 = stream.readUnsignedShort();
		} else if (opcode == 129) {
			unknownInt20 = stream.readUnsignedByte();
			unknownInt21 = stream.readUnsignedShort();
		} else if (opcode == 130) {
			unknownInt22 = stream.readUnsignedByte();
			unknownInt23 = stream.readUnsignedShort();
		} else if (opcode == 132) {
			int length = stream.readUnsignedByte();
			unknownArray2 = new int[length];
			for (int index = 0; index < length; index++)
				unknownArray2[index] = stream.readUnsignedShort();
		} else if (opcode == 134) {
			int unknownValue = stream.readUnsignedByte();
		} else if (opcode == 139) {
			unknownValue2 = stream.readUnsignedShort();
		} else if (opcode == 140) {
			unknownValue1 = stream.readUnsignedShort();
		} else if (opcode >= 142 && opcode < 147) {
			if (unknownArray4 == null) {
				unknownArray4 = new int[6];
				Arrays.fill(unknownArray4, -1);
			}
			unknownArray4[opcode - 142] = stream.readUnsignedShort();
		} else if (opcode >= 150 && opcode < 155) {
			if (null == unknownArray5) {
				unknownArray5 = new int[5];
				Arrays.fill(unknownArray5, -1);
			}
			unknownArray5[opcode - 150] = stream.readUnsignedShort();
		} else if (opcode == 142) {
			stream.readByte();
			stream.readByte();
		} else if (opcode == 144) {
			stream.readByte();
			stream.readByte();
			stream.readByte();
		} else if (opcode == 151) {
			stream.readByte();
			stream.readByte();
		} else if (opcode == 249) {
			int length = stream.readUnsignedByte();
			if (parameters == null)
				parameters = new HashMap<Integer, Object>(length);
			for (int index = 0; index < length; index++) {
				boolean stringInstance = stream.readUnsignedByte() == 1;
				int key = stream.read24BitInt();
				Object value = stringInstance ? stream.readString() : stream.readInt();
				parameters.put(key, value);
			}
		} else
			throw new RuntimeException("MISSING OPCODE " + opcode + " FOR ITEM " + getId());
	}

	public int unknownValue1;
	public int unknownValue2;

	public final void readOpcodeValues(InputStream stream) {
		while (true) {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0)
				break;
			if (getId() >= com.rs.Constants.OSRS_ITEMS_OFFSET)
				readOSRS(stream, opcode);
			else
				readValues(stream, opcode);
		}
	}
	
	public void readOSRS(InputStream stream, int opcode) {
		if (opcode == 1) {
			modelId = stream.readUnsignedShort();
		} else if (opcode == 2) {
			name = stream.readString();
		} else if (opcode == 4) {
			modelZoom = stream.readUnsignedShort();
		} else if (opcode == 5) {
			modelRotation1 = stream.readUnsignedShort();
		} else if (opcode == 6) {
			modelRotation2 = stream.readUnsignedShort();
		} else if (opcode == 7) {
			modelOffset1 = stream.readUnsignedShort();
			if (modelOffset1 > 32767) {
				modelOffset1 -= 65536;
			}
		} else if (opcode == 8) {
			modelOffset2 = stream.readUnsignedShort();
			if (modelOffset2 > 32767) {
				modelOffset2 -= 65536;
			}
		} else if (opcode == 11) {
			stackable = 1;
		} else if (opcode == 12) {
			value = stream.readInt();
		} else if (opcode == 16) {
			membersOnly = true;
		} else if (opcode == 23) {
			maleEquip1 = stream.readUnsignedShort();
			unknownInt12 = stream.readUnsignedByte();
		} else if (opcode == 24) {
			maleEquip2 = stream.readUnsignedShort();
		} else if (opcode == 25) {
			femaleEquip1 = stream.readUnsignedShort();
			unknownInt15 = stream.readUnsignedByte();
		} else if (opcode == 26) {
			femaleEquip2 = stream.readUnsignedByte();
		} else if (opcode >= 30 && opcode < 35) {
			groundOptions[opcode - 30] = stream.readString();
			if (groundOptions[opcode - 30].equalsIgnoreCase("hidden")) {
				groundOptions[opcode - 30] = null;
			}
		} else if (opcode >= 35 && opcode < 40) {
			inventoryOptions[opcode - 35] = stream.readString();
		} else if (opcode == 40) {
			int length = stream.readUnsignedByte();
			originalModelColors = new int[length];
			modifiedModelColors = new int[length];
			for (int index = 0; index < length; index++) {
				originalModelColors[index] = (short) stream.readUnsignedShort();
				modifiedModelColors[index] = (short) stream.readUnsignedShort();
			}
		} else if (opcode == 41) {
			int length = stream.readUnsignedByte();
			originalTextureIds = new short[length];
			modifiedTextureIds = new short[length];
			for (int index = 0; index < length; index++) {
				originalTextureIds[index] = (short) stream.readUnsignedShort();
				modifiedTextureIds[index] = (short) stream.readUnsignedShort();
			}
		} else if (opcode == 65) {
			grandExchange = true;
		} else if (opcode == 78) {
			maleEquip2 = stream.readUnsignedShort();
		} else if (opcode == 79) {
			femaleEquip2 = stream.readUnsignedShort();
		} else if (opcode == 90) {
			unknownInt1 = stream.readUnsignedShort();
		} else if (opcode == 91) {
			unknownInt2 = stream.readUnsignedShort();
		} else if (opcode == 92) {
			unknownInt3 = stream.readUnsignedShort();
		} else if (opcode == 93) {
			unknownInt4 = stream.readUnsignedShort();
		} else if (opcode == 95) {
			unknownInt5 = stream.readUnsignedShort();
		} else if (opcode == 97) {
			certId = stream.readUnsignedShort() + Constants.OSRS_ITEMS_OFFSET;
		} else if (opcode == 98) {
			certTemplateId = stream.readUnsignedShort() + Constants.OSRS_ITEMS_OFFSET;
		} else if (opcode >= 100 && opcode < 110) {
			if (stackIds == null) {
				stackIds = new int[10];
				stackAmounts = new int[10];
			}
			stackIds[opcode - 100] = stream.readUnsignedShort() + Constants.OSRS_ITEMS_OFFSET;
			stackAmounts[opcode - 100] = stream.readUnsignedShort();
		} else if (opcode == 110) {
			unknownInt7 = stream.readUnsignedShort();
		} else if (opcode == 111) {
			unknownInt8 = stream.readUnsignedShort();
		} else if (opcode == 112) {
			unknownInt9 = stream.readUnsignedShort();
		} else if (opcode == 113) {
			unknownInt10 = stream.readUnsignedByte();
		} else if (opcode == 114) {
			unknownInt11 = stream.readUnsignedByte();
		} else if (opcode == 115) {
			teamId = stream.readUnsignedByte();
		} else if (opcode == 139) {
			boughtId = stream.readUnsignedShort() + Constants.OSRS_ITEMS_OFFSET;
		} else if (opcode == 140) {
			boughtTemplateId = stream.readUnsignedShort() + Constants.OSRS_ITEMS_OFFSET;
		} else if (opcode == 148) {
			placeholderId = stream.readUnsignedShort() + Constants.OSRS_ITEMS_OFFSET;
		} else if (opcode == 149) {
			placeholderTemplateId = stream.readUnsignedShort() + Constants.OSRS_ITEMS_OFFSET;
		} else if (opcode == 249) {
			int length = stream.readUnsignedByte();
			if (parameters == null)
				parameters = new HashMap<>();
			for (int index = 0; index < length; index++) {
				boolean isString = stream.readUnsignedByte() == 1;
				int key = stream.read24BitInt();
				Object value;
				if (isString)
					value = stream.readString();
				else
					value = stream.readInt();
				parameters.put(key, value);
			}
		}
	}

	public String getName() {
		return name;
	}

	public int getFemaleWornModelId1() {
		return femaleEquip1;
	}

	public int getFemaleWornModelId2() {
		return femaleEquip2;
	}

	public int getMaleWornModelId1() {
		return maleEquip1;
	}

	public int getMaleWornModelId2() {
		return maleEquip2;
	}

	public boolean isOverSized() {
		return modelZoom > 5000;
	}

	public boolean isLended() {
		return lended;
	}

	public boolean isMembersOnly() {
		return membersOnly;
	}

	public boolean isStackable() {
		return stackable == 1;
	}

	public boolean isNoted() {
		return noted;
	}

	public int getLendId() {
		return lendId;
	}

	public int getCertId() {
		return certId;
	}

	public int getValue() {
		return value;
	}

	public int getId() {
		return id;
	}

	public int getEquipSlot() {
		return equipSlot;
	}

	public int getEquipType() {
		return equipType;
	}

	public void getShopStats(Player player, Item item) {
		if (item.getDefinitions().name.contains("sword") || item.getDefinitions().name.contains("dagger")
				|| item.getDefinitions().name.contains("scimitar") || item.getDefinitions().name.contains("whip")
				|| item.getDefinitions().name.contains("spear") || item.getDefinitions().name.contains("mace")
				|| item.getDefinitions().name.contains("battleaxe") || item.getDefinitions().name.contains("staff")
				|| item.getDefinitions().name.contains("hatchet") || item.getDefinitions().name.contains("pickaxe")
				|| item.getDefinitions().name.contains("plate") || item.getDefinitions().name.contains("body")
				|| item.getDefinitions().name.contains("robe top") || item.getDefinitions().name.contains("top")
				|| item.getDefinitions().name.contains("jacket") || item.getDefinitions().name.contains("tabard")

				|| item.getDefinitions().name.contains("shirt") || item.getDefinitions().name.contains("apron")
				|| item.getDefinitions().name.contains("chest") || item.getDefinitions().name.contains("gloves")
				|| item.getDefinitions().name.contains("gauntlets") || item.getDefinitions().name.contains("vambraces")
				|| item.getDefinitions().name.contains("boots") || item.getDefinitions().name.contains("necklace")
				|| item.getDefinitions().name.contains("amulet") || item.getDefinitions().name.contains("skirt")
				|| item.getDefinitions().name.contains("kilt") || item.getDefinitions().name.contains("leggings")
				|| item.getDefinitions().name.contains("chaps") || item.getDefinitions().name.contains("pants")
				|| item.getDefinitions().name.contains("shorts") || item.getDefinitions().name.contains("legs")
				|| item.getDefinitions().name.contains("helm") || item.getDefinitions().name.contains("cap")
				|| item.getDefinitions().name.contains("hood") || item.getDefinitions().name.contains("coif")
				|| item.getDefinitions().name.contains("fez") || item.getDefinitions().name.contains("mask")
				|| item.getDefinitions().name.contains("paint") || item.getDefinitions().name.contains("visor")
				|| item.getDefinitions().name.contains("cavalier") || item.getDefinitions().name.contains("hat")
				|| item.getDefinitions().name.contains("shield") || item.getDefinitions().name.contains("book")
				|| item.getDefinitions().name.contains("shield") || item.getDefinitions().name.contains("2h")
				|| item.getDefinitions().name.contains("maul") || item.getDefinitions().name.contains("claws")
				|| item.getDefinitions().name.contains("cape") || item.getDefinitions().name.contains("ava's")
				|| item.getDefinitions().name.contains("cloak") || item.getDefinitions().name.contains("Cape")
				|| item.getDefinitions().name.contains("arrow") || item.getDefinitions().name.contains("bolt")
				|| item.getDefinitions().name.contains("ball") || item.getDefinitions().name.contains("chinchompa")
				|| item.getDefinitions().name.contains("dart") || item.getDefinitions().name.contains("knife")
				|| item.getDefinitions().name.contains("javelin") || item.getDefinitions().name.contains("holy water")
				|| item.getDefinitions().name.contains("bow") || item.getDefinitions().name.contains("Staff")
				|| item.getDefinitions().name.contains("staff") || item.getDefinitions().name.contains("wand")) {
			player.getPackets().sendGlobalConfig(1876, 0);
		} else {
			player.getPackets().sendGlobalConfig(1876, -1);
		}
	}

	public String getEquipType(Item item) {
		if (item.getDefinitions().name.contains("sword") || item.getDefinitions().name.contains("dagger")
				|| item.getDefinitions().name.contains("scimitar") || item.getDefinitions().name.contains("whip")
				|| item.getDefinitions().name.contains("spear") || item.getDefinitions().name.contains("mace")
				|| item.getDefinitions().name.contains("battleaxe") || item.getDefinitions().name.contains("staff")
				|| item.getDefinitions().name.contains("hatchet") || item.getDefinitions().name.contains("pickaxe")) {
			return "wielded in the right hand";
		}

		if (item.getDefinitions().name.contains("plate") || item.getDefinitions().name.contains("body")
				|| item.getDefinitions().name.contains("robe top") || item.getDefinitions().name.contains("top")
				|| item.getDefinitions().name.contains("jacket") || item.getDefinitions().name.contains("tabard")
				|| item.getDefinitions().name.contains("shirt") || item.getDefinitions().name.contains("apron")
				|| item.getDefinitions().name.contains("chest")) {
			return "worn on the torso";
		}
		if (item.getDefinitions().name.contains("gloves") || item.getDefinitions().name.contains("gauntlets")
				|| item.getDefinitions().name.contains("vambraces")) {
			return "worn on the hands";
		}
		if (item.getDefinitions().name.contains("boots")) {
			return "worn on the feet";
		}
		if (item.getDefinitions().name.contains("necklace") || item.getDefinitions().name.contains("amulet")) {
			return "worn on the neck";
		}
		if (item.getDefinitions().name.contains("skirt") || item.getDefinitions().name.contains("kilt")
				|| item.getDefinitions().name.contains("leggings") || item.getDefinitions().name.contains("chaps")
				|| item.getDefinitions().name.contains("pants") || item.getDefinitions().name.contains("shorts")
				|| item.getDefinitions().name.contains("legs")) {
			return "worn on the legs";
		}
		if (item.getDefinitions().name.contains("helm") || item.getDefinitions().name.contains("cap")
				|| item.getDefinitions().name.contains("hood") || item.getDefinitions().name.contains("coif")
				|| item.getDefinitions().name.contains("fez") || item.getDefinitions().name.contains("mask")
				|| item.getDefinitions().name.contains("paint") || item.getDefinitions().name.contains("visor")
				|| item.getDefinitions().name.contains("cavalier") || item.getDefinitions().name.contains("hat")) {
			return "worn on the head";
		}
		if (item.getDefinitions().name.contains("shield") || item.getDefinitions().name.contains("book")) {
			return "held in the left hand";
		}
		if (item.getDefinitions().name.contains("shield") || item.getDefinitions().name.contains("2h")
				|| item.getDefinitions().name.contains("maul") || item.getDefinitions().name.contains("claws")) {
			return "wielded in both hands";
		}
		if (item.getDefinitions().name.contains("cape") || item.getDefinitions().name.contains("ava's")
				|| item.getDefinitions().name.contains("cloak") || item.getDefinitions().name.contains("Cape")) {
			return "worn on the back";
		}
		return "an item";
	}

	public String getItemType(Item item) {
		if (item.getDefinitions().name.contains("sword") || item.getDefinitions().name.contains("dagger")
				|| item.getDefinitions().name.contains("scimitar") || item.getDefinitions().name.contains("maul")
				|| item.getDefinitions().name.contains("whip") || item.getDefinitions().name.contains("claws")
				|| item.getDefinitions().name.contains("spear") || item.getDefinitions().name.contains("mace")
				|| item.getDefinitions().name.contains("cane") || item.getDefinitions().name.contains("hasta")
				|| item.getDefinitions().name.contains("brackish blade")
				|| item.getDefinitions().name.contains("battleaxe")) {
			return "a melee weapon";
		}
		if (item.getDefinitions().name.contains("Staff") || item.getDefinitions().name.contains("wand")) {
			return "a weapon for mages";
		}
		if (item.getDefinitions().name.contains("body") || item.getDefinitions().name.contains("legs")
				|| item.getDefinitions().name.contains("robe") || item.getDefinitions().name.contains("priest")
				|| item.getDefinitions().name.contains("helm")) {
			return "a piece of apparel";
		}
		if (item.getDefinitions().name.contains("shield")) {
			return "a shield";
		}
		if (item.getDefinitions().name.contains("hatchet")) {
			return "a hatchet";
		}
		if (item.getDefinitions().name.contains("arrow") || item.getDefinitions().name.contains("bolt")
				|| item.getDefinitions().name.contains("ball")) {
			return "ammunition for a ranged weapon";
		}
		if (item.getDefinitions().name.contains("chinchompa") || item.getDefinitions().name.contains("dart")
				|| item.getDefinitions().name.contains("knife") || item.getDefinitions().name.contains("javelin")
				|| item.getDefinitions().name.contains("holy water") || item.getDefinitions().name.contains("bow")) {
			return "a ranged weapon";
		}
		return "an item";
	}
	
	public void applyEquipData() {
		String name = this.name.toLowerCase();
		
		if (name.contains(" chestplate") || name.contains("body") 
				|| name.contains(" brassard") || name.contains(" top") 
					|| name.contains(" jacket") || name.contains(" shirt")
						|| name.contains(" apron") || name.contains(" coat")
							|| name.contains(" blouse") || name.contains(" hauberk") 
								|| name.contains(" chestguard") || name.contains(" torso") 
									|| name.contains(" garb") || name.contains(" tunic")
										|| name.contains(" armour")) {
			equipSlot = 4;
			equipType = 6;
		}
			
		if (name.contains("legs") || name.contains("skirt") 
				|| name.contains("robe bottom") || name.contains(" chaps")
					|| name.contains(" leggings") || name.contains(" tassets") 
						|| name.contains("slacks") || name.contains(" bottoms")
							|| name.contains(" trousers") || name.contains(" greaves")
								|| name.contains("Shorts")) {			
			equipSlot = 7;
		}
		
		if (name.contains("mask") || name.contains("helm") 
				|| name.contains("hat") || name.contains(" hood") 
					|| name.contains("coif") || name.contains("mitre")
						|| name.contains("eyepatch") || name.contains("mask") 
							|| name.contains(" boater") || name.contains(" beret")
								|| name.contains(" snelm") || name.contains(" tiara")
									|| name.contains(" ears") || name.contains(" head") 
										|| name.contains(" cavalier") || name.contains(" wreath") 
											|| name.contains("fedora") || name.contains("fez") 
												|| name.contains(" headband") || name.contains(" headgear")
													|| name.contains(" faceguard")) {
			if (name.contains("mask"))
				equipType = 8;
			equipSlot = 0;
		}
		
		if (name.contains("ring")) {
			equipSlot = 12;
		}
		
		if (name.contains("amulet") || name.contains("necklace") 
				|| name.contains("pendant") || name.contains("scarf") 
					|| name.contains("stole") || name.contains(" symbol")) {
			equipSlot = 2;
		}
		
		if (name.contains("boots") || name.contains("feet") 
				|| name.contains("shoes") || name.contains("sandals")) {
			equipSlot = 10;
		}
		
		if (name.contains("gloves") || name.contains(" vamb") 
				|| name.contains("bracelet") || name.contains("bracers") 
					|| name.contains("gauntlets") || name.contains(" cuffs") 
						|| name.contains("hands") || name.contains("armband")) {
			equipSlot = 9;
		}
		
		if (name.contains("cape") || name.contains("cloak") || name.contains("ava's")) {
			equipSlot = 1;
		}
		
		if (name.contains("shield") || name.contains("defender") 
				|| name.contains("book") || name.contains(" ward") 
					|| name.equalsIgnoreCase("tome of fire") || name.equalsIgnoreCase("toktz-ket-xil")
						|| name.contains("satchel")) {
			equipSlot = 5;
		}
		
		if (name.contains(" arrow") || name.contains(" bolts") 
				|| name.contains(" brutal") || name.contains(" arrows") 
					|| name.contains(" tar") || name.contains(" blessing") 
						|| name.contains(" javelin") || name.contains(" grapple")) {
			equipSlot = 13;
		}
		
		if (name.contains("sword") || name.contains("whip") 
				|| name.contains("halbard") || name.contains("claws") 
					|| name.contains("spear") || name.contains("anchor")
				|| name.contains("lance")
						|| name.contains("dagger") || name.contains("bow")
							|| name.contains("bulwark") || name.contains(" axe") 
								|| name.contains(" maul") || name.contains(" ballista")
									|| name.contains(" club") || name.contains("katana")
										|| name.contains("scythe") || name.contains("staff")
				|| name.contains("sceptre")
				|| name.contains("wand") || name.contains("lizard")
												|| name.contains("salamander") || name.contains("flail") 
													|| name.contains("mjolnir") || name.contains("mace") 
														|| name.contains("hammer") || name.contains("arclight")
															|| name.contains("crozier") || name.contains("banner ")
																|| name.contains("dart") || name.contains("cane") 
																	|| name.contains("chinchompa") || name.contains("knife")
																		|| name.contains("scimitar") || name.contains("hasta")
																			|| name.contains("rapier") || name.contains("sickle") 
																				|| name.contains("greegree") || name.contains("machete")
																					|| name.contains("blackjack") || name.contains("blowpipe")
																						|| name.contains("trident") || name.contains("pickaxe")
																							|| name.contains("harpoon") || name.contains("bludgeon")) {
			equipSlot = 3;		
			if (name.contains("2h") || name.contains("halbard") 
					|| name.contains("spear") || name.contains("bulwark") 
						|| name.contains("maul") || name.contains(" ballista") 
							|| name.contains("salamander") || name.contains("lizard") 
								|| (name.contains("bow") && (!name.contains("cross") && !name.contains("karil's"))) 
									|| name.contains("club") || name.contains("claws") 
										|| name.contains("blowpipe") || name.contains("anchor") 
											|| name.contains("greataxe") || name.contains("godsword") || name.contains("bludgeon")) {
				equipType = 5;
			}
		}
	}

	public void setValue(int value) {
		this.value = value;
	}

	public static ItemDefinitions forId(int i) {
		return ItemDefinitions.getItemDefinitions(i);
	}

	public static Item forName1(String new_name) {
		for (int i = 0; i < Misc.getItemDefinitionsSize(); i++) {
			if (ItemDefinitions.forId(i).name.equals(new_name)) {
				Item item = new Item(i);
				return item;
			}
		}
		return null;
	}

	public boolean hasShopPriceAttributes() {
		if (parameters == null)
			return false;
		if (parameters.get(258) != null && ((Integer) parameters.get(258)).intValue() == 1)
			return true;
		if (parameters.get(259) != null && ((Integer) parameters.get(259)).intValue() == 1)
			return true;
		return false;
	}

	public int getStageOnDeath() {
		if (parameters == null)
			return 0;
		Object protectedOnDeath = parameters.get(1397);
		if (protectedOnDeath != null && protectedOnDeath instanceof Integer)
			return (Integer) protectedOnDeath;
		return 0;
	}

	public double getDungShopValueMultiplier() {
		if (parameters == null)
			return 1;
		Object value = parameters.get(1046);
		if (value != null && value instanceof Integer)
			return ((Integer) value).doubleValue() / 100;
		return 1;
	}

	public boolean isBindItem() {
		if (inventoryOptions == null)
			return false;
		for (String option : inventoryOptions) {
			if (option == null)
				continue;
			if (option.equalsIgnoreCase("bind"))
				return true;
		}
		return false;
	}

	public boolean isRingOfKinship() {
		if (inventoryOptions == null)
			return false;
		for (String option : inventoryOptions) {
			if (option == null)
				continue;
			if (option.equals("Open party interface"))
				return true;
		}
		return false;
	}

}