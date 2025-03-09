package com.rs.game.world.entity.npc.combat.impl;

import com.google.common.collect.Lists;
import com.rs.game.map.Bounds;
import com.rs.game.map.Direction;
import com.rs.game.map.Position;
import com.rs.game.world.Projectile;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.listeners.HitListener;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.npc.others.AmethystDragonMinion;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.utility.Misc;
import com.rs.utility.TickDelay;

import java.util.List;

/**
 * @author ReverendDread
 * Created 3/27/2021 at 10:01 PM
 * @project 718---Server
 *
 * Npc: 16103
 *
 * Anims ---------
 * Claws Attack: 5023,
 * Bite Attack: 5024
 * IDK: 5025
 * Fire/range/magic attack: 5030
 * Defence: 5026
 * Death: 5027
 *
 *
 * GFX --------
 *
 * Burning projectile - 3442
 *
 * hit - 3358
 *
 * fire ball - 3390
 *
 * fireball 2 - 3394
 *
 * hit gfx 3176
 *
 * burning - 3005
 *
 * hit 3096
 *
 * projectile - 2938
 *
 * projectile - 2939
 *
 * hit - 2942
 *
 * fire projectile - 2862
 *
 * hit 2783
 *
 *
 */
public class AmethystDragonCombat extends CombatScript {

    public static boolean isSpawned;
    public static boolean getSpawned() {
        return isSpawned;
    }
    private static final Projectile MAGIC_PROJ = new Projectile(2938, 120, 30, 10, 16, 50, -4);
    private static final Projectile RANGE_PROJ = new Projectile(2937, 120, 30, 10, 16, 50, -4);
    private static final Projectile COMBUSTION_PROJ = new Projectile(2862, 120, 0, 10, 16, 20, -4);
    private static final Projectile MELEE_PROJ = new Projectile(2939, 120, 30, 10, 16, 50, -4);
    private static final Projectile MINION_PROJ = new Projectile(3394, 120, 0, 10, 16, 50, -4);
    private static final Graphics MAGIC_HIT = new Graphics(2941);
    private static final Graphics RANGE_HIT = new Graphics(2940);
    private static final Graphics MELEE_HIT = new Graphics(2942);
    private static final Graphics COMBUSTION = new Graphics(2783);
    private static final Graphics FALLING_BEAM = new Graphics(3096);
    private static final Bounds ARENA = new Bounds(3801, 2514, 3821, 2534, 0);

    private final TickDelay AOE = new TickDelay();
    private boolean performUltimate;
    private int stage = 0;

    private final List<NPC> minions = Lists.newArrayList();

    @Override
    public boolean init(NPC npc) {
        npc.setCanBeFrozen(false);
        npc.setCapDamage(750);
        npc.spawnListener = (entity) -> isSpawned = true;
        npc.hitListener = new HitListener().postDamage(e -> {
            double health = ((double) npc.getHitpoints() / npc.getMaxHitpoints());
            if (health <= .75 && stage == 0) {
                performUltimate = true;
                stage = 1;
            } else if (health <= .50 && stage == 1) {
                performUltimate = true;
                stage = 2;
            } else if (health <= .25 && stage == 2) {
                performUltimate = true;
                stage = 3;
            }
        });
        npc.deathEndListener = ((entity, killer) -> {
            minions.forEach(NPC::finish);
            isSpawned = false;
            stage = 0;
        });
        return true;
    }

    @Override
    public Object[] getKeys() {
        //return new Object[] { "Amethyst Dragon" };
        return new Object[] { "Ameythst Dragon" };
    }

    @Override
    public int attack(NPC npc, Entity target) {
        minions.removeIf(NPC::isDead);
        boolean melee = Misc.rollPercent(50) && target.withinDistance(npc.getMiddleTile(), 4);
        int random = Misc.random(minions.size() < 6 ? AOE.allowed() ? 12 : 10 : 8);
        if (melee) {
            npc.animate(Misc.get(new Integer[]{5023, 5024}));
            Hit hit = new Hit(npc).look(Hit.HitLook.MELEE_DAMAGE).damage(getRandomMaxHit(npc, 550, NPCCombatDefinitions.MELEE, target)).ignorePrayer();
            delayHit(npc, 1, target, hit);
            if (hit.getDamage() > 0) {
                target.addEvent(event -> {
                    event.delay(1);
                    for (int i = 0; i < 3; i++) {
                        target.applyHit(new Hit(npc).look(Hit.HitLook.REGULAR_DAMAGE).max(3, 5));
                        event.delay(2);
                    }
                });
            }
        } else if (performUltimate) {
            npc.animate(5030);
            npc.addEvent(event -> {
                for (int i = 0; i < 40; i++) {
                    final Position pos = ARENA.randomPosition();
                    World.sendGraphics(npc, FALLING_BEAM, pos);
                    event.delay(1);
                    for (Entity t : npc.getPossibleTargets()) {
                        if (t.getPosition().matches(pos))
                            t.applyHit(new Hit(npc).look(Hit.HitLook.REGULAR_DAMAGE).damage(1000));
                    }
                }
            });
            npc.addEvent(event -> {
                for (int i = 0; i < 40; i++) {
                    final Position pos = ARENA.randomPosition();
                    World.sendGraphics(npc, FALLING_BEAM, pos);
                    event.delay(1);
                    for (Entity t : npc.getPossibleTargets()) {
                        if (t.getPosition().matches(pos))
                            t.applyHit(new Hit(npc).look(Hit.HitLook.REGULAR_DAMAGE).damage(1000));
                    }
                }
            });
            npc.addEvent(event -> {
                for (int i = 0; i < 40; i++) {
                    final Position pos = ARENA.randomPosition();
                    World.sendGraphics(npc, FALLING_BEAM, pos);
                    event.delay(1);
                    for (Entity t : npc.getPossibleTargets()) {
                        if (t.getPosition().matches(pos))
                            t.applyHit(new Hit(npc).look(Hit.HitLook.REGULAR_DAMAGE).damage(1000));
                    }
                }
            });
            performUltimate = false;
        } else {
            npc.animate(5030);
            switch (random) {
                case 0:
                case 1:
                case 2:
                    npc.addEvent(event -> {
                        for (Entity t : npc.getPossibleTargets()) {
                            int delay = MAGIC_PROJ.send(npc, t);
                            Hit hit = new Hit(npc).look(Hit.HitLook.MAGIC_DAMAGE).damage(getRandomMaxHit(npc, 100, NPCCombatDefinitions.MAGE, target));
                            delayHit(npc, delay, t, hit);
                            if (hit.getDamage() > 0) {
                                t.addEvent(e -> {
                                    event.delay(delay);
                                    t.setNextGraphics(MAGIC_HIT);
                                });
                            }
                        }
                    });
                    break;
                case 3:
                case 4:
                case 5:
                    npc.addEvent(event -> {
                        for (Entity t : npc.getPossibleTargets()) {
                            int delay = RANGE_PROJ.send(npc, t);
                            Hit hit = new Hit(npc).look(Hit.HitLook.RANGE_DAMAGE).damage(getRandomMaxHit(npc, 100, NPCCombatDefinitions.RANGE, target));
                            delayHit(npc, delay, t, hit);
                            if (hit.getDamage() > 0) {
                                t.addEvent(e -> {
                                    event.delay(delay);
                                    t.setNextGraphics(RANGE_HIT);
                                });
                            }
                        }
                    });
                    break;
                case 6:
                case 7:
//                    npc.addEvent(event -> {
//                        for (Entity t : npc.getPossibleTargets()) {
//                            final Position before = t.getPosition().copy();
//                            int delay = COMBUSTION_PROJ.send(npc, before);
//                            t.addEvent(e -> {
//                                e.delay(delay);
//                                World.sendGraphics(npc, COMBUSTION, before);
//                                if (t.getPosition().matches(before)) {
//                                    t.applyHit(new Hit(npc).look(Hit.HitLook.REGULAR_DAMAGE).max(500, 989));
//                                }
//                            });
//                        }
//                    });
                    npc.addEvent(event -> {
                        for (Entity t : npc.getPossibleTargets()) {
                            int delay = MELEE_PROJ.send(npc, t);
                            Hit hit = new Hit(npc).look(Hit.HitLook.MELEE_DAMAGE).damage(getRandomMaxHit(npc, 100, NPCCombatDefinitions.MELEE, target));
                            delayHit(npc, delay, t, hit);
                            if (hit.getDamage() > 0) {
                                t.addEvent(e -> {
                                    event.delay(delay);
                                    t.setNextGraphics(MELEE_HIT);
                                });
                            }
                        }
                    });
                    break;
                case 8:
                case 9:
                    Position pos = ARENA.randomPosition();
                    int delay = MINION_PROJ.send(npc, pos);
                    npc.addEvent(event -> {
                        event.delay(delay);
                        minions.add(new AmethystDragonMinion(16094, pos, npc.getMapAreaNameHash(), Misc.random(8), npc.canBeAttackFromOutOfArea(), npc.isSpawned()));
                    });
                    break;
                case 10:
                case 11:
                    //TODO searing flames.
                    final Position landing = target.getPosition().relative(Misc.random(-3, 3), Misc.random(-3, 3));
                    int time = COMBUSTION_PROJ.send(npc, landing);
                    npc.addEvent(event -> {
                        event.delay(time);
                        for (int radius = 0; radius < 6; radius++) {
                            List<Position> tiles = landing.area(radius, Position::isUnclipped);
                            tiles.forEach(tile -> {
                                World.sendGraphics(npc, COMBUSTION, tile);
                                for (Entity t : npc.getPossibleTargets()) {
                                    if (t.getPosition().matches(tile)) {
                                        t.applyHit(new Hit(npc).look(Hit.HitLook.REGULAR_DAMAGE).max(580, 1200));
                                    }
                                }
                            });
                            event.delay(1);
                        }
                    });
                    AOE.delay(50);
                    return 10;
            }
        }
        return 3;
    }

    static {
        World.startEvent(event -> {
            NPC dragon = null;
            while (true) {
                event.delay(36_000); //every 6 hours
                if (dragon == null || dragon.isDead() || dragon.isFinished()) {
                    World.sendWorldMessage("<col=ff0000><img=19>[EventManager]: The Amethyst dragon has appeared in its lair!", false);
                    dragon = new NPC(16103, Position.of(3812, 2527, 0), -1, true, true);
                    AmethystDragonCombat.isSpawned = true;
                } else { //if its still alive when the timer rolls around
                    World.sendWorldMessage("<col=ff0000><img=19>[EventManager]: Reminder, the Amethyst dragon is still alive.", false);
                }
            }
        });
    }

}
