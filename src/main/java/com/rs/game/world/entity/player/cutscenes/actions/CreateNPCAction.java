package com.rs.game.world.entity.player.cutscenes.actions;

import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.cutscenes.Cutscene;

public class CreateNPCAction extends CutsceneAction {

	private int id, x, y, plane;

	public CreateNPCAction(int cachedObjectIndex, int id, int x, int y,
			int plane, int actionDelay) {
		super(cachedObjectIndex, actionDelay);
		this.id = id;
		this.x = x;
		this.y = y;
		this.plane = plane;
	}

	@Override
	public void process(Player player, Object[] cache) {
		Cutscene scene = (Cutscene) cache[0];
		if (cache[getCachedObjectIndex()] != null)
			scene.destroyCache(cache[getCachedObjectIndex()]);
		NPC npc = (NPC) (cache[getCachedObjectIndex()] = World
				.spawnNPC(id,
						new Position(scene.getBaseX() + x, scene.getBaseY()
								+ y, plane), -1, 0, true, true));
		npc.setRandomWalk(false);
	}

}
