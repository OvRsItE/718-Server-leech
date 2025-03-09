package com.rs.game.world.entity.npc.combat.impl;

import com.google.common.collect.Lists;
import com.rs.game.map.Bounds;
import com.rs.game.map.Position;
import com.rs.game.world.Projectile;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.listeners.HitListener;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.utility.Misc;
import com.rs.utility.TickDelay;

import java.util.List;

/**
 * @author ReverendDread
 * Created 3/21/2021 at 10:18 PM
 * @project 718---Server
 */
public class DarkDemonCombat extends CombatScript {

    //attacks 64, 69
    //16032 minion

    /**
     *
     * shadow attack - 3425
     *
     * darkness hit 3361
     *
     * dark clould - 3298
     *
     * dark gfx - 3062
     *
     * cloud - 3092
     *
     * cloud hit - 3093
     *
     * projectile - 2922
     *
     * projectile - 2947
     *
     * projectile - 2804
     *
     * projectile - 2812
     *
     * eyeball minion - 2847
     *
     * 2815
     */

    private static final Animation MAGIC_ANIM = new Animation(69);
    private static final Animation MELEE_ANIM = new Animation(64);

    private static final Projectile MAGIC_PROJ = new Projectile(2922, 75, 35, 20, 16, 30, 0);
    private static final Projectile CHAIN_PROJ = new Projectile(2804, 75, 35, 20, 16, 30, 0);
    private static final Projectile MINION_PROJ = new Projectile(2847, 75, 35, 20, 16, 30, 0);
    private static final Projectile DARKNESS_WAVE_1 = new Projectile(2812, 75, 35, 20, 16, 30, 0);
    private static final Projectile DARKNESS_WAVE_2 = new Projectile(2813, 75, 35, 20, 16, 30, 0);

    private static final Graphics CLOUD = new Graphics(3092);
    private static final Graphics CLOUD_DOWN = new Graphics(3093);
    private static final Graphics MINION_SPAWN = new Graphics(2847);
    private static final Graphics EXPLOSION = new Graphics(2815);
    private static final Projectile CLOUD_PROJ = new Projectile(CLOUD.getId(), 75, 0, 20, 16, 30, 0);

    private static final Bounds ARENA = new Bounds(3864, 2543, 3879, 2527, 0);

    private static final Bounds NORTH_WEST = new Bounds(3860, 2536, 3872, 2547, 0);
    private static final Bounds NORTH_EAST = new Bounds(3872, 2536, 3882, 2545, 0);
    private static final Bounds SOUTH_WEST = new Bounds(3861, 2525, 3872, 2536, 0);
    private static final Bounds SOUTH_EAST = new Bounds(3872, 2525, 3883, 2536, 0);
    private static final Bounds[] QUADRANTS = { NORTH_WEST, NORTH_EAST, SOUTH_WEST, SOUTH_EAST };

    private final TickDelay cloudCooldown = new TickDelay();
    private boolean enraged;
    private int selectedQuadrant;

    private final List<NPC> minions = Lists.newArrayList();

    @Override
    public boolean init(NPC npc) {
        npc.hitListener = new HitListener().postDamage(hit -> {
            if (npc.getHitpoints() <= npc.getMaxHitpoints() * .25) {
                enraged = true;
            }
        });
        npc.deathEndListener = ((entity, killer) -> {
            enraged = false;
            minions.forEach(NPC::finish);
            cloudCooldown.reset();
        });
        npc.setCanBeFrozen(false);
        npc.setCapDamage(750);
        return true;
    }

    @Override
    public Object[] getKeys() {
        return new Object[] { 16089 };
    }

    @Override
    public int attack(NPC npc, Entity target) {
        minions.removeIf(NPC::isFinished);
        boolean melee = target.withinDistance(npc.getMiddleTile(), 2) && Misc.rollDie(2);
        int roll = Misc.random(100);
        if (enraged) {
            if (melee) {
                npc.animate(MELEE_ANIM);
                delayHit(npc, 1, target, new Hit(npc).look(Hit.HitLook.MELEE_DAMAGE).damage(getRandomMaxHit(npc, 750, NPCCombatDefinitions.MELEE, target)).ignorePrayer());
                return 2;
            } else {
                npc.animate(MAGIC_ANIM);
                for (Entity possibleTarget : npc.getPossibleTargets()) {
                    possibleTarget.addEvent(event -> {

                        Position start = npc.getPosition().copy();
                        Position next = possibleTarget.getPosition().copy();

                        for (int bounce = 0; bounce < 3; bounce++) {
                            int delay = CHAIN_PROJ.send(start, next);
                            event.delay(delay);
                            World.sendGraphics(npc, EXPLOSION, next);
                            if (possibleTarget.getPosition().matches(next)) {
                                possibleTarget.applyHit(new Hit(npc).look(Hit.HitLook.REGULAR_DAMAGE).max(500, 950));
                            }
                            start = next;
                            next = possibleTarget.getPosition().copy();
                            event.delay(1);
                        }

                    });
                }
                return 10;
            }
        } else {
            if (melee) {
                npc.animate(MELEE_ANIM);
                delayHit(npc, 1, target, new Hit(npc).look(Hit.HitLook.MELEE_DAMAGE).damage(getRandomMaxHit(npc, 350, NPCCombatDefinitions.MELEE, target)).ignorePrayer());
            } else if (roll < 85) {
                boolean variant = Misc.rollPercent(50);
                npc.animate(MAGIC_ANIM);
                if (variant) {
                    npc.getPossibleTargets().forEach(t -> {
                        t.addEvent(event -> {
                            int delay = MAGIC_PROJ.send(npc, t);
                            event.delay(delay);
                            int damage = getRandomMaxHit(npc, 350, NPCCombatDefinitions.MAGE, t);
                            if (damage > 0)
                                t.setNextGraphics(3062);
                            t.applyHit(new Hit(npc).damage(damage).look(Hit.HitLook.MAGIC_DAMAGE));
                        });
                    });
                } else {
                    npc.getPossibleTargets().forEach(t -> {
                        t.addEvent(event -> {
                            int delay = Misc.rollPercent(50) ? DARKNESS_WAVE_1.send(npc, t) : DARKNESS_WAVE_2.send(npc, t);
                            event.delay(delay);
                            int damage = getRandomMaxHit(npc, 350, NPCCombatDefinitions.MAGE, t);
                            if (damage > 0)
                                t.setNextGraphics(3062);
                            t.applyHit(new Hit(npc).damage(damage).look(Hit.HitLook.MAGIC_DAMAGE));
                            if (t.isPlayer()) {
                                t.getAsPlayer().getPrayer().drainPrayer(Misc.random(10, 50));
                                t.getAsPlayer().getPackets().sendGameMessage("<col=ff0000>Your prayer is drained slightly.", true);
                            }
                        });
                    });
                }
            } else if (roll < 90 && cloudCooldown.allowed()) {
                npc.animate(MAGIC_ANIM);


                selectedQuadrant = Misc.random(QUADRANTS.length);
                npc.addEvent(event -> {

                    for (int i = 0; i < QUADRANTS.length; i++) {
                        if (i != selectedQuadrant) {
                            Bounds quadrant = QUADRANTS[i];
                            quadrant.forEachPos(pos -> {
                                if (!pos.isClipped()) World.sendGraphics(npc, CLOUD, pos);
                            });
                        }
                    }

                    event.delay(14);

                    //check if everyone is in the safe zone.
                    npc.getPossibleTargets().forEach(t -> {
                        if (!t.getBounds().intersects(QUADRANTS[selectedQuadrant])) {
                            t.setNextGraphics(CLOUD_DOWN);
                            t.applyHit(new Hit(npc).look(Hit.HitLook.REGULAR_DAMAGE).damage(t.getHitpoints()));
                        }
                    });

                });

//                for (int i = 0; i < Misc.random(3, 7); i++) {
//                    final Position pos = ARENA.randomPosition();
//                    npc.addEvent(event -> {
//                        int delay = CLOUD_PROJ.send(npc, pos);
//                        event.delay(delay);
//                        World.sendGraphics(npc, CLOUD, pos);
//                        event.delay(16);
//                        World.sendGraphics(npc, CLOUD_DOWN, pos);
//                        for (Entity possibleTarget : npc.getPossibleTargets()) {
//                            if (possibleTarget.withinDistance(pos, 1)) {
//                                possibleTarget.applyHit(new Hit(npc).look(Hit.HitLook.REGULAR_DAMAGE).max(150, 550));
//                            }
//                        }
//                    });
//                }
                cloudCooldown.delay(Misc.random(75, 100));
            } else if (roll < 100 && minions.size() < 5) {
                //minion
                npc.animate(MAGIC_ANIM);
                npc.addEvent(event -> {
                    final Position pos = ARENA.randomPosition();
                    int delay = MINION_PROJ.send(npc, pos);
                    event.delay(delay);
                    NPC minion = World.spawnNPC(16032, pos, npc.getMapAreaNameHash(), 0, npc.canBeAttackFromOutOfArea(), npc.isSpawned());
                    minion.setForceAgressive(true);
                    minion.setAtMultiArea(true);
                    minion.setTarget(target);
                    minions.add(minion);
                });
            }
        }
        return 4;
    }

}
