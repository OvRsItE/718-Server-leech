package com.rs.game.world.entity.npc.combat.impl.TheHive;

import com.rs.game.map.Bounds;
import com.rs.game.world.Projectile;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.instances.TheHive.BossNpc;
import com.rs.game.world.entity.player.CombatDefinitions;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.utility.Misc;
import com.rs.utility.Utils;

public class QueenBeeCombat extends CombatScript {



    private int selectedQuadrant;
    private static final Graphics MARK = new Graphics(2786);
    private static final Graphics FALLING_STING = new Graphics(4041);
    private static final Graphics EXPLOSION = new Graphics(2815);

    private static final Projectile MAGIC = new Projectile( 4044, 74, 36, 40, 16, 30, 1);

    private static final Projectile RANGE = new Projectile( 4045, 74, 36, 40, 16, 30, 1);


    private static final Projectile EXPLSOIVE_STING = new Projectile( 4043, 74, 36, 40, 16, 18, 1);
    @Override
    public Object[] getKeys() {
        return new Object[] { 16135 };
    }

    @Override
    public int attack(NPC npc, Entity target) {
        int random = Utils.random(10);
        int max_hit = npc.getCombatDefinitions().getMaxHit();
        if (random >= 2 && random <= 6) {
            magic_attack(npc, target, max_hit);

        }
        if (random >= 6 && random <= 10) {
            range_attack(npc, target, max_hit);

        }
        if (random == 1) {
                npc.animate(new Animation(6234));
                stingbarrage(npc, target);

        }
        if (random == 0) {
            npc.animate(new Animation(6234));
            StingDrop(npc, target);

        }
        return npc.getCombatDefinitions().getAttackDelay();
    }

    private void magic_attack(NPC npc, Entity target, int max_hit) {
        npc.animate(new Animation(6235));
        delayHit(npc, target, MAGIC, new Hit(npc, getRandomMaxHit(npc, max_hit, CombatDefinitions.MAGIC_ATTACK, target), Hit.HitLook.MAGIC_DAMAGE));
    }
    private void range_attack(NPC npc, Entity target, int max_hit) {
        npc.animate(new Animation(6235));
        delayHit(npc, target, RANGE, new Hit(npc, getRandomMaxHit(npc, max_hit, CombatDefinitions.MAGIC_ATTACK, target), Hit.HitLook.RANGE_DAMAGE));
    }

    public void StingDrop (NPC npc, Entity target) {
        //Queen_Bee = new BossNpc(16135, getLocation(2991, 5909, 0));
        final Bounds NORTH = new Bounds(npc.getSpawnPosition().getX() - 7, npc.getSpawnPosition().getY() + 5, npc.getSpawnPosition().getX() + 10, npc.getSpawnPosition().getY() + 8, 0);
        final Bounds EAST = new Bounds(npc.getSpawnPosition().getX() + 7, npc.getSpawnPosition().getY() - 6, npc.getSpawnPosition().getX() + 10, npc.getSpawnPosition().getY() + 8, 0);
        final Bounds WEST = new Bounds(npc.getSpawnPosition().getX() - 7 , npc.getSpawnPosition().getY() - 6, npc.getSpawnPosition().getX() - 5, npc.getSpawnPosition().getY() + 8, 0);
        final Bounds SOUTH = new Bounds(npc.getSpawnPosition().getX() - 7, npc.getSpawnPosition().getY() - 4, npc.getSpawnPosition().getX() + 10, npc.getSpawnPosition().getY() -4 , 0);
         final Bounds[] QUADRANTS = { NORTH, EAST, WEST, SOUTH };
        selectedQuadrant = Misc.random(QUADRANTS.length);
        npc.addEvent(event -> {

            for (int i = 0; i < QUADRANTS.length; i++) {
                if (i != selectedQuadrant) {
                    Bounds quadrant = QUADRANTS[i];
                    quadrant.forEachPos(pos -> {
                         World.sendGraphics(npc, MARK, pos);
                    });
                }
            }

            event.delay(8);

            for (int i = 0; i < QUADRANTS.length; i++) {
                if (i != selectedQuadrant) {
                    Bounds quadrant = QUADRANTS[i];
                    quadrant.forEachPos(pos -> {
                        World.sendGraphics(npc, FALLING_STING, pos);
                    });
                }
            }

            event.delay(2);

            //check if everyone is in the safe zone.
            npc.getPossibleTargets().forEach(t -> {
                if (!t.getBounds().intersects(QUADRANTS[selectedQuadrant])) {
                    t.applyHit(new Hit(npc).look(Hit.HitLook.REGULAR_DAMAGE).max(200, 300));
                }
            });

        });

    }
    public void stingbarrage (NPC npc, Entity target) {
        final Bounds NORTH = new Bounds(npc.getSpawnPosition().getX() - 7, npc.getSpawnPosition().getY() + 5, npc.getSpawnPosition().getX() + 10, npc.getSpawnPosition().getY() + 8, 0);
        final Bounds EAST = new Bounds(npc.getSpawnPosition().getX() + 7, npc.getSpawnPosition().getY() - 6, npc.getSpawnPosition().getX() + 10, npc.getSpawnPosition().getY() + 8, 0);
        final Bounds WEST = new Bounds(npc.getSpawnPosition().getX() - 7 , npc.getSpawnPosition().getY() - 6, npc.getSpawnPosition().getX() - 5, npc.getSpawnPosition().getY() + 8, 0);
        final Bounds SOUTH = new Bounds(npc.getSpawnPosition().getX() - 7, npc.getSpawnPosition().getY() - 4, npc.getSpawnPosition().getX() + 10, npc.getSpawnPosition().getY() -4 , 0);
        final Bounds[] QUADRANTS = { NORTH, EAST, WEST, SOUTH };
        selectedQuadrant = Misc.random(QUADRANTS.length);
        npc.addEvent(event -> {
            for (int i = 0; i < QUADRANTS.length; i++) {
                if (i != selectedQuadrant) {
                    Bounds quadrant = QUADRANTS[i];
                    quadrant.forEachPos(pos -> {
                        EXPLSOIVE_STING.send(npc, pos);
                    });
                }
            }

            event.delay(10);

            for (int i = 0; i < QUADRANTS.length; i++) {
                if (i != selectedQuadrant) {
                    Bounds quadrant = QUADRANTS[i];
                    quadrant.forEachPos(pos -> {
                        World.sendGraphics(npc, EXPLOSION, pos);
                    });
                }
            }

            event.delay(2);

            //check if everyone is in the safe zone.
            npc.getPossibleTargets().forEach(t -> {
                if (!t.getBounds().intersects(QUADRANTS[selectedQuadrant])) {
                    t.applyHit(new Hit(npc).look(Hit.HitLook.REGULAR_DAMAGE).max(200, 300));
                }
            });

            event.delay(4);



        });

    }
}
