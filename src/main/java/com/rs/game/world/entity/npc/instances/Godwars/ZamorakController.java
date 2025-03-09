package com.rs.game.world.entity.npc.instances.Godwars;

import com.rs.Constants;
import com.rs.game.map.Position;
import com.rs.game.map.WorldObject;
import com.rs.game.world.entity.player.controller.Controller;

public class ZamorakController extends Controller {

    ZamorakInstance instance;
    GodWarsNpc npc;

    @Override
    public void start() {
        instance = (ZamorakInstance) getArguments()[0];
        npc = new GodWarsNpc(6203, instance.getLocation(2928, 5320, 0));
        npc = new GodWarsNpc(6204, instance.getLocation(2929, 5328, 0));
        npc = new GodWarsNpc(6206, instance.getLocation(2920, 5327, 0));
        npc = new GodWarsNpc(6208, instance.getLocation(2921, 5321, 0));
        player.setForceMultiArea(true);
        player.setAtMultiArea(true);
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
        if (object.getId() == 26428 && player.getPosition().withinDistance(instance.getLocation(2925, 5333, 0), 0)) {
            player.setNextPosition(new Position(instance.getLocation(2925, 5332, 0)));
            return true;
        }
        else if (object.getId() == 26428 && player.getPosition().withinDistance(instance.getLocation(2925, 5332, 0), 1)) {
            player.setNextPosition(new Position(instance.getLocation(2925, 5333, 0)));
            return true;
        }
        return false;
    }

    private void finish() {
        instance.destroyRegion();
        player.setForceMultiArea(false);
        npc.finish();
        instance = null;
        removeControler();
    }

}
