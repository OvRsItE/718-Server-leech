package com.rs.game.world.entity.player.content.xeric.dungeon.chamber;

import com.rs.game.world.entity.player.content.xeric.dungeon.chamber.handler.*;
import lombok.Getter;

/**
 * @author ReverendDread
 * Created 4/5/2021 at 10:44 AM
 * @project 718---Server
 */
@Getter
public enum ChamberDefinition {

    START("Starting", ChamberType.BEGIN, CheckpointChamber.class, new Chunk(408, 648, 0)),
    SCAVENGERS("Scavengers", ChamberType.SCAVENGER, ScavengerChamber.class, new Chunk(408, 652, 0)),
    LIZARDMAN_SHAMAN("Lizardman Shamans", ChamberType.COMBAT, LizardmanShamanChamber.class, new Chunk(408, 656, 0)),
    VASA_NISTIRIO("Vasa Nistirio", ChamberType.COMBAT, VasaNistirioChamber.class, new Chunk(408, 660, 0)),
    VANGUARDS("Vanguards", ChamberType.COMBAT, VanguardsChamber.class, new Chunk(408, 664, 0)),
    //ICE_DEMON("Ice Demon", ChamberType.PUZZLE, IceDemonChamber.class, new Chunk(408, 668, 0)),
    CORRUPT_SCAVENGER("Corrupt Scavenger", ChamberType.PUZZLE, CorruptScavengerChamber.class, new Chunk(408, 672, 0)),
    SCAVENGER_RUINS("Scavenger Ruins", ChamberType.SCAVENGER, ScavengerRuinsChamber.class, new Chunk(408, 652, 1)),
    SKELETAL_MYSTICS("Skeletal Mystics", ChamberType.COMBAT, SkeletalMysticsChamber.class, new Chunk(408, 656, 1)),
    TEKTON("Tekton", ChamberType.COMBAT, TektonChamber.class, new Chunk(408, 660, 1)),
    MUTTADILES("Muttadiles", ChamberType.COMBAT, MuttadilesChamber.class, new Chunk(408, 664, 1)),
    TIGHTROPE("Tightrope", ChamberType.PUZZLE, TightRopeChamber.class, new Chunk(408, 668, 1)),
    GUARDIANS("Guardians", ChamberType.COMBAT, GuardiansChamber.class, new Chunk(408, 656, 2)),
    //VESPULA("Vespula", ChamberType.COMBAT, VespulaChamber.class, new Chunk(408, 660, 2)),
    //JEWELLED_CRABS("Jewelled crabs", ChamberType.PUZZLE, JewelledCrabsChamber.class, new Chunk(408, 668, 2)),
    FISHING("Resources (fishing)", ChamberType.RESOURCE, FishingChamber.class, new Chunk(408, 680, 0)),
    HUNTER("Resources (hunter)", ChamberType.RESOURCE, HunterChamber.class, new Chunk(408, 680, 1)),
    LOWER_LEVEL_START("Lower level start", ChamberType.BEGIN, CheckpointChamber.class, new Chunk(408, 712, 0)),
    UPPER_FLOOR_FINISH("Upper level finish", ChamberType.FINISH, CheckpointChamber.class, new Chunk[] { new Chunk(408, 644, 0) }),
    LOWER_FLOOR_FINISH("Lower level finish", ChamberType.FINISH, CheckpointChamber.class, new Chunk[] { new Chunk(412, 644, 0) }),
    GREAT_OLM("Great Olm", null, GreatOlmChamber.class, new Chunk[] { new Chunk(400, 712, 0) });

    public static final ChamberDefinition[] VALUES = values();

    /**
     * The name of the chamber used for layout preview.
     */
    private final String name;

    /**
     * The chamber type.
     */
    private final ChamberType type;

    /**
     * The chamber handler for the chamber.
     */
    private final Class<? extends Chamber> handler;

    /**
     * The chunks for the chamber.
     */
    private final Chunk[] chunks;

    /**
     *
     * @param type
     *      the chamber type.
     * @param handler
     *      the chamber handler.
     * @param chunk
     *      the chamber chunk.
     */
    ChamberDefinition(String name, ChamberType type, Class<? extends Chamber> handler, Chunk chunk) {
        this.name = name;
        this.type = type;
        this.handler = handler;
        this.chunks = new Chunk[3];
        this.chunks[0] = chunk;
        this.chunks[1] = new Chunk(chunk.getX() + 4, chunk.getY(), chunk.getZ());
        this.chunks[2] = new Chunk(chunk.getX() + 8, chunk.getY(), chunk.getZ());
    }

    /**
     *
     * @param type
     *      the chamber type.
     * @param handler
     *      the chamber handler.
     * @param chunks
     *      the chamber chunks.
     */
    ChamberDefinition(String name, ChamberType type, Class<? extends Chamber> handler, Chunk[] chunks) {
        this.name = name;
        this.type = type;
        this.handler = handler;
        this.chunks = chunks;
    }

    /**
     * Creates a new chamber instance.
     * @return
     */
    public Chamber newChamber() {
        try {
            Chamber chamber = handler.newInstance();
            chamber.setDefinition(this);
            return chamber;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets the base chunk in the desired layout.
     * @param layout
     * @return
     */
    public Chunk getBaseChunk(int layout) {
        if (layout < 0 || layout >= chunks.length)
            throw new IllegalArgumentException();
        return chunks[layout];
    }

}
