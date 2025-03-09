package com.rs.game.world.entity.player.content;

import java.util.Random;

import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;

/**
 * @Author: Apache Ah64
 */
public class TriviaBot {

	private static String questions[][] = {



		{ "What was the launch month of Venomite?", "october" },
		{ "What monster drops the abyssal whip?", "Abyssal demon" },
		{ "Where is the home of Venomite?", "Island" },
		{ "How many shops can ironman use?", "1" },
		{ "What is the Venomite game playing mode? (Multiplayer/Solo)", "Multiplayer" },
		{ "What is maximum combat level in Venomite?", "138" },
		{ "Is a tomato a fruit or a vegetable?", "Fruit" },
		{ "What is the required level to wear drygore weapons?", "90" },
		{ "What weapon is required to kill a Tormented Demon?", "Darklight" },
		{ "What is the attack requirement for Armadyl godsword?", "75" },
		{ "What is the attack requirement for Goliath Gloves?", "80" },
		{ "What is the most powerful curse?", "Turmoil" },
		{ "How much of a percentage does a dragon dagger special requires?", "25%" },
		{ "What color does a iron donator sign have?", "bronze" }, { "What color does a dragon donator sign have?", "red" },
		{ "What is the best free to play armour?", "Rune" },
		{ "Where do you get Zeal at?", "Soul wars" },
		{ "What is the highest donator rank?", "Dragon" },
		{ "Can i in any way get a higher combat level than 138?", "no" },
		{ "Which Non Player Character drops sigils?", "Corporeal beast" },
		{ "How long does a Trivia Ticket boost last?", "1 hour" },
		{ "What is the name of the new firecape?", "TokHaar-Kal" },


	};

	public static int questionid = -1;
	public static int round = 0;
	public static boolean victory = false;

	public TriviaBot() {
		// TODO
	}

	public static void Run() {
		int rand = RandomQuestion();
		questionid = rand;
		victory = false;
		for (Player participant : World.getPlayers()) {
			if (participant == null) {
				continue;
			}

			participant.getPackets().sendGameMessage(
					"<col=27408B>[Trivia Question]</col> - " + questions[rand][0]);

		}
	}

	/*public static void sendRoundWinner(String winner) {
		Player p = World.getPlayerByDisplayName(Utils.formatPlayerNameForDisplay(winner)); //declares the player winner
			for (Player participant : World.getPlayers()) {
				if (participant == null)
					continue;
				if (TriviaArea(participant)) {
					victory = true;
					participant.setTriviaPoints(participant.getAnswerPoints() + 1);
					participant.getPackets().sendGameMessage("<col=FFC700><shad=FFFF00>[Trivia]Congratulations, " + winner + " <col=FFC700><shad=FFFF00>Answered the question and won a point!");
				}
			}
			//p.getInventory().addItem(1, 1);
		}*/

	public static void sendRoundWinner(String winner, Player player) {

		for(Player participant : World.getPlayers()) {
			if(participant == null) {
				continue;
			}
			if (Player.isSunday() && !player.finishedTask) {
				player.answerTrivia++;
				if (player.answerTrivia >= 10 && !player.triviaMSG) {
					player.sm("<col=8B000>You have answered 10 questions for todays task!");
					player.triviaMSG = true;
				} else if (player.answerTrivia >= 10 && player.oresMined >= 50 && player.foundBox) {
					player.sm("<col=8B000>You have finished todays task and claimed your reward!");
					player.setSkillPoints(player.getSkillPoints() + 100);
					player.getSquealOfFortune().giveEarnedSpins(5);
					player.finishedTask = true;
					player.startedTask = false;
				}
			}
			if (player.TriviaBoostActive == true) {
				victory = true;
				player.setTriviaPoints(player.getTriviaPoints() + 2);
				player.hasAnswered = true;
				World.sendWorldMessage("<col=27408B>[Trivia Winner]</col> - "+ winner +" answered correctly with a boost ticket and is rewarded <col=27408N>2</col> Trivia points! ", false);
				return;
			}
			victory = true;
			player.setTriviaPoints(player.getTriviaPoints() + 1);
			player.hasAnswered = true;
			World.sendWorldMessage("<col=27408B>[Trivia Winner]</col> - "+ winner +" answered the question correctly and got <col=27408N>1</col> Trivia point!", false);
			return;

		}

	}

	public static void verifyAnswer(final Player player, String answer) {
		if (victory) {
			player.getPackets()
			.sendGameMessage(
					"<col=27408B>[Trivia Error]</col> - That round has already been won, wait for the next round.");
		} else if (questions[questionid][1].equalsIgnoreCase(answer)) {
			round++;
			sendRoundWinner(player.getDisplayName(), player);
		} else {
			player.getPackets().sendGameMessage(
					"<col=27408B>[Trivia Error]</col> - That answer wasn't correct, please try it again.");
		}
	}

	public static int RandomQuestion() {
		int random = 0;
		Random rand = new Random();
		random = rand.nextInt(questions.length);
		return random;
	}

	public static boolean TriviaArea(final Player participant) {
		if (participant.getX() >= 2630 && participant.getX() <= 2660
				&& participant.getY() >= 9377 && participant.getY() <= 9400) {
			return true;
		}
		return false;
	}
}
