package com.rs.game.world.entity.player.controller.impl;

import java.util.List;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.item.Item;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.others.CastleWarBarricade;
import com.rs.game.world.entity.player.Equipment;
import com.rs.game.world.entity.player.Inventory;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.activities.CastleWars;
import com.rs.game.world.entity.player.controller.Controller;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

public class CastleWarsPlaying extends Controller {

	private int team;

	@Override
	public void start() {
		team = (int) getArguments()[0];
		sendInterfaces();
	}

	@Override
	public boolean canMove(int dir) {
		Position toTile = new Position(player.getX()
				+ Utils.DIRECTION_DELTA_X[dir], player.getY()
				+ Utils.DIRECTION_DELTA_Y[dir], player.getZ());
		return !CastleWars.isBarricadeAt(toTile);
	}

	@Override
	public boolean processNPCClick2(NPC n) {
		if (n.getId() == 1532 && n instanceof CastleWarBarricade) {
			if (!player.getInventory().containsItem(590, 1)) {
				player.getPackets().sendGameMessage(
						"You do not have the required items to light this.");
				return false;
			}
			CastleWarBarricade barricade = (CastleWarBarricade) n;
			barricade.litFire();
			return false;
		}
		return true;
	}

	/*
	 * return process normaly
	 */
	@Override
	public boolean processButtonClick(int interfaceId, int componentId,
			int slotId, int packetId) {
		if (interfaceId == 387) {
			if (componentId == 9 || componentId == 6) {
				player.getPackets().sendGameMessage(
						"You can't remove your team's colours.");
				return false;
			}
			if (componentId == 15) {
				int weaponId = player.getEquipment().getWeaponId();
				if (weaponId == 4037 || weaponId == 4039) {
					player.getPackets().sendGameMessage(
							"You can't remove enemy's flag.");
					return false;
				}
			}
		} else if (interfaceId == Inventory.INVENTORY_INTERFACE) {
			Item item = player.getInventory().getItem(slotId);
			if (item != null) {
				if (item.getId() == 4053) {
					CastleWars.addBarricade(team, player);
					return false;
				} else if (item.getId() == 4049 || item.getId() == 4050
						|| item.getId() == 12853 || item.getId() == 14640
						|| item.getId() == 14648) {
					doBandageEffect(item);
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public boolean canDropItem(Item item) {
		if (item.getDefinitions().getName().toLowerCase().contains("flag")) {
			player.getPackets().sendGameMessage(
					"You cannot just drop the flag!");
			return false;
		}
		return true;
	}

	private void doBandageEffect(Item item) {
		int gloves = player.getEquipment().getGlovesId();
		player.heal((int) (player.getMaxHitpoints() * (gloves >= 11079
				&& gloves <= 11084 ? 0.15 : 0.10)));
		int restoredEnergy = (int) (player.getRunEnergy() * 1.3);
		player.setRunEnergy(restoredEnergy > 100 ? 100 : restoredEnergy);
		player.getInventory().deleteItem(item);
	}

	@Override
	public boolean canEquip(int slotId, int itemId) {
		if (slotId == Equipment.SLOT_CAPE || slotId == Equipment.SLOT_HAT) {
			player.getPackets().sendGameMessage(
					"You can't remove your team's colours.");
			return false;
		}
		if (slotId == Equipment.SLOT_WEAPON || slotId == Equipment.SLOT_SHIELD) {
			int weaponId = player.getEquipment().getWeaponId();
			if (weaponId == 4037 || weaponId == 4039) {
				player.getPackets().sendGameMessage(
						"You can't remove enemy's flag.");
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean canAttack(Entity target) {
		if (target instanceof Player) {
			if (canHit(target))
				return true;
			player.getPackets().sendGameMessage("You can't attack your team.");
			return false;
		}
		return true;
	}

	@Override
	public boolean processItemOnNPC(NPC npc, Item item) {
		if (npc.getId() == 1532 && npc instanceof CastleWarBarricade) {
			CastleWarBarricade barricade = (CastleWarBarricade) npc;
			if (item.getId() == 590) {
				barricade.litFire();
				return false;
			} else if (item.getId() == 4045) {
				player.getInventory().deleteItem(item);
				barricade.explode();
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean canHit(Entity target) {
		if (target instanceof NPC)
			return true;
		Player p2 = (Player) target;
		if (p2.getEquipment().getCapeId() == player.getEquipment().getCapeId())
			return false;
		return true;
	}

	// You can't leave just like that!

	public void leave() {
		player.getPackets().closeInterface(
				player.getInterfaceManager().hasRezizableScreen() ? 11 : 0);
		CastleWars.removePlayingPlayer(player, team);
	}

	@Override
	public void sendInterfaces() {
		player.getInterfaceManager().sendTab(
				player.getInterfaceManager().hasRezizableScreen() ? 11 : 0, 58);
	}

	@Override
	public boolean sendDeath() {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.animate(new Animation(836));
				} else if (loop == 1) {
					player.getPackets().sendGameMessage(
							"Oh dear, you have died.");
				} else if (loop == 3) {
					int weaponId = player.getEquipment().getWeaponId();
					if (weaponId == 4037 || weaponId == 4039) {
						CastleWars.setWeapon(player, null);
						CastleWars.dropFlag(player,
								weaponId == 4037 ? CastleWars.SARADOMIN
										: CastleWars.ZAMORAK);
					} else {
						Player killer = player
								.getMostDamageReceivedSourcePlayer();
						if (killer != null)
							killer.increaseKillCount(player);
					}

					player.reset();
					player.setNextPosition(new Position(
							team == CastleWars.ZAMORAK ? CastleWars.ZAMO_BASE
									: CastleWars.SARA_BASE, 1));
					player.animate(new Animation(-1));
				} else if (loop == 4) {
					player.getPackets().sendMusicEffect(90);
					stop();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}

	@Override
	public boolean logout() {
		player.setLocation(new Position(CastleWars.LOBBY, 2));
		return true;
	}

	@Override
	public boolean processMagicTeleport(Position toTile) {
		player.getDialogueManager().startDialogue("SimpleMessage",
				"You can't leave just like that!");
		return false;
	}

	@Override
	public boolean processItemTeleport(Position toTile) {
		player.getDialogueManager().startDialogue("SimpleMessage",
				"You can't leave just like that!");
		return false;
	}

	@Override
	public boolean processObjectTeleport(Position toTile) {
		player.getDialogueManager().startDialogue("SimpleMessage",
				"You can't leave just like that!");
		return false;
	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		int id = object.getId();
		if (id == 4406 || id == 4407) {
			removeControler();
			leave();
			return false;
		} else if ((id == 4469 && team == CastleWars.SARADOMIN)
				|| (id == 4470 && team == CastleWars.ZAMORAK)) {
			passBarrier(object);
			return false;
		} else if (id == 4377 || id == 4378) { // no flag anymore
			if (id == 4377 && team == CastleWars.SARADOMIN) {
				if (player.getEquipment().getWeaponId() == 4039) {
					CastleWars.addScore(player, team, CastleWars.ZAMORAK);
					return false;
				}
			} else if (id == 4378 && team == CastleWars.ZAMORAK) {
				if (player.getEquipment().getWeaponId() == 4037) {
					CastleWars.addScore(player, team, CastleWars.SARADOMIN);
					return false;
				}
			}
			player.getPackets().sendGameMessage(
					"You need to bring a flag back here!");
			return false;
		} else if (id == 4902 || id == 4903) { // take flag
			if (id == 4902 && team == CastleWars.SARADOMIN) {
				if (player.getEquipment().getWeaponId() == 4039) {
					CastleWars.addScore(player, team, CastleWars.ZAMORAK);
					return false;
				}
				player.getPackets().sendGameMessage(
						"Saradomin won't let you take his flag!");
			} else if (id == 4903 && team == CastleWars.ZAMORAK) {
				if (player.getEquipment().getWeaponId() == 4037) {
					CastleWars.addScore(player, team, CastleWars.SARADOMIN);
					return false;
				}
				player.getPackets().sendGameMessage(
						"Zamorak won't let you take his flag!");
			} else {
				// take flag
				CastleWars.takeFlag(player, team,
						id == 4902 ? CastleWars.SARADOMIN : CastleWars.ZAMORAK,
						object, false);
			}
			return false;
		} else if (id == 4900 || id == 4901) { // take dropped flag
			CastleWars.takeFlag(player, team, id == 4900 ? CastleWars.SARADOMIN
					: CastleWars.ZAMORAK, object, true);
			return false;
		} else if (id == 36579 || id == 36586) {
			player.getInventory().addItem(new Item(4049));
			return false;
		} else if (id == 36575 || id == 36582) {
			player.getInventory().addItem(new Item(4053));
			return false;
		} else if (id == 36577 || id == 36584) {
			player.getInventory().addItem(new Item(4045));
			return false;
			// under earth from basess
		} else if (id == 4411) {// stepping stone
			if (object.getX() == player.getX()
					&& object.getY() == player.getY())
				return false;
			player.lock(2);
			player.animate(new Animation(741));
			player.addWalkSteps(object.getX(), object.getY(), -1, false);
		} else if (id == 36693) {
			player.useStairs(827, new Position(2430, 9483, 0), 1, 2);
			return false;
		} else if (id == 36694) {
			player.useStairs(827, new Position(2369, 9524, 0), 1, 2);
			return false;
		} else if (id == 36645) {
			player.useStairs(828, new Position(2430, 3081, 0), 1, 2);
			return false;
		} else if (id == 36646) {
			player.useStairs(828, new Position(2369, 3126, 0), 1, 2);
			return false;
		} else if (id == 4415) {
			if (object.getX() == 2417 && object.getY() == 3075
					&& object.getZ() == 1)
				player.useStairs(-1, new Position(2417, 3078, 0), 0, 1);
			else if (object.getX() == 2419 && object.getY() == 3080
					&& object.getZ() == 1)
				player.useStairs(-1, new Position(2419, 3077, 0), 0, 1);
			else if (object.getX() == 2430 && object.getY() == 3081
					&& object.getZ() == 2)
				player.useStairs(-1, new Position(2427, 3081, 1), 0, 1);
			else if (object.getX() == 2425 && object.getY() == 3074
					&& object.getZ() == 3)
				player.useStairs(-1, new Position(2425, 3077, 2), 0, 1);
			else if (object.getX() == 2380 && object.getY() == 3127
					&& object.getZ() == 1)
				player.useStairs(-1, new Position(2380, 3130, 0), 0, 1);
			else if (object.getX() == 2382 && object.getY() == 3132
					&& object.getZ() == 1)
				player.useStairs(-1, new Position(2382, 3129, 0), 0, 1);
			else if (object.getX() == 2369 && object.getY() == 3126
					&& object.getZ() == 2)
				player.useStairs(-1, new Position(2372, 3126, 1), 0, 1);
			else if (object.getX() == 2374 && object.getY() == 3133
					&& object.getZ() == 3)
				player.useStairs(-1, new Position(2374, 3130, 2), 0, 1);
			return false;
		} else if (id == 36481) {
			player.useStairs(-1, new Position(2417, 3075, 0), 0, 1);
			return false;
		} else if (id == 36495 && object.getZ() == 0) {
			player.useStairs(-1, new Position(2420, 3080, 1), 0, 1);
			return false;
		} else if (id == 36480 && object.getZ() == 1) {
			player.useStairs(-1, new Position(2430, 3080, 2), 0, 1);
			return false;
		} else if (id == 36484 && object.getZ() == 2) {
			player.useStairs(-1, new Position(2426, 3074, 3), 0, 1);
			return false;
		} else if (id == 36532 && object.getZ() == 0) {
			player.useStairs(-1, new Position(2379, 3127, 1), 0, 1);
			return false;
		} else if (id == 36540) {
			player.useStairs(-1, new Position(2383, 3132, 0), 0, 1);
			return false;
		} else if (id == 36521 && object.getZ() == 1) {
			player.useStairs(-1, new Position(2369, 3127, 2), 0, 1);
			return false;
		} else if (id == 36523 && object.getZ() == 2) {
			player.useStairs(-1, new Position(2373, 3133, 3), 0, 1);
			return false;
		} else if (id == 36644) {
			if (object.getY() == 9508)
				player.useStairs(828, new Position(2400, 3106, 0), 1, 2);
			else if (object.getY() == 9499)
				player.useStairs(828, new Position(2399, 3100, 0), 1, 2);
			player.setFreezeDelay(0);
			player.setFrozeBlocked(0);
			return false;
		} else if (id == 36691) {
			if (object.getY() == 3099)
				player.useStairs(827, new Position(2399, 9500, 0), 1, 2);
			else if (object.getY() == 3108)
				player.useStairs(827, new Position(2400, 9507, 0), 1, 2);
			player.setFreezeDelay(0);
			player.setFrozeBlocked(0);
			return false;
		}/*
		 * else if (id == 4438) player.getActionManager().setSkill(new
		 * Mining(object, RockDefinitions.SMALLER_ROCKS)); else if (id == 4437)
		 * player.getActionManager().setSkill(new Mining(object,
		 * RockDefinitions.ROCKS ));
		 */
		else if (id == 4448) {
			for (List<Player> players : CastleWars.getPlaying()) {
				for (Player player : players) {
					if (player.withinDistance(object, 1))
						player.applyHit(new Hit(player, player.getHitpoints(),
								HitLook.REGULAR_DAMAGE));
				}
			}
			World.spawnObject(
					new WorldObject(4437, object.getType(), object
							.getRotation(), object.getX(), object.getY(),
							object.getZ()), true);
		}
		return true;
	}

	@Override
	public boolean processObjectClick2(WorldObject object) {
		int id = object.getId();
		if (id == 36579 || id == 36586) {
			player.getInventory().addItem(new Item(4049, 5));
			return false;
		} else if (id == 36575 || id == 36582) {
			player.getInventory().addItem(new Item(4053, 5));
			return false;
		} else if (id == 36577 || id == 36584) {
			player.getInventory().addItem(new Item(4045, 5));
			return false;
		}
		return true;
	}

	public void passBarrier(WorldObject object) {
		if (object.getRotation() == 0 || object.getRotation() == 2) {
			if (player.getY() != object.getY())
				return;
			player.lock(2);
			player.addWalkSteps(object.getX() == player.getX() ? object.getX()
					+ (object.getRotation() == 0 ? -1 : +1) : object.getX(),
					object.getY(), -1, false);
		} else if (object.getRotation() == 1 || object.getRotation() == 3) {
			if (player.getX() != object.getX())
				return;
			player.lock(2);
			player.addWalkSteps(
					object.getX(),
					object.getY() == player.getY() ? object.getY()
							+ (object.getRotation() == 3 ? -1 : +1) : object
							.getY(), -1, false);
		}
	}

	@Override
	public void magicTeleported(int type) {
		removeControler();
		leave();
	}

	@Override
	public void forceClose() {
		leave();
	}
}
