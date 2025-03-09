package com.rs.game.world.entity.player.content.xeric.dungeon.chamber.handler;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.xeric.dungeon.chamber.Chamber;

/**
 * @author ReverendDread
 * Created 4/6/2021 at 11:38 PM
 * @project 718---Server
 */
public class CheckpointChamber extends Chamber {

    private static final int[][] START_POSITION = { // starting chamber, entrance position
            {14, 3},
            {2, 4},
            {2, 4},
    };

    private static final int[] UPPER_FINISH_ENTRANCE_POSITION = {
            14, 16,
    };

    private static final int[] LOWER_START_ENTRANCE_POSITION = {
            15, 15
    };

    private static final int[] LOWER_FINISH_ENTRANCE_POSITION = {
            12, 21,
    };

    private static final int[][] START_RESPAWN = { // starting chamber, respawn
            {14, 12},
            {11, 13},
            {13, 13},
    };

    private static final int[] LOWER_RESPAWN = {
            5, 11
    };

    @Override
    public void onRaidStart() {

    }

    @Override
    public Position getRespawnPosition() {
        switch (getDefinition()) {
            case START:
                return getPosition(START_POSITION[getLayout()]);
            case LOWER_LEVEL_START:
                return getPosition(LOWER_RESPAWN);
        }
        return null;
    }

    @Override
    public Position getEntrancePosition() {
        switch (getDefinition()) {
            case START:
                return getPosition(START_RESPAWN[getLayout()]);
            case UPPER_FLOOR_FINISH:
                return getPosition(UPPER_FINISH_ENTRANCE_POSITION);
            case LOWER_LEVEL_START:
                return getPosition(LOWER_START_ENTRANCE_POSITION);
            case LOWER_FLOOR_FINISH:
                return getPosition(LOWER_FINISH_ENTRANCE_POSITION);
        }
        return null;
    }

}
