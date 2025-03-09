package com.rs.game.world.entity.npc.combat.impl.xeric;

import com.rs.game.world.Projectile;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.utility.Misc;

/**
 * @author ReverendDread
 * Created 4/25/2021 at 7:52 PM
 * @project 718---Server
 */
public class SkeletalMystic extends CombatScript {

    private static final int VULN_GFX = 1321;
    private static final int FIRE_GFX = 1322;
    private static final int VULN_HIT_GFX = 169;
    private static final int FIRE_HIT_GFX = 131;

    private static final Projectile VULN_PROJECTILE = new Projectile(Graphics.asOSRS(168), 70, 31, 34, 16, 30, 0);
    private static final Projectile FIRE_PROJECTILE = new Projectile(Graphics.asOSRS(130), 70, 31, 34, 16, 30, 0);

    @Override
    public boolean init(NPC npc) {
        npc.setForceFollowClose(true);
        npc.setForceAgressive(true);
        return true;
    }

    @Override
    public Object[] getKeys() {
        return new Object[] { "Skeletal Mystic" };
    }

    @Override
    public int attack(NPC npc, Entity target) {
        if (target.withinDistance(npc.getMiddleTile(), 1) && Misc.rollDie(2, 1)) {
            melee(npc, target);
        } else {
            if (Misc.rollDie(3, 1))
                vuln(npc, target);
            else
                fire(npc, target);
        }
        return 5;
    }

    private void melee(NPC npc, Entity target) {
        npc.animate(Animation.createOSRS(5487));
        int max = 300;
        if (target.isPlayer() && target.getAsPlayer().getPrayer().isMeleeProtecting()) {
            max /= 2;
        }
        target.applyHit(new Hit(npc).look(Hit.HitLook.MELEE_DAMAGE).damage(getRandomMaxHit(npc, max, NPCCombatDefinitions.MELEE, target)).ignorePrayer());
    }

    private void vuln(NPC npc, Entity target) {
        npc.animate(Animation.createOSRS(5523));
        npc.setNextGraphics(Graphics.createOSRS(VULN_GFX));
        int delay = VULN_PROJECTILE.send(npc, target);
        int max = 250;
        if (target.isPlayer() && target.getAsPlayer().getPrayer().isMageProtecting())
            max /= 2;
        int dmg = getRandomMaxHit(npc, max, NPCCombatDefinitions.MAGE, target);
        target.applyHit(new Hit(npc).look(Hit.HitLook.MAGIC_DAMAGE).damage(dmg).ignorePrayer());
        if (dmg > 10) {
            if (target.isPlayer())
                target.getAsPlayer().getSkills().drain(Skills.DEFENCE, 0.1);
        }
        target.setNextGraphics(Graphics.createOSRS(VULN_HIT_GFX).setDelay(delay));
    }

    private void fire(NPC npc, Entity target) {
        npc.animate(Animation.createOSRS(5523));
        npc.setNextGraphics(Graphics.createOSRS(FIRE_GFX));
        int delay = FIRE_PROJECTILE.send(npc, target);
        int max = 350;
        if (target.isPlayer() && target.getAsPlayer().getPrayer().isMageProtecting())
            max /= 2;
        target.applyHit(new Hit(npc).look(Hit.HitLook.MAGIC_DAMAGE).damage(getRandomMaxHit(npc, max, NPCCombatDefinitions.MAGE, target)).ignorePrayer());
        target.setNextGraphics(Graphics.createOSRS(FIRE_HIT_GFX).setDelay(delay));
    }

}
