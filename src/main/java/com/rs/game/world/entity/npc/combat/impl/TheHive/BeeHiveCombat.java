package com.rs.game.world.entity.npc.combat.impl.TheHive;

import com.google.common.collect.Lists;
import com.rs.game.map.Bounds;
import com.rs.game.map.Position;
import com.rs.game.map.WorldObject;
import com.rs.game.world.Projectile;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.instances.TheHive.MinionNpc;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.utility.Misc;
import com.rs.utility.Utils;

import java.util.List;

public class BeeHiveCombat extends CombatScript {
    private static final Projectile HONEY_PROJECTILE = new Projectile(4039, 30, 35, 20, 16, 30, 0);
    private static final Graphics HONEY_SPLAT = new Graphics(4038);
    private static final Graphics HONEYPLACED = new Graphics(4040);
    private final List<MinionNpc> minions = Lists.newArrayList();
    private final List<WorldObject> objects = Lists.newArrayList();
    private static final Integer[] MINIONS = {
            16170,
            16171,
            16172 };

    public boolean init(NPC npc) {
        npc.deathEndListener = ((entity, killer) -> {
            minions.forEach(NPC::finish);
            objects.clear();
        });

        return true;
    }

    @Override
    public Object[] getKeys() {
        return new Object[] { 16173 };
    }

    @Override
    public int attack(NPC npc, Entity target) {
        int random = Utils.random(4);
        npc.faceNone();
        HoneyAttack(npc, target);
        if (random == 0 && minions.size() < 3) {
            minionspawn(npc, target);
        }

        return npc.getCombatDefinitions().getAttackDelay();
    }


    public void HoneyAttack (NPC npc, Entity target) {
        target.addEvent(event -> {
            if (npc.isDead() || npc.isFinished() || target == null)
                return;
            final Position pos = new Position(npc.getSpawnPosition().getX(), npc.getSpawnPosition().getY(), 0);
            Position next = new Position(npc.getSpawnPosition().getX() - Misc.random(7), npc.getSpawnPosition().getY() + Misc.random(4), 0);
            Position start = pos;
            WorldObject object = new WorldObject(90001, 22, 1, next);
            int delay = HONEY_PROJECTILE.send(start, next);
            event.delay(delay);
            World.sendGraphics(npc, HONEY_SPLAT, next);
            World.spawnTemporaryObject(object, 20000);
            objects.add(object);
            for (WorldObject objects: objects) {
                if (target.getPosition().matches(objects.getPosition())) {
                        target.applyHit(new Hit(npc).look(Hit.HitLook.REGULAR_DAMAGE).max(100, 200));
                        target.addFreezeDelay(4000);
                }
            }
            event.delay(28);
            objects.remove(object);
        });
    }

    public void minionspawn(NPC npc, Entity target) {
        npc.addEvent(event -> {
            final Position pos = new Position(npc.getPosition().getX() + Misc.random(3), npc.getPosition().getY(), 0);
            MinionNpc minion = new MinionNpc(Misc.get(MINIONS), pos);
            minion.setForceAgressive(true);
            minion.setAtMultiArea(true);
            minion.setTarget(target);
            minions.add(minion);
        });
    }
}
