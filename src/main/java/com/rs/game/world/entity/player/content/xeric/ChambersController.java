package com.rs.game.world.entity.player.content.xeric;

import com.rs.game.item.Item;
import com.rs.game.map.Position;
import com.rs.game.map.WorldObject;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.xeric.dungeon.XericDungeon;
import com.rs.game.world.entity.player.controller.Controller;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * @author ReverendDread
 * Created 4/11/2021 at 7:52 PM
 * @project 718---Server
 */
@RequiredArgsConstructor
public class ChambersController extends Controller {

    //The xeric dungeon instance.
    private final XericDungeon dungeon;

    @Override
    public void start() {}

    @Override
    public boolean processObjectClick1(WorldObject object) {
        //doors
        if (WorldObject.is(29789, true, object)) {
            if (!dungeon.getGroup().getBoolean(ChambersOfXeric.STARTED)) {
                if (!dungeon.getGroup().isLeader(player))
                    player.getDialogueManager().startDialogue("SimpleMessage", "Your group leader hasn't started the raid.");
                else
                    dungeon.start();
            } else { //dungeon is started, lets interact.
                int x = player.getX();
                int y = player.getY();
                if (object.getRotation() == 0 || object.getRotation() == 2) {
                    if (x < object.getX())
                        x += 4;
                    else
                        x -= 4;
                } else {
                    if (y < object.getX())
                        y += 4;
                    else
                        y -= 4;
                }
                player.setNextPosition(Position.of(x, y, player.getZ()));
            }
        }
        //energy pool
        if (WorldObject.is(30066, true, object)) {
            player.setRunEnergy(100);
        }
        //chambers exit
        if (WorldObject.is(29778, true, object)) {
            player.getDialogueManager().startDialogue(new Dialogue() {

                @Override
                public void start() {
                    boolean started = dungeon.getGroup().getBoolean(ChambersOfXeric.STARTED);
                    sendOptionsDialogue(started ? "The raid has already begun, you will not be able to re-enter." : "If the raid begins, you won't be able to re-enter.", "Leave.", "Stay.");
                }

                @Override
                public void run(int interfaceId, int componentId) {
                    if (componentId == OPTION_1) {
                        dungeon.leave(player);
                    }
                    end();
                }

                @Override
                public void finish() {

                }

            });
        }
        //hole to 1st from 2nd
        if (WorldObject.is(29734, true, object)) {
            player.setNextPosition(dungeon.getLowerStartingChamber().getEntrancePosition());
            if (dungeon.checkpoint < 2) {
                dungeon.checkpoint = 2;
                String time = dungeon.getTimeSinceStart();
                dungeon.getGroup().notifyAll("<col=ef20ff>Upper level complete! Duration: <col=ff0000>" + time + "</col><col=ef20ff>.");
            }
        }
        //hole to 2nd floor from 1st
        if (WorldObject.is(29995, true, object)) {
            player.setNextPosition(dungeon.getUpperFinishChamber().getEntrancePosition());
        }
        //entrance to olms floor
        if (WorldObject.is(29735, true, object)) {
            player.setNextPosition(Position.of(dungeon.getMap().sw.getBaseX() + 32, dungeon.getMap().sw.getBaseY() + 25, 0));
            if (dungeon.checkpoint < 3) {
                dungeon.checkpoint = 3;
                String time = dungeon.getTimeSinceStart();
                dungeon.getGroup().notifyAll("<col=ef20ff>Lower level complete! Duration: <col=ff0000>" + time + "</col><col=ef20ff>.");
            }
        }
        //exit from olms floor to 1st floor
        if (WorldObject.is(29996, true, object)) {
            player.setNextPosition(dungeon.getLowerFinishChamber().getEntrancePosition());
        }
        return true;
    }

    @Override
    public boolean processItemTeleport(Position destination) {
        player.sendMessage("You cannot teleport from Chambers of Xeric.");
        return false;
    }

    @Override
    public boolean processObjectTeleport(Position destination) {
        player.sendMessage("You cannot teleport from Chambers of Xeric.");
        return false;
    }

    @Override
    public boolean processMagicTeleport(Position destination) {
        player.sendMessage("You cannot teleport from Chambers of Xeric.");
        return false;
    }

    @Override
    public boolean sendDeath() {
        finish();
        return true;
    }

    @Override
    public boolean canSummonFamiliar() {
        return false;
    }

    @Override
    public void forceClose() {
        finish();
    }

    private void finish() {
        //remove all cox items and drop them on the ground.
        Map<Integer, Item> removed = player.getInventory().getItems().removeIf(item -> item.getDefinitions().cox);
        removed.forEach((slot, item) -> World.addGroundItem(item, player.getPosition()));
        //restore stats
        player.getSkills().restoreSkills();
    }

}
