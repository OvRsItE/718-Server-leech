package com.rs.game.world.entity.player;

import com.rs.game.item.Item;
import com.rs.utility.Utils;
import lombok.Getter;

import java.util.function.Consumer;

@Getter
public enum GameMode {

    EASY(-1,0, 125D, 50D, 1D,"125x combat xp and 50x skilling xp",  (p) -> {
        //special code here if theres any extra shit
    },new Item(995, 1000000), new Item(11838, 1) ,new Item(1191, 1),new Item(28636, 1)
            ,new Item(28638, 1) ,new Item(28634, 1) ,new Item(4587, 1) ,new Item(1323, 1)
            ,new Item(1095, 1) ,new Item(1129, 1) ,new Item(841, 1) ,new Item(884, 500)
            ,new Item(861, 1) ,new Item(1381, 1) ,new Item(558, 500) ,new Item(556, 500)
            ,new Item(6109, 1),new Item(6107, 1),new Item(6108, 1),new Item(6106, 1)
            ,new Item(6110, 1)),

    NORMAL(-1,1, 25D, 25D, 1.05D,"25x combat xp and 25x skilling 5% droprate boost",  (p) -> {
        //special code here if theres any extra shit
    },new Item(995, 1000000), new Item(11838, 1) ,new Item(1191, 1),new Item(28636, 1)
            ,new Item(28638, 1) ,new Item(28634, 1) ,new Item(4587, 1) ,new Item(1323, 1)
            ,new Item(1095, 1) ,new Item(1129, 1) ,new Item(841, 1) ,new Item(884, 500)
            ,new Item(861, 1) ,new Item(1381, 1) ,new Item(558, 500) ,new Item(556, 500)
            ,new Item(6109, 1),new Item(6107, 1),new Item(6108, 1),new Item(6106, 1)
            ,new Item(6110, 1)),

    HARD(-1,2, 5D, 5D, 1.10D,"5x combat xp and 5x skilling xp 10% drop rate boost 5% chance to double drops", (p) -> {
        //special code here if theres any extra shit
    },new Item(995, 1000000), new Item(11838, 1) ,new Item(1191, 1),new Item(28636, 1)
            ,new Item(28638, 1) ,new Item(28634, 1) ,new Item(4587, 1) ,new Item(1323, 1)
            ,new Item(1095, 1) ,new Item(1129, 1) ,new Item(841, 1) ,new Item(884, 500)
            ,new Item(861, 1) ,new Item(1381, 1) ,new Item(558, 500) ,new Item(556, 500)
            ,new Item(6109, 1),new Item(6107, 1),new Item(6108, 1),new Item(6106, 1)
            ,new Item(6110, 1), new Item(24573, 1)),

    IRONMAN(-1,0,25D, 15D, 1.05D,"25x combat xp and 15x skilling 5% droprate boost", (p) -> {
        //special code here if theres any extra shit
    },new Item(995, 100000) ,new Item(1191, 1),new Item(29200, 1)
            ,new Item(29202, 1) ,new Item(29204, 1) ,new Item(1323, 1)
            ,new Item(1095, 1) ,new Item(1129, 1) ,new Item(841, 1) ,new Item(884, 500)
            ,new Item(861, 1) ,new Item(1381, 1) ,new Item(558, 500) ,new Item(556, 500)
            ,new Item(6109, 1),new Item(6107, 1),new Item(6108, 1),new Item(6106, 1), new Item(28636, 1)
            ,new Item(28638, 1) ,new Item(28634, 1)
            ,new Item(6110, 1)),

    HARDCORE_IRONMAN(-1,0, 25D, 15D, 1.10D,"25x combat xp and 15x skilling 10% droprate boost", (p) -> {
        //special code here if theres any extra shit
    },new Item(995, 100000) ,new Item(1191, 1),new Item(28745, 1)
            ,new Item(28743, 1) ,new Item(28741, 1) ,new Item(1323, 1)
            ,new Item(1095, 1) ,new Item(1129, 1) ,new Item(841, 1) ,new Item(884, 500)
            ,new Item(861, 1) ,new Item(1381, 1) ,new Item(558, 500) ,new Item(556, 500)
            ,new Item(6109, 1),new Item(6107, 1),new Item(6108, 1),new Item(6106, 1), new Item(28636, 1)
            ,new Item(28638, 1) ,new Item(28634, 1)
            ,new Item(6110, 1)),


    ULTIMATE_IRONMAN(-1,0, 25D, 15D, 1D, "25x combat xp and 15x skilling", (p) -> {
        //special code here if theres any extra shit
    },new Item(995, 100000) ,new Item(1191, 1),new Item(29200, 1)
            ,new Item(29202, 1) ,new Item(29204, 1) ,new Item(1323, 1)
            ,new Item(1095, 1) ,new Item(1129, 1) ,new Item(841, 1) ,new Item(884, 500)
            ,new Item(861, 1) ,new Item(1381, 1) ,new Item(558, 500) ,new Item(556, 500)
            ,new Item(6109, 1),new Item(6107, 1),new Item(6108, 1),new Item(6106, 1)
            ,new Item(6110, 1));

    public static final GameMode[] MODES = values();

    private int skullId;
    private int extraSpins;
    private double combatMulti;
    private double skillMulti;
    private double luckBoost;
    private String description;
    private Consumer<Player> action;
    private Item[] items;

    GameMode(int skullId, int extraSpins, double combatMulti, double skillMulti, double luckBoost, String description, Consumer<Player> action, Item... starter) {
        this.skullId = skullId;
        this.extraSpins = extraSpins;
        this.combatMulti = combatMulti;
        this.skillMulti = skillMulti;
        this.luckBoost = luckBoost;
        this.description = description;
        this.action = action;
        this.items = starter;
    }

    public boolean isMode(GameMode mode) {
        return this == mode;
    }

    public boolean isIronman() {
        return this == IRONMAN || this == HARDCORE_IRONMAN || this == ULTIMATE_IRONMAN;
    }

    public boolean isHardcoreIronman () {
        return this == HARDCORE_IRONMAN;
    }

    public String getName() {
        return Utils.getFormattedEnumName(name());
    }

}
