package com.rs.game.world.entity.player.content.xeric.dungeon;

import com.google.common.collect.Lists;
import com.rs.game.map.*;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.FadingScreen;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.xeric.ChambersController;
import com.rs.game.world.entity.player.content.xeric.ChambersOfXeric;
import com.rs.game.world.entity.player.content.xeric.dungeon.chamber.ChamberDefinition;
import com.rs.game.world.entity.player.content.xeric.dungeon.chamber.Chamber;
import com.rs.game.world.entity.player.content.xeric.dungeon.chamber.ChamberType;
import com.rs.game.world.entity.player.content.xeric.dungeon.chamber.handler.CheckpointChamber;
import com.rs.game.world.entity.player.content.xeric.dungeon.chamber.handler.GreatOlmChamber;
import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

import static com.rs.game.world.entity.player.content.xeric.dungeon.chamber.ChamberDefinition.*;

/**
 * Represents a Chambers of Xeric dungeon.
 *
 * TODO raids storage
 * TODO puzzle rooms
 * TODO combat rooms
 * TODO resource rooms
 * TODO object handling
 * TODO points system
 *
 * @author ReverendDread
 * Created 4/5/2021 at 7:11 AM
 * @project 718---Server
 */
@Getter
public class XericDungeon {

    private static final Position OUTSIDE = Position.of(1234, 3572, 0);
    private static final ChamberDefinition[] COMBAT_ROOMS = { /* VESPULA */ TEKTON, VASA_NISTIRIO, GUARDIANS, SKELETAL_MYSTICS, LIZARDMAN_SHAMAN, MUTTADILES, VANGUARDS}; // order is important here!!
    private static final int X_LENGTH = 4;
    private static final int Y_LENGTH = 2;
    private static final double SIXTH_ROOM_CHANCE = 0.05;
    private static final double FIVE_COMBAT_ROOMS_CHANCE = 0.08;
    private static final double THREE_PUZZLE_ROOM_CHANCE = 0.05;

    /**
     * The group associated with this dungeon instance.
     */
    @Getter private final ChambersOfXeric group;

    /**
     * The players that are in the dungeon.
     */
    private final List<Player> players = Lists.newArrayList();

    /**
     * The reserved map chunks.
     */
    private DynamicMap map;

    /**
     * The random generator used for rng layouts.
     */
    private final Random generator;

    /**
     * The unique seed for generation.
     */
    private long seed;

    private static Chamber[][][] chambers = new Chamber[2][4][2]; // zxy; 2 floors here since the final one is fixed
    private static Chamber[] layout = new Chamber[30];

    /**
     * The 1st floor starting chamber.
     */
    private CheckpointChamber startingChamber;

    /**
     * The 1st floor finish chamber.
     */
    private CheckpointChamber upperFinishChamber;

    /**
     * The 2nd floor starting chamber.
     */
    private CheckpointChamber lowerStartingChamber;

    /**
     * The 2nd floor finish chamber.
     */
    private CheckpointChamber lowerFinishChamber;

    /**
     * The great olm chamber.
     */
    private GreatOlmChamber olmChamber;

    /**
     * The instant that the dungeon has started.
     */
    private Instant startTime;

    /**
     * The checkpoint the dungeon is on.
     */
    public int checkpoint;

    /**
     *
     */
    public int points;

    /**
     * Creates a new xeric dungeon instance with a random seed.
     */
    public XericDungeon(ChambersOfXeric group) {
        this.seed = System.nanoTime();
        this.generator = new Random(seed);
        this.group = group;
    }

    /**
     * Marks the dungeon as begun, so we can enter the first room.
     */
    public void start() {
        //Mark the raid as started
        getGroup().setAttribute(ChambersOfXeric.STARTED, true);
        startTime = Instant.now();
        checkpoint = 1;
        System.out.println("dungeon pre-start");
        for (int z = 2; z > 0; z--) {
            for (int x = 0; x < X_LENGTH; x++) {
                for (int y = 0; y < Y_LENGTH; y++) {
                    Chamber chamber = chambers[z - 1][x][y];
                    if (chamber != null)
                        chamber.onRaidStart();
                }
            }
        }
        System.out.println("dungeon post-start");
        //TODO send points
    }

    /**
     * Generates the dungeon layout, and marks the dungeon as generated.
     */
    public void generate() {
        int layoutCount = 0;
        LinkedList<ChamberDefinition> puzzles = new LinkedList<>();
        Arrays.stream(values()).filter(def -> def.getType() == ChamberType.PUZZLE).forEach(puzzles::add);
        Collections.shuffle(puzzles);
        int combatIndex = generator.nextInt(COMBAT_ROOMS.length); // first boss
        int combatIndexDirection = generator.nextDouble() < 0.5 ? -1 : 1; // clockwise or counter clockwise?
        int budget = 8;
        int combatBudget = getCombatBudget(budget);
        budget -= combatBudget;
        int puzzleBudget = getPuzzleBudget(budget);
        budget -= puzzleBudget;
        int scavengerBudget = budget;
        for (int floorNumber = 0; floorNumber < 2; floorNumber++) {
            int forcedResourceRoom;
            if (floorNumber == 0) {
                forcedResourceRoom = 1 + generator.nextInt(4);
            } else {
                forcedResourceRoom = 4 + generator.nextInt(1);
            }
            Chamber[][] floor = chambers[floorNumber];
            //pick a starting place
            int x = generator.nextInt(floor.length);
            int y = generator.nextInt(floor[x].length);
            Chamber start = floorNumber == 1 ? START.newChamber() : LOWER_LEVEL_START.newChamber();
            floor[x][y] = start;
            if (floorNumber == 1)
                startingChamber = (CheckpointChamber) floor[x][y];
            else
                lowerStartingChamber = (CheckpointChamber) floor[x][y];
            boolean allowVertical = false;
            Direction previousMove = Direction.NORTH; // this is fixed
            Direction nextMove = getNextMove(floorNumber, x, y, previousMove, allowVertical, true);
            layout[layoutCount++] = start;
            int randomRooms = 0;
            while (true) { // add random rooms
                int rotation = getRotation(previousMove);
                floor[x][y].setRotation(rotation);
                floor[x][y].setLayout(getLayout(rotation, nextMove)); // set layout/rotation for previous room
                if (nextMove == null) {
                    chambers = new Chamber[2][4][2];
                    layout = new Chamber[30];
                    generate();
                    return;
                }
                x += nextMove.deltaX;
                y += nextMove.deltaY;
                if (x == 0 || x == X_LENGTH - 1)
                    allowVertical = true;
                previousMove = nextMove;
                randomRooms++;
                nextMove = getNextMove(floorNumber, x, y, previousMove, allowVertical, false);
                if (nextMove == null || randomRooms == 6 && generator.nextDouble() > SIXTH_ROOM_CHANCE) { // no moves left, or made 5 rooms and failed roll for 6th
                    if (floorNumber == 1) {
                        upperFinishChamber = (CheckpointChamber) (floor[x][y] = UPPER_FLOOR_FINISH.newChamber());
                    } else {
                        lowerFinishChamber = (CheckpointChamber) (floor[x][y] = LOWER_FLOOR_FINISH.newChamber());
                    }
                    break;
                } else {
                    if (randomRooms == forcedResourceRoom) {
                        floor[x][y] = getResourceChamber();
                        layout[layoutCount++] = floor[x][y];
                    } else if (floorNumber == 1 && scavengerBudget > 0 && generator.nextDouble() < 0.9) {
                        floor[x][y] = getScavengerChamber();
                        scavengerBudget--;
                        layout[layoutCount++] = floor[x][y];
                    } else {
                        List<ChamberType> possibilities = new ArrayList<>(3);
                        boolean allSpent = combatBudget <= 0 && puzzleBudget <= 0 && scavengerBudget <= 0;
                        if (allSpent || combatBudget > 0)
                            possibilities.add(ChamberType.COMBAT);
                        if (allSpent || puzzleBudget > 0)
                            possibilities.add(ChamberType.PUZZLE);
                        if (allSpent || scavengerBudget > 0)
                            possibilities.add(ChamberType.SCAVENGER);
                        ChamberType roll = possibilities.get(generator.nextInt(possibilities.size()));
                        if (roll == ChamberType.COMBAT) {
                            floor[x][y] = COMBAT_ROOMS[combatIndex].newChamber();
                            layout[layoutCount++] = floor[x][y];
                            combatIndex += combatIndexDirection;
                            if (combatIndex < 0)
                                combatIndex = COMBAT_ROOMS.length - 1;
                            else
                                combatIndex = combatIndex % COMBAT_ROOMS.length;
                            combatBudget--;
                        } else if (roll == ChamberType.PUZZLE) {
                            ChamberDefinition poll = puzzles.poll();

                            if (poll == null) {
                                chambers = new Chamber[2][4][2];
                                layout = new Chamber[30];
                                generate();
                                return;
                            }

                            floor[x][y] = poll.newChamber();
                            layout[layoutCount++] = floor[x][y];
                            puzzleBudget--;
                        } else {
                            floor[x][y] = getScavengerChamber();
                            layout[layoutCount++] = floor[x][y];
                            scavengerBudget--;
                        }
                    }
                }
            }
            layout[layoutCount++] = floor[x][y];
            floor[x][y].setRotation(getRotation(previousMove)); // set rotation for final room
            floor[x][y].setLayout(0); // final rooms only have one layout
        }

        List<DynamicChunk> west = new ArrayList<>(16 * 8);
        List<DynamicChunk> east = new ArrayList<>(16 * 8);

        map = new DynamicMap();

        for (int z = 2; z > 0; z--) { //lets start from the top
            for (int x = 0; x < X_LENGTH; x++) {
                for (int y = 0; y < Y_LENGTH; y++) {

                    int basePointX;
                    List<DynamicChunk> target;
                    if (x > 1) {
                        target = east;
                        basePointX = (x - 2) * 4;
                    } else {
                        target = west;
                        basePointX = x * 4;
                    }
                    int basePointY = y * 4;
                    Chamber chamber = chambers[z - 1][x][y];

                    if (chamber == null) { // nothing generated here
                        // need to fill out empty spots so all the upper floor rooms display at the same visual height
                        for (int i = 0; i < 4; i++)
                            for (int j = 0; j < 4; j++)
                                target.add(new DynamicChunk(400, 712, 0).pos(basePointX + i, basePointY + j, z));

                        continue;
                    }

                    chamber.setBasePosition(Position.of(map.sw.getBaseX() + (x * 32), map.sw.getBaseY() + (y * 32), z));
                    chamber.setPosition(basePointX, basePointY, z);
                    chamber.setDungeon(this);

                    target.addAll(chamber.getChunks());
                }
            }
        }

        //TODO olm chamber
        olmChamber = new GreatOlmChamber(this);
        west.addAll(olmChamber.getChunks());
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                east.add(new DynamicChunk(400, 712, 0).pos(i, j, 0));
        map.build(west).buildSe(east);

        olmChamber.onBuild();

        for (int z = 2; z > 0; z--) { //lets start from the top
            for (int x = 0; x < X_LENGTH; x++) {
                for (int y = 0; y < Y_LENGTH; y++) {
                    Chamber chamber = chambers[z - 1][x][y];
                    if (chamber != null)
                        chamber.onBuild();
                }
            }
        }

        olmChamber.onRaidStart();
    }

    /**
     * Destroys the dungeon safely.
     */
    public void destroy() {
        if (map != null)
            map.destroy();
        map = null;
        getGroup().setAttribute(ChambersOfXeric.STARTED, false);
        getGroup().setAttribute(ChambersOfXeric.POINTS, false);
        getGroup().setAttribute(ChambersOfXeric.COMPLETE, false);
    }

    /**
     *
     * @param player
     */
    public void join(Player player) {
        //TODO move the player into lobby room.
        getPlayers().add(player);
        player.lock();
        FadingScreen.fade(player, () -> {
            player.sendMessage("--Layout--");
            String[] layout = getLayout();
            player.sendMessage("Upper Level - " + layout[0]);
            player.sendMessage("Lower Level - " + layout[1]);
            player.sendMessage("--End Layout--");
            player.setNextPosition(getStartingChamber().getEntrancePosition());
            player.getControlerManager().startControler(new ChambersController(XericDungeon.this));
            player.unlock();
        });
    }

    /**
     * Safely removes a player from the dungeon.
     * @param player
     *          the player that is leaving.
     */
    public void leave(Player player) {
        //TODO move the player outside, remove chambers exclusive items
        if (players.contains(player)) {
            player.getControlerManager().forceStop(); //stop controller so we can drop items
            player.setNextPosition(OUTSIDE);
            getPlayers().remove(player);
        }
        if (players.size() == 0) { //last player has left the group, reset the map
            generator.setSeed(System.nanoTime());
            destroy();
        } else {
            //assign new leader
            Player leader = players.get(players.size() - 1);
            getGroup().setLeader(leader);
        }
    }

    /**
     * Gets a random resource room.
     * @return
     */
    private Chamber getResourceChamber() {
        return generator.nextDouble() < 0.5 ? FISHING.newChamber() : HUNTER.newChamber();
    }

    /**
     * Gets a random scavenger chamber.
     * @return
     */
    private Chamber getScavengerChamber() {
        return generator.nextDouble() < 0.5 ? SCAVENGERS.newChamber() : SCAVENGER_RUINS.newChamber();
    }

    /**
     * Gets the next direction to move.
     * @param currentZ
     *          the current z.
     * @param currentX
     *          the current x.
     * @param currentY
     *          the current y.
     * @param previousMove
     *          the previous moved direction.
     * @param allowVeritcal
     *          if allowed to move up in z.
     * @param startingChamber
     *          if this move is a starting chamber.
     * @return
     *          the next direction to move.
     */
    private Direction getNextMove(int currentZ, int currentX, int currentY, Direction previousMove, boolean allowVeritcal, boolean startingChamber) {
        List<Direction> directions = new ArrayList<>(4);
        if (currentX > 0 && getChamberInDirection(currentZ, currentX, currentY, Direction.WEST) == null)
            directions.add(Direction.WEST);
        if (currentX < X_LENGTH - 1 && getChamberInDirection(currentZ, currentX, currentY, Direction.EAST) == null)
            directions.add(Direction.EAST);
        if (allowVeritcal) {
            if (!startingChamber && currentY > 0 && getChamberInDirection(currentZ, currentX, currentY, Direction.SOUTH) == null)
                directions.add(Direction.SOUTH);
            if (currentY < Y_LENGTH - 1 && getChamberInDirection(currentZ, currentX, currentY, Direction.NORTH) == null)
                directions.add(Direction.NORTH);
        }
        if (directions.size() == 0)
            return null;
        return directions.get(generator.nextInt(directions.size()));
    }

    /**
     * Gets the chamber in the desired direction, relative to the current position.
     * @param currentZ
     *          the current z.
     * @param currentX
     *          the current x.
     * @param currentY
     *          the current y.
     * @param move
     *          the direction to move.
     * @return
     *          the chamber found, null if none.
     */
    private Chamber getChamberInDirection(int currentZ, int currentX, int currentY, Direction move) {
        return chambers[currentZ][currentX + move.deltaX][currentY + move.deltaY];
    }

    /**
     * Gets the next room rotation.
     * @param lastMove
     *          the last direction moved.
     * @return
     */
    private static int getRotation(Direction lastMove) {
        switch (lastMove) {
            case NORTH:
                return 0;
            case EAST:
                return 1;
            case SOUTH:
                return 2;
            case WEST:
                return 3;
            default:
                throw new IllegalArgumentException("Invalid last move");
        }
    }

    /**
     *
     * @param rotation
     * @param nextMove
     * @return
     */
    private static int getLayout(int rotation, Direction nextMove) {
        if (rotation == 0) {
            if (nextMove == Direction.WEST) {
                return 0;
            } else if (nextMove == Direction.NORTH) {
                return 1;
            } else {
                return 2;
            }
        } else if (rotation == 1) {
            if (nextMove == Direction.NORTH) {
                return 0;
            } else if (nextMove == Direction.EAST) {
                return 1;
            } else {
                return 2;
            }
        } else if (rotation == 2) {
            if (nextMove == Direction.EAST) {
                return 0;
            } else if (nextMove == Direction.SOUTH) {
                return 1;
            } else {
                return 2;
            }
        } else if (rotation == 3) {
            if (nextMove == Direction.SOUTH) {
                return 0;
            } else if (nextMove == Direction.WEST) {
                return 1;
            } else {
                return 2;
            }
        } else {
            throw new IllegalArgumentException("invalid");
        }
    }

    /**
     * Gets the budget for the dungeon puzzles rooms.
     * @param budget
     *          the remaining budget.
     * @return
     *          the budget.
     */
    private int getPuzzleBudget(int budget) {
        if (budget <= 2)
            return budget;
        if (generator.nextDouble() <= THREE_PUZZLE_ROOM_CHANCE)
            return 3;
        else
            return 2;
    }

    /**
     * Gets the budget for the dungeon combat rooms.
     * @param budget
     *          the remaining budget.
     * @return
     *          the budget.
     */
    private int getCombatBudget(int budget) {
        if (budget <= 4)
            return budget;
        if (generator.nextDouble() < FIVE_COMBAT_ROOMS_CHANCE)
            return 5;
        else
            return 4;
    }

    /**
     * Gets the layout as a readable string for scouting dungeon layouts.
     * @return
     *      the layout.
     */
    private String[] getLayout() {
        StringJoiner lowerLevel = new StringJoiner (", ", "[", "]");
        StringJoiner upperLevel = new StringJoiner (", ", "[", "]");
        boolean isLowerLevel = false;
        boolean isUpperLevel = false;
        for (Chamber room : layout) {
            if (room == null) {
                continue;
            }
            ChamberDefinition chamberDefinition = room.getDefinition();
            if (chamberDefinition.equals(LOWER_LEVEL_START) || chamberDefinition.equals(LOWER_FLOOR_FINISH)) {
                isLowerLevel = !isLowerLevel;
                continue;
            }
            if (chamberDefinition.equals(START) || chamberDefinition.equals(UPPER_FLOOR_FINISH)) {
                isUpperLevel = !isUpperLevel;
                continue;
            }
            if (isLowerLevel) {
                lowerLevel.add(chamberDefinition.getName());
            } else if (isUpperLevel) {
                upperLevel.add(chamberDefinition.getName());
            }
        }
        String[] levels = new String[2];
        levels[0] = upperLevel.toString();
        levels[1] = lowerLevel.toString();
        return levels;
    }

    /**
     * Gets the time between the start of the dungeon and Instance.now()
     * @return
     *      time in mm:ss as a string.
     */
    public String getTimeSinceStart() {
        Duration duration = Duration.between(startTime, Instant.now());
        return String.format("%02d:%02d", duration.toMinutes(), duration.getSeconds() % 60);
    }

    /**
     * Adds points to the dungeon total.
     * @param player
     *          the player adding the points.
     * @param amount
     *          the amount of points to add.
     */
    public void addPoints(Player player, int amount) {
        points += amount;
    }

}
