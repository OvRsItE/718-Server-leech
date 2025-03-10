package com.rs.game.world.entity.npc.dungeonnering;

import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonManager;
import com.rs.game.world.entity.player.content.skills.dungeoneering.RoomReference;
import com.rs.game.world.entity.updating.impl.Graphics;

import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings("serial")
public class BalLakThePummeler extends DungeonBoss {

	private boolean skip;
	private int barPercentage;

	private List<PoisionPuddle> puddles = new CopyOnWriteArrayList<PoisionPuddle>();

	public BalLakThePummeler(int id, Position tile, DungeonManager manager, RoomReference reference) {
		super(id, tile, manager, reference);
		setLureDelay(6000); //this way you can lure him hehe, still not as much as outside dung npcs
	}

	@Override
	public void processNPC() {
		if (isDead())
			return;
		super.processNPC();
		skip = !skip;
		if (!skip) {
			boolean reduced = false;
			for (PoisionPuddle puddle : puddles) {
				puddle.cycles++;
				if (puddle.canDestroyPoision()) {
					puddles.remove(puddle);
					continue;
				}
				List<Entity> targets = getPossibleTargets(true, true);
				if (com.rs.utility.Utils.colides(getX(), getY(), getSize(), puddle.tile.getX(), puddle.tile.getY(), 1)) {
					barPercentage = barPercentage > 1 ? barPercentage - 2 : 0;
					sendDefenceBar();
					reduced = true;
				}
				for (Entity t : targets) {
					if (!t.withinDistance(puddle.tile, 2))
						continue;
					t.applyHit(new Hit(this, (int) com.rs.utility.Utils.random((int) (t.getHitpoints() * 0.25)) + 1, HitLook.REGULAR_DAMAGE));
				}
			}
			if (!reduced) {
				if (!isUnderCombat()) {
					if (barPercentage > 0) {
						barPercentage--;
						sendDefenceBar();
					}
				} else {
					if (barPercentage < 100) {
						barPercentage++;
						sendDefenceBar();
					}
				}
			}
		}
	}

	@Override
	public void handleIngoingHit(Hit hit) {
		int damage = hit.getDamage();
		HitLook look = hit.getLook();
		if (damage > 0) {
			if (look == HitLook.MELEE_DAMAGE || look == HitLook.RANGE_DAMAGE || look == HitLook.MAGIC_DAMAGE) {
				double multiplier = (100D - ((double) barPercentage)) / 100D;
				hit.setDamage((int) (damage * multiplier));
			}
		}
		super.handleIngoingHit(hit);
	}

	@Override
	public void sendDeath(Entity source) {
		super.sendDeath(source);
		puddles.clear();
		sendDefenceBar();
	}

	private void sendDefenceBar() {
		if (isDead())
			getManager().hideBar(getReference());
		else
			getManager().showBar(getReference(), "Demon's Defence", barPercentage);
	}

	private static class PoisionPuddle {
		final Position tile;
		int cycles;

		public PoisionPuddle(Position tile, int barPercentage) {
			this.tile = tile;
		}

		public boolean canDestroyPoision() {
			return cycles == 15;
		}
	}

	public void addPoisionBubble(Position tile) {
		puddles.add(new PoisionPuddle(tile, barPercentage));
		World.sendGraphics(this, new Graphics(2588), tile);
	}

	public List<PoisionPuddle> getPoisionPuddles() {
		return puddles;
	}
}
