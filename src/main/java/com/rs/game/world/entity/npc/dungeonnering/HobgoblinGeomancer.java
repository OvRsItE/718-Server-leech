package com.rs.game.world.entity.npc.dungeonnering;


import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonManager;
import com.rs.game.world.entity.player.content.skills.dungeoneering.RoomReference;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;

@SuppressWarnings("serial")
public class HobgoblinGeomancer extends DungeonBoss {

	public HobgoblinGeomancer(int id, Position tile, DungeonManager manager, RoomReference reference) {
		super(id, tile, manager, reference);
	}

	public void sendTeleport(final Position tile, final RoomReference room) {
		setCantInteract(true);
		animate(new Animation(12991, 70));
		setNextGraphics(new Graphics(1576, 70, 0));
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				setCantInteract(false);
				animate(new Animation(-1));
				setNextPosition(Utils.getFreeTile(getManager().getRoomCenterTile(room), 6));
				resetReceivedDamage();
			}
		}, 5);
	}
}
