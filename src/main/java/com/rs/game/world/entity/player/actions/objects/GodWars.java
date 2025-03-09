package com.rs.game.world.entity.player.actions.objects;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.network.decoders.handlers.ObjectHandler;

public class GodWars {

	public static void JumpDown(Player player, final WorldObject object) {
		player.setNextPosition(new Position(2920, 5274, 0));
		player.getPackets().sendGameMessage("You tied the rope, and jumped down.");
	}

	public static void ArmadylSwing(Player player, final WorldObject object) {
		if (player.getX() == 2872 && player.getY() == 5280) {
			if (player.getSkills().getLevel(Skills.RANGE) >= 70) {
				player.setNextPosition(new Position(2872, 5272, 0));
				player.getPackets().sendGameMessage("You swing yourself over...");
			} else {
				player.getPackets().sendGameMessage("You must have 70 Ranged to enter the Armadyl GodWars.");
			}
		} else if (player.getX() == 2872 && player.getY() == 5272) {
			player.setNextPosition(new Position(2872, 5280, 0));
			player.getPackets().sendGameMessage("You swing yourself over...");
		}
	}

	public static void SaradominWall(Player player, final WorldObject object) {
		if (player.getSkills().getLevel(Skills.AGILITY) >= 70) {
			if (player.getX() == 2911) {
				player.setNextPosition(new Position(2915, 5298, 0));
				player.getPackets().sendGameMessage("You climb down the rocky wall...");
			}
			if (player.getX() == 2915) {
				player.setNextPosition(new Position(2911, 5298, 0));
				player.getPackets().sendGameMessage("You climb up the rocky wall...");
			}
			if (player.getY() == 5279) {
				player.setNextPosition(new Position(2920, 5275, 0));
				player.getPackets().sendGameMessage("You climb down the rocky wall...");
			}
			if (player.getY() == 5275) {
				player.setNextPosition(new Position(2920, 5279, 0));
				player.getPackets().sendGameMessage("You climb up the rocky wall...");
			}
		} else {
			player.getPackets().sendGameMessage("You must have 70 Agility to enter the Saradomin GodWars.");
		}
	}

	public static void SaradominLair(Player player, final WorldObject object) {
		if (World.saradomin == false) {
			if (player.saradomin >= 20) {
				if (player.getX() == 2923 && player.getY() == 5257) {
					player.setNextPosition(new Position(2923, 5256, 0));
					player.getPackets().sendGameMessage("You Enter Saradomins Lair... Prepare to fight!");
					player.saradomin = 0;
				}
			} else {
				player.getPackets().sendGameMessage("You must have a kill count of 20 to enter.");
			}
		} else {
			player.setNextPosition(new Position(2923, 5256, 0));
			player.getPackets().sendGameMessage("You Enter Saradomins Lair... Prepare to fight!");
		}
		if (player.getX() == 2923 && player.getY() == 5256) {
			player.setNextPosition(new Position(2923, 5257, 0));
			player.getPackets().sendGameMessage("You left Saradomins Lair...");
			player.armadyl = 0;
			player.bandos = 0;
			player.zamorak = 0;
			player.saradomin = 0;
		}
	}

	public static void BandosLair(Player player, final WorldObject object) {
		if (World.bandos == false) {
			if (player.bandos >= 20) {
				if (player.getX() == 2862 && player.getY() == 5357) {
					player.setNextPosition(new Position(2863, 5357, 0));
					player.getPackets().sendGameMessage("You Enter The Bandos Lair... Prepare to fight!");
					player.bandos = 0;
				}
			} else {
				player.getPackets().sendGameMessage("You must have a kill count of 20 to enter.");
			}
		} else {
			player.setNextPosition(new Position(2863, 5357, 0));
			player.getPackets().sendGameMessage("You Enter The Bandos Lair... Prepare to fight!");
		}
	}

	public static void ArmadylLair(Player player, final WorldObject object) {
		if (World.armadyl == false) {
			if (player.armadyl >= 20) {
				if (player.getX() == 2835 && player.getY() == 5294) {
					player.setNextPosition(new Position(2835, 5295, 0));
					player.getPackets().sendGameMessage("You enter Armadyls Lair... Prepare to fight!");
					player.armadyl = 0;
				}
			} else {
				player.getPackets().sendGameMessage("You must have a kill count of 20 to enter.");
			}
		} else {
			player.setNextPosition(new Position(2835, 5295, 0));
			player.getPackets().sendGameMessage("You enter Armadyls Lair... Prepare to fight!");
		}
	}

	public static void ZamorakLair(Player player, final WorldObject object) {
		if (World.zamorak == false) {
			if (player.zamorak >= 20) {
				player.setNextPosition(new Position(2925, 5332, 0));
				{
					player.setNextPosition(new Position(2925, 5332, 0));
					player.getPackets().sendGameMessage("You enter Zamoraks Lair... Prepare to fight!");
				}
			} else {
				player.getPackets().sendGameMessage("You must have a kill count of 20 to enter.");
			}
		} else {
			player.setNextPosition(new Position(2925, 5332, 0));
			player.getPackets().sendGameMessage("You enter Zamoraks Lair... Prepare to fight!");
		}
	}

	public static void ZamorakBridge(Player player, final WorldObject object) {
		if (player.getSkills().getLevel(Skills.HITPOINTS) >= 70) {
			if (player.getY() == 5338) {
				player.setNextPosition(new Position(2887, 5344, 0));
				player.getPackets().sendGameMessage("You cross the icey bridge...");
			}
			if (player.getY() == 5344) {
				player.setNextPosition(new Position(2887, 5338, 0));
				player.getPackets().sendGameMessage("You cross the icey bridge...");
			}
		} else {
			player.getPackets().sendGameMessage("You must have 70 Constitution to enter the Zamorak GodWars.");
		}
	}

	public static void BandosWall(Player player, final WorldObject object) {
		if (player.getSkills().getLevel(Skills.STRENGTH) >= 70 && player.getInventory().containsItemToolBelt(2347)) {
			if (player.getX() == 2851 && player.getY() == 5334) {
				player.animate(7002);
				player.getPackets().sendGameMessage("You smash the door with your hammer.", true);
				WorldTasksManager.schedule(new WorldTask() {

					@Override
					public void run() {					
						ObjectHandler.handleDoor(player, object, 1000);
						player.addWalkSteps(2850, 5334, 1, false);	
					}
					
				}, 2);
			}
			if (player.getX() == 2850 && player.getY() == 5334) {
				ObjectHandler.handleDoor(player, object, 1000);
				player.addWalkSteps(2851, 5334, 1, false);	
			}
		} else {
			player.getPackets().sendGameMessage("You must have at least 70 Strength, and a hammer to enter this door.");
		}
	}

	public static void NexDoor(Player player, final WorldObject object) {
		// if (player.getInventory().containsItem(20120, 1)) {
		// player.getInventory().deleteItem(20120, 1);
		// player.getPackets().sendGameMessage("You pass through the Zaros door.");
		player.setNextPosition(new Position(2887, 5277, 0));
		// } else {
		// player.getPackets().sendGameMessage("You need a frozen key in order to
		// enter.");
		// }
	}

	public static void NexDown(Player player, final WorldObject object) {
		player.useStairs(828, new Position(2855, 5222, 0), 1, 2);
	}

	public static void NexUp(Player player, final WorldObject object) {
		player.useStairs(828, new Position(2887, 5276, 0), 1, 2);
	}

	public static void NexSlide(Player player, final WorldObject object) {
		if (player.getX() == 2860) {
			player.addWalkSteps(2863, 5219, -1, false);
		} else if (player.getX() == 2863) {
			player.addWalkSteps(2860, 5219, -1, false);
		}
		player.animate(new Animation(839));
	}

	public static void NexDoor2(Player player, final WorldObject object) {
		ObjectHandler.handleDoor(player, object, 3000);
	}

	public static void HandleObject(Player player, final WorldObject object) {
		final int id = object.getId();
		if (id == 26444) {
			GodWars.JumpDown(player, object);
		}
		if (id == 75463 && player.getX() == 2872 && player.getY() == 5280) {
			GodWars.ArmadylSwing(player, object);
		}
		if (id == 75462) {
			GodWars.SaradominWall(player, object);
		}
		if (id == 26427) {
			GodWars.SaradominLair(player, object);
		}
		if (id == 26425) {
			GodWars.BandosLair(player, object);
		}
		if (id == 26426) {
			GodWars.ArmadylLair(player, object);
		}
		if (id == 26428) {
			GodWars.ZamorakLair(player, object);
		}
		if (id == 26439) {
			GodWars.ZamorakBridge(player, object);
		}
		if (id == 26384) {
			GodWars.BandosWall(player, object);
		}
		if (id == 75089) {
			GodWars.NexDoor(player, object);
		}
		if (id == 57256) {
			GodWars.NexDown(player, object);
		}
		if (id == 57260) {
			GodWars.NexUp(player, object);
		}
		if (id == 57234) {
			GodWars.NexSlide(player, object);
		}
		if (id == 57258) {
			GodWars.NexDoor2(player, object);
		}
	}

}
