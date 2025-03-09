package com.rs.game.world.entity.player.content;

import java.util.Random;
import java.util.TimerTask;

import com.rs.cores.CoresManager;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.utility.Logger;

public class SinkHoles {

	public enum Sinkholes {
		FREMENNIK(new Position(2781, 3608, 0), "Near a slayer cave in Fremmenik."),
		BATTLE(new Position(2547, 3254, 0), "In the midst of a battle  between homans and gnomes."),
		BRIMHAVEN(new Position(2741, 3170, 0), "Near a dungeon entrance in the jungle."),
		CANIFS(new Position(3518, 3512, 0), "Near a town east of the River Salve."),
		PORT_PHASMATYS(new Position(3650, 3529, 0), "Near an altar besides a ghostly town."),
		OOGLOG(new Position(2601, 2887, 0), "A good vacation spot."),
		BARBARIAN_OUTPOST(new Position(2543, 3560, 0), "Where barbarians train for agility."),
		FIGHT_ARENA(new Position(2623, 3134, 0), "Somewhere at the Fight arena."),
		RANGING_GUILD(new Position(2674, 3447, 0), "Beside a guild for archers."),
		MUDSKIPPER_POINT(new Position(3010, 3150, 0), "Besides a dungeon entrance, south of a port town."),
		BARROWS(new Position(3560, 3312, 0), "Place where several famous brothers lie."),
		MONASTERY(new Position(3033, 3486, 0), "Between a mountain and a place of peace."),
		TAVERLY(new Position(2927, 3401, 0), "Near a cave entrance in a druidic town."),
		LUMBRIDGE_SWAMP(new Position(3220, 3195, 0), "In the swamps south of a town."),
		CATHERBY(new Position(2819, 3477, 0), "North of a fishing town."),
		FISHING_COLONY(new Position(2315, 3643, 0), "South of a fishing colony, where hunters prey."),
		SEERS_VILLAGE(new Position(2751, 3425, 0), "Near a flax field where the future is seen."),
		FALADOR(new Position(2987, 3405, 0), "North of the White Knight city."),
		AL_KHARID(new Position(3304, 3260, 0), "North of a desert town."),
		RELLEKKA(new Position(2607, 3624, 0), "Close to a lighthouse.");

		private Position tile;
		private String hint;

		private Sinkholes(Position tile, String hint) {
			this.tile = tile;
			this.hint = hint;
		}

		public Position getTile() {
			return tile;
		}

		public String getHint() {
			return hint;
		}



	}

	public static boolean isObject(final WorldObject object) {
		switch (object.getId()) {
		case 59921:
			return true;
		default:
			return false;
		}
	}


	public static void enterSinkHole(Player player) {
		player.setNextPosition(new Position(1561, 4381, 0));
		player.animate(new Animation(7376));
		player.sm("You climb into the sinkhole.");
		player.sinkholes++;
		return;
	}

	public static String getLocation() {
		int pick = new Random().nextInt(Sinkholes.values().length);
		final Sinkholes sinkhole = Sinkholes.values()[pick];
		if (sinkhole.getTile().getX() == 2781) {
			return "Fremennik";
		}
		if (sinkhole.getTile().getX() == 2547) {
			return "Battlefield";
		}
		if (sinkhole.getTile().getX() == 2741) {
			return "Brimhaven";
		}
		if (sinkhole.getTile().getX() == 3518) {
			return "Canfis";
		}
		if (sinkhole.getTile().getX() == 3650) {
			return "Phasmatys";
		}
		if (sinkhole.getTile().getX() == 2601) {
			return "Ooglog";
		}
		if (sinkhole.getTile().getX() == 2543) {
			return "Barb Outpost";
		}
		if (sinkhole.getTile().getX() == 2623) {
			return "Fight Arena";
		}
		if (sinkhole.getTile().getX() == 2674) {
			return "Range Guild";
		}
		if (sinkhole.getTile().getX() == 3010) {
			return "Mudskipper";
		}
		if (sinkhole.getTile().getX() == 3560) {
			return "Barrows";
		}
		if (sinkhole.getTile().getX() == 3033) {
			return "Monastery";
		}
		if (sinkhole.getTile().getX() == 2927) {
			return "Taverly";
		}
		if (sinkhole.getTile().getX() == 3220) {
			return "Lumb Swamp";
		}
		if (sinkhole.getTile().getX() == 2819) {
			return "Catherby";
		}
		if (sinkhole.getTile().getX() == 2315) {
			return "Fish Colony";
		}
		if (sinkhole.getTile().getX() == 2751) {
			return "Seers Village";
		}
		if (sinkhole.getTile().getX() == 2987) {
			return "Falador";
		}
		if (sinkhole.getTile().getX() == 3304) {
			return "Al-Kharid";
		}
		if (sinkhole.getTile().getX() == 2607) {
			return "Rellekka";
		}
		return "Nowhere";
	}
	public static void HandleObject(Player player, final WorldObject object) {
		final int id = object.getId();
		if (id == 59921) {
			enterSinkHole(player);
		}
	}

	public static void startEvent() {
		int pick = new Random().nextInt(Sinkholes.values().length);
		final Sinkholes sinkhole = Sinkholes.values()[pick];
		CoresManager.fastExecutor.schedule(new TimerTask() {
			int loop;
			@Override
			public void run() {
				try {
					if (loop < 2) {
						for (Player players : World.getPlayers()) {
							if (players == null || !World.isSinkArea(players.getTile())) {
								continue;
							}
							players.setNextPosition(sinkhole.getTile());
							players.sm("The sink hole has collapsed and you have escaped to avoid any damage.");
						}
					} else {
						cancel();
					}
					loop++;
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 3600000);//3600000

		final WorldObject hole = new WorldObject(59921, 10, 0, sinkhole.getTile().getX(), sinkhole.getTile().getY(), 0);
		World.sendWorldMessage("[<col=8B0000>Sink Holes</col>] - A sink hole has appeared, raid the dungeon for large amounts of resources!", false);
		World.sendWorldMessage("[<col=8B0000>Sink Holes</col>] - Location: "+sinkhole.getHint()+"", false);
		World.spawnTemporaryObject(hole, 3500000, true);
		return;
	}


}