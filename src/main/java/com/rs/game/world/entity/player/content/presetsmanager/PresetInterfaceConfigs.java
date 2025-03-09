package com.rs.game.world.entity.player.content.presetsmanager;

public class PresetInterfaceConfigs {

	public static enum CONFIGS {
		PRESET_1(19),
		PRESET_2(22),
		PRESET_3(25),
		PRESET_4(28),
		PRESET_5(31),
		PRESET_6(34),
		PRESET_7(37),
		CURRENT_SETUP(40),
		PRAYER_BOOK(45),
		SPELL_BOOK(49),
		RESPAWN_LOCATION(53),
		ATTACK(62, new int[]{65, 66}),
		STRENGTH(70, new int[]{73, 74}),
		DEFENCE(78, new int[]{81, 82}),
		RANGE(86, new int[]{89, 90}),
		MAGIC(94, new int[]{97, 98}),
		PRAYER(102, new int[]{105, 106}),
		HITPOINTS(110, new int[]{113, 114}),
		EQUIP_ARROW_LEFT(226),
		EQUIP_ARROW_RIGHT(225),
		SAVE(163),
		DELETE(166),
		GEAR_UP(169);
		
		CONFIGS(int buttonId, int[] textComponents) {
			this.buttonId = buttonId;
			this.textComponents = textComponents;
		}
		
		CONFIGS(int buttonId) {
			this.buttonId = buttonId;
			this.textComponents = null;
		}
		
		int buttonId;
		int[] textComponents;
		
	}
	
	public static int getSaveIndexForButton(int button) {
		if(button == CONFIGS.PRESET_1.buttonId)
			return 0;
		if(button == CONFIGS.PRESET_2.buttonId)
			return 1;
		if(button == CONFIGS.PRESET_3.buttonId)
			return 2;
		if(button == CONFIGS.PRESET_4.buttonId)
			return 3;
		if(button == CONFIGS.PRESET_5.buttonId)
			return 4;
		if(button == CONFIGS.PRESET_6.buttonId)
			return 5;
		if(button == CONFIGS.PRESET_7.buttonId)
			return 6;
		return -1;
	}
	
}
