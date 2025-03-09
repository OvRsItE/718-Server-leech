package com.rs.game.world.entity.npc.combat.impl.eventboss;

import com.google.common.collect.Lists;
import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
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

public class DarkSireCombat extends CombatScript {



    private static final Projectile SPAWN_PROJECTILE = new Projectile(Graphics.createOSRS(1274).getId(), 70, 0, 20, 16, 30, 0);

    public static boolean isSpawned;
    public static boolean getSpawned() {
        return isSpawned;
    }

    @Override
    public Object[] getKeys() {
        return new Object[]{"Dark sire"};
    }

    public boolean init(NPC npc) {

        npc.setCanBeFrozen(false);
        npc.setCapDamage(750);
        npc.setAttackDistance(8);
        npc.setForceTargetDistance(16);
        return true;
    }

    @Override
    public int attack(NPC npc, Entity target) {
        List<Entity> targets = npc.getPossibleTargets(false, true);
        int max_hit = npc.getCombatDefinitions().getMaxHit();
        int random = Utils.random(4);
        for (Entity entity : targets) {
            if (entity.isNPC())
                continue;
            if (random >= 0 && random <= 2) {
                magic_attack(npc, entity, max_hit);

            }
            if (random >= 3 && random <= 4) {
                Barrage_attack(npc, entity);

            }


        }
        return npc.getCombatDefinitions().getAttackDelay();
    }


    private void magic_attack(NPC npc, Entity target, int max_hit) {
        npc.animate(new Animation(27095));
        delayHit(npc, target, SPAWN_PROJECTILE, new Hit(npc, getRandomMaxHit(npc, max_hit, CombatDefinitions.MAGIC_ATTACK, target), Hit.HitLook.MAGIC_DAMAGE));
    }

    private void Barrage_attack(NPC npc, Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        npc.animate(new Animation(25755));
        delayHit(
                npc,
                2,
                target,
                getMeleeHit(
                        npc,
                        getRandomMaxHit(npc, defs.getMaxHit(),
                                NPCCombatDefinitions.MELEE, target)));
    }



}
