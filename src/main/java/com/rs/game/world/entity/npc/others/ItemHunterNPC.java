package com.rs.game.world.entity.npc.others;

import java.util.List;

import com.rs.game.map.OwnedObjectManager;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.map.OwnedObjectManager.ConvertEvent;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.BoxAction.HunterNPC;
import com.rs.game.world.entity.player.content.Hunter;
import com.rs.game.world.entity.player.content.skills.Skills;

@SuppressWarnings("serial")
public class ItemHunterNPC extends NPC {

	public ItemHunterNPC(int id, Position tile, int mapAreaNameHash,
                         boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, 0, canBeAttackFromOutOfArea, spawned);
	}

	@Override
	public void processNPC() {
		super.processNPC();
		List<WorldObject> objects = World.getRegion(getRegionId()).getSpawnedObjects();
		if (objects != null) {
			final HunterNPC info = HunterNPC.forId(getId());
			int objectId = info.getEquipment().getObjectId();
			for (final WorldObject object : objects) {
				if (object.getId() == objectId) {
					if (OwnedObjectManager.convertIntoObject(object,
							new WorldObject(info.getSuccessfulTransformObjectId(), 10, 0,
									this.getX(), this.getY(), this.getZ()),
									new ConvertEvent() {
						@Override
						public boolean canConvert(Player player) {
							if (player == null || player.isLocked())
								return false;
							if (Hunter.isSuccessful(player, info.getLevel(), player1 -> {
								if (player1.getEquipment().wearingSkillCape(Skills.HUNTER))
									return 2;
								return 1;
							})) {
								failedAttempt(object, info);
								return false;
							} else {
								animate(info.getSuccessCatchAnim());
								return true;
							}
						}
					})) {
						setRespawnTask(); // auto finishes
					}
				}
			}
		}
	}

	private void failedAttempt(WorldObject object, HunterNPC info) {
		animate(info.getFailCatchAnim());
		if (OwnedObjectManager.convertIntoObject(object,
				new WorldObject(info.getFailedTransformObjectId(), 10, 0,
						this.getX(), this.getY(), this.getZ()),
						new ConvertEvent() {
			@Override
			public boolean canConvert(Player player) {
				//if(larupia)
				//blablabla
				return true;
			}
		})) {
		}
	}
}