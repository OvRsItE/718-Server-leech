package com.rs.game.world.entity.npc.combat.impl.TheHive;

import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;

public class YellowBeeCombat extends CombatScript {

    private static final Graphics PROJECTILE = new Graphics(1211);
    @Override
    public Object[] getKeys() {
        return new Object[] { 16169 };
    }

    @Override
    public int attack(NPC npc, Entity target) {
        JumpAttack(npc, target);
        return npc.getCombatDefinitions().getAttackDelay();
    }
    public void JumpAttack (NPC npc, Entity target) {
        target.addEvent(event -> {
            //npc.setForceWalk(new Position(target.getLastPosition()));
            npc.setForceFollowClose(true);
            event.delay(4);
            npc.animate(6234);
            World.sendGraphics(npc, PROJECTILE, new Position(npc.getPosition().getX() + 1, npc.getPosition().getY(), 0));
            World.sendGraphics(npc, PROJECTILE, new Position(npc.getPosition().getX() - 1, npc.getPosition().getY(), 0));
            World.sendGraphics(npc, PROJECTILE, new Position(npc.getPosition().getX(), npc.getPosition().getY() + 1, 0));
            World.sendGraphics(npc, PROJECTILE, new Position(npc.getPosition().getX(), npc.getPosition().getY() - 1, 0));
            World.sendGraphics(npc, PROJECTILE, new Position(npc.getPosition().getX() + 2, npc.getPosition().getY(), 0));
            World.sendGraphics(npc, PROJECTILE, new Position(npc.getPosition().getX() - 2, npc.getPosition().getY(), 0));
            World.sendGraphics(npc, PROJECTILE, new Position(npc.getPosition().getX(), npc.getPosition().getY() + 2, 0));
            World.sendGraphics(npc, PROJECTILE, new Position(npc.getPosition().getX(), npc.getPosition().getY() - 2, 0));
            event.delay(1);
            if (target.getPosition().withinDistance(npc.getPosition(), 2)) {
                target.applyHit(new Hit(npc).look(Hit.HitLook.MAGIC_DAMAGE).max(100, 200));
            }

        });

    }
}
