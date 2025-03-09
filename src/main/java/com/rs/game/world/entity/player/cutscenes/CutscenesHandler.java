package com.rs.game.world.entity.player.cutscenes;

import java.util.HashMap;

import com.rs.game.world.entity.player.cutscenes.impl.ChargeScene;
import com.rs.game.world.entity.player.cutscenes.impl.DTPreview;
import com.rs.game.world.entity.player.cutscenes.impl.EdgeWilderness;
import com.rs.game.world.entity.player.cutscenes.impl.HomeCutScene;
import com.rs.game.world.entity.player.cutscenes.impl.HungerGames;
import com.rs.game.world.entity.player.cutscenes.impl.MasterOfFear;
import com.rs.game.world.entity.player.cutscenes.impl.NexCutScene;
import com.rs.game.world.entity.player.cutscenes.impl.TowersPkCutscene;
import com.rs.utility.Logger;

public class CutscenesHandler {

	private static final HashMap<Object, Class<Cutscene>> handledCutscenes = new HashMap<Object, Class<Cutscene>>();

	@SuppressWarnings("unchecked")
	public static final void init() {
		try {
			Class<Cutscene> value1 = (Class<Cutscene>) Class
					.forName(EdgeWilderness.class.getCanonicalName());
			handledCutscenes.put("EdgeWilderness", value1);
			Class<Cutscene> value2 = (Class<Cutscene>) Class
					.forName(DTPreview.class.getCanonicalName());
			handledCutscenes.put("DTPreview", value2);
			Class<Cutscene> value3 = (Class<Cutscene>) Class
					.forName(NexCutScene.class.getCanonicalName());
			
			handledCutscenes.put("NexCutScene", value3);
			Class<Cutscene> value4 = (Class<Cutscene>) Class
					.forName(TowersPkCutscene.class.getCanonicalName());
			handledCutscenes.put("TowersPkCutscene", value4);
			Class<Cutscene> value5 = (Class<Cutscene>) Class
					.forName(HomeCutScene.class.getCanonicalName());
			handledCutscenes.put("HomeCutScene", value5);
			Class<Cutscene> value6 = (Class<Cutscene>) Class
					.forName(MasterOfFear.class.getCanonicalName());
			handledCutscenes.put("MasterOfFear", value6);
			handledCutscenes.put("ChargeScene", value6);
			handledCutscenes.put("NewTutorial", value6);
			handledCutscenes.put("MerryChristmas", value6);
			handledCutscenes.put("CustomScene", value6);
			handledCutscenes.put("HungerGames", (Class<Cutscene>) Class.forName(HungerGames.class.getCanonicalName()));
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	public static final void reload() {
		handledCutscenes.clear();
		init();
	}

	public static final Cutscene getCutscene(Object key) {
		Class<Cutscene> classC = handledCutscenes.get(key);
		if (classC == null)
			return null;
		try {
			return classC.newInstance();
		} catch (Throwable e) {
			Logger.handle(e);
		}
		return null;
	}
}
