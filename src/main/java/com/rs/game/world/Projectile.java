/**
 * 
 */
package com.rs.game.world;

import com.rs.Constants;
import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.utility.Utils;

import lombok.Getter;

/**
 * @author ReverendDread | Convertions to Matrix.
 * @author Kris | 14. okt 2017 : 10:35.03
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class Projectile {
	
	/** The default values for projectiles. */
	private static final int DEFAULT_SLOPE = 0, DEFAULT_OFFSET = 0, DEFAULT_SPEED = 10, DEFAULT_DELAY = 0;

	/** The id of the graphics that's going to be shot as a projectile. */
	@Getter private final int graphicsId;
	
	/** The starting height of the projectile. */
	@Getter private final int startHeight;
	
	/** The ending height of the projectile. */
	@Getter private final int endHeight;
	
	/** The delay before projectile starts flying, uses {@link Constant#CLIENT_CYCLE} as units. */
	@Getter private final int delay;
	
	/** The angle of the projectile, will be converted from actual degrees to units client reads. {@value can be from 0-90, excluding both extremes.} */
	@Getter private final int angle;
	
	/** The duration of the projectile that will be added or subtracted from the original flight duration of the projectile which is calculated from distance and delay. */
	@Getter private final int speed;
	
	/** The offset in distance. The projectile will be moved closer towards the end by input value. {@value 64 is equal to one tile distance.} */
	@Getter private final int distanceOffset;

	public Projectile(final int graphicsId, final int startHeight, final int endHeight, final int delay, final int angle, final int speed, final int distanceOffset) {
		this.graphicsId = graphicsId;
		this.startHeight = startHeight;
		this.endHeight = endHeight;
		this.delay = delay;
		this.angle = angle;
		this.speed = speed;
		this.distanceOffset = distanceOffset;
	}

	public Projectile(final int graphicsId, final int startHeight, final int endHeight, final int duration, final int delay) {
		this(graphicsId, startHeight, endHeight, delay, DEFAULT_SLOPE, duration, DEFAULT_OFFSET);
	}

	public Projectile(final int graphicsId, final int startHeight, final int endHeight, final int delay) {
		this(graphicsId, startHeight, endHeight, delay, DEFAULT_SLOPE, DEFAULT_SPEED, DEFAULT_OFFSET);
	}

	public Projectile(final int graphicsId, final int startHeight, final int endHeight) {
		this(graphicsId, startHeight, endHeight, DEFAULT_DELAY, DEFAULT_SLOPE, DEFAULT_SPEED, DEFAULT_OFFSET);
	}
	
	public int getHitSyncToMillis(final Position from, final Position to) {
		return getHitSync(from, to) * Constants.WORLD_CYCLE_TIME;
	}
	
	public int getHitSync(final Position from, final Position to) {
		return (int) Math.floor(Utils.getDistance(from, to) / 3);
	}

	public int getDuration(final Position startTile, final Position endTile) {
		return Utils.getDistance(startTile.getX(), startTile.getY(), endTile.getX(), endTile.getY()) * 30 / ((speed / 10) < 1 ? 1 : (speed / 10)) + delay;
	}

	public int send(Entity sender, Entity receiver) {
		World.sendProjectile(sender, receiver, this);
		return getHitSync(sender, receiver);
	}

	public int send(Position from, Entity to) {
		World.sendProjectile(from, to, this);
		return getHitSync(from, to);
	}

	public int send(Position from, Position to) {
		World.sendProjectile(from, to, this);
		return getHitSync(from, to);
	}

	public int send(Entity from, Position to) {
		World.sendProjectile(from, to, this);
		return getHitSync(from, to);
	}

}
