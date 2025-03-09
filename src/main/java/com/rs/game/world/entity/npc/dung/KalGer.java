package com.rs.game.world.entity.npc.dung;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceMovement;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Logger;
import com.rs.utility.Utils;

/***
 * 
 * @author `Leon on r-s
 * Fixed by Jae.
 *
 */


@SuppressWarnings("serial")
public class KalGer extends NPC {

	static Position SPLITTER_AREA = new Position(198, 5523, 0);
	public static Position KALGER_AREA = new Position(198, 5530, 0);
	static NPC splitter = new NPC(12844, SPLITTER_AREA, 0, false);
	WorldObject ladder = new WorldObject(3808, 10, 4, 207, 5523, 0);
	public static final List<Player> playersOn = Collections
			.synchronizedList(new ArrayList<Player>());
	
	/***
	 * - 12766 bare handed 0
	 * - 12781 sword 1
	 * - 12796 staff (chaotic) 2
	 * - 12811 another sword 3
	 * - 12826 Bow 4
	 * - 12841 Maul (also chaotic) 5
	 ***/
	
	public KalGer(int id, Position tile, int mapAreaNameHash,
                  boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, 0, canBeAttackFromOutOfArea, spawned);
		World.removeObject(ladder, false);
		animate(new Animation(14969));
		setRun(true);
		
		setNextPosition(KALGER_AREA);
		WorldTasksManager.schedule(new WorldTask() {
			public void run() {
				ArrayList<Entity> possibleTargets = getPossibleTargets();
				if (possibleTargets != null) {
					for (Entity entity : possibleTargets) {
						if (entity == null || entity.isDead()
								|| entity.isFinished()
								|| !entity.withinDistance(kalger, 10))
							continue;
						
						final Player inarea = (Player) entity;
						inarea.getPackets().sendResetCamera();
						inarea.getAppearence().transformIntoNPC(15674);
						inarea.setNextPosition(new Position(200, 5528, 0));
						inarea.getMusicsManager().playMusic(361);						
					}
				}
				StartScene();
				World.addNPC(splitter);
				stop();
			}
		}, 2);
	}
	
	public static void addPlayer(Player player) {
		if (playersOn.contains(player)) {
			 Logger.log("KalGer", "ERROR: DOUBLE ENTRY!");
			return;
		}
		playersOn.add(player);
	}

	public static void removePlayer(Player player) {
		playersOn.remove(player);
		cancelWar();
	}
	
	private static void cancelWar() {
		if (getPlayersCount() == 0)
			deleteNPCS();
	}

	public static int getPlayersCount() {
		return playersOn.size();
	}
	
	public static KalGer kalger;
	
	private static void deleteNPCS() {
		if(kalger != null) {
			kalger.finish();
			kalger = null;
		}
		
	}

	public ArrayList<Entity> getPossibleTargets() {
		ArrayList<Entity> possibleTarget = new ArrayList<Entity>(
				playersOn.size());
		for (Player player : playersOn) {
			if (player == null || player.isDead() || player.isFinished()
					|| !player.isRunning())
				continue;
			possibleTarget.add(player);
			
			  if (player.getFamiliar() != null &&
			  player.getFamiliar().isAgressive())
			  possibleTarget.add(player.getFamiliar());
			 
		}
		return possibleTarget;
	}
	
	public ArrayList<Entity> calculatePossibleTargets(Position current,
                                                      Position position, boolean northSouth) {
		ArrayList<Entity> list = new ArrayList<Entity>();
		for (Entity e : getPossibleTargets()) {
			if (e.inArea(current.getX(), current.getY(), position.getX()
					+ (northSouth ? 2 : 0), position.getY()
					+ (!northSouth ? 2 : 0))

					|| e.inArea(position.getX(), position.getY(),
							current.getX() + (northSouth ? 2 : 0),
							current.getY() + (!northSouth ? 2 : 0)))
				list.add(e);
		}
		return list;
	}

	private void ChooseWeapon() {
		WorldTasksManager.schedule(new WorldTask() {
			public void run() {
				ChooseWeapon();
		String location;
		final int id;
		switch(Utils.random(5)) {
		case 0:
			location = "bare";
			id = Type.BARE_HANDED.getId();
			break;
		case 1:
			location = "sword";
			id = Type.SWORDED.getId();
			break;
		case 2:
			location = "staff";
			id = Type.STAFFED.getId();
			break;
		case 3:
			location = "two";
			id = Type.TWO_HANDED.getId();
			break;
		case 4:
			location = "bow";
			id = Type.BOWED.getId();
			break;
		case 5:
			location = "maul";
			id = Type.MAULED.getId();
			break;
		default:
			location = "bare";
			id = Type.BARE_HANDED.getId();
			
			break;
		}
			Flytele(location);
			
			WorldTasksManager.schedule(new WorldTask() {
				public void run() {
					SwitchId(id);
					stop();
				}

				private void SwitchId(int id) {
				}
			}, 2);
			}
		}, 75);
		
	}
	public static enum Type {

		BARE_HANDED(12766, KALGER_AREA, 0, 14392),
		SWORDED(12781, new Position(194, 5520, 0), 1, 14374),
		STAFFED(12796, new Position(200, 5531, 0), 2, 14996),
		TWO_HANDED(12811, new Position(201, 5520, 0), 3, 14375),
		BOWED(12826, new Position(194, 5531, 0), 4, 14390),
		MAULED(12841, new Position(192, 5522, 0), 5, 14963);

		public final int id;
		private final int number;
		private final Position location;
		private int AttAnim;
		Type(int id, Position location, int nr, int AttAnim) {
			this.id = id;
			this.AttAnim = AttAnim;
			this.location = location;
			this.number = nr;
		}

		public int getId() {
			return id;
		}
		public int getNumber() {
			return number;
		}
		public int getAnim() {
			return AttAnim;
		}
		public Position getLocation() {
			return location;
		}
	}
	
	private void telecheck(final int stop) {
		if(targetset) {
		if(getCombat().getTarget() == null || getCombat().getTarget().isDead() || isDead()) {
			World.removeNPC(this);
			finish();
		}
		}
		WorldTasksManager.schedule(new WorldTask() {
			public void run() {
				if(stop == 1) {
					stop();
				} else 
				telecheck(0);
				if(getY() >= 5529 && getCombat().getTarget().getY() < 5529) {
					Flytele("south");
				} else if(getCombat().getTarget().getY() >= 5529 && getY() < 5529) {
					Flytele("north");
				}
			}
		}, 6);
		
	}
	
	private void Flytele(String string) {
		int anim = 14968;
		if(string.equalsIgnoreCase("south")) {
			final Entity target = getCombat().getTarget();
			animate(new Animation(14995));
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					setNextPosition(KALGER_AREA);
					setNextForceMovement(new ForceMovement(new Position(196, 5521, 0), 6, 0));
					stop();
				}
			}, 1);
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					setNextPosition(new Position(196, 5521, 0));
					animate(new Animation(-1));
					stop();
				}
			}, 7);
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					setTarget(target);
					setCantInteract(false);
					stop();
				}
			}, 8);
			
		} else if(string.equalsIgnoreCase("north")) {
			animate(new Animation(anim));
			setNextGraphics(new Graphics(2867));
			WorldTasksManager.schedule(new WorldTask() {
				public void run() {
					setNextPosition(new Position(197, 5530, 0));
					Hit();
					stop();
				}
			}, 1);
		} else if(string.equalsIgnoreCase("bare")) {
			animate(new Animation(anim));
			setNextGraphics(new Graphics(2867));
			WorldTasksManager.schedule(new WorldTask() {
				public void run() {
					setNextPosition(Type.BARE_HANDED.getLocation());
					Hit();
					stop();
				}
			}, 1);
		} else if(string.equalsIgnoreCase("sword")) {
			animate(new Animation(anim));
			setNextGraphics(new Graphics(2867));
			WorldTasksManager.schedule(new WorldTask() {
				public void run() {
					setNextPosition(Type.SWORDED.getLocation());
					Hit();
					stop();
				}
			}, 1);
		} else if(string.equalsIgnoreCase("staff")) {
			animate(new Animation(anim));
			setNextGraphics(new Graphics(2867));
			WorldTasksManager.schedule(new WorldTask() {
				public void run() {
					setNextPosition(Type.STAFFED.getLocation());
					Hit();
					stop();
				}
			}, 1);
		} else if(string.equalsIgnoreCase("two")) {
			animate(new Animation(anim));
			setNextGraphics(new Graphics(2867));
			WorldTasksManager.schedule(new WorldTask() {
				public void run() {
					setNextPosition(Type.TWO_HANDED.getLocation());
					Hit();
					stop();
				}
			}, 1);
		} else if(string.equalsIgnoreCase("bow")) {
			animate(new Animation(anim));
			setNextGraphics(new Graphics(2867));
			WorldTasksManager.schedule(new WorldTask() {
				public void run() {
					setNextPosition(Type.BOWED.getLocation());
					Hit();
					stop();
				}
			}, 1);
		} else if(string.equalsIgnoreCase("maul")) {
			animate(new Animation(anim));
			setNextGraphics(new Graphics(2867));
			WorldTasksManager.schedule(new WorldTask() {
				public void run() {
					setNextPosition(Type.MAULED.getLocation());
					Hit();
					stop();
				}
			}, 1);
		}
		
	}

	private void Hit() {
		ArrayList<Entity> possibleTargets = getPossibleTargets();
		if (possibleTargets != null) {
			for (Entity entity : possibleTargets) {
				if (entity == null || entity.isDead()
						|| entity.isFinished()
						|| !entity.withinDistance(kalger, 10))
					continue;
				
				final Player p = (Player) entity;
			//if(getLocation().withinDistance(p.getLocation(), 3)) {
				
				WorldTasksManager.schedule(new WorldTask() {
					public void run() {
						int damage = Utils.random(150) + Utils.random(50) + Utils.random(50) + Utils.random(50);
						p.applyHit(new Hit(p, damage, HitLook.REGULAR_DAMAGE));
						
						stop();
					}
				}, 1);
			//}
			}
		}
	}
	
	public void StartScene() {
    	splitter.animate(new Animation(14969));
    	splitter.setNextFaceEntity(this);
		WorldTasksManager.schedule(new WorldTask() {
			public void run() {
				splitter.setNextForceTalk(new ForceTalk("You summoned me, Master?"));
				playSound(3045, 2);
				scene1();
				stop();
			}
		}, 2);
		
	}
	private void scene1() {
		
    	setNextFaceEntity(splitter);
		WorldTasksManager.schedule(new WorldTask() {
			public void run() {
				setNextForceTalk(new ForceTalk("What news from your Watch?"));
				playSound(3000, 2);
				scene2();
				stop();
			}
		}, 3);
		
	}
	private void scene2() {

		WorldTasksManager.schedule(new WorldTask() {
			public void run() {
				splitter.setNextForceTalk(new ForceTalk("Master, I was defeated!"));
				playSound(3023, 2);
				scene3();
				stop();
			}
		}, 3);
		
	}
	private void scene3() {

		WorldTasksManager.schedule(new WorldTask() {
			public void run() {
				setNextForceTalk(new ForceTalk("YOU DARE FAIL ME?!"));
				playSound(3013, 2);
				animate(new Animation(14962));
				scene4();
				stop();
			}
		}, 3);
		
	}
	private void scene4() {

		WorldTasksManager.schedule(new WorldTask() {
			public void run() {
				splitter.animate(new Animation(14378));
				playSound(2986, 2);
				removesplitter();
				scene5();
				stop();
			}
		}, 1);
	}
	
	private void removesplitter() {

		WorldTasksManager.schedule(new WorldTask() {
			public void run() {
				splitter.finish();
				stop();
			}
		}, 7);
	}
	private boolean targetset = false;
	private void scene5() {

		WorldTasksManager.schedule(new WorldTask() {

			public void run() {
				setNextForceTalk(new ForceTalk("NOW IT'S YOUR TURN!"));
				playSound(3033, 2);
				for(Player target : playersOn) {
					  if(target.withinDistance(kalger, 1)) {
						target.getPackets().sendResetCamera();
						target.getAppearence().transformIntoNPC(-1);
						setTarget(playersOn.get(Utils.random(playersOn.size())));
						targetset = true;
						target.setNextPosition(new Position(199, 5521, 0));
						telecheck(0);
						ChooseWeapon();
						stop();
					}
				}
			}
		}, 10);
	}

	@Override
	public void sendDeath(Entity source) {
			telecheck(1);
			playSound(2997, 2);
			setNextForceTalk(new ForceTalk("IMPOSSIBLE!"));
			animate(new Animation(14378));
			WorldTasksManager.schedule(new WorldTask() {
				public void run() {
					drop();
					finish();
					World.spawnObject(ladder, false);
					for(Player inarea : playersOn) {
							inarea.getPackets().sendMusicEffect(415);
							inarea.getPackets().sendGameMessage("You recieved 5M in your bank as a reward for your braveness!");
							inarea.getPackets().sendGameMessage("The ladder at the south-east corner of this room gets you out of here!");
							inarea.getBank().addItem(995, 5000000, true);
					}
					stop();
				}
		}, 7);
				
	}



}