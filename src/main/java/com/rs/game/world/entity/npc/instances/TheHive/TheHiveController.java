package com.rs.game.world.entity.npc.instances.TheHive;

import com.rs.Constants;
import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.map.WorldObject;
import com.rs.game.world.entity.npc.instances.EliteDungeon.EliteDungeonOne;
import com.rs.game.world.entity.npc.instances.EliteDungeon.MinionNpc;
import com.rs.game.world.entity.player.content.chest.RaidsRewards;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.player.controller.Controller;
import com.rs.game.world.entity.updating.impl.Animation;


public class TheHiveController extends Controller {

    private TheHive getDungeon() {
        return player.getThehive();
    }

    @Override
    public void start() {

    }

    @Override
    public void process() {
        getDungeon().checkqueen();
        super.process();
    }

    @Override
    public boolean processItemTeleport(Position destination) {
        player.sendMessage("You cannot teleport from a dungeon leave via the entrance or finish the boss.");
        return false;
    }

    @Override
    public boolean processObjectTeleport(Position destination) {
        player.sendMessage("You cannot teleport from a dungeon leave via the entrance or finish the boss.");
        return false;
    }

    @Override
    public boolean processMagicTeleport(Position destination) {
        player.sendMessage("You cannot teleport from a dungeon leave via the entrance or finish the boss.");
        return false;
    }

    @Override
    public boolean login() {
        player.setNextPosition(Constants.START_PLAYER_LOCATION);
        return true;
    }

    @Override
    public boolean logout() {
        getDungeon().leave(player);
        return false;
    }

    @Override
    public boolean sendDeath() {
        WorldTasksManager.schedule(new WorldTask() {
            int loop;

            @Override
            public void run() {
                if (loop == 0) {
                    player.animate(new Animation(836));
                } else if (loop == 1) {
                    player.getPackets().sendGameMessage(
                            "Oh dear, you have died.");
                } else if (loop == 3) {
                    player.reset();
                    player.setNextPosition(getDungeon().getLocation(2954, 5910, 0));
                    player.animate(new Animation(-1));
                } else if (loop == 4) {
                    player.getPackets().sendMusicEffect(90);
                    stop();
                }
                loop++;
            }
        }, 0, 1);
        return false;
    }

    @Override
    public boolean processObjectClick1(WorldObject object) {
        if (object.getId() == 230397 && player.getPosition().withinDistance(getDungeon().getLocation(2993, 5920, 0), 1)) {
            if (player.getPosition().withinDistance(getDungeon().getLocation(2993, 5921, 0), 1) || player.getPosition().withinDistance(getDungeon().getLocation(2992, 5921, 0), 1) ) {
                if (getDungeon().getOwner() == player && !getDungeon().isStarted()) {
                    getDungeon().setStarted(true);
                    getDungeon().hpscale();
                    player.sm("The dungeon has now been started");
                }
                else {
                    if (!getDungeon().isStarted()) {
                        player.sm("The owner needs to start the dungeon first.");
                        return true;
                    }
                }
                player.addEvent(event -> {
                    player.animate(798);
                    event.delay(1);
                    player.setNextPosition(new Position(player.getPosition().getX(), player.getPosition().getY() - 2, 0));
                });
                return true;
            }
        }

        if (object.getId() == 229996 && player.getPosition().withinDistance(getDungeon().getLocation(2993, 5919, 0), 2)) {
            if (!getDungeon().getQueen_Bee().isFinished()) {
                player.sm("You need to finish the queen first.");
                return true;
            }
            if (player.getPosition().withinDistance(getDungeon().getLocation(2993, 5919, 0), 1)) {
                player.addEvent(event -> {
                    player.animate(828);
                    event.delay(1);
                getDungeon().leave(player);
                finish();
                });
                return true;
            }

        }
        if (object.getId() == 200195 && player.getPosition().withinDistance(getDungeon().getLocation(2949, 5910, 0), 1)) {
            player.addEvent(event -> {
                player.animate(828);
                event.delay(1);
            getDungeon().leave(player);
            finish();
            });
            return true;
        }
        return false;
    }

    private void finish() {
        removeControler();
    }

}
