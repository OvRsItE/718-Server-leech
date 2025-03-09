package com.rs.game.world.entity.player.content.titleHandler;

import java.io.Serializable;

import com.rs.cache.loaders.ClientScriptMap;
import com.rs.game.world.entity.player.Player;

import lombok.Getter;
import lombok.Setter;
/**
 * Handles TItles and Titles Reqs.
 * @author Delusional
 * Jan 13, 2019
 */
public class TitleManager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2622136133165019731L;

	
	@Getter @Setter static private transient Player player;
	
	public enum Titles {

		JUNIOR_CADET(1, "Junior Cadet ", "AA44AA", null, false), 
		SERJEANT(2, "Serjeant ", "AA44AA", null, false),
		COMMANDER(3, "Commander ", "AA44AA", null, false), 
		WAR_CHIEF(4, "War-chief ", "AA44AA", null, false),
		SIR(5, "Sir ", "C86400", null, false), 
		LORD(6, "Lord ", "C86400", null, false),
		DUDERINO(7, "Duderino ", "C86400", null, false), 
		LIONHEART(8, "Lionheart ", "C86400", null, false),
		HELLRAISER(9, "Hellraiser ", "C86400", null, false), 
		CRUSADER(10, "Crusader ", "C86400", null, false),
		DESPERADO(11, "Desperado ", "C86400", null, false), 
		BARON(12, "Baron ", "C86400", null, false),
		COUNT(13, "Count ", "C86400", null, false), 
		OVERLORD(14, "Overlord ", "C86400", null, false),
		BANDITO(15, "Bandito ", "C86400", null, false), 
		DUKE(16, "Duke ", "C86400", null, false),
		KING(17, "King ", "C86400", null, false), 
		BIG_CHEESE_(18, "Big Cheese ", "C86400", null, false),
		BIGWIG_(19, "Bigwig ", "C86400", null, false), 
		WUNDERKIND(20, "Wunderkind ", "C86400", null, false),
		VYRELING(21, "Vyreling ", "466AFA", null, false), 
		VYRE_GRUNT(22, "Vyre Grunt ", "7D3FEC", null, false),
		VYREWATCH(23, "Vyrewatch ", "6C0B2B", null, false), 
		VYRELORD(24, "Vyrelord ", "C12006", null, false),
		YT_HAAR(25, "Yt?Haar ", "C12006", null, false), 
		EMPEROR(26, "Emperor ", "C86400", null, false),
		PRINCE(27, "Prince ", "C86400", null, false), 
		WITCH_KING(28, "Witch king ", "C86400", null, false),
		ARCHON(29, "Archon ", "C86400", null, false), 
		JUSTICIAR(30, "Justiciar ", "C86400", null, false),
		THE_AWESOME(31, "The Awesome ", "C86400", null, false),
		THE_MAGNIFICENT(32, " the magnificent", "C86400", null, true),
		THE_UNDEFEATED(33, " the undefeated", "C86400", null, true),
		THE_STRANGE(34, " the strange", "C86400", null, true), 
		THE_DIVINE(35, " the divine", "C86400", null, true),
		THE_FALLEN(36, " the fallen", "C86400", null, true), 
		THE_WARRIOR(37, " the warrior", "C86400", null, true),
		THE_REAL_(38, "The Real ", "C86400", null, false), 
		COWARDLY_(39, "Cowardly ", "AA44AA", null, false),
		THE_REDUNDANT(40, " the Redundant", "AA44AA", null, true),
		EVERYONE_ATTACK(41, "Everyone attack ", "AA44AA", null, false), 
		SMELLY(42, "Smelly ", "AA44AA", null, false),
		THE_IDIOT(43, " the Idiot", "AA44AA", null, true), 
		SIR_LAME(44, "Sir Lame ", "AA44AA", null, false),
		THE_FLAMBOYANT(45, " the Flamboyant", "AA44AA", null, true), 
		WEAKLING(46, "Weakling ", "AA44AA", null, false),
		WAS_PUNISHED(47, " was punished", "AA44AA", null, true), 
		LOST(48, " lost", "AA44AA", null, true),
		YOU_FAIL(49, " ...you fail", "AA44AA", null, true), 
		NO_MATES_(50, "No-mates ", "AA44AA", null, false),
		ATE_DIRT(51, " ate dirt", "AA44AA", null, true), 
		DELUSIONAL_(52, "Delusional ", "AA44AA", null, false),
		THE_RESPAWNER(53, " the Respawner", "AA44AA", null, true), 
		CUTIE_PIE_(54, "Cutie-pie ", "AA44AA", null, false),
		THE_FAIL_MAGNET(55, " the Fail Magnet", "AA44AA", null, true),
		WAS_TERMINATED(56, " was terminated", "AA44AA", null, true), 
		LAZY(57, "Lazy ", "AA44AA", null, false),
		WHO(58, "? Who?", "AA44AA", null, true),
		FINAl_BOSS(159, " Final Boss", "990B26", null, true, Requirements.FINAl_BOSS),
		OWNER(160, " Owner", "EEFF00", null , true, Requirements.IS_OWNER);
		
		private int id;
		private String title, color, shade;
		private boolean after;
		private Requirements req;

		Titles(int id, String title, String color, String shade, boolean after) {
			this.id = id;
			this.title = title;
			this.color = color;
			this.shade = shade;
			this.after = after;
		}
		
		Titles(int id, String title, String color, String shade, boolean after, Requirements req) {
			this.id = id;
			this.title = title;
			this.color = color;
			this.shade = shade;
			this.after = after;
			this.req = req;
		}
		
		public int getId() {
			return id;
		}

		public String getTitle() {
			return title;
		}

		public String getColor() {
			return color;
		}

		public String getShade() {
			return shade;
		}

		public boolean goesAfter() {
			return after;
		}

	}

	public static Titles getTitle(int id) {
		for (Titles t : Titles.values()) {
			if (t.getId() == id) {
				return t;
			}
		}
		return null;
	}

	public boolean goesAfterName(int titleId) {
		Titles title = getTitle(titleId);

		if (title == null)
			return false;

		return title.goesAfter();
	}

	public String getFullTitle(boolean male, int titleId, Player player) {
		if (titleId < 60) {
			return ClientScriptMap.getMap(male ? 1093 : 3872).getStringValue(titleId);
		}
		
		Titles title = getTitle(titleId);
		
		if (title == null)
			return "";
		
		
		if (title.req != null && title.req.hasReq() == false) {
			player.sm("You do not meet the requirements for that title");
			return "";
		}
		
		String color = title.getColor() == null ? "" : "<col=" + title.getColor() + ">";
		String shade = title.getShade() == null ? "" : "<shad=" + title.getShade() + ">";
		String pref1 = title.getColor() == null ? "" : "</col>";
		String pref2 = title.getShade() == null ? "" : "</shad>";
		String t = color + shade + title.getTitle() + pref1 + pref2;
		return t;
	}
	
	
	
	public enum Requirements {

		FINAl_BOSS() {

			@Override
			public boolean hasReq() {
				return getPlayer().getKillcountManager().hasKillcount(3200,100) && getPlayer().getKillcountManager().hasKillcount(6260,100)
			&& getPlayer().getKillcountManager().hasKillcount(6247,100) && getPlayer().getKillcountManager().hasKillcount(6203,100)
			&& getPlayer().getKillcountManager().hasKillcount(6222,100) && getPlayer().getKillcountManager().hasKillcount(13450,100)
			&& getPlayer().getKillcountManager().hasKillcount(15454,100) && getPlayer().getKillcountManager().hasKillcount(15506,100) 
			&& getPlayer().getKillcountManager().hasKillcount(15507,100) && getPlayer().getKillcountManager().hasKillcount(50,100) 
			&& getPlayer().getKillcountManager().hasKillcount(1160,100) && getPlayer().getKillcountManager().hasKillcount(8133,100) 
			&& getPlayer().getKillcountManager().hasKillcount(8349,100) && getPlayer().getKillcountManager().hasKillcount(8350,100)
			&& getPlayer().getKillcountManager().hasKillcount(2881,100) && getPlayer().getKillcountManager().hasKillcount(2882,100) 
			&& getPlayer().getKillcountManager().hasKillcount(2883,100) && getPlayer().getKillcountManager().hasKillcount(27147,100)
			&& getPlayer().getKillcountManager().hasKillcount(27148,100) && getPlayer().getKillcountManager().hasKillcount(27149,100) 
			&& getPlayer().getKillcountManager().hasKillcount(28061,100) && getPlayer().getKillcountManager().hasKillcount(16641,100)
			&& getPlayer().getKillcountManager().hasKillcount(16642,100) && getPlayer().getKillcountManager().hasKillcount(16643,100)
			&& getPlayer().getKillcountManager().hasKillcount(3334,100) && getPlayer().getKillcountManager().hasKillcount(25862,100)
			&& getPlayer().getKillcountManager().hasKillcount(7133,100) && getPlayer().getKillcountManager().hasKillcount(16010,100)
			&& getPlayer().getKillcountManager().hasKillcount(16071,100) && getPlayer().getKillcountManager().hasKillcount(16080,100)
			&& getPlayer().getKillcountManager().hasKillcount(16081,100) && getPlayer().getKillcountManager().hasKillcount(16009,100)
			&& getPlayer().getKillcountManager().hasKillcount(26503,100) && getPlayer().getKillcountManager().hasKillcount(16008,100)
			&& getPlayer().getKillcountManager().hasKillcount(27286,100) && getPlayer().getKillcountManager().hasKillcount(16011,100)
			&& getPlayer().getKillcountManager().hasKillcount(26612,100) && getPlayer().getKillcountManager().hasKillcount(26619,100)
			&& getPlayer().getKillcountManager().hasKillcount(26618,100)
			;
			}
		},

		IS_OWNER() {
			@Override
			public boolean hasReq() {
				return getPlayer().isOwner();
			}
		}

		;

		public boolean hasReq() {
			return false;
		}
	}
		
}
