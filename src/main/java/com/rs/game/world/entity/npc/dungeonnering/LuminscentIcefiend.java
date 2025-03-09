package com.rs.game.world.entity.npc.dungeonnering;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.TemporaryAttributes;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonManager;
import com.rs.game.world.entity.player.content.skills.dungeoneering.RoomReference;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.NewForceMovement;
import com.rs.utility.Utils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class LuminscentIcefiend extends DungeonBoss {

	private static final long serialVersionUID = -624907500327825702L;
	private static final byte FIRST_STAGE = 3;
	private static final Graphics ICE_SHARDS = new Graphics(2525);
	private static final Animation KNOCKBACK = new Animation(10070);

	private List<Position> icicles;

	private int specialStage;
	private boolean specialEnabled;

	public LuminscentIcefiend(int id, Position tile, DungeonManager manager, RoomReference reference) {
		super(id, tile, manager, reference);
		specialStage = FIRST_STAGE;
		icicles = new LinkedList<Position>();
	}

	@Override
	public void processNPC() {
		super.processNPC();

		int max_hp = getMaxHitpoints();
		int current_hp = getHitpoints();

		if (current_hp < max_hp * (.25 * specialStage) && !specialEnabled)//75, 50, 25
			prepareSpecial();
	}

	private void prepareSpecial() {
		setNextFaceEntity(null);
		setCapDamage(0);
		specialEnabled = true;
		specialStage--;
	}

	@Override
	public void handleIngoingHit(Hit hit) {
		int current_hp = getHitpoints();
		if (hit.getDamage() >= current_hp && specialStage == 0 && !specialEnabled) {
			hit.setDamage(current_hp - 1);
			prepareSpecial();
		}
		super.handleIngoingHit(hit);
	}

	public boolean isSpecialEnabled() {
		return specialEnabled;
	}

	public void commenceSpecial() {
		specialEnabled = false;

		final NPC icefiend = this;
		WorldTasksManager.schedule(new WorldTask() {

			int count = 0;

			@Override
			public void run() {
				if (count == 21 || isDead()) {
					stop();
					icicles.clear();
					setCapDamage(-1);
					for (Player player : getManager().getParty().getTeam()) {
						player.setCantWalk(false);
						player.getTemporaryAttributtes().remove(TemporaryAttributes.Key.FIEND_FLAGGED);
					}
					return;
				}
				count++;

				if (count < 5) {
					if (count == 1) {
						for (Entity t : getPossibleTargets()) {
							Player player = (Player) t;
							player.getPackets().sendGameMessage("The luminescent ice fiend is encased in ice and cannot be harmed!");
						}
					}
					return;
				}

				for (Entity t : getPossibleTargets()) {
					Player player = (Player) t;
					boolean flagEnabled = player.getTemporaryAttributtes().get(TemporaryAttributes.Key.FIEND_FLAGGED) != null;
					if (player == null || player.isDead() || player.hasFinished())
						continue;

					Position currentTile = flagEnabled ? new Position(player) : player.getLastPosition();
					tileLoop: for (int i = 0; i < icicles.size(); i++) {
						Position tile = icicles.remove(i);
						player.getPackets().sendGraphics(ICE_SHARDS, tile);
						if (flagEnabled || player.getX() != tile.getX() || player.getY() != tile.getY())
							continue tileLoop;
						player.getTemporaryAttributtes().put(TemporaryAttributes.Key.FIEND_FLAGGED, true);
					}
					icicles.add(currentTile);
				}

				if (count < 5)
					return;

				for (Iterator<Position> it = icicles.iterator(); it.hasNext();) {
					Position tile = it.next();

					entityLoop: for (Entity t : getPossibleTargets()) {
						Player player = (Player) t;
						if (player.getTemporaryAttributtes().get(TemporaryAttributes.Key.FIEND_FLAGGED) == null)
							continue entityLoop;

						Position nextTile = Utils.getFreeTile(player, 1);

						if (!player.isCantWalk())
							player.setCantWalk(true);
						if (player.getActionManager().getAction() != null)
							player.getActionManager().forceStop();
						player.animate(KNOCKBACK);
						player.setNextPosition(nextTile);
						player.setNextForceMovement(new NewForceMovement(tile, 0, nextTile, 1, com.rs.utility.Utils.getAngle(tile.getX() - nextTile.getX(), tile.getY() - nextTile.getY())));
						int damageCap = (int) (player.getMaxHitpoints() * .10);
						if (player.getHitpoints() < damageCap)// If has 10% of HP.
							continue;
						int damage = com.rs.utility.Utils.random(20, 100);
						if (player.getHitpoints() - damage <= damageCap)
							damage = damageCap;
						player.applyHit(new Hit(icefiend, damage, Hit.HitLook.REGULAR_DAMAGE));
					}
				}
			}
		}, 0, 0);
	}
}
