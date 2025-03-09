package com.rs.game.map;

import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.game.world.entity.player.Player;

/**
 * @author ReverendDread
 * Created 4/12/2021 at 2:09 AM
 * @project 718---Server
 */
public interface ObjectAction {

    void handle(Player player, WorldObject object);

    static void register(int objectId, int option, ObjectAction action) {
        ObjectDefinitions def = ObjectDefinitions.getObjectDefinitions(objectId);
        if (def.actions == null)
            def.actions = new ObjectAction[5];
        def.actions[option - 1] = action;
    }

    static void register(WorldObject object, int option, ObjectAction action) {
        if(object.actions == null)
            object.actions = new ObjectAction[5];
        object.actions[option - 1] = action;
    }

    static boolean register(int objectId, String optionName, ObjectAction action) {
        int option = ObjectDefinitions.getObjectDefinitions(objectId).getOption(optionName);
        if (option == -1)
            return false;
        register(objectId, option, action);
        return true;
    }

}
