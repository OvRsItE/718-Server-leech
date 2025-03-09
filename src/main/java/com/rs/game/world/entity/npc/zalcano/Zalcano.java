package com.rs.game.world.entity.npc.zalcano;

import com.google.common.collect.Lists;
import com.rs.cores.coroutines.CoroutineEvent;
import com.rs.game.map.Position;
import com.rs.game.map.WorldObject;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.activities.zalcano.ZalcanoGame;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.utility.Misc;
import kilim.Pausable;

import java.util.Arrays;
import java.util.List;

/**
 * @author ReverendDread
 * Created 3/12/2021 at 6:37 AM
 * @project 718---Server
 */
public class Zalcano extends NPC {

    private static final int GLOWING = 36192, NORMAL = 36193, DEPLETED = 36194;
    private static final int ORANGE_SYMBOL = 36199, BLUE_SYMBOL = 36200; //27 tick duration
    private static final int ATTACKABLE = 9049, MINABLE = 9050;
    private static final int ATTACK_SPEED = 5;

    /** Rock formation locations */
    private static final Position[] FORMATIONS = {
            new Position(3040, 6057),
            new Position(3040, 6040),
            new Position(3025, 6057),
            new Position(3025, 6040),
    };

    /** Symbol spawn locations */
    private static final Position[] SYMBOL_SPOTS = {
            new Position(3035, 6055),
            new Position(3035, 6050),
            new Position(3035, 6045),
            new Position(3035, 6040),

            new Position(3030, 6055),
            new Position(3030, 6050),
            new Position(3030, 6045),
            new Position(3030, 6040),

            new Position(3038, 6053),
            new Position(3027, 6043),
            new Position(3027, 6053),
            new Position(3038, 6043),
    };

    /** List of symbols that are spawned */
    private List<WorldObject> symbols = Lists.newArrayList();

    public Zalcano(int id, Position position, int mapAreaNameHash, int faceDirection, boolean canBeAttackFromOutOfArea, boolean spawned) {
        super(id, position, mapAreaNameHash, faceDirection, canBeAttackFromOutOfArea, spawned);
        setCanRetaliate(false);
        setCantFollowUnderCombat(true);
        setClickOptionListener(1, ZalcanoGame::handleZalcanoClick);
        startEvent(this::symbols);
        setCanBeFrozen(false);
    }

    private void symbols(CoroutineEvent event) throws Pausable {
        while (true) {
            event.delay(25);
            animate(Animation.createOSRS(8433));
            Arrays.stream(SYMBOL_SPOTS).forEach(pos -> {
                WorldObject symbol = new WorldObject(WorldObject.asOSRS(Misc.random(3) == 1 ? BLUE_SYMBOL : ORANGE_SYMBOL), 10, 0, pos);
                symbols.add(symbol);
                World.addObject(symbol);
            });
            for (int tick = 0; tick < 25; tick++) {
                getPossibleTargets().forEach(player -> {
                    symbols.forEach(symbol -> {
                        if (symbol.getId() == WorldObject.asOSRS(ORANGE_SYMBOL) && player.inBounds(symbol.getBounds())) {
                            player.applyHit(new Hit(this).look(Hit.HitLook.REGULAR_DAMAGE).damage(3));
                            if (player.isPlayer())
                                player.getAsPlayer().setRunEnergy(player.getAsPlayer().getRunEnergy() > 3 ? player.getAsPlayer().getRunEnergy() - 3 : player.getAsPlayer().getRunEnergy());
                        }
                    });
                });
                event.delay(1);
            }
            symbols.forEach(World::removeObject);
        }
    }

}
