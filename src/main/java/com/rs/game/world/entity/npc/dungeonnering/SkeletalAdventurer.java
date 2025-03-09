package com.rs.game.world.entity.npc.dungeonnering;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.item.Item;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.Drop;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonManager;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonUtils;
import com.rs.game.world.entity.player.content.skills.dungeoneering.RoomReference;
import com.rs.game.world.entity.player.content.skills.dungeoneering.journals.Chronicles;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Hit;

import java.util.List;

@SuppressWarnings("serial")
public final class SkeletalAdventurer extends DungeonBoss {

	private int npcId;

	public SkeletalAdventurer(int id, Position tile, DungeonManager manager, RoomReference reference, double multiplier) {
		super(id, tile, manager, reference, multiplier);
		npcId = id;
	}

	@Override
	public void processNPC() {
		if (isDead())
			return;
		super.processNPC();
		if (com.rs.utility.Utils.random(15) == 0)
			setNextNPCTransformation(npcId + com.rs.utility.Utils.random(3));
	}

	@Override
	public void sendDeath(final Entity source) {
		final NPCCombatDefinitions defs = getCombatDefinitions();
		resetWalkSteps();
		getCombat().removeTarget();
		animate(null);
		boolean last = true;
		List<Integer> npcsIndexes = World.getRegion(getRegionId()).getNPCsIndexes();
		if (npcsIndexes != null) {
			for (int npcIndex : npcsIndexes) {
				NPC npc = World.getNPCs().get(npcIndex);
				if (npc == this || npc.isDead() || npc.hasFinished() || !npc.getName().startsWith("Skeletal "))
					continue;
				last = false;
				break;
			}
		}
		final boolean l = last;
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					animate(new Animation(defs.getDeathEmote()));
				} else if (loop >= defs.getDeathDelay()) {
					if (source instanceof Player)
						((Player) source).getControlerManager().processNPCDeath(SkeletalAdventurer.this);
					if (l)
						drop();
					reset();
					finish();
					stop();
				}
				loop++;
			}
		}, 0, 1);
		if (last)
			getManager().openStairs(getReference());
	}

	@Override
	public int getMaxHit() {
		return super.getMaxHit() * 2;
	}

	public int getPrayer1() {
		return this.getId() - npcId;
	}

	@Override
	public void handleIngoingHit(Hit hit) {
		if ((hit.getLook() == Hit.HitLook.MELEE_DAMAGE && getPrayer1() == 0) || (hit.getLook() == Hit.HitLook.RANGE_DAMAGE && getPrayer1() == 1) || (hit.getLook() == Hit.HitLook.MAGIC_DAMAGE && getPrayer1() == 2))
			hit.setDamage(0);
		super.handleIngoingHit(hit);
	}

	@Override
	public void sendDrop(Player player, Drop drop) {
		Item item = new Item(drop.getItemId());
		player.getInventory().addItemDrop(item.getId(), item.getAmount());
		int tier = (item.getId() - 16867) / 2 + 1;
		player.getInventory().addItemDrop(DungeonUtils.getArrows(tier), 125);
		if (com.rs.utility.Utils.random(3) == 1) {
			Chronicles chronicle = Chronicles.BELLEFLEUR;
			dropChronicle(player, chronicle);
		}
		
	}

}
