package com.rs.game.world.entity.player.pets;

import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.utility.Utils;

public enum SkillingPets {

	ROCKY(29592, 27353, "thieving", 10_000),
	BLINK(29059, 16049, "dungeoneering", 10_000),
	NATURE_GUARDIAN(29593, 27337, "runecrafting", 10_000),
	SKELETAL_PUPPY(29060, 16050, "prayer", 10_000),
	HOUSE(29061, 16065, "construction", 10_000),
	Giant_squirrel(29595, 27334, "agility", 10_000),
	HERBY(29063, 16053, "herblore", 10_000),
	RUBY_GUARDIAN(29064, 16054, "crafting", 10_000),
	CRABBE(29065, 16055, "slayer", 10_000),
	RED_CHINCHOMPA(29066, 16056, "hunter", 10_000),
	ROCK(29067, 16057, "mining", 10_000),
	RUNITE(29068, 16058, "smithing", 10_000),
	HERON(29594, 26715, "fishing", 10_000),
	GOBLIN_COOK(29070, 16060, "cooking", 10_000),
	PHEONIX(29071, 16061, "firemaking", 10_000),
	BEAVER(29596, 26717, "woodcutting", 10_000),
	TANGLEROOT(29591, 27352, "farming", 10_000),
	WOLFY(29074, 16064, "summoning", 10_000),
	ROBINS_HAT(29075, 16073, "ranged", 20_000),
	SITAPH(29076, 16074, "attack", 20_000),
	TOME_OF_MAGIC(29077, 16075, "magic", 20_000),
	TANKY_BROAV(29078, 16076, "defence", 20_000),
	DREADNAUT(29079, 16077, "strength", 20_000),
	CUTE_CREATURE(29080, 16078, "constitution", 20_000);
	
	private int itemId, npcId;
	private String skill;
	private double rate;
	
	private SkillingPets(int itemId, int npcId, String skill, double rate) {
		this.itemId = itemId;
		this.npcId = npcId;
		this.skill = skill;
		this.rate = rate;
	}

	public int getItemId() {
		return itemId;
	}

	public int getNpcId() {
		return npcId;
	}

	public int getSkill() {
		return Skills.getSkillId(skill);
	}

	public double getRate() {
		return rate;
	}
	
	public String getName() {
		return Utils.getFormattedEnumName(name());
	}
	
	public static SkillingPets forSkill(int skill) {
		for (SkillingPets pet : SkillingPets.values()) {
			if (pet.getSkill() == skill)
				return pet;
		}
		return null;
	}
	
}
