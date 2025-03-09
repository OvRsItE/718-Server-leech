package com.rs.game.world.entity.player.actions;
			 
import com.rs.Constants;
import com.rs.game.map.Position;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.Magic;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;
			 
public class HomeTeleport extends Action {
			 
		private final int HOME_ANIMATION = 16385, HOME_GRAPHIC = 3017;
		public static final Position LUMBRIDGE_LODE_STONE = new Position(3233, 3221, 0),
				BURTHORPE_LODE_STONE = new Position(2899, 3544, 0),
				LUNAR_ISLE_LODE_STONE = new Position(2085, 3914, 0),
			    BANDIT_CAMP_LODE_STONE = new Position(3214, 2954, 0),
			    TAVERLY_LODE_STONE = new Position(2878, 3442, 0),
			    ALKARID_LODE_STONE = new Position(3297, 3184, 0),
			    VARROCK_LODE_STONE = new Position(3214, 3376, 0),
			    EDGEVILLE_LODE_STONE = new Position(3067, 3505, 0),
			    FALADOR_LODE_STONE = new Position(2967, 3403, 0),
			    PORT_SARIM_LODE_STONE = new Position(3011, 3215, 0),
			    DRAYNOR_VILLAGE_LODE_STONE = new Position(3105, 3298, 0),
			    ARDOUGNE_LODE_STONE = new Position(2634, 3348, 0),
			    CATHERBAY_LODE_STONE = new Position(2831, 3451, 0),
			    YANILLE_LODE_STONE = new Position(2529, 3094, 0),
			    SEERS_VILLAGE_LODE_STONE = new Position(2689, 3482, 0),
			    HOME_TELEPORT = Constants.START_PLAYER_LOCATION;
			 
			    private int currentTime;
			    private Position tile;
			 
			    public HomeTeleport(Position tile) {
			        this.tile = tile;
			    }
			 
			    @Override
			    public boolean start(final Player player) {
			        if (!player.getControlerManager().processMagicTeleport(tile))
			            return false;
			        return process(player);
			    }
			 
			    @Override
			    public int processWithDelay(Player player) {
			        if (currentTime++ == 0) {
			            if (player.Demon == true && player.Ass == false && player.Gnome == false
			                && player.SuperJump == false &&  player.Pony == false) {
			                player.animate(new Animation(17108));
			                player.setNextGraphics(new Graphics(3224));
			                player.setNextGraphics(new Graphics(3225));
			            }
			            if (player.Demon == false && player.Ass == false && player.Gnome == false
			                    && player.SuperJump == false &&  player.Pony == true) {
			            player.animate(new Animation(17106));
			            player.setNextGraphics(new Graphics(3223));
			        }
			            if (player.Demon == false && player.Ass == true && player.Gnome == false
			                    && player.SuperJump == false &&  player.Pony == false) {
			                player.animate(new Animation(17074));
			                player.setNextGraphics(new Graphics(3215));
			        }
			            if (player.Demon == false && player.Ass == false && player.Gnome == true
			                    && player.SuperJump == false &&  player.Pony == false) {
			                player.animate(new Animation(17191));
			                player.setNextGraphics(new Graphics(3254));
			            }
			            if (player.Demon == false && player.Ass == false && player.Gnome == false
			                    && player.SuperJump == true &&  player.Pony == false) {
			                player.animate(new Animation(17317));
			                player.setNextGraphics(new Graphics(3311));
			                player.setNextGraphics(new Graphics(3310));
			                player.setNextGraphics(new Graphics(3309));
			            }
			            if (player.Demon == false && player.Ass == false && player.Gnome == false
			                && player.SuperJump == false &&  player.Pony == false) {
			            player.animate(new Animation(HOME_ANIMATION));
			            player.setNextGraphics(new Graphics(HOME_GRAPHIC));
			            }
			        } else if (currentTime == 18) {
			            player.setNextPosition(tile.transform(0, 1, 0));
			            player.getControlerManager().magicTeleported(Magic.MAGIC_TELEPORT);
			            if (player.getControlerManager().getControler() == null)
			                Magic.teleControlersCheck(player, tile);
			            player.setNextFacePosition(new Position(tile.getX(), tile.getY(),
			                    tile.getZ()));
			            player.setDirection(6);
			        } else if (currentTime == 19) {
			            player.setNextGraphics(new Graphics(HOME_GRAPHIC + 1));
			            player.animate(new Animation(HOME_ANIMATION + 1));
			        } else if (currentTime == 24) {
			            player.setNextPosition(tile);
			            player.animate(new Animation(16393));
			        } else if (currentTime == 25)
			            return -1;
			        return 0;
			    }
			 
			    @Override
			    public boolean process(Player player) {
			        if (player.getAttackedByDelay() + 10000 > Utils.currentTimeMillis()) {
			            player.getPackets()
			                    .sendGameMessage(
			                            "You can't home teleport until 10 seconds after the end of combat.");
			            return false;
			        }
			        return true;
			    }
			 
			    @Override
			    public void stop(Player player) {
			        player.animate(new Animation(-1));
			        player.setNextGraphics(new Graphics(-1));
			    }
			 
			}