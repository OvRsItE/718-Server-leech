package com.rs.game.world.entity.npc.instances.hydra;

import com.rs.Constants;
import com.rs.game.map.Direction;
import com.rs.game.map.Position;
import com.rs.game.map.WorldObject;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.randomspawns.SuperiorBosses;
import com.rs.game.world.entity.player.controller.Controller;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceMovement;
import com.rs.utility.Utils;

/**
 * @author ReverendDread
 * Created 2/18/2021 at 3:32 PM
 * @project 718---Server
 */
public class HydraController extends Controller {

    HydraInstance instance;
    AlchemicalHydraNPC hydra;

    @Override
    public void start() {
        instance = (HydraInstance) getArguments()[0];
        hydra = new AlchemicalHydraNPC(NPC.asOSRS(8615), instance.getLocation(1364, 10265, 0));
    }

    @Override
    public boolean processItemTeleport(Position destination) {
        finish();
        return true;
    }

    @Override
    public boolean processObjectTeleport(Position destination) {
        finish();
        return true;
    }

    @Override
    public boolean processMagicTeleport(Position destination) {
        finish();
        return true;
    }

    @Override
    public boolean logout() {
        player.setNextPosition(Constants.START_PLAYER_LOCATION);
        finish();
        return true;
    }

    @Override
    public boolean login() {
        player.setNextPosition(Constants.START_PLAYER_LOCATION);
        return true;
    }

    @Override
    public boolean sendDeath() {
        finish();
        return true;
    }

    @Override
    public boolean processObjectClick1(WorldObject object) {
        if (WorldObject.asOSRS(34548) == object.getId()) {
            player.animate(new Animation(839));
            player.setNextForceMovement(new ForceMovement(player.relative(0, -2), 2, ForceMovement.SOUTH));
            player.addEvent(event -> {
                event.delay(1);
                player.setNextPosition(Position.of(1351, 10250, 0));
                player.getControlerManager().startControler("KaruulmController");
            });
            return true;
        }
        if (WorldObject.asOSRS(34554) == object.getId() || WorldObject.asOSRS(34553) == object.getId()) {
            boolean inside = instance.withinArea(player, new Position(1356, 10257), new Position(1377, 10278));
            if (inside) {
                player.sendMessage("You can't open the door from this side.");
            } else {
                player.setNextPosition(instance.getLocation(1356, 10259, player.getZ()));
            }
            return true;
        }
        return false;
    }

    @Override
    public void processNPCDeath(NPC npc) {
        if (npc.getId() == 16140) {
            hydra.setSpawned(false);
            hydra.setRespawnTask();
        }
        if (npc.getId() == 28622 && Utils.random(180) == 1 && player.UnlockedSuperiorBosses == 1) {
            World.sendWorldMessage("[<col=ff0000>Boss Spawn</col>] - <col=ff0000>" + Utils.formatPlayerNameForDisplay(player.getUsername()) +
                    "<col=ff0000> has spawned a superior hydra boss they have 5 minutes to kill it. <col=ff0000>", false);
            SuperiorBosses.SpawnHydra(player, npc);
            hydra.setSpawned(true);
        }
        super.processNPCDeath(npc);
    }

    private void finish() {
        instance.destroyRegion();
        player.setForceMultiArea(false);
        hydra.finish();
        instance = null;
        removeControler();
    }

}
