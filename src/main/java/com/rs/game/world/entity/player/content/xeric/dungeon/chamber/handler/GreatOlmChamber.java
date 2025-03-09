package com.rs.game.world.entity.player.content.xeric.dungeon.chamber.handler;

import com.rs.game.map.DynamicChunk;
import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.xeric.dungeon.XericDungeon;
import com.rs.game.world.entity.player.content.xeric.dungeon.chamber.Chamber;
import com.rs.game.world.entity.player.content.xeric.dungeon.chamber.Chunk;
import com.rs.game.world.entity.player.content.xeric.dungeon.chamber.ChamberDefinition;

import java.util.LinkedList;
import java.util.List;

/**
 * @author ReverendDread
 * Created 4/7/2021 at 12:01 AM
 * @project 718---Server
 */
public class GreatOlmChamber extends Chamber {

    private static final Chunk firstChunk = ChamberDefinition.GREAT_OLM.getBaseChunk(0);
    private static List<DynamicChunk> chunks = null;

    public GreatOlmChamber(XericDungeon dungeon) {
        setDefinition(ChamberDefinition.GREAT_OLM);
        setRotation(0);
        setLayout(0);
        setPosition(0, 0,0);
        setDungeon(dungeon);
        setBasePosition(Position.of(dungeon.getMap().sw.getBaseX(), dungeon.getMap().sw.getBaseY(), 0));
    }

    @Override
    public void onRaidStart() {

    }

    @Override
    public List<DynamicChunk> getChunks() {
        //olm's room has no variations, so override this to use a lazy init singleton pattern
        if (chunks != null)
            return chunks;
        LinkedList<DynamicChunk> list = new LinkedList<>();
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                list.add(new DynamicChunk(firstChunk.getX() + x, firstChunk.getY() + y, 0).pos(x, y, 0));
            }
        }
        chunks = list;
        return list;
    }

    @Override
    protected int getTileSize() {
        return 64;
    }

    @Override
    protected int getChunkSize() {
        return 8;
    }

}
