package com.rs.game.world.entity.npc.dungeonnering;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.item.Item;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonConstants;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonManager;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonUtils;
import com.rs.game.world.entity.player.content.skills.dungeoneering.RoomReference;
import com.rs.game.world.entity.player.content.skills.dungeoneering.journals.Chronicles;
import com.rs.game.world.entity.player.controller.impl.Wilderness;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;

import java.util.ArrayList;
import java.util.List;



@SuppressWarnings("serial")
public class DungeonNPC extends NPC {

	private DungeonManager manager;
	private int[] bonuses;
	private boolean marked;
	private double multiplier;

	public DungeonNPC(int id, Position tile, DungeonManager manager, double multiplier) {
		super(id, tile, -1, true, true);
		setManager(manager);
		setMultiplier(multiplier);
		if (getDefinitions().hasAttackOption()) {
			int level = manager.getTargetLevel(id, this instanceof DungeonBoss || this instanceof DungeonSkeletonBoss, multiplier);
			setCombatLevel(level);
			setHitpoints(getMaxHitpoints());
			resetBonuses();
			setForceMultiArea(true);
		}
		setForceTargetDistance(20); //includes whole room
	}

	public void resetBonuses() {
		bonuses = manager.getBonuses(this instanceof DungeonBoss, getCombatLevel());
		//setBonuses(bonuses);
	}

	/*
	 * they dont respawn anyway, and this way stomp will be fine
	 */
	@Override
	public int getRespawnDirection() {
		return getDirection();
	}
	
	private static final String[] HOOD_NPCS = new String[] { "zombie", "skeleton", "giant skeleton", "hill giant", "forgotten ranger", "forgotten warrior" };
	public static final String[] HOOD_DISABLE_NPCS = new String[] { "forgotten mage", "necromancer", "reborn mage", "mercenary leader" };
	
	@Override
	public boolean checkAgressivity() {
		if (!isForceAgressive()) {
			NPCCombatDefinitions defs = getCombatDefinitions();
			if (defs.getAgressivenessType() == NPCCombatDefinitions.PASSIVE)
				return false;
		}
		ArrayList<Entity> possibleTarget = getPossibleTargets();
		if (!possibleTarget.isEmpty()) {
			Entity target = possibleTarget.get(com.rs.utility.Utils.random(possibleTarget.size()));
			if (target instanceof Player) {
				Player player = (Player) target;
				int aggressionLevel = (getCombatLevel() * 2) + 1;
				if (player.getControlerManager().getControler() != null || isForceAgressive()) {
					if (player.getEquipment().getHatId() == 17279 || player.getEquipment().getHatId() == 15828) {
						for (String npc : HOOD_NPCS) {
							if (this.getName().toLowerCase().equals(npc) && player.silkTime < com.rs.utility.Utils.currentTimeMillis())
								return false;
						}
						for (String npcs : HOOD_DISABLE_NPCS) {
							if (this.getName().toLowerCase().equals(npcs) && player.silkTime < com.rs.utility.Utils.currentTimeMillis()) {
								player.silkTime = com.rs.utility.Utils.currentTimeMillis() + (30 * 1000);
								player.sendMessage("You've been exposed by " + this.getName() + ".");
								//this.setCannotMove(true);
								setFindTargetDelay(com.rs.utility.Utils.currentTimeMillis() + 5000);
								WorldTasksManager.schedule(new WorldTask() {
									int loop;

									@Override
									public void run() {
										if (loop == 0) {
											faceEntity(player);
											setNextGraphics(new Graphics(1059));
											animate(new Animation(6293));
										} else if (loop == 1) {
											player.setNextGraphics(new Graphics(736));
											//setCannotMove(false);
											this.stop();
										}
										loop++;
									}
								}, 0, 1);
								return false;
							}
						}
					}
					setTarget(target);
					target.setAttackedBy(target);
					target.setFindTargetDelay(com.rs.utility.Utils.currentTimeMillis() + 5000);
					return true;
				}
				if (player.getSkills().getCombatLevel() <= aggressionLevel) {
					/*if ((System.currentTimeMillis() - player.toleranceTimer) < Utils.random(600000, 700000)) {
						setTarget(target);
						target.setAttackedBy(target);
						target.setFindTargetDelay(Utils.currentTimeMillis() + 5000);
						return true;
					}*/
					return false;
				}
				return false;
			}
			return false;
		}
		return false;
	}
	
	@Override
	public ArrayList<Entity> getPossibleTargets(boolean checkNPCs, boolean checkPlayers) {
		int size = getSize();
		int agroRatio = 1;
		ArrayList<Entity> possibleTarget = new ArrayList<Entity>();
		for (int regionId : getMapRegionsIds()) {
			if (checkPlayers) {
				List<Integer> playerIndexes = World.getRegion(regionId).getPlayerIndexes();
				if (playerIndexes != null) {
					for (int playerIndex : playerIndexes) {
						Player player = World.getPlayers().get(playerIndex);
						if (this.getManager() == null)
							continue;
						if (this.getManager().getCurrentRoomReference(this) == null)
							continue;
						if (player == null)
							continue;
						if (this.getManager().getCurrentRoomReference(player) == null)
							continue;
						if (!this.getManager().getCurrentRoomReference(this).equals(this.getManager().getCurrentRoomReference(player)) || player == null || player.isDead() || player.hasFinished() || !player.isRunning() || player.getAppearence().isHidden() || !com.rs.utility.Utils.isOnRange(getX(), getY(), size, player.getX(), player.getY(), player.getSize(), getForceTargetDistance() > 0 ? getForceTargetDistance() : agroRatio) || (!isForceMultiAttacked() && (!isAtMultiArea() || !player.isAtMultiArea()) && (player.getAttackedBy() != this && (player.getAttackedByDelay() > com.rs.utility.Utils.currentTimeMillis() || player.getFindTargetDelay() > com.rs.utility.Utils.currentTimeMillis()))) || !clipedProjectile(player, false) || (!isForceAgressive() && !Wilderness.isAtWild(this) && player.getSkills().getCombatLevelWithSummoning() >= getCombatLevel() * 2))
							continue;
						possibleTarget.add(player);
					}
				}
			}
		}
		
		return possibleTarget;
	}

	public NPC getNPC(int id) {
		List<Integer> npcsIndexes = World.getRegion(getRegionId()).getNPCsIndexes();
		if (npcsIndexes != null) {
			for (int npcIndex : npcsIndexes) {
				NPC npc = World.getNPCs().get(npcIndex);
				if (npc.getId() == id) {
					return npc;
				}
			}
		}
		return null;
	}

	@Override
	public void sendDeath(Entity source) {
		super.sendDeath(source);
		if (marked) {
			getManager().removeMark();
			marked = false;
		}
	}

	@Override
	public void processNPC() {
		super.processNPC();
		if (isUnderCombat()) {
			Entity target = getCombat().getTarget();
			RoomReference thisR = getManager().getCurrentRoomReference(this);
			RoomReference targetR = getManager().getCurrentRoomReference(target);
			if (!targetR.equals(thisR))
				getCombat().removeTarget();
		}
	}

	@Override
	public int getMaxHitpoints() {
		return getCombatLevel() * (this instanceof DungeonBoss ? 25 : 8) / 10 + 1 ;
	}

	public int getMaxHit() {
		return getCombatLevel();
	}



	@Override
	public int[] getBonuses() {
		return bonuses == null ? super.getBonuses() : bonuses;
	}

	public void setMarked(boolean marked) {
		this.marked = marked;
	}
	
	public boolean isMarked() {
		return marked;
	}

	private int getBones() {
		return getName().toLowerCase().contains("dragon") ? 536 : getSize() > 1 ? 532 : 526;
	}

	@Override
	public void drop() {
		int size = getSize();
		ArrayList<Item> drops = new ArrayList<Item>();
		if (getId() != 10831 && getId() != 10821) //nature & ghost
			drops.add(new Item(getBones()));
		for (int i = 0; i < 1 + com.rs.utility.Utils.random(10); i++)
			drops.add(new Item(DungeonUtils.getFood(1 + com.rs.utility.Utils.random(8))));

		if (com.rs.utility.Utils.random(10) == 0)
			drops.add(new Item(DungeonUtils.getDagger(1 + com.rs.utility.Utils.random(5))));

		if (com.rs.utility.Utils.random(5) == 0)
			drops.add(new Item(DungeonConstants.RUNES[com.rs.utility.Utils.random(DungeonConstants.RUNES.length)], 90 + com.rs.utility.Utils.random(30)));

		if (getManager().getParty().getComplexity() >= 5 && com.rs.utility.Utils.random(5) == 0) //torm bag, 1
			drops.add(new Item(DungeonUtils.getTornBag(1 + com.rs.utility.Utils.random(10))));

		if (getManager().getParty().getComplexity() >= 3 && com.rs.utility.Utils.random(5) == 0) //ore, up to 10
			drops.add(new Item(DungeonUtils.getOre(1 + com.rs.utility.Utils.random(5)), 1 + com.rs.utility.Utils.random(10)));

		if (getManager().getParty().getComplexity() >= 2 && com.rs.utility.Utils.random(5) == 0) //branche, up to 10
			drops.add(new Item(DungeonUtils.getBranche(1 + com.rs.utility.Utils.random(5)), 1 + com.rs.utility.Utils.random(10)));

		if (getManager().getParty().getComplexity() >= 4 && com.rs.utility.Utils.random(5) == 0) //textile, up to 10
			drops.add(new Item(DungeonUtils.getTextile(1 + com.rs.utility.Utils.random(10)), 1 + com.rs.utility.Utils.random(10)));

		if (getManager().getParty().getComplexity() >= 5 && com.rs.utility.Utils.random(5) == 0) //herb, up to 10
			drops.add(new Item(DungeonUtils.getHerb(1 + com.rs.utility.Utils.random(9)), 1 + com.rs.utility.Utils.random(10)));

		if (getManager().getParty().getComplexity() >= 5 && com.rs.utility.Utils.random(5) == 0) //seed, up to 10
			drops.add(new Item(DungeonUtils.getSeed(1 + com.rs.utility.Utils.random(12)), 1 + com.rs.utility.Utils.random(10)));

		if (getManager().getParty().getComplexity() >= 5 && com.rs.utility.Utils.random(3) == 0) //charms, depending in mob size
			drops.add(new Item(DungeonConstants.CHARMS[com.rs.utility.Utils.random(DungeonConstants.CHARMS.length)], size));

		if (getManager().getParty().getComplexity() >= 2) //coins, 1000 up to 11000
			drops.add(new Item(DungeonConstants.RUSTY_COINS, 1000 + com.rs.utility.Utils.random(10001)));

		if (getManager().getParty().getComplexity() >= 3 && com.rs.utility.Utils.random(5) == 0) //essence, 10 up to 300
			drops.add(new Item(DungeonConstants.RUNE_ESSENCE, 10 + com.rs.utility.Utils.random(300)));
		if (getManager().getParty().getComplexity() >= 2 && com.rs.utility.Utils.random(5) == 0) //feather, 10 up to 300
			drops.add(new Item(DungeonConstants.FEATHER, 10 + com.rs.utility.Utils.random(300)));
		if ((getManager().getParty().getComplexity() >= 5 && com.rs.utility.Utils.random(10) == 0)) //vial, 1
			drops.add(new Item(17490));
		if ((com.rs.utility.Utils.random(10) == 0)) //anti dragon shield
			drops.add(new Item(16933));
		if ((getManager().getParty().getComplexity() >= 4 && com.rs.utility.Utils.random(10) == 0)) //bowstring, 1
			drops.add(new Item(17752));
		if ((getManager().getParty().getComplexity() >= 2 && com.rs.utility.Utils.random(10) == 0)) //fly fishing rod, 1
			drops.add(new Item(17794));
		if ((getManager().getParty().getComplexity() >= 4 && com.rs.utility.Utils.random(5) == 0)) //thread, 10 up to 300
			drops.add(new Item(17447, 10 + com.rs.utility.Utils.random(300)));
		for (Item item : drops)
			World.addDungeoneeringGroundItem(manager.getParty(), item, new Position(getCoordFaceX(size), getCoordFaceY(size), getZ()), false);
		int floorId = this.getManager().getParty().getFloor();
		boolean drop = com.rs.utility.Utils.random(200) == 0;
		if (this.getMostDamageReceivedSourcePlayer() == null)
			drop = false;
		Player player = this.getMostDamageReceivedSourcePlayer();
		if (drop) {
			if (floorId < 6) {
				Chronicles chronicle = Chronicles.MATPART1;
				dropChronicle(player, chronicle);
			} else if (floorId >= 6 && floorId <= 11) {
				Chronicles chronicle = Chronicles.MATPART2;
				dropChronicle(player, chronicle);
			} else if (floorId >= 12 && floorId <= 14) {
				Chronicles chronicle = Chronicles.MATPART3;
				dropChronicle(player, chronicle);
			} else if (floorId >= 15 && floorId <= 17) {
				Chronicles chronicle = Chronicles.MATPART4;
				dropChronicle(player, chronicle);
			} else if (floorId >= 18 && floorId <= 22) {
				Chronicles chronicle = Chronicles.MATPART5;
				dropChronicle(player, chronicle);
			} else if (floorId >= 23 && floorId <= 29) {
				Chronicles chronicle = Chronicles.MATPART6;
				dropChronicle(player, chronicle);
			} else if (floorId >= 36 && floorId <= 40) {
				Chronicles chronicle = Chronicles.MATPART7;
				dropChronicle(player, chronicle);
			} else if (floorId >= 42 && floorId <= 47) {
				Chronicles chronicle = Chronicles.MATPART8;
				dropChronicle(player, chronicle);
			} else if (floorId >= 48 && floorId <= 53) {
				Chronicles chronicle = Chronicles.MATPART9;
				dropChronicle(player, chronicle);
			} else if (floorId >= 54) {
				Chronicles chronicle = Chronicles.MATPART10;
				dropChronicle(player, chronicle);
			}
		}
		
	}
	
	public void dropChronicle(Player luckyPlayer, Chronicles chronicle) {

	}

	public DungeonManager getManager() {
		return manager;
	}

	public void setManager(DungeonManager manager) {
		this.manager = manager;
	}

	public double getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(double multiplier) {
		this.multiplier = multiplier;
	}
}
