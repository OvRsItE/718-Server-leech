package com.rs.game.world.entity.npc.dungeonnering;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.Drop;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonManager;
import com.rs.game.world.entity.player.content.skills.dungeoneering.RoomReference;
import com.rs.game.world.entity.updating.impl.Hit;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public final class IcyBones extends DungeonBoss {

	public IcyBones(int id, Position tile, DungeonManager manager, RoomReference reference) {
		super(id, tile, manager, reference);
		spikes = new ArrayList<WorldObject>();
	}

	@Override
	public double getMeleePrayerMultiplier() {
		return 0.6;
	}

	@Override
	public double getMagePrayerMultiplier() {
		return 0.6;
	}

	@Override
	public double getRangePrayerMultiplier() {
		return 0.6;
	}

	private List<WorldObject> spikes;

	public void removeSpikes() {
		if (spikes.isEmpty())
			return;
		for (WorldObject object : spikes)
			World.removeObject(object);
		spikes.clear();
	}

	public boolean sendSpikes() {
		if (!spikes.isEmpty())
			return false;
		int size = getSize();
		for (int x = -1; x < 7; x++) {
			for (int y = -1; y < 7; y++) {
				if (((x != -1 && x != 6) && (y != -1 && y != 6)) || com.rs.utility.Utils.random(2) != 0)
					continue;
				Position tile = transform(x - size, y - size, 0);
				RoomReference current = getManager().getCurrentRoomReference(tile);
				if (current.getX() != getReference().getX() || current.getY() != getReference().getY() || !World.isFloorFree(tile.getZ(), tile.getX(), tile.getY()))
					continue;
				WorldObject object = new WorldObject(52285 + com.rs.utility.Utils.random(3), 10, com.rs.utility.Utils.random(4), tile.getX(), tile.getY(), tile.getZ());
				spikes.add(object);
				World.spawnObject(object);
				for (Player player : getManager().getParty().getTeam()) {
					if (player.getX() == object.getX() && player.getY() == object.getY())
						player.applyHit(new Hit(this, 1 + com.rs.utility.Utils.random(getMaxHit()), Hit.HitLook.REGULAR_DAMAGE));
				}
			}
		}
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				removeSpikes();
			}

		}, 10);
		return true;
	}
	
	@Override
	public void sendDrop(Player player, Drop drop) {
		if (drop.getItemId() >= 27963 && drop.getItemId() <= 27973) {
			drop.setMinAmount(125);
			drop.setMaxAmount(125);
		}
		super.sendDrop(player, drop);
	}

}
