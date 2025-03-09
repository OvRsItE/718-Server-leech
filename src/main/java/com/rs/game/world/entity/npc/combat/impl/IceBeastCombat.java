package com.rs.game.world.entity.npc.combat.impl;

import com.rs.game.map.Bounds;
import com.rs.game.map.Position;
import com.rs.game.world.Projectile;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.player.CombatDefinitions;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.utility.Misc;
import com.rs.utility.Utils;

import java.util.List;

/**
 * @author ReverendDread
 * Created 3/27/2021 at 8:23 PM
 * @project 718---Server
 */

/**
 * Attacks:2731
 * Defence:2732
 * Death:2733
 */

public class IceBeastCombat extends CombatScript {

    private static final Projectile NORMALBUBBLES = new Projectile( 4027, 35, 36, 40, 16, 30, 1);
    private static final Projectile BARRGEBUBBLES = new Projectile( 4028, 35, 36, 40, 16, 30, 1);
    private static final Projectile BEAMS = new Projectile( 4026, 35, 36, 40, 16, 30, 1);
    private static final Graphics FALLING_BEAM = new Graphics(4029);
    private static final Bounds ARENA = new Bounds(1246, 2653, 1267, 2679, 0);

    @Override
    public Object[] getKeys() {
        return new Object[]{"Ice Beast"};
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
                npc.animate(2731);
                magic_attack(npc, entity, max_hit);

            }
            if (random >= 5 && random <= 7) {
                npc.animate(2731);
                range_attack(npc, entity, max_hit);

            }

            if (random == 1) {
                npc.animate(2731);
                Barrage_attack(npc, entity, max_hit);

            }

            if (random == 0) {
                npc.animate(13840);
                meteor_attack(npc, entity);

            }
        }
        return npc.getCombatDefinitions().getAttackDelay();
    }


    private void magic_attack(NPC npc, Entity target, int max_hit) {
        delayHit(npc, target, NORMALBUBBLES, new Hit(npc, getRandomMaxHit(npc, max_hit, CombatDefinitions.MAGIC_ATTACK, target), Hit.HitLook.MAGIC_DAMAGE));
    }

    private void range_attack(NPC npc, Entity target, int max_hit) {
        delayHit(npc, target, NORMALBUBBLES, new Hit(npc, getRandomMaxHit(npc, max_hit, CombatDefinitions.MAGIC_ATTACK, target), Hit.HitLook.RANGE_DAMAGE));
    }

    private void Barrage_attack(NPC npc, Entity target, int max_hit) {
        delayHit(npc, target, BARRGEBUBBLES, new Hit(npc, getRandomMaxHit(npc, max_hit, CombatDefinitions.MAGIC_ATTACK, target), Hit.HitLook.MAGIC_DAMAGE));
        delayHit(npc, target, BARRGEBUBBLES, new Hit(npc, getRandomMaxHit(npc, max_hit, CombatDefinitions.MAGIC_ATTACK, target), Hit.HitLook.MAGIC_DAMAGE));
        delayHit(npc, target, BARRGEBUBBLES, new Hit(npc, getRandomMaxHit(npc, max_hit, CombatDefinitions.MAGIC_ATTACK, target), Hit.HitLook.MAGIC_DAMAGE));
    }


    private void meteor_attack (NPC npc, Entity target) {
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
}
}
