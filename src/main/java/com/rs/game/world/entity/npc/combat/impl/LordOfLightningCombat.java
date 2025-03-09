package com.rs.game.world.entity.npc.combat.impl;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
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
import com.rs.game.world.entity.updating.impl.NewForceMovement;
import com.rs.utility.Misc;
import com.rs.utility.TickDelay;
import com.rs.utility.Utils;

/**
 * @author ReverendDread
 * Created 3/20/2021 at 1:47 PM
 * @project 718---Server
 */
public class LordOfLightningCombat extends CombatScript {

    private static final int KNOCKBACK = 734;
    private static final int LIGHTNING_THROW = 57018;
    private static final int UPPER_CUT = 58893;
    private static final int UNDER_CUT = 57021;
    private static final int FLOAT = 58895;
    private static final Integer[] POWER_FIST = { UPPER_CUT, UNDER_CUT };
    private static final Graphics SHIELD = new Graphics(3440, 0, 35);
    private static final Graphics CHARGE_UP = new Graphics(3411);
    private static final Graphics LIGHTNING_HIT = new Graphics(3434, 0, 50);
    private static final Graphics DTD_HIT = new Graphics(4006/* 3428 */);
    private static final Graphics DEATH_SPOT = new Graphics(3417);
    private static final Projectile LIGHTNING_CLOUD_PROJ = new Projectile(3433, 54, 30, 20, 16, 80, 0);
    private static final Projectile LIGHTNING_PROJ = new Projectile(2950, 74, 30, 20, 16, 40, 0);
    private static final Projectile LIGHTNING_ORB = new Projectile(3378, 74, 30, 20, 16, 40, 0);
    private static final Bounds ARENA = new Bounds(3738, 2539, 3757, 2519, 0);
    private static final Bounds FORCE_CLIP = new Bounds(3730, 2519, 3737, 2547, 0);

    private boolean enraged;
    private final TickDelay healingTime = new TickDelay();
    private final TickDelay healingCooldown = new TickDelay();
    private final TickDelay knockbackCooldown = new TickDelay();
    private final TickDelay deathSpots = new TickDelay();

    @Override
    public boolean init(NPC npc) {
        npc.hitListener = new HitListener().preDamage(hit -> {
            if (npc.getHitpoints() < npc.getMaxHitpoints() * .50D && healingTime.allowed() && healingCooldown.allowed()) {
                npc.animate(-1);
                npc.animate(FLOAT);
                healingTime.delay(15);
                npc.getCombat().setCombatDelay(healingTime.remaining());
                healingCooldown.delay(50);
                enraged = true;
            }
            if (!healingTime.allowed()) {
                npc.setNextGraphics(SHIELD);
                hit.setHealHit();
                if (hit.getSource() != null && hit.getSource().isPlayer()) {
                    hit.getSource().getAsPlayer().getPackets().sendGameMessage("<col=ff0000>Your hit is absorbed by the Lord of Lightning.", true);
                }
            }
        });
        npc.deathEndListener = ((entity, killer) -> {
            enraged = false;
            healingTime.reset();
            healingCooldown.reset();
            knockbackCooldown.reset();
            deathSpots.reset();
        });
        FORCE_CLIP.forEachPos(Position::clip);
        npc.setAttackDistance(8);
        npc.setCanBeFrozen(false);
        npc.setCapDamage(750);
        npc.setForceTargetDistance(12);
        return true;
    }

    @Override
    public Object[] getKeys() {
        return new Object[] { 16087 };
    }

    @Override
    public int attack(NPC npc, Entity target) {
        int random = Misc.random(100);
        if (random >= 0 && random <= 15 && knockbackCooldown.allowed()) {
            npc.animate(Misc.get(POWER_FIST));
            npc.getPossibleTargets().forEach(t -> knock_back(npc, t));
            knockbackCooldown.delay(Misc.random(50, 75));
            return 6;
        } else if (random > 15 && enraged && deathSpots.allowed()) {
            for (int i = 0; i < Misc.random(5, 7); i++) {
                final Position pos = ARENA.randomPosition();
                final int delay = LIGHTNING_ORB.send(npc, pos);
                npc.addEvent(event -> {
                    event.delay(delay);
                    for (int tick = 0; tick < 3; tick++) {
                        World.sendGraphics(npc, DEATH_SPOT, pos);
                        event.delay(2);
                    }
                    Bounds bound = new Bounds(pos.getX() - 2, pos.getY() - 1, pos.getX() + 2, pos.getY() + 1, 0);
                    bound.forEachPos(p -> {
                        World.sendGraphics(npc, CHARGE_UP, p);
                    });
                    event.delay(2);
                    for (Entity possibleTarget : npc.getPossibleTargets()) {
                        if (possibleTarget.getBounds().intersects(bound)) {
                            possibleTarget.applyHit(new Hit(npc).look(Hit.HitLook.REGULAR_DAMAGE).damage(possibleTarget.getHitpoints()));
                        }
                    }
                });
            }
            deathSpots.delay(Misc.random(50, 75));
            return 10;
        } else {
            npc.animate(LIGHTNING_THROW);
            npc.addEvent(event -> {
                for (Entity possibleTarget : npc.getPossibleTargets()) {
                    int delay = LIGHTNING_PROJ.send(npc, possibleTarget);
                    event.delay(delay);
                    possibleTarget.applyHit(new Hit(npc).look(Hit.HitLook.MAGIC_DAMAGE).damage(getRandomMaxHit(npc, enraged ? 80 : 140, NPCCombatDefinitions.MAGE, possibleTarget)).ignorePrayer());
                    possibleTarget.setNextGraphics(LIGHTNING_HIT);
                }
            });
            return enraged ? 1 : 3;
        }
    }

    /**
     * Callistos knockback attack.
     * @param npc
     * 			callisto.
     * @param target
     * 			the target.
     */
    private void knock_back(NPC npc, Entity target) {
        Position tile = getKnockbackTile(target, npc);
        final boolean dontMove = tile == null || tile.matches(target);
        target.getAsPlayer().lock(2);
        target.animate(Animation.createOSRS(KNOCKBACK));
        if (!dontMove) {
            target.setNextForceMovement(new NewForceMovement(target, 0, tile, 1, Utils.getAngle(tile.getX() - target.getX(), tile.getY() - target.getY())));
            target.setNextPosition(tile);
        }
        npc.addEvent(e -> {
            target.addEvent(event -> {
                event.delay(3);
                int cycle = 0;
                while (cycle++ < 4) {
                    tile.area(1, p -> true).forEach(pos -> {
                        if (!pos.isClipped()) {
                            World.sendGraphics(npc, DTD_HIT, pos);
                            if (target.getPosition().matches(pos))
                                target.applyHit(new Hit(npc).look(Hit.HitLook.REGULAR_DAMAGE).max(250, 990));
                        }
                    });
                    event.delay(1);
                }
            });
            target.getAsPlayer().resetTarget();
        });
    }

    /**
     * Gets the target tile from knockback.
     * @param target
     * 			the target entity.
     * @return
     * 			the target tile.
     */
    public Position getKnockbackTile(Entity target, NPC npc) {
        byte[] dirs = Utils.getDirection(npc.getDirection());
        int distance = Utils.random(3, 5);
        Position destination = new Position(target.getX() + (dirs[0] * distance), target.getY() + (dirs[1] * distance), target.getZ());
        if (Utils.colides(target.getX(), target.getY(), target.getSize(), destination.getX(), destination.getY(), target.getSize()) || !World.isTileFree(destination.getZ(), destination.getX(), destination.getY(), target.getSize()))
            return target.copy();
        return destination;
    }

}
