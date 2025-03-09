package com.rs.game.world.entity.npc.combat.impl.eventboss;

import com.rs.game.map.Bounds;
import com.rs.game.map.Position;
import com.rs.game.world.Projectile;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.utility.Misc;
import com.rs.utility.TickDelay;
import com.rs.utility.Utils;

import java.util.List;

public class EnragedOlm extends CombatScript {

    private static final Projectile CRYSTAL_SPIKE_PROJECTILE = new Projectile(Graphics.createOSRS(1352).getId(), 200, 0, 0, 30, 0, 0);

    private static final Projectile ACID_POOL_PROJECTILE = new Projectile(Graphics.createOSRS(1354).getId(), 90, 0, 30, 100, 0, 16);

    private static final Projectile BURN_PROJECTILE = new Projectile(Graphics.createOSRS(1350).getId(), 90, 43, 35, 20, 6, 16);
    private static final Projectile FLAME_WALL_PROJECTILE_1 = new Projectile(Graphics.createOSRS(1347).getId(), 90, 0, 15, 55, 0, 16);
    private static final Projectile FLAME_WALL_PROJECTILE_2 = new Projectile(Graphics.createOSRS(1348).getId(), 0, 0, 0, 30, 0, 16);

    private static final Projectile SIPHON_PROJECTILE = new Projectile(Graphics.createOSRS(1355).getId(), 90, 0, 30, 100, 0, 16);

    private static final Projectile MAGIC_PROJECTILE = new Projectile(Graphics.createOSRS(1339).getId(), 90, 36, 40, 16, 30, 1);
    private static final Projectile RANGED_PROJECTILE = new Projectile(Graphics.createOSRS(1340).getId(), 90, 36, 40, 16, 30, 1);

    private static final Projectile MAGIC_SPHERE = new Projectile(Graphics.createOSRS(1341).getId(), 90, 43, 30, 150, 0, 16);
    private static final Projectile RANGED_SPHERE = new Projectile(Graphics.createOSRS(1343).getId(), 90, 43, 30, 150, 0, 16);
    private static final Projectile MELEE_SPHERE = new Projectile(Graphics.createOSRS(1345).getId(), 90, 36, 40, 16, 30, 1);
    private static final Graphics FIRE = Graphics.createOSRS(1667);
    private static final Graphics FALLING_BEAM = new Graphics(4020);

    private static final Bounds ARENA = new Bounds(3666, 4445, 3699, 4466, 0);
    private static final Bounds NORTH_WEST = new Bounds(3660, 4450, 3675, 4464, 0);
    private static final Bounds NORTH_EAST = new Bounds(3688, 4453, 3694, 4462, 0);
    public static boolean isSpawned;

    public static boolean getSpawned() {
        return isSpawned;
    }

    //head anims
    //7335 - rise
    //7336 - facing center
    //7337 - facing right
    //7338 - facing left
    //7339 - mid to face right
    //7340 - right to face mid
    //7341 - mid to face left
    //7342 - left to face mid
    //7343 - left to face right
    //7344 - right to face left
    //7345 - attack (mid)
    //7346 - attack (right)
    //7347 - attack (left)
    //7348 - go down
    //7371 - mid attack (empowered)
    //7372 - left attack (empowered)
    //7373 - right attack (empowered)
    //7374 - idle mid (empowered)
    //7375 - idle left (empowered)
    //7376 - idle right (empowered)
    //7377 - mid to face left (empowered)
    //7378 - left to face mid(empowered)
    //7379 - left to face right(empowered)
    //7380 - right to face left (empowered)
    //7381 - mid to face right (empowered)
    //7382 - right to face mid (empowered)
    //7383 - rise (empowered)
    //attack animation - 27345
    //Left attack - 27346
    //right attack 27347
    //death 27348
    //spawn animation - 27335

    //gfx
    //normal dragon fire - 393 - hit 157
    //red orb 3372
    // red field 3238

    @Override
    public Object[] getKeys() {
        return new Object[] { 16083 };
    }
    @Override
    public boolean init(NPC npc) {
        npc.spawnListener = (entity) -> isSpawned = true;
        npc.deathEndListener = ((entity, killer) -> {
            isSpawned = false;

        });
        npc.setCantWalk(true);
        npc.setCanBeFrozen(false);
        npc.setCapDamage(750);
        npc.setForceTargetDistance(16);
        npc.setCantFollowUnderCombat(true);
        return true;
    }

    @Override
    public int attack(NPC npc, Entity target) {

        List<Entity> targets = npc.getPossibleTargets();
        for (Entity entity : targets) {
            int random = Utils.random(11);
            if (random == 0) {
                meteor_attack(npc, entity);

            }
            else if (random >= 1 && random <= 3) {
                melee_attack(npc, entity);
            }
            else if (random >= 3 && random <= 6) {
                mage_attack(npc, entity);
            }
            else if (random >= 6 && random <= 10) {
                range_attack(npc, entity);
            }
        }
        npc.addEvent(event -> {
            event.delay(1);
            npc.animate(-1);
        });
        return npc.getCombatDefinitions().getAttackDelay();


    }


    private void melee_attack(NPC npc, Entity target) {
        npc.animate(Animation.createOSRS(7345));
        delayHit(npc, target, MELEE_SPHERE, new Hit(npc, getRandomMaxHit(npc, 500,
                NPCCombatDefinitions.MELEE, target), Hit.HitLook.MELEE_DAMAGE));
    }

    private void mage_attack(NPC npc, Entity target) {
        npc.animate(Animation.createOSRS(7345));
        delayHit(npc, target, MAGIC_PROJECTILE, new Hit(npc, getRandomMaxHit(npc, 500,
                NPCCombatDefinitions.MAGE, target), Hit.HitLook.MAGIC_DAMAGE));
    }

    private void range_attack(NPC npc, Entity target) {
        npc.animate(Animation.createOSRS(7345));
        delayHit(npc, target, RANGED_PROJECTILE, new Hit(npc, getRandomMaxHit(npc, 500,
                NPCCombatDefinitions.RANGE, target), Hit.HitLook.RANGE_DAMAGE));
    }

        private void meteor_attack (NPC npc, Entity target) {
            for (int i = 0; i < Misc.random(10, 20); i++) {
                npc.animate(Animation.createOSRS(7345));
                final Position pos = ARENA.randomPosition();
                npc.addEvent(event -> {
                    Bounds bound = new Bounds(pos.getX() - 2, pos.getY() - 1, pos.getX() + 2, pos.getY() + 1, 0);
                    bound.forEachPos(p -> {
                        World.sendGraphics(npc, FALLING_BEAM, p);
                    });
                    event.delay(2);
                    for (Entity possibleTarget : npc.getPossibleTargets()) {
                        if (possibleTarget.getBounds().intersects(bound)) {
                            possibleTarget.applyHit(new Hit(npc).look(Hit.HitLook.REGULAR_DAMAGE).damage(possibleTarget.getHitpoints()));
                        }
                    }
                });
            }
        }
    static {
        World.startEvent(event -> {
            NPC dragon = null;
            while (true) {
                event.delay(13000); //every 2 hours
                if (dragon == null || dragon.isDead() || dragon.isFinished()) {
                    World.sendWorldMessage("<col=ff0000><img=19>[EventManager]: A enraged olm has spawned in the burnt areana!", false);
                    dragon = new NPC(16083, Position.of(3681, 4450, 0), -1, true, true);
                    EnragedOlm.isSpawned = true;
                } else { //if its still alive when the timer rolls around
                    World.sendWorldMessage("<col=ff0000><img=19>[EventManager]: Reminder, enraged olm is still alive.", false);
                }
            }
        });
    }
}
