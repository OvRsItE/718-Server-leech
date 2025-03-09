/**
 * 
 */
package com.rs.game.world.entity.npc.instances.zulrah;

import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;

import lombok.Getter;

/**
 * @author Kris
 * @author ReverendDread
 * Jul 23, 2018
 */
public class SnakelingNPC extends NPC {

	private static final long serialVersionUID = 1L;
	
	private static final Animation ANIM = new Animation(17823);
	
	public SnakelingNPC(final ZulrahNPC zulrah, final Player player, final Position tile) {
		super(asOSRS(2045), tile, -1, 0, true, true);
		setForceMultiArea(true);
		lock(3);
		animate(ANIM);
		this.player = player;
		this.zulrah = zulrah;
	}
	
	@Getter private final ZulrahNPC zulrah;
	private final Player player;
	
	@Override
	public void processNPC() {
		if (isLocked() || isDead()) {
			return;
		}
		if (getCombat().getTarget() != player) {
			getCombat().setTarget(player);
		}
		getCombat().process();
	}
	
	@Override
	public void sendDeath(final Entity source) {
		super.sendDeath(source);
		zulrah.getSnakelings().remove(this);
	}
	
}
