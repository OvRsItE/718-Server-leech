package com.rs.game.world.entity.npc.dungeonnering;

import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonManager;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.utility.Utils;

@SuppressWarnings("serial")
public class FamishedEye extends DungeonNPC {

	private int weakness, type, sleepCycles;
	private boolean firstHit;
	private WorldGorgerShukarhazh boss;
	
	public FamishedEye(final WorldGorgerShukarhazh boss, int id, Position tile, final DungeonManager manager, double multiplier) {
		super(id, tile, manager, multiplier);
		this.boss = boss;
		this.sleepCycles = -1;
		int rotationType = (id - 12436) / 15;
		weakness = findWeakness(rotationType);
		type = findType(rotationType);
		setCantFollowUnderCombat(true);
		setForceAgressive(true);
		//setCantSetTargetAutoRelatio(true);
		int rotation = manager.getRoom(manager.getCurrentRoomReference(this)).getRotation() + rotationType;
		setDirection(com.rs.utility.Utils.getAngle(Utils.ROTATION_DIR_X[(rotation + 1) & 0x3], Utils.ROTATION_DIR_Y[(rotation + 1) & 0x3]));
	}

	@Override
	public boolean clipedProjectile(Position tile, boolean checkClose, int size) {
		//because npc is under cliped data
		return getManager().isAtBossRoom(tile);
	}

	@Override
	public void setNextFaceEntity(Entity entity) {
		//this boss doesnt face
	}

	@Override
	public void handleIngoingHit(Hit hit) {
		if (weakness != hit.getLook().getMark())
			hit.setDamage((int) (hit.getDamage() * 0.2));
		super.handleIngoingHit(hit);
	}

	@Override
	public void processNPC() {
		super.processNPC();
		if (sleepCycles == 45) {
			resetSleepCycles();
		} else if (sleepCycles >= 0)
			sleepCycles++;
	}

	private void resetSleepCycles() {
		sleepCycles = -1;
		setCantInteract(false);
		animate(new Animation(-1));
		setHitpoints(getMaxHitpoints());
		boss.refreshCapDamage();
		for (Entity t : getPossibleTargets())
			if (t instanceof Player)
				((Player) t).getPackets().sendGameMessage("The creature shifts, and one of it's many eyes opens.");
	}

	@Override
	public void drop() {

	}

	@Override
	public void sendDeath(Entity source) {
		sleepCycles++;//start the cycles
		boss.refreshCapDamage();
		setCantInteract(true);
		animate(new Animation(14917));
		for (Entity t : getPossibleTargets())
			if (t instanceof Player)
				((Player) t).getPackets().sendGameMessage("The creature shifts, and one of it's many eyes closes.");
	}

	public boolean isInactive() {
		return sleepCycles != -1;
	}

	public WorldGorgerShukarhazh getBoss() {
		return boss;
	}
	
	public int getType() {
		return type;
	}

	public int getWeaknessStyle() {
		return weakness;
	}

	private int findType(int rotation) {
		switch (type) {
		case 0: //mage
			return 2;
		case 1: //warrior
			return 0;
		default: //range
			return 1;
		}
	}
	
	private int findWeakness(int type) {
		switch (type) {
		case 0: //mage
			return 1; //range was mage 2
		case 1: //warrior
			return 2; //mage was melee 0
		default: //range
			return 0; //melee was range 1
		}
	}

	public boolean isFirstHit() {
		return firstHit;
	}

	public void setFirstHit(boolean firstHit) {
		this.firstHit = firstHit;
	}
}
