package com.rs.game.world.entity.npc.combat.impl.sire;

import com.rs.Constants;
import com.rs.game.world.Projectile;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.utility.Misc;
import com.rs.utility.TickDelay;

/**
 * @author ReverendDread
 * Created 3/8/2021 at 10:24 AM
 * @project 718---Server
 */
public class ScionCombat extends CombatScript {

    private static final Projectile PROJECTILE = new Projectile(Graphics.createOSRS(628).getId(), 28, 43, 20, 16, 35, 0); //TODO find correct gfx id

    private TickDelay transformDelay = new TickDelay();
    private boolean melee = false;

    @Override
    public Object[] getKeys() {
        return new Object[] { NPC.asOSRS(5918) };
    }

    @Override
    public int attack(NPC npc, Entity target) {

        if (npc.getId() != NPC.asOSRS(5918) && !transformDelay.allowed()) { //transform
            npc.addEvent(event -> {
                npc.lock();
                npc.transformIntoNPC(NPC.asOSRS(5918));
                npc.animate(Animation.createOSRS(7123));
                transformDelay.delay(30);
                npc.unlock();

            });
        } else if (npc.getId() == NPC.asOSRS(5918) && !transformDelay.allowed()) {
            npc.sendDeath(null);
            return 1;
        }

        if (melee) {
            if (!target.withinDistance(npc.getMiddleTile(), 1)) {
                npc.addWalkSteps(target.getX(), target.getY());
                return 1;
            }
            npc.animate(npc.getCombatDefinitions().getAttackEmote());
            target.applyHit(new Hit(npc).look(Hit.HitLook.MELEE_DAMAGE).max(0, 150));
        } else {
            if (!target.withinDistance(npc.getMiddleTile(), 4)) {
                npc.addWalkSteps(target.getX(), target.getY());
                return 1;
            }
            npc.animate(npc.getId() == NPC.asOSRS(5918) ? Animation.createOSRS(7127) : Animation.createOSRS(npc.getCombatDefinitions().getAttackEmote() - Constants.OSRS_SEQ_OFFSET));
            int delay = PROJECTILE.send(npc, target);
            target.applyHit(new Hit(npc).look(Hit.HitLook.RANGE_DAMAGE).delay(delay));
        }

        if (Misc.get() < 0.6)
            melee = !melee;

        return npc.getCombatDefinitions().getAttackDelay();
    }

}
