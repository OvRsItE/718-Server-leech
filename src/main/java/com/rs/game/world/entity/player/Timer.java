package com.rs.game.world.entity.player;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.content.XPWell;
import com.rs.game.world.entity.player.content.quests.impl.KharzardLures;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.utility.Logger;
import com.rs.utility.ShopsHandler;
import com.rs.utility.Utils;

public class Timer {
 	
	public static void startTimer() {
	CoresManager.fastExecutor.schedule(new TimerTask() {
			int timer = 10;
			
			
			@Override
			public void run() {
				for (Player player : World.getPlayers()) {
				
				if (timer > 0) {
					player.getInterfaceManager().sendTimerInter();
					timer--;
				} else if (timer == 0) {
					player.getInterfaceManager().closeOverlay(true);
					}
				}
			}
		}, 0L, 1000L);
	}
	
	public static void startWellTimer() {
		CoresManager.fastExecutor.schedule(new TimerTask() {
				
				@Override
				public void run() {
					
					if (World.isWellActive() && World.WellTimer > 0) {
						World.WellTimer--;
					} else {
						
					}
				}
			}, 0L, 1000L);
		}
	
	public static void startTriviaBoost(final Player player) {
		CoresManager.fastExecutor.schedule(new TimerTask() {
				
				@Override
				public void run() {
					
					if (player.TriviaBoost > 0 && player.TriviaBoostActive == true) {	
						player.TriviaBoost--;
					} else if (player.TriviaBoost == 0 && player.TriviaBoostActive == true) {
						
						player.getDialogueManager().startDialogue("SimpleMessage", "Your Trivia points boost effect has worn off");
						player.TriviaBoostActive = false;
						player.TriviaBoost = 0;
					}
				}
			}, 0L, 1000L);
		}
	
	public static void BertReading(final Player player) {
			player.sm("Bert starst to read the note...");
		CoresManager.fastExecutor.schedule(new TimerTask() {
				int loop = 3;
				
				@Override
				public void run() {
					
					
					if (loop == 3) {
						player.sm("Bert seems to be shocked from what hes reading..");
						loop--;
					}else if (loop == 2) {
						//player.sm("Bert starst to read the note...");
						loop--;
					}else if (loop == 1) {
						player.getDialogueManager().startDialogue("AdventurerQuest");
						loop--;
					} 
				
			}
			}, 0L, 1000L);
		}
	
	public static void startXPTimeLeftTimer(final Player player) {
		CoresManager.fastExecutor.schedule(new TimerTask() {
				int timer1 = 20;
			@Override
			public void run() {
					if (timer1 > 0) {
						player.getInterfaceManager().sendOverlay(798, true);
						player.getPackets().sendIComponentText(798, 2, "XP Well Time Remaning.");
						//player.getPackets().sendIComponentText(798, 3, "There is : "+XPWell.getXPTimeLeft(player)+"");
						timer1--;
					} else if (timer1 == 0) {
						player.getInterfaceManager().closeOverlay(true);
				}
			}
		}, 0L, 1000L);
	}
}
	
	
	
	
	
	