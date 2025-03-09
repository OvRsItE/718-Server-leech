package com.rs.utility;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;

public class ObjectSpawning {

	/**
	 * Contains the custom npc spawning
	 */

	public static void spawnObjects() {
		World.spawnObject(new WorldObject(24452, 10, 0, 2611, 3088, 0), true);

		//Thieving stalls @ home

		World.spawnObject(new WorldObject(4874, 10, 2, 3092, 3487, 0), true);
		World.spawnObject(new WorldObject(4875, 10, 2, 3093, 3487, 0), true);
		World.spawnObject(new WorldObject(4876, 10, 2, 3094, 3487, 0), true);
		World.spawnObject(new WorldObject(4877, 10, 2, 3095, 3487, 0), true);
		World.spawnObject(new WorldObject(4878, 10, 2, 3096, 3487, 0), true);

		//World.spawnObject(new WorldObject(47120, 10, 1, 2600, 3105, 0), true); //Lunar Altar (Turmoil)

		World.spawnObject(new WorldObject(47758, 10, 1, 2601, 3104, 0), true); //Lunar Altar (Turmoil)
		World.spawnObject(new WorldObject(47761, 10, 1, 2602, 3105, 0), true); //Lunar Altar (Turmoil)
		World.spawnObject(new WorldObject(47762, 10, 1, 2601, 3106, 0), true); //Lunar Altar (Turmoil)
		World.spawnObject(new WorldObject(47760, 10, 1, 2600, 3105, 0), true); //Lunar Altar (Turmoil)
		World.spawnObject(new WorldObject(65857, 10, 1, 2600, 3104, 0), true); //Lunar Altar (Turmoil)
		//portal starter
		World.spawnObject(new WorldObject(7288, 10, 2, 2592, 5601, 1), true);
		World.spawnObject(new WorldObject(200, 10, 2, 2603, 3092, 0), true); // Lamp
		World.spawnObject(new WorldObject(200, 10, 2, 2603, 3095, 0), true); // Lamp
		World.spawnObject(new WorldObject(1187, 10, 2, 2606, 3088, 0), true); //
		World.spawnObject(new WorldObject(1187, 10, 2, 2611, 3098, 0), true); //
		World.spawnObject(new WorldObject(1187, 10, 2, 2602, 3092, 0), true); //
		World.spawnObject(new WorldObject(1187, 10, 2, 2604, 3100, 0), true); //
		/*World.spawnObject(new WorldObject(1187, 10, 2, 2601, 3092, 0), true); //
		World.spawnObject(new WorldObject(1187, 10, 2, 2600, 3092, 0), true); //
		World.spawnObject(new WorldObject(1187, 10, 2, 2600, 3093, 0), true); //
		World.spawnObject(new WorldObject(1187, 10, 2, 2600, 3094, 0), true); //
		World.spawnObject(new WorldObject(1187, 10, 2, 2600, 3095, 0), true); //
		World.spawnObject(new WorldObject(1187, 10, 2, 2599, 3094, 0), true); //
		World.spawnObject(new WorldObject(1187, 10, 2, 2599, 3095, 0), true); //
		World.spawnObject(new WorldObject(1187, 10, 2, 2598, 3095, 0), true); //
		World.spawnObject(new WorldObject(1187, 10, 2, 2603, 3091, 0), true); //
		World.spawnObject(new WorldObject(1187, 10, 2, 2602, 3091, 0), true); //
		World.spawnObject(new WorldObject(1187, 10, 2, 2601, 3091, 0), true); //
		World.spawnObject(new WorldObject(1187, 10, 2, 2600, 3091, 0), true); //
		World.spawnObject(new WorldObject(1187, 10, 2, 2603, 3093, 0), true); //
		 *///xpWell
		World.spawnObject(new WorldObject(43096, 10, 2, 2552, 3088, 0), true);

		World.spawnObject(new WorldObject(14860, 10, 2, 3061, 3883, 0), true);
		World.spawnObject(new WorldObject(14860, 10, 2, 3062, 3882, 0), true);
		World.spawnObject(new WorldObject(14860, 10, 2, 3063, 3880, 0), true);
		World.spawnObject(new WorldObject(14860, 10, 2, 3064, 3880, 0), true);
		World.spawnObject(new WorldObject(14860, 10, 2, 3065, 3880, 0), true);
		World.spawnObject(new WorldObject(14860, 10, 2, 3066, 3880, 0), true);
		World.spawnObject(new WorldObject(14860, 10, 2, 3067, 3880, 0), true);
		World.spawnObject(new WorldObject(14860, 10, 2, 3068, 3880, 0), true);
		World.spawnObject(new WorldObject(14860, 10, 2, 3069, 3880, 0), true);
		World.spawnObject(new WorldObject(14860, 10, 2, 3070, 3880, 0), true);
		World.spawnObject(new WorldObject(14860, 10, 2, 3058, 3885, 0), true);
		World.spawnObject(new WorldObject(14860, 10, 2, 3057, 3884, 0), true);
		World.spawnObject(new WorldObject(14860, 10, 2, 3056, 3883, 0), true);
        //iz
		World.spawnObject(new WorldObject(20607, 10, 1, 5748, 4797, 2), true);
		World.spawnObject(new WorldObject(37821, 10, 1, 5748, 4801, 2), true);
		World.spawnObject(new WorldObject(37821, 10, 1, 5748, 4803, 2), true);
		World.spawnObject(new WorldObject(37821, 10, 1, 5748, 4805, 2), true);
		World.spawnObject(new WorldObject(37821, 10, 1, 5748, 4805, 2), true);
		World.spawnObject(new WorldObject(20607, 10, 2, 5752, 4811, 2), true);
		World.spawnObject(new WorldObject(14859, 10, 1, 5755, 4811, 2), true);
		World.spawnObject(new WorldObject(14859, 10, 1, 5756, 4811, 2), true);
		World.spawnObject(new WorldObject(14859, 10, 1, 5757, 4811, 2), true);
		World.spawnObject(new WorldObject(14859, 10, 1, 5758, 4811, 2), true);
		World.spawnObject(new WorldObject(14859, 10, 1, 5759, 4811, 2), true);
		World.spawnObject(new WorldObject(20607, 10, 2, 5762, 4811, 2), true);
		World.spawnObject(new WorldObject(5999, 10, 1, 5765, 4810, 2), true);
		World.spawnObject(new WorldObject(5999, 10, 1, 5768, 4810, 2), true);
		World.spawnObject(new WorldObject(20607, 10, 3, 5772, 4808, 2), true);
		World.spawnObject(new WorldObject(61330, 10, 1, 5771, 4805, 2), true);
		World.spawnObject(new WorldObject(20607, 10, 3, 5772, 4801, 2), true);
		World.spawnObject(new WorldObject(6150, 10, 1, 5772, 4798, 2), true);
		World.spawnObject(new WorldObject(6150, 10, 1, 5772, 4799, 2), true);
		World.spawnObject(new WorldObject(20607, 10, 3, 5772, 4795, 2), true);
		World.spawnObject(new WorldObject(114, 10, 2, 5772, 4792, 2), true);
		World.spawnObject(new WorldObject(20607, 10, 4, 5765, 4789, 2), true);
		World.spawnObject(new WorldObject(28716, 10, 0, 5763, 4789, 2), true);
		World.spawnObject(new WorldObject(20607, 10, 0, 5760, 4789, 2), true);
		World.spawnObject(new WorldObject(30624, 10, 0, 5757, 4790, 2), true);
		//dark portal
		World.spawnObject(new WorldObject(46935, 10, 10, 3824, 4760, 0), true);
		//lz banks
		World.spawnObject(new WorldObject(782, 10, 2, 2833, 3866, 3), true);
		World.spawnObject(new WorldObject(782, 10, 2, 2832, 3866, 3), true);
		World.spawnObject(new WorldObject(782, 10, 2, 2831, 3866, 3), true);
		//lz magic trees
		World.spawnObject(new WorldObject(37823, 10, 2, 2824, 3851, 3), true);
		World.spawnObject(new WorldObject(37823, 10, 2, 2824, 3853, 3), true);
		World.spawnObject(new WorldObject(37823, 10, 2, 2824, 3856, 3), true);
		World.spawnObject(new WorldObject(37823, 10, 2, 2824, 3859, 3), true);
		World.spawnObject(new WorldObject(37823, 10, 2, 2824, 3862, 3), true);
		World.spawnObject(new WorldObject(37823, 10, 2, 2824, 3865, 3), true);
		//lz rune rock
		World.spawnObject(new WorldObject(14860, 10, 2, 2841, 3861, 3), true);
		World.spawnObject(new WorldObject(14860, 10, 2, 2841, 3860, 3), true);
		World.spawnObject(new WorldObject(14860, 10, 2, 2841, 3859, 3), true);
		World.spawnObject(new WorldObject(14860, 10, 2, 2841, 3858, 3), true);
		World.spawnObject(new WorldObject(14860, 10, 2, 2841, 3857, 3), true);
		World.spawnObject(new WorldObject(14860, 10, 2, 2841, 3856, 3), true);
		World.spawnObject(new WorldObject(14860, 10, 2, 2841, 3855, 3), true);
		World.spawnObject(new WorldObject(14860, 10, 2, 2841, 3854, 3), true);
		//lz furnace
		World.spawnObject(new WorldObject(6189, 10, 2, 2841, 3866, 3), true);
		//lz anvil
		World.spawnObject(new WorldObject(2782, 10, 2, 2841, 3864, 3), true);
		//lz stalls
		World.spawnObject(new WorldObject(4874, 10, -2, 2826, 3867, 3), true);
		World.spawnObject(new WorldObject(4875, 10, -2, 2827, 3867, 3), true);
		World.spawnObject(new WorldObject(4876, 10, -2, 2828, 3867, 3), true);
		World.spawnObject(new WorldObject(4877, 10, -2, 2829, 3867, 3), true);
		World.spawnObject(new WorldObject(4878, 10, -2, 2830, 3867, 3), true);
		//lz noticeboard
		World.spawnObject(new WorldObject(40760, 10, -2, 2832, 3858, 3), true);
		//lz rune ess
		World.spawnObject(new WorldObject(2491, 10, -2, 2834, 3867, 3), true);
		/*
		 * World.spawnObject(new WorldObject(-1, 10, -2, 2772, 3688, 0), false);
		 * World.spawnObject(new WorldObject(-1, 10, -2, 2768, 3690, 0), false);
		 * World.spawnObject(new WorldObject(-1, 10, -2, 2768, 3687, 0), false);
		 * World.spawnObject(new WorldObject(-1, 10, -2, 2761, 3686, 0), false);
		 * World.spawnObject(new WorldObject(-1, 10, -2, 2760, 3685, 0), false);
		 * World.removeObject(new WorldObject(-1, 10, -2, 2760, 3687, 0), false);
		 * World.removeObject(new WorldObject(-1, 10, -2, 2762, 3687, 0), false);
		 * World.removeObject(new WorldObject(-1, 10, -2, 2762, 3686, 0), false);
		 * World.removeObject(new WorldObject(-1, 10, -2, 2763, 3684, 0), false);
		 * World.removeObject(new WorldObject(-1, 10, -2, 2772, 3688, 0), false);
		 * World.removeObject(new WorldObject(5897, 10, -2, 2766, 3686, 0), false);
		 */

		//World.spawnObject(new WorldObject(123, 10, 0, ));
//		World.spawnObject(new WorldObject(2563, 10, 0, 3618, 2658, 0)); //Comp stand
		World.spawnObject(new WorldObject(231989, 10, 1, 2638, 3694, 0)); //Vorkath boat in relleka
//		World.spawnObject(new WorldObject(114, 10, 0, 3628, 2675, 0)); //Skilling zone range

		//Trees at home
//		World.spawnObject(new WorldObject(54778, 10, 0, 3615, 2649, 0));
//		World.spawnObject(new WorldObject(54779, 10, 0, 3615, 2649, 1));
//
//		World.spawnObject(new WorldObject(54778, 10, 1, 3619, 2641, 0));
//		World.spawnObject(new WorldObject(54779, 10, 1, 3619, 2641, 1));
//
//		World.spawnObject(new WorldObject(54778, 10, 2, 3619, 2661, 0));
//		World.spawnObject(new WorldObject(54779, 10, 2, 3619, 2661, 1));
//
//		World.spawnObject(new WorldObject(54778, 10, 1, 3620, 2666, 0));
//		World.spawnObject(new WorldObject(54779, 10, 1, 3620, 2666, 1));
//
//		World.spawnObject(new WorldObject(54778, 10, 0, 3624, 2668, 0));
//		World.spawnObject(new WorldObject(54779, 10, 0, 3624, 2668, 1));
//
//		World.spawnObject(new WorldObject(54778, 10, 0, 3628, 2666, 0));
//		World.spawnObject(new WorldObject(54779, 10, 0, 3628, 2666, 1));
//
//		World.spawnObject(new WorldObject(54778, 10, 2, 3623, 2672, 0));
//		World.spawnObject(new WorldObject(54779, 10, 2, 3623, 2672, 1));
//
//		World.spawnObject(new WorldObject(54778, 10, 3, 3628, 2679, 0));
//		World.spawnObject(new WorldObject(54779, 10, 3, 3628, 2679, 1));
//
//		World.spawnObject(new WorldObject(54778, 10, 5, 3633, 2680, 0));
//		World.spawnObject(new WorldObject(54779, 10, 5, 3633, 2680, 1));
//
//		World.spawnObject(new WorldObject(54778, 10, 1, 3637, 2675, 0));
//		World.spawnObject(new WorldObject(54779, 10, 1, 3637, 2675, 1));
//
//		World.spawnObject(new WorldObject(54778, 10, 0, 3635, 2666, 0));
//		World.spawnObject(new WorldObject(54779, 10, 0, 3635, 2666, 1));
//
//		World.spawnObject(new WorldObject(54778, 10, 3, 3639, 2663, 0));
//		World.spawnObject(new WorldObject(54779, 10, 3, 3639, 2663, 1));
//
//		World.spawnObject(new WorldObject(54778, 10, 0, 3636, 2659, 0)); //
//		World.spawnObject(new WorldObject(54779, 10, 0, 3636, 2659, 1));
//
//		World.spawnObject(new WorldObject(54778, 10, 1, 3637, 2649, 0));
//		World.spawnObject(new WorldObject(54779, 10, 1, 3637, 2649, 1));
//
//		World.spawnObject(new WorldObject(54778, 10, 0, 3634, 2645, 0));
//		World.spawnObject(new WorldObject(54779, 10, 0, 3634, 2645, 1));
//
//		World.spawnObject(new WorldObject(54778, 10, 4, 3633, 2642, 0));
//		World.spawnObject(new WorldObject(54779, 10, 4, 3633, 2642, 1));

//		//Big ass christmas tree
//		World.spawnObject(new WorldObject(65996, 10, 0, 3635, 2653, 0));
//
//		//Presents
//		World.spawnObject(new WorldObject(47762, 10, 3, 3635, 2652, 0));
//		World.spawnObject(new WorldObject(47762, 10, 0, 3634, 2655, 0));
//		World.spawnObject(new WorldObject(47762, 10, 1, 3637, 2657, 0));
//		World.spawnObject(new WorldObject(65910, 10, 3, 3634, 2653, 0));
//		World.spawnObject(new WorldObject(65910, 10, 0, 3635, 2657, 0));
//		World.spawnObject(new WorldObject(66005, 10, 0, 3635, 2654, 0));
//		World.spawnObject(new WorldObject(66005, 10, 0, 3635, 2656, 0));
//
//		//Snow
//		World.spawnObject(new WorldObject(3701, 10, 0, 3624, 2650, 3));
//		World.spawnObject(new WorldObject(3701, 10, 0, 3613, 2641, 3));
//		World.spawnObject(new WorldObject(3701, 10, 0, 3613, 2651, 3));
//		World.spawnObject(new WorldObject(3701, 10, 0, 3622, 2660, 3));
//		World.spawnObject(new WorldObject(3701, 10, 0, 3632, 2660, 3));
//		World.spawnObject(new WorldObject(3701, 10, 0, 3632, 2638, 3));
//		World.spawnObject(new WorldObject(3701, 10, 0, 3623, 2636, 3));
//
//		//Caged santa
//		World.spawnObject(new WorldObject(47857, 10, 1, 3635, 2650, 0));

		//Demonic gorilla gate
		World.spawnObject(new WorldObject(228656, 0, 3, 2435, 3520, 0));

		//Rouges chest
		World.spawnObject(new WorldObject(226757, 10, 2, 3282, 3946, 0));
		World.spawnObject(new WorldObject(226757, 10, 2, 3287, 3946, 0));
		World.spawnObject(new WorldObject(226757, 10, 2, 3298, 3940, 0));
		World.spawnObject(new WorldObject(55328, 10, 3, 3611, 2650, 0));
		World.spawnObject(new WorldObject(172, 10, 3, 3611, 2652, 0));
		World.spawnObject(new WorldObject(12355, 10, 1, 3611, 2646, 0));
		World.spawnObject(new WorldObject(46933, 10, 1, 3608, 2654, 0));

		//stalls at home
		World.spawnObject(new WorldObject(4874, 10, 0, 3618, 2661, 0));
		World.spawnObject(new WorldObject(4875, 10, 0, 3619, 2661, 0));
		World.spawnObject(new WorldObject(4876, 10, 0, 3620, 2661, 0));
		World.spawnObject(new WorldObject(4877, 10, 0, 3621, 2661, 0));
		World.spawnObject(new WorldObject(4878, 10, 0, 3622, 2661, 0));
		World.spawnObject(new WorldObject(34385, 10, 0, 3623, 2661, 0));


		//workbench
		World.spawnObject(new WorldObject(80321, 10, 2, 3612, 2644, 0));

		//house portal
		World.spawnObject(new WorldObject(15482, 10, 0, 3619, 2655, 0));
		World.spawnObject(new WorldObject(13705, 10, 0, 3617, 2650, 0));
		World.spawnObject(new WorldObject(21250, 10, 1, 3610, 2654, 0));
        //home mine
		World.spawnObject(new WorldObject(11959, 10, 0, 3634, 2673, 0));
		World.spawnObject(new WorldObject(11959, 10, 0, 3633, 2673, 0));
		World.spawnObject(new WorldObject(11961, 10, 0, 3632, 2675, 0));
		World.spawnObject(new WorldObject(11961, 10, 0, 3631, 2676, 0));
		World.spawnObject(new WorldObject(5770, 10, 0, 3632, 2678, 0));
		World.spawnObject(new WorldObject(5770, 10, 0, 3633, 2679, 0));
		World.spawnObject(new WorldObject(5770, 10, 0, 3634, 2679, 0));
		World.spawnObject(new WorldObject(11956, 10, 0, 3636, 2680, 0));
		World.spawnObject(new WorldObject(11956, 10, 0, 3637, 2679, 0));
		World.spawnObject(new WorldObject(5784, 10, 0, 3639, 2678, 0));
		World.spawnObject(new WorldObject(5784, 10, 0, 3640, 2677, 0));
		World.spawnObject(new WorldObject(5783, 10, 0, 3640, 2675, 0));
		World.spawnObject(new WorldObject(5783, 10, 0, 3639, 2674, 0));

		//underwater entrace
		World.spawnObject(new WorldObject(70356, 10, 2, 3623, 2625, 0));

		//well
		World.spawnObject(new WorldObject(70436, 10, 0, 3615, 2654, 0));
		//slayer chests
		World.spawnObject(new WorldObject(70435, 10, 3, 3602, 2657, 0));
		World.spawnObject(new WorldObject(234660, 10, 0, 3602, 2658, 0));
		World.spawnObject(new WorldObject(234829, 10, 3, 3021, 3954, 0));

		//donator hub
		World.spawnObject(new WorldObject(210822, 10, 0, 1327, 2524, 0));
		World.spawnObject(new WorldObject(210822, 10, 0, 1328, 2520, 0));
		World.spawnObject(new WorldObject(210822, 10, 0, 1331, 2526, 0));
		World.spawnObject(new WorldObject(210822, 10, 0, 1333, 2524, 0));
		World.spawnObject(new WorldObject(210834, 10, 0, 1334, 2521, 0));
		World.spawnObject(new WorldObject(210834, 10, 0, 1333, 2518, 0));
		World.spawnObject(new WorldObject(210834, 10, 0, 1330, 2519, 0));
		World.spawnObject(new WorldObject(5783, 10, 0, 1316, 2512, 0));
		World.spawnObject(new WorldObject(5783, 10, 0, 1317, 2512, 0));
		World.spawnObject(new WorldObject(5783, 10, 0, 1318, 2511, 0));
		World.spawnObject(new WorldObject(33078, 10, 0, 1319, 2509, 0));
		World.spawnObject(new WorldObject(33078, 10, 0, 1319, 2508, 0));
		World.spawnObject(new WorldObject(33078, 10, 0, 1319, 2507, 0));
		World.spawnObject(new WorldObject(33078, 10, 0, 1318, 2507, 0));
		World.spawnObject(new WorldObject(32440, 10, 0, 1316, 2507, 0));
		World.spawnObject(new WorldObject(32440, 10, 0, 1315, 2507, 0));
		World.spawnObject(new WorldObject(32440, 10, 0, 1314, 2508, 0));
		World.spawnObject(new WorldObject(14233, 10, 0, 1292, 2519, 0));
		World.spawnObject(new WorldObject(14233, 10, 0, 1295, 2521, 0));
		World.spawnObject(new WorldObject(14233, 10, 0, 1295, 2526, 0));
		World.spawnObject(new WorldObject(14233, 10, 0, 1291, 2525, 0));
		World.spawnObject(new WorldObject(14233, 10, 0, 1291, 2527, 0));
		World.spawnObject(new WorldObject(14233, 10, 0, 1296, 2532, 0));
		World.spawnObject(new WorldObject(14233, 10, 0, 1295, 2533, 0));


		//dungeon object removing
//		World.spawnObject(new WorldObject(70197, 10, 0, 3397, 9233, 1));
//		World.spawnObject(new WorldObject(70197, 10, 0, 3399, 9233, 1));
//		World.spawnObject(new WorldObject(70197, 10, 0, 3384, 9230, 1));
//		//Alter of the Occult
//		World.spawnObject(new WorldObject(WorldObject.asOSRS(31923), 10, 2, 3615, 2653, 0));

		//The overseer
		World.spawnObject(new WorldObject(WorldObject.asOSRS(27042), 10, 2, 3038, 4777, 0)); //ass end
		World.spawnObject(new WorldObject(WorldObject.asOSRS(27044), 10, 0, 3037, 4780, 0)); //head end
	}

	/**
	 * The NPC classes.
	 */
	private static final Map<Integer, Class<?>> CUSTOM_NPCS = new HashMap<Integer, Class<?>>();

	public static void npcSpawn() {
		int size = 0;
		boolean ignore = false;
		try {
			for (String string : FileUtilities
					.readFile("data/npcs/spawns.txt")) {
				if (string.startsWith("//") || string.equals("")) {
					continue;
				}
				if (string.contains("/*")) {
					ignore = true;
					continue;
				}
				if (ignore) {
					if (string.contains("*/")) {
						ignore = false;
					}
					continue;
				}
				String[] spawn = string.split(" ");
				@SuppressWarnings("unused")
				int id = Integer.parseInt(spawn[0]), x = Integer
				.parseInt(spawn[1]), y = Integer.parseInt(spawn[2]), z = Integer
				.parseInt(spawn[3]), faceDir = Integer
				.parseInt(spawn[4]);
				NPC npc = null;
				Class<?> npcHandler = CUSTOM_NPCS.get(id);
				if (npcHandler == null) {
					npc = new NPC(id, new Position(x, y, z), -1, 0, true, false);
				} else {
					npc = (NPC) npcHandler.getConstructor(int.class)
							.newInstance(id);
				}
				if (npc != null) {
					Position spawnLoc = new Position(x, y, z);
					npc.setLocation(spawnLoc);
					World.spawnNPC(npc.getId(), spawnLoc, -1, 0, true, false);
					size++;
				}
			}
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			e1.printStackTrace();
		} catch (NoSuchMethodException e1) {
			e1.printStackTrace();
		}
		System.err.println("Loaded " + size + " custom npc spawns!");
	}

}