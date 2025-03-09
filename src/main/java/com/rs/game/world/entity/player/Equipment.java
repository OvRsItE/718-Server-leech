package com.rs.game.world.entity.player;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.game.world.entity.player.actions.skilling.Bonfire;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.player.controller.impl.Wilderness;
import com.rs.utility.ItemExamines;

import java.io.Serializable;

public final class Equipment implements Serializable {

	private static final long serialVersionUID = -4147163237095647617L;

	public static final byte SLOT_HAT = 0, SLOT_CAPE = 1, SLOT_AMULET = 2,
			SLOT_WEAPON = 3, SLOT_CHEST = 4, SLOT_SHIELD = 5, SLOT_LEGS = 7,
			SLOT_HANDS = 9, SLOT_FEET = 10, SLOT_RING = 12, SLOT_ARROWS = 13,
			SLOT_AURA = 14;


	private ItemsContainer<Item> items;

	private transient Player player;
	private transient int equipmentHpIncrease;

	public ItemsContainer<Item> getItemsContainer() {
		return items;
	}

	public boolean containsOneItem(int... itemIds) {
		for (int itemId : itemIds) {
			if (items.containsOne(new Item(itemId, 1))) {
				return true;
			}
		}
		return false;
	}

	public static final int[] DISABLED_SLOTS = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0 };

	public Equipment() {
		items = new ItemsContainer<Item>(15, false);
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void init() {
		player.getPackets().sendItems(94, items);
		refresh(null);
	}

	public void refresh(int... slots) {
		if (slots != null) {
			player.getPackets().sendUpdateItems(94, items, slots);
			player.getCombatDefinitions().checkAttackStyle();
		}
		player.getCombatDefinitions().refreshBonuses();
		refreshConfigs(slots == null);
	}

	public void reset() {
		items.reset();
		init();
	}

	public Item getItem(int slot) {
		return items.get(slot);
	}

	public void sendExamine(int slotId) {
		Item item = items.get(slotId);
		if (item == null) {
			return;
		}
		player.getPackets().sendGameMessage(ItemExamines.getExamine(item));
	}

	public void refreshConfigs(boolean init) {
		double hpIncrease = 0;
		for (int index = 0; index < items.getSize(); index++) {
			Item item = items.get(index);
			if (item == null) {
				continue;
			}
			int id = item.getId();
			if (index == Equipment.SLOT_HAT) {
				if (id == 20135 || id == 20137 // torva
						|| id == 20147 || id == 20149 // pernix
						|| id == 20159 || id == 20161 // virtus
						) {
					hpIncrease += 6;
				}

			} else if (index == Equipment.SLOT_CHEST) {
				if (id == 20139 || id == 20141 // torva
						|| id == 20151 || id == 20153 // pernix
						|| id == 20163 || id == 20165 // virtus
						) {
					hpIncrease += 20;
				}

			}
			else if (index == Equipment.SLOT_LEGS) {
				if (id == 20143 || id == 20145 // torva
						|| id == 20155 || id == 20157 // pernix
						|| id == 20167 || id == 20169 // virtus
						) {
					hpIncrease += 13;
				}
			}
		}

		if(player.getLastBonfire() > 0) {
			int maxhp = player.getSkills().getLevel(Skills.HITPOINTS) * 10;
			hpIncrease += maxhp * Bonfire.getBonfireBoostMultiplier(player) - maxhp;
		}
		if (player.getBuffManager().hasBuff(BuffManager.BuffType.HEALTH_BOOST_POTION)) {
			hpIncrease += 20;
		}
		if(player.getHpBoostMultiplier() != 0) {
			int maxhp = player.getSkills().getLevel(Skills.HITPOINTS) * 10;
			hpIncrease += maxhp*player.getHpBoostMultiplier();
		}
		if (hpIncrease != equipmentHpIncrease) {
			equipmentHpIncrease = (int) hpIncrease;
			if (!init) {
				player.refreshHitPoints();
			}
		}
	}

	public static boolean hideArms(Item item) {
		String name = item.getName().toLowerCase();
		if
		//temp old graphics fix, but bugs alil new ones
		(name.contains("d'hide body")
				|| name.contains("dragonhide body")
				|| name.contains("dark ether body")
				|| name.equals("stripy pirate shirt")
				|| name.contains("chainbody")
				&& (name.contains("iron") || name.contains("bronze")
						|| name.contains("steel")
						|| name.contains("black")
						|| name.contains("mithril")
						|| name.contains("adamant")
						|| name.contains("rune")
						|| name.contains("white"))
				|| name.equals("leather body")
				|| name.equals("hardleather body")
				|| name.contains("studded body")) {
			return false;
		}
		return item.getDefinitions().getEquipType() == 6;
	}

	public static boolean hideHair(Item item) {
		return item.getDefinitions().getEquipType() == 8;
	}

	public static boolean showBear(Item item) {
		String name = item.getName().toLowerCase();
		return !hideHair(item)
				|| name.contains("horns")
				|| name.contains("hat")
				|| name.contains("afro")
				|| name.contains("cowl")
				|| name.contains("coif")
				|| name.contains("tattoo")
				|| name.contains("headdress")
				|| name.contains("hood") && !name.contains("nazgul hood") && !name.contains("eternal hood")
				|| name.contains("mask") && !name.contains("h'ween") && !name.contains("sirenic") && !name.contains("death slayer helmet")
				|| name.contains("helm") && !name.contains("full");
	}
	
	public boolean wearingSkillCape(int skill) {	
		String skillName = Skills.SKILL_NAME[skill];
		String capeName = ItemDefinitions.forId(player.getEquipment().getCapeId()).getName();
		if (capeName.contains(skillName) 
			|| capeName.equalsIgnoreCase("max cape") 
			|| capeName.equalsIgnoreCase("completionist cape") 
			|| capeName.equalsIgnoreCase("fire max cape"))
			return true;
		return false;
	}

	public static int getItemSlot(int itemId) {
		return ItemDefinitions.getItemDefinitions(itemId).getEquipSlot();
	}

	public static boolean isTwoHandedWeapon(Item item) {
		String name = item.getName().toLowerCase();
		if (name.contains("staff")) {
			return false;
		} else {
			return item.getDefinitions().getEquipType() == 5;
		}
	}

	public int getWeaponRenderEmote() {
		Item weapon = items.get(3);
		if (weapon == null) {
//			if (player.getDisplayName().equalsIgnoreCase("corey"))
//				return 2578;
			return 1426;
		}
		if (weapon.getDefinitions().getName().equalsIgnoreCase("dragon hunter lance")) {
			return 1381;
		}

		return weapon.getDefinitions().getRenderAnimId();
	}

	public boolean hasShield() {
		return items.get(5) != null;
	}

	public int getWeaponId() {
		Item item = items.get(SLOT_WEAPON);
		if (item == null) {
			return -1;
		}
		return item.getId();
	}

	public int getChestId() {
		Item item = items.get(SLOT_CHEST);
		if (item == null) {
			return -1;
		}
		return item.getId();
	}

	public int getHatId() {
		Item item = items.get(SLOT_HAT);
		if (item == null) {
			return -1;
		}
		return item.getId();
	}

	public int getShieldId() {
		Item item = items.get(SLOT_SHIELD);
		if (item == null) {
			return -1;
		}
		return item.getId();
	}

	public int getLegsId() {
		Item item = items.get(SLOT_LEGS);
		if (item == null) {
			return -1;
		}
		return item.getId();
	}

	public void removeAmmo(int ammoId, int ammount) {
		if (ammount == -1) {
			items.remove(SLOT_WEAPON, new Item(ammoId, 1));
			refresh(SLOT_WEAPON);
		} else {
			items.remove(SLOT_ARROWS, new Item(ammoId, ammount));
			refresh(SLOT_ARROWS);
		}
	}

	public int getAuraId() {
		Item item = items.get(SLOT_AURA);
		if (item == null) {
			return -1;
		}
		return item.getId();
	}

	public int getCapeId() {
		Item item = items.get(SLOT_CAPE);
		if (item == null) {
			return -1;
		}
		return item.getId();
	}

	public int getRingId() {
		Item item = items.get(SLOT_RING);
		if (item == null) {
			return -1;
		}
		return item.getId();
	}

	public int getAmmoId() {
		Item item = items.get(SLOT_ARROWS);
		if (item == null) {
			return -1;
		}
		return item.getId();
	}

	public void deleteItem(int itemId, int amount) {
		Item[] itemsBefore = items.getItemsCopy();
		items.remove(new Item(itemId, amount));
		refreshItems(itemsBefore);
	}

	public void refreshItems(Item[] itemsBefore) {
		int[] changedSlots = new int[itemsBefore.length];
		int count = 0;
		for (int index = 0; index < itemsBefore.length; index++) {
			if (itemsBefore[index] != items.getItems()[index]) {
				changedSlots[count++] = index;
			}
		}
		int[] finalChangedSlots = new int[count];
		System.arraycopy(changedSlots, 0, finalChangedSlots, 0, count);
		refresh(finalChangedSlots);
	}

	public int getBootsId() {
		Item item = items.get(SLOT_FEET);
		if (item == null) {
			return -1;
		}
		return item.getId();
	}

	public int getGlovesId() {
		Item item = items.get(SLOT_HANDS);
		if (item == null) {
			return -1;
		}
		return item.getId();
	}

	public boolean wearingGloves() {
		return items.get(SLOT_HANDS) != null;
	}

	public ItemsContainer<Item> getItems() {
		return items;
	}

	public int getEquipmentHpIncrease() {
		return equipmentHpIncrease;
	}

	public void setEquipmentHpIncrease(int hp) {
		equipmentHpIncrease = hp;
	}

	public boolean wearingArmour() {
		return getItem(SLOT_HAT) != null || getItem(SLOT_CAPE) != null
				|| getItem(SLOT_AMULET) != null || getItem(SLOT_WEAPON) != null
				|| getItem(SLOT_CHEST) != null || getItem(SLOT_SHIELD) != null
				|| getItem(SLOT_LEGS) != null || getItem(SLOT_HANDS) != null
				|| getItem(SLOT_FEET) != null;
	}

	public int getAmuletId() {
		Item item = items.get(SLOT_AMULET);
		if (item == null) {
			return -1;
		}
		return item.getId();
	}

	public boolean hasTwoHandedWeapon() {
		Item weapon = items.get(SLOT_WEAPON);
		return weapon != null && isTwoHandedWeapon(weapon);
	}

	public boolean wearingFullCeremonial() {
		if (items.get(SLOT_HAT) != null && items.get(SLOT_CHEST) != null && items.get(SLOT_LEGS) != null && items.get(SLOT_HANDS) != null && items.get(SLOT_FEET) != null) {
			if (items.get(SLOT_HAT).getId() == 20125 && items.get(SLOT_CHEST).getId() == 20127 && items.get(SLOT_LEGS).getId() == 20129 && items.get(SLOT_HANDS).getId() == 20131 && items.get(SLOT_FEET).getId() == 20133) {
				return true;
			}
		}
		return false;
	}

	public boolean Full_Hydra() {
		if (items.get(SLOT_HAT) != null && items.get(SLOT_CHEST) != null && items.get(SLOT_LEGS) != null) {
			if (items.get(SLOT_HAT).getId() == 28681 && items.get(SLOT_CHEST).getId() == 28679 && items.get(SLOT_LEGS).getId() == 28683) {
				return true;
			}
		}
		return false;
	}
	
	public double getLuckBoost() {	
		double boost = 0D;
		switch (getRingId()) {
			
			case 2572: //Ring of wealth
			case 20659://Ring of wealth (4)
			case 20657://Ring of wealth (3)
			case 20655://Ring of wealth (2)
			case 20653://Ring of wealth (1)
				boost = 0.01;
				break;
		
			case 29833: //Luck of the dwarves
				boost = 0.05;
				break;
				
			case 29463: //Ring of wealth (i)
				boost = 0.04;
				break;
				
			case 29107: //Hazelmere's signet ring
				boost = 0.1;
				break;	
		}
		if (Wilderness.isAtWild(player) && getRingId() == 29463) {
			boost *= 2;
		}
		return 1 + boost;
	}

	public static boolean isCape(Item item) {
		String wepEquiped = item.getDefinitions().getName().toLowerCase();
		if (wepEquiped == null) {
			return false;
		} else if (wepEquiped.contains("cape")) {
			return true;
		} else if (wepEquiped.contains("Master") && wepEquiped.contains("Cape")) {
			return true;
		} else if (wepEquiped.contains("tokhaar-kal")) {
			return true;
		} else if (wepEquiped.contains("cloak")) {
			return true;
		} else if (wepEquiped.contains("ava")) {
			return true;
		}
		return false;
	}
	
	public boolean hasAmuletOfSouls() {
		return (getAmuletId() == 29100 || getAmuletId() == 29118);
	}

	public boolean wearingBlackMask() {
		if (items.get(SLOT_HAT) != null) {
			if (ItemDefinitions.getItemDefinitions(items.get(SLOT_HAT).getId()).getName().toLowerCase().contains("black mask")) {
				return true;
			}
		}
		return false;
	}

	public boolean wearingHexcrest() {
		if (items.get(SLOT_HAT) != null) {
			if (items.get(SLOT_HAT).getId() == 15488) {
				return true;
			}
		}
		return false;
	}
	public static boolean isWearingObbyneck;
	public boolean iswearingObbyNeck() {
		if (items.get(SLOT_AMULET) != null) {
			if (items.get(SLOT_WEAPON).getId() == 6528 && items.get(SLOT_AMULET).getId() == 11128) {
				isWearingObbyneck = true;
			}
			return true;
		}
		return false;
	}

	public static boolean checkDartsType;

	public boolean wearingFocusSight() {
		if (items.get(SLOT_HAT) != null) {
			if (items.get(SLOT_HAT).getId() == 15490) {
				return true;
			}
		}
		return false;
	}

	public boolean wearingSlayerHelmet() {
		int id = getHatId();
		switch (id) {
			case 13263:
			case 15492:
			case 15496:
			case 15497:
			case 22528:
			case 22534:
			case 22540:
			case 22546:
				return true;
			default:
				return false;
		}
	}
	
	public boolean wearingFaceMask() {
		return wearingSlayerHelmet() || getHatId() == 4164;
	}

	public float getWitchDoctorBoost() {
		float boost = 1.0f;
		if (items.get(SLOT_HAT) != null) {
			if (items.get(SLOT_HAT).getId() == 20046) {
				boost += 0.15;
			}
		}
		if (items.get(SLOT_LEGS) != null) {
			if (items.get(SLOT_LEGS).getId() == 20045) {
				boost += 0.15;
			}
		}
		if (items.get(SLOT_CHEST) != null) {
			if (items.get(SLOT_CHEST).getId() == 20044) {
				boost += 0.15;
			}
		}
		return boost;
	}

	public boolean wearingArmourNoWeapon() {
		return getItem(SLOT_HAT) != null || getItem(SLOT_CAPE) != null
				|| getItem(SLOT_AMULET) != null
				|| getItem(SLOT_CHEST) != null || getItem(SLOT_SHIELD) != null
				|| getItem(SLOT_LEGS) != null || getItem(SLOT_HANDS) != null
				|| getItem(SLOT_FEET) != null;
	}

	public boolean hasWeapon() {
		return items.get(SLOT_WEAPON) != null;
	}

	/**
	 * TODO
	 * @return
	 */
	public boolean wearingShayzianArmor() {
		return false;
	}

	public void set(int i, Item item) {
		items.set(i, item);
		refresh();
	}

}
