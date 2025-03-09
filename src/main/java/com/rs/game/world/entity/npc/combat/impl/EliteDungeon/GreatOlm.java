package com.rs.game.world.entity.npc.combat.impl.EliteDungeon;

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
import com.rs.game.world.entity.npc.instances.EliteDungeon.EliteDungeonOne;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.utility.Misc;
import com.rs.utility.Utils;

import java.util.List;

public class GreatOlm extends CombatScript {


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
    private static final Graphics FALLING_BEAM = new Graphics(4034);

    private final List<NPC> healers = Lists.newArrayList();



    //head anims
    //7335 - rise
    //7336 - facing centerh
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
        return new Object[]{16160};
    }

    @Override
    public boolean init(NPC npc) {
        npc.deathEndListener = ((entity, killer) -> {
            healers.forEach(NPC::finish);

        });
        npc.setCantWalk(true);
        npc.setCanBeFrozen(false);
        npc.setCapDamage(750);
        npc.setCantFollowUnderCombat(true);
        npc.faceNone();
        npc.setForceTargetDistance(64);
        npc.setAttackDistance(64);
        return true;
    }

    @Override
    public int attack(NPC npc, Entity target) {
        int random = Utils.random(11);
        List<Entity> targets = npc.getPossibleTargets();
        if (random == 0) {
            Crystals(npc, target);
        }
        if (random == 1) {
            healers(npc, target);
        }
            for (Entity entity : targets) {
            if (random >= 2 && random <= 3) {
                    melee_attack(npc, entity);
                } else if (random >= 3 && random <= 6) {
                    mage_attack(npc, entity);
                } else if (random >= 6 && random <= 10) {
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
    private void healers(NPC npc, Entity target) {
        if (healers.size() < 1) {
            npc.animate(Animation.createOSRS(7345));
            npc.addEvent(event -> {
                final Position pos = new Position(npc.getSpawnPosition().getX() - 8, npc.getSpawnPosition().getY() + 6, 1).relative(Utils.random(5), Utils.random(5));
                int delay = SIPHON_PROJECTILE.send(npc, pos);
                event.delay(delay);
                NPC healer = World.spawnNPC(16166, pos, npc.getMapAreaNameHash(), 0, npc.canBeAttackFromOutOfArea(), npc.isSpawned());
                healer.setForceAgressive(true);
                healer.setAtMultiArea(true);
                healer.setTarget(npc);
                healer.setAttackDistance(12);
                healers.add(healer);
            });
        }
    }

    private void Crystals (NPC npc, Entity target) {
        for (int i2 = 0; i2 < 20; i2++) {
            npc.addEvent(event -> {
                for (int i = 0; i < 40; i++) {
                    final Position pos = new Position(npc.getSpawnPosition().getX() - 8, npc.getSpawnPosition().getY() + 4, 1).relative(Utils.random(24), Utils.random(17));
                    World.sendGraphics(npc, FALLING_BEAM, pos);
                    event.delay(2);
                    for (Entity t : npc.getPossibleTargets()) {
                        if (t.getPosition().matches(pos))
                            t.applyHit(new Hit(npc).look(Hit.HitLook.REGULAR_DAMAGE).damage(200));
                    }
                }
            });
        }
    }

}
