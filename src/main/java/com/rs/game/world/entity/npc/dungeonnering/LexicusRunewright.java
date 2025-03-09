package com.rs.game.world.entity.npc.dungeonnering;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonManager;
import com.rs.game.world.entity.player.content.skills.dungeoneering.RoomReference;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.utility.Utils;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings("serial")
public class LexicusRunewright extends DungeonBoss {

	private static final int[] TELEPORT_LOCS =
	{ 8, 7, 3, 3, 3, 12, 12, 12, 12, 3 };

	private boolean completedFirstAttack;
	private int attackStage;
	private List<TombOfLexicus> books = new CopyOnWriteArrayList<TombOfLexicus>();

	public LexicusRunewright(int id, Position tile, DungeonManager manager, RoomReference reference) {
		super(id, tile, manager, reference);
	}

	@Override
	public void handleIngoingHit(Hit hit) {
		int damage = hit.getDamage();
		if (damage > 0) {
			if (hit.getLook() == Hit.HitLook.MELEE_DAMAGE)
				hit.getSource().applyHit(new Hit(this, (int) (damage * .33), Hit.HitLook.REFLECTED_DAMAGE));
		}
		super.handleIngoingHit(hit);
	}

	@Override
	public void sendDeath(Entity source) {
		super.sendDeath(source);
		for (TombOfLexicus book : books)
			book.sendDeath(book);
	}

	public void sendTeleport() {
		setCantInteract(true);
		animate(new Animation(13499));
		setNextGraphics(new Graphics(1576));
		WorldTasksManager.schedule(new WorldTask() {

			int cycles = 0;

			@Override
			public void run() {
				cycles++;
				if (cycles == 2) {
					int random = com.rs.utility.Utils.random(TELEPORT_LOCS.length);
					if (random % 1 == 0 && random != 0)
						random -= 1;
					setNextPosition(Utils.getFreeTile(getManager().getTile(getReference(), TELEPORT_LOCS[random], TELEPORT_LOCS[random + 1]), 2));
					animate(new Animation(13500));
					setNextGraphics(new Graphics(1577));
				} else if (cycles == 4) {
					setCantInteract(false);
					getCombat().removeTarget();
				}
			}
		}, 0, 0);
	}

	public boolean sendAlmanacArmyAttack(final Entity target) {
		final LexicusRunewright boss = this;
		boss.setNextForceTalk(new ForceTalk("Almanac Army, attack!"));
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				for (int id = 0; id < 2; id++) {
					if (reachedMaxBookSize())
						break;
					Position tile = getManager().getTile(getReference(), 6 + com.rs.utility.Utils.random(4), 6 + com.rs.utility.Utils.random(4));
					TombOfLexicus book = new TombOfLexicus(boss, 9856 + com.rs.utility.Utils.random(3), tile, getManager(), 0.5D);
					book.setTarget(target);
					books.add(book);
				}
			}
		}, 2);
		return true;
	}

	public void removeBook(TombOfLexicus book) {
		books.remove(book);
	}

	private boolean reachedMaxBookSize() {
		int size = getManager().getParty().getTeam().size();
		return books.size() >= (size > 3 ? 4 : size) * 3;
	}

	public boolean hasCompletedFirstAttack() {
		return completedFirstAttack;
	}

	public void setCompletedFirstAttack(boolean firstAttack) {
		this.completedFirstAttack = firstAttack;
	}

	public int getAttackStage() {
		return attackStage;
	}

	public void resetAttackStage() {
		attackStage = reachedMaxBookSize() ? 1 : 0;
	}

	public void incrementAttackStage() {
		attackStage++;
	}
}
