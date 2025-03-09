package com.rs.game.world.entity.player.actions.objects;

import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.Action;
import com.rs.game.world.entity.player.content.skills.construction.HouseConstants;
import com.rs.game.world.entity.updating.impl.Animation;

public class SitChair extends Action {

    private int chair;
    private Position originalTile;
    private Position chairTile;
    private boolean tped;
    public SitChair(Player player, int chair, WorldObject object) {
	this.chair = chair;
	this.originalTile = new Position(player);
	chairTile = object;
	 Position face = new Position(player);
	    if(object.getType() == 10) {
		if(object.getRotation() == 0)
		    face.moveLocation(0, -1, 0);
		else if(object.getRotation() == 2)
		    face.moveLocation(0, 1, 0);
	    }else if(object.getType() == 11) {
		if(object.getRotation() == 1)
		    face.moveLocation(-1, 1, 0);
		else if(object.getRotation() == 2)
		    face.moveLocation(1, 1, 0);
	    }
	    player.setNextFacePosition(face);
    }
    @Override
    public boolean start(Player player) {
	setActionDelay(player, 1);
	return true;
    }

    @Override
    public boolean process(Player player) {
	return true;
    }

    @Override
    public int processWithDelay(Player player) {
	if(!tped) {
	    player.setNextPosition(chairTile);
	    tped = true;
	}
	player.animate(new Animation(HouseConstants.CHAIR_EMOTES[chair]));
	return 0;
    }

    @Override
    public void stop(final Player player) {
	player.lock(1);
	player.setNextPosition(originalTile);
	player.animate(new Animation(-1));
    }
}
