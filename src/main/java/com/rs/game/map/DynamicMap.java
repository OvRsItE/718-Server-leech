package com.rs.game.map;

import com.rs.game.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DynamicMap {

    public DynamicRegion sw, se, nw, ne;

    /**
     * Fixed size is 2x2 regions
     */
    public DynamicMap() {
        int reserved = RegionBuilder.findEmptyRegionId(2 * 8, 2 * 8);
        sw = RegionBuilder.createDynamicRegion(reserved);
        se = RegionBuilder.createDynamicRegion(reserved + 256);
        ne = RegionBuilder.createDynamicRegion(reserved + 257);
        nw = RegionBuilder.createDynamicRegion(reserved + 1);
    }

    public DynamicMap build(List<DynamicChunk> chunks) {

        List<DynamicChunk> swChunks = chunks.stream().filter(c -> c.pointX <= 7 && c.pointY <= 7).collect(Collectors.toList());
        if (!swChunks.isEmpty())
            build(sw, swChunks);

        List<DynamicChunk> seChunks = chunks.stream().filter(c -> c.pointX > 7 && c.pointY <= 7).collect(Collectors.toList());
        seChunks.forEach(c -> c.pointX -= 8);
        if (!seChunks.isEmpty())
            buildSe(seChunks);

        List<DynamicChunk> nwChunks = chunks.stream().filter(c -> c.pointX <= 7 && c.pointY > 7).collect(Collectors.toList());
        nwChunks.forEach(c -> c.pointY -= 8);
        if (!nwChunks.isEmpty())
            buildNw(nwChunks);

        List<DynamicChunk> neChunks = chunks.stream().filter(c -> c.pointX > 7 && c.pointY > 7).collect(Collectors.toList());
        neChunks.forEach(c -> {
            c.pointX -= 8;
            c.pointY -= 8;
        });
        if (!neChunks.isEmpty())
            buildNe(chunks);
        return this;
    }

    public DynamicMap buildSe(List<DynamicChunk> chunks) {
        build(se, chunks);
        return this;
    }

    public DynamicMap buildNw(List<DynamicChunk> chunks) {
        build(nw, chunks);
        return this;
    }

    public DynamicMap buildNe(List<DynamicChunk> chunks) {
        build(ne, chunks);
        return this;
    }

    /**
     * Build by region
     */

    public DynamicMap build(int regionId, int maxHeight) {
        return buildSw(regionId, maxHeight);
    }

    public DynamicMap buildSw(int regionId, int maxHeight) {
        build(sw, regionId, maxHeight);
        return this;
    }

    public DynamicMap buildNw(int regionId, int maxHeight) {
        build(nw, regionId, maxHeight);
        return this;
    }

    public DynamicMap buildSe(int regionId, int maxHeight) {
        build(se, regionId, maxHeight);
        return this;
    }

    public DynamicMap buildNe(int regionId, int maxHeight) {
        build(ne, regionId, maxHeight);
        return this;
    }

    private void build(DynamicRegion targetRegion, int regionId, int maxHeight) {
        DynamicRegion sourceRegion = RegionBuilder.createDynamicRegion(regionId);
        List<DynamicChunk> chunks = new ArrayList<>(64 * (maxHeight + 1));
        for (int pointZ = 0; pointZ <= maxHeight; pointZ++) {
            for (int pointX = 0; pointX < 8; pointX++) {
                int chunkX = (sourceRegion.getRegionMap().getRegionX() + (pointX * 8)) >> 3;
                for (int pointY = 0; pointY < 8; pointY++) {
                    int chunkY = (sourceRegion.getRegionMap().getRegionY() + (pointY * 8)) >> 3;
                    chunks.add(new DynamicChunk(chunkX, chunkY, pointZ).pos(pointX, pointY, pointZ));
                }
            }
        }
        build(targetRegion, chunks);
    }

    private void build(DynamicRegion targetRegion, List<DynamicChunk> chunks) {
        for (DynamicChunk chunk : chunks) {
            targetRegion.getRegionCoords()[chunk.pointZ][chunk.pointX][chunk.pointY][0] = chunk.x;
            targetRegion.getRegionCoords()[chunk.pointZ][chunk.pointX][chunk.pointY][1] = chunk.y;
            targetRegion.getRegionCoords()[chunk.pointZ][chunk.pointX][chunk.pointY][2] = chunk.z;
            targetRegion.getRegionCoords()[chunk.pointZ][chunk.pointX][chunk.pointY][3] = chunk.rotation;
            targetRegion.setReloadObjects(chunk.pointZ, chunk.pointX, chunk.pointY);
        }
    }

    /**
     * Build by bounds (May not be precise if bounds coordinates don't match absolute chunk coordinates)
     */

    public DynamicMap build(Bounds bounds) {
        int xChunks = (int) (Math.ceil((bounds.neX - bounds.swX) / 8) + 1);
        int yChunks = (int) Math.ceil((bounds.neY - bounds.swY) / 8) + 1;
        if (xChunks > 16 || yChunks > 16) {
            throw new IllegalArgumentException("Dynamic regions cannot exceed 2x2 region dimensions");
        }
        List<DynamicChunk> chunks = new ArrayList<>();
        for(int pointZ = 0; pointZ < 4; pointZ++) {
            if(bounds.z != -1 && pointZ != bounds.z) //ignore this height
                continue;
            for(int pointX = 0; pointX < xChunks; pointX++) {
                int chunkX = (bounds.swX + (pointX * 8)) >> 3;
                for(int pointY = 0; pointY < yChunks; pointY++) {
                    int chunkY = (bounds.swY + (pointY * 8)) >> 3;
                    chunks.add(new DynamicChunk(chunkX, chunkY, pointZ).pos(pointX, pointY, pointZ));
                }
            }
        }
        return build(chunks);
    }

    public void destroy() {
        World.startEvent(event -> {
            event.delay(10); //wait 10 ticks for players to be moved out, etc.
            sw.destroy();
            se.destroy();
            ne.destroy();
            nw.destroy();
        });
    }

}
