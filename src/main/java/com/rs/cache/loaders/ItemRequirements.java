/**
 * 
 */
package com.rs.cache.loaders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.rs.game.item.Item;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.utility.Utils;

import lombok.Getter;

/**
 * Handles item related skill requirements.
 * @author ReverendDread
 * Jul 30, 2018
 */
public class ItemRequirements {

	/** Map of item requirements */
	@Getter private static Map<Integer, Requirement[]> requirements;
	
	/**
	 * Initializes item requirements.
	 */
	public static final void init() {
		requirements = new HashMap<Integer, Requirement[]>();
		loadCachedRequirements();
		loadManualRequirements();
		System.out.println("[ItemRequirements] Loaded " + requirements.size() + " item requirements.");
	}

	/**
	 * Loads cached item requirements.
	 */
	private static void loadCachedRequirements() {
		ItemDefinitions definition;
		int size = Utils.getItemDefinitionsSize();
		for (int index = 0; index < size; index++) {
			definition = new ItemDefinitions(index);
			if (definition == null || definition.parameters == null)
				continue;
			ArrayList<Requirement> reqs = new ArrayList<Requirement>();
			for (int key = 0; key < 10; key++) {
				Integer skill = (Integer) definition.parameters.get(749 + (key * 2));
				if (skill != null) {
					Integer level = (Integer) definition.parameters.get(750 + (key * 2));
					if (level != null) {
						reqs.add(new Requirement(skill, level));
					}
				}
			}
			Integer maxed_skill = (Integer) definition.parameters.get(277);
			if (maxed_skill != null) {
				reqs.add(new Requirement(maxed_skill, definition.getId() == 19709 ? 120 : 99));
			}
			requirements.put(definition.getId(), reqs.toArray(new Requirement[reqs.size()]));
		}
	}

	/**
	 * Loads item requirements into map.
	 */
	private static void loadManualRequirements() {
		requirements.put(7462, new Requirement[] { new Requirement(Skills.DEFENCE, 40) });
		requirements.put(19784, new Requirement[] { new Requirement(Skills.ATTACK, 78), new Requirement(Skills.STRENGTH, 78) });
		requirements.put(22401, new Requirement[] { new Requirement(Skills.ATTACK, 78), new Requirement(Skills.STRENGTH, 78) });
		requirements.put(19780, new Requirement[] { new Requirement(Skills.ATTACK, 78), new Requirement(Skills.STRENGTH, 78) });
		requirements.put(13661, new Requirement[] { new Requirement(Skills.FIREMAKING, 92) });
		requirements.put(20822, new Requirement[] { new Requirement(Skills.DEFENCE, 99) });
		requirements.put(20823, new Requirement[] { new Requirement(Skills.DEFENCE, 99) });
		requirements.put(20824, new Requirement[] { new Requirement(Skills.DEFENCE, 99) });
		requirements.put(20825, new Requirement[] { new Requirement(Skills.DEFENCE, 99) });
		requirements.put(20826, new Requirement[] { new Requirement(Skills.DEFENCE, 99) });
		requirements.put(20072, new Requirement[] { new Requirement(Skills.ATTACK, 60), new Requirement(Skills.DEFENCE, 60) });
		requirements.put(29965, new Requirement[] { new Requirement(Skills.ATTACK, 75) });
		requirements.put(29953, new Requirement[] { new Requirement(Skills.MAGIC, 75) });
		requirements.put(29951, new Requirement[] { new Requirement(Skills.MAGIC, 70) });
		requirements.put(29949, new Requirement[] { new Requirement(Skills.MAGIC, 75) });
		requirements.put(29947, new Requirement[] { new Requirement(Skills.MAGIC, 75) });
		requirements.put(29945, new Requirement[] { new Requirement(Skills.ATTACK, 75) });
		requirements.put(29943, new Requirement[] { new Requirement(Skills.RANGE, 75) });
		requirements.put(29939, new Requirement[] { new Requirement(Skills.ATTACK, 60) });		
		requirements.put(29931, new Requirement[] { new Requirement(Skills.DEFENCE, 75) });
		requirements.put(29929, new Requirement[] { new Requirement(Skills.DEFENCE, 75) });
		requirements.put(29927, new Requirement[] { new Requirement(Skills.DEFENCE, 75) });
		requirements.put(29926, new Requirement[] { new Requirement(Skills.DEFENCE, 75) });
		requirements.put(29925, new Requirement[] { new Requirement(Skills.MAGIC, 75) });
		requirements.put(29899, new Requirement[] { new Requirement(Skills.MAGIC, 75) });
		requirements.put(29897, new Requirement[] { new Requirement(Skills.ATTACK, 60), new Requirement(Skills.RANGE, 60) });
		requirements.put(29895, new Requirement[] { new Requirement(Skills.ATTACK, 75), new Requirement(Skills.STRENGTH, 75) });
		requirements.put(19709, new Requirement[] { new Requirement(Skills.DUNGEONEERING, 120) });	
		requirements.put(29893, new Requirement[] { new Requirement(Skills.ATTACK, 75), new Requirement(Skills.MAGIC, 75) });
		requirements.put(29891, new Requirement[] { new Requirement(Skills.ATTACK, 75), new Requirement(Skills.MAGIC, 75) });
		requirements.put(29889, new Requirement[] { new Requirement(Skills.ATTACK, 75), new Requirement(Skills.MAGIC, 75) });
		requirements.put(29885, new Requirement[] { new Requirement(Skills.MAGIC, 90) });
		requirements.put(29883, new Requirement[] { new Requirement(Skills.MAGIC, 90) });	
		requirements.put(29881, new Requirement[] { new Requirement(Skills.ATTACK, 90)});
		requirements.put(29879, new Requirement[] { new Requirement(Skills.RANGE, 90) });
		requirements.put(29877, new Requirement[] { new Requirement(Skills.MAGIC, 90) });
		requirements.put(29874, new Requirement[] { new Requirement(Skills.DEFENCE, 75), new Requirement(Skills.MAGIC, 75) });
		requirements.put(29872, new Requirement[] { new Requirement(Skills.ATTACK, 65) });			
		requirements.put(29870, new Requirement[] { new Requirement(Skills.ATTACK, 60) });
		requirements.put(29866, new Requirement[] { new Requirement(Skills.MAGIC, 75) });	
		requirements.put(29864, new Requirement[] { new Requirement(Skills.DEFENCE, 75), new Requirement(Skills.RANGE, 75) });
		requirements.put(29967, new Requirement[] { new Requirement(Skills.ATTACK, 90) });
		requirements.put(29965, new Requirement[] { new Requirement(Skills.ATTACK, 75) });
		requirements.put(29966, new Requirement[] { new Requirement(Skills.RANGE, 75) });
		requirements.put(29953, new Requirement[] { new Requirement(Skills.MAGIC, 75) });
		requirements.put(29949, new Requirement[] { new Requirement(Skills.MAGIC, 75) });
		requirements.put(29947, new Requirement[] { new Requirement(Skills.MAGIC, 75) });
		requirements.put(29945, new Requirement[] { new Requirement(Skills.ATTACK, 75) });
		requirements.put(29897, new Requirement[] { new Requirement(Skills.RANGE, 65) });
		requirements.put(29895, new Requirement[] { new Requirement(Skills.ATTACK, 75) });
		requirements.put(29891, new Requirement[] { new Requirement(Skills.ATTACK, 75), new Requirement(Skills.MAGIC, 75) });
		requirements.put(29889, new Requirement[] { new Requirement(Skills.ATTACK, 75), new Requirement(Skills.MAGIC, 75) });
		requirements.put(29874, new Requirement[] { new Requirement(Skills.ATTACK, 75), new Requirement(Skills.MAGIC, 70) });
		requirements.put(29831, new Requirement[] { new Requirement(Skills.DEFENCE, 90) });
		requirements.put(29829, new Requirement[] { new Requirement(Skills.DEFENCE, 90) });
		requirements.put(29827, new Requirement[] { new Requirement(Skills.DEFENCE, 90) });
		requirements.put(29825, new Requirement[] { new Requirement(Skills.DEFENCE, 90) });
		requirements.put(29823, new Requirement[] { new Requirement(Skills.DEFENCE, 90) });
		requirements.put(29821, new Requirement[] { new Requirement(Skills.DEFENCE, 90) });
		requirements.put(29819, new Requirement[] { new Requirement(Skills.DEFENCE, 90) });
		requirements.put(29817, new Requirement[] { new Requirement(Skills.DEFENCE, 90) });
		requirements.put(29815, new Requirement[] { new Requirement(Skills.DEFENCE, 90) });
		requirements.put(29790, new Requirement[] { new Requirement(Skills.MAGIC, 75) });
		requirements.put(29786, new Requirement[] { new Requirement(Skills.ATTACK, 75) });
		requirements.put(29788, new Requirement[] { new Requirement(Skills.ATTACK, 75) });
		requirements.put(29784, new Requirement[] { new Requirement(Skills.ATTACK, 75) });
		requirements.put(29783, new Requirement[] { new Requirement(Skills.DEFENCE, 90) });
		requirements.put(29782, new Requirement[] { new Requirement(Skills.DEFENCE, 90) });
		requirements.put(29781, new Requirement[] { new Requirement(Skills.DEFENCE, 90) });
		requirements.put(29780, new Requirement[] { new Requirement(Skills.DEFENCE, 90) });
		requirements.put(29779, new Requirement[] { new Requirement(Skills.DEFENCE, 90) });
		requirements.put(29778, new Requirement[] { new Requirement(Skills.DEFENCE, 90) });
		requirements.put(29777, new Requirement[] { new Requirement(Skills.RANGE, 90) });
		requirements.put(29776, new Requirement[] { new Requirement(Skills.MAGIC, 90) });
		requirements.put(29774, new Requirement[] { new Requirement(Skills.MAGIC, 90) });
		requirements.put(29772, new Requirement[] { new Requirement(Skills.MAGIC, 90) });
		requirements.put(29770, new Requirement[] { new Requirement(Skills.MAGIC, 90) });
		requirements.put(29768, new Requirement[] { new Requirement(Skills.MAGIC, 90) });
		requirements.put(29766, new Requirement[] { new Requirement(Skills.MAGIC, 90) });
		requirements.put(29764, new Requirement[] { new Requirement(Skills.ATTACK, 75) });
		requirements.put(29757, new Requirement[] { new Requirement(Skills.DEFENCE, 75) });
		requirements.put(29758, new Requirement[] { new Requirement(Skills.DEFENCE, 75) });
		requirements.put(29760, new Requirement[] { new Requirement(Skills.DEFENCE, 75) });
		requirements.put(29754, new Requirement[] { new Requirement(Skills.ATTACK, 90) });
		requirements.put(29752, new Requirement[] { new Requirement(Skills.ATTACK, 90) });
		requirements.put(29750, new Requirement[] { new Requirement(Skills.ATTACK, 75) });
		requirements.put(29743, new Requirement[] { new Requirement(Skills.DEFENCE, 75) });
		requirements.put(29739, new Requirement[] { new Requirement(Skills.ATTACK, 60) });
		requirements.put(29741, new Requirement[] { new Requirement(Skills.ATTACK, 60) });
		requirements.put(29738, new Requirement[] { new Requirement(Skills.DEFENCE, 75) });
		requirements.put(29737, new Requirement[] { new Requirement(Skills.RANGE, 60) , new Requirement(Skills.ATTACK, 60) });
		requirements.put(29722, new Requirement[] { new Requirement(Skills.DEFENCE, 75) , new Requirement(Skills.MAGIC, 75) });
		requirements.put(29724, new Requirement[] { new Requirement(Skills.DEFENCE, 75) , new Requirement(Skills.RANGE, 75) });
		requirements.put(29726, new Requirement[] { new Requirement(Skills.DEFENCE, 75) , new Requirement(Skills.ATTACK, 75) });
		requirements.put(29715, new Requirement[] { new Requirement(Skills.ATTACK, 60) });
		requirements.put(29714, new Requirement[] { new Requirement(Skills.ATTACK, 60) });
		requirements.put(29713, new Requirement[] { new Requirement(Skills.RANGE, 60) });
		requirements.put(29711, new Requirement[] { new Requirement(Skills.RANGE, 60) });
		requirements.put(29709, new Requirement[] { new Requirement(Skills.MAGIC, 60) });
		requirements.put(29708, new Requirement[] { new Requirement(Skills.MAGIC, 60) });
		requirements.put(29706, new Requirement[] { new Requirement(Skills.ATTACK, 70) });
		requirements.put(29704, new Requirement[] { new Requirement(Skills.ATTACK, 70) });
		requirements.put(29545, new Requirement[] { new Requirement(Skills.ATTACK, 85) });
		requirements.put(29616, new Requirement[] { new Requirement(Skills.RANGE, 85) });
		requirements.put(29671, new Requirement[] { new Requirement(Skills.RANGE, 92) });
		requirements.put(29673, new Requirement[] { new Requirement(Skills.MAGIC, 92) });
		requirements.put(29521, new Requirement[] { new Requirement(Skills.DEFENCE, 60) });
		requirements.put(29523, new Requirement[] { new Requirement(Skills.DEFENCE, 60) });
		requirements.put(29653, new Requirement[] { new Requirement(Skills.ATTACK, 92) });
		requirements.put(29493, new Requirement[] { new Requirement(Skills.MAGIC, 75) , new Requirement(Skills.DEFENCE, 65) });
		requirements.put(29489, new Requirement[] { new Requirement(Skills.MAGIC, 75) , new Requirement(Skills.DEFENCE, 65) });
		requirements.put(29491, new Requirement[] { new Requirement(Skills.MAGIC, 75) , new Requirement(Skills.DEFENCE, 65) });
		requirements.put(29483, new Requirement[] { new Requirement(Skills.ATTACK, 65) });
		requirements.put(29482, new Requirement[] { new Requirement(Skills.ATTACK, 65) });
		requirements.put(29481, new Requirement[] { new Requirement(Skills.ATTACK, 65) });
		requirements.put(29631, new Requirement[] { new Requirement(Skills.RANGE, 65) });
		requirements.put(29633, new Requirement[] { new Requirement(Skills.RANGE, 75) });
		requirements.put(29673, new Requirement[] { new Requirement(Skills.MAGIC, 92) });
		requirements.put(29639, new Requirement[] { new Requirement(Skills.ATTACK, 60) });
		requirements.put(24455, new Requirement[] { new Requirement(Skills.ATTACK, 85) });
		requirements.put(24456, new Requirement[] { new Requirement(Skills.RANGE, 85) });
		requirements.put(24457, new Requirement[] { new Requirement(Skills.MAGIC, 85) });
	}
	
	/**
	 * Checks if the desired player has the requirements to wear the specified item.
	 * @param player
	 * 				the player.
	 * @param item
	 * 				the item id.
	 */
	public static final boolean hasRequirements(final Player player, final Item item) {
		Requirement[] reqs = requirements.get(item.getId());
		if (reqs == null)
			return true;
		boolean passed = true;
		for (Requirement requirement : reqs) {
			if (player.getSkills().getLevelForXp(requirement.getSkill()) < requirement.getLevel()) {
				String name = Skills.SKILL_NAME[requirement.getSkill()].toLowerCase();
				player.getPackets().sendGameMessage("You need to have a" + (name.startsWith("a") ? "n" : "") + " " 
				+ name + " level of " + requirement.getLevel() + ".");
				passed = false;
			}
		}
		return passed;
	}
	
	/**
	 * Checks if the player has the requirement.
	 * @param player
	 * 				the {@code Player}.
	 * @param requirement
	 * 				the {@code Requirement}.
	 * @return
	 * 			true if they passed the requirement, false otherwise.
	 */
	public static final boolean hasRequirement(final Player player, final Requirement requirement) {
		return player.getSkills().getLevelForXp(requirement.getSkill()) >= requirement.getLevel();
	}
	
}
