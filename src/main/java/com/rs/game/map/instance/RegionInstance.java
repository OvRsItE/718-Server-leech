/**
 * 
 */
package com.rs.game.map.instance;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import com.rs.cores.CoresManager;
import com.rs.game.map.RegionBuilder;
import com.rs.game.map.Position;
import lombok.Getter;
import lombok.Setter;

/**
 * Handles instance creation.
 * @author ReverendDread
 * @author Kris for original design.
 * Jul 23, 2018
 */
public class RegionInstance implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Getter @Setter protected int chunkX, chunkY, regionX, regionY, size;

	private final int[] coordinates;

	public RegionInstance(final int size, final int regionX, final int regionY) {
		this.size = size;
		this.regionX = regionX;
		this.regionY = regionY;
		this.coordinates = RegionBuilder.findEmptyChunkBound(size, size);
		this.chunkX = coordinates[0];
		this.chunkY = coordinates[1];
	}
	
	/**
	 * Gets an instanced worldtile based on the coordinates of the static area in the map.
	 * @param x coordinate to the static map location.
	 * @param y coordinate to the static map location.
	 * @param height height of the static map location.
	 * @return instanced worldtile.
	 */
	public Position getLocation(final int x, final int y, final int height) {
		final int offsetX = x - (regionX * 8);
		final int offsetY = y - (regionY * 8);
		return new Position((chunkX * 8) + offsetX, (chunkY * 8) + offsetY, height);
	}
	
	/**
	* Gets an instanced worldtile based on the coordinates of the static area in the map.
	 * @param tile coordinates to the static map location.
	 * @return instanced worldtile.
	 */
	public Position getLocation(final Position tile) {
		final int offsetX = tile.getX() - (regionX * 8);
		final int offsetY = tile.getY() - (regionY * 8);
		return new Position((chunkX * 8) + offsetX, (chunkY * 8) + offsetY, tile.getZ());
	}
	
	/**
	 * Checks if the tile is within the desired boundry.
	 * @param tile
	 * 			the tile to check.
	 * @param bottom_left
	 * 			the bottom left.
	 * @param top_right
	 * 			the top right.
	 * @return
	 * 			the result.
	 */
	public boolean withinArea(final Position tile, final Position bottom_left, final Position top_right) {
		final int offsetX_left = bottom_left.getX() - (regionX * 8);
		final int offsetY_left = bottom_left.getY() - (regionY * 8);
		final int offsetX_right = top_right.getX() - (regionX * 8);
		final int offsetY_right = top_right.getY() - (regionY * 8);
		return tile.withinArea((chunkX * 8) + offsetX_left, (chunkY * 8) + offsetY_left, (chunkX * 8) + offsetX_right, (chunkY * 8) + offsetY_right);
	}

	/**
	 * Constructs the instanced area on the map.
	 * Handled on a different thread from the game thread, to avoid problems with load
	 * since it takes quite a long time to build.
	 */
	public void constructRegion() {
		constructRegion(null);
	}
	
	/**
	 * Constructs the instanced area on the map.
	 * Handled on a different thread from the game thread, to avoid problems with load
	 * since it takes quite a long time to build.
	 */
	public void constructRegion(Function<RegionInstance, Boolean> function) {
		CoresManager.slowExecutor.schedule(() -> { 
			RegionBuilder.copyAllPlanesMap(regionX, regionY, chunkX, chunkY, size);
			if (function != null)
			function.apply(null);
		}, 1000, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Destroys the instanced area on the map.
	 */
	public final void destroyRegion() {
		CoresManager.slowExecutor.schedule(() -> {
			RegionBuilder.destroyMap(chunkX, chunkY, size, size);
		}, 5000, TimeUnit.MILLISECONDS);
	}
	
}
