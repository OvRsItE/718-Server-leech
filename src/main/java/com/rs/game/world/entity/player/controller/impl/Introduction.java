package com.rs.game.world.entity.player.controller.impl;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.item.Item;
import com.rs.game.map.RegionBuilder;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.content.FadingScreen;
import com.rs.game.world.entity.player.content.PlayerLook;
import com.rs.game.world.entity.player.controller.Controller;
import com.rs.network.decoders.handlers.ObjectHandler;
import com.rs.utility.Utils;
import com.rs.utility.tools.FileLogger;

import java.io.*;

/**
 * Official Rome introduction as of 3/15/2014
 * Created by Arham 4 on 3/15/14.
 */
public class Introduction extends Controller implements Serializable {
    private static final long serialVersionUID = -5085199980760260952L;
    private int stage;
    private int[] boundChunks;
    private NPC nurse;
    private NPC guard;

    public void setStage(int stage) {
        this.stage = stage;
    }

    @Override
    public void start() {
        if (getArguments().length > 0)
            stage = (Integer) getArguments()[0];
        else
            stage = 0;
        if (stage == 0) {
            player.setFlashGuide(true);
            player.setForceNoClose(true);
            PlayerLook.openCharacterCustomizing(player);
            player.setNextPosition(new Position(0,0,0));
            player.getPackets().sendMusicEffect(-1);
            player.getPackets().sendMusic(-1);
            player.getPackets().sendBlackOut(2);
            BufferedReader reader = null;
            String ip = player.getSession().getIP();
            try {
                reader = new BufferedReader(new FileReader("./data/logs/starters.txt"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.contains(ip))
                    return;
            }
            } catch (FileNotFoundException e) {
                return;
            } catch (IOException e) {
                return;
            }
            player.getBank().addItems(
                    new Item[]{new Item(995, 5000000), new Item(1323), new Item(8845),
                            new Item(1333), new Item(8850), new Item(385, 200), new Item(554, 250),
                            new Item(
                                    555,
                                    250
                            ),
                            new Item(556, 250), new Item(557, 250), new Item(558, 250), new Item(566, 250),
                            new Item(841), new Item(882, 100)}, false
            );
            FileLogger.logData("starters", ip);
        } else {
            generateRegion();
            player.getInterfaceManager().sendInterface(317);
            player.getDialogueManager().startDialogue("Nurse", nurse.getId(), this);
        }
    }

    private void generateRegion() {
        boundChunks = RegionBuilder.findEmptyChunkBound(8, 8);
        RegionBuilder.copyAllPlanesMap(397, 425, boundChunks[0], boundChunks[1], 8);
        player.setNextPosition(getWorldTile(27, 34, 1));
        nurse = new NPC(15017, new Position(player.getX(), player.getY() + 1, player.getZ()), -1, false);
        nurse.spawn();
        World.spawnObject(new WorldObject(1990, 10, 0, getWorldTile(25, 36, 1)), true);
        World.spawnObject(new WorldObject(2071, 10, 0, getWorldTile(28, 36, 1)), true);
        World.spawnObject(new WorldObject(2064, 10, 0, getWorldTile(28, 32, 1)), true);
        guard = new NPC(5919, getWorldTile(27, 36, 0), -1, false);
        guard.setRandomWalk(false);
        NPC guard2 = new NPC(5920, getWorldTile(28, 31, 0), -1, false);
        NPC guard3 = new NPC(5920, getWorldTile(28, 32, 0), -1, false);
        NPC guard4 = new NPC(5920, getWorldTile(28, 33, 0), -1, false);
        guard2.setDirection(Utils.EntityDirection.WEST.getValue());
        guard2.setRandomWalk(false);
        guard3.setDirection(Utils.EntityDirection.WEST.getValue());
        guard3.setRandomWalk(false);
        guard4.setDirection(Utils.EntityDirection.WEST.getValue());
        guard4.setRandomWalk(false);
        World.addNPC(guard2);
        World.addNPC(guard3);
        World.addNPC(guard4);
        PlayerLook.openCharacterCustomizing(player);
    }

    @Override
    public boolean processObjectClick1(WorldObject object) {
        switch (stage) {
            case 1:
                if (object.getX() == getWorldTile(25, 36, 1).getX() && object.getY() == getWorldTile(25, 36, 1).getY()) {
                    player.getDialogueManager().startDialogue("SimpleMessage", "Nothing useful in here.");
                    return false;
                } else if (object.getX() == getWorldTile(28, 36, 1).getX() && object.getY() == getWorldTile(28, 36, 1).getY()) {
                    player.getDialogueManager().startDialogue("SimpleMessage", "A strength potion! Perfect!");
                    player.getInventory().addItem(new Item(119));
                    return false;
                } else if (object.getX() == getWorldTile(28, 32, 1).getX() && object.getY() == getWorldTile(28, 32, 1).getY()) {
                    player.getDialogueManager().startDialogue("SimpleMessage", "Nothing useful in here.");
                    return false;
                } else if (object.getId() == 24355) {
                    player.getDialogueManager().startDialogue("SimpleMessage", "I probably shouldn't try climbing down yet.", "Maybe there is something here that can help gain my strength back.");
                    return false;
                }
                break;
            case 2:
                if (object.getId() == 24355) {
                    player.setNextPosition(new Position(player.getX(), player.getY(), 0));
                    ObjectHandler.handleLadder(player, object, 1);
                    guard.faceEntity(player);
                    player.faceEntity(guard);
                    player.lock();
                    player.getDialogueManager().startDialogue("RomanGuard", guard.getId(), this);
                    return false; // so we can continue the moving.
                }
                break;
        }
        return true;
    }

    @Override
    public boolean canAttack(Entity entity) {
        return false;
    }

    public boolean processItemClick1(Item item) {
        switch (stage) {
            case 1:
                if (item.getId() == 119) {
                    player.getAppearence().setRenderEmote(-1);
                    player.getDialogueManager().startDialogue("Ladder");
                    setStage(2);
                    return true;
                }
                break;
            default:
                if (item.getId() == 757)
                    return true;
                break;
        }
        return true;
    }

    public Position getWorldTile(int mapX, int mapY, int plane) {
        return new Position(boundChunks[0] * 8 + mapX, boundChunks[1] * 8 + mapY, plane);
    }

    public void doStageAction() {
        switch (stage) {
            case 3:
                player.getPackets().sendBlackOut(2);
                player.getCutscenesManager().play("Varrock");
                break;
            case 4:
                player.setNextPosition(getWorldTile(36, 25, 0));
                guard.setNextPosition(getWorldTile(37, 25, 0));
                player.faceEntity(guard);
                guard.faceEntity(player);
                break;
            case 5:
                FadingScreen.fade(player, new Runnable() {
                    @Override
                    public void run() {
                        player.setNextPosition(new Position(3212, 3425, 0));
                        player.unlock();
                        RegionBuilder.destroyMap(boundChunks[0], boundChunks[1], 8, 8);
                        player.getControlerManager().startControler(null);
                        player.setForceNoClose(false);
                        WorldTasksManager.schedule(new WorldTask() {
                            private int timesLooped;
                            @Override
                            public void run() {
                                timesLooped++;
                                if (timesLooped == 2) {
                                    player.getPackets().receivePrivateMessage("arham 4", "<col=ff0000>Emperor</col> Arham 4", 2, "Welcome to Rome!");
                                } else if (timesLooped == 17) {
                                    player.getPackets().receivePrivateMessage("arham 4", "<col=ff0000>Emperor</col> Arham 4", 2, "I suggest you check out the book I wrote, which is in your inventory right now. Also, your items");
                                } else if (timesLooped == 22) {
                                    player.getPackets().receivePrivateMessage("arham 4", "<col=ff0000>Emperor</col> Arham 4", 2, "as stated in the tutorial, are located in your bank. Have fun, Roman!");
                                    this.stop();
                                }
                            }
                        }, 1, 1);
                    }
                });
                break;
        }
    }
}
