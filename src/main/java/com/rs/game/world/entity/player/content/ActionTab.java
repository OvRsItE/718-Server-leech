package com.rs.game.world.entity.player.content;

import java.text.DecimalFormat;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;

/**
 *
 * @author Chaoz
 */
public class ActionTab {
	private static final DecimalFormat format = new DecimalFormat("#.##");
	public static void sendTab(final Player player) {
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {

				String startTime = "00:00";
				int h = player.onlinetime / 60 + Integer.valueOf(startTime.substring(0, 1));
				int m = player.onlinetime % 60 + Integer.valueOf(startTime.substring(3, 4));

				final String eventBossLocation = World.eventBoss == null || World.eventBoss.isDead() || World.eventBoss.isFinished() ? "dead" : World.eventBoss.getZone().getName();
				player.getPackets().sendIComponentText(930, 10, "<col=228B22>Information Panel</col>");
				player.getPackets().sendIComponentText(930, 16, "Community<"
						+ "<br><col=228B22>Venomite</col>"
						+ "<br>"
						+ "<br>General Information"
						+ "<br><col=228B22>Players Online : <col=FFFFFF>"+ World.getPlayers().size() + " </col>"
						+ "<br><col=228B22>Total Donated : <col=FFFFFF>$" + player.getDonationManager().getDonationTotal() + "</col>"
						+ "<br><col=228B22>Game Mode : <col=FFFFFF>"+ player.getGameMode().name() +" </col>"
						+ "<br><col=228B22>Drop rate modfier : <col=FFFFFF>"+ format.format((player.getEquipment().getLuckBoost() * player.getGameMode().getLuckBoost() * player.getLuckBoost() * player.getDonationManager().getRank().getLuckBoost() *  (player.getCooldownManager().hasCooldown("vote_boosts") ? 1.05 : 1))) + "x </col>"
						+ "<br><col=228B22>Warning Marks : <col=FFFFFF>"+player.blackMark+"</col>"
						+ "<br><col=228B22>Reaper assignment : <col=FFFFFF>" + player.getBossSlayer().getTaskInformation() + "</col>"
						+ "<br><col=228B22>Event Boss : <col=FFFFFF>" + eventBossLocation + "</col>"
						+ "<br>"
						+ "<br>Player Statistics" + "<br><col=228B22>Total Play Time :<col=FFFFFF> "+ h + " H " + m + " M </col>"
						+ "<br><col=228B22>Total Kills :<col=FFFFFF>  "+ player.getKillCount() + "</col> "
						+ "<br><col=228B22>Total Deaths :<col=FFFFFF> "+ player.getDeathCount() + "</col>"
						+ "<br><col=228B22>Total Votes :<col=FFFFFF> "+ player.getAllVotes() + "</col>"
						+ "<br><col=228B22>Skill Points :<col=FFFFFF> "+ player.getSkillPoints() +"</col>"
						+ "<br>"
						+ "<br>Account Points"
						+ "<br><col=228B22>Reaper Points :<col=FFFFFF> "+ player.getBossSlayer().getReaperPoints()+ "</col>"
						+ "<br><col=228B22>Trivia Points :<col=FFFFFF> "+ player.getTriviaPoints() + "</col>"
						+ "<br><col=228B22>venomite Points :<col=FFFFFF> "+ player.getPvmPoints() + "</col>"
						+ "<br><col=228B22>Vote Points :<col=FFFFFF> "+ player.getVotePoints() + "</col>"
						+ "<br><col=228B22>Achievement Points :<col=FFFFFF> " + "TODO" + "</col>"
						+ "<br><col=228B22>PvP Points :<col=FFFFFF> "+ player.getPvPPoints() + "</col>"
						+ "<br><col=228B22>Slayer Points :<col=FFFFFF> "+ player.getSlayerManager().getPoints() +"</col>"
						+ "<br><col=228B22>Loyality Points :<col=FFFFFF> " + player.getLoyaltyPoints() + "</col>");
				// stop();
			}

		}, 0, 5);

	}
}