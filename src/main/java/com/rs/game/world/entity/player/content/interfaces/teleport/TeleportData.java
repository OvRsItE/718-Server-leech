package com.rs.game.world.entity.player.content.interfaces.teleport;

import com.rs.game.map.Position;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Paolo, Discord Shnek#6969
 * 22/01/2019
 */
public enum TeleportData {
       SKILLING(new TeleportLocation[]  {
               new TeleportLocation("Dungeoneering", new Position(3616,2657,0)) ,
               new TeleportLocation("Ardougne thieving", new Position(2662, 3303,0)),
               new TeleportLocation("Seers village woodcutting", new Position(2725, 3491,0)),
               new TeleportLocation("Gnome Agility", new Position(2468, 3437,0)),
               new TeleportLocation("Barbarian Agility", new Position(2552, 3557,0))
        }),
        BOSSES(new TeleportLocation[]  {
                new TeleportLocation("General Graardor", new Position(2854, 5334)) ,
                new TeleportLocation("Kree'arra", new Position(2872, 5285)) ,
                new TeleportLocation("Commander Zilyana", new Position(2909, 5300)) ,
                new TeleportLocation("K'ril Tsutsaroth", new Position(2887, 5334)) ,
                new TeleportLocation("Queen Black Dragon", new Position(1197, 6499)) ,
                new TeleportLocation("King Black Dragon", new Position(3067, 10254)) ,
                new TeleportLocation("Kalphite Queen", new Position(3228, 3106)) ,
                new TeleportLocation("The Alchemist", new Position(3825, 4768)) ,
                new TeleportLocation("Dagannoth Kings", new Position(1914, 4368)) ,
                new TeleportLocation("Kraken Cove", new Position(3043, 2500)) ,
                new TeleportLocation("Zulrah", new Position(2200, 3056)) ,
                new TeleportLocation("Vorkath", new Position(2641, 3697)) ,
                new TeleportLocation("Cerberus", new Position(1310, 1251)) ,
        }),
        MONSTERS(new TeleportLocation[]  {
                new TeleportLocation("Cows", new Position(3259, 3263)) ,
                new TeleportLocation("Yaks", new Position(2324, 3798)) ,
                new TeleportLocation("Rock crabs", new Position(2680, 3719)) ,
                new TeleportLocation("Sand crabs", new Position(1809,3457)) ,
                new TeleportLocation("Experiments", new Position(3559,9947)) ,
                new TeleportLocation("Desert bandits", new Position(3163,2984)) ,
                new TeleportLocation("Chaos Druids", new Position(2928,9844)) ,
                new TeleportLocation("Frost dragons", new Position(3033, 9597)) ,
                new TeleportLocation("Adamant & Rune dragons", new Position(1568, 5062)) ,

        }),
       MINIGAMES(new TeleportLocation[]  {
                new TeleportLocation("Fight Caves", new Position(4612, 5129, 0)) ,
                new TeleportLocation("Clan Wars", new Position(2993, 9679, 0)) ,
        }),
        WILDERNESS(new TeleportLocation[]  {
                new TeleportLocation("Forinithry dungeon", new Position(3080, 10057, 0)) ,
              
        }),
        SLAYER(new TeleportLocation[]  {
                new TeleportLocation("Kuradel's Dungeon", new Position(1736, 5313, 1)) ,
                new TeleportLocation("Fremmenik Dungeon", new Position(2808, 10002, 0)) ,
                new TeleportLocation("Slayer Tower", new Position(3429, 3534, 0)) ,
                new TeleportLocation("Catacombs of Kourend", new Position(1662,10048, 0)) ,
                new TeleportLocation("Edgeville dungeon", new Position(3097, 9870, 0)) ,
                new TeleportLocation("Glacor Cave", new Position(4180, 5731, 0)) ,
                new TeleportLocation("Jadinko Lair", new Position(3025, 9224, 0)) ,
        });
        


       @Getter @Setter TeleportLocation[] locations;
       
      TeleportData(TeleportLocation[] locations){
           this.locations = locations;
       }
}

