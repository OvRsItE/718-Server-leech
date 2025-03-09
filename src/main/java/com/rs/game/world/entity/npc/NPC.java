package com.rs.game.world.entity.npc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Lists;
import com.rs.Constants;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.cores.CoresManager;
import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.item.Item;
import com.rs.game.map.Position;
import com.rs.game.world.Projectile;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.listeners.ClickOptionListener;
import com.rs.game.world.entity.npc.combat.NPCCombat;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.npc.familiar.Familiar;
import com.rs.game.world.entity.npc.instances.hydra.HydraController;
import com.rs.game.world.entity.npc.randomspawns.RandomSpawns;
import com.rs.game.world.entity.npc.randomspawns.SuperiorBosses;
import com.rs.game.world.entity.npc.randomspawns.WildyBossSpawns;
import com.rs.game.world.entity.player.BuffManager;
import com.rs.game.world.entity.player.DonatorRanks;
import com.rs.game.world.entity.player.GameMode;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.skilling.HerbCleaning;
import com.rs.game.world.entity.player.content.Lootbeams;
import com.rs.game.world.entity.player.content.Lootbeams.Tier;
import com.rs.game.world.entity.player.content.RingOfWealth;
import com.rs.game.world.entity.player.content.Slayer;
import com.rs.game.world.entity.player.content.Slayer.SlayerMaster;
import com.rs.game.world.entity.player.content.SlayerManager;
import com.rs.game.world.entity.player.content.clues.CasketHandler;
import com.rs.game.world.entity.player.content.diary.Achievement;
import com.rs.game.world.entity.player.content.pets.Pets;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.player.content.skills.prayer.Burying;
import com.rs.game.world.entity.player.content.skills.slayer.SuperiorSlayer;
import com.rs.game.world.entity.player.content.skills.slayer.SuperiorSlayer.SuperiorMonsters;
import com.rs.game.world.entity.player.content.social.FriendChatsManager;
import com.rs.game.world.entity.player.controller.impl.Wilderness;
import com.rs.game.world.entity.updating.impl.*;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.stream.Collectors;

public class NPC extends Entity implements Serializable {

	private static final long serialVersionUID = -4794678936277614443L;

	private int id;
	private Position spawnPosition;
	private int mapAreaNameHash;
	private boolean canBeAttackFromOutOfArea;
	private boolean randomwalk;
	private int[] bonuses; // 0 stab, 1 slash, 2 crush,3 mage, 4 range, 5 stab def, blahblah till 9
	private boolean spawned;
	private transient NPCCombat combat;
	public Position forceWalk;
	@Setter
	@Getter
	public boolean canRetaliate = true;
	@Setter
	public int followDistance;

	private long lastAttackedByTarget;
	private boolean cantInteract;
	private int capDamage;
	private int lureDelay;
	private boolean cantFollowUnderCombat;
	private boolean forceAgressive;
	private int forceTargetDistance;
	private boolean forceFollowClose;
	private boolean forceMultiAttacked;
	private boolean noDistanceCheck;
	private Entity following;

	// npc masks
	private transient Transformation nextTransformation;
	private transient SecondaryBar nextSecondaryBar;
	// name changing masks
	private String name;
	private transient boolean changedName;
	private int combatLevel;
	private transient boolean changedCombatLevel;
	private transient boolean locked;
	private int respawnDirection;
	@Getter
	@Setter
	private int walkRange = 5;
	private int maxHitpoints;

	private int rareDropTableChance;

	public NPC(int id, Position tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		this(id, tile, mapAreaNameHash, 0, canBeAttackFromOutOfArea, false);
	}

	/**
	 * for dungeoneering
	 *
	 * @param id
	 * @param tile
	 * @param mapAreaNameHash
	 * @param
	 * @param canBeAttackFromOutOfArea
	 * @param spawned
	 */
	public NPC(int id, Position tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		this(id, tile, mapAreaNameHash, 0, canBeAttackFromOutOfArea, spawned);
	}

	/*
	 * creates and adds npc
	 */
	public NPC(int id, Position tile, int mapAreaNameHash, int faceDirection, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(tile);
		this.id = id;
		spawnPosition = new Position(tile);
		this.mapAreaNameHash = mapAreaNameHash;
		this.canBeAttackFromOutOfArea = canBeAttackFromOutOfArea;
		setSpawned(spawned);
		combatLevel = -1;
		this.maxHitpoints = (int) Math.round((double) (getCombatDefinitions().getHitpoints()));
		setHitpoints(getMaxHitpoints());
		this.respawnDirection = faceDirection;
		if (getCombatLevel() <= -1) {
			setCombatLevel(0);
		}
		setDirection(getRespawnDirection());
		setRandomWalk((!forceStandStill(id)) && ((getDefinitions().walkMask & 0x2) != 0 || forceRandomWalk(id) || id >= Constants.OSRS_NPCS_OFFSET));
		bonuses = NPCBonuses.getBonuses(id);
		combat = new NPCCombat(this);
		capDamage = -1;
		lureDelay = 12000;
		rareDropTableChance = 100;
		// npc is inited on creating instance
		initEntity();
		World.addNPC(this);
		World.updateEntityRegion(this);
		// npc is started on creating instance
		loadMapRegions();
		checkMultiArea();
	}

	private boolean forceStandStill(int id2) {
		switch (id) {
			//case 9085:
			case 1597:
			case 27663:
			case 14620:
			case 9085:
			case 1598:
			case 8481:
			case 598:
			case 945:
			case 720:
			case 5626:
			case 5925:
			case 550:
			case 6970:
			case 599:
			case 266:
			case 546:
			case 1694:
			case 8227:
			case 3789:
			case 279:
			case 948:
			case 219:
			case 7530:
			case 524:
			case 1918:
			case 3021:
			case 529:
			case 2617:
			case 4288:
			case 3820:
			case 538:
			case 587:
			case 5112:
			case 11674:
			case 1699:
			case 2259:
			case 552:
			case 11678:
			case 6070:
			case 554:
			case 551:
			case 534:
			case 585:
			case 6361:
			case 548:
			case 1167:
			case 8556:
			case 528:
			case 457:
			case 576:
			case 5866:
			case 11:
			case 14240:
			case 6892:
			case 8480:
			case 659:
			case 28059:
			case 25870:
			case 27288:
			case 27290:
			case 27292:
			case 27294:
			case 25909:
			case 25910:
			case 25911:
			case 25912:
			case 25886:
			case 25914:
			case 27566:
				return true;
			default:
				return false;
		}
	}

	@Override
	public boolean needMasksUpdate() {
		return super.needMasksUpdate() || nextSecondaryBar != null || nextTransformation != null || changedCombatLevel;
	}

	public void transformIntoNPC(int id) {
		setNPC(id);
		nextTransformation = new Transformation(id);
	}

	public void setNextNPCTransformation(int id) {
		setNPC(id);
		nextTransformation = new Transformation(id);
		if (getCustomCombatLevel() != -1) {
			changedCombatLevel = true;
		}
		if (getCustomName() != null) {
			changedName = true;
		}
	}

	public static void moo() {
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				String[] mooing = {"Moo", "Moooo", "MOOOOOOOOO", "Mooooooooo"};
				int i = Misc.random(1, 3);
				for (NPC n : World.getNPCs()) {
					if (!n.getName().equalsIgnoreCase("Cow")) {
						continue;
					}
					n.setNextForceTalk(new ForceTalk(mooing[i]));
				}
			}
		}, 0, 5); // time in seconds
	}

	public void setNPC(int id) {
		this.id = id;
		bonuses = NPCBonuses.getBonuses(id);
	}

	@Override
	public void resetMasks() {
		super.resetMasks();
		nextTransformation = null;
		changedCombatLevel = false;
		changedName = false;
		nextSecondaryBar = null;
	}

	public int getMapAreaNameHash() {
		return mapAreaNameHash;
	}

	public void setCanBeAttackFromOutOfArea(boolean b) {
		canBeAttackFromOutOfArea = b;
	}

	public boolean canBeAttackFromOutOfArea() {
		return canBeAttackFromOutOfArea;
	}

	public void setBonuses(int[] bonuses) {
		this.bonuses = bonuses;
	}

	public NPCDefinitions getDefinitions() {
		return NPCDefinitions.getNPCDefinitions(id);
	}

	public NPCCombatDefinitions getCombatDefinitions() {
		return NPCCombatDefinitionsL.getNPCCombatDefinitions(id);
	}

	@Override
	public int getMaxHitpoints() {
		return maxHitpoints <= 0 ? 1 : maxHitpoints;
	}
	public void setMaxHitpoints(int maxHitpoints) {
		this.maxHitpoints = maxHitpoints;
	}

	public int getId() {
		return id;
	}

	public void processNPC() {
		if (isDead() || locked) {
			return;
		}
		if (!combat.process()) { // if not under combat
			if (!isForceWalking()) {// combat still processed for attack delay
				// go down
				// random walk
				if (!cantInteract) {
					if (!checkAgressivity()) {
						if (getFreezeDelay() < Utils.currentTimeMillis()) {
							if (hasRandomWalk() && World.getRotation(getZ(), getX(), getY()) == 0 // temporary
									// fix
									&& Math.random() * 1000.0 < 100.0) {
								int moveX = (int) Math.round(Math.random() * walkRange);
								int moveY = (int) Math.round(Math.random() * walkRange);
								resetWalkSteps();
								if (getMapAreaNameHash() != -1) {
									if (!MapAreas.isAtArea(getMapAreaNameHash(), this)) {
										forceWalkRespawnTile();
										return;
									}
									addWalkSteps(getX() + moveX, getY() + moveY, 5);
								} else {
									addWalkSteps(spawnPosition.getX() + moveX, spawnPosition.getY() + moveY, 5);
								}
							}
						}
					}
				}
			}
		}

		// Changing npc combat levels
		if (id == 15581) {// Party Demon
			setCombatLevel(1500);
		}
		if (id == 7891) {// Party Demon
			setCanRetaliate(false);
			setCantFollowUnderCombat(true);
			setCantWalk(true);
			heal(2000);
		}
		if (id == 3064) {// Lesser Demon Champion
			setCombatLevel(900);
		}
		if (id == 3058) {// Giant Champion
			setCombatLevel(800);
		}
		if (id == 3063) {// Jogre Champion
			setCombatLevel(850);
		}
		if (id == 15187) {// TokHaar-Ket Champion
			setCombatLevel(1100);
		}
		if (id == 10495) {// High level Lesser Demon
			setCombatLevel(550);
		}
		if (id == 4706) {// High Level Moss giant
			setCombatLevel(600);
		}
		if (id == 10769) {// High level ice giant
			setCombatLevel(650);
		}
		if (id == 10717) {// High level Hill giant
			setCombatLevel(550);
		}
		if (id == 10761) {// High level Fire giant
			setCombatLevel(680);
		}
		if (id == 3450) {// High level Jogre
			setCombatLevel(600);
		}
		if (id == 999) {// Doomion
			setCombatLevel(900);
		}
		if (id == 998) {// Othainian
			setCombatLevel(900);
		}
		if (id == 1000) {// Holthion
			setCombatLevel(900);
		}
		if (id == 14550) {// Chronozon
			setCombatLevel(950);
		}
		if (id == 14503) {// Agrith Naar
			setCombatLevel(850);
		}
		if (id == 9356) {// Agrith Naar
			setCombatLevel(100);
		}
		// Renaming basic npcs
		if (id == 14240) {
			setName("Donator Shop");
		}
		if (id == 541) {
			setName("Donator Points Shop 2");
		}
		if (id == 410) {
			setName("Mysterious Gambler");
		}
		if (id == 1694) {
			setName("Range Supplies");
		}
		if (id == 4519) {
			setName("Banker");
		}
		if (id == 620) {
			setName("Fortune Guide");
		}
		if (id == 945) {
			setName("<img=5>Ironman Shop");
		}
		if (id == 4544) {
			setName("Prestige Master");
		}
		if (id == 13191) {
			setName("Corporeal Beast Shop");
		}
		if (id == 279) {
			setName("Ticket Exchange");
		}
		if (id == 5113) {
			setName("Hunter Falconry");
		}
		if (id == 8864) {
			setName("Fishing Shop");
		}
		if (id == 585) {
			setName("Crafting Shop");
		}
		if (id == 529) {
			setName("General Store");
		}
		if (id == 546) {
			setName("Magic Supplies");
		}
		if (id == 15811) {
			setName("Skilling Supplies");
		}
		if (id == 2253) {
			setName("Prestige Master");
		}
		if (id == 550) {
			setName("Ranging Shop");
		}
		if (id == 576) {
			setName("Food & Potion Shop");
		}
		if (id == 14792) {
			setName("Skill Cape Shop");
		}
		if (id == 6539) {
			setName("Vote Shop");
		}
		if (id == 6970) {
			setName("Summoning Shop");
		}
		if (id == 1285) {
			setName("Donator stores");
		}
		if (id == 758) {
			setName("Farming Shop");
		}
		if (id == 11506) {
			setName("General Korasi");
		}
		if (id == 5112) {
			setName("Hunter Shop");
		}
		if (id == 9711) {
			setName("Dungeoneering Rewards");
		}
		if (id == 14165) {
			setName("Tokkul Shop");
		}
		if (id == 659) {
			setName("Point Shops");
		}
		if (id == 13727) {
			setName("Loyalty Rewards");
		}
		if (id == 6361) {
			setName("Monster teleports");
		}
		if (id == 11571) {
			setName("Elite shop");
		}
		if (id == 11577) {
			setName("Elite shop");
		}
		if (id == 3709) {
			setName("More teleports");
		}
		if (id == 6988) {
			setName("Summoning Shop");
		}
		if (id == 1918) {
			setName("Extreme Donator Donator Shop");
		}
		if (id == 14854) {
			setName("Extreme Donator Potions");
		}
		if (id == 14811) {
			setName("Combat Supplies");
		}
		if (id == 8556) {
			setName("DTRewards");
		}
		if (id == 2904) {
			setName("Account Guardian");
		}
		if (id == 8085) {
			setName("Dice Host");
		}
		if (id == 6892) {
			setName("Boss Pet Manager");
		}
		if (id == 109) {
			setForceAgressive(true);
		}


		// edit npc's movement

		if (following != null) {
			sendFollow();
			return;
		}

		if (isForceWalking()) {
			if (getFreezeDelay() < Utils.currentTimeMillis()) {
				if (getX() != forceWalk.getX() || getY() != forceWalk.getY()) {
					if (!hasWalkSteps()) {
						addWalkSteps(forceWalk.getX(), forceWalk.getY(), getSize(), true);
					}
					if (!hasWalkSteps()) { // failing finding route
						setNextPosition(new Position(forceWalk)); // force
						// tele
						// to
						// the
						// forcewalk
						// place
						forceWalk = null; // so ofc reached forcewalk place

					}
				} else {
					// walked till forcewalk place
					forceWalk = null;
				}
			}
		}
	}

	@Override
	public void processEntity() {
		super.processEntity();
		processNPC();
	}

	@Override
	public boolean canMove(int dir) {
		return true;
	}

	public int getRespawnDirection() {
		NPCDefinitions definitions = getDefinitions();
		if (definitions.rotation << 32 != 0 && definitions.respawnDirection > 0 && definitions.respawnDirection <= 8) {
			return 4 + definitions.respawnDirection << 11;
		}
		return respawnDirection > 0 ? (4 + respawnDirection << 11) : 0;
	}

	public void setFollowing(Entity following) {
		this.following = following;
	}

	public Entity getFollowing() {
		return following;
	}

	protected void sendFollow() {
		if (getLastFaceEntity() != following.getClientIndex()) {
			setNextFaceEntity(following);
		}
		if (getFreezeDelay() >= Utils.currentTimeMillis()) {
			return;
		}
		int size = getSize();
		int distanceX = following.getX() - getX();
		int distanceY = following.getY() - getY();
		if (distanceX < size && distanceX > -1 && distanceY < size
				&& distanceY > -1 && !following.hasWalkSteps() && !hasWalkSteps()) {
			resetWalkSteps();
			if (!addWalkSteps(following.getX() + 1, getY())) {
				resetWalkSteps();
				if (!addWalkSteps(following.getX() - size, getY())) {
					resetWalkSteps();
					if (!addWalkSteps(getX(), following.getY() + 1)) {
						resetWalkSteps();
						addWalkSteps(getX(), following.getY() - size);
					}
				}
			}
			return;
		}
		if (!clipedProjectile(following, true) || distanceX > size || distanceX < -1 || distanceY > size || distanceY < -1) {
			resetWalkSteps();
			addWalkStepsInteract(following.getX(), following.getY(), getRun() ? 2 : 1, size, true);
			return;
		}
		resetWalkSteps();
	}

	/*
	 * forces npc to random walk even if cache says no, used because of fake
	 * cache information
	 */
	private static boolean forceRandomWalk(int npcId) {
		switch (npcId) {
			case 11226:
			case 14620:
			case 3341:
			case 3342:
			case 3343:
			case 16014:
				return true;
			default:
				return false;
		}
	}

	public void sendSoulSplit(final Hit hit, final Entity user) {
		final NPC target = this;
		if (hit.getDamage() > 0) {
			World.sendProjectile(user, this, 2263, 11, 11, 20, 5, 0, 0);
		}
		int healing = 0;
		if (user instanceof Player) {
			healing += hit.getDamage() / (((Player) user).getEquipment().hasAmuletOfSouls() || ((Player) user).petManager.getNpcId() == 16119 ? 4 : 5);
		}
		user.heal(healing);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				setNextGraphics(new Graphics(2264));
				if (hit.getDamage() > 0) {
					World.sendProjectile(target, user, 2263, 11, 11, 20, 5, 0, 0);
				}
			}
		}, 1);
	}

	@Override
	public void handleIngoingHit(final Hit hit) {
		if (capDamage != -1 && hit.getDamage() > capDamage) {
			hit.setDamage(capDamage);
		}
		if (hit.getLook() != HitLook.MELEE_DAMAGE && hit.getLook() != HitLook.RANGE_DAMAGE
				&& hit.getLook() != HitLook.MAGIC_DAMAGE) {
			return;
		}
		Entity source = hit.getSource();
		if (source == null)
			return;
		if (source instanceof Player) {
			final Player player = (Player) source;
			if (Slayer.getLevelRequirement(getName()) > player.getSkills().getLevel(Skills.SLAYER)) {
				hit.setDamage(0);
				player.sendMessage("You don't have a high enough Slayer level to damage this creature.");
			}
			if (hitListener != null && hitListener.preDamage != null) {
				hitListener.preDamage.accept(hit);
			}
			if (source.hitListener != null && source.hitListener.preTargetDamage != null) {
				source.hitListener.preTargetDamage.accept(hit, source);
			}
			if (player.getPrayer().hasPrayersOn()) {
				if (player.getPrayer().usingPrayer(1, 18)) {
					sendSoulSplit(hit, player);
				}
				if (hit.getDamage() == 0) {
					return;
				}
				if (!player.getPrayer().isBoostedLeech()) {
					if (hit.getLook() == HitLook.MELEE_DAMAGE) {
						if (player.getPrayer().usingPrayer(1, 19)) {
							if (Utils.getRandom(4) == 0) {
								Player p = (Player) source;
								player.getPrayer().setBoostedLeech(true);
								player.prayer.increaseTurmoilBonus(p);
								return;
							}
						}  else if (player.getPrayer().usingPrayer(1, 20)) {
								if (Utils.getRandom(4) == 0) {
									Player p = (Player) source;
									player.getPrayer().setBoostedLeech(true);
									player.prayer.increaseMalevolenceBonus(p);
									return;
								}

						} else if (player.getPrayer().usingPrayer(1, 1)) { // sap
							// att
							if (Utils.getRandom(4) == 0) {
								if (player.getPrayer().reachedMax(0)) {
									player.getPackets().sendGameMessage(
											"Your opponent has been weakened so much that your sap curse has no effect.",
											true);
								} else {
									player.getPrayer().increaseLeechBonus(0);
									player.getPackets().sendGameMessage(
											"Your curse drains Attack from the enemy, boosting your Attack.", true);
								}
								player.animate(new Animation(12569));
								player.setNextGraphics(new Graphics(2214));
								player.getPrayer().setBoostedLeech(true);
								World.sendProjectile(player, this, 2215, 35, 35, 20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2216));
									}
								}, 1);
								return;
							}
						} else {
							if (player.getPrayer().usingPrayer(1, 10)) {
								if (Utils.getRandom(7) == 0) {
									if (player.getPrayer().reachedMax(3)) {
										player.getPackets().sendGameMessage(
												"Your opponent has been weakened so much that your leech curse has no effect.",
												true);
									} else {
										player.getPrayer().increaseLeechBonus(3);
										player.getPackets().sendGameMessage(
												"Your curse drains Attack from the enemy, boosting your Attack.", true);
									}
									player.animate(new Animation(12575));
									player.getPrayer().setBoostedLeech(true);
									World.sendProjectile(player, this, 2231, 35, 35, 20, 5, 0, 0);
									WorldTasksManager.schedule(new WorldTask() {
										@Override
										public void run() {
											setNextGraphics(new Graphics(2232));
										}
									}, 1);
									return;
								}
							}
							if (player.getPrayer().usingPrayer(1, 14)) {
								if (Utils.getRandom(7) == 0) {
									if (player.getPrayer().reachedMax(7)) {
										player.getPackets().sendGameMessage(
												"Your opponent has been weakened so much that your leech curse has no effect.",
												true);
									} else {
										player.getPrayer().increaseLeechBonus(7);
										player.getPackets().sendGameMessage(
												"Your curse drains Strength from the enemy, boosting your Strength.",
												true);
									}
									player.animate(new Animation(12575));
									player.getPrayer().setBoostedLeech(true);
									World.sendProjectile(player, this, 2248, 35, 35, 20, 5, 0, 0);
									WorldTasksManager.schedule(new WorldTask() {
										@Override
										public void run() {
											setNextGraphics(new Graphics(2250));
										}
									}, 1);
									return;
								}
							}

						}
					}
					if (hit.getLook() == HitLook.RANGE_DAMAGE) {
					 if (player.getPrayer().usingPrayer(1, 2)) { // sap range
						 if (Utils.getRandom(4) == 0) {
							 if (player.getPrayer().reachedMax(1)) {
								 player.getPackets().sendGameMessage(
										 "Your opponent has been weakened so much that your sap curse has no effect.",
										 true);
							 } else {
								 player.getPrayer().increaseLeechBonus(1);
								 player.getPackets().sendGameMessage(
										 "Your curse drains Range from the enemy, boosting your Range.", true);
							 }
							 player.animate(new Animation(12569));
							 player.setNextGraphics(new Graphics(2217));
							 player.getPrayer().setBoostedLeech(true);
							 World.sendProjectile(player, this, 2218, 35, 35, 20, 5, 0, 0);
							 WorldTasksManager.schedule(new WorldTask() {
								 @Override
								 public void run() {
									 setNextGraphics(new Graphics(2219));
								 }
							 }, 1);
							 return;
						 }
					 } else if (player.getPrayer().usingPrayer(1, 21)) {
							 if (Utils.getRandom(1) == 0) {
								 Player p = (Player) source;
								 player.getPrayer().setBoostedLeech(true);
								 player.prayer.increaseDesolationBonus(p);
								 return;
							 }
						} else if (player.getPrayer().usingPrayer(1, 11)) {
							if (Utils.getRandom(7) == 0) {
								if (player.getPrayer().reachedMax(4)) {
									player.getPackets().sendGameMessage(
											"Your opponent has been weakened so much that your leech curse has no effect.",
											true);
								} else {
									player.getPrayer().increaseLeechBonus(4);
									player.getPackets().sendGameMessage(
											"Your curse drains Range from the enemy, boosting your Range.", true);
								}
								player.animate(new Animation(12575));
								player.getPrayer().setBoostedLeech(true);
								World.sendProjectile(player, this, 2236, 35, 35, 20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2238));
									}
								});
								return;
							}
						}
					}
					if (hit.getLook() == HitLook.MAGIC_DAMAGE) {
					 if (player.getPrayer().usingPrayer(1, 22)) {
						 if (Utils.getRandom(4) == 0) {
							 Player p = (Player) source;
							 player.getPrayer().setBoostedLeech(true);
							 player.prayer.increaseAfflictionBonus(p);
							 return;
						 }
					 } else if (player.getPrayer().usingPrayer(1, 3)) { // sap mage
							if (Utils.getRandom(4) == 0) {
								if (player.getPrayer().reachedMax(2)) {
									player.getPackets().sendGameMessage(
											"Your opponent has been weakened so much that your sap curse has no effect.",
											true);
								} else {
									player.getPrayer().increaseLeechBonus(2);
									player.getPackets().sendGameMessage(
											"Your curse drains Magic from the enemy, boosting your Magic.", true);
								}
								player.animate(new Animation(12569));
								player.setNextGraphics(new Graphics(2220));
								player.getPrayer().setBoostedLeech(true);
								World.sendProjectile(player, this, 2221, 35, 35, 20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2222));
									}
								}, 1);
								return;
							}
						} else if (player.getPrayer().usingPrayer(1, 12)) {
							if (Utils.getRandom(7) == 0) {
								if (player.getPrayer().reachedMax(5)) {
									player.getPackets().sendGameMessage(
											"Your opponent has been weakened so much that your leech curse has no effect.",
											true);
								} else {
									player.getPrayer().increaseLeechBonus(5);
									player.getPackets().sendGameMessage(
											"Your curse drains Magic from the enemy, boosting your Magic.", true);
								}
								player.animate(new Animation(12575));
								player.getPrayer().setBoostedLeech(true);
								World.sendProjectile(player, this, 2240, 35, 35, 20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2242));
									}
								}, 1);
								return;
							}
						}
					}

					// overall

					if (player.getPrayer().usingPrayer(1, 13)) { // leech defence
						if (Utils.getRandom(10) == 0) {
							if (player.getPrayer().reachedMax(6)) {
								player.getPackets().sendGameMessage(
										"Your opponent has been weakened so much that your leech curse has no effect.",
										true);
							} else {
								player.getPrayer().increaseLeechBonus(6);
								player.getPackets().sendGameMessage(
										"Your curse drains Defence from the enemy, boosting your Defence.", true);
							}
							player.animate(new Animation(12575));
							player.getPrayer().setBoostedLeech(true);
							World.sendProjectile(player, this, 2244, 35, 35, 20, 5, 0, 0);
							WorldTasksManager.schedule(new WorldTask() {
								@Override
								public void run() {
									setNextGraphics(new Graphics(2246));
								}
							}, 1);
							return;
						}
					}
				}
			}
		}
	}

	@Override
	public void reset() {
		super.reset();
		setDirection(getRespawnDirection());
		combat.reset();
		bonuses = NPCBonuses.getBonuses(id); // back to real bonuses
		forceWalk = null;
	}

	@Override
	public void finish() {
		if (isFinished()) {
			return;
		}
		setFinished(true);

		World.updateEntityRegion(this);
		World.removeNPC(this);
	}

	public void setRespawnTask() {
		if (!isFinished()) {
			reset();
			setLocation(spawnPosition);
			finish();
		}
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					spawn();
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, getCombatDefinitions().getRespawnDelay() * 600, TimeUnit.MILLISECONDS);
	}

	public void deserialize() {
		if (combat == null) {
			combat = new NPCCombat(this);
		}
		spawn();
	}

	public void spawn() {
		setFinished(false);
		World.addNPC(this);
		setLastRegionId(0);
		World.updateEntityRegion(this);
		loadMapRegions();
		checkMultiArea();
		if (spawnListener != null) {
			spawnListener.handle(this);
		}
	}

	public NPCCombat getCombat() {
		return combat;
	}

	@Override
	public void sendDeath(Entity source) {
		final NPCCombatDefinitions defs = getCombatDefinitions();
		Player killer = getMostDamageReceivedSourcePlayer();
		resetWalkSteps();
		combat.removeTarget();
		animate(null);
		setFollowing(null);
		if (id  >= 16106 &&  id <= 16111) {
			RandomSpawns.SpawnRev();

	}
		if (id == 16008) {
			WildyBossSpawns.veneatispawned = false;

		}

		if (id == 16007) {
			WildyBossSpawns.callistospawned = false;

		}

		if (id == 16011) {
			WildyBossSpawns.veneatispawned = false;

		}

		if (id == 25886 && Utils.random(180) == 1 && killer.UnlockedSuperiorBosses == 1) {
			SuperiorBosses.SpawnSire(killer, getAsNPC());
			World.sendWorldMessage("[<col=ff0000>Boss Spawn</col>] - <col=ff0000>" + Utils.formatPlayerNameForDisplay(killer.getUsername()) +
					"<col=ff0000> has spawned a superior abyssal sire boss they have 5 minutes to kill it. <col=ff0000>", false);
		}

		if (id == 26504 && Utils.random(80) == 1 && WildyBossSpawns.veneatispawned == false) {
			WildyBossSpawns.SpawnWildyBoss();
			World.sendWorldMessage("[<col=ff0000>Boss Spawn</col>] - <col=ff0000>" + Utils.formatPlayerNameForDisplay(killer.getUsername()) +
					"<col=ff0000> has awakend a cursed venenatis. <col=ff0000>", false);


		}

		if (id == 26612 && Utils.random(80) == 1 && WildyBossSpawns.vetionspawned == false) {
			WildyBossSpawns.SpawnCursedvetion();
			World.sendWorldMessage("[<col=ff0000>Boss Spawn</col>] - <col=ff0000>" + Utils.formatPlayerNameForDisplay(killer.getUsername()) +
					"<col=ff0000> has awakend a cursed vet'ion. <col=ff0000>", false);


		}

		if (id == 26503 && Utils.random(80) == 1 && WildyBossSpawns.callistospawned == false) {
			WildyBossSpawns.SpawnCursedCallsito();
			World.sendWorldMessage("[<col=ff0000>Boss Spawn</col>] - <col=ff0000>" + Utils.formatPlayerNameForDisplay(killer.getUsername()) +
					"<col=ff0000> has awakend a cursed vet'ion. <col=ff0000>", false);


		}


		if (deathStartListener != null) {
			deathStartListener.handle(this, source);
			if (isFinished())
				return;
		}

		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					animate(new Animation(defs.getDeathEmote()));
				} else if (loop >= defs.getDeathDelay()) {
					if (source instanceof Player)
						((Player) source).getControlerManager().processNPCDeath(NPC.this);
					if (deathEndListener != null) {
						deathEndListener.handle(NPC.this, source);
						if (isFinished())
							return;
					}
					drop();
					reset();
					setLocation(spawnPosition);
					finish();
					if (!isSpawned()) {
						if (killer.isOwner()) {
							if (!isFinished()) {
								reset();
								setLocation(spawnPosition);
								finish();
							}
							CoresManager.slowExecutor.schedule(new Runnable() {
								@Override
								public void run() {
									try {
										spawn();
									} catch (Throwable e) {
										Logger.handle(e);
									}
								}
							}, getCombatDefinitions().getRespawnDelay() * 600 / 2, TimeUnit.MILLISECONDS);
						} else {
							setRespawnTask();
						}
					}
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}

	public final void handleHerbBox(final Player player) {

	}

	public final void handleSeedBox(final Player player) {

	}

	public final void handleGemBag(final Player player) {

	}

	public void drop() {
		try {
			if (id == 16008 || id == 16011 || id == 16007 || id == 16083 || id == 16113 || id == 16103 || id == 16138 || id == 16150 || id == 16087 || id == 16089 || id == 13450)  {
				sendMultiDrop();
				return;
			}
			Drop[] drops = com.rs.utility.NPCDrops.getDrops(id);

			Player killer = getMostDamageReceivedSourcePlayer();


			if (killer == null) {
				return;
			}

			if (!(getId() >= 28058 && getId() <= 28061)) { //Vorkath id
				killer.getBossSlayer().deincrementKills(this);
				killer.getBossPetsManager().rollBossPet(getId());
			}

			killer.getKillcountManager().incremenetAndGet(getId());



			SuperiorSlayer.rollSpawn(killer, this);
			CasketHandler.dropCasket(killer, this);

			//The alchemist rare drops.
//			if (getId() == 16071 || getId() == 16080 || getId() == 16081) {
//				int[] legs = { 29794 ,29795, 29796 };
//				int[] hilts = { 29800, 29798, 29799 };
//				if (Utils.random(60) == 1) {
//					Item reward = new Item(legs[Utils.random(legs.length)]);
//					World.addGroundItem(reward, new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ()), killer, true, killer.getGameMode().isIronman() ? 1800000000 : 60);
//					World.sendWorldMessage("[<col=ff8c38>Drop News</col>] - <col=ff8c38>" + Utils.formatPlayerNameForDisplay(killer.getUsername()) +
//							"</col> has received a <col=ff8c38>" + Utils.formatPlayerNameForDisplay(reward.getName()) + "</col> as a rare drop!", false);
//					killer.getItemCollectionManager().handleCollection(reward);
//				} else if (Utils.random(200) == 1) {
//					Item reward = new Item(hilts[Utils.random(hilts.length)]);
//					World.addGroundItem(reward, new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ()), killer, true, killer.getGameMode().isIronman() ? 1800000000 : 60);
//					World.sendWorldMessage("[<col=ff8c38>Drop News</col>] - <col=ff8c38>" + Utils.formatPlayerNameForDisplay(killer.getUsername()) +
//							"</col> has received a <col=ff8c38>" + Utils.formatPlayerNameForDisplay(reward.getName()) + "</col> as a rare drop!", false);
//					killer.getItemCollectionManager().handleCollection(reward);
//				}
//			}
			if (getId() == 16157 || getId() == 16158) {
				killer.getSkills().addXp(Skills.DUNGEONEERING, 250);
			}
			if (getId() == 27566 || getId() == 27541) {
				killer.getSkills().addXp(Skills.DUNGEONEERING, 1000);
			}

			//Khourend dungeon
			if (killer.getX() >= 1600 && killer.getY() >= 9984 && killer.getX() <= 1730 && killer.getY() <= 10111) {
				int[] totem_parts = { 29621, 29620, 29619 };
				if (SuperiorMonsters.isSuperiorMonster(getId())) {
					Item part = new Item(totem_parts[Utils.random(totem_parts.length)]);
					killer.getItemCollectionManager().handleCollection(part);
					World.addGroundItem(part, new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ()), killer, true, 180);
				} else if (Utils.random(2) == 1) { //Dark totem parts
					int roll = (int) ((200 + -getMaxHitpoints() / 50D));
					if (Utils.random(roll) == 1) {
						Item part = new Item(totem_parts[Utils.random(totem_parts.length)]);
						killer.getItemCollectionManager().handleCollection(part);
						World.addGroundItem(part, new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ()), killer, true, 180);
					}
				} else { //Ancient shard drops
					int ancient_shard = 29955;
					int roll = (int) ((200 + -getMaxHitpoints() / 50D));
					if (Utils.random(roll) == 1) {
						Item shard = new Item(ancient_shard);
						killer.getItemCollectionManager().handleCollection(shard);
						World.addGroundItem(shard, new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ()), killer, true, 180);
					}
				}
			}

			if (getName().contains("Cave bug")) {
				killer.getDiaryManager().progress(Achievement.CAVE_BUG, 1);
			}

			if (getCombatLevel() > 12) {
				if (Utils.random(50) == 0) {
					if (getId() == 16027 || getId() == 20492) {
						killer.getPackets().sendGameMessage("<col=ff0000>A herb box falls to the ground as you slay your opponent.</col>");
						World.addGroundItem(new Item(29535), new Position(killer), killer, true, 60);
					} else {
						World.addGroundItem(new Item(29535, 1),
								new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ()), killer, true,
								180);
						killer.getPackets().sendGameMessage(
								"<col=ff0000>A herb box falls to the ground as you slay your opponent.</col>");
					}
				}
			}
			if (getCombatLevel() > 12) {
				if (Utils.random(50) == 0) {
					if (getId() == 16027 || getId() == 20492) {
						killer.getPackets().sendGameMessage("<col=ff0000>A seed box falls to the ground as you slay your opponent.</col>");
						World.addGroundItem(new Item(29536), new Position(killer), killer, true, 60);
					} else {
						World.addGroundItem(new Item(29536, 1),
								new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ()), killer, true,
								180);
						killer.getPackets().sendGameMessage(
								"<col=ff0000>A seed box falls to the ground as you slay your opponent.</col>");
					}
				}
			}
			if (getCombatLevel() > 180) {
				if (Utils.random(500) == 0) {
					Item part = new Item(29478);
					killer.getItemCollectionManager().handleCollection(part);
					World.addGroundItem(part, new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ()), killer, true, 180);
					World.sendWorldMessage("<img=24><col=ff8c38>" + killer.getDisplayName() + "<col=ff8c38> just found <col=F39407>a Toxic key part 1.<img=24>", false);
					Lootbeams.create(new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ()), killer, Tier.SMALL);
				}
			}
			if (getCombatLevel() > 180) {
				if (Utils.random(500) == 0) {
					Item part = new Item(29479);
					killer.getItemCollectionManager().handleCollection(part);
					World.addGroundItem(part, new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ()), killer, true, 180);
					World.sendWorldMessage("<img=24><col=ff8c38>" + killer.getDisplayName() + "<col=ff8c38> just found <col=ff8c38>a Toxic key part 2.<img=24>", false);
					Lootbeams.create(new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ()), killer, Tier.SMALL);
				}
			}
			if (getCombatLevel() > 180) {
				if (Utils.random(500) == 0) {
					Item part = new Item(29480);
					killer.getItemCollectionManager().handleCollection(part);
					World.addGroundItem(part, new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ()), killer, true, 180);
					World.sendWorldMessage("<img=24><col=ff8c38>" + killer.getDisplayName() + "<col=ff8c38> just found <col=ff8c38>a Toxic key part 3.<img=24>", false);
					Lootbeams.create(new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ()), killer, Tier.SMALL);
				}
			}

			if (getCombatLevel() > 20 && Utils.random(300) == 0) {
				if (getId() == 16027 || getId() == 20492) {
					killer.setPvmPoints(killer.getPvmPoints() + 300);
					killer.getPackets().sendGameMessage("<col=ff0000>You recieve 300 venomite points for slaying your opponent.</col>");
				} else {
					killer.setPvmPoints(killer.getPvmPoints() + 300);
					killer.getPackets().sendGameMessage("<col=ff0000>You recieve 300 venomite points for slaying your opponent.</col>");
				}
			}
			if (getCombatLevel() > 70 && Utils.random(150) == 0) {
				if (getId() == 16027 || getId() == 20492) {
					killer.setPvmPoints(killer.getPvmPoints() + 600);
					killer.getPackets().sendGameMessage("<col=ff0000>You recieve 600 venomite points for slaying your opponent.</col>");
				} else {

					killer.setPvmPoints(killer.getPvmPoints() + 600);
					killer.getPackets().sendGameMessage("<col=ff0000>You recieve 600 venomite points for slaying your opponent.</col>");
				}
			}
			if (getCombatLevel() > 120 && Utils.random(100) == 0) {
				if (getId() == 16027 || getId() == 20492) {
					killer.setPvmPoints(killer.getPvmPoints() + 1000);
					killer.getPackets().sendGameMessage("<col=ff0000>You recieve 1000 venomite points for slaying your opponent.</col>");
				} else {
					killer.setPvmPoints(killer.getPvmPoints() + 1000);
					killer.getPackets().sendGameMessage("<col=ff0000>You recieve 1000 venomite points for slaying your opponent.</col>");
				}
			}
			if (getCombatLevel() > 8 && killer.getSlayerManager().isValidTask(getName())) {
				if (Utils.random(50) == 0) {
					if (getId() == 16027 || getId() == 20492) {
						killer.getPackets().sendGameMessage("<col=ff0000>A slayer casket tier 1 falls to the ground as you slay your opponent.</col>");
						Lootbeams.create(new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ()), killer, Tier.SMALL);
						World.addGroundItem(new Item(28858), new Position(killer), killer, true, 60);
					} else {
						killer.getPackets().sendGameMessage("<col=ff0000>A slayer casket tier 1 falls to the ground as you slay your opponent.</col>");
						Lootbeams.create(new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ()), killer, Tier.SMALL);
						World.addGroundItem(new Item(28858, 1),
								new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ()),
								killer, true, 180);
					}
				}

			}
			if (getCombatLevel() > 115 && killer.getSlayerManager().isValidTask(getName())) {
				if (Utils.random(50) == 0) {
					if (getId() == 16027 || getId() == 20492) {
						killer.getPackets().sendGameMessage("<col=ff0000>A slayer casket tier 2 falls to the ground as you slay your opponent.</col>");
						Lootbeams.create(new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ()), killer, Tier.SMALL);
						World.addGroundItem(new Item(28857), new Position(killer), killer, true, 60);
					} else {
						killer.getPackets().sendGameMessage("<col=ff0000>A slayer casket tier 2 falls to the ground as you slay your opponent.</col>");
						Lootbeams.create(new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ()), killer, Tier.SMALL);
						World.addGroundItem(new Item(28857, 1),
								new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ()),
								killer, true, 180);
					}
				}

			}

			if (getCombatLevel() > 301 && killer.getSlayerManager().isValidTask(getName())) {
				if (Utils.random(50) == 0) {
					if (getId() == 16027 || getId() == 20492) {
						killer.getPackets().sendGameMessage("<col=ff0000>A slayer casket tier 3 falls to the ground as you slay your opponent.</col>");
						Lootbeams.create(new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ()), killer, Tier.SMALL);
						World.addGroundItem(new Item(28856), new Position(killer), killer, true, 60);
					} else {
						killer.getPackets().sendGameMessage("<col=ff0000>A slayer casket tier 3 falls to the ground as you slay your opponent.</col>");
						Lootbeams.create(new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ()), killer, Tier.SMALL);
						World.addGroundItem(new Item(28856, 1),
								new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ()),
								killer, true, 180);
					}
				}

			}
			if (killer.getBossSlayer().isValidTask(getName())) {
				if (Utils.random(50) == 1) {
					if (killer.petManager.getNpcId() == 16115) {
						Lootbeams.create(new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ()), killer, Tier.SMALL);
						World.addGroundItem(new Item(28651, 2),
								new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ()),
								killer, true, 180);
						killer.getPackets().sendGameMessage("<col=ff0000>A reaper key has fallen to the ground below your opponent and your pet doubled it.</col>");
					} else {
						Lootbeams.create(new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ()), killer, Tier.SMALL);
						World.addGroundItem(new Item(28651, 1),
								new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ()),
								killer, true, 180);
						killer.getPackets().sendGameMessage("<col=ff0000>A reaper key has fallen to the ground below your opponent.</col>");
					}
				}

			}
			if (Wilderness.isAtWild(killer) && killer.getSlayerManager().hasMaster(SlayerMaster.KRYSTILLIA)) {
				if (killer.getSlayerManager().isValidTask(getName())) {
					double chance = 1 / ((double)120 - getCombatLevel() / 5);
					double chancelow = 1 / ((double)100 + 100 - getCombatLevel() / 5);
					if (getCombatLevel() < 100 && Utils.getRandomDouble(1) < chancelow) {
						Lootbeams.create(new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ()), killer, Tier.SMALL);
						World.addGroundItem(new Item(53490, 1),
								new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ()),
								killer, true, 180);
						killer.getPackets().sendGameMessage("<col=ff0000>A larran's key has fallen to the ground below your opponent.</col>");

					} else {
						if (Utils.getRandomDouble(1) < chance) {
							Lootbeams.create(new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ()), killer, Tier.SMALL);
							World.addGroundItem(new Item(53490, 1),
									new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ()),
									killer, true, 180);
							killer.getPackets().sendGameMessage("<col=ff0000>A larran's key has fallen to the ground below your opponent.</col>");
						}
					}
				}
			}
			if (killer.getSlayerManager().hasMaster(SlayerMaster.KURADAL)) {
				if (killer.getSlayerManager().isValidTask(getName())) {
					double chance = 1 / ((double)120 - getCombatLevel() / 5);
					double chancelow = 1 / ((double)100 + 100 - getCombatLevel() / 5);
					if (getCombatLevel() < 100 && Utils.getRandomDouble(1) < chancelow) {
							Lootbeams.create(new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ()), killer, Tier.SMALL);
							World.addGroundItem(new Item(53083, 1),
									new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ()),
									killer, true, 180);
						killer.getPackets().sendGameMessage("<col=ff0000>A brimstone key has fallen to the ground below your opponent.</col>");

					} else {
						if (Utils.getRandomDouble(1) < chance) {
							Lootbeams.create(new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ()), killer, Tier.SMALL);
							World.addGroundItem(new Item(53083, 1),
									new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ()),
									killer, true, 180);
							killer.getPackets().sendGameMessage("<col=ff0000>A brimstone key has fallen to the ground below your opponent.</col>");
						}
						}
					}
				}



			if (Wilderness.isAtWild(killer) && killer.getSlayerManager().hasMaster(SlayerMaster.KRYSTILLIA)) {
				if (killer.getSlayerManager().isValidTask(getName())) {
					int amount = (int) (150 * 1.5);
						if (killer.getEquipment().getRingId() == 29463 || killer.getEquipment().getRingId() == 28830) {
							if (killer.getInventory().containsItem(28652, 1) && Misc.rollPercent(25)) {
								killer.getInventory().addItemDrop(29464, amount);
								return;
							}
							killer.getInventory().addItemDrop(29464, 150);
						} else {
							World.addGroundItem(new Item(29464, 150),
									new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ()),
									killer, true, 180);
						}
					}
				}


//			//Satan's imp
//			if (getId() == 8541 && !(killer.getInventory().contains(new Item(6542, 1))) && !(killer.getBank().containsItem(6542, 1))) {
//				World.addGroundItem(new Item(6542, 1), new WorldTile(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getPlane()), killer, true, 180);
//			}
//
//			//Satan's imp
//			if (getId() == 8542 && !(killer.getInventory().contains(new Item(15420, 1))) && !(killer.getBank().containsItem(15420, 1))) {
//				World.addGroundItem(new Item(15420, 1), new WorldTile(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getPlane()), killer, true, 180);
//			}
//
//			//Satan's imp
//			if (getId() == 8543 && !(killer.getInventory().contains(new Item(22991, 1))) && !(killer.getBank().containsItem(22991, 1))) {
//				World.addGroundItem(new Item(22991, 1), new WorldTile(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getPlane()), killer, true, 180);
//			}
//
//			//Satan
//			if (getId() == 16099 && !(killer.getInventory().contains(new Item(29541, 1))) && !(killer.getBank().containsItem(29541, 1))) {
//				World.addGroundItem(new Item(29541, 1), new WorldTile(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getPlane()), killer, true, 180);
//				killer.killedAntiSanta = true;
//			}

			Player otherPlayer = killer.getSlayerManager().getSocialPlayer();
			SlayerManager manager = killer.getSlayerManager();
			if (manager.isValidTask(getName())) {
				manager.checkCompletedTask(getDamageReceived(killer),
						otherPlayer != null ? getDamageReceived(otherPlayer) : 0);
			}

			if (drops == null)
				return;

			Drop[] possibleDrops = new Drop[drops.length];
			int possibleDropsCount = 0;
			for (Drop drop : drops) {
				if (getId() == 16008) {

				} else {
					if (drop.getRate() == 100) {
						sendDrop(killer, drop);
					} else {
						if (Utils.getRandomDouble(99) + 1 <= drop.getRate() * 2 *
								(killer.getEquipment().getLuckBoost() * killer.getGameMode().getLuckBoost() *
										killer.getDonationManager().getRank().getLuckBoost() * killer.getLuckBoost() *
										(killer.getCooldownManager().hasCooldown("vote_boosts") ? 1.05 : 1))) {
							possibleDrops[possibleDropsCount++] = drop;
							System.out.println(drops.length);
						}
					}
				}
			}

			if (possibleDropsCount > 0) {
//				if (killer.getInventory().containsItem(6199, 1)) {// this is for future stuff
//					sendDrop(killer, possibleDrops[Utils.getRandom(possibleDropsCount - 1)]);
//					killer.getInventory().deleteItem(6199, 1);
//				}
				sendDrop(killer, possibleDrops[Utils.getRandom(possibleDropsCount - 1)]);

			}

			/**
			 * Rare drop table
			 */
			int chance = rareDropTableChance;
			double boost = killer.getEquipment().getLuckBoost();
			double total = chance - chance * (boost / 100f);
			Item potential = RareDropTable.roll(killer, (int) total);
			if (potential != null) {
				String ring_name = ItemDefinitions.forId(killer.getEquipment().getRingId()).getName();
				if (getId() == 16027 || getId() == 20492) {
					World.addGroundItem(potential, new Position(killer), killer, true, 60);
				} else {
					World.addGroundItem(potential, new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ()), killer, true, 180);
				}
				if (boost != 1) {
					killer.sendMessage("<col=ff8c38>Your " + ring_name + " shines more brightly! You recieve: " + potential.getAmount() + "x " + potential.getName() + ".");
				}
				if ((killer.getEquipment().getRingId() == 29833 || killer.getEquipment().getRingId() == 28830 || killer.getEquipment().getRingId() == 29107 || killer.getEquipment().getRingId() == 28803 || killer.getEquipment().getRingId() == 28804) && Utils.random(250) == 1 && getCombatLevel() > 100) {
					if (getId() == 16027 || getId() == 20492) {
						World.sendWorldMessage("<col=ff0000><img=24>News: " + Utils.formatPlayerNameForDisplay(killer.getUsername()) + " has just recieved a Hazelmere's signet ring drop!", false);
						Lootbeams.create(new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ()), killer, Tier.SMALL);
						World.addGroundItem(new Item(29107), new Position(killer), killer, true, 60);
					} else {
						World.sendWorldMessage("<col=ff0000><img=24>News: " + Utils.formatPlayerNameForDisplay(killer.getUsername()) + " has just recieved a Hazelmere's signet ring drop!", false);
						World.addGroundItem(new Item(29107), new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ()), killer, true, 180);
						Lootbeams.create(new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ()), killer, Tier.SMALL);
					}
				}
//				if (potential.getId() == 29107) {
//					World.sendWorldMessage("<col=ff0000><img=24>News: " + killer.getDisplayName() + " has just recieved a " + potential.getName() + " drop!", false);
//					System.out.println("got hazelmere");
//				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} catch (Error e) {
			e.printStackTrace();
		}
	}
	public void sendMultiDrop() {
		Drop[] drops = com.rs.utility.NPCDrops.getDrops(id);
		if (drops == null)
			return;
		for (Entity entity : getReceivedDamage().keySet()) {
			int damage = getReceivedDamage().getOrDefault(entity, 0);
			if (entity == null || entity.isFinished() || !(entity instanceof Player) || damage < 100)
				continue;
			Drop[] possibleDrops = new Drop[drops.length];
			int possibleDropsCount = 0;
			for (Drop drop : drops) {
					if (drop.getRate() == 100) {
						sendDrop(entity.getAsPlayer(), drop);
					} else {
						if (Utils.getRandomDouble(99) + 1 <= drop.getRate() *
								(entity.getAsPlayer().getEquipment().getLuckBoost() * entity.getAsPlayer().getGameMode().getLuckBoost() *
										entity.getAsPlayer().getDonationManager().getRank().getLuckBoost() * entity.getAsPlayer().getLuckBoost() *
										(entity.getAsPlayer().getCooldownManager().hasCooldown("vote_boosts") ? 1.05 : 1))) {
							possibleDrops[possibleDropsCount++] = drop;
						}
					}
			}

			if (possibleDropsCount > 0) {
				sendDrop(entity.getAsPlayer(), possibleDrops[Utils.getRandom(possibleDropsCount - 1)]);
			}
		}


	}

	public void sendDrop(Player player, RingOfWealth drop) {
		World.addGroundItem(new Item(drop.getItemId(), drop.getAmount()),
				new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ()), player, false, 180,
				true);
	}

	public int getMaxHit(int style) {
		int maxHit = bonuses[0];
		if (style == 1)
			maxHit = bonuses[1];
		else if (style == 2)
			maxHit = bonuses[2];
		maxHit += getCombatLevel() * 0.5;
		return (int) (maxHit * 0.33);
	}


	private double charmDropPercentage;

	public double getCharmDropPercentage() {
		return charmDropPercentage;
	}

	public void setCharmDropPercentage(double charmDropPercentage) {
		this.charmDropPercentage = charmDropPercentage;
	}

	public void dropCharm(Player player, Item item) {
		if (Utils.random(1, 10) < 2)
		{
			return; // no drop for you...gg
		}
		if (player.getDonationManager().hasRank(DonatorRanks.MITHRIL)) {
			player.sm("Your Donator Rank sends a " + ItemDefinitions.getItemDefinitions(item.getId()).getName().toLowerCase() + " to your bank.");
			player.getBank().addItem(item.getId(), item.getAmount(), true);
			return;
		}
		if (player.getInventory().containsItem(29968, 1) && player.getInventory().getFreeSlots() >= 1 || player.getInventory().containsItem(item.getId(), 1)) {
			player.getPackets().sendGameMessage("Your charm magnet picks up a " + ItemDefinitions.getItemDefinitions(item.getId()).getName().toLowerCase() + ".", true);
			player.getInventory().addItem(item.getId(), item.getAmount());
			return;
		} else {
			int amount = Utils.random(3); // amount equal to 15% (??) of the npc's level
			if (amount < 1)
			{
				amount = 1;  // if amount is less than 1, make it 1 so it drops atleast 1 >.>
			}
			Item dropItem = new Item(CHARMS[Utils.random(CHARMS.length - 1)].getId(), amount);
			World.addGroundItem(dropItem,  new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ()), player, false, 180, true);
		}
	}

	private static final Item[] CHARMS = { //list of all the charms added. Can add more if needed
		new Item(12158, 1),
		new Item(12159, 1),
		new Item(12163, 1),
		new Item(12160, 1),
		new Item(12161, 1),
		new Item(12162, 1),
		new Item(12164, 1),
		new Item(12165, 1),
	};

	public void sendCharms(Player player) {
		if(Utils.random(3) != 0) {
			dropCharm(player, CHARMS[Utils.random(CHARMS.length)]);
		}
	}

	public void sendDrop(final Player player, Drop drop) {

		final int size = getSize();
		String dropName = ItemDefinitions.getItemDefinitions(drop.getItemId()).getName().toLowerCase();
		Item item = ItemDefinitions.getItemDefinitions(drop.getItemId())
				.isStackable() ? new Item(drop.getItemId(),
						drop.getMinAmount() * Constants.DROP_RATE + Utils.getRandom(drop.getExtraAmount() * Constants.DROP_RATE)) :
							new Item(drop.getItemId(), drop.getMinAmount() + Utils.getRandom(drop.getExtraAmount()));





		//Lootbeams
		if (rareDropMsg(player, dropName, item, getAsNPC()) || ItemDefinitions.forId(drop.getItemId()).getValue() >= 1_000_000) {
			Lootbeams.create(new Position(getCoordFaceX(size), getCoordFaceY(size), getZ()), player, Tier.MEDIUM);
		}
		else if (rareDropMsg(player, dropName, item, getAsNPC()) || ItemDefinitions.forId(drop.getItemId()).getValue() >= 100_000) {
            Lootbeams.create(new Position(getCoordFaceX(size), getCoordFaceY(size), getZ()), player, Tier.SMALL);
        }
		if (player.isOwner() && player.getRights() == 2) {
				player.getBank().addItem(item.getId(), item.getAmount(), true);
			return;
			}

		if (player.getEquipment().getRingId() == 29463 && item.getId() == 29464) {
			if (player.getInventory().containsItem(28652, 1) && Misc.rollPercent(25)) {
				int amount = (int) (item.getAmount() * 1.5);
				int item1 = item.getId();
				player.getInventory().addItemDrop(item1, amount);
				return;
			} else {
				player.getInventory().addItemDrop(item);
				return;
			}

		}

		//Add cash to money pouch.
		if (item.getId() == 995) {
			player.getInventory().addItemMoneyPouch(item);
			return;
		}

		//Karamja gloves shit.
		if (player.getEquipment().getGlovesId() == 19754) {
			if (getId() == 1590) {//Bronze dragon
				if (item.getId() == 2349) {
					item.setId(item.getId() + 1);
				}
			}
			if (getId() == 1591) {//Iron Dragon
				if (item.getId() == 2351) {
					item.setId(item.getId() + 1);
				}
			}
			if (getId() == 1592) {
				if (item.getId() == 2353) {
					item.setId(item.getId() + 1);
				}
			}
		}

		//Hazelmere's signet ring chance.
		boolean roll = Misc.rollDie(100);
		if ((player.getEquipment().getRingId() == 29107 || player.getEquipment().getRingId() == 28804 || player.getEquipment().getRingId() == 28803 || player.getBuffManager().hasBuff(BuffManager.BuffType.GREED_POTION) || player.getEquipment().getRingId() == 28830) && roll) { //Hazelmere's signet ring
			player.sendMessage("<col=ff8c38>The power of Hazelmere blesses your " + item.getName() + " doubling it before your very eyes!");
			World.addGroundItem(item, new Position(getCoordFaceX(size), getCoordFaceY(size), getZ()), player, true, player.getGameMode().isIronman() ? 180 : 60);
			rareDropMsg(player, dropName, item, getAsNPC());
		}

		else if (player.getGameMode().isMode(GameMode.HARD) && Misc.rollPercent(5)) {
			player.sendMessage("<col=ff8c38>Your gamemode blesses your " + item.getName() + " doubling it before your very eyes!");
			World.addGroundItem(item, new Position(getCoordFaceX(size), getCoordFaceY(size), getZ()), player, true, player.getGameMode().isIronman() ? 180 : 60);
			rareDropMsg(player, dropName, item, getAsNPC());
		}

		/**
		 * Cyclops - Defenders
		 */
		int bronzeDefender = 8844;
		int ironDefender = 8845;
		int steelDefender = 8846;
		int blackDefender = 8847;
		int mithrilDefender = 8848;
		int adamantDefender = 8849;
		int runeDefender = 8850;
		int dragonDefender = 20072;

		if (getId() == 4291 || getId() == 4292 || getId() == 6078 || getId() == 6079) {
			if (Utils.random(30) == 0) {
				if (player.getEquipment().getShieldId() == dragonDefender || player.getInventory().contains(dragonDefender) >= 1) {
					item = new Item(dragonDefender, 1);
				} else if (player.getEquipment().getShieldId() == runeDefender || player.getInventory().contains(runeDefender) >= 1) {
					item = new Item(dragonDefender, 1);
				} else if (player.getEquipment().getShieldId() == adamantDefender || player.getInventory().contains(adamantDefender) >= 1) {
					item = new Item(runeDefender, 1);
				} else if (player.getEquipment().getShieldId() == mithrilDefender || player.getInventory().contains(mithrilDefender) >= 1) {
					item = new Item(adamantDefender, 1);
				} else if (player.getEquipment().getShieldId() == blackDefender || player.getInventory().contains(blackDefender) >= 1) {
					item = new Item(mithrilDefender, 1);
				} else if (player.getEquipment().getShieldId() == steelDefender || player.getInventory().contains(steelDefender) >= 1) {
					item = new Item(blackDefender, 1);
				} else if (player.getEquipment().getShieldId() == ironDefender || player.getInventory().contains(ironDefender) >= 1) {
					item = new Item(steelDefender, 1);
				} else if (player.getEquipment().getShieldId() == bronzeDefender || player.getInventory().contains(bronzeDefender) >= 1) {
					item = new Item(ironDefender, 1);
				} else
					item = new Item(bronzeDefender, 1);
				World.addGroundItem(item, new Position(getPosition()), player, true, 60);
				Lootbeams.create(new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ()), player, Tier.SMALL);
			}
		}

		 //Kraken
		if (getId() == 16027 || getId() == 20492) {
			player.getItemCollectionManager().handleCollection(item);
			if (player.getEquipment().getAmuletId() == 28961 && Misc.rollPercent(25)) {
				player.getBank().addItem(item, true);
				player.sm(item.getName() + " has been sent to your bank");
			}
			else
			World.addGroundItem(item, new Position(player), player, true, 60);
			return;
		}
		//Zulrah
		if (getId() >= 22042 && getId() <= 22044) {
			player.getItemCollectionManager().handleCollection(item);
			if (player.getEquipment().getAmuletId() == 28961 && Misc.rollPercent(25)) {
				player.getBank().addItem(item, true);
				player.sm(item.getName() + " has been sent to your bank");
			} else
			World.addGroundItem(item, new Position(player), player, true, 60);
			return;
		}

		if (getId() >= 28058 && getId() <= 28061) {
			player.getItemCollectionManager().handleCollection(item);
			World.addGroundItem(item, this, player, true, 60);
			return;
		}

		if (dropName.contains("Bones")
				|| dropName.contains("Ashes")
				|| dropName.contains("bone")){
			sendCharms(player);
		}

		if (player.getInventory().containsItem(18337, 1)// Bonecrusher
				&& item.getDefinitions().getName().toLowerCase().contains("bones")) {
			player.getSkills().addXp(Skills.PRAYER, Burying.Bone.forId(drop.getItemId()).getExperience());
			return;
		}

		if (player.getInventory().containsItem(19675, 1)// Herbicide
				&& item.getDefinitions().getName().toLowerCase().contains("grimy")) {
			if (player.getSkills().getLevelForXp(Skills.HERBLORE) >= HerbCleaning.getHerb(item.getId()).getLevel()) {
				player.getSkills().addXp(Skills.HERBLORE, HerbCleaning.getHerb(drop.getItemId()).getExperience() * 2);
				return;
			}
		}
		/* LootShare/CoinShare */
		FriendChatsManager fc = player.getCurrentFriendChat();
		if (player.lootshareEnabled()) {
			if (fc != null) {
				CopyOnWriteArrayList<Player> players = fc.getPlayers();
				CopyOnWriteArrayList<Player> playersWithLs = new CopyOnWriteArrayList<Player>();
				for (Player p : players) {
					if (p.lootshareEnabled() && p.getRegionId() == player.getRegionId()) {
						playersWithLs.add(p);
					}
				}
				Player luckyPlayer = playersWithLs.get((int) (Math.random() * playersWithLs.size()));
				World.addGroundItem(item, new Position(getCoordFaceX(size), getCoordFaceY(size), getZ()), luckyPlayer, true, 180);
				luckyPlayer.getItemCollectionManager().handleCollection(item);
				luckyPlayer.sendMessage(String.format("<col=115b0d>You received: %sx %s.</col>", item.getAmount(), dropName));
				for (Player p : playersWithLs) {
					if (!p.equals(luckyPlayer)) {
						p.sendMessage(String.format("%s received: %sx %s.", luckyPlayer.getDisplayName(),
								item.getAmount(), dropName));
					}
				}
				return;
			}
		}
		/* End of LootShare/CoinShare */
		player.npcLog(player, item.getId(), item.getAmount(), item.getName(), getName(), getId());

		// Added to prevent items stacking
		ItemDefinitions defs = ItemDefinitions.getItemDefinitions(drop.getItemId());
		if (!defs.isStackable() && item.getAmount() > 1) {
			for (int i = 0; i < item.getAmount(); i++) {
				player.getItemCollectionManager().handleCollection(item);
				World.addGroundItem(new Item(item.getId(), 1), new Position(getCoordFaceX(size), getCoordFaceY(size), getZ()), player, true, player.getGameMode().isIronman() ? 180 : 60);
			}
		} else if (!player.isOwner()) {
			player.getItemCollectionManager().handleCollection(item);
			if (player.petManager.getNpcId() == 16116 && item.getDefinitions().value >= 10000) {
				final Projectile projectile = new Projectile(144, 50, 0, 70, 2, 30, 0);
				Position attackedTile = new Position(getCoordFaceX(size), getCoordFaceY(size), getZ());
				World.sendProjectile(player.getPet(), attackedTile, projectile);
				player.sm("<col=00FF00>" +item.getName() + " x" + item.getAmount() + " has been sent to your bank");
				player.getBank().addItem(item, true);
			} else if (player.getEquipment().getAmuletId() == 28961 && item.getDefinitions().value >= 10000) {
				final Projectile projectile = new Projectile(144, 60, 0, 70, 2, 30, 0);
				Position attackedTile = new Position(getCoordFaceX(size), getCoordFaceY(size), getZ());
				World.sendProjectile(player, attackedTile, projectile);
				player.sm("<col=00FF00>" +item.getName() + " x" + item.getAmount() + " has been sent to your bank");
				player.getBank().addItem(item, true);
			} else {
				World.addGroundItem(item, new Position(getCoordFaceX(size), getCoordFaceY(size), getZ()), player, true, player.getGameMode().isIronman() ? 180 : 60);
			}

		}
		if (player.isOwner()) {
			player.getItemCollectionManager().handleCollection(item);
			World.addGroundItem(item, new Position(getCoordFaceX(size), getCoordFaceY(size), getZ()), player, true, player.getGameMode().isIronman() ? 180 : 60);
		}

	}


	public static String[] rareDrops = { "torva", "pernix", "virtus", "staff of the dead" , "visage", "dragon full helm",
		"dragon platebody", "glaiven", "steadfast", "dragon claws", "chaotic",
		"vesta's", "Berserker ring", "bandos", "armadyl chestplate", "armadyl chainskirt",
		"armadyl helmet", "Reward Token","hilt", "sigil", "elixir", "steadfast", "glaiven",
		"ragefire", "dragon pickaxe", "dark bow", "treasonous ring", "ring of the gods", "staff of pure flame", "book of pure frost", "pegasian crystal", "primodrial crystal", "smouldering stone", "eternal crystal", "tyranical ring", "(g)", "vanguard", "vesta's", "Zuriel", "drygore",
		"3rd", "cannon base", "cannon stand", "cannon barrel", "cannon furance", "Subjugation", "Whisper","Murmur", "Hiss", "Dominion", "Dark Bow", "whip", "Mercenary", "Sagittarian",
		"Korasi", "Polypore stick", "Archer's ring", "tanzanite fang", "tanzanite", "viggora's", "thammaron", "craw's", "magma", "magic fang", "noxious", "araxxi's", "serpentine visage", "armadyl boots", "armadyl gloves", "armadyl buckler", "armadyl crossbow", "trident", "kraken", "legendary egg", "nazgul",
			"upgrade gem", "cursed shard", "ameythst scale", "lava dragonhide", "bloodied shard", "blood shard", "cinderbane", "dark ether", "icicle", "ice shard", "imbued stone", "dark stone", "venomous staff", "skeletal longsword", "arcane staff", "deathly scythe", "dark glaive",
	     "dragonbane longsword", "brutal battleaxe", "heavy frame", "light frame", "ballista spring", "zenyte shard", "ballista limbs", "occult necklace", "uncharged trident", "darkend spear", "ice shard", "icicle", "mystery box", "shark scale", "raids", "harpoon launcher", "hydra's", "dragon hunter lance", "jar of ", "tome of smoke",  "sword of justice",  "staff of sand", "smoke cloak", "unsired", "dark abyssal bludgeon", "bludegon crossbow", "abyssal dagger" };


	public static boolean rareDropMsg(Player player, String drop, Item item, NPC npc) {
		for (String dropNames : rareDrops) {
			if (drop.contains(dropNames)) {
				World.sendWorldMessage("[<img=24><col=ff8c38>Drop News] - <col=ff8c38>" + Utils.formatPlayerNameForDisplay(player.getUsername()) +
						" has received x<col=ff8c38> "+ item.getAmount() + " " + Utils.formatPlayerNameForDisplay(drop) + " as a rare drop from a " + npc.getName() + " (KC:"+ player.getKillcountManager().getKillcount(npc.getId())+ ")" +"</col>", false);
				return true;
			}
		}
		return false;
	}

	public static Item dropToItem(Drop drop) {
		Item item = null;
		if (drop.getMaxAmount() > 1) {
			item = new Item(drop.getItemId(), drop.getMinAmount() + Utils.getRandom(drop.getMaxAmount()));
		} else {
			item = new Item(drop.getItemId(), 1);
		}
		return item;
	}

	public static void sendDropDirectlyToBank(Player player, Drop drop) {
		Item item = null;
		if (drop.getMaxAmount() > 1) {
			item = new Item(drop.getItemId(), drop.getMinAmount() + Utils.getRandom(drop.getMaxAmount()));
		} else {
			item = new Item(drop.getItemId(), 1);
		}
		player.getBank().addItem(item.getId(), item.getAmount(), true);
	}

	@Override
	public int getSize() {
		return getDefinitions().size;
	}

	public int getMaxHit() {
		return getCombatDefinitions().getMaxHit();
	}

	public int[] getBonuses() {
		return bonuses;
	}

	@Override
	public double getMagePrayerMultiplier() {
		return 0.0;
	}

	@Override
	public double getRangePrayerMultiplier() {
		return 0.0;
	}

	@Override
	public double getMeleePrayerMultiplier() {
		return 0.0;
	}

	public Position getSpawnPosition() {
		return spawnPosition;
	}

	public boolean isUnderCombat() {
		return combat.underCombat();
	}

	@Override
	public void setAttackedBy(Entity target) {
		super.setAttackedBy(target);
		if (target == combat.getTarget() && !(combat.getTarget() instanceof Familiar)) {
			lastAttackedByTarget = Utils.currentTimeMillis();
		}
	}

	public int getAttackStyle() {
		if (bonuses[2] > 0)
			return NPCCombatDefinitions.MAGE;
		if (bonuses[1] > 0)
			return NPCCombatDefinitions.RANGE;
		return NPCCombatDefinitions.MELEE;
	}

	public boolean canBeAttackedByAutoRelatie() {
		return Utils.currentTimeMillis() - lastAttackedByTarget > lureDelay;
	}

	public boolean isForceWalking() {
		return forceWalk != null;
	}

	public void setTarget(Entity entity) {
		if (isForceWalking()) {
			return;
		}
		combat.setTarget(entity);
		lastAttackedByTarget = Utils.currentTimeMillis();
	}

	public void removeTarget() {
		if (combat.getTarget() == null) {
			return;
		}
		combat.removeTarget();
	}

	public void forceWalkRespawnTile() {
		setForceWalk(spawnPosition);
	}

	public void setForceWalk(Position tile) {
		resetWalkSteps();
		forceWalk = tile;
	}

	public boolean hasForceWalk() {
		return forceWalk != null;
	}

	public ArrayList<Entity> getPossibleTargets() {
		ArrayList<Entity> possibleTarget = new ArrayList<Entity>();
		for (int regionId : getMapRegionsIds()) {
			List<Integer> playerIndexes = World.getRegion(regionId).getPlayerIndexes();
			if (playerIndexes != null) {
				for (int playerIndex : playerIndexes) {
					Player player = World.getPlayers().get(playerIndex);
					if (player == null || player.isDead() || player.isFinished() || !player.isRunning()
							|| !player.withinDistance(this, forceTargetDistance > 0 ? forceTargetDistance
									: getCombatDefinitions().getAttackStyle() == NPCCombatDefinitions.MELEE ? 4
											: getCombatDefinitions().getAttackStyle() == NPCCombatDefinitions.RANGE_FOLLOW
											? 64 : 8)
											|| !forceMultiAttacked && (!isAtMultiArea() || !player.isAtMultiArea())
											&& player.getAttackedBy() != this
											&& (player.getAttackedByDelay() > Utils.currentTimeMillis()
													|| player.getFindTargetDelay() > Utils.currentTimeMillis())
													|| !clipedProjectile(player, false)
													|| (!player.getBuffManager().hasBuff(BuffManager.BuffType.AGGRESSION_POTION) && !getDefinitions().hasAttackOption()) && !forceAgressive && !Wilderness.isAtWild(this) && immunity(player)) {
						continue;
					}
					possibleTarget.add(player);
				}
			}
		}
		return possibleTarget;
	}

	public ArrayList<Player> localPlayers() {
		ArrayList<Player> players = Lists.newArrayList();
		for (int regionId : getMapRegionsIds()) {
			List<Integer> indices = World.getRegion(regionId).getPlayerIndexes();
			if (indices != null) {
				for (int index : indices) {
					Player player = World.getPlayers().get(index);
					if (player != null && !player.isDead() && !player.isFinished()) {
						players.add(player);
					}
				}
			}
		}
		return players;
	}

	public boolean immunity(Player player) {
		if (player.getSkills().getCombatLevelWithSummoning() >= getCombatLevel() * 2) {
			return true;
		} else {
			return false;
		}
	}

	public boolean checkAgressivity() {
		List<Entity> possibleTarget = getPossibleTargets().stream().filter(entity -> {
			if (entity instanceof Player) {
				Player player = (Player) entity;
				if (player.getBuffManager().hasBuff(BuffManager.BuffType.AGGRESSION_POTION) && getDefinitions().hasAttackOption()) {
					return true;
				}

			}
			if (!forceAgressive) {
				NPCCombatDefinitions defs = getCombatDefinitions();
				if (defs.getAgressivenessType() == NPCCombatDefinitions.PASSIVE) {
					return false;
				}
			}
			return true;
		}).collect(Collectors.toList());

		if (!possibleTarget.isEmpty()) {
			Entity target = possibleTarget.get(Utils.random(possibleTarget
					.size()));
			setTarget(target);
			target.setAttackedBy(target);
			target.setFindTargetDelay(Utils.currentTimeMillis() + 10000);
			return true;
		}
		return false;
	}

	public boolean isCantInteract() {
		return cantInteract;
	}

	public void setCantInteract(boolean cantInteract) {
		this.cantInteract = cantInteract;
		if (cantInteract) {
			combat.reset();
		}
	}

	public int getCapDamage() {
		return capDamage;
	}

	public void setDamageCap(int capDamage) {
		this.capDamage = capDamage;
	}

	public int getLureDelay() {
		return lureDelay;
	}

	public void setLureDelay(int lureDelay) {
		this.lureDelay = lureDelay;
	}

	public boolean isCantFollowUnderCombat() {
		return cantFollowUnderCombat;
	}

	public void setCantFollowUnderCombat(boolean canFollowUnderCombat) {
		cantFollowUnderCombat = canFollowUnderCombat;
	}

	public Transformation getNextTransformation() {
		return nextTransformation;
	}

	@Override
	public String toString() {
		return getDefinitions().name + " - " + id + " - " + getX() + " " + getY() + " " + getZ();
	}

	public boolean isForceAgressive() {
		return forceAgressive;
	}

	public void setForceAgressive(boolean forceAgressive) {
		this.forceAgressive = forceAgressive;
	}

	public int getForceTargetDistance() {
		return forceTargetDistance;
	}

	public void setForceTargetDistance(int forceTargetDistance) {
		this.forceTargetDistance = forceTargetDistance;
	}

	public boolean isForceFollowClose() {
		return forceFollowClose;
	}

	public void setForceFollowClose(boolean forceFollowClose) {
		this.forceFollowClose = forceFollowClose;
	}

	public boolean isForceMultiAttacked() {
		return forceMultiAttacked;
	}

	public void setForceMultiAttacked(boolean forceMultiAttacked) {
		this.forceMultiAttacked = forceMultiAttacked;
	}

	public boolean hasRandomWalk() {
		return randomwalk;
	}

	public void setRandomWalk(boolean forceRandomWalk) {
		randomwalk = forceRandomWalk;
	}

	public String getCustomName() {
		return name;
	}

	public void setName(String string) {
		name = getDefinitions().name.equals(string) ? null : string;
		changedName = true;
	}

	public int getCustomCombatLevel() {
		return combatLevel;
	}

	public int getCombatLevel() {
		return combatLevel >= 0 ? combatLevel : getDefinitions().combatLevel;
	}

	public String getName() {
		return name != null ? name : getDefinitions().name;
	}

	public String getName2() {
		return getDefinitions().name != null ? getDefinitions().name : name;
	}

	public void setCombatLevel(int level) {
		combatLevel = getDefinitions().combatLevel == level ? -1 : level;
		changedCombatLevel = true;
	}

	public boolean hasChangedName() {
		return changedName;
	}

	public boolean hasChangedCombatLevel() {
		return changedCombatLevel;
	}

	public Position getMiddleWorldTile() {
		int size = getSize();
		return new Position(getCoordFaceX(size), getCoordFaceY(size), getZ());
	}

	public boolean isSpawned() {
		return spawned;
	}

	public void setSpawned(boolean spawned) {
		this.spawned = spawned;
	}

	public boolean isNoDistanceCheck() {
		return noDistanceCheck;
	}

	public void setNoDistanceCheck(boolean noDistanceCheck) {
		this.noDistanceCheck = noDistanceCheck;
	}

	public boolean withinDistance(Player tile, int distance) {
		return super.withinDistance(tile, distance);
	}

	/**
	 * Gets the locked.
	 *
	 * @return The locked.
	 */
	public boolean isLocked() {
		return locked;
	}

	/**
	 * Sets the locked.
	 *
	 * @param locked
	 *            The locked to set.
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public SecondaryBar getNextSecondaryBar() {
		return nextSecondaryBar;
	}

	public void setNextSecondaryBar(SecondaryBar secondaryBar) {
		nextSecondaryBar = secondaryBar;
	}

	public ArrayList<Entity> getPossibleTargets(boolean checkNPCs, boolean checkPlayers) {
		int size = getSize();
		// int agroRatio = getCombatDefinitions().getAgroRatio();
		ArrayList<Entity> possibleTarget = new ArrayList<Entity>();
		for (int regionId : getMapRegionsIds()) {
			if (checkPlayers) {
				List<Integer> playerIndexes = World.getRegion(regionId).getPlayerIndexes();
				if (playerIndexes != null) {
					for (int playerIndex : playerIndexes) {
						Player player = World.getPlayers().get(playerIndex);
						if (player == null || player.isDead() || player.isFinished() || !player.isRunning()
								|| player.getAppearence().isHidden()
								|| !Utils.isOnRange(getX(), getY(), size, player.getX(), player.getY(),
										player.getSize(), forceTargetDistance)
										|| !forceMultiAttacked && (!isAtMultiArea() || !player.isAtMultiArea())
										&& player.getAttackedBy() != this
										&& (player.getAttackedByDelay() > Utils.currentTimeMillis()
												|| player.getFindTargetDelay() > Utils.currentTimeMillis())
												|| !clipedProjectile(player, false) || !forceAgressive && !Wilderness.isAtWild(this)
												&& player.getSkills().getCombatLevelWithSummoning() >= getCombatLevel() * 2) {
							continue;
						}
						possibleTarget.add(player);
					}
				}
			}
			if (checkNPCs) {
				List<Integer> npcsIndexes = World.getRegion(regionId).getNPCsIndexes();
				if (npcsIndexes != null) {
					for (int npcIndex : npcsIndexes) {
						NPC npc = World.getNPCs().get(npcIndex);
						if (npc == null || npc == this || npc.isDead() || npc.isFinished()
								|| !Utils.isOnRange(getX(), getY(), size, npc.getX(), npc.getY(), npc.getSize(),
										forceTargetDistance)
										|| !npc.getDefinitions().hasAttackOption()
										|| (!isAtMultiArea() || !npc.isAtMultiArea()) && npc.getAttackedBy() != this
										&& npc.getAttackedByDelay() > Utils.currentTimeMillis()
										|| !clipedProjectile(npc, false)) {
							continue;
						}
						possibleTarget.add(npc);
					}
				}
			}
		}
		return possibleTarget;
	}

	public int getAttackSpeed() {
		Map<Integer, Object> data = getDefinitions().params;
		if (data != null) {
			Integer speed = (Integer) data.get(14);
			if (speed != null)
				return speed;
		}
		return 4;
	}

	public void lowerDefence(float multiplier) {
		if (bonuses != null) {
			int[] realBonuses = NPCBonuses.getBonuses(id);
			if (realBonuses != null) {
				if (bonuses[5] > realBonuses[5] / 3) {
					bonuses[5] -= bonuses[5] * multiplier;
				}
				if (bonuses[6] > realBonuses[6] / 3) {
					bonuses[6] -= bonuses[6] * multiplier;
				}
				if (bonuses[7] > realBonuses[7] / 3) {
					bonuses[7] -= bonuses[7] * multiplier;
				}
				if (bonuses[8] > realBonuses[8] / 3) {
					bonuses[8] -= bonuses[8] * multiplier;
				}
				if (bonuses[9] > realBonuses[9] / 3) {
					bonuses[9] -= bonuses[9] * multiplier;
				}
			}
		}
	}

	public void setCapDamage(int capDamage) {
		this.capDamage = capDamage;
	}

	public static int asOSRS(int id) {
		return id + Constants.OSRS_NPCS_OFFSET;
	}

	public boolean isAt(Position position) {
		return isAt(position.getX(), position.getY());
	}

	public boolean isAt(int x, int y) {
		return getPosition().getX() == x && getPosition().getY() == y;
	}

	@Setter @Getter private int attackDistance = 7; //default to 7 tiles

	@Getter private ClickOptionListener[] clickOptionListener = new ClickOptionListener[5];

	public void setClickOptionListener(int option, ClickOptionListener listener) {
		this.clickOptionListener[option - 1] = listener;
	}

}
