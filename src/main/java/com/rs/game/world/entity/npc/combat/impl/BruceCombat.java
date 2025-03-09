package com.rs.game.world.entity.npc.combat.impl;

import com.rs.game.map.Bounds;
import com.rs.game.map.Position;
import com.rs.game.world.Projectile;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.CombatDefinitions;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.utility.Misc;
import com.rs.utility.Utils;

import java.util.List;


public class BruceCombat extends CombatScript {

    private static final Projectile NORMALBUBBLES = new Projectile( 4023, 35, 36, 40, 16, 30, 1);
    private static final Projectile RANGEBUBBLE = new Projectile( 4032, 35, 36, 40, 16, 30, 1);
    private static final Projectile BARRGEBUBBLES = new Projectile( 4024, 35, 36, 40, 16, 30, 1);
    private static final Projectile BEAMS = new Projectile( 4033, 35, 36, 40, 16, 30, 1);
    private static final Graphics FALLING_BEAM = new Graphics(4025);
    private static final Bounds ARENA = new Bounds(2971, 9515, 2984, 9520, 1);

    @Override
    public Object[] getKeys() {
        return new Object[]{"Bruce"};
    }

    public boolean init(NPC npc) {
        npc.setCanBeFrozen(false);
        npc.setCapDamage(750);
        npc.setForceTargetDistance(8);
        return true;
    }

    @Override
    public int attack(NPC npc, Entity target) {
        List<Entity> targets = npc.getPossibleTargets(false, true);
        int max_hit = npc.getCombatDefinitions().getMaxHit();
        int random = Utils.random(7);
        for (Entity entity : targets) {
            if (entity.isNPC())
                continue;
            if (random >= 3 && random <= 4) {
                npc.animate(3433);
                magic_attack(npc, entity, max_hit);

            }
            if (random >= 5 && random <= 7) {
                npc.animate(3433);
                range_attack(npc, entity, max_hit);

            }

            if (random == 1) {
                npc.animate(3434);
                Barrage_attack(npc, entity, max_hit);

            }

            if (random == 2) {
                npc.animate(3434);
                Beam_attack(npc, entity, max_hit);

            }

            if (random == 0) {
                npc.animate(3434);
                meteor_attack(npc, entity);

            }
        }
        return npc.getCombatDefinitions().getAttackDelay() + 2;
    }


    private void magic_attack(NPC npc, Entity target, int max_hit) {
        delayHit(
                npc,
                3,
                target,
                getMagicHit(
                        npc,
                        getRandomMaxHit(npc, max_hit,
                                NPCCombatDefinitions.MAGE, target)));
        World.sendProjectile(npc, target, NORMALBUBBLES);
        //delayHit(npc, target, NORMALBUBBLES, new Hit(npc, getRandomMaxHit(npc, max_hit, CombatDefinitions.MAGIC_ATTACK, target), Hit.HitLook.MAGIC_DAMAGE));
    }

    private void range_attack(NPC npc, Entity target, int max_hit) {
        delayHit(
                npc,
                3,
                target,
                getRangeHit(
                        npc,
                        getRandomMaxHit(npc, max_hit,
                                NPCCombatDefinitions.RANGE, target)));
        World.sendProjectile(npc, target, RANGEBUBBLE);
    }

    private void Barrage_attack(NPC npc, Entity target, int max_hit) {
        delayHit(
                npc,
                3,
                target,
                getMagicHit(
                        npc,
                        getRandomMaxHit(npc, max_hit,
                                NPCCombatDefinitions.MAGE, target)));
        delayHit(
                npc,
                3,
                target,
                getMagicHit(
                        npc,
                        getRandomMaxHit(npc, max_hit,
                                NPCCombatDefinitions.MAGE, target)));
        delayHit(
                npc,
                3,
                target,
                getMagicHit(
                        npc,
                        getRandomMaxHit(npc, max_hit,
                                NPCCombatDefinitions.MAGE, target)));
        World.sendProjectile(npc, target, BARRGEBUBBLES);
    }

    private void Beam_attack(NPC npc, Entity target, int max_hit) {
        delayHit(
                npc,
                3,
                target,
                getRangeHit(
                        npc,
                        getRandomMaxHit(npc, max_hit,
                                NPCCombatDefinitions.RANGE, target)));
        World.sendProjectile(npc, target, BEAMS);
    }

    private void meteor_attack (NPC npc, Entity target) {
        for (int i = 0; i < Misc.random(2, 5); i++) {
            final Position pos = ARENA.randomPosition();
            npc.addEvent(event -> {
                Bounds bound = new Bounds(pos.getX() - 2, pos.getY() - 1, pos.getX() + 2, pos.getY() + 1, 1);
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
}
