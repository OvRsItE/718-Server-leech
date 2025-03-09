package com.rs.game.world.entity.npc.combat.impl.legendarypets;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.world.Projectile;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.player.CombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.utility.Utils;

import java.util.List;

public class Nazgul extends CombatScript {

    private static final Projectile MAGIC = new Projectile( 4035, 18, 18, 50, 50, 0, 0);
    private static final Projectile SPECIAL = new Projectile( 374, 24, 36, 40, 16, 30, 1);

    @Override
    public Object[] getKeys() {
        return new Object[] {16177};
    }

    @Override
    public int attack(NPC npc, Entity target) {
        List<Entity> targets = npc.getPossibleTargets(true, false);
        int random = Utils.random(10);
        int max_hit = npc.getCombatDefinitions().getMaxHit();
        if (random >= 3 && random <= 10) {
            magic_attack(npc, target, max_hit);
        }
        for (Entity entity : targets) {
            if (random >= 1 && random <= 3) {
                SPECIAL(npc, entity, max_hit);

            }
        }

        return npc.getCombatDefinitions().getAttackDelay();
    }

    private void magic_attack(NPC npc, Entity target, int max_hit) {
        npc.animate(new Animation(1979));
        delayHit(npc, target, MAGIC, new Hit(npc, getRandomMaxHit(npc, max_hit, CombatDefinitions.MAGIC_ATTACK, target), Hit.HitLook.MAGIC_DAMAGE));
    }
    private void SPECIAL(NPC npc, Entity target, int max_hit) {
        npc.animate(new Animation(1979));
        delayHit(npc, target, MAGIC, new Hit(npc, getRandomMaxHit(npc, max_hit, CombatDefinitions.MAGIC_ATTACK, target), Hit.HitLook.LIFE_STEAL));
        for (final Player p : World.getPlayers()) {
            if (p != null && p.withinDistance(target.getPosition(), 10)) {
                World.sendProjectile(target, p, 2263, 41, 16, 31, 35, 16, 0);
                WorldTasksManager.schedule(new WorldTask() {
                    int ticks;

                    @Override
                    public void run() {
                        ticks++;
                        if (ticks == 2) {
                            p.applyHit(new Hit(p, max_hit / 4, Hit.HitLook.HEALED_DAMAGE));
                        }

                    }
                }, 0, 0);
            }
        }
    }



}

