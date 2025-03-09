package com.rs.game.world.entity.npc.dungeonnering;

import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonManager;
import com.rs.game.world.entity.player.content.skills.dungeoneering.RoomReference;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings("serial")
public class LakkTheRiftSplitter extends DungeonBoss {

	private static final int[] RAIN_GRAPHICS =
	{ 2581, 2583, 2585 };

	private List<PortalCluster> clusters;

	public LakkTheRiftSplitter(int id, Position tile, DungeonManager manager, RoomReference reference) {
		super(id, tile, manager, reference);
		clusters = new CopyOnWriteArrayList<PortalCluster>();
	}

	@Override
	public void processNPC() {
		if (isDead() || clusters == null)
			return;
		super.processNPC();
		for (PortalCluster cluster : clusters) {
			cluster.incrementCycle();
			if (cluster.getCycle() == 35) {
				clusters.remove(cluster);
				continue;
			}
			for (Entity t : getPossibleTargets()) {
				Player player = (Player) t;
				if (cluster.getCycle() < 1)
					continue;
				if (cluster.getCycle() % 2 == 0) {
					for (Position tile : cluster.getBoundary()) {
						if (player.getX() == tile.getX() && player.getY() == tile.getY()) {
							cluster.increaseEffectMultipier();
							int type = cluster.getType();
							double effectMultiplier = cluster.getEffectMultiplier();
							int maxHit = getMaxHit();

							if (type == 0)
								player.applyHit(new Hit(this, (int) (com.rs.utility.Utils.random(maxHit * .35, maxHit * .55) * effectMultiplier), Hit.HitLook.REGULAR_DAMAGE));
							//else if (type == 1)   //TODO poison
								
							//	player.getPoison().makePoisoned((int) (com.rs.utility.Utils.random(maxHit * .10, maxHit * .30) * effectMultiplier));
							else {
								int skill = com.rs.utility.Utils.random(6);
								player.getSkills().drainLevel(skill == 3 ? Skills.MAGIC : skill, (int) (com.rs.utility.Utils.random(2, 3) * effectMultiplier));
							}
						}
					}
				}
			}
			if (cluster.getCycle() % 15 == 0)
				submitGraphics(cluster, this);
		}
	}

	@Override
	public void sendDeath(Entity killer) {
		super.sendDeath(killer);
		clusters.clear();
	}

	public void addPortalCluster(int type, Position[] boundary) {
		PortalCluster cluster = new PortalCluster(type, boundary);
		submitGraphics(cluster, this);
		clusters.add(cluster);
	}

	public static void submitGraphics(PortalCluster cluster, NPC creator) {
		for (Position tile : cluster.getBoundary())
			World.sendGraphics(creator, new Graphics((com.rs.utility.Utils.random(3) == 0 ? 1 : 0) + RAIN_GRAPHICS[cluster.getType()]), tile);
	}

	private static class PortalCluster {

		private final int type;
		private final Position[] boundary;
		private int cycle;
		private double effectMultiplier;

		public PortalCluster(int type, Position[] boundary) {
			this.type = type;
			this.boundary = boundary;
			effectMultiplier = 0.5;
		}

		public Position[] getBoundary() {
			return boundary;
		}

		public int getType() {
			return type;
		}

		public void incrementCycle() {
			cycle++;
		}

		public int getCycle() {
			return cycle;
		}

		public double getEffectMultiplier() {
			return effectMultiplier;
		}

		public void increaseEffectMultipier() {
			effectMultiplier += 0.5;
		}
	}

	public boolean doesBoundaryOverlap(List<Position> boundaries) {
		for (PortalCluster cluster : clusters) {
			for (Position tile : cluster.getBoundary()) {
				for (Position boundary : boundaries) {
					if (tile.getX() == boundary.getX() && tile.getY() == boundary.getY())
						return true;
				}
			}
		}
		return false;
	}
}
