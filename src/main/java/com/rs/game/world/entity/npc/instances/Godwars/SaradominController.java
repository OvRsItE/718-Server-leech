package com.rs.game.world.entity.npc.instances.Godwars;

import com.rs.Constants;
import com.rs.game.map.Position;
import com.rs.game.map.WorldObject;
import com.rs.game.world.entity.player.controller.Controller;

public class SaradominController extends Controller {

    SaradominInstance instance;
    GodWarsNpc npc;

    @Override
    public void start() {
        instance = (SaradominInstance) getArguments()[0];
        npc = new GodWarsNpc(6247, instance.getLocation(2919, 5252, 0));
        npc = new GodWarsNpc(6248, instance.getLocation(2919, 5246, 0));
        npc = new GodWarsNpc(6250, instance.getLocation(2922, 5249, 0));
        npc = new GodWarsNpc(6252, instance.getLocation(2927, 5248, 0));
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
        if (object.getId() == 26427 && player.getPosition().withinDistance(instance.getLocation(2923, 5257, 0), 0)) {
            player.setNextPosition(new Position(instance.getLocation(2923, 5256, 0)));
            return true;
        }
        else if (object.getId() == 26427 && player.getPosition().withinDistance(instance.getLocation(2923, 5256, 0), 1)) {
            player.setNextPosition(new Position(instance.getLocation(2923, 5257, 0)));
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
