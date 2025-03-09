package com.rs.game.world.entity.player.content;

import com.rs.game.item.Item;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.utility.Utils;


/**
 * 
 * @author Andreas, Dennis - AvalonPK
 * 
 */

public class CharmDrops {

	public static final Charms[] ratio = Charms.values();
	public static final double NO_CHARMS_CHANCE = 49;

	public static final int GOLD_CHARM_MAX_DROP = 4, GREEN_CHARM_MAX_DROP = 4, CRIMSON_CHARM_MAX_DROP = 3,
			BLUE_CHARM_MAX_DROP = 2;

	public enum Charms {

		GOLD_CHARM(12158, 45.0),

		GREEN_CHARM(12159, 27.0),

		CRIMSON_CHARM(12160, 11.0),

		BLUE_CHARM(12163, 5.0);

		private int itemId;

		private double chance;

		private Charms(int itemId, double chance) {
			this.itemId = itemId;
			this.chance = chance;
		}

		public int getItemId() {
			return itemId;
		}

		public void setItemId(int itemId) {
			this.itemId = itemId;
		}

		public double getChance() {
			return chance;
		}

		public void setChance(double chance) {
			this.chance = chance;
		}

	}

	public static void sendCharmDrop(Player player, NPC npc) {
		if (npc.getId() == 7134) {
			int size = npc.getSize();
			Charms drop = calculateCharm(npc);
			World.addGroundItem(new Item(drop.getItemId(), getCharmsAmount(npc, drop)),
					new Position(npc.getCoordFaceX(size), npc.getCoordFaceY(size), npc.getZ()), player, true, 60,
					0);
			;
		}
		Charms drop = calculateCharm(npc);
		int size = npc.getSize();
		if (drop == null)
			return;
		World.addGroundItem(new Item(drop.getItemId(), getCharmsAmount(npc, drop)),
				new Position(npc.getCoordFaceX(size), npc.getCoordFaceY(size), npc.getZ()), player, true, 60, 0);
		;
	}

	private static Charms calculateCharm(NPC npc) {
		if (npc.getId() == 7134) {
			npc.setCharmDropPercentage(100);
		}
		if (npc.getCombatLevel() < 10)
			return null;
		if (Utils.getRandomDouble(100) < NO_CHARMS_CHANCE)
			return null;
		while (true) {
			double chance = Utils.getRandomDouble(100);
			Charms charm = Charms.values()[Utils.getRandom(Charms.values().length - 1)];
			double npcExtraChance = npc.getCharmDropPercentage();
			if ((charm.getChance() + npcExtraChance) > chance)
				return charm;
			else
				continue;
		}
	}

	public static int getCharmsAmount(NPC npc, Charms charm) {
		switch (charm) {
		case BLUE_CHARM:
			if (npc.getId() == 7134)
				return Utils.random(17, 27);
			return Utils.random(1, BLUE_CHARM_MAX_DROP);
		case CRIMSON_CHARM:
			if (npc.getId() == 7134)
				return Utils.random(20, 32);
			return Utils.random(1, CRIMSON_CHARM_MAX_DROP);
		case GOLD_CHARM:
			if (npc.getId() == 7134)
				return Utils.random(29, 59);
			return Utils.random(1, GOLD_CHARM_MAX_DROP);
		case GREEN_CHARM:
			if (npc.getId() == 7134)
				return Utils.random(24, 41);
			return Utils.random(1, GREEN_CHARM_MAX_DROP);
		}
		return -1;
	}

	public static void setNpcCharmDropRates(NPC npc) {
		switch (npc.getId()) {
		}
		switch (npc.getName()) {

		}
	}

}
