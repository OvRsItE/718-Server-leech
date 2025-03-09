package com.rs.game.world.entity.npc.combat.impl.karuulm;

import com.rs.game.map.Position;
import com.rs.game.world.Projectile;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.listeners.HitListener;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.Combat;
import com.rs.game.world.entity.player.content.Slayer;
import com.rs.game.world.entity.player.controller.impl.KaruulmController;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.utility.Misc;

/**
 * @author ReverendDread
 * Created 3/13/2021 at 4:27 AM
 * @project 718---Server
 */
public class DrakeCombat extends CombatScript {

    private static final Projectile RANGE_PROJECTILE = new Projectile(Graphics.asOSRS(1636), 25, 31, 30, 16, 30, 0);
    private static final Projectile SPECIAL_PROJECTILE = new Projectile(Graphics.asOSRS(1637), 25, 0, 85, 16, 30, 0);
    private int count = 0;

    @Override
    public Object[] getKeys() {
        return new Object[] { "Drake" };
    }

    @Override
    public boolean init(NPC npc) {
        npc.deathEndListener = (entity, killer) -> npc.transformIntoNPC(NPC.asOSRS(8612));
        npc.hitListener = new HitListener().preDamage(hit -> {
            if (hit.getSource() != null && hit.getSource().isPlayer()) {
                Player plr = hit.getSource().getAsPlayer();
                if (!plr.getSlayerManager().hasTask(Slayer.SlayerTask.WYRM) && npc.inBounds(KaruulmController.SLAYER_ONLY_BOUNDS[0])) {
                    hit.negate();
                }
            }
        });
        npc.setFollowDistance(6);
        return true;
    }

    @Override
    public int attack(NPC npc, Entity target) {
        if (target.withinDistance(npc.getMiddleTile(), 2) && Misc.rollDie(2, 1)) {
            delayHit(npc, 1, target, new Hit(npc).damage(getRandomMaxHit(npc, 150, NPCCombatDefinitions.MELEE, target)).look(Hit.HitLook.MELEE_DAMAGE));
            npc.animate(Animation.createOSRS(8275));
        } else if (count > 6) {
            specialAttack(npc, target);
            count = 0;
        } else
            rangedAttack(npc, target);
        count++;
        return 4;
    }

    private void rangedAttack(NPC npc, Entity target) {
        projectileAttack(npc, target, RANGE_PROJECTILE, new Hit(npc).damage(getRandomMaxHit(npc, 150, NPCCombatDefinitions.RANGE, target)).look(Hit.HitLook.RANGE_DAMAGE), Animation.createOSRS(8276).getId());
    }

    private void specialAttack(NPC npc, Entity target) {
        npc.animate(Animation.createOSRS(8276));
        final Position targetPos = target.copy();
        int delay = SPECIAL_PROJECTILE.send(npc, target);
        npc.addEvent(event -> {
            event.delay(delay);
            World.sendGraphics(npc, Graphics.createOSRS(1638), targetPos);
            if (target.getPosition().matches(targetPos)) {
                target.applyHit(new Hit(npc).max(240, 320).look(Hit.HitLook.REGULAR_DAMAGE));
            }
        });
    }

}
