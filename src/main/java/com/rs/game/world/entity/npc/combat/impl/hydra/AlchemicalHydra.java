package com.rs.game.world.entity.npc.combat.impl.hydra;

import com.rs.game.map.*;
import com.rs.game.world.Projectile;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.combat.AttackStyle;
import com.rs.game.world.entity.listeners.HitListener;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.updating.impl.*;
import com.rs.utility.Misc;

import java.util.LinkedList;
import java.util.List;

public class AlchemicalHydra extends CombatScript {

    private NPC npc;
    private WorldObject redVent, greenVent, blueVent;
    private Form currentForm = Form.GREEN;
    private AttackStyle currentStyle = Misc.rollPercent(50) ? AttackStyle.RANGED : AttackStyle.MAGIC;
    private FireArea fireLockdownArea;
    private boolean resistant = true;
    private int power = 0;
    private int attackCounter = -3; // start at -3 because the first special attack comes after 3 attacks
    private int lastSpecial = -1;
    private boolean firesActive = false;
    private static final Graphics MAGIC = Graphics.createOSRS(1662);
    private static final Graphics RANGED = Graphics.createOSRS(1663);
    private static final Graphics POISON = Graphics.createOSRS(1644);
    private static final Graphics SHOCK = Graphics.createOSRS(1665);
    private static final Graphics FIRE = Graphics.createOSRS(1667);
    private static final Projectile MAGIC_PROJECTILE_1 = new Projectile(MAGIC.getId(), 60, 21, 25, 30, 30, 0);
    private static final Projectile MAGIC_PROJECTILE_2 = new Projectile(MAGIC.getId(), 50, 21, 40, 30, 30,0);
    private static final Projectile RANGED_PROJECTILE_1 = new Projectile(RANGED.getId(), 120, 35, 25, 30, 30, 0);
    private static final Projectile RANGED_PROJECTILE_2 = new Projectile(RANGED.getId(), 120, 35, 40, 30, 30, 0);
    private static final Projectile POISON_PROJECTILE = new Projectile(POISON.getId(), 50, 0, 25, 30, 20, 0);
    private static final Projectile SHOCK_PROJECTILE_1 = new Projectile(SHOCK.getId(), 50, 60, 15, 60, 20, 0);
    private static final Projectile SHOCK_PROJECTILE_2 = new Projectile(SHOCK.getId(), 60, 0, 0, 50, 20, 0);
    private static final Projectile FIRE_PROJECTILE = new Projectile(FIRE.getId(), 50, 0, 25, 50, 20, 0);
    public static final int[] POISON_POOLS = { // indexed by direction as in Direction class
            1658,
            1659,
            1660,
            1657,
            1661,
            1656,
            1655,
            1654,
    };
    private static final int[][] SHOCK_SPAWN_POINTS = {
            {7, -1},
            {8, 7},
            {-2, 8},
            {-2, -1},
    };

    @Override
    public Object[] getKeys() {
        return new String[] { "Alchemical Hydra" };
    }

    @Override
    public boolean init(NPC npc) {
        this.npc = npc;
        npc.setFollowDistance(4);
        npc.setCanBeFrozen(false);
        npc.hitListener = new HitListener().postDamage(this::postDamage).preDamage(this::preDamage);
        redVent = new WorldObject(WorldObject.asOSRS(34568), 10, 0, new Position(npc.getSpawnPosition().getX() + 7, npc.getSpawnPosition().getY() + -2, npc.getSpawnPosition().getZ()));
        greenVent = new WorldObject(WorldObject.asOSRS(34569), 10, 0, new Position(npc.getSpawnPosition().getX() + 7, npc.getSpawnPosition().getY() + 7, npc.getSpawnPosition().getZ()));
        blueVent = new WorldObject(WorldObject.asOSRS(34570), 10, 0, new Position(npc.getSpawnPosition().getX() - 2, npc.getSpawnPosition().getY() + 7, npc.getSpawnPosition().getZ()));
        World.spawnObject(redVent);
        World.spawnObject(greenVent);
        World.spawnObject(blueVent);
        startVents();
        npc.deathEndListener = (entity, killer) -> {
            npc.setNextNPCTransformation(NPC.asOSRS(8615));
            currentForm = Form.GREEN;
            resistant = true;
            power = 0;
            attackCounter = -3;
            lastSpecial = -1;
        };
        npc.setCapDamage(750);
        return true;
    }

    private void preDamage(Hit hit) {
        if (resistant && currentForm != Form.GREY) {
            hit.damage((int) (hit.getDamage() * 0.25));
            if (hit.getSource() != null && hit.getSource().isPlayer())
                hit.getSource().getAsPlayer().sendMessage("The Alchemical Hydra's defences partially absorb your attack!");
        }
    }

    private void postDamage(Hit hit) {
        double hpRatio = (double) npc.getHitpoints() / npc.getMaxHitpoints();
        if (hpRatio == 0)
            return;
        if ((currentForm == Form.GREEN && hpRatio < 0.75)
                || (currentForm == Form.BLUE && hpRatio < 0.5)
                || (currentForm == Form.RED && hpRatio < 0.25)) {
            nextForm(hit.getSource());
        }
    }

    private void startVents() {
        for (WorldObject vent : new WorldObject[] {redVent, greenVent, blueVent}) {
            Bounds ventBounds = new Bounds(new Position(vent.getX(), vent.getY(), vent.getZ()), 2);
            npc.addEvent(event -> {
                event.delay(6);
                while (true) {
                    vent.animate(Animation.createOSRS(8279));
                    event.delay(1);
                    if (npc.getCombat().getTarget() != null) {
                        Entity target = npc.getCombat().getTarget();
                        if (target.getPosition().equals(vent.getX(), vent.getY())) {
                            target.applyHit(new Hit(npc).damage(200));
                            target.getAsPlayer().sendMessage("The chemical burns you as it cascades over you.");
                        }
                        Bounds npcBounds = new Bounds(npc.getPosition().getX() + 1, npc.getPosition().getY() + 1, npc.getPosition().getX() + npc.getSize() - 2, npc.getPosition().getY() + npc.getSize() - 2, npc.getPosition().getZ());
                        if (ventBounds.intersects(npcBounds)) {
                            if (vent.getId() == WorldObject.asOSRS(currentForm.weaknessVent)) {
                                if (resistant) {
                                    resistant = false;
                                    npc.setNextForceTalk(new ForceTalk("Roaaaaaaaaaaaaaaaaaar!"));
                                    target.getAsPlayer().sendMessage("The chemicals neutralise the Alchemical Hydra's defences!");
                                }
                            } else {
                                power++;
                                target.getAsPlayer().sendMessage("The chemicals are absorbed by the Alchemical Hydra; empowering it further!");
                            }
                        }
                    }
                    event.delay(4);
                    vent.animate(Animation.createOSRS(8280));
                    event.delay(5);
                }
            });
        }
    }

    private void nextForm(Entity target) {
        if (currentForm == Form.GREY)
            return;
        Form nextForm = Form.values()[currentForm.ordinal() + 1];
        npc.addEvent(event -> {
            npc.lock();
            npc.transformIntoNPC(NPC.asOSRS(currentForm.loseHeadNPCId));
            npc.animate(Animation.createOSRS(currentForm.loseHeadAnim));
            int delay = currentForm.loseHeadDuration;
            currentForm = nextForm;
            event.delay(delay);
            if (currentForm != Form.GREY) {
                resistant = true;
                power = 0;
            } else {
                switchStyle();
                power = 8;
                attackCounter = -3;
                lastSpecial = -1;
            }
            npc.transformIntoNPC(NPC.asOSRS(nextForm.npcId));
            npc.animate(Animation.createOSRS(nextForm.fadeInAnim));
            npc.face(target);
            event.delay(2);
            npc.unlock();
        });
    }

    @Override
    public int attack(NPC npc, Entity target) {
//        if (!npc.withinDistance(target, firesActive ? 16 : 6))
//            return 0;
        if (attackCounter % 9 == 0 && attackCounter != lastSpecial) {
            specialAttack(target);
            lastSpecial = attackCounter;
        } else {
            if (currentStyle == AttackStyle.MAGIC)
                magicAttack(target);
            else
                rangedAttack(target);
            attackCounter++;
            if (currentForm == Form.GREY || attackCounter % 3 == 0) {
                switchStyle();
            }
        }
        return 6;
    }

    private void rangedAttack(Entity target) {
        int maxDamage = 340;
        maxDamage *= 1 + Math.min(0.5, power * 0.0625);
        npc.animate(Animation.createOSRS(currentForm.rightHeadsAttackAnim));
        if (currentForm == Form.GREEN || currentForm == Form.BLUE) { // 2 attacks
            maxDamage /= 2;
            Hit first = new Hit(npc).damage(getRandomMaxHit(npc, maxDamage, NPCCombatDefinitions.RANGE, target)).look(Hit.HitLook.RANGE_DAMAGE);
            projectileAttack(npc, target, RANGED_PROJECTILE_1, first, -1);
            Hit second = new Hit(npc).damage(getRandomMaxHit(npc, maxDamage, NPCCombatDefinitions.RANGE, target)).look(Hit.HitLook.RANGE_DAMAGE);
            projectileAttack(npc, target, RANGED_PROJECTILE_2, second, -1);
        } else { // only 1, but stronger, attack
            Hit hit = new Hit(npc).damage(getRandomMaxHit(npc, maxDamage, NPCCombatDefinitions.RANGE, target)).look(Hit.HitLook.RANGE_DAMAGE);
            projectileAttack(npc, target, RANGED_PROJECTILE_1, hit, -1);
        }
    }

    private void magicAttack(Entity target) {
        int maxDamage = 340;
        maxDamage *= 1 + Math.min(0.5, power * 0.0625);
        npc.animate(Animation.createOSRS(currentForm.leftHeadsAttackAnim));
        if (currentForm == Form.GREEN) { // 2 attacks
            maxDamage /= 2;
            Hit first = new Hit(npc).damage(getRandomMaxHit(npc, maxDamage, NPCCombatDefinitions.MAGE, target)).look(Hit.HitLook.MAGIC_DAMAGE);
            projectileAttack(npc, target, MAGIC_PROJECTILE_1, first, -1);
            Hit second = new Hit(npc).damage(getRandomMaxHit(npc, maxDamage, NPCCombatDefinitions.MAGE, target)).look(Hit.HitLook.MAGIC_DAMAGE);
            projectileAttack(npc, target, MAGIC_PROJECTILE_2, second, -1);
        } else { // only 1, but stronger, attack
            Hit hit = new Hit(npc).damage(getRandomMaxHit(npc, maxDamage, NPCCombatDefinitions.MAGE, target)).look(Hit.HitLook.MAGIC_DAMAGE);
            projectileAttack(npc, target, MAGIC_PROJECTILE_1, hit, -1);
        }
    }

    private void switchStyle() {
        currentStyle = currentStyle == AttackStyle.MAGIC ? AttackStyle.RANGED : AttackStyle.MAGIC;
    }

    private void specialAttack(Entity target) {
        switch (currentForm) {
            case GREEN:
            case GREY:
                poisonAttack(target);
                break;
            case BLUE:
                shockAttack(target);
                break;
            case RED:
                fireAttack(target);
                break;
        }
    }

    private void poisonAttack(Entity target) {
        if (currentForm == Form.GREEN)
            npc.animate(Animation.createOSRS(currentForm.middleHeadAttackAnim));
        else
            npc.animate(Animation.createOSRS(currentForm.leftHeadsAttackAnim));
        List<Position> poison = new LinkedList<>();
        poison.add(target.getPosition().copy());
        if (currentForm != Form.GREY || Misc.random(2) == 1) {
            Bounds hydraBounds = npc.getBounds();
            List<Position> positions = target.getPosition().area(3, pos -> !pos.isClipped() && !pos.inBounds(hydraBounds) && npc.clipedProjectile(pos, false));
            for (int i = 0; i < 4; i++)
                poison.add(Misc.get(positions));
        }
        poison.forEach(pos -> {
            npc.addEvent(event -> {
                int delay = POISON_PROJECTILE.send(npc, pos);
                Direction dir = Direction.getDirection(Misc.getClosestPosition(npc, pos), pos);
                World.sendGraphics(npc, Graphics.createOSRS(1645).setDelay(delay), pos.copy());
                World.sendGraphics(npc, Graphics.createOSRS(POISON_POOLS[dir.ordinal()]).setDelay(delay), pos.copy());
                event.delay(3);
                for (int i = 0; i < 15; i++) {
                    if (target.getPosition().isWithinDistance(pos, i == 0 ? 1 : 0)) {
                        target.applyHit(new Hit(npc).look(Hit.HitLook.POISON_DAMAGE).max(10, 120));
                    }
                    event.delay(1);
                }
            });
        });
    }

    private void shockAttack(Entity target) {
        npc.animate(Animation.createOSRS(currentForm.middleHeadAttackAnim));
        Position src = npc.getSpawnPosition().relative(2, 2);
        int delay = SHOCK_PROJECTILE_1.send(npc, src);
        Graphics landing = Graphics.createOSRS(1664);
        landing.setDelay(delay);
        World.sendGraphics(npc, landing, src);
        for (int[] coords : SHOCK_SPAWN_POINTS) {
            npc.addEvent(event -> {
                Position shockPosition = npc.getSpawnPosition().relative(coords[0], coords[1]);
                event.delay(3);
                SHOCK_PROJECTILE_2.send(src, shockPosition);
                event.delay(2);
                for (int i = 0; i < 12; i++) {
                    if (target == null)
                        return;
                    int moveX = shockPosition.unitVectorX(target.getPosition());
                    int moveY = shockPosition.unitVectorY(target.getPosition());
                    if (moveX != 0 || moveY != 0) {
                        shockPosition.translate(moveX, moveY, 0);
                        World.sendGraphics(npc, Graphics.createOSRS(1666), shockPosition);
                        if (target.getPosition().equals(shockPosition.getX(), shockPosition.getY())) {
                            target.applyHit(new Hit(npc).damage(200).look(Hit.HitLook.REGULAR_DAMAGE));
                            target.addFreezeDelay(4000, false);
                            if (target.isPlayer()) {
                                target.getAsPlayer().sendMessage("<col=ffffff>The electricity temporarily paralyzes you!");
                            }
                            return;
                        }
                    }
                    event.delay(1);
                }
            });
        }

    }

    private void fireAttack(Entity target) {
        npc.lock();
        npc.faceNone();
        npc.addEvent(event -> {
            npc.setCantFollowUnderCombat(true);
            npc.removeTarget();
            npc.setFollowDistance(12);
            npc.setAttackDistance(16);
            while (!npc.isAt(npc.getSpawnPosition())) {
                npc.addWalkSteps(npc.getSpawnPosition().getX(), npc.getSpawnPosition().getY());
                event.delay(1);
            }
            if (target == null) {
                cancelFireAttack();
                return;
            }
            Bounds blockBounds = npc.getBounds();
            blockBounds.forEachPos(Position::clip);
            if (target.getPosition().inBounds(npc.getBounds())) {
                target.addEvent(e -> {
                    target.lock();
                    target.resetWalkSteps();
                    target.setNextForceMovement(new ForceMovement(target.getPosition().relative(-6, 0), 10, target.getPosition().relative(0, 0), 60, Direction.EAST));
                    target.animate(1157);
                    e.delay(1);
                    target.unlock();
                });
                fireLockdownArea = FireArea.WEST;
            } else {
                fireLockdownArea = FireArea.getTargetArea(target, npc);
            }
            if (fireLockdownArea == null) { //?
                cancelFireAttack();
                return;
            }
            target.setFreezeDelay(6000);
            if (target.isPlayer())
                target.getAsPlayer().sendMessage("The Alchemical Hydra temporarily stuns you!");

            event.delay(1);

            if (currentForm != Form.RED) {
                cancelFireAttack();
                return;
            }
            firesActive = true;
            for (FireArea adjacents : fireLockdownArea.getAdjacents()) {
                npc.setNextFacePosition(Position.of(adjacents.waveStep[0], adjacents.waveStep[1]));
                npc.animate(Animation.createOSRS(currentForm.middleHeadAttackAnim));
                for (Position p : adjacents.waveStart) {
                    FIRE_PROJECTILE.send(npc, new Position(npc.getSpawnPosition().getX() + p.getX(), npc.getSpawnPosition().getY() + p.getY()));
                }
                adjacents.coverInFire(npc);
                target.addEvent(e -> { // fire damage
                    e.delay(2);
                    for (int i = 0; i < 40; i++) {
                        if (adjacents.isInFireArea(target, npc)) {
                            doFireDamage(target, true);
                        }
                        e.delay(1);
                    }
                });
                event.delay(3);
                if (currentForm != Form.RED) {
                    cancelFireAttack();
                    return;
                }
            }
            npc.setNextFacePosition(Position.of(fireLockdownArea.waveStep[0], fireLockdownArea.waveStep[1]));
            npc.animate(Animation.createOSRS(currentForm.middleHeadAttackAnim));
            Position firePosition = npc.getSpawnPosition().relative(fireLockdownArea.waveStart[2].getX(), fireLockdownArea.waveStart[2].getY());
            FIRE_PROJECTILE.send(npc, firePosition);
            sendTrackingFire(firePosition, target);

            event.delay(2);
            npc.face(target);
            npc.unlock();
            event.delay(40);
            cancelFireAttack();
        });
    }

    private void sendTrackingFire(Position firePosition, Entity target) {
        target.addEvent(e -> {
            e.delay(1);
            World.sendGraphics(npc, Graphics.createOSRS(1668), firePosition);
            e.delay(1);
            int fireLife = 18;
            int buildUp = 0;
            while (fireLife > 0 && currentForm == Form.RED) {
                int x = firePosition.unitVectorX(target.getLastPosition());
                int y = firePosition.unitVectorY(target.getLastPosition());
                if (x == 0 && y == 0) {
                    if (++buildUp >= 2) {
                        target.applyHit(new Hit(npc).damage(200));
                        doFireDamage(target, false);
                        fireLife -= 3;
                        buildUp = 0;
                        e.delay(2);
                    }
                } else {
                    firePosition.translate(x, y, 0);
                    World.sendGraphics(npc, Graphics.createOSRS(1668), firePosition);
                    registerFire(target, firePosition);
                }
                fireLife--;
                e.delay(1);
            }
        });
    }

    private void registerFire(Entity target, Position firePosition) {
        Position finalFirePosition = firePosition.copy();
        npc.addEvent(event -> {
            for (int i = 0; i < 40 && target != null; i++) {
                if (target.getPosition().equals(finalFirePosition)) {
                    doFireDamage(target, false);
                }
                event.delay(1);
            }
        });
    }

    private void doFireDamage(Entity entity, boolean move) {
        if (move && entity.isPlayer()) {
            Bounds bounds = fireLockdownArea.getBounds(npc);
            int centerX = (bounds.swX + bounds.neX) / 2;
            int centerY = (bounds.swY + bounds.neY) / 2;
            Position center = new Position(centerX, centerY, npc.getZ());
            entity.getAsPlayer().setNextForceMovement(
                    new ForceMovement(
                            new Position(entity.getPosition().unitVectorX(center) * 3, entity.getPosition().unitVectorY(center) * 3, 0), 10,
                            null, 60,
                            Direction.getDirection(entity.getPosition().unitVectorX(center) * 3, entity.getPosition().unitVectorY(center) * 3)));
            entity.animate(1157);
        }
        entity.setNextForceTalk(new ForceTalk("Yowch!"));
        World.startEvent(event -> {
            for (int i = 0; i < 4; i++) {
                entity.applyHit(new Hit(npc).damage(50).look(Hit.HitLook.REGULAR_DAMAGE));
                event.delay(2);
            }
        });
    }

    private void cancelFireAttack() {
        npc.getBounds().forEachPos(Position::unclip);
        npc.setCantFollowUnderCombat(false);
        npc.setCanRetaliate(true);
        firesActive = false;
        npc.unlock();
        npc.setFollowDistance(4);
        npc.setAttackDistance(7);
    }

}
