package com.rs.game.map;

import com.google.common.collect.Maps;
import com.rs.Constants;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.utility.Utils;
import lombok.Getter;

import java.util.Map;

@SuppressWarnings("serial")
public class WorldObject extends Position {

	private int id;
	private int type;
	private int rotation;
	private int life;
	private int amount = Utils.random(10);
	private int originalId;
	public ObjectAction[] actions;
	public Map<Object, Object> attributes;
	
    public void setId(int id) {
		this.id = id;
    }

	public WorldObject(int id, int type, int rotation, Position tile) {
		super(tile.getX(), tile.getY(), tile.getZ());
		this.id = id;
		this.originalId = id;
		this.type = type;
		this.rotation = rotation;
		this.life = 1;
	}
	
	public WorldObject(int id, int type, int rotation, int x, int y, int plane) {
		super(x, y, plane);
		this.id = id;
		this.originalId = id;
		this.type = type;
		this.rotation = rotation;
		this.life = 1;
	}
	
	public WorldObject(int id, int type, int rotation, int x, int y, int plane, int life) {
		super(x, y, plane);
		this.id = id;
		this.originalId = id;
		this.type = type;
		this.rotation = rotation;
		this.life = life;
	}
	

	public WorldObject(WorldObject object) {
		super(object.getX(), object.getY(), object.getZ());
		this.id = object.id;
		this.originalId = id;
		this.type = object.type;
		this.rotation = object.rotation;
		this.life = object.life;
	}

	public static int asOSRS(int id) {
    	return id + Constants.OSRS_OBJECTS_OFFSET;
	}

	public int getId() {
		return id;
	}

	public int getType() {
		return type;
	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}
	
	public void decrementObjectLife() {
		this.life--;
	}
	
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void decrementAmount() {
        this.amount--;
    }

	public ObjectDefinitions getDefinitions() {
		return ObjectDefinitions.getObjectDefinitions(id);
	}

	public void animate(int animation) {
		World.sendObjectAnimation(this, new Animation(animation));
	}

	public void animate(Animation animation) {
		World.sendObjectAnimation(this, animation);
	}

	public void delete() {
    	World.getRegion(getRegionId()).removeObject(this);
	}

	public void spawn(boolean clip) {
    	World.spawnObject(this, clip);
	}

	public void sendUpdate() {
    	for (Player player : World.getPlayers()) {
    		if (player == null || !player.hasStarted() || player.hasFinished() || !player.getMapRegionsIds().contains(getRegionId()))
    			continue;
    		player.getPackets().sendSpawnedObject(this);
		}
	}

	public Bounds getBounds() {
		return new Bounds(getX(), getY(), getX() + getDefinitions().getSizeX() - 1, getY() + getDefinitions().getSizeY() - 1, getZ());
	}

	public void temporary(int transform, int duration) {
    	setId(transform);
    	sendUpdate();
    	World.startEvent(event -> {
    		event.delay(duration);
    		restore();
		});
	}

	public void restore() {
    	setId(originalId);
    	sendUpdate();
	}

	public static boolean is(int id, boolean osrs, WorldObject object) {
    	return osrs ? WorldObject.asOSRS(id) == object.getId() : object.getId() == id;
	}

	@Override
	public String toString() {
		return "WorldObject{" +
				"id=" + id +
				", type=" + type +
				", rotation=" + rotation +
				", x=" + getX() +
				", y=" + getY() +
				", z=" + getZ() +
				'}';
	}
}
