package com.rs.game.map;

import com.rs.Constants;
import com.rs.game.world.World;
import com.rs.utility.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class Position extends AttributesHolder implements Serializable {
	private transient Position lastPosition;
	private static final long serialVersionUID = -6567346497259686765L;

	private short x, y;
	private byte z;
	
    public int getXInChunk() {
    	return x & 0x7;
    }

    public int getYInChunk() {
    	return y & 0x7;
    }

	public Position(int x, int y, int plane) {
		this.x = (short) x;
		this.y = (short) y;
		this.z = (byte) plane;
	}
	public Position getPosition() {
		return new Position(getX(), getY(), getZ());
	}
	public Position getLastPosition() {
		return lastPosition;
	}
	
	public String getLocation() {
		return "X - "+getX()+", Y - "+getY()+", Z - "+ getZ();
	}

	public Position(Position tile) {
		this.x = tile.x;
		this.y = tile.y;
		this.z = tile.z;
	}

	public Position(Position tile, int randomize) {
		this.x = (short) (tile.x + Utils.getRandom(randomize * 2) - randomize);
		this.y = (short) (tile.y + Utils.getRandom(randomize * 2) - randomize);
		this.z = tile.z;
	}
	
	public Position(Position tile, int min, int max) {
		this.x = (short) (tile.x + Utils.getRandom(max * 2) - min);
		this.y = (short) (tile.y + Utils.getRandom(max * 2) - min);
		this.z = tile.z;
	}

	public Position(int hash) {
		this.x = (short) (hash >> 14 & 0x3fff);
		this.y = (short) (hash & 0x3fff);
		this.z = (byte) (hash >> 28);
	}

	public Position(int x, int y) {
		this.x = (short) x;
		this.y = (short) y;
		this.z = 0;
	}

	public void moveLocation(int xOffset, int yOffset, int planeOffset) {
		x += xOffset;
		y += yOffset;
		z += planeOffset;
	}

	public final void setLocation(Position tile) {
		setLocation(tile.x, tile.y, tile.z);
	}

	public final void setLocation(int x, int y, int plane) {
		this.x = (short) x;
		this.y = (short) y;
		this.z = (byte) plane;
	}

	public int getX() {
		return x;
	}

	public int getXInRegion() {
		return x & 0x3F;
	}

	public int getYInRegion() {
		return y & 0x3F;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public int getChunkX() {
		return (x >> 3);
	}

	public int getChunkY() {
		return (y >> 3);
	}

	public int getRegionX() {
		return (x >> 6);
	}

	public int getRegionY() {
		return (y >> 6);
	}

	public int getRegionId() {
		return ((getRegionX() << 8) + getRegionY());
	}

	public int getLocalX(Position tile, int mapSize) {
		return x - 8 * (tile.getChunkX() - (Constants.MAP_SIZES[mapSize] >> 4));
	}

	public int getLocalY(Position tile, int mapSize) {
		return y - 8 * (tile.getChunkY() - (Constants.MAP_SIZES[mapSize] >> 4));
	}

	public int getLocalX(Position tile) {
		return getLocalX(tile, 0);
	}

	public int getLocalY(Position tile) {
		return getLocalY(tile, 0);
	}

	public int getLocalX() {
		return getLocalX(this);
	}

	public int getLocalY() {
		return getLocalY(this);
	}

	public int getRegionHash() {
		return getRegionY() + (getRegionX() << 8) + (z << 16);
	}

	public int getTileHash() {
		return y + (x << 14) + (z << 28);
	}

	public boolean withinDistance(Position tile, int distance) {
		if (tile.z != z)
			return false;
		int deltaX = tile.x - x, deltaY = tile.y - y;
		return deltaX <= distance && deltaX >= -distance && deltaY <= distance
				&& deltaY >= -distance;
	}

	public boolean withinDistance(Position tile) {
		if (tile.z != z)
			return false;
		// int deltaX = tile.x - x, deltaY = tile.y - y;
		return Math.abs(tile.x - x) <= 14 && Math.abs(tile.y - y) <= 14;// deltaX
																		// <= 14
																		// &&
																		// deltaX
																		// >=
																		// -15
																		// &&
																		// deltaY
																		// <= 14
																		// &&
																		// deltaY
																		// >=
																		// -15;
	}

	public List<Position> area(int radius, Predicate<Position> filter) {
		List<Position> list = new ArrayList<>((int)Math.pow((1 + radius), 2));
		for (int x = -radius; x <= radius; x++) {
			for (int y = -radius; y <= +radius; y++) {
				Position pos = relative(x, y);
				if (filter.test(pos))
					list.add(pos);
			}
		}
		return list;
	}

	public Position relative(int changeX, int changeY, int changeZ) {
		return copy().translate(changeX, changeY, changeZ);
	}

	public Position translate(int changeX, int changeY, int changeZ) {
		x += changeX;
		y += changeY;
		z += changeZ;
		return this;
	}

	public Position translate(int changeX, int changeY) {
		x += changeX;
		y += changeY;
		return this;
	}

	public Position relative(int changeX, int changeY) {
		return relative(changeX, changeY, 0);
	}

	public boolean isWithinDistance(Position other) {
		return isWithinDistance(other, 14);
	}

	public boolean isWithinDistance(Position other, int distance) {
		return isWithinDistance(other, true, distance);
	}

	public boolean isWithinDistance(Position other, boolean checkHeight, int distance) {
		return (!checkHeight || other.z == z) && Math.abs(x - other.x) <= distance && Math.abs(y - other.y) <= distance;
	}

	public boolean inBounds(Bounds bounds) {
		return bounds.inBounds(x, y, z, 0);
	}
	
	public int getCoordFaceX(int sizeX) {
		return getCoordFaceX(sizeX, -1, -1);
	}

	public static final int getCoordFaceX(int x, int sizeX, int sizeY,
			int rotation) {
		return x + ((rotation == 1 || rotation == 3 ? sizeY : sizeX) - 1) / 2;
	}

	public static final int getCoordFaceY(int y, int sizeX, int sizeY,
			int rotation) {
		return y + ((rotation == 1 || rotation == 3 ? sizeX : sizeY) - 1) / 2;
	}

	public int getCoordFaceX(int sizeX, int sizeY, int rotation) {
		return x + ((rotation == 1 || rotation == 3 ? sizeY : sizeX) - 1) / 2;
	}

	public int getCoordFaceY(int sizeY) {
		return getCoordFaceY(-1, sizeY, -1);
	}

	public int getCoordFaceY(int sizeX, int sizeY, int rotation) {
		return y + ((rotation == 1 || rotation == 3 ? sizeX : sizeY) - 1) / 2;
	}
	
	public Position transform(int x, int y, int plane) {
		return new Position(this.x + x, this.y + y, this.z + plane);
	}
	
	public void set(Position tile) {
		this.x = tile.x;
		this.y = tile.y;
		this.z = tile.z;
	}

	public Position copy() {
		return new Position(x, y, z);
	}

	/**
	 * Checks if this world tile's coordinates match the other world tile.
	 * @param other The world tile to compare with.
	 * @return {@code True} if so.
	 */
	public boolean matches(Position other) {
		return x == other.x && y == other.y && z == other.z;
	}
	
	public boolean nextTo(Position other) {
		List<Position> tiles = Arrays.asList(Utils.getAdjacentTiles(other));
		for (Position tile : tiles) {
			if (tile.matches(this))
				return true;
		}
		return false;
	}
	
    public boolean withinArea(int a, int b, int c, int d) {
    	return getX() >= a && getY() >= b && getX() <= c && getY() <= d;
    }

	public int unitVectorX(Position target) {
		int diff = target.getX() - getX();
		if (diff != 0)
			diff /= Math.abs(diff);
		return diff;
	}

	public int unitVectorY(Position target) {
		int diff = target.getY() - getY();
		if (diff != 0)
			diff /= Math.abs(diff);
		return diff;
	}

    public static Position getLocationByName(String name) {
		if(name.equals("Edgevill"))
			 new Position(3087, 3492, 0);
		if(name.equals("Lumbridge"))
			 new Position(3222, 3219, 0);
		if(name.equals("Al-kharid"))
			 new Position(3293, 3188, 0);
		if(name.equals("Varrock"))
			 new Position(3211, 3423, 0);
		if(name.equals("Falador"))
			 new Position(2965, 3379, 0);
		if(name.equals("Camelot"))
			 new Position(2757, 3478, 0);
		if(name.equals("Ardounge"))
			 new Position(2661, 3305, 0);
		if(name.equals("Watchtower"))
			 new Position(2569, 3098, 0);
		if(name.equals("Trollheim"))
			 new Position(2867, 3593, 0);
		if(name.equals("Ape Atoll"))
			 new Position(2764, 2775, 0);
		if(name.equals("Canifas"))
			 new Position(3052, 3497, 0);
		if(name.equals("Port Sarim"))
			 new Position(3025, 3217, 0);
		if(name.equals("Rimmington"))
			 new Position(2957, 3214, 0);
		if(name.equals("Draynor"))
			 new Position(3093, 3244, 0);
		if(name.equals("IceQueen Lair"))
			 new Position(2866, 9953, 0);
		if(name.equals("Brimhaven Dungeon"))
			 new Position(2713, 9453, 0);
		if(name.equals("Gnome Agility"))
			 new Position(2477, 3438, 0);
		if(name.equals("Wilderness Agility"))
			 new Position(2998, 3932, 0);
		if(name.equals("Distant kingdom"))
			 new Position(2767, 4723, 0);
		if(name.equals("Maze Event"))
			 new Position(2911, 4551, 0);
		if(name.equals("Drill Instructor"))
			 new Position(3163, 4828, 0);
		if(name.equals("Grave Digger"))
			 new Position(1928, 5002, 0);
		if(name.equals("Karamja Lessers"))
			 new Position(2835, 9563, 0);
		if(name.equals("Evil Bob's Island"))
			 new Position(2525, 4776, 0);
		if(name.equals("Secret Island"))
			 new Position(2152, 5095, 0);
		if(name.equals("Ibans Trap"))
			 new Position(2319, 9804, 0);
		if(name.equals("Fishing Docks"))
			 new Position(2767, 3277, 0);
		if(name.equals("Mage Trainging"))
			 new Position(3365, 9640, 0);
		if(name.equals("Quest Place"))
			 new Position(2907, 9712, 0);
		if(name.equals("Duel Arena"))
			 new Position(3367, 3267, 0);
		if(name.equals("Bandit Camp"))
			 new Position(3171, 3028, 0);
		if(name.equals("Uzer"))
			 new Position(3484, 3092, 0);
		if(name.equals("SeaQueen"))
			 new Position(3484, 3092, 0);

		return null;
	}
	public int getHash() {
		return y | x << 14 | z << 28;
	}

	public boolean equals(int x, int y) {
		return this.x == x && this.y == y;
	}

	public int getClip() {
		return World.getMask(z, x, y);
	}

	public boolean isClipped() {
		return !World.isNotCliped(z, x, y, 1);
	}

	public static Position of(int x, int y) {
		return of(x, y, 0);
	}

	public static Position of(int x, int y, int z) {
		return new Position(x, y, z);
	}

	public Region getRegion() {
		return World.getRegion(getRegionId(), true);
	}

	public void clip() {
		getRegion().getRegionMap().clip(z, getXInRegion(), getYInRegion());
	}

	public void unclip() {
		getRegion().getRegionMap().unclip(z, getXInRegion(), getYInRegion());
	}

	public boolean isUnclipped() {
		return !isClipped();
	}

}
