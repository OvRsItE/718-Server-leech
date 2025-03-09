package com.rs.game.world.entity.npc.combat.impl;

import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.toxin.ToxinType;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.utility.Utils;

public class AncientCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[] { 13456 };
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        int attackStyle = Utils.getRandom(5);
        int size = npc.getSize();

        if (attackStyle == 0) {
            int distanceX = target.getX() - npc.getX();
            int distanceY = target.getY() - npc.getY();
            if (distanceX > size || distanceX < -1 || distanceY > size
                    || distanceY < -1)
                attackStyle = Utils.getRandom(4) + 1;
            else {
                delayHit(
                        npc,
                        0,
                        target,
                        getMeleeHit(
                                npc,
                                getRandomMaxHit(npc, defs.getMaxHit(),
                                        NPCCombatDefinitions.MELEE, target)));
                npc.animate(new Animation(defs.getAttackEmote()));
                return defs.getAttackDelay();
            }
        } else if (attackStyle == 1 || attackStyle == 2) {
            int damage = Utils.getRandom(650);
            final Player player = target instanceof Player ? (Player) target
                    : null;
            if  (player != null && (player.getPrayer()
                    .usingPrayer(0, 17) || player.getPrayer()
                    .usingPrayer(1, 7)))
                damage = 0;
            if (player != null
                    && player.getFireImmune() > Utils.currentTimeMillis()) {
                if (damage != 0)
                    damage = Utils.getRandom(164);
            } else if (damage == 0)
                damage = Utils.getRandom(164);
            else if (player != null)
                player.getPackets().sendGameMessage(
                        "You are hit by the ancient zarosians special attack!", true);
            delayHit(npc, 2, target, getRegularHit(npc, damage));
            World.sendProjectile(npc, target, 3346, 34, 16, 30, 35, 16, 0);
            npc.animate(new Animation(11989));

        } else if (attackStyle == 3) {
            int damage;
            final Player player = target instanceof Player ? (Player) target
                    : null;
            if (player != null
                    && (player.getPrayer().usingPrayer(0, 17) || player
                    .getPrayer().usingPrayer(1, 7))) {
                damage = getRandomMaxHit(npc, 164, NPCCombatDefinitions.MAGE,
                        target);
                if (player != null)
                    player.getPackets()
                            .sendGameMessage(
                                    "Your prayer absorbs most of the Zarosian's breath.",
                                    true);
            } else {
                damage = Utils.getRandom(480);
                if (player != null)
                    player.getPackets().sendGameMessage(
                            "You got powerful smash hit from the Zarosian's Special Attack.",
                            true);
            }
            if (Utils.getRandom(2) == 0)
            	target.getToxin().applyToxin(ToxinType.POISON);
            delayHit(npc, 2, target, getRegularHit(npc, damage));
            World.sendProjectile(npc, target, 3346, 34, 16, 30, 35, 16, 0);
            npc.animate(new Animation(11991));
        } else if (attackStyle == 4) {
            int damage;
            final Player player = target instanceof Player ? (Player) target
                    : null;
             if (player != null
                    && (player.getPrayer().usingPrayer(0, 17) || player
                    .getPrayer().usingPrayer(1, 7))) {
                damage = getRandomMaxHit(npc, 164, NPCCombatDefinitions.MAGE,
                        target);
                if (player != null)
                    player.getPackets()
                            .sendGameMessage(
                                    "You are hit by the Zarosian's powerful smash!",
                                    true);
            } else {
                damage = Utils.getRandom(350);
                if (player != null)
                    player.getPackets().sendGameMessage(
                            "You are hit by the Zarosian's powerful smash!",
                            true);
            }
            if (Utils.getRandom(2) == 0)
                target.addFreezeDelay(15000);
            delayHit(npc, 2, target, getRegularHit(npc, damage));
            World.sendProjectile(npc, target, 3346, 34, 16, 30, 35, 16, 0);
            npc.animate(new Animation(12019));
        } else {
            int damage;
            final Player player = target instanceof Player ? (Player) target
                    : null;
            if (player != null
                    && (player.getPrayer().usingPrayer(0, 17) || player
                    .getPrayer().usingPrayer(1, 7))) {
                damage = getRandomMaxHit(npc, 164, NPCCombatDefinitions.MAGE,
                        target);
                if (player != null)
                    player.getPackets()
                            .sendGameMessage(
                                    "Your prayer absorbs most of the Zarosian's curses!",
                                    true);
            } else {
                damage = Utils.getRandom(501);
                if (player != null)
                    player.getPackets().sendGameMessage(
                            "You are hit by the Zarosian's powerful smash!",
                            true);
            }
            delayHit(npc, 2, target, getRegularHit(npc, damage));
            World.sendProjectile(npc, target, 3346, 34, 16, 30, 35, 16, 0);
            npc.animate(new Animation(11981));
        }
        return defs.getAttackDelay();
    }
}
