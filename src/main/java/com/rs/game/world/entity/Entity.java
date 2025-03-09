package com.rs.game.world.entity;

import com.google.common.collect.Lists;
import com.rs.Constants;
import com.rs.cache.loaders.AnimationDefinitions;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.cores.coroutines.CoroutineEvent;
import com.rs.cores.coroutines.CoroutineWorker;
import com.rs.game.map.*;
import com.rs.game.world.World;
import com.rs.game.world.entity.listeners.*;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.NpcTypes;
import com.rs.game.world.entity.npc.familiar.Familiar;
import com.rs.game.world.entity.npc.qbd.TorturedSoul;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.Prayer;
import com.rs.game.world.entity.player.actions.Action;
import com.rs.game.world.entity.player.actions.PlayerCombat;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.player.content.skills.magic.Magic;
import com.rs.game.world.entity.player.content.toxin.ToxinManager;
import com.rs.game.world.entity.player.controller.impl.Cages;
import com.rs.game.world.entity.updating.impl.*;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Entity extends Position {

	private transient boolean cantWalk;

	public Position getPosition() {
		return new Position(getX(), getY(), getZ());
	}

	public void sm(String message) {
		Player.getPackets1().sendGameMessage(message);
	}
	
	public Position getMiddleTile() {
		return new Position(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getZ());
	}
	
	@Getter public Prayer prayer;

	public int getDamageReceived(Player source) {
		Integer d = receivedDamage.get(source);
		if (d == null || source.isFinished()) {
			receivedDamage.remove(source);
			return -1;
		}
		return d;
	}

	public Player getMostDamageCages() {
		Player player = null;
		int damage = -1;
		for (Entity source : receivedDamage.keySet()) {
			if (!(source instanceof Player)) {
				continue;
			}
			Integer d = receivedDamage.get(source);
			Player p2 = (Player) source;
			if (d == null
					|| source.isFinished()
					|| this == source
					|| !(p2.getControlerManager().getControler() instanceof Cages)) {
				receivedDamage.remove(source);
				continue;
			}
			if (d > damage) {
				player = (Player) source;
				damage = d;
			}
		}
		return player;
	}

	private static final long serialVersionUID = -3372926325008880753L;
	private final static AtomicInteger hashCodeGenerator = new AtomicInteger();


	// transient stuff
	private transient int index;
	private transient int lastRegionId; // the last region the entity was at
	private transient Position lastLoadedMapRegionTile;
	private transient CopyOnWriteArrayList<Integer> mapRegionsIds; // called by
	// more than
	// 1thread
	// so
	// concurent
	private transient int direction;
	private transient Position lastPosition;
	private Position lastLocation;
	private transient Position nextPosition;
	private transient int nextWalkDirection;
	private transient int nextRunDirection;
	private transient Position nextFacePosition;
	private transient boolean teleported;
	private transient ConcurrentLinkedQueue<int[]> walkSteps;// called by more
	// than 1thread
	// so concurent
	private transient ConcurrentLinkedQueue<Hit> receivedHits;
	@Getter private transient Map<Entity, Integer> receivedDamage;
	private transient boolean finished; // if removed
	private transient long freezeDelay;
	private transient long Armorbreakdelay;
	// entity masks
	private transient Animation nextAnimation;
	private transient Graphics nextGraphics1;
	private transient Graphics nextGraphics2;
	private transient Graphics nextGraphics3;
	private transient Graphics nextGraphics4;
	private transient ArrayList<Hit> nextHits;
	private transient ForceMovement nextForceMovement;
	private transient ForceTalk nextForceTalk;
	private transient int nextFaceEntity;
	private transient int lastFaceEntity;
	private transient Entity attackedBy; // whos attacking you, used for single
	private transient long attackedByDelay; // delay till someone else can
	// attack you
	private transient boolean multiArea;
	private transient boolean morytaniaArea;
	private transient boolean smokeyArea;
	private transient boolean underwater;
	private transient boolean desertArea;
	private transient boolean sinkArea;
	private transient boolean isAtDynamicRegion;
	private transient long lastAnimationEnd;
	private transient boolean forceMultiArea;
	private transient boolean forceMorytaniaArea;
	private transient boolean forceSmokeyArea;
	private transient boolean forceUnderwaterArea;
	private transient boolean forceDesertArea;
	private transient boolean forceSinkArea;
	private transient long frozenBlocked;
	private transient long findTargetDelay;
	private transient ConcurrentHashMap<Object, Object> temporaryAttributes;
	private transient int hashCode;
	@Setter @Getter private transient boolean canBeFrozen = true;

	// saving stuff
	private int hitpoints;
	private int mapSize; // default 0, can be setted other value usefull on
	// static maps
	private boolean run;
	@Getter private ToxinManager toxin;
	@Getter @Setter private boolean invincible;
	
	private long locked_ticks;

	// creates Entity and saved classes
	public Entity(Position tile) {
		super(tile);
		toxin = new ToxinManager();
	}

	public ConcurrentHashMap<Object, Object> getAttributes() {
		return temporaryAttributes;
	}
	
	public void lock() {
		lock(Integer.MAX_VALUE);
	}
	
	public void lock(int ticks) {
		locked_ticks = ticks;
	}
	
	public void unlock() {
		locked_ticks = 0;
	}
	
	public boolean isLocked() {
		return locked_ticks > 0;
	}

	@Override
	public int hashCode() {
		return hashCode;
	}

	public boolean isFrozen() {
		return freezeDelay >= Utils.currentTimeMillis();
	}

	public boolean inArea(int a, int b, int c, int d) {
		return getX() >= a && getY() >= b && getX() <= c && getY() <= d;
	}

	public int getFaceWorldTile(Position nextFacePosition) {
		if (nextFacePosition.getX() == getX()
				&& nextFacePosition.getY() == getY()) {
			return 0;
		}
		this.nextFacePosition = nextFacePosition;
		if (nextPosition != null) {
			return Utils.getFaceDirection(nextFacePosition.getX()
					- nextPosition.getX(), nextFacePosition.getY()
					- nextPosition.getY());
		} else {
			return Utils.getFaceDirection(nextFacePosition.getX() - getX(),
					nextFacePosition.getY() - getY());
		}
	}


	public final void initEntity() {
		hashCode = hashCodeGenerator.getAndIncrement();
		mapRegionsIds = new CopyOnWriteArrayList<Integer>();
		walkSteps = new ConcurrentLinkedQueue<int[]>();
		receivedHits = new ConcurrentLinkedQueue<Hit>();
		receivedDamage = new ConcurrentHashMap<Entity, Integer>();
		temporaryAttributes = new ConcurrentHashMap<Object, Object>();
		nextHits = new ArrayList<Hit>();
		nextWalkDirection = nextRunDirection - 1;
		lastFaceEntity = -1;
		nextFaceEntity = -2;
		toxin.setEntity(this);
	}

	public int getClientIndex() {
		return index + (this instanceof Player ? 32768 : 0);
	}

	public void move(Position nextPosition) {
		this.nextPosition = nextPosition;
	}

	public void applyHit(Hit hit) {
		if (isDead()) {
			return;
		}
		Entity source = hit.getSource();
		if (source != null)
			hit = source.handleOutgoingHit(hit, this);
		// todo damage for who gets drop
		final int original = hit.getDamage();
		hit.setDamage(original);
		if (!isInvincible()) {
			receivedHits.add(hit);
			handleIngoingHit(hit);
		}
	}

	public int index() {
		return index;
	}

	public Entity index(int index) {
		this.index = index;
		return this;
	}

	public NPC getMostDamageReceivedSourceNPC() {
		NPC player = null;
		int damage = -1;
		for (Entity entity : receivedDamage.keySet()) {
			Entity source = entity;
			if (source instanceof NPC) {
				Integer d = receivedDamage.get(source);
				if (d != null && d.intValue() > damage) {
					player = (NPC) source;
					damage = d.intValue();
				}
			}
		}
		return player;
	}

	public abstract void handleIngoingHit(Hit hit);

	public void reset(boolean attributes) {
		setHitpoints(getMaxHitpoints());
		receivedHits.clear();
		resetCombat();
		walkSteps.clear();
		toxin.reset();
		cantWalk = false;
		resetReceivedDamage();
		if (attributes) {
			temporaryAttributes.clear();
		}
	}

	public void reset() {
		reset(true);
	}

	public void resetCombat() {
		attackedBy = null;
		attackedByDelay = 0;
		freezeDelay = 0;
		Armorbreakdelay = 0;
	}

	public void resetTarget() {
		if (isPlayer()) {
			getAsPlayer().getActionManager().forceStop();
			attackedBy = null;
		} else {
			getAsNPC().getCombat().removeTarget();
		}
	}

	public void processReceivedHits() {
		if (this instanceof Player) {
			if (((Player) this).getEmotesManager().getNextEmoteEnd() >= Utils
					.currentTimeMillis()) {
				return;
			}
		}
		Hit hit;
		int count = 0;
		while ((hit = receivedHits.poll()) != null && count++ < 10) {
			processHit(hit);
		}
	}

	private void processHit(Hit hit) {
		if (isDead()) {
			return;
		}
		removeHitpoints(hit);
		nextHits.add(hit);
	}

	public void removeHitpoints(Hit hit) {
		if (isDead() || hit.getLook() == HitLook.ABSORB_DAMAGE) {
			return;
		}
		if (hit.getLook() == HitLook.HEALED_DAMAGE) {
			heal(hit.getDamage());
			return;
		}
		if (hit.getLook() == HitLook.PRAYER_DAMAGE) {
			getPrayer().restorePrayer(hit.getDamage());
			return;
		}
		if (Armorbreakdelay > 1) {
			hit.look(HitLook.ARMOR_BREAK);
		}

		if (hit.getDamage() > hitpoints) {
			hit.setDamage(hitpoints);
		}
		addReceivedDamage(hit.getSource(), hit.getDamage() * 10);
		setHitpoints(hitpoints - hit.getDamage());
		if (hit.getSource() != null && hit.getSource().isPlayer() && hit.getSource() != this) {
			hit.getSource().getAsPlayer().getPackets().sendRunScript(-3, getHitpoints(), getMaxHitpoints());
			hit.getSource().getAsPlayer().getPackets().sendRunScript(-4, isNPC() ? getAsNPC().getName() : getAsPlayer().getDisplayName());
		}
		if (hitListener != null && hitListener.postDamage != null)
			hitListener.postDamage.accept(hit);
		if (hit.getSource() != null && hit.getSource().hitListener != null && hit.getSource().hitListener.postTargetDamage != null)
			hit.getSource().hitListener.postTargetDamage.accept(hit, hit.getSource());
		if (hitpoints <= 0) {
			if (hitpoints <= 0) {
				sendDeath(hit.getSource());
				if (hit.getSource() != null && hit.getSource().isPlayer())
					hit.getSource().getAsPlayer().getPackets().sendRunScript(-3, 0);
			} else if (this instanceof Player) {
				Player player = (Player) this;
				if (player.getEquipment().getRingId() == 2550 || player.getEquipment().getRingId() == 28830 || player.getEquipment().getRingId() == 28804) {
					if (hit.getSource() != null && hit.getSource() != player) {
						hit.getSource().applyHit(
								new Hit(player, (int) (hit.getDamage() * 0.1),
										HitLook.REFLECTED_DAMAGE));
					}
				}
				if (player.getPrayer().hasPrayersOn()) {
					if (hitpoints < player.getMaxHitpoints() * 0.1
							&& player.getPrayer().usingPrayer(0, 23)) {
						setNextGraphics(new Graphics(436));
						setHitpoints((int) (hitpoints + player.getSkills().getLevelForXp(Skills.PRAYER) * 2.5));
						player.getSkills().set(Skills.PRAYER, 0);
						player.getPrayer().setPrayerpoints(0);
					} else if (player.getEquipment().getAmuletId() != 11090
							&& player.getEquipment().getRingId() == 11090
							&& player.getHitpoints() <= player.getMaxHitpoints() * 0.1) {
						Magic.sendNormalTeleportSpell(player, 1, 0,
								Constants.RESPAWN_PLAYER_LOCATION[0]);
						player.getEquipment().deleteItem(11090, 1);
						player.getPackets()
						.sendGameMessage(
								"Your ring of life saves you, but is destroyed in the process.");
					}
				}
				if (player.getEquipment().wearingSkillCape(Skills.DEFENCE) && 
						player.getHitpoints() <= player.getMaxHitpoints() * 0.1 && 
							player.isDefenceCapeOffCooldown()) {
					Magic.sendNormalTeleportSpell(player, 1, 0, Constants.RESPAWN_PLAYER_LOCATION[0]);
					player.getPackets()
					.sendGameMessage("Your defence cape saves you from dying. You cape is now on cooldown for 1 day.");
					player.defenceCapeCooldown = System.currentTimeMillis();		
				}
				if (player.getEquipment().getAmuletId() == 11090
						&& player.getHitpoints() <= player.getMaxHitpoints() * 0.2) {// priority
					// over
					// ring
					// of
					// life
					player.heal((int) (player.getMaxHitpoints() * 0.3));
					player.getEquipment().deleteItem(11090, 1);
					player.getPackets()
					.sendGameMessage(
							"Your pheonix necklace heals you, but is destroyed in the process.");
				}
			}
		}
	}

	public void resetReceivedDamage() {
		receivedDamage.clear();
	}

	public void removeDamage(Entity entity) {
		receivedDamage.remove(entity);
	}
	
	public Player getMostDamageReceivedSourcePlayer() {
		Player player = null;
		int damage = -1;
		for (Entity source : receivedDamage.keySet()) {
			if (!(source instanceof Player)) {
				continue;
			}
			Integer d = receivedDamage.get(source);
			if (d == null || source.isFinished()) {
				receivedDamage.remove(source);
				continue;
			}
			if (d > damage) {
				player = (Player) source;
				damage = d;
			}
		}
		return player;
	}

	public void processReceivedDamage() {
		for (Entity source : receivedDamage.keySet()) {
			Integer damage = receivedDamage.get(source);
			if (damage == null || source.isFinished()) {
				receivedDamage.remove(source);
				continue;
			}
			damage--;
			if(damage == 0) {
				receivedDamage.remove(source);
				continue;
			}
			receivedDamage.put(source, damage);
		}
	}

	public void addReceivedDamage(Entity source, int amount) {
		if (source == null) {
			return;
		}
		Integer damage = receivedDamage.get(source);
		damage = damage == null ? amount : damage + amount;
		if(damage < 0) {
			receivedDamage.remove(source);
		} else {
			receivedDamage.put(source, damage);
		}
	}

	public void heal(int ammount) {
		if (ammount > getMaxHitpoints()) {
			heal(getMaxHitpoints(), 0);
			return;
		}
		heal(ammount, 0);
	}

	public void heal(int ammount, int extra) {
		if (ammount > getMaxHitpoints()) {
			setHitpoints(getMaxHitpoints());
			return;
		}
		setHitpoints(hitpoints + ammount >= getMaxHitpoints() + extra ? getMaxHitpoints() + extra : hitpoints + ammount);
	}

	public boolean hasWalkSteps() {
		return !walkSteps.isEmpty();
	}

	public Position lastLocation() {
		return lastLocation;
	}

	public abstract void sendDeath(Entity source);

	public void processMovement() {
		lastPosition = new Position(this);
		if (lastFaceEntity >= 0) {
			Entity target = lastFaceEntity >= 32768 ? World.getPlayers().get(
					lastFaceEntity - 32768) : World.getNPCs().get(
							lastFaceEntity);
					if (target != null) {
						direction = Utils.getFaceDirection(
								target.getCoordFaceX(target.getSize()) - getX(),
								target.getCoordFaceY(target.getSize()) - getY());
					}
		}
		nextWalkDirection = nextRunDirection = -1;
		if (nextPosition != null) {
			int lastPlane = getZ();
			setLocation(nextPosition);
			nextPosition = null;
			teleported = true;
			if (this instanceof Player && ((Player) this).getTemporaryMoveType() == -1) {
				((Player) this).setTemporaryMoveType(Player.TELE_MOVE_TYPE);
			}
			World.updateEntityRegion(this);
			if (needMapUpdate()) {
				loadMapRegions();
			} else if (this instanceof Player && lastPlane != getZ()) {
				((Player) this).setClientHasntLoadedMapRegion();
			}
			resetWalkSteps();
			return;
		}
		teleported = false;
		if (walkSteps.isEmpty()) {
			return;
		}
		if (this instanceof Player) {
			if (((Player) this).getEmotesManager().getNextEmoteEnd() >= Utils
					.currentTimeMillis()) {
				return;
			}
		}
		if (this instanceof TorturedSoul) {
			if (((TorturedSoul) this).switchWalkStep()) {
				return;
			}
		}
		nextWalkDirection = getNextWalkStep();
		if (nextWalkDirection != -1) {
			if (this instanceof Player) {
				if (!((Player) this).getControlerManager().canMove(
						nextWalkDirection)) {
					nextWalkDirection = -1;
					resetWalkSteps();
					return;
				}
			}
			moveLocation(Utils.DIRECTION_DELTA_X[nextWalkDirection],
					Utils.DIRECTION_DELTA_Y[nextWalkDirection], 0);
			if (run) {
				if (this instanceof Player
						&& ((Player) this).getRunEnergy() <= 0) {
					setRun(false);
				} else {
					nextRunDirection = getNextWalkStep();
					if (nextRunDirection != -1) {
						if (this instanceof Player) {
							Player player = (Player) this;
							if (!player.getControlerManager().canMove(
									nextRunDirection)) {
								nextRunDirection = -1;
								resetWalkSteps();
								return;
							}
							if (!player.runBath) {
								player.drainRunEnergy();
							}
						}
						moveLocation(Utils.DIRECTION_DELTA_X[nextRunDirection],
								Utils.DIRECTION_DELTA_Y[nextRunDirection], 0);
					} else if (this instanceof Player) {
						((Player) this)
						.setTemporaryMoveType(Player.WALK_MOVE_TYPE);
					}
				}
			}
		}
		World.updateEntityRegion(this);
		if (needMapUpdate()) {
			loadMapRegions();
		}
	}

	@Override
	public void moveLocation(int xOffset, int yOffset, int planeOffset) {
		super.moveLocation(xOffset, yOffset, planeOffset);
		direction = Utils.getFaceDirection(xOffset, yOffset);
	}

	private boolean needMapUpdate() {
		int lastMapRegionX = lastLoadedMapRegionTile.getChunkX();
		int lastMapRegionY = lastLoadedMapRegionTile.getChunkY();
		int regionX = getChunkX();
		int regionY = getChunkY();
		int size = (Constants.MAP_SIZES[mapSize] >> 3) / 2 - 1;
		return Math.abs(lastMapRegionX - regionX) >= size
				|| Math.abs(lastMapRegionY - regionY) >= size;
	}

	public boolean addWalkSteps(int destX, int destY) {
		return addWalkSteps(destX, destY, -1);
	}

	/*
	 * returns if cliped
	 */
	public boolean clipedProjectile(Position tile, boolean checkClose) {
		if(tile instanceof NPC) {
			NPC n = (NPC) tile;
			if (this instanceof Player) {
				return n.clipedProjectile(this, checkClose);
			}
			tile = n.getMiddleWorldTile();
		}else if (tile instanceof Player && this instanceof Player){
			Player p = (Player) tile;
			return clipedProjectile(tile, checkClose, 1) || p.clipedProjectile(this, checkClose, 1);
		}
		return clipedProjectile(tile, checkClose, 1); // size 1 thats arrow
		// size, the tile has to
		// be target center
		// coord not base
	}

	/*
	 * return added all steps
	 */
	public boolean checkWalkStepsInteract(int fromX, int fromY,
			final int destX, final int destY, int maxStepsCount, int size,
			boolean calculate) {
		int[] lastTile = new int[] { fromX, fromY };
		int myX = lastTile[0];
		int myY = lastTile[1];
		int stepCount = 0;
		while (true) {
			stepCount++;
			int myRealX = myX;
			int myRealY = myY;

			if (myX < destX) {
				myX++;
			} else if (myX > destX) {
				myX--;
			}
			if (myY < destY) {
				myY++;
			} else if (myY > destY) {
				myY--;
			}
			if (!checkWalkStep(myX, myY, lastTile[0], lastTile[1], true)) {
				if (!calculate) {
					return false;
				}
				myX = myRealX;
				myY = myRealY;
				int[] myT = checkcalculatedStep(myRealX, myRealY, destX, destY,
						lastTile[0], lastTile[1], size);
				if (myT == null) {
					return false;
				}
				myX = myT[0];
				myY = myT[1];
			}
			int distanceX = myX - destX;
			int distanceY = myY - destY;
			if (!(distanceX > size || distanceX < -1 || distanceY > size || distanceY < -1)) {
				return true;
			}
			if (stepCount == maxStepsCount) {
				return true;
			}
			lastTile[0] = myX;
			lastTile[1] = myY;
			if (lastTile[0] == destX && lastTile[1] == destY) {
				return true;
			}
		}
	}

	public int[] checkcalculatedStep(int myX, int myY, int destX, int destY,
			int lastX, int lastY, int size) {
		if (myX < destX) {
			myX++;
			if (!checkWalkStep(myX, myY, lastX, lastY, true)) {
				myX--;
			} else if (!(myX - destX > size || myX - destX < -1
					|| myY - destY > size || myY - destY < -1)) {
				if (myX == lastX || myY == lastY) {
					return null;
				}
				return new int[] { myX, myY };
			}
		} else if (myX > destX) {
			myX--;
			if (!checkWalkStep(myX, myY, lastX, lastY, true)) {
				myX++;
			} else if (!(myX - destX > size || myX - destX < -1
					|| myY - destY > size || myY - destY < -1)) {
				if (myX == lastX || myY == lastY) {
					return null;
				}
				return new int[] { myX, myY };
			}
		}
		if (myY < destY) {
			myY++;
			if (!checkWalkStep(myX, myY, lastX, lastY, true)) {
				myY--;
			} else if (!(myX - destX > size || myX - destX < -1
					|| myY - destY > size || myY - destY < -1)) {
				if (myX == lastX || myY == lastY) {
					return null;
				}
				return new int[] { myX, myY };
			}
		} else if (myY > destY) {
			myY--;
			if (!checkWalkStep(myX, myY, lastX, lastY, true)) {
				myY++;
			} else if (!(myX - destX > size || myX - destX < -1
					|| myY - destY > size || myY - destY < -1)) {
				if (myX == lastX || myY == lastY) {
					return null;
				}
				return new int[] { myX, myY };
			}
		}
		if (myX == lastX || myY == lastY) {
			return null;
		}
		return new int[] { myX, myY };
	}

	/*
	 * returns if cliped
	 */
	public boolean clipedProjectile(Position tile, boolean checkClose, int size) {
		int myX = getX();
		int myY = getY();
		if(this instanceof NPC && size == 1) {
			NPC n = (NPC) this;
			Position thist = n.getMiddleWorldTile();
			myX = thist.getX();
			myY = thist.getY();
		}
		int destX = tile.getX();
		int destY = tile.getY();
		int lastTileX = myX;
		int lastTileY = myY;
		while (true) {
			if (myX < destX) {
				myX++;
			} else if (myX > destX) {
				myX--;
			}
			if (myY < destY) {
				myY++;
			} else if (myY > destY) {
				myY--;
			}
			int dir = Utils.getMoveDirection(myX - lastTileX, myY - lastTileY);
			if (dir == -1) {
				return false;
			}
			if (checkClose) {
				if (!World.checkWalkStep(getZ(), lastTileX, lastTileY, dir,
						size)) {
					return false;
				}
			} else if (!World.checkProjectileStep(getZ(), lastTileX,
					lastTileY, dir, size)) {
				return false;
			}
			lastTileX = myX;
			lastTileY = myY;
			if (lastTileX == destX && lastTileY == destY) {
				return true;
			}
		}
	}

	public boolean addWalkStepsInteract(int destX, int destY,
			int maxStepsCount, int size, boolean calculate) {
		return addWalkStepsInteract(destX, destY, maxStepsCount, size, size,
				calculate);
	}

	public boolean canWalkNPC(int toX, int toY) {
		return canWalkNPC(toX, toY, false);
	}

	private int getPreviewNextWalkStep() {
		int step[] = walkSteps.poll();
		if (step == null) {
			return -1;
		}
		return step[0];
	}

	public boolean canWalkNPC(int toX, int toY, boolean checkUnder) {
		if (!isAtMultiArea() /* || (!checkUnder && !canWalkNPC(getX(), getY(), true)) */) {
			return true;
		}
		int size = getSize();
		boolean ignoreOthers = this instanceof NPC ? Utils.ignoresCollision(getAsNPC().getId()) : false;
		for (int regionId : getMapRegionsIds()) {
			List<Integer> npcIndexes = World.getRegion(regionId).getNPCsIndexes();
			if (npcIndexes != null) {
				for (int npcIndex : npcIndexes) {
					NPC target = World.getNPCs().get(npcIndex);
					if (target == null || target == this || target.isDead() || target.isFinished()
							|| target.getZ() != getZ() || !target.isAtMultiArea()
							|| !(this instanceof Familiar) && target instanceof Familiar) {
						continue;
					}
					int targetSize = target.getSize();
					if (!checkUnder && target.getNextWalkDirection() == -1) {
						int previewDir = getPreviewNextWalkStep();
						if (previewDir != -1) {
							Position tile = target.transform(Utils.DIRECTION_DELTA_X[previewDir],
									Utils.DIRECTION_DELTA_Y[previewDir], 0);
							if (Utils.colides(tile.getX(), tile.getY(), targetSize, getX(), getY(), size)) {
								continue;
							}

							if (!ignoreOthers && Utils.colides(tile.getX(), tile.getY(), targetSize, toX, toY, size)) {
								return false;
							}
						}
					}
					if (Utils.colides(target.getX(), target.getY(), targetSize, getX(), getY(), size)) {
						continue;
					}
					if (!ignoreOthers && Utils.colides(target.getX(), target.getY(), targetSize, toX, toY, size)) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	/*
	 * return added all steps
	 */
	public boolean addWalkStepsInteract(final int destX, final int destY,
			int maxStepsCount, int sizeX, int sizeY, boolean calculate) {
		int[] lastTile = getLastWalkTile();
		int myX = lastTile[0];
		int myY = lastTile[1];
		int stepCount = 0;
		while (true) {
			stepCount++;
			int myRealX = myX;
			int myRealY = myY;

			if (myX < destX) {
				myX++;
			} else if (myX > destX) {
				myX--;
			}
			if (myY < destY) {
				myY++;
			} else if (myY > destY) {
				myY--;
			}
			if (this instanceof NPC && !canWalkNPC(myX, myY) || !addWalkStep(myX, myY, lastTile[0], lastTile[1], true)) {
				if (!calculate) {
					return false;
				}
				myX = myRealX;
				myY = myRealY;
				int[] myT = calculatedStep(myRealX, myRealY, destX, destY,
						lastTile[0], lastTile[1], sizeX, sizeY);
				if (myT == null) {
					return false;
				}
				myX = myT[0];
				myY = myT[1];
			}
			int distanceX = myX - destX;
			int distanceY = myY - destY;
			if (!(distanceX > sizeX || distanceX < -1 || distanceY > sizeY || distanceY < -1)) {
				return true;
			}
			if (stepCount == maxStepsCount) {
				return true;
			}
			lastTile[0] = myX;
			lastTile[1] = myY;
			if (lastTile[0] == destX && lastTile[1] == destY) {
				return true;
			}
		}
	}

	public int[] calculatedStep(int myX, int myY, int destX, int destY,
			int lastX, int lastY, int sizeX, int sizeY) {
		if (myX < destX) {
			myX++;
			if (this instanceof NPC && !canWalkNPC(myX, myY) || !addWalkStep(myX, myY, lastX, lastY, true)) {
				myX--;
			} else if (!(myX - destX > sizeX || myX - destX < -1
					|| myY - destY > sizeY || myY - destY < -1)) {
				if (myX == lastX || myY == lastY) {
					return null;
				}
				return new int[] { myX, myY };
			}
		} else if (myX > destX) {
			myX--;
			if (this instanceof NPC && !canWalkNPC(myX, myY) || !addWalkStep(myX, myY, lastX, lastY, true)) {
				myX++;
			} else if (!(myX - destX > sizeX || myX - destX < -1
					|| myY - destY > sizeY || myY - destY < -1)) {
				if (myX == lastX || myY == lastY) {
					return null;
				}
				return new int[] { myX, myY };
			}
		}
		if (myY < destY) {
			myY++;
			if (this instanceof NPC && !canWalkNPC(myX, myY) || !addWalkStep(myX, myY, lastX, lastY, true)) {
				myY--;
			} else if (!(myX - destX > sizeX || myX - destX < -1
					|| myY - destY > sizeY || myY - destY < -1)) {
				if (myX == lastX || myY == lastY) {
					return null;
				}
				return new int[] { myX, myY };
			}
		} else if (myY > destY) {
			myY--;
			if (this instanceof NPC && !canWalkNPC(myX, myY) || !addWalkStep(myX, myY, lastX, lastY, true)) {
				myY++;
			} else if (!(myX - destX > sizeX || myX - destX < -1
					|| myY - destY > sizeY || myY - destY < -1)) {
				if (myX == lastX || myY == lastY) {
					return null;
				}
				return new int[] { myX, myY };
			}
		}
		if (myX == lastX || myY == lastY) {
			return null;
		}
		return new int[] { myX, myY };
	}

	/*
	 * return added all steps
	 */
	public boolean addWalkSteps(final int destX, final int destY,
			int maxStepsCount) {
		return addWalkSteps(destX, destY, -1, true);
	}

	/*
	 * return added all steps
	 */
	public boolean addWalkSteps(final int destX, final int destY,
			int maxStepsCount, boolean check) {
		int[] lastTile = getLastWalkTile();
		int myX = lastTile[0];
		int myY = lastTile[1];
		int stepCount = 0;
		while (true) {
			stepCount++;
			if (myX < destX) {
				myX++;
			} else if (myX > destX) {
				myX--;
			}
			if (myY < destY) {
				myY++;
			} else if (myY > destY) {
				myY--;
			}
			if (!addWalkStep(myX, myY, lastTile[0], lastTile[1], check)) {
				// here
				// so
				// stop
				return false;
			}
			if (stepCount == maxStepsCount) {
				return true;
			}
			lastTile[0] = myX;
			lastTile[1] = myY;
			if (lastTile[0] == destX && lastTile[1] == destY) {
				return true;
			}
		}
	}

	public int[] getLastWalkTile() {
		Object[] objects = walkSteps.toArray();
		if (objects.length == 0) {
			return new int[] { getX(), getY() };
		}
		int step[] = (int[]) objects[objects.length - 1];
		return new int[] { step[1], step[2] };
	}

	// return cliped step
	public boolean checkWalkStep(int nextX, int nextY, int lastX, int lastY,
			boolean check) {
		int dir = Utils.getMoveDirection(nextX - lastX, nextY - lastY);
		if (dir == -1) {
			return false;
		}

		if (check
				&& !World.checkWalkStep(getZ(), lastX, lastY, dir,
						getSize())) {
			return false;
		}
		return true;
	}

	// return cliped step
	public boolean addWalkStep(int nextX, int nextY, int lastX, int lastY,
			boolean check) {
		int dir = Utils.getMoveDirection(nextX - lastX, nextY - lastY);
		if (dir == -1) {
			return false;
		}

		if (check) {
			if (!World.checkWalkStep(getZ(), lastX, lastY, dir, getSize())) {
				return false;
			}
			if (this instanceof Player) {
				if (!((Player) this).getControlerManager().checkWalkStep(lastX,
						lastY, nextX, nextY)) {
					return false;
				}
			}
		}
		walkSteps.add(new int[] { dir, nextX, nextY });
		return true;
	}

	public ConcurrentLinkedQueue<int[]> getWalkSteps() {
		return walkSteps;
	}

	public void resetWalkSteps() {
		walkSteps.clear();
	}

	private int getNextWalkStep() {
		int step[] = walkSteps.poll();
		if (step == null) {
			return -1;
		}
		return step[0];
	}

	public boolean restoreHitPoints() {
		int maxHp = getMaxHitpoints();
		if (hitpoints > maxHp) {
			if (this instanceof Player) {
				Player player = (Player) this;
				if (player.getPrayer().usingPrayer(1, 5)
						&& Utils.getRandom(100) <= 15) {
					return false;
				}
			}
			setHitpoints(hitpoints - 1);
			return true;
		} else if (hitpoints < maxHp) {
			setHitpoints(hitpoints + 1);
			if (this instanceof Player) {
				Player player = (Player) this;
				if (player.getPrayer().usingPrayer(0, 9) && hitpoints < maxHp) {
					setHitpoints(hitpoints + 1);
				} else if (player.getPrayer().usingPrayer(0, 26)
						&& hitpoints < maxHp) {
					setHitpoints(hitpoints + (hitpoints + 4 > maxHp ? maxHp - hitpoints : 4));
				}

			}
			return true;
		}
		return false;
	}

	public boolean needMasksUpdate() {
		return nextFaceEntity != -2 || nextAnimation != null
				|| nextGraphics1 != null || nextGraphics2 != null
				|| nextGraphics3 != null || nextGraphics4 != null
				|| nextWalkDirection == -1 && nextFacePosition != null
				|| !nextHits.isEmpty() || nextForceMovement != null
				|| nextForceTalk != null;
	}

	public boolean isDead() {
		return hitpoints == 0;
	}

	public void resetMasks() {
		nextAnimation = null;
		nextGraphics1 = null;
		nextGraphics2 = null;
		nextGraphics3 = null;
		nextGraphics4 = null;
		if (nextWalkDirection == -1) {
			nextFacePosition = null;
		}
		nextForceMovement = null;
		nextForceTalk = null;
		nextFaceEntity = -2;
		nextHits.clear();
	}

	public abstract void finish();

	public abstract int getMaxHitpoints();

	public void processEntity() {
		toxin.processToxins();
		processMovement();
		processReceivedHits();
		processReceivedDamage();
		processEvents();
		if (locked_ticks > 0)
			locked_ticks--;
	}

	public void loadMapRegions() {
		mapRegionsIds.clear();
		isAtDynamicRegion = false;
		int chunkX = getChunkX();
		int chunkY = getChunkY();
		int mapHash = Constants.MAP_SIZES[mapSize] >> 4;
				int minRegionX = (chunkX - mapHash) / 8;
				int minRegionY = (chunkY - mapHash) / 8;
				for (int xCalc = minRegionX < 0 ? 0 : minRegionX; xCalc <= (chunkX + mapHash) / 8; xCalc++) {
					for (int yCalc = minRegionY < 0 ? 0 : minRegionY; yCalc <= (chunkY + mapHash) / 8; yCalc++) {
						int regionId = yCalc + (xCalc << 8);
						if (World.getRegion(regionId, this instanceof Player) instanceof DynamicRegion) {
							isAtDynamicRegion = true;
						}
						mapRegionsIds.add(regionId);
					}
				}
				lastLoadedMapRegionTile = new Position(this); // creates a immutable
				// copy of this
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public int getHitpoints() {
		return hitpoints;
	}

	public void setHitpoints(int hitpoints) {
		this.hitpoints = hitpoints;
	}

	public void setLastRegionId(int lastRegionId) {
		this.lastRegionId = lastRegionId;
	}

	public int getLastRegionId() {
		return lastRegionId;
	}

	public int getMapSize() {
		return mapSize;
	}

	public void setMapSize(int size) {
		mapSize = size;
		loadMapRegions();
	}

	public CopyOnWriteArrayList<Integer> getMapRegionsIds() {
		return mapRegionsIds;
	}

	public void animate(int animation) {
		Animation nextAnimation = new Animation(animation);
		if (nextAnimation != null && nextAnimation.getIds()[0] >= 0) {
			lastAnimationEnd = Utils.currentTimeMillis()
					+ AnimationDefinitions.getAnimationDefinitions(
							nextAnimation.getIds()[0]).getDuration();
		}
		this.nextAnimation = nextAnimation;
		
	}
	
	public void animate(Animation nextAnimation) {
		if (nextAnimation != null && nextAnimation.getIds()[0] >= 0) {
			lastAnimationEnd = Utils.currentTimeMillis()
					+ AnimationDefinitions.getAnimationDefinitions(
							nextAnimation.getIds()[0]).getDuration();
		}
		this.nextAnimation = nextAnimation;
	}

	public void setNextAnimationNoPriority(Animation nextAnimation) {
		if (lastAnimationEnd > Utils.currentTimeMillis()) {
			return;
		}
		animate(nextAnimation);
	}

	public abstract boolean canMove(int dir);

	public Animation getNextAnimation() {
		return nextAnimation;
	}

	public void setNextGraphics(Graphics nextGraphics) {
		if (nextGraphics == null) {
			if (nextGraphics4 != null) {
				nextGraphics4 = null;
			} else if (nextGraphics3 != null) {
				nextGraphics3 = null;
			} else if (nextGraphics2 != null) {
				nextGraphics2 = null;
			} else {
				nextGraphics1 = null;
			}
		} else {
			if (nextGraphics.equals(nextGraphics1)
					|| nextGraphics.equals(nextGraphics2)
					|| nextGraphics.equals(nextGraphics3)
					|| nextGraphics.equals(nextGraphics4)) {
				return;
			}
			if (nextGraphics1 == null) {
				nextGraphics1 = nextGraphics;
			} else if (nextGraphics2 == null) {
				nextGraphics2 = nextGraphics;
			} else if (nextGraphics3 == null) {
				nextGraphics3 = nextGraphics;
			} else {
				nextGraphics4 = nextGraphics;
			}
		}
	}
	
	public void setNextGraphics(int graphics) {
		Graphics nextGraphics = (graphics < 0 ? null : new Graphics(graphics));
		if (nextGraphics == null) {
			if (nextGraphics4 != null) {
				nextGraphics4 = null;
			} else if (nextGraphics3 != null) {
				nextGraphics3 = null;
			} else if (nextGraphics2 != null) {
				nextGraphics2 = null;
			} else {
				nextGraphics1 = null;
			}
		} else {
			if (nextGraphics.equals(nextGraphics1)
					|| nextGraphics.equals(nextGraphics2)
					|| nextGraphics.equals(nextGraphics3)
					|| nextGraphics.equals(nextGraphics4)) {
				return;
			}
			if (nextGraphics1 == null) {
				nextGraphics1 = nextGraphics;
			} else if (nextGraphics2 == null) {
				nextGraphics2 = nextGraphics;
			} else if (nextGraphics3 == null) {
				nextGraphics3 = nextGraphics;
			} else {
				nextGraphics4 = nextGraphics;
			}
		}
	}

	public Graphics getNextGraphics1() {
		return nextGraphics1;
	}

	public Graphics getNextGraphics2() {
		return nextGraphics2;
	}

	public Graphics getNextGraphics3() {
		return nextGraphics3;
	}

	public Graphics getNextGraphics4() {
		return nextGraphics4;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getDirection() {
		return direction;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setNextPosition(Position nextPosition) {

		this.nextPosition = nextPosition;
	}

	public Position getNextPosition() {
		return nextPosition;
	}

	public boolean hasTeleported() {
		return teleported;
	}

	public Position getLastLoadedMapRegionTile() {
		return lastLoadedMapRegionTile;
	}

	public int getNextWalkDirection() {
		return nextWalkDirection;
	}

	public int getNextRunDirection() {
		return nextRunDirection;
	}

	public void setRun(boolean run) {
		this.run = run;
	}

	public boolean getRun() {
		return run;
	}

	public Position getNextFacePosition() {
		return nextFacePosition;
	}

	public void setNextFacePosition(Position nextFacePosition) {
		if (nextFacePosition.getX() == getX()
				&& nextFacePosition.getY() == getY()) {
			return;
		}
		this.nextFacePosition = nextFacePosition;
		if (nextPosition != null) {
			direction = Utils.getFaceDirection(nextFacePosition.getX()
					- nextPosition.getX(), nextFacePosition.getY()
					- nextPosition.getY());
		} else {
			direction = Utils.getFaceDirection(nextFacePosition.getX()
					- getX(), nextFacePosition.getY() - getY());
		}
	}

	public abstract int getSize();

	public void cancelFaceEntityNoCheck() {
		nextFaceEntity = -2;
		lastFaceEntity = -1;
	}

	public void setNextFaceEntity(Entity entity) {
		if (entity == null) {
			nextFaceEntity = -1;
			lastFaceEntity = -1;
		} else {
			nextFaceEntity = entity.getClientIndex();
			lastFaceEntity = nextFaceEntity;
		}
	}

	public int getNextFaceEntity() {
		return nextFaceEntity;
	}

	public long getFreezeDelay() {
		return freezeDelay; // 2500 delay
	}

	public int getLastFaceEntity() {
		return lastFaceEntity;
	}

	public long getFrozenBlockedDelay() {
		return frozenBlocked;
	}

	public void setFrozeBlocked(int time) {
		frozenBlocked = time;
	}

	public void setFreezeDelay(int time) {
		freezeDelay = time;
	}

	public void setArmorbreakdelay(int time) {
		Armorbreakdelay = time;
	}

	public void addFrozenBlockedDelay(int time) {
		frozenBlocked = time + Utils.currentTimeMillis();
	}

	public void addFreezeDelay(long time) {
		addFreezeDelay(time, false);
	}

	public void addFreezeDelay(long time, boolean entangleMessage) {
		long currentTime = Utils.currentTimeMillis();
		if (currentTime > freezeDelay) {
			resetWalkSteps();
			freezeDelay = time + currentTime;
			if (this instanceof Player) {
				Player p = (Player) this;
				if (!entangleMessage) {
					p.getPackets().sendGameMessage("You have been frozen.");
				}
			}
		}
	}



	public abstract double getMagePrayerMultiplier();

	public abstract double getRangePrayerMultiplier();

	public abstract double getMeleePrayerMultiplier();

	public Entity getAttackedBy() {
		return attackedBy;
	}

	public void setAttackedBy(Entity attackedBy) {
		this.attackedBy = attackedBy;
	}

	public long getAttackedByDelay() {
		return attackedByDelay;
	}

	public void setAttackedByDelay(long attackedByDelay) {
		this.attackedByDelay = attackedByDelay;
	}

	public void checkMultiArea() {
		multiArea = forceMultiArea || World.isMultiArea(this);
	}
	public void checkMorytaniaArea() {
		morytaniaArea = forceMorytaniaArea ? true : World.isMorytaniaArea(this);
	}
	public boolean isAtMorytaniaArea() {
		return morytaniaArea;
	}

	public boolean isAtMultiArea() {
		return multiArea;
	}

	public void setAtMultiArea(boolean multiArea) {
		this.multiArea = multiArea;
	}
	public void setAtMorytaniaArea(boolean morytaniaArea) {
		this.morytaniaArea = morytaniaArea;
	}

	public void checkSmokeyArea() {
		smokeyArea = forceSmokeyArea ? true : World.isSmokeyArea(this);
	}

	public boolean isAtSmokeyArea() {
		return smokeyArea;
	}
	public boolean isAtUnderwater() {
		return underwater;
	}


	public void setAtSmokeyArea(boolean smokeyArea) {
		this.smokeyArea = smokeyArea;
	}

	public void checkUnderwaterArea() {
		underwater = forceUnderwaterArea ? true : World.isUnderwaterArea(this);
	}

	public void setAtUnderwaterArea(boolean underwaterarea) {
		this.underwater = underwater;
	}


	public void checkDesertArea() {
		desertArea = forceDesertArea ? true : World.isDesertArea(this);
	}

	public boolean isAtDesertArea() {
		return desertArea;
	}

	public void setAtDesertArea(boolean desertArea) {
		this.desertArea = desertArea;
	}

	public void checkSinkArea() {
		sinkArea = forceSinkArea ? true : World.isSinkArea(this);
	}

	public boolean isAtSinkArea() {
		return sinkArea;
	}

	public void setAtSinkArea(boolean sinkArea) {
		this.sinkArea = sinkArea;
	}

	public boolean isAtDynamicRegion() {
		return isAtDynamicRegion;
	}

	public ForceMovement getNextForceMovement() {
		return nextForceMovement;
	}

	public void setNextForceMovement(ForceMovement nextForceMovement) {
		this.nextForceMovement = nextForceMovement;
	}

	public ForceTalk getNextForceTalk() {
		return nextForceTalk;
	}

	public void setNextForceTalk(ForceTalk nextForceTalk) {
		this.nextForceTalk = nextForceTalk;
	}

	public void setNextForceTalk(String text) {
		setNextForceTalk(new ForceTalk(text));
	}

	public void faceEntity(Entity target) {			
		setNextFacePosition(new Position(target.getCoordFaceX(target
				.getSize()), target.getCoordFaceY(target.getSize()),
				target.getZ()));
	}

	public void faceObject(WorldObject object) {
		ObjectDefinitions objectDef = object.getDefinitions();
		setNextFacePosition(new Position(object.getCoordFaceX(
				objectDef.getSizeX(), objectDef.getSizeY(),
				object.getRotation()), object.getCoordFaceY(
						objectDef.getSizeX(), objectDef.getSizeY(),
						object.getRotation()), object.getZ()));
	}

	public long getLastAnimationEnd() {
		return lastAnimationEnd;
	}

	public ConcurrentHashMap<Object, Object> getTemporaryAttributtes() {
		return temporaryAttributes;
	}

	public boolean isForceMultiArea() {
		return forceMultiArea;
	}
	public boolean isForceMorytaniaArea() {
		return forceMorytaniaArea;
	}

	public boolean isForceUndewaterArea() {
		return forceUnderwaterArea;
	}

	public boolean isForceSmokeyArea() {
		return forceSmokeyArea;
	}

	public boolean isForceDesertArea() {
		return forceDesertArea;
	}

	public boolean isForceSinkArea() {
		return forceSinkArea;
	}

	public void setForceMultiArea(boolean forceMultiArea) {
		this.forceMultiArea = forceMultiArea;
		checkMultiArea();
	}
	public void setForceMorytaniaArea(boolean forceMorytaniaArea) {
		this.forceMorytaniaArea = forceMorytaniaArea;
		checkMorytaniaArea();
	}

	public void setForceSmokeyArea(boolean forceSmokeyArea) {
		this.forceSmokeyArea = forceSmokeyArea;
		checkSmokeyArea();
	}

	public void setForceDesertArea(boolean forceDesertArea) {
		this.forceDesertArea = forceDesertArea;
		checkDesertArea();
	}
	public void setForceUnderwaterArea(boolean forceUnderwaterArea) {
		this.forceUnderwaterArea = forceUnderwaterArea;
		checkUnderwaterArea();
	}

	public void setForceSinkArea(boolean forceSinkArea) {
		this.forceSinkArea = forceSinkArea;
		checkSinkArea();
	}

	public void deathResetCombat() {
		attackedBy = null;
		attackedByDelay = 0;
	}

	public Position getLastPosition() {
		return lastPosition;
	}

	public ArrayList<Hit> getNextHits() {
		return nextHits;
	}

	public void playSound(int soundId, int type) {
		for (int regionId : getMapRegionsIds()) {
			List<Integer> playerIndexes = World.getRegion(regionId)
					.getPlayerIndexes();
			if (playerIndexes != null) {
				for (int playerIndex : playerIndexes) {
					Player player = World.getPlayers().get(playerIndex);
					if (player == null || !player.isRunning()|| !withinDistance(player)) {
						continue;
					}
					player.getPackets().sendSound(soundId, 0, type);
				}
			}
		}
	}

	public long getFindTargetDelay() {
		return findTargetDelay;
	}

	public void setFindTargetDelay(long findTargetDelay) {
		this.findTargetDelay = findTargetDelay;
	}

	public void addGlacorFreezeDelay(long time, boolean entangleMessage) {
		long currentTime = Utils.currentTimeMillis();
		if (currentTime > freezeDelay) {
			resetWalkSteps();
			freezeDelay = time + currentTime;
			if (this instanceof Player) {
				Player p = (Player) this;
				if (!entangleMessage) {
					p.getPackets().sendGameMessage(
							"<col=00FFFF>You have been frozen! " + "Try to escape as quickly as possible.</col>");
				}
			}
		}
	}


	public boolean isCantWalk() {
		return cantWalk;
	}

	public void setCantWalk(boolean cantWalk) {
		this.cantWalk = cantWalk;
	}


	public Player getAsPlayer() {
		return (Player) this;
	}

	public NPC getAsNPC() {
		return (NPC) this;
	}
	
	public boolean isPlayer() {
		return this instanceof Player;
	}
	
	public boolean isNPC() {
		return this instanceof NPC;
	}
	
	public ConcurrentLinkedQueue<Hit> getReceivedHits() {
		return receivedHits;
	}
	
	public boolean hasFinished() {
		return finished;
	}

	
	@Deprecated
	public Hit handleOutgoingHit(Hit hit, Entity target) {
		// EMPTY
		return hit;
	}

	public Bounds getBounds() {
		return new Bounds(getPosition().getX(), getPosition().getY(), getPosition().getX() + getSize() - 1, getPosition().getY() + getSize() - 1, getPosition().getZ());
	}

	private transient CoroutineEvent activeEvent, nextEvent;
	private transient ArrayList<CoroutineEvent> backgroundEvents;

	public final CoroutineEvent startEvent(CoroutineEvent.EventConsumer consumer) {
		stopEvent();
		return nextEvent = CoroutineWorker.createEvent(consumer);
	}

	public final void addEvent(CoroutineEvent.EventConsumer consumer) {
		if (backgroundEvents == null)
			backgroundEvents = Lists.newArrayList();
		backgroundEvents.add(CoroutineWorker.createEvent(consumer));
	}

	public final void processEvents() {
		if (nextEvent != null) {
			activeEvent = nextEvent;
			nextEvent = null;
		}
		if (activeEvent != null && !activeEvent.tick())
			activeEvent = null;
		if (backgroundEvents != null && !backgroundEvents.isEmpty())
			backgroundEvents.removeIf(event -> !event.tick());
	}

	public final void stopEvent() {
		if (activeEvent != null)
			activeEvent = null;
	}

	public void faceNone() {
		face(null);
	}

	public void face(Entity target) {
		setNextFaceEntity(target);
	}

	public transient HitListener hitListener;
	public transient AttackNPCListener attackNPCListener;
	public transient DeathListener deathStartListener;
	public transient DeathListener deathEndListener;
	public transient SpawnListener spawnListener;
	public transient TeleportListener teleportListener;

}
