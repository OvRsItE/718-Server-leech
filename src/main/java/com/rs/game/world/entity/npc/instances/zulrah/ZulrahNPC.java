/**
 * 
 */
package com.rs.game.world.entity.npc.instances.zulrah;

import static com.rs.game.world.entity.npc.instances.zulrah.ZulrahPosition.CENTER;
import static com.rs.game.world.entity.npc.instances.zulrah.ZulrahPosition.SOUTH;
import static com.rs.game.world.entity.npc.instances.zulrah.ZulrahPosition.WEST;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.Drop;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.npc.instances.zulrah.combat.CloudsSequence;
import com.rs.game.world.entity.npc.instances.zulrah.combat.DiveSequence;
import com.rs.game.world.entity.npc.instances.zulrah.combat.FlickingSequence;
import com.rs.game.world.entity.npc.instances.zulrah.combat.MagicSequence;
import com.rs.game.world.entity.npc.instances.zulrah.combat.MeleeSequence;
import com.rs.game.world.entity.npc.instances.zulrah.combat.RangedSequence;
import com.rs.game.world.entity.npc.instances.zulrah.combat.SnakelingSequence;
import com.rs.game.world.entity.npc.instances.zulrah.combat.SpawnSequence;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.Prayer;
import com.rs.game.world.entity.player.content.toxin.ToxinType;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.NPCBonuses;
import com.rs.utility.NPCDrops;
import com.rs.utility.Utils;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Kris
 * @author ReverendDread
 * Jul 24, 2018
 */
public class ZulrahNPC extends NPC {

	private static final long serialVersionUID = 2384789188608618388L;

	private static final int RANGED = asOSRS(2042), MAGIC = asOSRS(2044), MELEE = asOSRS(2043);
	
	@Getter private ZulrahInstance instance;
	@Getter private final Player player;
	@Getter @Setter private int rotation = Utils.random(3), sequence, phase;
	@Getter private final List<Position> venomClouds = new CopyOnWriteArrayList<Position>();
	@Getter private final List<SnakelingNPC> snakelings = new CopyOnWriteArrayList<SnakelingNPC>();
	
	private static final Sequence[][] SEQUENCES = new Sequence[][] {
		/** First rotation */
		new Sequence[] { 
				new RangedSequence(5), 
				new DiveSequence(CENTER, MELEE),
				new CloudsSequence(new Position(2269, 3069, 0), new Position(2272, 3070, 0), false),
				new CloudsSequence(new Position(2266, 3069, 0), new Position(2263, 3070, 0), false),
				new CloudsSequence(new Position(2273, 3072, 0), new Position(2273, 3075, 0), false),
				new CloudsSequence(new Position(2263, 3073, 0), new Position(2263, 3076, 0), false),
		},
		new Sequence[] { new MeleeSequence(), new MeleeSequence(), new DiveSequence(CENTER, MAGIC) },
		new Sequence[] { new MagicSequence(4), new DiveSequence(SOUTH, RANGED) },
		new Sequence[] { 
				new RangedSequence(5), 
				new SnakelingSequence(new Position(2263, 3076, 0)),
				new SnakelingSequence(new Position(2263, 3073, 0)),
				new CloudsSequence(new Position(2263, 3070, 0), new Position(2266, 3069, 0), false),
				new CloudsSequence(new Position(2272, 3069, 0), new Position(2273, 3072, 0), false),
				new SnakelingSequence(new Position(2273, 3075, 0)),
				new SnakelingSequence(new Position(2273, 3077, 0)),
				new DiveSequence(CENTER, MELEE) 
		},
		new Sequence[] { new MeleeSequence(), new MeleeSequence(), new DiveSequence(WEST, MAGIC) },
		new Sequence[] { new MagicSequence(5), new DiveSequence(SOUTH, RANGED) },
		new Sequence[] { 
				new CloudsSequence(new Position(2269, 3069, 0), new Position(2272, 3069, 0), false),
				new CloudsSequence(new Position(2263, 3070, 0), new Position(2266, 3069, 0), false),
				new CloudsSequence(new Position(2263, 3073, 0), new Position(2263, 3076, 0), false),
				new SnakelingSequence(new Position(2272, 3071, 0)),
				new SnakelingSequence(new Position(2273, 3075, 0)),
				new SnakelingSequence(new Position(2273, 3077, 0)),
				new SnakelingSequence(new Position(2273, 3072, 0)),
				new DiveSequence(SOUTH, MAGIC) },
		new Sequence[] { 
				new MagicSequence(5), 
				new SnakelingSequence(new Position(2263, 3070, 0)),
				new CloudsSequence(new Position(2266, 3069, 0), new Position(2269, 3069, 0), false),
				new SnakelingSequence(new Position(2263, 3076, 0)),
				new CloudsSequence(new Position(2272, 3069, 0), new Position(2273, 3072, 0), false),
				new SnakelingSequence(new Position(2263, 3073, 0)),
				new DiveSequence(WEST, RANGED)
		},
		new Sequence[] { 
				new FlickingSequence(false),
				new CloudsSequence(new Position(2263, 3070, 0), new Position(2266, 3069, 0), false),
				new CloudsSequence(new Position(2269, 3069, 0), new Position(2272, 3069, 0), false),
				new CloudsSequence(new Position(2263, 3073, 0), new Position(2263, 3076, 0), false),
				new CloudsSequence(new Position(2273, 3072, 0), new Position(2273, 2075, 0), false),
				new DiveSequence(CENTER, MELEE)
		},
		new Sequence[] { new MeleeSequence(), new MeleeSequence(), new DiveSequence(CENTER, RANGED) }
	};
	
	public ZulrahNPC(Position Position, Player player, ZulrahInstance instance) {
		super(RANGED, Position, -1, 0, true, false);
		this.instance = instance;
		this.player = player;
		setNextFacePosition(instance.getLocation(new Position(2268, 3069)));
		setCantFollowUnderCombat(true);
		setForceMultiArea(true);
		animate(Animation.createOSRS(5071));
		getToxin().applyImmunity(ToxinType.POISON, Integer.MAX_VALUE);
		getToxin().applyImmunity(ToxinType.VENOM, Integer.MAX_VALUE);
		new SpawnSequence().attack(this, instance, player);
		setCanBeFrozen(false);
	}
	
	@Override
	public void applyHit(final Hit hit) {
		if (hit.getDamage() >= 500) {
			hit.setDamage(Utils.random(450, 500));
		}
		if (hit.getLook() == HitLook.MELEE_DAMAGE) {
			hit.setDamage(0);
		}
		super.applyHit(hit);
	}
	
	@Override
	public void transformIntoNPC(int id) {
		super.transformIntoNPC(id);
		int[] bonuses = NPCBonuses.getBonuses(id);
		setBonuses(bonuses);
	}
	
	@Override
	public void sendDeath(final Entity source) {
		final NPCCombatDefinitions defs = getCombatDefinitions();
		resetWalkSteps();
		getCombat().removeTarget();
		animate(null);
		WorldTasksManager.schedule(new WorldTask() {
			private int loop;
			@Override
			public void run() {
				if (loop == 0) {	
					animate(new Animation(defs.getDeathEmote()));
					if (player.withinDistance(ZulrahNPC.this, 50)) {
						drop();
					}
					reset();
					finish();
					stop();
					return;
				}
				loop++;
			}
		}, 0, 1);
	}
	
	@Override
	public void finish() {
		snakelings.forEach(NPC::finish);
		snakelings.clear();
		super.finish();
	}
	
	@Override
	public void drop() {
		final Player killer = getMostDamageReceivedSourcePlayer();
		if (killer == null) {
			return;
		}
		final Drop[] drops = NPCDrops.getDrops(getId());
		if (drops == null) {
			return;
		}
		killer.getKillcountManager().incremenetAndGet(getId());
		for (int i = 0; i < 2; i++) {
			final Drop[] possibleDrops = new Drop[drops.length];
			int possibleDropsCount = 0;
			for (final Drop drop : drops) {
				if (drop == null) {
					continue;
				}
				if (drop.getRate() == 100) {
					if (i == 0) {
						sendDrop(killer, drop);
					}
					continue;
				} else {
					final double rate = drop.getRate();
					final double random = Utils.getRandomDouble(100);
					if (random <= rate && random != 100 && random != 0) {
						possibleDrops[possibleDropsCount++] = drop;
					}
				}
			}
			if (possibleDropsCount > 0) {
				final Drop drop = possibleDrops[Utils.random(possibleDropsCount - 1)];
				sendDrop(killer, drop);
			}
		}
	}
	
	/**
	 * Gets the next Sequence in the current rotation, in the current sequence array.
	 * Sets the position of the sequence to the next one.
	 * @return the next Sequence object to execute.
	 */
	public final Sequence getNextSequence() {
		Sequence[] seq = SEQUENCES[sequence];
		if (phase >= seq.length) {
			phase = 0;
			if (++sequence >= SEQUENCES.length) {
				sequence = 0;
			}
			seq = SEQUENCES[sequence];
		}
		return seq[phase++];
	}
	
	/**
	 * Adds the venom cloud to the requested tile. 
	 * The cloud only remains for 18 seconds AKA 30 ticks.
	 * The tile requested should be the center of the cloud, 
	 * the tile on which the cloud is spawned on is directly SW of that.
	 * @param tile the center of the cloud.
	 * @param delay the delay in ticks until the clouds spawning.
	 */
	public void addCloud(final Position tile, final int delay) {
		tile.setLocation(tile.getX() - 1, tile.getY() - 1, tile.getZ());
		WorldTasksManager.schedule(new WorldTask() {
			private VenomCloud cloud;
			private int ticks;
			@Override
			public void run() {
				if (ZulrahNPC.this.isDead() || ZulrahNPC.this.isFinished()) {
					if (cloud != null) {
						World.removeObject(cloud);
					}
					stop();
					return;
				}
				if (cloud == null) {
					World.spawnObject(cloud = new VenomCloud(tile));
				} else {
					if (ticks == 30) {
						World.removeObject(cloud);
						stop();
					} else {
						if (player.withinDistance(tile, 1)) {
							player.applyHit(new Hit(null, Utils.random(10, 40), HitLook.POISON_DAMAGE));
						}
					}
					ticks++;
				}
			}
		}, delay, 0);
	}
	
	/**
	 * Schedules a hit from Zulrah. Additionally, if the hit is successful, 
	 * applies venom on the target.
	 * @param delay the delay in ticks until the hit.
	 * @param hit the hit being applied on the player.
	 */
	public void delayHit(final int delay, final Hit hit) {
		WorldTasksManager.schedule(new WorldTask() {
			
			@Override
			public void run() {
				CombatScript.delayHit(ZulrahNPC.this, 0, player, hit);
				if (hit.getDamage() > 0) {
					final Prayer prayer = player.getPrayer();
					final HitLook type = hit.getLook();
					final int p = type == HitLook.MELEE_DAMAGE ? Prayer.PROTECT_FROM_MELEE 
							: type == HitLook.RANGE_DAMAGE ? Prayer.PROTECT_FROM_MISSILES 
									: type == HitLook.MAGIC_DAMAGE ? Prayer.PROTECT_FROM_MAGIC : 0;
					if (!prayer.usingPrayer(prayer.isAncientCurses() ? 1 : 0, p)) {
						player.getToxin().applyToxin(ToxinType.VENOM, 6);
					}
				}
			}
			
		}, delay);
	}
	
	@Override
	public void processNPC() {
		if (isFinished()) {
			return;
		}
		if (player.isDead() || player.isFinished()) {
			finish();
			return;
		}
		if (isLocked() || isDead()) {
			return;
		}
		if (!player.withinDistance(this, 50)) {
			finish();
			return;
		}
	}

}
