package com.rs.game.world.entity.npc.slayer;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.utility.Utils;

@SuppressWarnings("serial")
public class Strykewyrm extends NPC {
    
	private int stompId;
	private boolean hasEmerged;
	boolean changing = false;
	public Strykewyrm(int id, Position tile, int mapAreaNameHash,
                      boolean canBeAttackFromOutOfArea) {
		super(id, tile, mapAreaNameHash, 0, canBeAttackFromOutOfArea, true);
		stompId = id;
	}

	@Override
	public void processNPC() {
		
		super.processNPC();
		if (isDead())
			return;
		if (hasEmerged) {
			if (!isUnderCombat()) {
				if (isCantInteract())
					return;
				getAsNPC().animate(new Animation(12796));
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						reset();
					}
				});
			}
		}

	}

	/*	if (getId() != stompId && !isCantInteract() && !isUnderCombat() && !changing) {
			setNextAnimation(new Animation(12796));
			setCantInteract(true);
			changing = true;
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					
					transformIntoNPC(9462);
					transformIntoNPC(9464);
					transformIntoNPC(9466);
					setCantInteract(false);
				}
			});
		}*/
	
	@Override
	public void sendDeath(Entity source) {
		final NPCCombatDefinitions defs = getCombatDefinitions();
		resetWalkSteps();
		getCombat().removeTarget();
		//setNextAnimation(null);
		//shieldTimer = 0;
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					animate(new Animation(defs.getDeathEmote()));
				} else if (loop >= defs.getDeathDelay()) {	
					drop();
					reset();
					setLocation(getSpawnPosition());
					finish();
					setRespawnTask();
					stop();
					
				}
				loop++;
			}
		}, 0, 1);
	}
	@Override
	public void reset() {
		if (hasEmerged)
			setNextNPCTransformation(getId() - 1);
		hasEmerged = false;
		setCantInteract(false);
		super.reset();
	}

	public static void handleStomping(final Player player, final NPC npc) {
		boolean isStomping = false;
		if (npc.isCantInteract() || ((Strykewyrm) npc).hasEmerged)
			return;
		if (isStomping) {
			return;
		}
		
		if (!npc.isAtMultiArea() || !player.isAtMultiArea()) {
			if (player.getAttackedBy() != npc
					&& player.getAttackedByDelay() > Utils.currentTimeMillis()) {
				player.getPackets().sendGameMessage(
						"You are already in combat.");
				return;
			}
			if (npc.getAttackedBy() != player
					&& npc.getAttackedByDelay() > Utils.currentTimeMillis()) {
				if (npc.getAttackedBy() instanceof NPC) {
					npc.setAttackedBy(player); // changes enemy to player,
					// player has priority over
					// npc on single areas
				} else {
					player.getPackets().sendGameMessage(
							"That npc is already in combat.");
					return;
				}
			}
		}
      
		player.animate(new Animation(4278));
		npc.setCantInteract(true);
		WorldTasksManager.schedule(new WorldTask() {
			//boolean stomping;
			int ticks;
			@Override
			public void run() {

				ticks++;
				if (ticks == 2) {
					npc.animate(new Animation(12795));
					npc.setNextNPCTransformation(npc.getId() + 1);
				} else if (ticks == 4) {
					((Strykewyrm) npc).setEmerged(true);
					npc.getCombat().setTarget(player);
					npc.setCantInteract(false);
					stop();
					return;
				}


				
				
			}

		}, 1, 2);
	}

	public void setEmerged(boolean hasEmerged) {
		this.hasEmerged = hasEmerged;
	}

	public boolean hasEmerged() {
		return hasEmerged;
	}
}
