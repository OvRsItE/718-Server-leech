package com.rs.game.item;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.Player;

public class FloorItem extends Item {

	private static final long serialVersionUID = -2287633342490535089L;

	private Position tile;
	private String ownerName;
	// 0 visible, 1 invisible, 2 visible and reappears 30sec after taken
	private int type;

	public FloorItem(int id) {
		super(id);
	}

	public FloorItem(Item item, Position tile, Player owner, boolean underGrave, boolean invisible) {
		super(item.getId(), item.getAmount());
		this.tile = tile;
		if (owner != null)
			this.ownerName = owner.getUsername();
		this.type = invisible ? 1 : 0;
	}

	@Deprecated
	public FloorItem(Item item, Position tile, boolean appearforever) {
		super(item.getId(), item.getAmount());
		this.tile = tile;
		this.type = appearforever ? 2 : 0;
	}

	public Position getTile() {
		return tile;
	}

	public boolean isInvisible() {
		return type == 1;
	}

	public boolean isForever() {
		return type == 2;
	}

	public String getOwner() {
		return ownerName;
	}

	public boolean hasOwner() {
		return ownerName != null;
	}

	public void setInvisible(boolean invisible) {
		type = invisible ? 1 : 0;
	}

}
