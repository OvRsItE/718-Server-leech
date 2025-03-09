package com.rs.game.world.entity.npc.instances.Corp;

import com.rs.Constants;
import com.rs.game.map.Position;
import com.rs.game.map.WorldObject;
import com.rs.game.world.entity.npc.instances.Godwars.ArmadylInstance;
import com.rs.game.world.entity.npc.instances.Godwars.GodWarsNpc;
import com.rs.game.world.entity.player.controller.Controller;

public class CorpController extends Controller {

    CorpInstance instance;
    CorpNpc npc;

    @Override
    public void start() {
        instance = (CorpInstance) getArguments()[0];
        npc = new CorpNpc(8133, instance.getLocation(2987, 4383, 2));
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
        if (object.getId() == 38811 && player.getPosition().withinDistance(instance.getLocation(2970, 4384, 2), 2)) {
            player.setNextPosition(new Position(instance.getLocation(2974, 4385, 2)));
            return true;
        }
        else if (object.getId() == 38811 && player.getPosition().withinDistance(instance.getLocation(2974, 4385, 2), 2)) {
            player.setNextPosition(new Position(instance.getLocation(2970, 4384, 2)));
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
