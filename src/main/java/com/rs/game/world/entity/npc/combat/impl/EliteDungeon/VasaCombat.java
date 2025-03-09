package com.rs.game.world.entity.npc.combat.impl.EliteDungeon;

import com.google.common.collect.Lists;
import com.rs.game.map.Position;
import com.rs.game.world.Projectile;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.listeners.HitListener;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.npc.instances.EliteDungeon.EliteDungeon;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.utility.Misc;
import com.rs.utility.Utils;

import java.util.List;


public class VasaCombat extends CombatScript {
    private static final Projectile MAGE_PROJECTILE = new Projectile( 6329, 90, 25, 75, 16, 30, 1);
    private static final Projectile TELEPORT_PROJECTILE = new Projectile( 6327, 35, 36, 40, 16, 35, 1);
    private final List<NPC> healers = Lists.newArrayList();
    private boolean ishealing = false;
    @Override
    public Object[] getKeys() {
        return new Object[]{"Vasa Nistirio"};
    }
    public boolean init(NPC npc) {
        npc.hitListener = new HitListener().postDamage(hit -> {
            int halfhealth = npc.getMaxHitpoints() / 2;
            if (npc.getHitpoints() <= halfhealth && healers.isEmpty() && !ishealing) {
                healers(npc, npc.getAsNPC());
            }
            return;
        });
        npc.deathEndListener = ((entity, killer) -> {
            healers.forEach(NPC::finish);

        });
        npc.setCanBeFrozen(false);
        npc.setCapDamage(750);
        npc.setForceTargetDistance(16);
        npc.setAttackDistance(16);
        npc.setCantWalk(true);
        npc.setCantFollowUnderCombat(true);
        return true;
    }



    @Override
    public int attack(NPC npc, Entity target) {
        List<Entity> targets = npc.getPossibleTargets();
        int random = Utils.random(10);
        int max_hit = npc.getCombatDefinitions().getMaxHit();
        for (Entity entity : targets) {
            if (random >= 1 && random <= 10) {
                npc.animate(27410);
                magic_attack(npc, entity, max_hit);

            }
            if (random == 0) {
                npc.animate(27409);
                teleport_attack(npc, entity);

            }
        }
        return npc.getCombatDefinitions().getAttackDelay();
    }


    private void magic_attack(NPC npc, Entity target, int max_hit) {
        delayHit(
                npc,
                3,
                target,
                getMagicHit(
                        npc,
                        getRandomMaxHit(npc, max_hit,
                                NPCCombatDefinitions.MAGE, target)));
        World.sendProjectile(npc, target, MAGE_PROJECTILE);
    }

    private void teleport_attack(NPC npc, Entity target) {
        World.sendProjectile(npc, target, TELEPORT_PROJECTILE);
        npc.faceNone();
        npc.lock();
        npc.setCantInteract(true);
        npc.addEvent(event -> {
            target.setNextPosition(new Position(npc.getX() + 2, npc.getY() + 2, 0));
            event.delay(4);
            npc.getCombat().setCombatDelay(4);
                if (target.withinDistance(npc.getMiddleTile(), 2)) {
                    target.applyHit(new Hit(npc).max(0, 720).look(Hit.HitLook.REGULAR_DAMAGE));
                }
            npc.setCantInteract(false);
            npc.face(target);
            npc.unlock();
        });




    }

    private void healers(NPC npc, Entity target) {
        if (healers.size() < 4) {
            npc.animate(27410);
            ishealing = true;
            npc.addEvent(event -> {
                //final Position pos = Position.of(1239, 6280, 0);
                final Position pos = new Position(npc.getX() - 1, npc.getY() - 1, 0);
                int delay = TELEPORT_PROJECTILE.send(npc, pos);
                event.delay(delay);
                NPC healer = World.spawnNPC(16159, pos, npc.getMapAreaNameHash(), 0, npc.canBeAttackFromOutOfArea(), npc.isSpawned());
                healer.setForceAgressive(true);
                healer.setAtMultiArea(true);
                healer.setTarget(npc);
                healer.setAttackDistance(12);
                healers.add(healer);
            });
            npc.addEvent(event -> {
                //final Position pos = Position.of(1239, 6288, 0);
                final Position pos = new Position(npc.getX() - 1, npc.getY() + 5, 0);
                int delay = TELEPORT_PROJECTILE.send(npc, pos);
                event.delay(delay);
                NPC healer = World.spawnNPC(16159, pos, npc.getMapAreaNameHash(), 0, npc.canBeAttackFromOutOfArea(), npc.isSpawned());
                healer.setForceAgressive(true);
                healer.setAtMultiArea(true);
                healer.setTarget(npc);
                healer.setAttackDistance(12);
                healers.add(healer);
            });
            npc.addEvent(event -> {
                //final Position pos = Position.of(1245, 6287, 0);
                final Position pos = new Position(npc.getX() + 5, npc.getY() + 5, 0);
                int delay = TELEPORT_PROJECTILE.send(npc, pos);
                event.delay(delay);
                NPC healer = World.spawnNPC(16159, pos, npc.getMapAreaNameHash(), 0, npc.canBeAttackFromOutOfArea(), npc.isSpawned());
                healer.setForceAgressive(true);
                healer.setAtMultiArea(true);
                healer.setTarget(npc);
                healer.setAttackDistance(12);
                healers.add(healer);
            });
            npc.addEvent(event -> {
               // final Position pos = Position.of(1245, 6281, 0);
                final Position pos = new Position(npc.getX() + 5, npc.getY() -1, 0);
                int delay = TELEPORT_PROJECTILE.send(npc, pos);
                event.delay(delay);
                NPC healer = World.spawnNPC(16159, pos, npc.getMapAreaNameHash(), 0, npc.canBeAttackFromOutOfArea(), npc.isSpawned());
                healer.setForceAgressive(true);
                healer.setAtMultiArea(true);
                healer.setTarget(npc);
                healer.setAttackDistance(12);
                healers.add(healer);
            });
        }
    }



}
