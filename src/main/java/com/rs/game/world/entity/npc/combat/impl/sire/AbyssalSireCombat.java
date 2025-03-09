package com.rs.game.world.entity.npc.combat.impl.sire;

import com.rs.cores.coroutines.CoroutineEvent;
import com.rs.game.map.Direction;
import com.rs.game.map.Position;
import com.rs.game.map.WorldObject;
import com.rs.game.world.Projectile;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.listeners.AttackNPCListener;
import com.rs.game.world.entity.listeners.DeathListener;
import com.rs.game.world.entity.listeners.HitListener;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.sire.RespritorySystem;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.toxin.ToxinType;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.utility.Misc;
import com.rs.utility.TickDelay;
import kilim.Pausable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author ReverendDread
 * Created 3/2/2021 at 9:01 AM
 * @project 718---Server
 */
public class AbyssalSireCombat extends CombatScript {

    private static final int TENTACLE_WAKE_UP_ANIM = 7108;
    private static final int TENTACLE_ATTACK_ANIM = 7109;
    private static final int TENTACLE_WAKE_UP_ANIM_2 = 7111;
    private static final int TENTACLE_SLEEP_ANIM = 7112;
    private static final int TENTACLE_WAKE_UP_ANIM_3 = 7114;

    private static final int SIRE_WAKE_ANIM = 4528;
    private static final int SIRE_PHASE_1_SPAWN = 4530;
    private static final int SIRE_PHASE_1_POISON = 4531;
    private static final int SIRE_PHASE_1_TRANSITION = 4532;

    private static final int SIRE_PHASE_2_MELEE = 5366;
    private static final int SIRE_PHASE_2_POISON = 5367;
    private static final int SIRE_PHASE_2_SPAWN = 7095;
    private static final int SIRE_PHASE_2_STRONG_MELEE = 5369;
    private static final int SIRE_PHASE_2_STRONGER_MELEE = 5755;
    private static final int SIRE_PHASE_2_TRANSITION = 7096;

    private static final int SIRE_PHASE_3_EXPLODE = 7098;
private static final Projectile SPAWN_PROJECTILE = new Projectile(Graphics.createOSRS(1274).getId(), 70, 0, 20, 16, 30, 0);

    private static final int POISON_EFFECT = 1275;

    //npc ids
    private static final int STUNNED_TENTACLE = 5911;
    private static final int ACTIVE_TENTACLE = 5912;
    private static final int SPAWN = 5916;

    private final List<NPC> tentacles = new ArrayList<>(6);
    private final List<NPC> respiratorySystemNPCs = new ArrayList<>(4);
    private final List<WorldObject> respiratorySystemObjects = new ArrayList<>(4);
    private final List<NPC> spawns = new LinkedList<>();
    private NPC sire, nwTentacle, wTentacle, swTentacle, neTentacle, eTentacle, seTentacle;

    private int phase = 0;
    private int activatedBy = -1;
    private final TickDelay resetDelay = new TickDelay();
    private int attempts = 0;

    private Entity target;

    @Override
    public Object[] getKeys() {
        return new Object[] { "Abyssal Sire" };
    }

    @Override
    public boolean init(NPC npc) {
        this.sire = npc;
        //tentacles
        tentacles.add(nwTentacle = new NPC(NPC.asOSRS(5910), Position.of(npc.getSpawnPosition().getX() - 10, npc.getSpawnPosition().getY() - 11, npc.getSpawnPosition().getZ()), -1, Direction.SOUTH.ordinal(), false, false)); //nw tentacle
        tentacles.add(wTentacle = new NPC(NPC.asOSRS(STUNNED_TENTACLE), Position.of(npc.getSpawnPosition().getX() - 7, npc.getSpawnPosition().getY() - 20, npc.getSpawnPosition().getZ()), -1, Direction.SOUTH.ordinal(), false, false)); //w tentacle
        tentacles.add(swTentacle = new NPC(NPC.asOSRS(5909), Position.of(npc.getSpawnPosition().getX() - 9, npc.getSpawnPosition().getY() - 29, npc.getSpawnPosition().getZ()), -1, Direction.SOUTH.ordinal(), false, false)); //sw tentacle
        tentacles.add(neTentacle = new NPC(NPC.asOSRS(5910), Position.of(npc.getSpawnPosition().getX() + 7, npc.getSpawnPosition().getY() - 11, npc.getSpawnPosition().getZ()), -1, Direction.SOUTH.ordinal(), false, false)); //ne tentacle
        tentacles.add(eTentacle = new NPC(NPC.asOSRS(STUNNED_TENTACLE), Position.of(npc.getSpawnPosition().getX() + 5, npc.getSpawnPosition().getY() - 20, npc.getSpawnPosition().getZ()), -1, Direction.SOUTH.ordinal(), false, false)); //e tentacle
        tentacles.add(seTentacle = new NPC(NPC.asOSRS(5909), Position.of(npc.getSpawnPosition().getX() + 8, npc.getSpawnPosition().getY() - 29, npc.getSpawnPosition().getZ()), -1, Direction.SOUTH.ordinal(), false, false)); //se tentacle
        tentacles.forEach(n -> n.addEvent(event -> tentacleLogic(n, event)));
        //respiratory sys objects
        Position nwResp = npc.getSpawnPosition().relative(-13, -11);
        Position neResp = npc.getSpawnPosition().relative(15, -12);
        Position seResp = npc.getSpawnPosition().relative(18, -22);
        Position swResp = npc.getSpawnPosition().relative(-10, -21);
        respiratorySystemObjects.add(World.getObjectWithId(Position.of(nwResp.getX(), nwResp.getY(), nwResp.getZ()), WorldObject.asOSRS(26954)));
        respiratorySystemObjects.add(World.getObjectWithId(Position.of(swResp.getX(), swResp.getY(), swResp.getZ()), WorldObject.asOSRS(26954)));
        respiratorySystemObjects.add(World.getObjectWithId(Position.of(neResp.getX(), neResp.getY(), neResp.getZ()), WorldObject.asOSRS(26954)));
        respiratorySystemObjects.add(World.getObjectWithId(Position.of(seResp.getX(), seResp.getY(), seResp.getZ()), WorldObject.asOSRS(26954)));
        respiratorySystemObjects.stream().filter(Objects::nonNull).forEach(o -> o.setId(WorldObject.asOSRS(26953)));
        //respiratory sys npcs
        respiratorySystemNPCs.add(new RespritorySystem(NPC.asOSRS(5914), nwResp));
        respiratorySystemNPCs.add(new RespritorySystem(NPC.asOSRS(5914), swResp));
        respiratorySystemNPCs.add(new RespritorySystem(NPC.asOSRS(5914), neResp));
        respiratorySystemNPCs.add(new RespritorySystem(NPC.asOSRS(5914), seResp));

        AttackNPCListener attackListener = (player, sire) -> {
//            if (!player.getSlayerManager().hasTask(Slayer.SlayerTask.ABYSSAL_DEMON)) {
//                player.sendMessage("The Sire takes no interest in you.");
//                return false;
//            }
            if (activatedBy != -1 && player.getIndex() != activatedBy) {
                player.sendMessage("The Sire takes no interest in you.");
                return false;
            }
            return !npc.isLocked();
        };
        respiratorySystemNPCs.forEach(resp -> {
            resp.deathEndListener = (entity, killer) -> respiratoryDeath(resp);
            resp.hitListener = new HitListener().preDamage(hit -> {
                if (sire.getId() != NPC.asOSRS(5888))
                    hit.damage(0);
                if (tentacles.get(0).getId() == NPC.asOSRS(ACTIVE_TENTACLE))
                    hit.damage(hit.getDamage() / 10);
            });
            resp.attackNPCListener = attackListener;
            resp.setCanRetaliate(false);
        });
        npc.attackNPCListener = attackListener;
        npc.deathEndListener = ((entity, killer) -> resetStates());
        npc.hitListener = new HitListener().preDamage(this::postDefend);
        npc.addEvent(event -> {
            event.delay(100);
            while (true) {
                if (phase > 0 && !resetDelay.allowed()) {
                    Player owner = World.getPlayers().get(activatedBy);
                    if (owner == null || !owner.getPosition().isWithinDistance(npc.getPosition(), 30)){
                        npc.setRespawnTask();
                        resetStates();
                    }
                }
                event.delay(5);
            }
        });
        sire.getToxin().applyImmunity(ToxinType.POISON, Integer.MAX_VALUE);
        sire.getToxin().applyImmunity(ToxinType.VENOM, Integer.MAX_VALUE);
        sire.setAttackDistance(15);
        sire.setCantFollowUnderCombat(true);
        sire.setCanBeFrozen(false);
        sire.setCapDamage(750);
        return true;
    }

    @Override
    public int attack(NPC npc, Entity target) {
        this.target = target;
        switch (phase) {
            case 1:
                sire.faceNone();
                if (Misc.rollDie(3, 1)) { // doesnt actually attack every turn
                    if (Misc.rollDie(4, 1))
                        projectileSpawn();
                    else
                        poisonPool(target.getPosition().copy());
                }
                break;
            case 2:
                if (!target.withinDistance(npc.getMiddleTile(), 3)) {
                    if (!npc.hasWalkSteps()) {
                        if (++attempts == 3) {
                            teleportPlayer();
                            attempts = 0;
                            return 1;
                        }
                    }
                } else {
                    if (Misc.rollDie(6, 4))
                        meleeAttack();
                    else if (Misc.rollDie(2, 1))
                        poisonPool(target.getPosition().copy());
                    else
                        projectileSpawn();
                }
                break;
            case 3:
                sire.faceNone();
                poisonPool(target.getPosition().copy());
                if (Misc.rollDie(3, 1))
                    spawn();
                break;
            case 4:
                sire.faceNone();
                spawn();
                if (Misc.rollDie(2, 1)) // anotha one
                    spawn();
                break;
        }
        return 4;
    }

    private void postDefend(Hit hit) {
        resetDelay.delay(50);
        if (phase == 0 && hit.getSource() != null && hit.getSource().isPlayer()) {
            startFight(hit.getSource().getAsPlayer().getIndex());
            hit.getSource().getAsPlayer().deathStartListener = (DeathListener.SimpleKiller) killer -> activatedBy = -1;
            hit.getSource().getAsPlayer().teleportListener = () -> {
                activatedBy = -1;
                return true;
            };
        } else if (phase == 1 && sire.getId() != NPC.asOSRS(5888)) { // look for stun
            if (sire.getHitpoints() - hit.getDamage() <= 0) { //make sure sire doesnt die
                hit.setDamage(0);
            }
            if (sire.getHitpoints() - hit.getDamage() <= sire.getMaxHitpoints() - 75) {
                stun();
                hit.getSource().getAsPlayer().sendMessage("The Sire has been disorientated.");
            } else if (hit.getSource().isPlayer() && Misc.get() <= getStunChance(hit.getSource().getAsPlayer())) {
                stun();
                hit.getSource().getAsPlayer().sendMessage("Your Shadow spell disorientates the Sire.");
            }
        } else if (phase == 2 && sire.getHitpoints() < sire.getMaxHitpoints() / 2) {
            startPhase3();
        } else if (phase == 3 && sire.getHitpoints() < 140) {
            hit.setDamage(0);
            startPhase4();
        }
    }

    private void startPhase4() {
        phase = 4;
        sire.transformIntoNPC(NPC.asOSRS(5908));
        sire.animate(Animation.createOSRS(SIRE_PHASE_3_EXPLODE));
        sire.faceNone();
        sire.lock();
        sire.addEvent(event -> {

            //problem here
            teleportPlayer();
            event.delay(6);
            sire.getCombat().setCombatDelay(7);

            sire.localPlayers().forEach(p -> {
                if (p.withinDistance(sire.getMiddleTile(), 2)) {
                    p.applyHit(new Hit(sire).max(0, 720).look(Hit.HitLook.REGULAR_DAMAGE));
                }
            });

            int spawns = Misc.get(3, 6);
            for (int i = 0; i < spawns; i++) {
                spawn(Misc.get(sire.localPlayers()), sire.getPosition().relative(Misc.get(1, 4), Misc.get(1, 4)));
            }

            sire.unlock();
            sire.faceNone();
        });
    }

    private void startPhase3() {
        phase = 3;
        sire.lock();
        sire.faceNone();
        sire.setCantInteract(true);
        sire.setInvincible(true);
        target.resetTarget();
        sire.addEvent(event -> {
            Position dest = sire.getSpawnPosition().relative(0, -18);
            while (!sire.isAt(dest.getX(), dest.getY())) {
                sire.addWalkSteps(dest.getX(), dest.getY(), 1, false);
                event.delay(1);
            }
            sire.animate(Animation.createOSRS(SIRE_PHASE_2_TRANSITION));
            event.delay(2);
            sire.transformIntoNPC(NPC.asOSRS(5891));
            event.delay(6);
            recoverTentacles();
            sire.unlock();
            sire.setCantInteract(false);
            sire.setInvincible(false);
        });
    }

    private void teleportPlayer() {
        if (target == null)
            target = Misc.get(sire.localPlayers());
        final Player player = target.getAsPlayer();
        player.addEvent(e -> {
            player.animate(1816);
            player.setNextGraphics(342);
            player.lock();
            e.delay(3);
            player.animate(-1);
            player.setNextPosition(Position.of(sire.getX() + 2, sire.getY() - 1));
            player.unlock();
        });
    }

    private void tentacleLogic(NPC tentacle, CoroutineEvent event) throws Pausable {
        while (true) {
            if (tentacle.localPlayers().size() == 0) { // thought this wasnt necessary but apparently it is
                event.delay(3);
                continue;
            }
            Player player = Misc.get(tentacle.localPlayers());
            if (sire.isDead() || player == null || tentacle.getId() != NPC.asOSRS(ACTIVE_TENTACLE) || phase == 2) {
                event.delay(5);
            } else {
                tentacle.setNextFacePosition(player.getPosition());
                if (player.getPosition().getX() >= tentacle.getX() && player.getPosition().getX() <= tentacle.getX() + 8
                        && player.getPosition().getY() >= tentacle.getY() && player.getPosition().getY() <= tentacle.getY() + 8) {
                    tentacle.animate(Animation.createOSRS(TENTACLE_ATTACK_ANIM));
                    player.applyHit(new Hit(tentacle).max(0, 300).look(Hit.HitLook.REGULAR_DAMAGE)); // no attacker, tentacles have no stats
                    event.delay(4);
                } else {
                    event.delay(2);
                }
            }
        }
    }

    private void respiratoryDeath(NPC respSystem) {
        WorldObject obj = World.getObjectWithId(Position.of(respSystem.getX(), respSystem.getY(), respSystem.getZ()), WorldObject.asOSRS(26953));
        obj.animate(Animation.createOSRS(7102));
        World.startEvent(event -> {
            event.delay(5);
            obj.setId(WorldObject.asOSRS(26954));
        });
        if (respiratorySystemNPCs.stream().allMatch(Entity::isFinished) && phase == 1) { // all systems dead, onwards to phase 2!
            stunTentacles();
            phase = 2;
            sire.addEvent(event -> {
                sire.setCanRetaliate(false);
                sire.setHitpoints(sire.getMaxHitpoints());
                sire.transformIntoNPC(NPC.asOSRS(5889));
                sire.animate(Animation.createOSRS(SIRE_PHASE_1_TRANSITION));
                event.delay(5);
                Position dest = sire.getSpawnPosition().relative(0, -7);
                while (!sire.isAt(dest.getX(), dest.getY())) {
                    sire.addWalkSteps(dest.getX(), dest.getY(), 1, false);
                    event.delay(1);
                }
                sire.transformIntoNPC(NPC.asOSRS(5890));
                sire.setCanRetaliate(true);
            });
        }
    }

    private void stunTentacles() {
        tentacles.forEach(n -> {
            if (n.getId() == NPC.asOSRS(STUNNED_TENTACLE)) // already sleepin
                return;
            n.transformIntoNPC(NPC.asOSRS(STUNNED_TENTACLE));
            n.animate(Animation.createOSRS(TENTACLE_SLEEP_ANIM));
            n.faceNone();
        });
    }

    private void stun() {
        sire.transformIntoNPC(NPC.asOSRS(5888));
        stunTentacles();
        sire.getCombat().setCombatDelay(30);
        //delayAttack(30); //TODO
        sire.addEvent(event -> {
            event.delay(50);
            if (phase != 1) { // player killed all resp systems while we were stunned
                return;
            }
            sire.transformIntoNPC(NPC.asOSRS(5887));
            sire.animate(Animation.createOSRS(SIRE_WAKE_ANIM));
            sire.setHitpoints(sire.getMaxHitpoints());
            recoverTentacles();
        });
    }

    private void recoverTentacles() {
        if (phase == 2)
            return;
        tentacles.forEach(n -> {
            n.animate(TENTACLE_WAKE_UP_ANIM_3);
            n.addEvent(event -> {
                event.delay(2);
                if (sire.getId() == NPC.asOSRS(5888))
                    return;
                n.transformIntoNPC(NPC.asOSRS(ACTIVE_TENTACLE));
            });
        });
    }

    private double getStunChance(Player player) {
        int spellId = player.getCombatDefinitions().getSpellId();
        boolean manualCast = spellId >= 256;
        int spell = manualCast ? spellId - 256 : spellId;
        if (spell == 0)
            return 0;
        else if (spell == 32)
            return 0.25;
        else if (spell == 34)
            return 0.5;
        else if (spell == 33)
            return 0.75;
        else if (spell == 35)
            return 1;
        else
            return 0;
    }

    private void startFight(int uid) {
        activatedBy = uid;
        sire.animate(Animation.createOSRS(SIRE_WAKE_ANIM));
        phase = 1;
        sire.transformIntoNPC(NPC.asOSRS(5887));
       // delayAttack(8); //TODO
        wakeTentacles();
    }

    private void wakeTentacles() { // from INITIAL state, not stunned
        nwTentacle.animate(Animation.createOSRS(TENTACLE_WAKE_UP_ANIM));
        neTentacle.animate(Animation.createOSRS(TENTACLE_WAKE_UP_ANIM));
        wTentacle.animate(Animation.createOSRS(TENTACLE_WAKE_UP_ANIM_3));
        eTentacle.animate(Animation.createOSRS(TENTACLE_WAKE_UP_ANIM_3));
        swTentacle.animate(Animation.createOSRS(TENTACLE_WAKE_UP_ANIM_2));
        seTentacle.animate(Animation.createOSRS(TENTACLE_WAKE_UP_ANIM_2));
        tentacles.forEach(n -> {
            n.addEvent(event -> {
                event.delay(2);
                n.transformIntoNPC(NPC.asOSRS(5912));
            });
        });
    }

    private void resetStates() {
        respiratorySystemNPCs.forEach(NPC::spawn);
        respiratorySystemObjects.forEach(o -> o.setId(WorldObject.asOSRS(26953)));
        phase = 0;
        sire.transformIntoNPC(NPC.asOSRS(5886));
        nwTentacle.transformIntoNPC(NPC.asOSRS(5910));
        wTentacle.transformIntoNPC(NPC.asOSRS(STUNNED_TENTACLE));
        swTentacle.transformIntoNPC(NPC.asOSRS(5909));
        neTentacle.transformIntoNPC(NPC.asOSRS(5910));
        eTentacle.transformIntoNPC(NPC.asOSRS(STUNNED_TENTACLE));
        seTentacle.transformIntoNPC(NPC.asOSRS(5909));
        tentacles.forEach(Entity::faceNone);
        spawns.forEach(NPC::finish);
        spawns.clear();
        activatedBy = -1;
    }

    private void meleeAttack() { // phase 2 ONLY
        if (phase != 2)
            throw new IllegalStateException();
        double roll = Misc.get();
        int minDamage, maxDamage;
        if (roll < 0.25) { // double back swipe
            minDamage = 80;
            maxDamage = 260;
            sire.animate(Animation.createOSRS(SIRE_PHASE_2_STRONGER_MELEE));
        } else if (roll < 0.65) { // single back swipe
            minDamage = 40;
            maxDamage = 120;
            sire.animate(Animation.createOSRS(SIRE_PHASE_2_STRONG_MELEE));
        } else { // arm swipe
            minDamage = 20;
            maxDamage = 60;
            sire.animate(Animation.createOSRS(SIRE_PHASE_2_MELEE));
        }
        if (target.isPlayer()) {
            if (!target.getAsPlayer().getPrayer().isMeleeProtecting()) {
                minDamage *= 2;
                maxDamage *= 2;
            }
        }
        target.applyHit(new Hit(sire).max(minDamage, maxDamage).look(Hit.HitLook.MELEE_DAMAGE));
    }

    private void projectileSpawn() {
        spawns.removeIf(Entity::isDead);
        if (spawns.size() >= 14)
            return;
        if (phase == 1)
            sire.animate(Animation.createOSRS(SIRE_PHASE_1_SPAWN));
        else
            sire.animate(Animation.createOSRS(SIRE_PHASE_2_SPAWN)); // phases 3 and 4 don't do projectile-based spawns!
        Position dest = Misc.get(target.getPosition().area(2, pos -> !pos.isClipped()));
        int delay = SPAWN_PROJECTILE.send(sire, Position.of(dest.getX(), dest.getY()));
        final Entity entity = target;
        sire.addEvent(event -> {
            event.delay(delay);
            if (sire.isDead())
                return;
            spawn(entity, dest);
        });
    }

    private void spawn() {
        spawn(target, Misc.get(target.getPosition().area(2, pos -> !pos.isClipped())));
    }

    private void spawn(Entity entity, Position dest) {
        spawns.removeIf(Entity::isDead);
        if (spawns.size() >= 14)
            return;
        if (target == null) {
            return;
        }
        NPC spawn = new NPC(NPC.asOSRS(SPAWN), dest, sire.getMapAreaNameHash(), sire.canBeAttackFromOutOfArea(), true);
        spawn.addEvent(event -> {
            event.delay(Misc.random(14, 18)); //wait ~10 seconds
            spawn.transformIntoNPC(NPC.asOSRS(5918));
            spawn.animate(Animation.createOSRS(7123));
        });
        spawn.setTarget(entity);
        spawns.add(spawn);
    }

    private void poisonPool(Position pos) {
        if (phase == 1)
            sire.animate(Animation.createOSRS(SIRE_PHASE_1_POISON));
        else if (phase == 2)
            sire.animate(Animation.createOSRS(SIRE_PHASE_2_POISON));
        // phase 3 and 4 don't do an animation on spawning a pool
        World.startEvent(event -> {
            World.sendGraphics(sire, Graphics.createOSRS(POISON_EFFECT), pos);
            event.delay(2);
            for (int i = 0; i < 4; i++) {
                pos.getRegion().players().forEach(p -> { // IMO it would make more sense to just check for the sire's target, but OSRS does an AOE check so i guess i'll do that too... whatever
                    int distance = Misc.getDistance(p.getPosition(), pos);
                    if (distance > 1)
                        return;
                    int maxDamage = 300;
                    if (p.getToxin().isImmune(ToxinType.POISON))
                        maxDamage *= 0.7;
                    maxDamage /= distance + 10;
                    p.applyHit(new Hit(sire).max(10, maxDamage).look(Hit.HitLook.POISON_DAMAGE));
                    if (Misc.rollDie(5, 1))
                        p.getToxin().applyToxin(ToxinType.POISON, 8);
                });
                event.delay(1);
            }
        });
    }

}
