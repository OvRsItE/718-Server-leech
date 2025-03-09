package com.rs.game.world.entity.player.content.xeric.dungeon.chamber.handler;

import com.rs.game.map.Direction;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.content.Hunter;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.player.content.xeric.dungeon.chamber.Chamber;

/**
 * @author ReverendDread
 * Created 4/6/2021 at 11:59 PM
 * @project 718---Server
 */
public class HunterChamber extends Chamber {

    private static final int[][][] batSpawns = {
            {
                    {18, 21},
                    {22, 23},
                    {23, 19},
                    {23, 18},
            },
            {
                    {13, 14},
                    {17, 16},
                    {18, 14},
                    {18, 15},
            },
            {
                    {17, 20},
                    {18, 16},
                    {20, 18},
                    {20, 19},
            }
    };

    @Override
    public void onRaidStart() {
        Hunter.XericBat type = getBaseBatType();
        for (int i = 0; i < batSpawns[getLayout()].length; i++) {
            NPC npc = spawnNPC(NPC.asOSRS(type.getNpcId()), batSpawns[getLayout()][i][0], batSpawns[getLayout()][i][1], Direction.SOUTH, 5);
            npc.setClickOptionListener(1, type::net);
            if (i >= 2 && type != Hunter.XericBat.GUANIC)
                type = Hunter.XericBat.VALUES[type.ordinal() - 1];
        }
    }

    private Hunter.XericBat getBaseBatType() {
        int level = getDungeon().getGroup().getMembers().stream().mapToInt(m -> m.getSkills().getLevel(Skills.HUNTER)).max().orElse(1);
        Hunter.XericBat[] values = Hunter.XericBat.VALUES;
        for (int i = values.length - 1; i >= 0; i--) {
            Hunter.XericBat type = values[i];
            if (level >= type.getLevelReq()) {
                return type;
            }
        }
        return Hunter.XericBat.GUANIC;
    }

}
