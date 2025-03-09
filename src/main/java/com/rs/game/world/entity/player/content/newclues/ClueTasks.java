package com.rs.game.world.entity.player.content.newclues;

import com.rs.game.world.entity.player.Player;
import lombok.Getter;

public class ClueTasks {
	
	
	public enum Maps {
		Map1(337, 2971, 3414, "If you Fala by A Door you might need help on this one!"),
		Map2(338, 3021, 3912, "In between a lava blaze and a near Deathly Agility Course!"),
		Map3(339, 2722, 3339, "South of where legends may be placed, and east of great thieving!"),
		Map4(341, 3435, 3265, "South of a muchky mucky mucky mucky swamp lands, and barely north of Haunted Mines!"),
		Map5(344, 2665, 3561, "West of a murderous Mansion, and south of a city of vikings!"),
		Map6(346, 3166, 3359, "Slightly South of a city of great knights and lots of Shops!"),
		Map7(347, 3290, 3372, "A mining place located near a city of great knights and lots of Shops"),
		Map8(348, 3092, 3225, "Slightly south of a village known for thieving masters of farming!"),
		Map9(351, 3043, 3398, "North East Corner of a city based around a castle with a mort around it!"),
		Map10(352, 2906, 3295, "Right next to a guild known for people with skilled hands!"),
		Map11(353, 2616, 3077, "In a city that Rhymes with tan i will, if you say it really fast!"),
		Map12(354, 2612, 3482, "West of some woods that sound like Mc Jagger!"),
		Map13(356, 3110, 3152, "South of a tower full of magical people!"),
		Map14(360, 2652, 3232, "North of a tower known to give life and south of a city that contains thieving!"),
		Map15(362, 2923, 3210, "West of the place best known for starting a house!");

		String chat;
		int interfaceId, xcoord, ycoord;

		private Maps(int interid, int x, int y, String hint) {
			this.interfaceId = interid;
			this.xcoord = x;
			this.ycoord = y;
			this.chat = hint;
		}
	}
	
	public enum ObjectMaps {
		Map1(358, new int[] { 18506, 2457, 3182 }, "Near an observatory meant for getting a compas on RS!"),
		Map2(361, new int[] { 46331, 2565, 3248 },
				"Just south of a city known for thieving and outside a tower of clock!");

		@Getter public int objectId, objectX, objectY;
		@SuppressWarnings("unused")
		int[] objectinfo;
		String hint;
		int interid;

		private ObjectMaps(int interid, int[] object, String text) {
			this.hint = text;
			this.interid = interid;
			this.objectinfo = object;
			this.objectId = object[0];
			this.objectX = object[1];
			this.objectY = object[2];
		}
	}
	
	public enum Riddles {
		Riddle1(22, new int[] { 2967, 4386, 2970, 4380 },
				new String[] { "There once was a villan", "of grey and white", "he also had a bit of bage",
						"do a clap outside his cave", "to scare him off", "", "", "" }), // Corp
		Riddle2(15, new int[] { 3190, 9828, 3193, 9825 },
				new String[] { "I am a token of the greatest love", "I have no beginning or end",
						"Go to the place where money is lent", "Jig by the gate to be my friend!", "", "", "", "" }), // Varrock
																														// Bank
																														// Basement
		Riddle3(28, new int[] { 3162, 3255, 3171, 3244 },
				new String[] { "For the reward you seek", "a city of lumber and bridge", "is west of a place that you",
						"must go to get some ham", "once outside do a lean", " to meat Mr. Mean!", "", "" }), // Ham
																												// Entrance
		Riddle4(14, new int[] { 2987, 3123, 3001, 3109 },
				new String[] { "Near a ring known to teleport", "On a point full of mud", "A simple emote is needed",
						"An emote known as skipping or dance!", "", "", "", "" }),
		Riddle5(30, new int[] { 2884, 3449, 2898, 3438 },
				new String[] { "This reward will require a bit", "For the first thing you will", "Need to be at a den",
						"and you have to be a rouge", "You must have an idea outside",
						"Of its entrance to get a reward!", "", "" });// Mudsckipper
																		// Point
		int[] locations;
		String[] riddles;
		int emoteid;

		private Riddles(int id, int[] location, String[] riddles) {
			this.locations = location;
			this.riddles = riddles;
			this.emoteid = id;
		}
		// Riddle interface 345
	}
	
	
	public static void showObjectMap(Player p, ObjectMaps objmap) {
		p.getPackets().sendInterface(false,
				p.getInterfaceManager().hasRezizableScreen() ? 746 : 548,
				p.getInterfaceManager().hasRezizableScreen() ? 28 : 27,
				objmap.interid);
		p.sm(objmap.hint);

	}

	public static void showRiddle(Player p, Riddles riddle) {
		p.getPackets().sendInterface(false,
				p.getInterfaceManager().hasRezizableScreen() ? 746 : 548,
				p.getInterfaceManager().hasRezizableScreen() ? 28 : 27, 345);
		p.getPackets().sendIComponentText(345, 1, riddle.riddles[0]);
		p.getPackets().sendIComponentText(345, 2, riddle.riddles[1]);
		p.getPackets().sendIComponentText(345, 3, riddle.riddles[2]);
		p.getPackets().sendIComponentText(345, 4, riddle.riddles[3]);
		p.getPackets().sendIComponentText(345, 5, riddle.riddles[4]);
		p.getPackets().sendIComponentText(345, 6, riddle.riddles[5]);
		p.getPackets().sendIComponentText(345, 7, riddle.riddles[6]);
		p.getPackets().sendIComponentText(345, 8, riddle.riddles[7]);
	}

	public static void showMap(Player p, Maps map) {
		p.getPackets().sendInterface(false,
				p.getInterfaceManager().hasRezizableScreen() ? 746 : 548,
				p.getInterfaceManager().hasRezizableScreen() ? 28 : 27,
				map.interfaceId);
		p.sm(map.chat);
	}
		
}
