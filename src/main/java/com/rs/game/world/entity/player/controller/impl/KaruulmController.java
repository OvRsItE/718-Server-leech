package com.rs.game.world.entity.player.controller.impl;

import com.rs.game.item.Item;
import com.rs.game.map.Bounds;
import com.rs.game.map.Direction;
import com.rs.game.map.Position;
import com.rs.game.map.WorldObject;
import com.rs.game.world.entity.npc.instances.hydra.HydraInstance;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.Slayer;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.skills.magic.Magic;
import com.rs.game.world.entity.player.controller.Controller;
import com.rs.game.world.entity.updating.impl.ForceMovement;
import com.rs.game.world.entity.updating.impl.Hit;

/**
 * @author ReverendDread
 * Created 3/13/2021 at 4:53 AM
 * @project 718---Server
 */
public class KaruulmController extends Controller {

    private static final Bounds DUNGEON_BOUNDS = new Bounds(1216, 10112, 1406, 10303, -1);
    private static final Bounds SAFE_BOUNDS = new Bounds(1303, 10187, 1320, 10214, 0);
    public static final Bounds[] SLAYER_ONLY_BOUNDS = {
            new Bounds(1251, 10147, 1279, 10170, 0), // Wyrms
            new Bounds(1300, 10255, 1336, 10277, 0), // Hydras
            new Bounds(1337, 10223, 1366, 10255, 1), // Drakes
    };

    @Override
    public void start() {

    }

    @Override
    public void process() {
        if (!player.getPosition().inBounds(DUNGEON_BOUNDS)) {
            System.out.println("remove controller");
            removeControler();
            return;
        }
        if (!player.inBounds(SAFE_BOUNDS) && !isProtectedFromBurn(player)) {
            player.applyHit(new Hit(player).max(10, 30));
        }
    }

    @Override
    public boolean processItemTeleport(Position toTile) {
        removeControler();
        return true;
    }

    @Override
    public boolean processMagicTeleport(Position toTile) {
        removeControler();
        return true;
    }

    @Override
    public boolean processObjectTeleport(Position toTile) {
        removeControler();
        return true;
    }

    @Override
    public boolean login() {
        return false;
    }

    @Override
    public boolean logout() {
        return false;
    }

    @Override
    public boolean processObjectClick1(WorldObject object) {
        if (object.getId() == WorldObject.asOSRS(34515)) {
            jumpGap(player, object);
        } else if (object.getId() == WorldObject.asOSRS(34516)) {
            crawlThroughTunnel(player, object);
        } else if (object.getId() == WorldObject.asOSRS(34530) && object.getZ() == 0) {
            player.setNextPosition(Position.of(1334, 10205, 1));
        } else if (object.getId() == WorldObject.asOSRS(34531) && object.getZ() == 1) {
            player.setNextPosition(Position.of(1329, 10205, 0));
        } else if (object.getId() == WorldObject.asOSRS(34530) && object.getZ() == 1) {
            player.setNextPosition(Position.of(1318, 10188, 2));
        } else if (object.getId() == WorldObject.asOSRS(34531) && object.getZ() == 2) {
            player.setNextPosition(Position.of(1313, 10188, 1));
        } else if (object.getId() == WorldObject.asOSRS(34548)) {
            player.addEvent(e -> {
                player.lock();
                player.animate(839);
                Position to = Position.of(player.getX(), player.getY() + 2);
                player.setNextPosition(to);
                player.setNextForceMovement(new ForceMovement(to, 2, ForceMovement.NORTH));
                e.delay(2);
                player.unlock();
                HydraInstance.launch(player);
            });
        } else if (object.getId() == WorldObject.asOSRS(34544)) {
            if (!isProtectedFromBurn(player) && player.inBounds(SAFE_BOUNDS)) {
                player.getDialogueManager().startDialogue(new Dialogue() {

                    @Override
                    public void start() {
                        sendDialogue("The floor looks dangerously hot ahead, you will likely need feet protection.", "Perhaps a slayer master could help.", "", "Are you sure you want to continue?");
                    }

                    @Override
                    public void run(int interfaceId, int componentId) {
                        if (stage == -1) {
                            sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Yes.", "No.");
                            stage = 0;
                        } else if (stage == 0) {
                            if (componentId == OPTION_1) {
                                climbWall(player, object);
                            }
                            end();
                        }
                    }

                    @Override
                    public void finish() {

                    }

                });
            } else {
                climbWall(player, object);
            }
        }
        return false;
    }

    private void jumpGap(Player player, WorldObject object) {
        Direction dir = player.getY() > object.getY() ? Direction.SOUTH : Direction.NORTH;
        player.startEvent(event -> {
            player.lock();
            player.animate(3067);
            Position to = Position.of(player.getX() + (dir.deltaX * 5), player.getY() + (dir.deltaY * 5));
            player.setNextForceMovement(new ForceMovement(to, 3, dir.forceMovementDir));
            event.delay(2);
            player.setNextPosition(to);
            if (isInSlayerOnlyArea(player))
                player.sendMessage("In this area, you may only attack monsters if they are your slayer assignment.");
            player.unlock();
        });
    }

    private static void crawlThroughTunnel(Player player, WorldObject gap) {
        Direction dir = player.getX() > gap.getX() ? Direction.WEST : Direction.EAST;
        player.startEvent(e -> {
            player.lock();
            player.animate(2796);
            Position to = Position.of(player.getX() + (dir.deltaX * 7), player.getY() + (dir.deltaY * 7));
            player.setNextPosition(to);
            player.setNextForceMovement(new ForceMovement(to, 3, dir.forceMovementDir));
            e.delay(3);
            player.animate(-1);
            player.unlock();
        });
    }

    private static void climbWall(Player player, WorldObject wall) {
        Direction dir;
        switch (wall.getRotation()) {
            case 0:
            case 2:
                dir = player.getY() < wall.getY() ? Direction.NORTH : Direction.SOUTH;
                break;
            case 1:
            case 3:
                dir = player.getX() < wall.getX() ? Direction.EAST : Direction.WEST;
                break;
            default:
                return;
        }
        player.addEvent(e -> {
            player.lock();
            player.animate(839);
            Position to = Position.of(player.getX() + (dir.deltaX * 2), player.getY() + (dir.deltaY * 2));
            player.setNextPosition(to);
            player.setNextForceMovement(new ForceMovement(to, 2, dir.forceMovementDir));
            e.delay(2);
            player.unlock();
        });
    }

    private static boolean isProtectedFromBurn(Player player) {
        return player.getEquipment().getBootsId() == Item.asOSRS(23037) // boots of stone
                || player.getEquipment().getBootsId() == Item.asOSRS(22951) // boots of brimstone
                || player.getEquipment().getBootsId() == Item.asOSRS(21643) || player.petManager.getNpcId() == 16156; // granite boots
    }

    public static boolean isInSlayerOnlyArea(Player player) {
        for (Bounds b : SLAYER_ONLY_BOUNDS) {
            if (player.getPosition().inBounds(b)) {
                return true;
            }
        }
        return false;
    }

}
