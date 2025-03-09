package com.rs.game.world.entity.npc.combat.impl;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.cutscenes.impl.ChargeScene;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;


public class RammerNaut extends CombatScript {


	@Override
	public Object[] getKeys() {
		return new Object[] { 9780 };
	}

	final int basicAttack = 13706;
	final int walkAnim = 13699;
	final int missCharge = 13707;
	final int chargeAnim = 13704;
	final int spinAnim = 13705;



	public Position[] CHARGE_PATHS = {
			new Position(2039, 5726, 0), // north
			new Position(2045, 5719, 0), // east,
			new Position(2039, 5713, 0), // south
			new Position(2033, 5719, 0),
	}; // west

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		final Player player = (Player) target;
		npc.setForceFollowClose(true);
		boolean haveSpinned = false;
		boolean isCharging = false;


		if (Utils.random(5) == 0 && !isCharging) {
			isCharging = true;
			handleCharge(npc, target);
		}
		if (!isDistant(npc, target) && !isCharging) {
			if (Utils.getRandom(3) == 3 && haveSpinned == false) {
				haveSpinned = true;
				isCharging = false;
				npc.animate(new Animation(spinAnim));
				npc.getCombat().setCombatDelay(5);
				target.sm("The rammernaut starts to swing hes hammer..");
				WorldTasksManager.schedule(new WorldTask() {

					int loop;
					@Override
					public void run() {
						if (loop == 2) {
							if (!isDistant(npc, target)) {
								target.applyHit(
										new Hit(target, Utils.random(100, 250), HitLook.MELEE_DAMAGE));
								target.applyHit(
										new Hit(target, Utils.random(100, 250), HitLook.MELEE_DAMAGE));
								target.sm("You did not walk away in time..");
								npc.setRun(false);

							} else {
								npc.setRun(false);
								target.sm("You succesfully dodged the swing.");
							}

						}
						loop++;
					}
				},0, 0);
			} else if (Utils.getRandom(10) >= 1){
				isCharging = false;
				haveSpinned = false;
				npc.animate(new Animation(basicAttack));
				delayHit(npc, 1,target, getMeleeHit(npc,getRandomMaxHit(npc, 360,NPCCombatDefinitions.MELEE, target)));
			}
		}
		return defs.getAttackDelay();


	}

	public void handleCharge(final NPC npc,final Entity target) {
		final Player player = (Player) target;
		npc.setForceFollowClose(false);
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		final int x = player.getX();
		final int y = player.getY();

		final int miss = 13707;
		final int charge = 13704;
		npc.setCantInteract(true);
		npc.cancelFaceEntityNoCheck();


		target.sm("Rammernaut gets ready to charge at you!");
		specialAttack(npc, target);




	}


	public void specialAttack(final NPC npc, final Entity target) {
		final Player player = (Player) target;
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		final int x = player.getX();
		final int y = player.getY();
		final int nX = npc.getX();
		final int nY = npc.getY();
		final int miss = 13707;
		final int idx = Utils.random(CHARGE_PATHS.length);
		final Position dir = CHARGE_PATHS[idx];
		final Position center = new Position(2924, 5202, 0);
		npc.setCantInteract(true);
		final Position tile = new Position(npc);
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 0) {
					if(Utils.getDistance(npc, target) < 30) {
						if (player instanceof Player) {
							player.getCutscenesManager().play(
									new ChargeScene(tile, idx));

							//player.setNextForceMovement(new ForceMovement(
									// player, 1, idx == 3 ? 3 : idx == 2 ? 2 : idx == 1 ? 1 : 0));
						}
						npc.animate(new Animation(13699));
						player.sm("The rammernaut is preparing for a charge!");
						npc.getCombat().addCombatDelay(5);
						npc.setNextFacePosition(tile);
						npc.setRun(false);
						npc.addWalkSteps(2039, 5719);
					}
				}
				if (loop == 4) {
					npc.setNextForceTalk(new ForceTalk("CHAAARGEE!"));

					npc.setRun(true);
					npc.setCantInteract(false);
					npc.addWalkSteps(player.getX(), player.getY());


				}
				if (loop >= 5) {
					if (Utils.colides(npc.getX(), npc.getY(), npc.getSize(), player.getX(), player.getY(), player.getSize())) {
						npc.animate(new Animation(miss));
					}
				}

				npc.setRun(true);
				loop++;
			}
		},0, 3);

	}



	public boolean isDistant(NPC npc, Entity target) {
		int size = npc.getSize();
		int dX = target.getX() - npc.getX();
		int dY = target.getY() - npc.getY();
		if (dX > size || dX < -1 || dY > size
				|| dY < -1) {
			return true;

		}
		return false;
	}

}
