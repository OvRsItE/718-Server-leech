package com.rs.game.world.entity.listeners;

import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.updating.impl.Hit;

/**
 * @author ReverendDread
 * Created 3/2/2021 at 9:50 AM
 * @project 718---Server
 */
public interface DeathListener {

    void handle(Entity entity, Entity killer);

    interface Simple extends DeathListener {
        default void handle(Entity entity, Entity killer) { handle(); }
        void handle();
    }

    interface SimpleKiller extends DeathListener {
        default void handle(Entity entity, Entity killer) { handle(killer); }
        void handle(Entity killer);
    }

}
