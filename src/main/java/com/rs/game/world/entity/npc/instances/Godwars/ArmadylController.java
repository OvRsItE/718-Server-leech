package com.rs.game.world.entity.npc.instances.Godwars;

import com.rs.Constants;
import com.rs.game.map.Position;
import com.rs.game.map.WorldObject;
import com.rs.game.world.entity.player.controller.Controller;

public class ArmadylController extends Controller {

    ArmadylInstance instance;
    GodWarsNpc npc;

    @Override
    public void start() {
        instance = (ArmadylInstance) getArguments()[0];
        npc = new GodWarsNpc(6222, instance.getLocation(2826, 5305, 0));
        npc = new GodWarsNpc(6227, instance.getLocation(2837, 5300, 0));
        npc = new GodWarsNpc(6225, instance.getLocation(2835, 5308, 0));
        npc = new GodWarsNpc(6223, instance.getLocation(2832, 5300, 0));
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
        if (object.getId() == 26426 && player.getPosition().withinDistance(instance.getLocation(2835, 5294, 0), 0)) {
            player.setNextPosition(new Position(instance.getLocation(2835, 5295, 0)));
            return true;
        }
        else if (object.getId() == 26426 && player.getPosition().withinDistance(instance.getLocation(2835, 5295, 0), 1)) {
            player.setNextPosition(new Position(instance.getLocation(2835, 5294, 0)));
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
