package com.rs.game.world.entity.player.content.xeric.dungeon.chamber.handler;

import com.rs.game.item.Item;
import com.rs.game.map.Direction;
import com.rs.game.map.ObjectAction;
import com.rs.game.map.Position;
import com.rs.game.map.WorldObject;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.player.content.xeric.dungeon.chamber.Chamber;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.utility.Misc;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author ReverendDread
 * Created 4/6/2021 at 11:48 PM
 * @project 718---Server
 */
public class CorruptScavengerChamber extends Chamber {

    @RequiredArgsConstructor @Getter
    private enum ChestType {
        
        POISON(29743),
        HATCHING(29744),
        HATCHED(29745),
        BATS(29743),
        EMPTY(29743);
        
        private final int objId;

    }

    private static final int EMPTY_TROUGH = 29746;
    private static final int FILLED_TROUGH = 29874;
    private static final int CLOSED_CHEST = 29742;

    private static final int CORRUPTED_SCAVENGER = 7602;
    private static final int SLEEPING_SCAVENGER = 7603;

    private static final int CAVERN_GRUBS = 20885;
    
    private static final int[][] scavengerSpawns = {
            {7, 14},
            {22, 21},
            {22, 21},
    };

    private static final int[][] deathLocations = {
            {7, 17},
            {25, 21},
            {22, 18},
    };

    private static final int[][] troughLocations = { //x,y,dir
            {7, 13, 2}, //2
            {21, 21, 3}, //3
            {22, 23, 0},
    };

    private WorldObject trough;
    private int grubs = 0;
    private boolean dead;
    
    @Override
    public void onRaidStart() {
        trough = getObject(WorldObject.asOSRS(EMPTY_TROUGH), troughLocations[getLayout()][0], troughLocations[getLayout()][1], troughLocations[getLayout()][2]);
        System.out.println("layout-" + getLayout() + ", rot: " + getRotation());
        if (trough == null) {
            throw new IllegalStateException("Couldn't find trough with rotation " + getRotation()); //todo fix object not being found.
        }
        ObjectAction.register(trough, 1, this::deposit);
        NPC npc = spawnNPC(NPC.asOSRS(CORRUPTED_SCAVENGER), scavengerSpawns[getLayout()][0], scavengerSpawns[getLayout()][1], rotatedDir(Direction.SOUTH, getLayout()), 0);
        npc.getBounds().forEachPos(Position::clip);
        npc.startEvent(event -> {
            int inactiveTicks = 0;
            int eatCooldown = 0;
            while (true) {
                if (eatCooldown > 0)
                    eatCooldown--;
                else if (grubs > 0) {
                    npc.animate(Animation.createOSRS(256));
                    grubs--;
                    if (grubs == 0)
                        trough.restore();
                    npc.setHitpoints(Math.max(0, npc.getHitpoints() - 8));
                    npc.faceObject(trough);
                    eatCooldown = 2;
                    if (npc.getHitpoints() <= 0) {
                        npc.setHitpoints(0);
                        dead = true;
                        death(npc);
                        return;
                    }
                } else if (++inactiveTicks >= 6) {
                    npc.setHitpoints(Math.min(npc.getHitpoints() + 1, npc.getMaxHitpoints()));
                    inactiveTicks = 0;
                }
                npc.applyHit(new Hit(null).look(Hit.HitLook.REGULAR_DAMAGE).damage(1));
                event.delay(1);
            }
        });
    }

    public static ChestType roll() {
        if (Misc.rollDie(20, 1))
            return ChestType.BATS;
        if (Misc.rollDie(12, 1))
            return ChestType.POISON;
        if (Misc.rollDie(3, 1))
            return ChestType.HATCHING;
        return ChestType.HATCHED;
    }

    private void deposit(Player player, WorldObject gameObject) {
        int amount = player.getInventory().getAmountOf(Item.asOSRS(CAVERN_GRUBS));
        if (amount == 0) {
            player.sendMessage("You don't have any cavern grubs to deposit on the trough.");
            return;
        }
        if (dead) {
            player.sendMessage("Theres no point to deposting any more grubs.");
            return;
        }
        player.animate(832);
        player.getInventory().deleteItem(Item.asOSRS(CAVERN_GRUBS), amount);
        grubs += amount;
        if (WorldObject.is(EMPTY_TROUGH, true, trough)) {
            trough.setId(WorldObject.asOSRS(FILLED_TROUGH));
            trough.sendUpdate();
        }
        player.sendMessage("You deposit the cave grubs in the trough.");
        getDungeon().addPoints(player, amount * 50);
    }

    private void death(NPC npc) {
        npc.getBounds().forEachPos(Position::unclip);
        npc.startEvent(event -> {
            Position pos = getPosition(deathLocations[getLayout()][0], deathLocations[getLayout()][1]);
            npc.addWalkSteps(pos.getX(), pos.getY());
            event.waitForMovement(npc);
            event.delay(1);
            npc.animate(Animation.createOSRS(7497));
            npc.setNextForceTalk("Yawwwwwwwn!");
            event.delay(3);
            npc.transformIntoNPC(NPC.asOSRS(SLEEPING_SCAVENGER));
        });
    }
    
    static {
        ObjectAction.register(WorldObject.asOSRS(CLOSED_CHEST), 1, (player, obj) -> {
            player.startEvent(event -> {
                while (true) {

                    //if chest isnt closed, stop
                    if (obj.getId() != WorldObject.asOSRS(CLOSED_CHEST))
                        return;

                    player.animate(832);
                    if (Misc.get() > (player.getSkills().getLevel(Skills.THIEVING) / 2.0 / 100.0)) {
                        event.delay(1);
                    } else {
                        CorruptScavengerChamber.ChestType type = obj.getAtt("GRUB_CHEST_TYPE", roll());
                        CorruptScavengerChamber.ChestType nextType = type;
                        obj.temporary(WorldObject.asOSRS(type.getObjId()), 12);
                        switch (type) {
                            case POISON:
                                World.sendGraphics(player, Graphics.createOSRS(184, 0, 100), Position.of(obj.getX(), obj.getY(), obj.getZ()));
                                player.applyHit(new Hit().max(10, 30).look(Hit.HitLook.POISON_DAMAGE));
                                break;
                            case EMPTY:
                                player.sendMessage("This chest is empty.");
                                break;
                            case BATS:
                                player.getInventory().addItemDrop(Item.asOSRS(20883), 6);
                                player.getDialogueManager().startDialogue("ItemMessage", "You find some bats in the chest.", Item.asOSRS(20883));
                                nextType = CorruptScavengerChamber.ChestType.EMPTY;
                                break;
                            case HATCHING:
                                player.getDialogueManager().startDialogue("SimpleMessage", "No cocoons in this chest have hatched.");
                                nextType = CorruptScavengerChamber.ChestType.HATCHED;
                                break;
                            case HATCHED:
                                player.getInventory().addItemDrop(Item.asOSRS(CAVERN_GRUBS), 1);
                                player.getDialogueManager().startDialogue("ItemMessage", "Some cavern grubs have hatched.", Item.asOSRS(CAVERN_GRUBS));
                                break;
                        }
                        obj.setAtt("GRUB_CHEST_TYPE", nextType.name());
                    }
                    player.lock(1); //lock for an extra tick bc feels better
                }
            }).setCancelCondition(player::hasWalkSteps);
        });
    }

}
