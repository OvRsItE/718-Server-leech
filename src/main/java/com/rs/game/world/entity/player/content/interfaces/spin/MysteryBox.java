package com.rs.game.world.entity.player.content.interfaces.spin;

import com.rs.cache.loaders.IComponentDefinitions;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cores.CoresManager;
import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.game.item.LootTable;
import com.rs.game.item.WeightedItem;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.controller.impl.Wilderness;
import com.sun.javafx.charts.Legend;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.*;

/**
 * @author Sagacity - http://rune-server.ee/sagacity
 * @author ReverendDread - fixed shit code
 */
@RequiredArgsConstructor
public class MysteryBox implements Serializable {

    /**
     * Add to this arraylist the box id (that will bee applied as openable
     */
    public static final List<Integer> validBoxesList = Arrays.asList(28641, 28738, 28705,28706, 28999, 28825, 6199,29484,28880,28858,28857,28856 /*, BOXID*/);

    public static final String MYSTERY_SELECTED = "selectedMbox";

    public static final LootTable Venomite_mystery_box = new LootTable().addItems(
            //uncommon
            new WeightedItem(4151, 1, 100),//barrows
            new WeightedItem(6737, 1, 100),//barrows
            new WeightedItem(6733, 1, 100),//barrows
            new WeightedItem(6731, 1, 100),//barrows
            new WeightedItem(28881, 250, 100),//barrows
            //rare
            new WeightedItem(11726, 1, 2),
            new WeightedItem(43265, 1, 2),//barrows
            new WeightedItem(11724, 1, 2),
            new WeightedItem(11728, 1, 2),
            new WeightedItem(11696, 1, 2),
            new WeightedItem(25037, 1, 2),
            new WeightedItem(11716, 1, 2),
            new WeightedItem(11700, 1, 2),
            new WeightedItem(11698, 1, 2),
            new WeightedItem(11694, 1, 2),
            new WeightedItem(11720, 1, 2),
            new WeightedItem(11720, 1, 2),
            new WeightedItem(11718, 1, 2),
            //legendary
            new WeightedItem(28750, 10, 1),
            new WeightedItem(11283, 1, 1),
            new WeightedItem(28756, 1, 1),
            new WeightedItem(28925, 1, 1),
            new WeightedItem(28670, 1, 1),
            new WeightedItem(28668, 1, 1),
            new WeightedItem(21371, 1, 1),
            new WeightedItem(6199, 1, 1),
            new WeightedItem(29484, 1, 1),
            new WeightedItem(6199, 1, 1),
            new WeightedItem(28703, 1, 1),
            new WeightedItem(28881, 5000, 1)
    );

    public static final LootTable Hween_Box = new LootTable().addItems(
            //uncommon
            new WeightedItem(1419, 1, 50),
            new WeightedItem(9925, 1, 50),
            new WeightedItem(9924, 1, 50),
            new WeightedItem(9923, 1, 54),
            new WeightedItem(9921, 1, 50),
            new WeightedItem(9922, 1, 50),
            new WeightedItem(9920, 1, 50),
            new WeightedItem(11789, 1, 50),
            //rare
            new WeightedItem(1959, 1, 2),
            new WeightedItem(1053, 1, 2),
            new WeightedItem(1055, 1, 2),
            new WeightedItem(1057, 1, 2),
            new WeightedItem(29002, 1, 2),
            new WeightedItem(29046, 1, 2),
            new WeightedItem(29048, 1, 2),
            new WeightedItem(29050, 1, 2),
            new WeightedItem(29052, 1, 2),
            new WeightedItem(29054, 1, 2),
            new WeightedItem(29054, 1, 2),
            //legendary
            new WeightedItem(28707, 1, 1),
            new WeightedItem(28713, 1, 1),
            new WeightedItem(28825, 1, 1),
            new WeightedItem(28831, 1, 1),
            new WeightedItem(28717, 1, 1),
            new WeightedItem(29467, 1, 1),
            new WeightedItem(29466, 1, 1),
            new WeightedItem(6199, 2, 1),
            new WeightedItem(29484, 1, 1),
            new WeightedItem(6199, 1, 1),
            new WeightedItem(28739, 50000, 1)
    );

    public static final LootTable Toxic_Mystery_Chest = new LootTable().addItems(
            //uncommon
            new WeightedItem(14484, 1, 3),//dragon claws
            new WeightedItem(11694, 1, 3),//ags
            new WeightedItem(21777, 1, 3),//armadyl battlestaff
            new WeightedItem(6199, 2, 4),//mbox
            new WeightedItem(11283, 1, 4),//dragonfire shield
            new WeightedItem(11718, 1, 4),//armadyl helmet
            new WeightedItem(13744, 1, 4),//spectral spirit shield
            //rare
            new WeightedItem(1050, 1, 2),//santa hat
            new WeightedItem(18351, 1, 2),//chaotic longsword
            new WeightedItem(18349, 1, 2),//chaotic rapier
            new WeightedItem(18353, 1, 2),//chaotic maul
            new WeightedItem(18355, 1, 2),//chaotic staff
            new WeightedItem(18357, 1, 2),//chaotic crossbow
            new WeightedItem(28882, 1, 2),//overloaded heart
            new WeightedItem(1037, 1, 2),//bunny ears
            new WeightedItem(11720, 1, 2),//armadyl chestplate
            new WeightedItem(11722, 1, 2),//armadyl chainskirt
            new WeightedItem(11726, 1, 2),//bandos tassets
            new WeightedItem(11724, 1, 2),//bcp
            new WeightedItem(13738, 1, 2),//arcane spirit shield
            //legendary
            new WeightedItem(29012, 1, 1),//black santa hat
            new WeightedItem(29834, 1, 1),//christmas cracker
            new WeightedItem(13740, 1, 1),//divine spirit shield
            new WeightedItem(52978, 1, 1),//dragon hunter lance
            new WeightedItem(51012, 1, 1),//dragon hunter crossbow
            new WeightedItem(29489, 1, 1),//ancestral robe bottoms
            new WeightedItem(29491, 1, 1),//ancestral hat
            new WeightedItem(29493, 1, 1),//ancestral robe top
            new WeightedItem(52486, 1, 1),//scythe of vitur
            new WeightedItem(29231, 1, 1),//inquistor top
            new WeightedItem(29233, 1, 1),//inquistor helm
            new WeightedItem(29235, 1, 1),//inquistor bottomss
            new WeightedItem(54417, 1, 1)//inquistor mace
    );


    public static final LootTable Mystery_Chest = new LootTable().addItems(

            //uncommon
            new WeightedItem(14484, 1, 3),//dragon claws
            new WeightedItem(11694, 1, 3),//ags
            new WeightedItem(21777, 1, 3),//armadyl battlestaff
            new WeightedItem(6199, 2, 4),//mbox
            new WeightedItem(11283, 1, 4),//dragonfire shield
            new WeightedItem(11718, 1, 4),//armadyl helmet
            new WeightedItem(13744, 1, 4),//spectral spirit shield
            //rare
            new WeightedItem(1050, 1, 2),//santa hat
            new WeightedItem(18351, 1, 2),//chaotic longsword
            new WeightedItem(18349, 1, 2),//chaotic rapier
            new WeightedItem(18353, 1, 2),//chaotic maul
            new WeightedItem(18355, 1, 2),//chaotic staff
            new WeightedItem(18357, 1, 2),//chaotic crossbow
            new WeightedItem(28882, 1, 2),//overloaded heart
            new WeightedItem(1037, 1, 2),//bunny ears
            new WeightedItem(11720, 1, 2),//armadyl chestplate
            new WeightedItem(11722, 1, 2),//armadyl chainskirt
            new WeightedItem(11726, 1, 2),//bandos tassets
            new WeightedItem(11724, 1, 2),//bcp
            new WeightedItem(13738, 1, 2),//arcane spirit shield
            //legendary
            new WeightedItem(29012, 1, 1),//black santa hat
            new WeightedItem(962, 1, 1),//christmas cracker
            new WeightedItem(13740, 1, 1),//divine spirit shield
            new WeightedItem(52978, 1, 1),//dragon hunter lance
            new WeightedItem(51012, 1, 1),//dragon hunter crossbow
            new WeightedItem(29565, 1, 1),//lava dye
            new WeightedItem(29563, 1, 1),//rainbow dye
            new WeightedItem(29805, 1, 1),//shadow dye
            new WeightedItem(29806, 1, 1),//third-age dye
            new WeightedItem(1038, 1, 1),//red partyhat
            new WeightedItem(1048, 1, 1),//white partyhat
            new WeightedItem(1042, 1, 1),//blue partyhat
            new WeightedItem(29666, 1, 1)//mbox pet


    );

    public static final LootTable Pvpbox = new LootTable().addItems(
            new WeightedItem(6199, 1, 1),
            new WeightedItem(11694, 1, 1),
            new WeightedItem(43239, 1, 1),
            new WeightedItem(29153, 1, 1),
            new WeightedItem(14484, 1, 1),
            new WeightedItem(13887, 1, 2),
            new WeightedItem(13893, 1, 2),
            new WeightedItem(13899, 1, 1),
            new WeightedItem(995, 30000009,  1),
            new WeightedItem(13858, 1, 2),
            new WeightedItem(13861, 1, 2),
            new WeightedItem(13864, 1, 2),
            new WeightedItem(13867, 1, 2),
            new WeightedItem(13884, 1, 2),
            new WeightedItem(13890, 1, 2),
            new WeightedItem(13896, 1, 2),
            new WeightedItem(13902, 1, 1),
            new WeightedItem(15273, 300, 1),
            new WeightedItem(29110, 1, 50),
            new WeightedItem(29112, 1, 50),
            new WeightedItem(12791, 50, 50),
            new WeightedItem(51902, 1, 2),
            new WeightedItem(42902, 1, 1),
            new WeightedItem(29951, 1, 1),
            new WeightedItem(43237, 1, 1),
            new WeightedItem(43237, 1, 1),
            new WeightedItem(41926, 1, 2),
            new WeightedItem(41924, 1, 2),
            new WeightedItem(29113, 1, 50),
            new WeightedItem(29113, 1, 50)
    );

    public static final LootTable LegendaryEgg = new LootTable().addItems(
            new WeightedItem(28819, 1, 10),
            new WeightedItem(28820, 1, 10),
            new WeightedItem(28821, 1, 10),
            new WeightedItem(28823, 1, 10),
            new WeightedItem(28818, 1, 10),
            new WeightedItem(28822, 1, 10),
            new WeightedItem(28819, 1, 10),
            new WeightedItem(28820, 1, 10),
            new WeightedItem(28821, 1, 10),
            new WeightedItem(28823, 1, 10),
            new WeightedItem(28818, 1, 10),
            new WeightedItem(28822, 1, 10),
            new WeightedItem(28819, 1, 10),
            new WeightedItem(28820, 1, 10),
            new WeightedItem(28821, 1, 10),
            new WeightedItem(28823, 1, 10),
            new WeightedItem(28818, 1, 10),
            new WeightedItem(28822, 1, 10),
            new WeightedItem(28819, 1, 10),
            new WeightedItem(28820, 1, 10),
            new WeightedItem(28821, 1, 10),
            new WeightedItem(28823, 1, 10),
            new WeightedItem(28818, 1, 10),
            new WeightedItem(28822, 1, 10),
            new WeightedItem(28819, 1, 10),
            new WeightedItem(28820, 1, 10),
            new WeightedItem(28821, 1, 10),
            new WeightedItem(28823, 1, 10),
            new WeightedItem(28818, 1, 10),
            new WeightedItem(28822, 1, 10),
            new WeightedItem(28824, 1, 1)
    );

    public static final LootTable Mystery_Box = new LootTable().addItems(

            //common
            new WeightedItem(11846, 1, 100),//barrows
            new WeightedItem(11848, 1, 100),//barrows
            new WeightedItem(11850, 1, 100),//barrows
            new WeightedItem(11852, 1, 100),//barrows
            new WeightedItem(11854, 1, 100),//barrows
            new WeightedItem(11856, 1, 100),//barrows
            new WeightedItem(4151, 1, 100),//barrows
            new WeightedItem(6737, 1, 100),//barrows
            new WeightedItem(6733, 1, 100),//barrows
            new WeightedItem(6731, 1, 100),//barrows
            new WeightedItem(29951, 1, 100),//barrows
            //uncommon
            new WeightedItem(14484, 1, 10),//dragon claws
            new WeightedItem(11694, 1, 10),//ags
            new WeightedItem(21777, 1, 10),//armadyl battlestaff
            new WeightedItem(6199, 2, 10),//mbox
            new WeightedItem(11283, 1, 10),//dragonfire shield
            new WeightedItem(11718, 1, 10),//armadyl helmet
            new WeightedItem(13744, 1, 10),//spectral spirit shield
            //rare
            new WeightedItem(1050, 1, 8),//santa hat
            new WeightedItem(18351, 1, 8),//chaotic longsword
            new WeightedItem(18349, 1, 8),//chaotic rapier
            new WeightedItem(18353, 1, 8),//chaotic maul
            new WeightedItem(18355, 1, 8),//chaotic staff
            new WeightedItem(18357, 1, 8),//chaotic crossbow
            new WeightedItem(28882, 1, 8),//overloaded heart
            new WeightedItem(1037, 1, 8),//bunny ears
            new WeightedItem(11720, 1, 8),//armadyl chestplate
            new WeightedItem(11722, 1, 8),//armadyl chainskirt
            new WeightedItem(11726, 1, 8),//bandos tassets
            new WeightedItem(11724, 1, 8),//bcp
            new WeightedItem(13738, 1, 8),//arcane spirit shield
            //legendary
            new WeightedItem(29012, 1, 6),//black santa hat
            new WeightedItem(962, 1, 6),//christmas cracker
            new WeightedItem(13740, 1, 6),//divine spirit shield
            new WeightedItem(52978, 1, 6),//dragon hunter lance
            new WeightedItem(29897, 1, 6),//dragon hunter crossbow
            new WeightedItem(29565, 1, 6),//lava dye
            new WeightedItem(29563, 1, 6),//rainbow dye
            new WeightedItem(1038, 1, 6),//red partyhat
            new WeightedItem(1048, 1, 6),//white partyhat
            new WeightedItem(1042, 1, 6),//blue partyhat
            new WeightedItem(20135, 1, 6),//torva full helm
            new WeightedItem(20139, 1, 6),//torva platebody
            new WeightedItem(20143, 1, 6),//torva platelegs
            new WeightedItem(20159, 1, 6),//virtus mask
            new WeightedItem(20163, 1, 6),//virtus robe top
            new WeightedItem(20167, 1, 6),//virtus robe legs
            new WeightedItem(20147, 1, 6),//pernix cowl
            new WeightedItem(20151, 1, 6),//pernix top
            new WeightedItem(20155, 1, 6),//pernix chaps
            new WeightedItem(20171, 1, 6),//zbow
            new WeightedItem(29666, 1, 6)//mbox pet


    );

    public static final LootTable Toxic_Mystery_Box = new LootTable().addItems(
            //common
            new WeightedItem(11846, 1, 100),//barrows
            new WeightedItem(11848, 1, 100),//barrows
            new WeightedItem(11850, 1, 100),//barrows
            new WeightedItem(11852, 1, 100),//barrows
            new WeightedItem(11854, 1, 100),//barrows
            new WeightedItem(11856, 1, 100),//barrows
            new WeightedItem(4151, 1, 100),//barrows
            new WeightedItem(6737, 1, 100),//barrows
            new WeightedItem(6733, 1, 100),//barrows
            new WeightedItem(6731, 1, 100),//barrows
            new WeightedItem(29951, 1, 100),//barrows
            //uncommon
            new WeightedItem(14484, 1, 10),//dragon claws
            new WeightedItem(11694, 1, 10),//ags
            new WeightedItem(21777, 1, 10),//armadyl battlestaff
            new WeightedItem(6199, 2, 10),//mbox
            new WeightedItem(11283, 1, 10),//dragonfire shield
            new WeightedItem(11718, 1, 10),//armadyl helmet
            new WeightedItem(13744, 1, 10),//spectral spirit shield
            //rare
            new WeightedItem(1050, 1, 9),//santa hat
            new WeightedItem(18351, 1, 9),//chaotic longsword
            new WeightedItem(18349, 1, 9),//chaotic rapier
            new WeightedItem(18353, 1, 9),//chaotic maul
            new WeightedItem(18355, 1, 9),//chaotic staff
            new WeightedItem(18357, 1, 9),//chaotic crossbow
            new WeightedItem(28882, 1, 9),//overloaded heart
            new WeightedItem(1037, 1, 9),//bunny ears
            new WeightedItem(11720, 1, 9),//armadyl chestplate
            new WeightedItem(11722, 1, 9),//armadyl chainskirt
            new WeightedItem(11726, 1, 9),//bandos tassets
            new WeightedItem(11724, 1, 9),//bcp
            new WeightedItem(13738, 1, 9),//arcane spirit shield
            //legendary
            new WeightedItem(29012, 1, 7),//black santa hat
            new WeightedItem(29834, 1, 7),//christmas cracker
            new WeightedItem(13740, 1, 7),//divine spirit shield
            new WeightedItem(52978, 1, 7),//dragon hunter lance
            new WeightedItem(29467, 1, 7),//xeric chest
            new WeightedItem(29466, 1, 7),//tob chest
            new WeightedItem(28825, 1, 7),//legendary egg
            new WeightedItem(28831, 1, 7),//upgrade gem
            new WeightedItem(28753, 1, 7),//$30 bond
            new WeightedItem(28749, 1, 7),//fero gloves
            new WeightedItem(29231, 1, 7),//inquistor top
            new WeightedItem(29233, 1, 7),//inquistor helm
            new WeightedItem(29235, 1, 7),//inquistor bottomss
            new WeightedItem(54417, 1, 7)//inquistor mace
    );

    public static final LootTable LaunchChest = new LootTable().addItems(
            new WeightedItem(28877, 1, 50),
            new WeightedItem(28875, 1, 50),
            new WeightedItem(28879, 1, 50),
            new WeightedItem(28869, 1, 25),
            new WeightedItem(28873, 1, 25),
            new WeightedItem(28871, 1, 25),
            new WeightedItem(29246, 1, 5),
            new WeightedItem(29248, 1, 5),
            new WeightedItem(29244, 1, 5),
            new WeightedItem(29484, 5, 5),
            new WeightedItem(28877, 1, 50),
            new WeightedItem(28875, 1, 50),
            new WeightedItem(28879, 1, 50),
            new WeightedItem(28869, 1, 25),
            new WeightedItem(28873, 1, 25),
            new WeightedItem(28871, 1, 25),
            new WeightedItem(29246, 1, 5),
            new WeightedItem(29248, 1, 5),
            new WeightedItem(29244, 1, 5),
            new WeightedItem(29484, 5, 5),
            new WeightedItem(28877, 1, 50),
            new WeightedItem(28875, 1, 50),
            new WeightedItem(28879, 1, 50),
            new WeightedItem(28869, 1, 25),
            new WeightedItem(28873, 1, 25),
            new WeightedItem(28871, 1, 25),
            new WeightedItem(28859, 1, 5),
            new WeightedItem(29248, 1, 5),
            new WeightedItem(29244, 1, 5),
            new WeightedItem(29484, 5, 5),
            new WeightedItem(28877, 1, 50),
            new WeightedItem(28875, 1, 50),
            new WeightedItem(28879, 1, 50),
            new WeightedItem(28869, 1, 25),
            new WeightedItem(28873, 1, 25),
            new WeightedItem(28871, 1, 25),
            new WeightedItem(29246, 1, 5),
            new WeightedItem(29248, 1, 5),
            new WeightedItem(29244, 1, 5),
            new WeightedItem(29484, 5, 5)


    );

    public static final LootTable SLAYER_CASKET_TIER_1 = new LootTable().addItems(
            //common
            new WeightedItem(995, 500000, 50),
            new WeightedItem(386, 25, 50),
            new WeightedItem(2435, 10, 50),
            new WeightedItem(2437, 10, 50),
            new WeightedItem(2441, 10, 50),
            new WeightedItem(2443, 10, 50),
            new WeightedItem(208, 10, 50),
            new WeightedItem(3052, 10, 50),
            new WeightedItem(537, 10,  50),
            new WeightedItem(5294, 3, 50),
            new WeightedItem(5300, 3, 50),
            new WeightedItem(454, 50, 50),
            new WeightedItem(452, 5, 50),
            new WeightedItem(2362, 5, 50),
            new WeightedItem(2354, 15, 50),
            new WeightedItem(2, 250, 50),
            new WeightedItem(445, 50, 50),
            //uncommon
            new WeightedItem(11232, 50, 25),
            new WeightedItem(11212, 50, 25),
            new WeightedItem(7937, 150, 25),
            new WeightedItem(565, 150, 25),
            new WeightedItem(560, 150, 25),
            new WeightedItem(566, 150, 25),
            new WeightedItem(214, 150, 25),
            //rare
            new WeightedItem(11037, 1, 1),
            new WeightedItem(8921, 1, 1),
            new WeightedItem(51028, 1, 1),
            new WeightedItem(2513, 1, 1),
            new WeightedItem(4131, 1, 1),
            new WeightedItem(4153, 1, 1)
    );
    public static final LootTable SLAYER_CASKET_TIER_2 = new LootTable().addItems(
            //common
            new WeightedItem(995, 1000000, 50),
            new WeightedItem(386, 50, 50),
            new WeightedItem(2435, 20, 50),
            new WeightedItem(2437, 20, 50),
            new WeightedItem(2441, 20, 50),
            new WeightedItem(2443, 20, 50),
            new WeightedItem(208, 20, 50),
            new WeightedItem(3052, 20, 50),
            new WeightedItem(537, 20,  50),
            new WeightedItem(5294, 6, 50),
            new WeightedItem(5300, 6, 50),
            new WeightedItem(454, 100, 50),
            new WeightedItem(452, 10, 50),
            new WeightedItem(2362, 10, 50),
            new WeightedItem(2354, 30, 50),
            new WeightedItem(2, 500, 50),
            new WeightedItem(445, 100, 50),
            //uncommon
            new WeightedItem(11232, 100, 25),
            new WeightedItem(11212, 100, 25),
            new WeightedItem(7937, 300, 25),
            new WeightedItem(565, 300, 25),
            new WeightedItem(560, 300, 25),
            new WeightedItem(566, 300, 25),
            new WeightedItem(214, 300, 25),
            //rare
            new WeightedItem(51637, 1, 1),
            new WeightedItem(11286, 1, 1),
            new WeightedItem(11732, 1, 1),
            new WeightedItem(4151, 1, 1),
            new WeightedItem(11235, 1, 1),
            new WeightedItem(22449, 150, 1)
    );
    public static final LootTable SLAYER_CASKET_TIER_3 = new LootTable().addItems(
            //common
            new WeightedItem(995, 1500000, 50),
            new WeightedItem(386, 100, 50),
            new WeightedItem(2435, 30, 50),
            new WeightedItem(2437, 30, 50),
            new WeightedItem(2441, 30, 50),
            new WeightedItem(2443, 30, 50),
            new WeightedItem(208, 30, 50),
            new WeightedItem(3052, 30, 50),
            new WeightedItem(537, 30,  50),
            new WeightedItem(5294, 9, 50),
            new WeightedItem(5300, 9, 50),
            new WeightedItem(454, 150, 50),
            new WeightedItem(452, 20, 50),
            new WeightedItem(2362, 20, 50),
            new WeightedItem(2354, 60, 50),
            new WeightedItem(2, 750, 50),
            new WeightedItem(445, 200, 50),
            //uncommon
            new WeightedItem(11232, 200, 25),
            new WeightedItem(11212, 200, 25),
            new WeightedItem(7937, 400, 25),
            new WeightedItem(565, 400, 25),
            //rare
            new WeightedItem(21369, 1, 1),
            new WeightedItem(22494, 1, 1),
            new WeightedItem(22451, 150, 1),
            new WeightedItem(43273, 1, 1),
            new WeightedItem(43231, 1, 1),
            new WeightedItem(43229, 1, 1),
            new WeightedItem(43233, 1, 1),
            new WeightedItem(42004, 1, 1),
            new WeightedItem(29951, 1, 1),
            new WeightedItem(52966, 1, 1),
            new WeightedItem(52973, 1, 1),
            new WeightedItem(52971, 1, 1),
            new WeightedItem(52969, 1, 1),
            new WeightedItem(43227, 1, 1)
    );

    private static final long serialVersionUID = 6286579554518818674L;

    /*public static final Item[] BOXNAME = {
            new Item(605, 1),
            new Item(989, 1),
            new Item(537, 45),
            new Item(18831, 30),
            new Item(2354, 100),
            new Item(2360, 100),
            new Item(452, 70),
            new Item(27858, 5),
            new Item(4151, 1),
            new Item(995, 3000000),
            new Item(24544, 1),
            new Item(27859, 1),
            new Item(1754, 120),
            new Item(565, 3000),
            new Item(10034, 100),
            new Item(220, 10),
            new Item(12791, 7),
            new Item(12094, 7),
            new Item(12435, 100),
            new Item(12825, 50),
            new Item(1618, 100),
            new Item(11230, 300),
            new Item(71, 50),
            new Item(73, 50),
            new Item(1514, 85),
            new Item(441, 100),
            new Item(454, 100),
            new Item(12158, 170),
            new Item(12159, 150),
            new Item(12160, 50)
    };*/

    private LootTable randomRewards;
    private LootTable commonListItems;
    private final transient Player player;

    /**
     * Opens the mystery box for the player
     * They won't be able to spin if they don't have spins (lol)
     */
    public void openBox() {
        int selectedBox = (Integer) player.getTemporaryAttributtes().get(MYSTERY_SELECTED);

        //player.getInterfaceManager().sendTabInterfaces(false);

        player.getInterfaceManager().sendInterface(1161);
        ItemsContainer<Item> asContainer = new ItemsContainer<Item>(30, false);

        sendItemsToBox(selectedBox);
        updateTitle(selectedBox);

        for (int i = 0; i < 30; i++) {
            player.getPackets().sendItemOnIComponent(1161, spinItemComponents[i]+4, randomRewards.getItem(i).getId(), randomRewards.getItem(i).getAmount());
            asContainer.add(new Item(commonListItems.getItem(i).getId(), commonListItems.getItem(i).getAmount()));
        }
        player.getPackets().sendItems(90, false, asContainer);
        player.getPackets().sendInterSetItemsOptionsScript(1161, 104, 90, 4, 9, "Examine");
        player.getPackets().sendUnlockIComponentOptionSlots(1161, 104, 0, 160, 0);
        for (int i = 0; i < componentsLastRewards.length; i++) {
            player.getPackets().sendIComponentText(1161, componentsLastRewards[i] + 1, "");
            player.getPackets().sendHideIComponent(1161, componentsLastRewards[i]+4, true);
        }
        List<Item> recent = player.getRecentMBoxRewards().getOrDefault(selectedBox, new ArrayList<>());

        for (int i = 0; i < recent.size(); i++) {
            player.getPackets().sendHideIComponent(1161, componentsLastRewards[i]+4, false);
            player.getPackets().sendIComponentText(1161, componentsLastRewards[i] + 1, recent.get(i).getName());
            player.getPackets().sendItemOnIComponent(1161, componentsLastRewards[i] + 4, recent.get(i).getId(), recent.get(i).getAmount());
        }
        player.getPackets().sendIComponentText(1161, 101, "Get more spins (" + player.getMBoxSpins() + ")");

        player.setCloseInterfacesEvent(() -> {
            player.getTemporaryAttributtes().put(MYSTERY_STOP, true);
        });
    }

    public void openAllBoxes() {
        int selectedBox = (Integer) player.getTemporaryAttributtes().get(MYSTERY_SELECTED);
        int quantity = player.getInventory().getAmountOf(selectedBox);
        for (int i = 0; i < quantity; i++) {
            sendItemsToBox(selectedBox);
            WeightedItem reward = this.randomRewards.getRandomItems(false).get(0);
            player.addMBoxReward(reward, selectedBox);
        }
    }

    /**
     * Static method to add the items to the item collection
     * @param selectedBox The valid box id that will contain the items
     */
    private void sendItemsToBox(int selectedBox) {
        switch (selectedBox) {
            case 28641:
                randomRewards = Venomite_mystery_box;
                commonListItems = Venomite_mystery_box;
                break;
            case 28738:
                randomRewards = Hween_Box;
                commonListItems = Hween_Box;
                break;
            case 28705:
                randomRewards = Toxic_Mystery_Chest;
                commonListItems = Toxic_Mystery_Chest;
                break;
            case 28706:
                randomRewards = Mystery_Chest;
                commonListItems = Mystery_Chest;
                break;
            case 28999:
                randomRewards = Pvpbox;
                commonListItems = Pvpbox;
                break;
            case 6199:
                randomRewards = Mystery_Box;
                commonListItems = Mystery_Box;
                break;
            case 29484:
                randomRewards = Toxic_Mystery_Box;
                commonListItems = Toxic_Mystery_Box;
                break;
            case 28880:
                randomRewards = LaunchChest;
                commonListItems = LaunchChest;
                break;
            case 28858:
                randomRewards = SLAYER_CASKET_TIER_1;
                commonListItems = SLAYER_CASKET_TIER_1;
                break;
            case 28857:
                randomRewards = SLAYER_CASKET_TIER_2;
                commonListItems = SLAYER_CASKET_TIER_2;
                break;
            case 28856:
                randomRewards = SLAYER_CASKET_TIER_3;
                commonListItems = SLAYER_CASKET_TIER_3;
                break;
            case 28825:
                randomRewards = LegendaryEgg;
                commonListItems = LegendaryEgg;
                break;

            /*
                case BOXID:
                BOXNAMERewards = new ArrayList<>(Arrays.asList(BOXNAME));
                BOXNAMEListItems = new ArrayList<>(Arrays.asList(BOXNAME));
                    break;
                    */
        }
        randomRewards.shuffle();
        commonListItems.shuffle();
    }


    /**
     * the selectedBox here need to have a id set, otherwise the box will say 'Mistery Box' on top of it
     */
    private void updateTitle(int selectedBox) {
        player.getPackets().sendIComponentText(1161, 36,
                (selectedBox == 28641 ? "Venomite Mystery Box" :
                        selectedBox == 28738 ? "H'ween Mystery Box" :
                        selectedBox == 28705 ? "Toxic Mystery Chest" :
                 selectedBox == 28706 ? "Mystery Chest" :
                 selectedBox == 28999 ? "Pvp Mystery Box" :
                        selectedBox == 6199 ? "Mystery Box" :
                                selectedBox == 29484 ? "Toxic Mystery Box" :
                                        /* selectedBox == BOXID ? "new Awesome Box Name" : */
                                        selectedBox == 28880 ? "Laucnh Chest" :
                                                selectedBox == 28825 ? "Legendary Egg" :
                                                selectedBox == 28858 ? "Slayer Casket Tier 1" :
                                                        selectedBox == 28857 ? "Slayer Casket tier 2" :
                                                                selectedBox == 28856 ? "Slayer Casket tier 3" :
                                        selectedBox == 6833 ? "Dragonstone Mystery Box": "Mystery Box"));

    }


    /**
     * Spins the mystery box
     */
    public void spin() {
        if (Wilderness.isAtWild(player)) {
            player.getPackets().sendGameMessage("You cannot use this in the wilderness.");
            player.getPackets().sendGameMessage("Also, you are cheating. Stahp.");
            return;
        }
        if (player.getControlerManager().getControler() != null) {
            player.getPackets().sendGameMessage("You must be in a bank to use this item!");
            return;
        }
        /*if (player.getMBoxSpins() < 1) {
            player.sm("The box is unable to spin. Please get more spins first.");
            return;
        }*/
        if (!player.getInventory().containsAnyItems(validBoxesList, 1)) {
            player.getPackets().sendGameMessage("You need a box to use this item");
            return;
        }
        if (player.spinningMBox) {
            player.sm("You are already using the mystery box, please wait.");
            return;
        }
        if (!player.getInventory().hasFreeSlots()) {
            player.sm("Please clear up your inventory before using the box.");
            return;
        }
        //player.getPackets().spinMysteryBox(commonItems.length);
        spinMisteryBox();

        ItemsContainer<Item> asContainer = new ItemsContainer<Item>(30, false);
        List<Item> randomRewards = this.randomRewards.getAsItems();
        List<Item> commonListItems = this.commonListItems.getAsItems();
        WeightedItem reward = this.randomRewards.getRandomItems(false).get(0);
        for (int i = 0; i < randomRewards.size(); i++) {
            if (i == 12) {
                player.getPackets().sendItemOnIComponent(1161, 71 + i, reward.getId(), reward.getAmount());
                asContainer.add(new Item(reward.getId(), reward.getAmount()));
            } else {
                player.getPackets().sendItemOnIComponent(1161, 71 + i, randomRewards.get(i).getId(), randomRewards.get(i).getAmount());
                asContainer.add(new Item(commonListItems.get(i).getId(), commonListItems.get(i).getAmount()));
            }
        }

        player.getPackets().sendItems(90, false, asContainer);
        player.getPackets().sendInterSetItemsOptionsScript(1161, 104, 90, 4, 9, "Examine");
        player.getPackets().sendUnlockIComponentOptionSlots(1161, 104, 0, 160, 0);

        for (int i = 0; i < 6; i++) {
            player.getPackets().sendIComponentText(1161, componentsLastRewards[i] + 1, "");
            player.getPackets().sendHideIComponent(1161, componentsLastRewards[i]+4, true);
        }

        Integer selectedBox = (Integer) player.getTemporaryAttributtes().get(MYSTERY_SELECTED);
        updateTitle(selectedBox);
        List<Item> recent = player.getRecentMBoxRewards().getOrDefault(selectedBox, new ArrayList<>());

        for (int i = 0; i < recent.size(); i++) {
            player.getPackets().sendHideIComponent(1161, componentsLastRewards[i]+4, false);
            player.getPackets().sendIComponentText(1161, componentsLastRewards[i] + 1, recent.get(i).getName());
            player.getPackets().sendItemOnIComponent(1161, componentsLastRewards[i] + 4, recent.get(i).getId(), recent.get(i).getAmount());
        }

        List<Item> asList = new ArrayList<Item>();

        for (int i = 0; i < 30; i++)
            asList.add(new Item(randomRewards.get(i).getId(), randomRewards.get(i).getAmount()));

        for (int i = 0; i < 30; i++) {
            if (i != 12) {
                player.getPackets().sendItemOnIComponent(1161, spinItemComponents[i] + 4, asList.get(i).getId(), asList.get(i).getAmount());
            } else {
                player.getPackets().sendItemOnIComponent(1161, spinItemComponents[i] + 4, reward.getId(), reward.getAmount());
            }
        }

        player.getPackets().sendHideIComponent(1161, 13, true); //close button
        player.spinningMBox = true;
        player.getPackets().sendHideIComponent(1161, 349, true); //114
        player.getPackets().sendIComponentText(1161, 101, "Get more spins (" + player.getMBoxSpins() + ")");
        player.getPackets().sendHideIComponent(1161, 146, true); // hide spin button
        player.getPackets().sendBlackOut(5);
        player.setInfiniteStopDelay();
        player.lock();
        player.stopAll(true, false, false);
        player.doAfterDelay(12, () -> { //15
            int arroba = (int) player.getTemporaryAttributtes().get(MYSTERY_SELECTED);
            player.getPackets().sendHideIComponent(1161, 13, false); //show close button
            player.addMBoxReward(reward, arroba);
            player.getPackets().sendIComponentText(1161, 101, "Get more spins (" + player.getMBoxSpins() + ")");
            player.getPackets().sendBlackOut(0);
            player.spinningMBox = false;

            List<Item> recentRewards = player.getRecentMBoxRewards().getOrDefault(selectedBox, new ArrayList<>());
            for (int i = 0; i < recentRewards.size(); i++) {
                player.getPackets().sendIComponentText(1161, componentsLastRewards[i] + 1, recentRewards.get(i).getName());
                player.getPackets().sendItemOnIComponent(1161, componentsLastRewards[i] + 4, recentRewards.get(i).getId(), 1);
            }
            sendYouWon(reward, selectedBox);
            player.resetStopDelay();
            player.unlock();
        });
    }

    private void sendYouWon(WeightedItem item, int selectedBox) {
        player.getPackets().sendHideIComponent(1161, 362, true);
        player.getPackets().sendHideIComponent(1161, 349, false);
        player.getPackets().sendIComponentText(1161, 357, item.getName());
        player.getPackets().sendItemOnIComponent(1161, 361, item.getId(), item.getAmount());

        player.doAfterDelay(3, () -> {
            player.getPackets().sendHideIComponent(1161, 349, true);
            player.getPackets().sendHideIComponent(1161, 146, false);
            player.closeInterfaces();
        });
    }

    public void spinMisteryBox() {
        // Reset positions
        player.getInterfaceManager().sendInterface(1161);

        player.getTemporaryAttributtes().put(MYSTERY_STOP, Boolean.FALSE);

        for (int index = 0; index < 30; index++) {
            IComponentDefinitions def = IComponentDefinitions.getInterfaceComponent(1161, spinItemComponents[index]);
            if (player.getTemporaryAttributtes().get(MYSTERY_STOP) == Boolean.TRUE) {
                break;
            }

            SendSpin spin = new SendSpin(def.getComponentPositionX(), def.getComponentPositionY(), player, index, spinItemComponents);

            CoresManager.fastExecutor.schedule(spin, 120, 10); //23
        }
    }


    public void handleButtons(int interfaceId, int componentId, int slotId, int packetId) {
        if (componentId == 147)
            player.getMysteryBoxes().spin();
        if (componentId == 362) {
            player.getPackets().sendHideIComponent(1161, 349, true);
            player.getPackets().sendHideIComponent(1161, 146, false);
        }
    }

    private static int[] componentsLastRewards = {
            149, 154, 159, 164, 169, 174
    };

    private static final int[] spinItemComponents = {198, 203, 208, 213, 218, 223, 228, 233,
            238, 243, 248, 253, 258, 263, 268, 273, 278, 283, 288, 293, 298, 303, 308, 313,
            318, 323, 328, 333, 338, 343};

    private static final String MYSTERY_STOP = "mbox_stop";
}