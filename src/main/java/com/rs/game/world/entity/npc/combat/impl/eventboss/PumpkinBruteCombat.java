package com.rs.game.world.entity.npc.combat.impl.eventboss;

import com.google.common.collect.Lists;
import com.rs.game.map.Bounds;
import com.rs.game.map.Position;
import com.rs.game.world.Projectile;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.player.CombatDefinitions;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.utility.Misc;
import com.rs.utility.Utils;

import java.util.List;

public class PumpkinBruteCombat extends CombatScript {


    private static final Projectile PUMPKIN = new Projectile( 4030, 74, 36, 40, 16, 30, 1);
    private static final Projectile PUMPKIN1 = new Projectile( 4030, 75, 36, 40, 16, 30, 1);
    private static final Projectile PUMPKIN2 = new Projectile( 4030, 76, 36, 40, 16, 30, 1);
    private static final Projectile PUMPKIN3 = new Projectile( 4030, 77, 36, 40, 16, 30, 1);
    private static final Projectile MINION_PROJ = new Projectile(4030, 75, 35, 20, 16, 30, 0);
    private static final Bounds ARENA = new Bounds(3544, 3245, 3559, 3229, 0);

    public static boolean isSpawned;
    public static boolean getSpawned() {
        return isSpawned;
    }

    private final List<NPC> minions = Lists.newArrayList();
    private static final Integer[] MINIONS = {  16125,
            16123,
            16094,
            16032 };
    @Override
    public Object[] getKeys() {
        return new Object[]{"Pumpkin brute"};
    }

    public boolean init(NPC npc) {
        npc.spawnListener = (entity) -> isSpawned = true;
        npc.deathEndListener = ((entity, killer) -> {
            isSpawned = false;
            minions.forEach(NPC::finish);

        });

        npc.setCanBeFrozen(false);
        npc.setCapDamage(750);
        npc.setAttackDistance(4);
        npc.setForceTargetDistance(4);
        return true;
    }

    @Override
    public int attack(NPC npc, Entity target) {
        List<Entity> targets = npc.getPossibleTargets(false, true);
        minions.removeIf(NPC::isFinished);
        int max_hit = npc.getCombatDefinitions().getMaxHit();
        int random = Utils.random(4);
        for (Entity entity : targets) {
            if (entity.isNPC())
                continue;
            if (random >= 1 && random <= 2) {
                magic_attack(npc, entity, max_hit);

            }
            if (random >= 3 && random <= 4) {
                Barrage_attack(npc, entity, max_hit);

            }

            if (random == 0 && minions.size() < 4) {
                minionspawn(npc, target);

            }

        }
        return npc.getCombatDefinitions().getAttackDelay();
    }


    private void magic_attack(NPC npc, Entity target, int max_hit) {
        npc.animate(new Animation(57021));
        delayHit(npc, target, PUMPKIN, new Hit(npc, getRandomMaxHit(npc, max_hit, CombatDefinitions.MAGIC_ATTACK, target), Hit.HitLook.MAGIC_DAMAGE));
    }

    private void Barrage_attack(NPC npc, Entity target, int max_hit) {
        npc.animate(new Animation(57021));
        delayHit(npc, target, PUMPKIN1, new Hit(npc, getRandomMaxHit(npc, max_hit, CombatDefinitions.RANGE_ATTACK, target), Hit.HitLook.RANGE_DAMAGE));
        delayHit(npc, target, PUMPKIN2, new Hit(npc, getRandomMaxHit(npc, max_hit, CombatDefinitions.RANGE_ATTACK, target), Hit.HitLook.RANGE_DAMAGE));
    }

    public void minionspawn(NPC npc, Entity target) {
        npc.animate(new Animation(58895));
        npc.addEvent(event -> {
            final Position pos = ARENA.randomPosition();
            int delay = MINION_PROJ.send(npc, pos);
            event.delay(delay);
            NPC minion = World.spawnNPC(Misc.get(MINIONS), pos, npc.getMapAreaNameHash(), 0, npc.canBeAttackFromOutOfArea(), npc.isSpawned());
            minion.setForceAgressive(true);
            minion.setAtMultiArea(true);
            minion.setTarget(target);
            minions.add(minion);
        });
    }
//    static {
//        World.startEvent(event -> {
//            NPC brute = null;
//            while (true) {
//                event.delay(12000); //every 2 hours
//                if (brute == null || brute.isDead() || brute.isFinished()) {
//                    World.sendWorldMessage("<col=ff0000><img=19>[EventManager]: A pumpkin brute has spawned!", false);
//                    brute = new NPC(16138, Position.of(3553, 3233, 0), -1, true, true);
//                    PumpkinBruteCombat.isSpawned = true;
//                } else { //if its still alive when the timer rolls around
//                    World.sendWorldMessage("<col=ff0000><img=19>[EventManager]: Reminder, pumpkin brute still alive.", false);
//                }
//            }
//        });
//    }
}
