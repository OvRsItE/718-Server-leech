package com.rs.game.world.entity.npc.combat.impl;

import com.rs.game.map.Bounds;
import com.rs.game.map.Direction;
import com.rs.game.map.Position;
import com.rs.game.map.ProjectileRoute;
import com.rs.game.world.Projectile;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.combat.AttackStyle;
import com.rs.game.world.entity.listeners.HitListener;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.impl.hydra.AlchemicalHydra;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.Slayer;
import com.rs.game.world.entity.player.controller.impl.KaruulmController;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.utility.Misc;
import com.rs.utility.Utils;

import java.util.LinkedList;
import java.util.List;

/**
 * @author ReverendDread
 * Created 2/17/2021 at 5:24 AM
 * @project 718---Server
 */
public class HydraCombat extends CombatScript {

    private static final Graphics MAGIC = Graphics.createOSRS(1662);
    private static final Graphics RANGED = Graphics.createOSRS(1663);
    private static final Graphics POISON = Graphics.createOSRS(1644);
    private static final Projectile MAGIC_PROJECTILE = new Projectile(MAGIC.getId(), 35, 21, 25, 30, 30, 0);
    private static final Projectile RANGED_PROJECTILE = new Projectile(RANGED.getId(), 40, 31, 25, 30, 30, 0);
    private static final Projectile POISON_PROJECTILE = new Projectile(POISON.getId(), 35, 0, 25, 16, 20, 1);

    private AttackStyle currentStyle = Utils.random(50) == 1 ? AttackStyle.RANGED : AttackStyle.MAGIC;
    private int attackCounter = 0;
    private NPC npc;

    @Override
    public Object[] getKeys() {
        return new String[] { "Hydra" };
    }

    @Override
    public boolean init(NPC npc) {
        this.npc = npc;
        this.npc.hitListener = new HitListener().preDamage(hit -> {
            if (hit.getSource() != null && hit.getSource().isPlayer()) {
                Player plr = hit.getSource().getAsPlayer();
                if (!plr.getSlayerManager().hasTask(Slayer.SlayerTask.HYDRA) && npc.inBounds(KaruulmController.SLAYER_ONLY_BOUNDS[1])) {
                    hit.negate();
                }
            }
        });
        return true;
    }

    @Override
    public int attack(NPC npc, Entity target) {
        if (attackCounter > 0 && attackCounter % 9 == 0) {
            poisonAttack(target);
        } else {
            if (currentStyle == AttackStyle.MAGIC)
                magicAttack(target);
            else
                rangedAttack(target);
        }
        attackCounter++;
        if (attackCounter % 3 == 0)
            switchStyle();
        return 6;
    }

    private void poisonAttack(Entity target) {
        npc.animate(Animation.createOSRS(8263));
        List<Position> targets = new LinkedList<>();
        targets.add(target.getPosition().copy());
        Bounds hydraBounds = npc.getBounds();
        List<Position> positions = target.getPosition().area(3, pos -> !pos.isClipped() && !pos.inBounds(hydraBounds) && npc.clipedProjectile(pos, false));
        for (int i = 0; i < 2; i++)
            targets.add(Misc.get(positions));
        targets.forEach(pos -> npc.addEvent(event -> {
            int delay = POISON_PROJECTILE.send(npc, pos);
            Direction dir = Direction.getDirection(Misc.getClosestPosition(npc, pos), pos);
            World.sendGraphics(npc, Graphics.createOSRS(1645).setDelay(delay), pos.copy());
            World.sendGraphics(npc, Graphics.createOSRS(AlchemicalHydra.POISON_POOLS[dir.ordinal()]).setDelay(delay), pos.copy());
            event.delay(3);
            for (int i = 0; i < 15; i++) {
                if (target.getPosition().isWithinDistance(pos, i == 0 ? 1 : 0)) {
                    target.applyHit(new Hit(npc).look(Hit.HitLook.POISON_DAMAGE).max(10, 40));
                }
                event.delay(2);
            }
        }));
    }

    private void magicAttack(Entity target) {
        projectileAttack(npc, target, MAGIC_PROJECTILE, new Hit(npc).look(Hit.HitLook.MAGIC_DAMAGE).max(0, 220), Animation.createOSRS(8261).getId());
    }

    private void rangedAttack(Entity target) {
        projectileAttack(npc, target, RANGED_PROJECTILE, new Hit(npc).look(Hit.HitLook.RANGE_DAMAGE).max(0, 220), Animation.createOSRS(8262).getId());
    }

    private void switchStyle() {
        currentStyle = currentStyle == AttackStyle.MAGIC ? AttackStyle.RANGED : AttackStyle.MAGIC;
    }

}
