package com.rs.game.world.entity.player.content.quests.impl;

import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;

public class HalloweenEvent {
	
	/**
	 * @author Mario (AlterOPS)
	 **/

	static Player player;
	
	public HalloweenEvent(Player player) {
		this.player = player;
	}
	

	public static void startEvent() {
		World.spawnObject(new WorldObject(62621, 10, 1, 3108, 3260, 0), true);
		World.spawnNPC(12377, new Position(3108, 3265, 0), 1, 0, true);
		World.spawnNPC(12378, new Position(4315, 5470, 0), 1, 0, true);
		World.spawnNPC(12392, new Position(4313, 5462, 0), 1, 0, true);
		World.spawnNPC(14398, new Position(3107, 3261, 0), 1, 0, true);
		World.spawnNPC(14398, new Position(3107, 3265, 0), 1, 0, true);
		World.spawnNPC(14398, new Position(3108, 3265, 0), 1, 0, true);
		World.spawnNPC(12379, new Position(4311, 5478, 0), 1, 0, true);
		World.spawnNPC(12375, new Position(4311, 5465, 0), 1, 0, true);
		World.spawnNPC(14400, new Position(4318, 5464, 0), 1, 0, true);
		World.spawnNPC(14389, new Position(4330, 5460, 0), 1, 0, true);
		World.sendWorldMessage("<col=228B22>Halloween event hast started in Draynor! Happy Halloween!", false);
	}


}