package com.rs.game.world.entity.player.content.interfaces.teleport;

import com.rs.game.map.Position;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Paolo, Discord Shnek#6969
 * 22/01/2019
 */
public class TeleportLocation implements Serializable {

    @Getter @Setter private String name;
    @Getter @Setter private Position teleTile;
    @Getter @Setter private String controller;

    public TeleportLocation(String name, Position tile, String controller){
        this.name = name;
        this.teleTile = tile;
        this.controller = controller;
    }

    public TeleportLocation(String name, Position tile){
             this(name,tile,null);
    }

    
}
