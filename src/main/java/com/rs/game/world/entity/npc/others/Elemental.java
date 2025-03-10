package com.rs.game.world.entity.npc.others;

import java.util.ArrayList;
import java.util.List;

import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.FadingScreen;
import com.rs.game.world.entity.player.controller.impl.SorceressGarden;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;

@SuppressWarnings("serial")
public class Elemental extends NPC {

    private boolean beingTeleported = false;

    private static final Position[][] tiles = { { new Position(2908, 5460, 0), new Position(2898, 5460, 0) }, { new Position(2900, 5448, 0), new Position(2900, 5455, 0) }, { new Position(2905, 5449, 0), new Position(2899, 5449, 0) }, { new Position(2903, 5451, 0), new Position(2903, 5455, 0), new Position(2905, 5455, 0), new Position(2905, 5451, 0) }, { new Position(2903, 5457, 0), new Position(2917, 5457, 0) }, { new Position(2908, 5455, 0), new Position(2917, 5455, 0) }, { new Position(2922, 5471, 0), new Position(2922, 5459, 0) }, { new Position(2924, 5463, 0), new Position(2928, 5463, 0), new Position(2928, 5461, 0), new Position(2924, 5461, 0) }, { new Position(2924, 5461, 0), new Position(2926, 5461, 0), new Position(2926, 5458, 0), new Position(2924, 5458, 0) }, { new Position(2928, 5458, 0), new Position(2928, 5460, 0), new Position(2934, 5460, 0), new Position(2934, 5458, 0) }, { new Position(2931, 5477, 0), new Position(2931, 5470, 0) }, { new Position(2935, 5469, 0), new Position(2928, 5469, 0) }, { new Position(2925, 5464, 0), new Position(2925, 5475, 0) }, { new Position(2931, 5477, 0), new Position(2931, 5470, 0) }, { new Position(2907, 5488, 0), new Position(2907, 5482, 0) }, { new Position(2907, 5490, 0), new Position(2907, 5495, 0) }, { new Position(2910, 5493, 0), new Position(2910, 5487, 0) }, { new Position(2918, 5483, 0), new Position(2918, 5485, 0), new Position(2915, 5485, 0), new Position(2915, 5483, 0), new Position(2912, 5483, 0), new Position(2912, 5485, 0), new Position(2915, 5485, 0), new Position(2915, 5483, 0) }, { new Position(2921, 5486, 0), new Position(2923, 5486, 0), new Position(2923, 5490, 0), new Position(2923, 5486, 0) }, { new Position(2921, 5491, 0), new Position(2923, 5491, 0), new Position(2923, 5495, 0), new Position(2921, 5495, 0) }, { new Position(2899, 5466, 0), new Position(2899, 5468, 0), new Position(2897, 5468, 0), new Position(2897, 5466, 0), new Position(2897, 5468, 0), new Position(2899, 5468, 0) }, { new Position(2897, 5470, 0), new Position(2891, 5470, 0) }, { new Position(2897, 5471, 0), new Position(2899, 5471, 0), new Position(2899, 5478, 0), new Position(2897, 5478, 0) }, { new Position(2896, 5483, 0), new Position(2900, 5483, 0), new Position(2900, 5480, 0), new Position(2897, 5480, 0), new Position(2896, 5482, 0) }, { new Position(2896, 5483, 0), new Position(2896, 5481, 0), new Position(2891, 5481, 0), new Position(2891, 5483, 0) }, { new Position(2889, 5485, 0), new Position(2900, 5485, 0) } };

    /**
     * 
     * @param id
     *            NPC id
     * @param tile
     *            WorldTile
     * @param mapAreaNameHash
     *            -1
     * @param canBeAttackFromOutOfArea
     *            true
     * @param spawned
     *            false
     */
    public Elemental(int id, Position tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
	super(id, tile, mapAreaNameHash, 0, canBeAttackFromOutOfArea, spawned);
	setCantFollowUnderCombat(true);
	setCantInteract(true);
    }

    @Override
    public ArrayList<Entity> getPossibleTargets() {
	ArrayList<Entity> possibleTarget = new ArrayList<Entity>();
	for (int regionId : getMapRegionsIds()) {
	    List<Integer> playerIndexes = World.getRegion(regionId).getPlayerIndexes();
	    if (playerIndexes != null) {
		for (int playerIndex : playerIndexes) {
		    Player player = World.getPlayers().get(playerIndex);
		    if (player == null || player.isDead() || player.getAppearence().isHidden() || player.isFinished() || player.isLocked() || !clipedProjectile(player, false))
			continue;
		    possibleTarget.add(player);
		}
	    }
	}
	return possibleTarget;
    }

    private int steps;

    @Override
    public void processNPC() {
	if (!beingTeleported) {
	    for (Entity t : getPossibleTargets()) {
		if (withinDistance(t, 2) && Utils.getFaceDirection(t.getX() - getX(), t.getY() - getY()) == getDirection()) {
		    final Player player = (Player) t;
		    animate(new Animation(5803));
		    player.setNextGraphics(new Graphics(110, 0, 100));
		    player.stopAll();
		    player.lock();
		    player.getPackets().sendGameMessage("You've been spotted by an elemental and teleported out of its garden.");
		    FadingScreen.fade(player, new Runnable() {
			@Override
			public void run() {
			    player.setNextPosition(SorceressGarden.inAutumnGarden(player) ? new Position(2913, 5467, 0) : (SorceressGarden.inSpringGarden(player) ? new Position(2916, 5473, 0) : (SorceressGarden.inSummerGarden(player) ? new Position(2910, 5476, 0) : new Position(2906, 5470, 0))));
			    player.lock(1);
			    beingTeleported = false;
			}
		    });
		    break;
		}
	    }
	}
	int index = getId() - 5533;
	if (!isForceWalking()) {
	    if (steps >= tiles[index].length)
		steps = 0;
	    setForceWalk(tiles[index][steps]);
	    if (withinDistance(tiles[index][steps], 0))
		steps++;
	}
	super.processNPC();
    }
}
