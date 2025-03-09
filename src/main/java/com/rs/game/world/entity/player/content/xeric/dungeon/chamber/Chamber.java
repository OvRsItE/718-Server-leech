package com.rs.game.world.entity.player.content.xeric.dungeon.chamber;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.game.item.Item;
import com.rs.game.map.*;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.xeric.dungeon.XericDungeon;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.rs.game.world.entity.player.content.xeric.dungeon.skilling.CoxHerblore.EMPTY_GOURD_VIAL;

/**
 * @author ReverendDread
 * Created 4/6/2021 at 11:34 PM
 * @project 718---Server
 */
@NoArgsConstructor @Setter @Getter
public abstract class Chamber {

    public static final int CHUNK_SIZE = 4;
    public static final int TILE_SIZE = CHUNK_SIZE * 8;

    private int basePointX, basePointY, pointZ;
    private int layout;
    private int rotation = 0;

    private Position basePosition;
    private DynamicChunk[][] chunks = new DynamicChunk[getChunkSize()][getChunkSize()];

    private ChamberDefinition definition;
    private XericDungeon dungeon;
    private Bounds bounds;

    public abstract void onRaidStart();

    public void onBuild() {}

    public Position getPosition(int localX, int localY) {
        if (basePosition == null)
            throw new IllegalStateException("Base position not set");
        return basePosition.relative(rotatedX(localX, localY),  rotatedY(localX, localY));
    }

    public Position getPosition(int[] localCoords) {
        return getPosition(localCoords[0], localCoords[1]);
    }

    public void setBasePosition(Position basePosition) {
        this.basePosition = basePosition;
        this.bounds = new Bounds(basePosition.getX(), basePosition.getY(), basePosition.getX() + TILE_SIZE, basePosition.getY() + TILE_SIZE, basePosition.getZ());
    }

    public void setPosition(int basePointX, int basePointY, int pointZ) {
        this.basePointX = basePointX;
        this.basePointY = basePointY;
        this.pointZ = pointZ;
        Chunk baseChunk = getDefinition().getBaseChunk(layout);
        for (int x = 0; x < getChunkSize(); x++) {
            for (int y = 0; y < getChunkSize(); y++) {
                chunks[x][y] = new DynamicChunk(baseChunk.getX() + x, baseChunk.getY() + y, baseChunk.getZ());
            }
        }
        rotate(rotation);
    }

    public Position getRespawnPosition() { return null; }

    public Position getEntrancePosition() { return null; }

    /**
     * Sets the chambers rotation and then updates the map.
     * @param rotation
     */
    private void rotate(int rotation) {
        setRotation(rotation);
        rotate();
    }

    /**
     * Rotates the room to the appropriate points.
     */
    private void rotate() {
        for (int x = 0; x < getChunkSize(); x++) {
            for (int y = 0; y < getChunkSize(); y++) {
                DynamicChunk chunk = chunks[x][y];
                chunk.pos(basePointX + rotatedX(getChunkSize(), x, y, 1, 1, 0),
                        basePointY + rotatedY(getChunkSize(), x, y, 1, 1, 0),
                        pointZ);
                chunk.rotate(rotation);
            }
        }
    }

    public int rotatedX(int areaSize, int localX, int localY, int xLength, int yLength, int direction) {
        if((direction & 0x1) == 1) {
            int oldXLength = xLength;
            xLength = yLength;
            yLength = oldXLength;
        }
        if(rotation == 0)
            return localX;
        if(rotation == 1)
            return localY;
        if(rotation == 2)
            return areaSize - 1 - localX - (xLength - 1);
        return areaSize - 1 - localY - (yLength - 1);
    }

    public int rotatedY(int areaSize, int localX, int localY, int xLength, int yLength, int direction) {
        if((direction & 0x1) == 1) {
            int oldXLength = xLength;
            xLength = yLength;
            yLength = oldXLength;
        }
        if(rotation == 0)
            return localY;
        if(rotation == 1)
            return areaSize - 1 - localX - (xLength - 1);
        if(rotation == 2)
            return areaSize - 1 - localY - (yLength - 1);
        return localX;
    }

    public int rotatedX(int localX, int localY) {
        return rotatedX(getTileSize(), localX, localY, 1, 1, 0);
    }

    public int rotatedY(int localX, int localY) {
        return rotatedY(getTileSize(), localX, localY, 1, 1, 0);
    }

    private static final Direction[] directions = {Direction.SOUTH, Direction.WEST, Direction.NORTH, Direction.EAST};

    public Direction rotatedDir(Direction dir, int rotation) {
        for (int i = 0; i < directions.length; i++) {
            if (directions[i] == dir) {
                return directions[(i + rotation) % directions.length];
            }
        }
        return dir;
    }

    public Direction rotatedDir(Direction dir) {
        return rotatedDir(dir, rotation);
    }

    /**
     * Gets the chunks making up this chamber.
     * @return
     */
    public List<DynamicChunk> getChunks() {
        ArrayList<DynamicChunk> list = new ArrayList<>(getChunkSize() * getChunkSize());
        for (DynamicChunk[] chunk : chunks) {
            list.addAll(Arrays.asList(chunk));
        }
        return list;
    }

    @Override
    public String toString() {
        return "[" + definition.getHandler().getSimpleName() + ", "
                + definition.getType() + "] ";
    }

    /**
     * Gets the width of the chamber in tiles.
     * @return
     */
    protected int getTileSize() {
        return TILE_SIZE;
    }

    /**
     * Gets the width of the chamber in chunks. A chunk is 8 tiles wide.
     * @return
     */
    protected int getChunkSize() {
        return CHUNK_SIZE;
    }

    protected NPC spawnNPC(int id, int[] coords, Direction direction, int walkRange) {
        return spawnNPC(id, coords[0], coords[1], direction, walkRange);
    }

    /**
     * Spawns an npc within the local area of this chamber.
     * @param id
     *          the npc id.
     * @param localX
     *          the local x coordinate.
     * @param localY
     *          the local y coordinate.
     * @param faceDirection
     *          the direction the npc is facing.
     * @param walkRange
     *          the walk range of the npc.
     */
    protected NPC spawnNPC(int id, int localX, int localY, Direction faceDirection, int walkRange) {
        NPCDefinitions def = NPCDefinitions.getNPCDefinitions(id);
        Position position = basePosition.relative(rotatedX(getTileSize(), localX, localY, def.size, def.size, 0), rotatedY(getTileSize(), localX, localY, def.size, def.size, 0));
        NPC npc = new NPC(id, position, -1, rotatedDir(faceDirection).ordinal(), true, true);
        npc.setWalkRange(walkRange);
        scaleNPC(npc);
        return npc;
    }

    /**
     * Scales the npcs stats based on the player count of the dungeon.
     * @param npc
     */
    protected void scaleNPC(NPC npc) {
        //TODO
    }

    public WorldObject getObject(int id, int localX, int localY, int rotation) {
        ObjectDefinitions def = ObjectDefinitions.getObjectDefinitions(id);
        Position position = basePosition.relative(
                rotatedX(getTileSize(), localX, localY, def.sizeX, def.sizeY, rotation),
                rotatedY(getTileSize(), localX, localY, def.sizeX, def.sizeY, rotation)
        );
        return World.getObject(position);
    }

    public WorldObject spawnObject(int id, int[] coords, int type, int direction) {
        return spawnObject(id, coords[0], coords[1], type, direction);
    }

    public WorldObject spawnObject(int id, int localX, int localY, int type, int direction) {
        ObjectDefinitions def = ObjectDefinitions.getObjectDefinitions(id);
        Position pos = basePosition.relative(
                rotatedX(getTileSize(), localX, localY, def.sizeX, def.sizeY, direction),
                rotatedY(getTileSize(), localX, localY, def.sizeX, def.sizeY, direction));
        direction = (direction + getRotation()) % 4; // rotate
        WorldObject obj = new WorldObject(id, type, direction, pos.getX(), pos.getY(), pos.getZ());
        World.spawnObject(obj);
        return obj;
    }

    static {
        ObjectAction.register(WorldObject.asOSRS(29771), 1, (player, obj) -> {
            player.getDialogueManager().startDialogue(new Dialogue() {

                @Override
                public void start() {
                    sendOptionsDialogue("Which tool would you like to take?", "Take Rake.", "Take Spade.", "Take Seed dibber.", "Take All", "Nevermind.");
                }

                @Override
                public void run(int interfaceId, int componentId) {
                    switch (componentId) {
                        case OPTION_1:
                            player.getInventory().addItem(5341, 1);
                            break;
                        case OPTION_2:
                            player.getInventory().addItem(952, 1);
                            break;
                        case OPTION_3:
                            player.getInventory().addItem(5343, 1);
                            break;
                        case OPTION_4:
                            if (player.getInventory().getFreeSlots() >= 3) {
                                player.getInventory().addItem(5341, 1);
                                player.getInventory().addItem(952, 1);
                                player.getInventory().addItem(5343, 1);
                                player.sendMessage("You 'borrow' a selection of tools, or is that 'lend'?");
                            } else {
                                player.sendMessage("You don't have enough inventory space.");
                            }
                            break;
                    }
                    end();
                }

                @Override
                public void finish() {

                }

            });
        });
        ObjectAction.register(WorldObject.asOSRS(29771), 2, (player, obj) -> {
            if (player.getInventory().hasFreeSlots()) {
                player.getInventory().addItem(5341, 1); //rake
            } else {
                player.sendMessage("You don't have enough inventory space to do that.");
            }
        });
        ObjectAction.register(WorldObject.asOSRS(29771), 3, (player, obj) -> {
            if (player.getInventory().hasFreeSlots()) {
                player.getInventory().addItem(952, 1); //spade
            } else {
                player.sendMessage("You don't have enough inventory space to do that.");
            }
        });
        ObjectAction.register(WorldObject.asOSRS(29771), 4, (player, obj) -> {
            if (player.getInventory().hasFreeSlots()) {
                player.getInventory().addItem(5343, 1); //seed dibber
            } else {
                player.sendMessage("You don't have enough inventory space to do that.");
            }
        });
        ObjectAction.register(WorldObject.asOSRS(29772), "pick", (player, obj) -> pickGourdTree(player, false));
        ObjectAction.register(WorldObject.asOSRS(29772), "pick-lots", (player, obj) -> pickGourdTree(player, true));
    }

    private static void pickGourdTree(Player player, boolean lots) {
        if (!player.getInventory().hasFreeSlots()) {
            player.getDialogueManager().startDialogue("SimpleMessage", "Your inventory is too full to do this.");
            return;
        }
        player.startEvent(event -> {
            player.lock();
            player.animate(2280);
            event.delay(2);
            player.getInventory().addItem(Item.asOSRS(EMPTY_GOURD_VIAL), lots ? player.getInventory().getFreeSlots() : 1);
            player.getPackets().sendGameMessage("You pick " + (lots ? "some" : "a") + " gourd fruit" + (lots ? "s" : "") + " from the tree, tearing the top" + (lots ? "s" : "") + " off in the process.");
            event.delay(1);
            player.unlock();
        });
    }

}
