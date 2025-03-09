/**
 * 
 */
package com.rs.game.world.entity.npc.instances.zulrah.combat;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.world.entity.npc.instances.zulrah.Sequence;
import com.rs.game.world.entity.npc.instances.zulrah.ZulrahInstance;
import com.rs.game.world.entity.npc.instances.zulrah.ZulrahNPC;
import com.rs.game.world.entity.npc.instances.zulrah.ZulrahPosition;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;

/**
 * @author Kris
 * @author ReverendDread
 * Jul 23, 2018
 */
public class DiveSequence implements Sequence {

	private static final Animation DIVE_ANIM = Animation.createOSRS(5072);
	private static final Animation RISE_ANIM = Animation.createOSRS(5073);
	
	public DiveSequence(final ZulrahPosition pos, final int npcId) {
		this.pos = pos;
		this.npcId = npcId;
	}
	
	private final ZulrahPosition pos;
	private final int npcId;
	
	@Override
	public int attack(ZulrahNPC zulrah, ZulrahInstance instance, Player target) {
		zulrah.animate(DIVE_ANIM);
		zulrah.setCantInteract(true);
		zulrah.getReceivedHits().clear();
		WorldTasksManager.schedule(new WorldTask() {
			private boolean risen;
			@Override
			public void run() {
				if (zulrah.isDead() || zulrah.isFinished()) {
					stop();
					return;
				}
				if (!risen) {
					zulrah.setNextPosition(instance.getLocation(pos.getSpawn()));
					zulrah.setNextFacePosition(instance.getLocation(pos.getFace()));
					zulrah.transformIntoNPC(npcId);
					zulrah.animate(RISE_ANIM);
					zulrah.setCantInteract(false);
					risen = true;
				} else {
					zulrah.unlock();
					Sequence sequence = zulrah.getNextSequence();
					sequence.attack(zulrah, instance, target);
					stop();
				}
			}
		}, 3, 3);
		return 6;
	}


}
