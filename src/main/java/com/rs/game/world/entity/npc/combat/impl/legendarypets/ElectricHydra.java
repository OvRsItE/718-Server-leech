package com.rs.game.world.entity.npc.combat.impl.legendarypets;

import com.rs.game.world.Projectile;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.player.CombatDefinitions;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.utility.Utils;

public class ElectricHydra extends CombatScript {

    private static final Projectile MAGIC = new Projectile( 6662, 24, 36, 40, 16, 30, 1);
    private static final Projectile SPECIAL = new Projectile( 6662, 24, 36, 40, 16, 30, 1);

    @Override
    public Object[] getKeys() {
        return new Object[] {16121};
    }

    @Override
    public int attack(NPC npc, Entity target) {
        int random = Utils.random(10);
        int max_hit = npc.getCombatDefinitions().getMaxHit();
        if (random >= 1 && random <= 10) {
            magic_attack(npc, target, max_hit);
        }
        if (random == 0) {
            SPECIAL(npc, target, max_hit);

        }

        return npc.getCombatDefinitions().getAttackDelay();
    }

    private void magic_attack(NPC npc, Entity target, int max_hit) {
        npc.animate(new Animation(28263));
        delayHit(npc, target, MAGIC, new Hit(npc, getRandomMaxHit(npc, max_hit, CombatDefinitions.MAGIC_ATTACK, target), Hit.HitLook.MAGIC_DAMAGE));
    }
    private void SPECIAL(NPC npc, Entity target, int max_hit) {
        npc.setNextForceTalk("Full Power!");
        npc.animate(new Animation(28263));
        delayHit(npc, target, SPECIAL, new Hit(npc, getRandomMaxHit(npc, max_hit, CombatDefinitions.MAGIC_ATTACK, target), Hit.HitLook.MAGIC_DAMAGE));
        delayHit(npc, target, SPECIAL, new Hit(npc, getRandomMaxHit(npc, max_hit, CombatDefinitions.MAGIC_ATTACK, target), Hit.HitLook.MAGIC_DAMAGE));
        delayHit(npc, target, SPECIAL, new Hit(npc, getRandomMaxHit(npc, max_hit, CombatDefinitions.MAGIC_ATTACK, target), Hit.HitLook.MAGIC_DAMAGE));
        delayHit(npc, target, SPECIAL, new Hit(npc, getRandomMaxHit(npc, max_hit, CombatDefinitions.MAGIC_ATTACK, target), Hit.HitLook.MAGIC_DAMAGE));
    }


}

