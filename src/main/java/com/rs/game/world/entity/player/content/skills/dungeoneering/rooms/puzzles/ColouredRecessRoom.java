package com.rs.game.world.entity.player.content.skills.dungeoneering.rooms.puzzles;


import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.item.Item;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.dungeonnering.DungeonNPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.dungeoneering.rooms.PuzzleRoom;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceMovement;
import com.rs.game.world.entity.updating.impl.Hit;

public class ColouredRecessRoom extends PuzzleRoom {

	public static final int[] SHELVES =
	{ 35243, 35242, 35241, 35245, 35246 };

	//+1-4 for colors
	public static final int[] BASE_BLOCKS =
	{ 13024, 13029, 13034, 13039, 13044 };

	public static final int[][] LOCATIONS =
	{
	{ 5, 10 },
	{ 10, 10 },
	{ 10, 5 },
	{ 5, 5 }, };

	private Block[] blocks;
	private boolean[] used;

	@Override
	public void openRoom() {
		manager.spawnRandomNPCS(reference);
		blocks = new Block[4];
		used = new boolean[4];
		for (int i = 0; i < blocks.length; i++) {
			while_: while (true) {
				Position tile = manager.getTile(reference, 4 + com.rs.utility.Utils.random(8), 4 + com.rs.utility.Utils.random(8));
				if (!World.isTileFree(0, tile.getX(), tile.getY(), 1)) {
					continue;
				}
				for (int j = 0; j < i; j++) {
					if (blocks[j].matches(tile)) {
						continue while_;
					}
				}
				blocks[i] = new Block(tile);
				break;
			}
		}

	}

	public void checkComplete() {
		if(isComplete())
			return;
		outer: for (Block block : blocks) {
			for (int tileColor = 0; tileColor < LOCATIONS.length; tileColor++) {
				int[] location = LOCATIONS[tileColor];
				if (manager.getTile(reference, location[0], location[1]).matches(block)) {
					int color = block.getId() - BASE_BLOCKS[type] - 1;
					if (color == tileColor) {
						continue outer;
					} else {
						return;
					}

				}
			}
			return;
		}
		setComplete();
	}

	public class Block extends DungeonNPC {

		private static final long serialVersionUID = -1770292505264759755L;

		public Block(Position tile) {
			super(BASE_BLOCKS[type], tile, manager, 0);
		}

		public void handle(final Player player, final boolean push) {
			//TODO: make sure 2 players can't move 2 statues ontop of eachother in the same tick? although it doesn't really matter
			boolean pull = !push;

			int[] nPos = manager.getRoomPos(this);
			int[] pPos = manager.getRoomPos(player);

			final int dx = push ? getX() - player.getX() : player.getX() - getX();
			final int dy = push ? getY() - player.getY() : player.getY() - getY();
			final int ldx = push ? nPos[0] - pPos[0] : pPos[0] - nPos[0];
			final int ldy = push ? nPos[1] - pPos[1] : pPos[1] - nPos[1];
			
			if (nPos[0] + ldx < 4 || nPos[0] + ldx > 11 || nPos[1] + ldy < 4 || nPos[1] + ldy > 11) {
				player.getPackets().sendGameMessage("You cannot push the block there.");
				return;
			}
			final Position nTarget = transform(dx, dy, 0);
			final Position pTarget = player.transform(dx, dy, 0);

			if (!World.isTileFree(0, nTarget.getX(), nTarget.getY(), 1) || !World.isTileFree(0, pTarget.getX(), pTarget.getY(), 1)) {
				player.getPackets().sendGameMessage("Something is blocking the way.");
				return;
			}
			if (!ColouredRecessRoom.this.canMove(null, nTarget) || (pull && !ColouredRecessRoom.this.canMove(null, pTarget))) {
				player.getPackets().sendGameMessage("A block is blocking the way.");
				return;
			}

			for (Player team : manager.getParty().getTeam()) {
				if (team != player && team.matches(nTarget)) {
					player.getPackets().sendGameMessage("A party member is blocking the way.");
					return;
				}
			}

			player.lock(2);
			WorldTasksManager.schedule(new WorldTask() {

				private boolean moved;

				@Override
				public void run() {
					if (!moved) {
						moved = true;
						addWalkSteps(getX() + dx, getY() + dy);
						Position fromTile = new Position(player.getX(), player.getY(), player.getZ());
						player.setNextPosition(pTarget);
						player.setNextForceMovement(new ForceMovement(fromTile, 0, pTarget, 1, getFaceDirection(Block.this, player)));
						player.animate(new Animation(push ? 3065 : 3065));
					} else {
						checkComplete();
						stop();
					}
				}
			}, 0, 0);

		}

		public boolean useItem(Player player, Item item) {
			int color = (item.getId() - 19869) / 2;
			if (color < 0 || color > 3) {
				return true;
			}
			if (getId() != BASE_BLOCKS[type]) {
				return true;
			}
			if (used[color]) {
				return true;
			}
			used[color] = true;
			player.getInventory().deleteItem(item);
			player.animate(new Animation(832));
			setNextNPCTransformation(getId() + color + 1);
			checkComplete();
			return false;
		}

	}

	@Override
	public boolean canMove(Player player, Position to) {
		for (Position block : blocks) {
			if (to.matches(block)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean processObjectClick1(Player p, WorldObject object) {
		if (object.getId() == SHELVES[type]) {
			p.getDialogueManager().startDialogue("ColouredRecessShelvesD", (Object[])null);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean processObjectClick2(Player player, WorldObject object) {
		if (Math.random() < 0.2) {
			player.getPackets().sendGameMessage("The vial reacts explosively as you pick it up.");
			player.applyHit(new Hit(player, (int) (player.getMaxHitpoints() * 0.25D), Hit.HitLook.REGULAR_DAMAGE));
			return false;
		}
		player.getInventory().addItem(19869, 1);
		player.animate(new Animation(832));
		return true;
	}
	
	@Override
	public boolean processObjectClick3(Player player, WorldObject object) {
		if (Math.random() < 0.2) {
			player.getPackets().sendGameMessage("The vial reacts explosively as you pick it up.");
			player.applyHit(new Hit(player, (int) (player.getMaxHitpoints() * 0.25D), Hit.HitLook.REGULAR_DAMAGE));
			return false;
		}
		player.getInventory().addItem(19871, 1);
		player.animate(new Animation(832));
		return true;
	}
	
	@Override
	public boolean processObjectClick4(Player player, WorldObject object) {
		if (Math.random() < 0.2) {
			player.getPackets().sendGameMessage("The vial reacts explosively as you pick it up.");
			player.applyHit(new Hit(player, (int) (player.getMaxHitpoints() * 0.25D), Hit.HitLook.REGULAR_DAMAGE));
			return false;
		}
		player.getInventory().addItem(19873, 1);
		player.animate(new Animation(832));
		return true;
	}
	
	@Override
	public boolean processObjectClick5(Player player, WorldObject object) {
		if (Math.random() < 0.2) {
			player.getPackets().sendGameMessage("The vial reacts explosively as you pick it up.");
			player.applyHit(new Hit(player, (int) (player.getMaxHitpoints() * 0.25D), Hit.HitLook.REGULAR_DAMAGE));
			return false;
		}
		player.getInventory().addItem(19875, 1);
		player.animate(new Animation(832));
		return true;
	}
	
	@Override
	public boolean processItemOnNPC(Player player, NPC npc, Item item) {
		if (npc instanceof Block) {
			((Block) npc).useItem(player, item);
			return false;
		}
		return true;
	}

	@Override
	public boolean processNPCClick1(Player player, NPC npc) {
		if (npc instanceof Block) {
			((Block) npc).handle(player, true);
			return false;
		}
		return true;
	}

	@Override
	public boolean processNPCClick2(Player player, NPC npc) {
		if (npc instanceof Block) {
			((Block) npc).handle(player, false);
			return false;
		}
		return true;
	}
	
	public static int getFaceDirection(Position faceTile, Player player) {
		if (player.getX() < faceTile.getX())
			return ForceMovement.EAST;
		else if (player.getX() > faceTile.getX())
			return ForceMovement.WEST;
		else if (player.getY() < faceTile.getY())
			return ForceMovement.NORTH;
		else if (player.getY() > faceTile.getY())
			return ForceMovement.SOUTH;
		else
			return 0;
	}

}
