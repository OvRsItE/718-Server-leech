package com.rs.game.world.entity.npc.combat.impl.EliteDungeon;

import com.google.common.collect.Lists;
import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.Projectile;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.listeners.HitListener;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.utility.Misc;
import com.rs.utility.Utils;

import java.util.List;
import java.util.Random;


public class TektonCombat extends CombatScript {
    private static final Projectile MAGE_PROJECTILE = new Projectile( 6329, 90, 25, 75, 16, 30, 1);
    private static final Projectile FIRE_PROJECTILE = new Projectile( 5660, 35, 36, 40, 16, 40, 1);
    private final List<NPC> healers = Lists.newArrayList();
    private boolean ismoving = false;
    @Override
    public Object[] getKeys() {
        return new Object[]{"Tekton"};
    }
    public boolean init(NPC npc) {
        npc.hitListener = new HitListener().postDamage(hit -> {
            int halfhealth = npc.getMaxHitpoints() / 2;
            if ((npc.getHitpoints() <= halfhealth && !ismoving)) {
                Force_walk(npc, npc.getAsNPC());
            }
            return;
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
        List<Entity> targets = npc.getPossibleTargets(false, true);
        int random = Utils.random(10);
        int max_hit = npc.getCombatDefinitions().getMaxHit();
            if (random >= 0 && random <= 10) {
                npc.animate(27483);
                melee_attack(npc, target, max_hit);

            }
        return npc.getCombatDefinitions().getAttackDelay();
    }


    private void melee_attack(NPC npc, Entity target, int max_hit) {
        delayHit(
                npc,
                2,
                target,
                getMeleeHit(
                        npc,
                        getRandomMaxHit(npc, max_hit,
                                NPCCombatDefinitions.MELEE, target)));
        //World.sendProjectile(npc, target, MAGE_PROJECTILE);
    }

    public void Fire (NPC npc, Entity target) {
        for (Player p : npc.localPlayers()) {
            p.addEvent(event -> {
            if (npc.isDead() || npc.isFinished() || target == null)
                return;
                final Position pos = new Position(npc.getSpawnPosition().getX() + 1, npc.getSpawnPosition().getY() -10, 1);
            Position next = p.getPosition().copy();
            Position start = pos;
                int delay = FIRE_PROJECTILE.send(start, next);
                event.delay(delay);
                World.sendGraphics(npc, new Graphics(5659), next);
            if (p.getPosition().matches(next)) {
                p.applyHit(new Hit(npc).look(Hit.HitLook.REGULAR_DAMAGE).max(100, 500));
                event.delay(1);
            }
            });
        }
    }
    private void Force_walk(NPC npc, Entity target) {
        npc.faceNone();
        npc.lock();
        npc.setCantInteract(true);
        ismoving = true;
        npc.addEvent(event -> {
           //npc.setForceWalk(new Position(1259, 6284, 1));
            npc.setForceWalk(new Position(npc.getSpawnPosition().getX(), npc.getSpawnPosition().getY() -9 , 1));
           event.delay(10);
            for (int i = 0; i < 20; i++) {
                npc.animate(new Animation(27473));
                event.delay(3);
                Fire(npc, target);
            }
           // npc.setForceWalk(new Position(1259, 6293, 1));
            npc.setForceWalk(new Position(npc.getSpawnPosition().getX(), npc.getSpawnPosition().getY() , 1));
            npc.setCantInteract(false);
            npc.setForceTargetDistance(8);
            npc.setForceAgressive(true);
            npc.face(target);
            npc.setTarget(target);
            npc.unlock();
        });




    }



}
