package com.rs.game.world.entity.npc.combat.impl.EliteDungeon;

import com.rs.game.world.Projectile;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.utility.Utils;


public class HealingPool extends CombatScript {

    private static final Projectile MAGE_PROJECTILE = new Projectile( (Graphics.createOSRS(1355).getId()), 0, 25, 75, 16, 30, 1);
    private static final Projectile TELEPORT_PROJECTILE = new Projectile( 6327, 35, 36, 40, 16, 40, 1);
    @Override
    public Object[] getKeys() {
        return new Object[]{"Healing pool"};
    }

    public boolean init(NPC npc) {
        npc.setForceTargetDistance(14);
        npc.setCantWalk(true);
        npc.setCantFollowUnderCombat(true);
        npc.setForceAgressive(true);
        npc.setCanRetaliate(true);
        return true;
    }


    @Override
    public int attack(NPC npc, Entity target) {
        int random = Utils.random(10);
            if (random >= 1 && random <= 10) {
                if (target instanceof Player) {
                    return 0;
                }
                npc.lock();
                npc.animate(25835);
                target.applyHit(new Hit(npc).max(0, 180).look(Hit.HitLook.HEALED_DAMAGE));
                World.sendProjectile(npc, target, MAGE_PROJECTILE);
            }

        return npc.getCombatDefinitions().getAttackDelay();
    }




}
