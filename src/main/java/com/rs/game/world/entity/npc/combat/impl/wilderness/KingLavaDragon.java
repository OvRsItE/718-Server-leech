package com.rs.game.world.entity.npc.combat.impl.wilderness;

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
import com.rs.game.world.entity.player.content.Combat;
import com.rs.game.world.entity.player.content.toxin.ToxinType;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.NewForceMovement;
import com.rs.utility.Utils;

import java.util.List;

public class KingLavaDragon extends CombatScript {

    //flying transform -
    //flying land - 27872
    //wind gfx - 2699
    //wind hit gfx - 2700
    //fire gfx - 393
    //fire hit gfx - 157
    //venom gfx - 1470 - osrs
    //venom splash - 1473 = osrs
    //flying attack - 27871
    //ground attack - 20025
    //ground fire breath - 20081
    //ground headbutt - 20091
    public static boolean getSpawned() {
        return isSpawned;
    }

    public static boolean isSpawned;
    private static final Graphics PROJECTILE_WIND_HIT = new Graphics(2700);
    private static final Graphics PROJECTILE_FIRE_HIT = new Graphics(Graphics.createOSRS(157).getId());
    private static final Graphics PROJECTILE_VENOM_HIT = new Graphics(Graphics.createOSRS(1472).getId());
    private static final Projectile PROJECTILE_WIND = new Projectile(2699, 90, 36, 40, 16, 30, 1);
    private static final Projectile PROJECTILE_FIRE = new Projectile(393, 90, 36, 40, 16, 30, 1);
    private static final Projectile PROJECTILE_FIRE_GROUND = new Projectile(393, 50, 36, 40, 16, 30, 1);
    private static final Projectile PROJECTILE_VENOM = new Projectile(Graphics.createOSRS(1470).getId(), 90, 36, 40, 16, 30, 1);

    @Override
    public Object[] getKeys() {
        return new Object[] { 16112, 16113 };
    }
    @Override
    public boolean init(NPC npc) {
        npc.spawnListener = (entity) -> isSpawned = true;
        npc.hitListener = new HitListener().postDamage(hit -> {
            int halfhealth = npc.getMaxHitpoints() / 2;
            if (npc.getHitpoints() <= halfhealth && npc.getId() == 16112) {
                npc.animate(Animation.createOSRS(7872));
                npc.transformIntoNPC(16113);
            }

        });
        npc.deathEndListener = ((entity, killer) -> {
            isSpawned = false;

        });
        npc.setCanBeFrozen(false);
        npc.setCapDamage(750);
        npc.setForceTargetDistance(12);
        return true;

    }
    @Override
    public int attack(NPC npc, Entity target) {
        List<Entity> targets = npc.getPossibleTargets(false, true);
        int max_hit = Utils.getRandom(getMaxDamage(target.getAsPlayer(), 100));
        for (Entity entity : targets) {
            int random = Utils.random(11);
            if (entity.isNPC())
                continue;
            if (npc.getId() == 16113) {
                if (random == 0) {
                    npc.animate(Animation.createOSRS(91));
                    knock_back(npc, entity);
                }
                else if (random >= 1 && random <= 5) {
                    melee_attack(npc, entity);
                    return npc.getCombatDefinitions().getAttackDelay();
                }
                else if (random >= 6 && random <= 10) {
                    magic_attack_ground(npc, entity, max_hit);
                    return npc.getCombatDefinitions().getAttackDelay();
                }
            } else {
                if (random == 0) {
                    npc.animate(Animation.createOSRS(7871));
                    knock_back_air(npc, entity);
                    return npc.getCombatDefinitions().getAttackDelay();
                } else if (random >= 1 && random <= 5) {
                    magic_attack(npc, entity, max_hit);
                    return npc.getCombatDefinitions().getAttackDelay();
                } else if (random >= 6 && random <= 10) {
                    venom_attack(npc, entity);
                    return npc.getCombatDefinitions().getAttackDelay();
                }
            }

        }
        return npc.getCombatDefinitions().getAttackDelay();
    }

    private void melee_attack(NPC npc, Entity target) {
        List<Entity> targets = npc.getPossibleTargets(false, true);
        for (Entity entity : targets) {
            npc.animate(Animation.createOSRS(25));
            delayHit(npc, 2, entity, new Hit(npc, getRandomMaxHit(npc, 300,
                    NPCCombatDefinitions.MELEE, entity), Hit.HitLook.MELEE_DAMAGE));
        }
    }

    private void magic_attack_ground(NPC npc, Entity target, int max_hit) {
        List<Entity> targets = npc.getPossibleTargets(false, true);
        for (Entity entity : targets) {
            npc.animate(Animation.createOSRS(81));
            delayHit(npc, entity, PROJECTILE_FIRE_GROUND, new Hit(npc, max_hit, Hit.HitLook.MAGIC_DAMAGE));
            syncProjectileHit(npc, entity, Graphics.createOSRS(157, 0, 1), PROJECTILE_FIRE_GROUND);
        }
    }


    private void magic_attack(NPC npc, Entity target, int max_hit) {
        List<Entity> targets = npc.getPossibleTargets(false, true);
        for (Entity entity : targets) {
            npc.animate(Animation.createOSRS(7871));
            delayHit(npc, entity, PROJECTILE_FIRE, new Hit(npc, max_hit, Hit.HitLook.MAGIC_DAMAGE));
            syncProjectileHit(npc, entity, Graphics.createOSRS(157, 0, 1), PROJECTILE_FIRE);
        }
    }

    private void venom_attack(NPC npc, Entity target) {
        if (!target.getToxin().isImmune(ToxinType.VENOM)) {
            List<Entity> targets = npc.getPossibleTargets(false, true);
            for (Entity entity : targets) {
                if (!target.getToxin().isImmune(ToxinType.VENOM)) {
                    npc.animate(Animation.createOSRS(7871));
                    entity.sm("<col=00ff00>You've been inflicted with venom.");
                    entity.getToxin().applyToxin(ToxinType.VENOM);
                    delayHit(npc, entity, PROJECTILE_VENOM, new Hit(npc, getRandomMaxHit(npc, 300,
                            NPCCombatDefinitions.MAGE, entity), Hit.HitLook.MAGIC_DAMAGE));
                    syncProjectileHit(npc, entity, Graphics.createOSRS(1472, 0, 1), PROJECTILE_VENOM);
                } else {
                    npc.animate(Animation.createOSRS(7871));
                    delayHit(npc, entity, PROJECTILE_VENOM, new Hit(npc, getRandomMaxHit(npc, 300,
                            NPCCombatDefinitions.MAGE, entity), Hit.HitLook.MAGIC_DAMAGE));
                    syncProjectileHit(npc, entity, Graphics.createOSRS(1472, 0, 1), PROJECTILE_VENOM);
                }
            }
        }
    }


    private void knock_back(NPC npc, Entity target) {
        final Position tile = getKnockbackTile(target, npc);
        if (tile == null || tile.matches(target))
            return;
        List<Entity> targets = npc.getPossibleTargets(false, true);
        for (Entity entity : targets) {
            entity.getAsPlayer().lock(2);
            entity.animate(Animation.createOSRS(734));
            entity.setNextForceMovement(new NewForceMovement(entity, 0, tile, 1, Utils.getAngle(tile.getX() - entity.getX(), tile.getY() - entity.getY())));
            WorldTasksManager.schedule(new WorldTask() {

                @Override
                public void run() {
                    entity.setNextPosition(tile);
                    int stun_chance = Utils.random(0, 100);
                    if (stun_chance < 15) {
                        entity.addFreezeDelay(2000, false);
                        entity.setNextGraphics(new Graphics(80, 5, 60));
                    }
                    entity.getAsPlayer().getActionManager().forceStop();
                }

            }, 1);
            delayHit(npc, 2, entity, new Hit(npc, getRandomMaxHit(npc, 300,
                    NPCCombatDefinitions.MELEE, entity), Hit.HitLook.REGULAR_DAMAGE));
        }
    }

    private void knock_back_air(NPC npc, Entity target) {
        List<Entity> targets = npc.getPossibleTargets(false, true);
        final Position tile = getKnockbackTile(target, npc);
        if (tile == null || tile.matches(target))
            return;
        for (Entity entity : targets) {
            entity.getAsPlayer().lock(2);
            entity.animate(Animation.createOSRS(734));
            entity.setNextForceMovement(new NewForceMovement(entity, 0, tile, 1, Utils.getAngle(tile.getX() - entity.getX(), tile.getY() - entity.getY())));
            WorldTasksManager.schedule(new WorldTask() {

                @Override
                public void run() {
                    entity.setNextPosition(tile);
                    int stun_chance = Utils.random(0, 100);
                    if (stun_chance < 15) {
                        entity.addFreezeDelay(2000, false);
                        entity.setNextGraphics(new Graphics(80, 5, 60));
                    }
                    entity.getAsPlayer().getActionManager().forceStop();
                }

            }, 1);
            delayHit(npc, entity, PROJECTILE_WIND, new Hit(npc, getRandomMaxHit(npc, 300,
                    NPCCombatDefinitions.MAGE, entity), Hit.HitLook.MAGIC_DAMAGE));
            syncProjectileHit(npc, entity, new Graphics(2700, 0, 1), PROJECTILE_WIND);
        }
    }

    public Position getKnockbackTile(Entity target, NPC npc) {
        byte[] dirs = Utils.getDirection(npc.getDirection());
        int distance = Utils.random(3, 5);
        Position destination = new Position(target.getX() + (dirs[0] * distance), target.getY() + (dirs[1] * distance), target.getZ());
        if (Utils.colides(target.getX(), target.getY(), target.getSize(), destination.getX(), destination.getY(), target.getSize()) || !World.isTileFree(destination.getZ(), destination.getX(), destination.getY(), target.getSize()))
            return null;
        return destination;
    }
    private int getMaxDamage(Player target, int minDamage) {
        return (int) Math.max(650 * (1 - Combat.getDragonfireResistance(target)), minDamage);
    }

    static {
        World.startEvent(event -> {
            NPC dragon = null;
            while (true) {
                event.delay(6000); //every 1 hour
                if (dragon == null || dragon.isDead() || dragon.isFinished()) {
                    World.sendWorldMessage("<col=ff0000><img=19>[EventManager]: The King lava dragon as appeared north of the red dragon isle!", false);
                    dragon = new NPC(16112, Position.of(3202, 3883, 0), -1, true, true);
                    KingLavaDragon.isSpawned = true;
                } else { //if its still alive when the timer rolls around
                    World.sendWorldMessage("<col=ff0000><img=19>[EventManager]: Reminder, the king lava dragon is still alive.", false);
                }
            }
        });
    }

}
