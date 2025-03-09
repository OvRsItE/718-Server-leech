package com.rs.game.world.entity.npc.instances.EliteDungeon;

import com.google.common.collect.Lists;
import com.rs.Constants;
import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.map.WorldObject;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.chest.RaidsRewards;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.player.content.skills.construction.House;
import com.rs.game.world.entity.player.controller.Controller;
import com.rs.game.world.entity.updating.impl.Animation;
import lombok.Getter;

import java.util.List;


public class EliteDungeonController extends Controller {

    private EliteDungeonOne getDungeon() {
        return player.getEliteDungeonOne();
    }

    @Override
    public void start() {

    }

    @Override
    public boolean processItemTeleport(Position destination) {
        player.sendMessage("You cannot teleport from elite dungeons.");
        return false;
    }

    @Override
    public boolean processObjectTeleport(Position destination) {
        player.sendMessage("You cannot teleport from elite dungeons.");
        return false;
    }

    @Override
    public boolean processMagicTeleport(Position destination) {
        player.sendMessage("You cannot teleport from elite dungeons.");
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
                    player.setNextPosition(getDungeon().getLocation(1227, 6308, 0));
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
        if (object.getId() == 230397 && player.getPosition().withinDistance(getDungeon().getLocation(1235, 6308, 0), 1)) {
            if (player.getPosition().withinDistance(getDungeon().getLocation(1234, 6308, 0), 1) || player.getPosition().withinDistance(getDungeon().getLocation(1234, 6309, 0), 1) ) {
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
                    player.setNextPosition(new Position(player.getPosition().getX() + 2, player.getPosition().getY(), 0));
                });
                return true;
            }
            else if (player.getPosition().withinDistance(getDungeon().getLocation(1236, 6308, 0), 1) || player.getPosition().withinDistance(getDungeon().getLocation(1237, 6309, 0), 1)) {
                player.addEvent(event -> {
                    player.animate(798);
                    event.delay(1);
                    player.setNextPosition(new Position(player.getPosition().getX() - 2, player.getPosition().getY(), 0));
                });
                return true;
            }
        }
        if (object.getId() == 230397 && player.getPosition().withinDistance(getDungeon().getLocation(1244, 6297, 0), 3)) {
            for (MinionNpc npc : getDungeon().getNpcs()) {
                if (!npc.isFinished()) {
                    player.sm("You need to deal with all the monsters to move on");
                    return true;
                }
            }
            if (player.getPosition().withinDistance(getDungeon().getLocation(1244, 6297, 0), 1) || player.getPosition().withinDistance(getDungeon().getLocation(1243, 6297, 0), 1)) {
                player.addEvent(event -> {
                    player.animate(798);
                    event.delay(1);
                    player.setNextPosition(new Position(player.getPosition().getX() , player.getPosition().getY() - 2, 0));
                });
                return true;
            } else if (player.getPosition().withinDistance(getDungeon().getLocation(1244, 6294, 0), 1) || player.getPosition().withinDistance(getDungeon().getLocation(1243, 6294, 0), 1)) {
                player.addEvent(event -> {
                    player.animate(798);
                    event.delay(1);
                    player.setNextPosition(new Position(player.getPosition().getX(), player.getPosition().getY() + 2, 0));
                });
                return true;
            }
        }
        if (object.getId() == 230397 && player.getPosition().withinDistance(getDungeon().getLocation(1254, 6311, 0), 3)) {
            if (!getDungeon().getVasa().isFinished()) {
                player.sm("You need to deal with all the monsters to move on");
                return true;
            }
            if (player.getPosition().withinDistance(getDungeon().getLocation(1254, 6311, 0), 1) || player.getPosition().withinDistance(getDungeon().getLocation(1254, 6310, 0), 1) || player.getPosition().withinDistance(getDungeon().getLocation(1254, 6309, 0), 1)) {
                player.addEvent(event -> {
                    player.animate(798);
                    event.delay(1);
                    player.setNextPosition(new Position(player.getPosition().getX() + 2, player.getPosition().getY(), 0));
                });
                return true;
            } else if (player.getPosition().withinDistance(getDungeon().getLocation(1256, 6311, 0), 1) || player.getPosition().withinDistance(getDungeon().getLocation(1257, 6310, 0), 1) || player.getPosition().withinDistance(getDungeon().getLocation(1257, 6309, 0), 1)) {
                player.addEvent(event -> {
                    player.animate(798);
                    event.delay(1);
                    player.setNextPosition(new Position(player.getPosition().getX() - 2, player.getPosition().getY(), 0));
                });
                return true;
            }
        }
        if (object.getId() == 230397 && player.getPosition().withinDistance(getDungeon().getLocation(1272, 6300, 0), 2)) {
            for (MinionNpc npc : getDungeon().getMinions_2nd_room()) {
                if (!npc.isFinished()) {
                    player.sm("You need to deal with all the monsters to move on");
                    return true;

                }

            }
            if (player.getPosition().withinDistance(getDungeon().getLocation(1272, 6300, 0), 1) || player.getPosition().withinDistance(getDungeon().getLocation(1271, 6300, 0), 1) || player.getPosition().withinDistance(getDungeon().getLocation(1270, 6300, 0), 1)) {
                player.addEvent(event -> {
                    player.animate(798);
                    event.delay(1);
                    player.setNextPosition(new Position(player.getPosition().getX() , player.getPosition().getY() - 2, 0));
                });
                return true;
            } else if (player.getPosition().withinDistance(getDungeon().getLocation(1272, 6298, 0), 1) || player.getPosition().withinDistance(getDungeon().getLocation(1271, 6298, 0), 1) || player.getPosition().withinDistance(getDungeon().getLocation(1270, 6298, 0), 1)) {
                player.addEvent(event -> {
                    player.animate(798);
                    event.delay(1);
                    player.setNextPosition(new Position(player.getPosition().getX() , player.getPosition().getY() + 2, 0));
                });
                return true;


            }
        }
        if (object.getId() == 232542 && player.getPosition().withinDistance(getDungeon().getLocation(1270, 6283, 0), 2)) {
            for (MinionNpc npc : getDungeon().getMinions_2nd_room()) {
                if (!npc.isFinished()) {
                    player.sm("You need to deal with all the monsters to move on");
                    return true;

                }

            }
            if (player.getPosition().withinDistance(getDungeon().getLocation(1270, 6283, 0), 1)) {
                player.setNextPosition(getDungeon().getLocation(1262, 6321, 1));
                return true;
            }
        }
        if (object.getId() == 229996 && player.getPosition().withinDistance(getDungeon().getLocation(1262, 6320, 1), 3)) {
            for (MinionNpc npc : getDungeon().getMinions_2nd_room()) {
                if (!npc.isFinished()) {
                    player.sm("You need to deal with all the monsters to move on");
                    return true;

                }

            }
            if (player.getPosition().withinDistance(getDungeon().getLocation(1262, 6320, 1), 2)) {
                player.setNextPosition(getDungeon().getLocation(1270, 6283, 0));
                return true;
            }
        }

        if (object.getId() == 230397 && player.getPosition().withinDistance(getDungeon().getLocation(1261, 6315, 1), 3)) {
            for (MinionNpc npc : getDungeon().getMinions_2nd_room()) {
                if (!npc.isFinished()) {
                    player.sm("You need to deal with all the monsters to move on");
                    return true;

                }

            }
            if (player.getPosition().withinDistance(getDungeon().getLocation(1261, 6315, 1), 1) || player.getPosition().withinDistance(getDungeon().getLocation(1262, 6315, 1), 1) || player.getPosition().withinDistance(getDungeon().getLocation(1263, 6315, 1), 1)) {
                player.addEvent(event -> {
                    player.animate(798);
                    event.delay(1);
                    player.setNextPosition(new Position(player.getPosition().getX() , player.getPosition().getY() - 2, 1));
                });
                return true;
            } else if (player.getPosition().withinDistance(getDungeon().getLocation(1261, 6312, 1), 1) || player.getPosition().withinDistance(getDungeon().getLocation(1262, 6312, 1), 1) || player.getPosition().withinDistance(getDungeon().getLocation(1262, 6312, 1), 1)) {
                player.addEvent(event -> {
                    player.animate(798);
                    event.delay(1);
                    player.setNextPosition(new Position(player.getPosition().getX() , player.getPosition().getY() + 2, 1));
                });
                return true;


            }
        }
        if (object.getId() == 230397 && player.getPosition().withinDistance(getDungeon().getLocation(1234, 6322, 1), 2)) {
            if (!getDungeon().getTekton().isFinished()) {
                player.sm("You need to deal with all the monsters to move on");
                return true;
            }
            if (player.getPosition().withinDistance(getDungeon().getLocation(1234, 6322, 1), 1) || player.getPosition().withinDistance(getDungeon().getLocation(1234, 6321, 1), 1) || player.getPosition().withinDistance(getDungeon().getLocation(1234, 6320, 1), 1)) {
                player.addEvent(event -> {
                    player.animate(798);
                    event.delay(1);
                    player.setNextPosition(new Position(player.getPosition().getX() - 2, player.getPosition().getY(), 1));
                });
            } else if (player.getPosition().withinDistance(getDungeon().getLocation(1231, 6322, 1), 1) || player.getPosition().withinDistance(getDungeon().getLocation(1231, 6321, 1), 1) || player.getPosition().withinDistance(getDungeon().getLocation(1231, 6320, 1), 1)) {
                player.addEvent(event -> {
                    player.animate(798);
                    event.delay(1);
                    player.setNextPosition(new Position(player.getPosition().getX() + 2, player.getPosition().getY(), 1));
                });
                return true;
            }
        }
        if (object.getId() == 230397 && player.getPosition().withinDistance(getDungeon().getLocation(1225, 6298, 1), 2)) {
            for (MinionNpc npc : getDungeon().getMinions_2nd_floor()) {
                if (!npc.isFinished()) {
                    player.sm("You need to deal with all the monsters to move on");
                    return true;

                }

            }
            if (player.getPosition().withinDistance(getDungeon().getLocation(1225, 6298, 1), 1) || player.getPosition().withinDistance(getDungeon().getLocation(1226, 6298, 1), 1)) {
                player.addEvent(event -> {
                    player.animate(798);
                    event.delay(1);
                    player.setNextPosition(new Position(player.getPosition().getX() , player.getPosition().getY() - 2, 1));
                });
                return true;
            } else if (player.getPosition().withinDistance(getDungeon().getLocation(1225, 6295, 1), 1) || player.getPosition().withinDistance(getDungeon().getLocation(1226, 6295, 1), 1)) {
                player.addEvent(event -> {
                    player.animate(798);
                    event.delay(1);
                    player.setNextPosition(new Position(player.getPosition().getX() , player.getPosition().getY() + 2, 1));
                });
                return true;


            }
        }
        if (object.getId() == 229996 && player.getPosition().withinDistance(getDungeon().getLocation(1219, 6294, 1), 2)) {
            if (!getDungeon().getOlm().isFinished()) {
                player.sm("You need to deal with olm to finish.");
                return true;
            }
            if (player.getPosition().withinDistance(getDungeon().getLocation(1219, 6294, 1), 1)) {
                player.animate(828);
                getDungeon().leave(player);
                RaidsRewards.open(player);
                player.getSkills().addXp(Skills.DUNGEONEERING, 2500);
                player.getDungeoneeringManager().addTokens(10000);
                player.getPackets().sendGameMessage("<col=ff0000> You have recieved dungeoneering xp and 10k dungeneering tokens for completeing the elite dungeon");
                finish();
                return true;
            }

        }
        if (object.getId() == 200195 && player.getPosition().withinDistance(getDungeon().getLocation(1219, 6308, 0), 1)) {
            player.animate(828);
            getDungeon().leave(player);
            finish();
            return true;
        }
        return false;
    }

    private void finish() {
        removeControler();
    }

}
