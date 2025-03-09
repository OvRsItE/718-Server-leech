package com.rs.game.world.entity.npc.instances.Godwars;

import com.rs.Constants;
import com.rs.game.map.Position;
import com.rs.game.map.WorldObject;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.instances.hydra.AlchemicalHydraNPC;
import com.rs.game.world.entity.npc.instances.hydra.HydraInstance;
import com.rs.game.world.entity.player.controller.Controller;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceMovement;

public class BandosController extends Controller {

    BandosInstance instance;
    GodWarsNpc npc;

    @Override
    public void start() {
        instance = (BandosInstance) getArguments()[0];
        npc = new GodWarsNpc(6260, instance.getLocation(2872, 5364, 0));
        npc = new GodWarsNpc(6263, instance.getLocation(2869, 5360, 0));
        npc = new GodWarsNpc(6265, instance.getLocation(2875, 5359, 0));
        npc = new GodWarsNpc(6261, instance.getLocation(2867, 5365, 0));
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
        if (object.getId() == 26425 && player.getPosition().withinDistance(instance.getLocation(2862, 5357, 0), 0)) {
            player.setNextPosition(new Position(instance.getLocation(2864, 5357, 0)));
            return true;
        }
        else if (object.getId() == 26425 && player.getPosition().withinDistance(instance.getLocation(2864, 5357, 0), 1)) {
            player.setNextPosition(new Position(instance.getLocation(2862, 5357, 0)));
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
