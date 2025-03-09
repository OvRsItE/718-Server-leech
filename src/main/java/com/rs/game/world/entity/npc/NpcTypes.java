package com.rs.game.world.entity.npc;

import com.google.common.collect.Lists;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.player.Player;

import java.util.List;

public class NpcTypes {

    public static boolean isDemon(Entity npc) {
        if (npc instanceof Player)
            return false;
        String name = npc.getAsNPC().getName().toLowerCase();

        if (name.contains("demon"))
            return true;
        if (name.contains("skotizo"))
            return true;
        if (name.contains("hellhound"))
            return true;
        if (name.contains("imp"))
            return true;
        if (name.contains("bloodveld"))
            return true;
        if (name.contains("demonic gorilla"))
            return true;
        if (name.contains("nechryael"))
            return true;
        if (name.contains("icefiend"))
            return true;
        if (name.contains("pyrefiend"))
            return true;
        if (name.contains("waterfiend"))
            return true;
        if (name.contains("pyrelord"))
            return true;
        if (name.contains("abyssal sire"))
            return true;
        if (name.contains("cerberus"))
            return true;
        if (name.contains("k'ril"))
            return true;
        if (name.contains("porazdir"))
            return true;
        if (name.contains("kolodion"))
            return true;

        return false;
    }
    public static boolean isDragon(Entity npc) {
        if (npc instanceof Player)
            return false;
        String name = npc.getAsNPC().getName().toLowerCase();
        if (name.contains("dragon"))
            return true;
        if (name.contains("king black dragon"))
            return true;
        if (name.contains("vorkath"))
            return true;
        if (name.contains("hydra"))
            return true;
        if (name.contains("wyvern"))
            return true;
        if (name.contains("galvek"))
            return true;
        if (name.contains("wyrm"))
            return true;
        if (name.contains("sunfreet"))
            return true;
        if (name.contains("drake"))
            return true;
        if (name.contains("olm"))
            return true;
        return false;
    }
    public static boolean isUndead(Entity npc) {
        if (npc instanceof Player)
            return false;
        String name = npc.getAsNPC().getName().toLowerCase();
        if (name.contains("skeleton"))
            return true;
        if (name.contains("zombie"))
            return true;
        if (name.contains("vorkath"))
            return true;
        if (name.contains("revenant"))
            return true;
        if (name.contains("ghost"))
            return true;
        if (name.contains("aberrant spectre"))
            return true;
        if (name.contains("ankou"))
            return true;
        if (name.contains("banshee"))
            return true;
        if (name.contains("crawling hand"))
            return true;
        if (name.contains("ghast"))
            return true;
        if (name.contains("mummy"))
            return true;
        if (name.contains("shade"))
            return true;
        if (name.contains("skogre"))
            return true;
        if (name.contains("tortured soul"))
            return true;
        if (name.contains("undead"))
            return true;
        if (name.contains("zogre"))
            return true;
        if (name.contains("zombified spawn"))
            return true;
        if (name.contains("vet'ion"))
            return true;
        if (name.contains("pestilent bloat"))
            return true;
        if (name.contains("tree spirit"))
            return true;
        if (name.contains("treus dayth"))
            return true;
        if (name.contains("nazastarool"))
            return true;
        if (name.contains("slash bash"))
            return true;

        return false;
    }

}
