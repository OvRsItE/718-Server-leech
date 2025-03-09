package com.rs.game.world.entity.npc.combat.impl.karuulm;

import com.rs.game.world.Projectile;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.listeners.HitListener;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.Slayer;
import com.rs.game.world.entity.player.controller.impl.KaruulmController;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.utility.Misc;

/**
 * @author ReverendDread
 * Created 3/12/2021 at 8:08 AM
 * @project 718---Server
 */
public class WyrmCombat extends CombatScript {

    private static final int MELEE_ATTACK = 8270, MAGIC_ATTACK = 8271;
    private static final int PASSIVE = 8610, AGGRESSIVE = 8611;
    private static final int TRANSFORM_ANIM = 8268;

    private static final Graphics GRAPHICS = Graphics.createOSRS(1635); //TODO hit gfx
    private static final Projectile PROJECTILE = new Projectile(Graphics.asOSRS(1634), 50, 30, 30, 16, 50, 0);

    @Override
    public Object[] getKeys() {
        return new Object[] { "Wyrm" };
    }

    @Override
    public boolean init(NPC npc) {
        npc.hitListener = new HitListener().preDamage(hit -> {
           if (npc.getId() == NPC.asOSRS(PASSIVE)) {
                npc.transformIntoNPC(NPC.asOSRS(AGGRESSIVE));
                npc.animate(Animation.createOSRS(TRANSFORM_ANIM));
           }
           if (hit.getSource() != null && hit.getSource().isPlayer()) {
               Player plr = hit.getSource().getAsPlayer();
               boolean offtask = !plr.getSlayerManager().hasTask(Slayer.SlayerTask.WYRM);
               boolean inBounds = KaruulmController.isInSlayerOnlyArea(plr);
               System.out.println(offtask + ", " + inBounds);
               if (offtask && inBounds) {
                    System.out.println("were in the bounds & have a task!");
                    hit.negate();
               }
           }
        });
        npc.deathEndListener = (entity, killer) -> {
            npc.transformIntoNPC(NPC.asOSRS(PASSIVE));
        };
        return true;
    }

    @Override
    public int attack(NPC npc, Entity target) {
        boolean melee = Misc.random(2) == 1 && target.withinDistance(npc.getMiddleTile(), 2);
        npc.animate(Animation.createOSRS(melee ? MELEE_ATTACK : MAGIC_ATTACK));
        if (!melee) {
            int delay = PROJECTILE.send(npc, target);
            npc.addEvent(event -> {
               event.delay(delay);
               Hit hit = new Hit(npc).damage(getRandomMaxHit(npc, 130, NPCCombatDefinitions.MAGE, target)).look(Hit.HitLook.MAGIC_DAMAGE);
               if (hit.getDamage() > 10)
                   target.setNextGraphics(GRAPHICS);
               target.applyHit(hit);
            });
        } else
            delayHit(npc, 1, target, new Hit(npc).damage(getRandomMaxHit(npc, 130, NPCCombatDefinitions.MELEE, target)).look(Hit.HitLook.MELEE_DAMAGE));
        return 4;
    }

}
