package com.rs.cores.tasks.impl;

import com.alex.store.Index;
import com.rs.Launcher;
import com.rs.Constants;
import com.rs.cache.Cache;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.cores.tasks.Task;
import com.rs.game.map.Region;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.GameEngine;
import com.rs.utility.Logger;

public class CleanUpTask extends Task {

	private final static int TICK_DELAY = 1000;

	public CleanUpTask() {
		super(TICK_DELAY, true, TickType.MAIN);
	}

	@Override
	protected void execute() {
		try {

			if (Runtime.getRuntime().freeMemory() < Constants.MIN_FREE_MEM_ALLOWED) {
				ItemDefinitions.clearItemsDefinitions();
				NPCDefinitions.clearNPCDefinitions();
				ObjectDefinitions.clearObjectDefinitions();
				/*for (Region region : World.getRegions().values())
					region.removeMapFromMemory();*/
			}

			for (Index index : Cache.STORE.getIndexes())
				index.resetCachedFiles();

			GameEngine.get().fastExecutor().purge();
			System.gc();

		} catch (Exception e) {
			Logger.handle(e);
		}
	}

}
