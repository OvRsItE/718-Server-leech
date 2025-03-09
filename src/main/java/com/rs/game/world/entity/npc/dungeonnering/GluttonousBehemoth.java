package com.rs.game.world.entity.npc.dungeonnering;

import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonManager;
import com.rs.game.world.entity.player.content.skills.dungeoneering.RoomReference;
import com.rs.game.world.entity.updating.impl.Animation;

@SuppressWarnings("serial")
public final class GluttonousBehemoth extends DungeonBoss {

	private WorldObject heal;
	private int ticks;

	public GluttonousBehemoth(int id, Position tile, DungeonManager manager, RoomReference reference) {
		super(id, tile, manager, reference);
		setCantFollowUnderCombat(true);
	}

	public void setHeal(WorldObject food) {
		ticks = 0;
		heal = food;
		removeTarget();
	}

	@Override
	public void processNPC() {
		if (heal != null) {
			setNextFaceEntity(null);
			ticks++;
			if (ticks == 1) {
				//calcFollow(heal, true);    //TODO dungfollow
			} else if (ticks == 5) {
				animate(new Animation(13720));
			} else if (ticks < 900 && ticks > 7) {
				boolean blocked = false;
				Position mid = new Position(((heal.getX() + this.getX()) / 2), ((heal.getY() + this.getY()) / 2), this.getZ());
				for (int i = 0; i < this.getManager().getParty().getTeam().size(); i++) {
					if (this.getManager().getParty().getTeam().get(i).withinDistance(mid)) {
						blocked = true;
						break;
					}
				}
				if (blocked || getHitpoints() >= (getMaxHitpoints() * 0.75)) {
					animate(new Animation(-1));
					//calcDungFollow(getRespawnTile(), 20, true, false);  //TODO dungfollow
					ticks = 995;
					return;
				}
				heal(50 + com.rs.utility.Utils.random(50));
				animate(new Animation(13720));
			} else if (ticks > 1000)
				heal = null;
			return;
		}
		super.processNPC();
	}

}
