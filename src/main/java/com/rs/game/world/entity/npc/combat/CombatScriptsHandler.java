package com.rs.game.world.entity.npc.combat;

import java.util.HashMap;
import java.util.Objects;

import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.utility.Logger;
import com.rs.utility.Misc;
import com.rs.utility.Utils;

public class CombatScriptsHandler {

	private static final HashMap<Object, CombatScript> cachedCombatScripts = new HashMap<Object, CombatScript>();
	private static final CombatScript DEFAULT_SCRIPT = new Default();
	private static final String COMBAT_SCRIPTS = "com.rs.game.world.entity.npc.combat.impl";

	@SuppressWarnings("rawtypes")
	public static void init() {
		try {
			Misc.getClasses(COMBAT_SCRIPTS)
			.stream()
			.filter(Objects::nonNull)
			.filter(c -> !c.isAnonymousClass())
			.filter(c -> !c.isEnum())
			.forEach(c -> {
				try {
					Object clazz = c.newInstance();
					if (clazz instanceof CombatScript) {
						CombatScript script = (CombatScript) clazz;
						for (Object key : script.getKeys()) {
							cachedCombatScripts.put(key, script);
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			});
			Logger.log("CombatScriptsHandler", "Loaded " + cachedCombatScripts.size() + " combat scripts.");
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

//	public static int specialAttack(final NPC npc, final Entity target) {
//		CombatScript script = cachedCombatScripts.get(npc.getId());
//		if (script == null) {
//			script = cachedCombatScripts.get(npc.getDefinitions().name);
//			if (script == null)
//				script = DEFAULT_SCRIPT;
//		}
//		return script.attack(npc, target);
//	}

	public static CombatScript init(final NPC npc) {
		CombatScript scriptType = cachedCombatScripts.get(npc.getId());
		if (scriptType == null) {
			scriptType = cachedCombatScripts.get(npc.getDefinitions().name);
			if (scriptType == null)
				scriptType = DEFAULT_SCRIPT;
		}
		try {
			CombatScript inited = scriptType.getClass().newInstance();
			inited.init(npc);
			return inited;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

}
