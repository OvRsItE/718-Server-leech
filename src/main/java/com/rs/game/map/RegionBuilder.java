package com.rs.game.map;

import java.util.ArrayList;
import java.util.List;

import com.rs.cache.Cache;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.familiar.Familiar;
import com.rs.game.world.entity.player.Player;

public final class RegionBuilder {

	// used by construction preview
	public static final int[] FORCE_LOAD_REGIONS = { 7503, 7759 };

	public static final int NORTH = 0, EAST = 1, SOUTH = 2, WEST = 3;
	private static boolean lastSearchPositive;

	public static void init() {

		for (int mapX = 0; mapX < MAX_REGION_X; mapX++) {
			for (int mapY = 0; mapY < MAX_REGION_Y; mapY++) {
				if (Cache.STORE.getIndexes()[5].getArchiveId("m" + mapX + "_" + mapY) != -1)
					EXISTING_MAPS.add(MapUtils.encode(MapUtils.Structure.REGION, mapX, mapY, 0));
			}
		}
		for (int regionId : FORCE_LOAD_REGIONS)
			World.getRegion(regionId, true);
	}

	private static final Object ALGORITHM_LOCK = new Object();
	private static final List<Integer> EXISTING_MAPS = new ArrayList<Integer>();

	private static final int MAX_REGION_X = 127;
	private static final int MAX_REGION_Y = 255;

	public static int[] findEmptyRegionBound(int widthChunks, int heightChunks) {
		int regionHash = findEmptyRegionId(widthChunks, heightChunks);
		return new int[] { (regionHash >> 8), regionHash & 0xff };
	}

	public static int[] findEmptyChunkBound(int widthChunks, int heightChunks) {
		int[] map = findEmptyRegionBound(widthChunks, heightChunks);
		map[0] *= 8;
		map[1] *= 8;
		return map;
	}

	public static int getRegionId(int mapX, int mapY) {
		return (mapX << 8) + mapY;
	}

	public static int findEmptyRegionId(int widthChunks, int heightChunks) {
		int regionsDistanceX = 1;
		while (widthChunks > 8) {
			regionsDistanceX += 1;
			widthChunks -= 8;
		}
		int regionsDistanceY = 1;
		while (heightChunks > 8) {
			regionsDistanceY += 1;
			heightChunks -= 8;
		}
		synchronized (ALGORITHM_LOCK) {
			for (int regionX = 1; regionX <= MAX_REGION_X - regionsDistanceX; regionX++) {
				skip: for (int regionY = 1; regionY <= MAX_REGION_Y - regionsDistanceY; regionY++) {
					int regionHash = getRegionId(regionX, regionY); // map
					// hash
					// because
					// skiping
					// to next
					// map up
					for (int checkRegionX = regionX - 1; checkRegionX <= regionX + regionsDistanceX; checkRegionX++) {
						for (int checkRegionY = regionY - 1; checkRegionY <= regionY
								+ regionsDistanceY; checkRegionY++) {
							int hash = getRegionId(checkRegionX, checkRegionY);
							if (regionExists(hash))
								continue skip;

						}
					}
					reserveArea(regionX, regionY, regionsDistanceX, regionsDistanceY, false);
					return regionHash;
				}
			}
		}
		return -1;

	}

	public static void reserveArea(int fromRegionX, int fromRegionY, int width, int height, boolean remove) {
		for (int regionX = fromRegionX; regionX < fromRegionX + width; regionX++) {
			for (int regionY = fromRegionY; regionY < fromRegionY + height; regionY++) {
				if (remove)
					EXISTING_MAPS.remove((Integer) getRegionId(regionX, regionY));
				else
					EXISTING_MAPS.add(getRegionId(regionX, regionY));
			}
		}
	}

	public static void reserveRegion(int regionX, int regionY) {
		EXISTING_MAPS.add(getRegionId(regionX, regionY));
	}

	public static void reserveRegion(int region) {
		EXISTING_MAPS.add(region);
	}

	public static boolean regionExists(int mapHash) {
		return EXISTING_MAPS.contains(mapHash);

	}

	public static void cutChunk(int chunkX, int chunkY, int plane) {
		DynamicRegion toRegion = createDynamicRegion((((chunkX / 8) << 8) + (chunkY / 8)));
		int offsetX = (chunkX - ((chunkX / 8) * 8));
		int offsetY = (chunkY - ((chunkY / 8) * 8));
		toRegion.getRegionCoords()[plane][offsetX][offsetY][0] = 0;
		toRegion.getRegionCoords()[plane][offsetX][offsetY][1] = 0;
		toRegion.getRegionCoords()[plane][offsetX][offsetY][2] = 0;
		toRegion.getRegionCoords()[plane][offsetX][offsetY][3] = 0;
		toRegion.setReloadObjects(plane, offsetX, offsetY);
	}

	public static final void destroyMap(int chunkX, int chunkY, int widthRegions, int heightRegions) {
		synchronized (ALGORITHM_LOCK) {
			int fromRegionX = chunkX / 8;
			int fromRegionY = chunkY / 8;
			int regionsDistanceX = 1;
			while (widthRegions > 8) {
				regionsDistanceX += 1;
				widthRegions -= 8;
			}
			int regionsDistanceY = 1;
			while (heightRegions > 8) {
				regionsDistanceY += 1;
				heightRegions -= 8;
			}
			for (int regionX = fromRegionX; regionX < fromRegionX + regionsDistanceX; regionX++) {
				for (int regionY = fromRegionY; regionY < fromRegionY + regionsDistanceY; regionY++) {
					destroyRegion(getRegionId(regionX, regionY));
				}
			}
			reserveArea(fromRegionX, fromRegionY, regionsDistanceX, regionsDistanceY, true);
		}
	}

	public static final void repeatMap(int toChunkX, int toChunkY, int widthChunks, int heightChunks, int rx, int ry,
			int plane, int rotation, int... toPlanes) {
		for (int xOffset = 0; xOffset < widthChunks; xOffset++) {
			for (int yOffset = 0; yOffset < heightChunks; yOffset++) {
				int nextChunkX = toChunkX + xOffset;
				int nextChunkY = toChunkY + yOffset;
				DynamicRegion toRegion = createDynamicRegion((((nextChunkX / 8) << 8) + (nextChunkY / 8)));
				int regionOffsetX = (nextChunkX - ((nextChunkX / 8) * 8));
				int regionOffsetY = (nextChunkY - ((nextChunkY / 8) * 8));
				for (int pIndex = 0; pIndex < toPlanes.length; pIndex++) {
					int toPlane = toPlanes[pIndex];
					toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][0] = rx;
					toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][1] = ry;
					toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][2] = plane;
					toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][3] = rotation;
					toRegion.setReloadObjects(toPlane, regionOffsetX, regionOffsetY);
				}
			}
		}
	}

	public static final void cutMap(int toChunkX, int toChunkY, int widthChunks, int heightChunks, int... toPlanes) {
		for (int xOffset = 0; xOffset < widthChunks; xOffset++) {
			for (int yOffset = 0; yOffset < heightChunks; yOffset++) {
				int nextChunkX = toChunkX + xOffset;
				int nextChunkY = toChunkY + yOffset;
				DynamicRegion toRegion = createDynamicRegion((((nextChunkX / 8) << 8) + (nextChunkY / 8)));
				int regionOffsetX = (nextChunkX - ((nextChunkX / 8) * 8));
				int regionOffsetY = (nextChunkY - ((nextChunkY / 8) * 8));
				for (int pIndex = 0; pIndex < toPlanes.length; pIndex++) {
					int toPlane = toPlanes[pIndex];
					toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][0] = 0;
					toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][1] = 0;
					toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][2] = 0;
					toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][3] = 0;
					toRegion.setReloadObjects(toPlane, regionOffsetX, regionOffsetY);
				}
			}
		}
	}

	/*
	 * copys a single 8x8 map tile and allows you to rotate it
	 */
	public static void copyChunk(int fromChunkX, int fromChunkY, int fromPlane, int toChunkX, int toChunkY, int toPlane,
			int rotation) {
		DynamicRegion toRegion = createDynamicRegion(((toChunkX / 8) << 8) + (toChunkY / 8));
		int regionOffsetX = toChunkX - ((toChunkX / 8) * 8);
		int regionOffsetY = toChunkY - ((toChunkY / 8) * 8);
		toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][0] = fromChunkX;
		toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][1] = fromChunkY;
		toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][2] = fromPlane;
		toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][3] = rotation;
		toRegion.setReloadObjects(toPlane, regionOffsetX, regionOffsetY);
	}

	/*
	 * copy a exactly square of map from a place to another
	 */
	public static final void copyAllPlanesMap(int fromRegionX, int fromRegionY, int toRegionX, int toRegionY,
			int ratio) {
		int[] planes = new int[4];
		for (int plane = 1; plane < 4; plane++)
			planes[plane] = plane;
		copyMap(fromRegionX, fromRegionY, toRegionX, toRegionY, ratio, ratio, planes, planes);
	}

	/*
	 * copy a exactly square of map from a place to another
	 */
	public static final void copyAllPlanesMap(int fromRegionX, int fromRegionY, int toRegionX, int toRegionY,
			int widthRegions, int heightRegions) {
		int[] planes = new int[4];
		for (int plane = 1; plane < 4; plane++)
			planes[plane] = plane;
		copyMap(fromRegionX, fromRegionY, toRegionX, toRegionY, widthRegions, heightRegions, planes, planes);
	}

	/*
	 * copy a square of map from a place to another
	 */
	public static final void copyMap(int fromRegionX, int fromRegionY, int toRegionX, int toRegionY, int ratio,
			int[] fromPlanes, int[] toPlanes) {
		copyMap(fromRegionX, fromRegionY, toRegionX, toRegionY, ratio, ratio, fromPlanes, toPlanes);
	}

	/*
	 * copy a rectangle of map from a place to another
	 */
	public static final void copyMap(int fromRegionX, int fromRegionY, int toRegionX, int toRegionY, int widthRegions,
			int heightRegions, int[] fromPlanes, int[] toPlanes) {
		if (fromPlanes.length != toPlanes.length)
			throw new RuntimeException("PLANES LENGTH ISNT SAME OF THE NEW PLANES ORDER!");
		for (int xOffset = 0; xOffset < widthRegions; xOffset++) {
			for (int yOffset = 0; yOffset < heightRegions; yOffset++) {
				int fromThisRegionX = fromRegionX + xOffset;
				int fromThisRegionY = fromRegionY + yOffset;
				int toThisRegionX = toRegionX + xOffset;
				int toThisRegionY = toRegionY + yOffset;
				int regionId = ((toThisRegionX / 8) << 8) + (toThisRegionY / 8);
				DynamicRegion toRegion = createDynamicRegion(regionId);
				int regionOffsetX = (toThisRegionX - ((toThisRegionX / 8) * 8));
				int regionOffsetY = (toThisRegionY - ((toThisRegionY / 8) * 8));
				for (int pIndex = 0; pIndex < fromPlanes.length; pIndex++) {
					int toPlane = toPlanes[pIndex];
					toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][0] = fromThisRegionX;
					toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][1] = fromThisRegionY;
					toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][2] = fromPlanes[pIndex];
					toRegion.setReloadObjects(toPlane, regionOffsetX, regionOffsetY);
				}
			}
		}
	}

	/**
	 * Copys a square area with the desired ratio.
	 * @param fromChunkX
	 * @param fromChunkY
	 * @param toChunkX
	 * @param toChunkY
	 * @param ratio
	 * @param rotation TODO fix rotation
	 */
	public static final void copyRatioSquare(int fromChunkX, int fromChunkY, int toChunkX, int toChunkY, int ratio, int rotation) {
		for (int x = 0; x < (ratio * 2); x++) {
			for (int y = 0; y < (ratio * 2); y++) {				
				copyChunk(fromChunkX, fromChunkY, 0, toChunkX + (rotation == 2 || rotation == 3 ? x : 0), 
						toChunkY + (rotation == 1 || rotation == 2 ? y : 0), 0, rotation);			
				copyChunk(fromChunkX + x, fromChunkY, 0, toChunkX + (rotation == 0 || rotation == 3 ? x : 0),
						toChunkY + (rotation == 2 || rotation == 3 ? y : 0), 0, rotation);				
				copyChunk(fromChunkX, fromChunkY + y, 0, toChunkX + (rotation == 1 || rotation == 2 ? x : 0), 
						toChunkY + (rotation == 0 || rotation == 1 ? y : 0), 0, rotation);				
				copyChunk(fromChunkX + x, fromChunkY + y, 0, toChunkX + (rotation == 0 || rotation == 1 ? x : 0), 
						toChunkY + (rotation == 0 || rotation == 3 ? y : 0), 0, rotation);				
			}
		}
	}

	/**
	 * Copys a square area with the desired ratio.
	 * @param fromChunkX
	 * @param fromChunkY
	 * @param toChunkX
	 * @param toChunkY
	 * @param ratio
	 * @param rotation TODO fix rotation
	 */
	public static final void copyRatioSquare(int fromChunkX, int fromChunkY, int fromPlane, int toChunkX, int toChunkY, int toPlane, int ratio, int rotation) {
		for (int x = 0; x < (ratio * 2); x++) {
			for (int y = 0; y < (ratio * 2); y++) {
				copyChunk(fromChunkX, fromChunkY, fromPlane, toChunkX + (rotation == 2 || rotation == 3 ? x : 0), toChunkY + (rotation == 1 || rotation == 2 ? y : 0), toPlane, rotation);
				copyChunk(fromChunkX + x, fromChunkY, fromPlane, toChunkX + (rotation == 0 || rotation == 3 ? x : 0), toChunkY + (rotation == 2 || rotation == 3 ? y : 0), toPlane, rotation);
				copyChunk(fromChunkX, fromChunkY + y, fromPlane, toChunkX + (rotation == 1 || rotation == 2 ? x : 0), toChunkY + (rotation == 0 || rotation == 1 ? y : 0), toPlane, rotation);
				copyChunk(fromChunkX + x, fromChunkY + y, fromPlane, toChunkX + (rotation == 0 || rotation == 1 ? x : 0), toChunkY + (rotation == 0 || rotation == 3 ? y : 0), toPlane, rotation);
			}
		}
	}

	/*
	 * temporary and used for dungeonnering only
	 * 
	 * //rotation 0 // a b // c d //rotation 1 // c a // d b //rotation2 // d c // b
	 * a //rotation3 // b d // a c
	 */
	public static final void copy2RatioSquare(int fromRegionX, int fromRegionY, int toRegionX, int toRegionY,
			int rotation) {
		if (rotation == 0) {
			copyChunk(fromRegionX, fromRegionY, 0, toRegionX, toRegionY, 0, rotation);
			copyChunk(fromRegionX + 1, fromRegionY, 0, toRegionX + 1, toRegionY, 0, rotation);
			copyChunk(fromRegionX, fromRegionY + 1, 0, toRegionX, toRegionY + 1, 0, rotation);
			copyChunk(fromRegionX + 1, fromRegionY + 1, 0, toRegionX + 1, toRegionY + 1, 0, rotation);
		} else if (rotation == 1) {
			copyChunk(fromRegionX, fromRegionY, 0, toRegionX, toRegionY + 1, 0, rotation);
			copyChunk(fromRegionX + 1, fromRegionY, 0, toRegionX, toRegionY, 0, rotation);
			copyChunk(fromRegionX, fromRegionY + 1, 0, toRegionX + 1, toRegionY + 1, 0, rotation);
			copyChunk(fromRegionX + 1, fromRegionY + 1, 0, toRegionX + 1, toRegionY, 0, rotation);
		} else if (rotation == 2) {
			copyChunk(fromRegionX, fromRegionY, 0, toRegionX + 1, toRegionY + 1, 0, rotation);
			copyChunk(fromRegionX + 1, fromRegionY, 0, toRegionX, toRegionY + 1, 0, rotation);
			copyChunk(fromRegionX, fromRegionY + 1, 0, toRegionX + 1, toRegionY, 0, rotation);
			copyChunk(fromRegionX + 1, fromRegionY + 1, 0, toRegionX, toRegionY, 0, rotation);
		} else if (rotation == 3) {
			copyChunk(fromRegionX, fromRegionY, 0, toRegionX + 1, toRegionY, 0, rotation);
			copyChunk(fromRegionX + 1, fromRegionY, 0, toRegionX + 1, toRegionY + 1, 0, rotation);
			copyChunk(fromRegionX, fromRegionY + 1, 0, toRegionX, toRegionY, 0, rotation);
			copyChunk(fromRegionX + 1, fromRegionY + 1, 0, toRegionX, toRegionY + 1, 0, rotation);
		}
	}

	/*
	 * not recommended to use unless you want to make a more complex map
	 */
	public static DynamicRegion createDynamicRegion(int regionId) {
		synchronized (ALGORITHM_LOCK) {
			Region region = World.getRegions().get(regionId);
			if (region != null) {
				if (region instanceof DynamicRegion) // if its already dynamic
					// lets
					// keep building it
					return (DynamicRegion) region;
				else
					destroyRegion(regionId);
			}
			DynamicRegion newRegion = new DynamicRegion(regionId);
			World.getRegions().put(regionId, newRegion);
			return newRegion;
		}
	}

	/*
	 * Safely destroys a dynamic region
	 */
	public static void destroyRegion(int regionId) {
		Region region = World.getRegions().get(regionId);
		if (region != null) {
			List<Integer> playerIndexes = region.getPlayerIndexes();
			List<Integer> npcIndexes = region.getNPCsIndexes();
			if (region.getGroundItems() != null)
				region.getGroundItems().clear();
			region.getSpawnedObjects().clear();
			region.getRemovedOriginalObjects().clear();
			if (npcIndexes != null) {
				for (int npcIndex : npcIndexes) {
					NPC npc = World.getNPCs().get(npcIndex);
					if (npc == null)
						continue;
					if (npc instanceof Familiar) {
						npc.setLocation(0, 0, 0); // sets 0,0,0 and forces to
						// teleto player and not force
						// load region and loss space
						continue;
					}
					npc.finish();
				}
			}
			World.getRegions().remove(regionId);

			if (playerIndexes != null) {
				for (int playerIndex : playerIndexes) {
					Player player = World.getPlayers().get(playerIndex);
					if (player == null || !player.hasStarted() || player.isFinished())
						continue;
					player.setForceNextMapLoadRefresh(true);
					player.loadMapRegions();
				}
			}
		}
	}

	private RegionBuilder() {

	}

	public static int[] findEmptyMap(int widthRegions, int heightRegions) {
		boolean lastSearchPositive = RegionBuilder.lastSearchPositive = !RegionBuilder.lastSearchPositive;
		int regionsXDistance = ((widthRegions) / 8) + 1; // 1map distance at
															// least
		int regionsYDistance = ((heightRegions) / 8) + 1; // 1map distance at
															// least
		for (int regionIdC = 0; regionIdC < 23629; regionIdC++) {
			int regionId = lastSearchPositive ? 20000 - regionIdC : regionIdC;
			int regionX = (regionId >> 8) * 64;
			int regionY = (regionId & 0xff) * 64;
			if (regionX >> 3 < 336 || regionY >> 3 < 336)
				continue;
			boolean found = true;
			for (int thisRegionX = regionX - 64; thisRegionX < (regionX + (regionsXDistance * 64)); thisRegionX += 64) {
				for (int thisRegionY = regionY - 64; thisRegionY < (regionY
						+ (regionsYDistance * 64)); thisRegionY += 64) {
					if (thisRegionX < 0 || thisRegionY < 0)
						continue;
					if (!emptyRegion(thisRegionX, thisRegionY,
							!(thisRegionX < regionX || thisRegionY < regionY
									|| thisRegionX > (regionX + ((regionsXDistance - 1) * 64)))
									|| thisRegionY > (regionY + ((regionsYDistance - 1) * 64)))) {
						found = false;
						break;
					}

				}
			}
			if (found)
				return new int[] { getRegion(regionX), getRegion(regionY) };
		}
		return null;
	}

	public static int getRegion(int c) {
		return c >> 3;
	}

	private static boolean emptyRegion(int regionX, int regionY, boolean checkValid) {
		if (regionX > 10000 || regionY > 16000)
			return !checkValid; // invalid map gfto
		int rx = getRegion(regionX) / 8;
		int ry = getRegion(regionY) / 8;
		if (Cache.STORE.getIndexes()[5].getArchiveId("m" + rx + "_" + ry) != -1)
			return false; // a real map already exists
		Region region = World.getRegions().get((rx << 8) + ry);
		return region == null || !(region instanceof DynamicRegion);
	}
}
