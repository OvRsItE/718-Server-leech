package com.rs.game.world.entity.player.content.collection;

import com.rs.game.item.Item;
import com.rs.game.world.entity.player.Player;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.rs.game.world.entity.player.content.collection.CollectionLogLoader.COLLECTION_LOG_CACHE;

public class ItemCollectionManager implements Serializable {

    private static final long serialVersionUID = -4835606309245720271L;

    public static final int INTERFACE_ID = 3202;
    private static final int OBTAINED_TXT = 268;
    private static final int KILLS_TXT = 270;
    private static final int NAME_TXT = 266;
    private static final int CONTAINER_ID = 90;
    private static final int PROGRESS_TXT = 269;
    private static final int COLLECTION_TITLE = 266;

    @Setter
    private transient Player player;
    private final List<CollectionLog> collectionLogs = new ArrayList<>();
    private transient Category viewingCategory;

    public void init() {
        for (CollectionLog log : COLLECTION_LOG_CACHE) {
            addCollectionLog(log);
        }
    }

    public void openInterface(Category category) {
        viewingCategory = category;
        player.getInterfaceManager().sendInterface(INTERFACE_ID);
        showCategory();
        handleClick(25); //default to first in the catagory
    }

    public void handleClick(int component) {
        switch(component) {
            case 275:
                player.getItemCollectionManager().openInterface(Category.BOSSES);
                break;
            case 279:
                player.getItemCollectionManager().openInterface(Category.RAIDS);
                break;
            case 283:
                player.getItemCollectionManager().openInterface(Category.CLUES);
                break;
            case 287:
                player.getItemCollectionManager().openInterface(Category.OTHER);
                break;
            default: {
                List<CollectionLog> logs = collectionLogs.stream().filter(log -> log.getCategory() == viewingCategory).collect(Collectors.toList());
                int idx = (component - 25) / 3;
                //player.getPackets().sendIComponentText(INTERFACE_ID, PROGRESS_TXT,"Kills:"); //components will have to move if you want longer test sadly.
                player.getPackets().sendIComponentText(INTERFACE_ID, KILLS_TXT, "0");
                if (idx < logs.size()) {
                    CollectionLog log = logs.get(idx);
                    player.getPackets().sendIComponentText(INTERFACE_ID, COLLECTION_TITLE, log.getName());
                    player.getPackets().sendIComponentText(INTERFACE_ID, OBTAINED_TXT, log.getAcquired().size() + "/" + log.getRequired().size());
                    player.getPackets().sendIComponentText(INTERFACE_ID, KILLS_TXT, getCount(log.getType().toLowerCase(), log.getArg().toLowerCase()) + "");
                    List<Item> filtered = log.getRequired().stream().map(i -> {
                        Optional<Item> acquired = log.getAcquired().stream().filter(i2 -> i2.getId() == i.getId()).findFirst();
                        return acquired.orElse(i);
                    }).collect(Collectors.toList());
                    player.getPackets().sendItems(CONTAINER_ID, filtered.toArray(new Item[0]));
                    player.getPackets().sendInterSetItemsOptionsScript(INTERFACE_ID, 273, CONTAINER_ID, 7, 20, "Examine");
                    player.getPackets().sendUnlockIComponentOptionSlots(INTERFACE_ID, 273, 0, 160, 0);
                }
            }
        }
    }

    private int getCount(String type, String arg) {
        switch (type) {
            case "npc":
                return player.getKillcountManager().getKillcount(Integer.parseInt(arg));
            case "raids":
                switch (arg) {
                    case "tob":
                        return 0;
                    case "xeric":
                        return player.elitedungeonone;
                }
            case "clues":
                switch (arg) {
                    case "easy":
                        return player.completedEasyClues;
                    case "medium":
                        return player.completedMediumClues;
                    case "hard":
                        return player.completedHardClues;
                    case "elite":
                        return player.completedEliteClues;
                    case "master":
                        return player.completedMasterClues;
                    case "reaperchest":
                        return player.reaperchests;
                    case "cchest":
                        return player.crystalchest;
                    case "tchest":
                        return player.toxicchest;
                    case "bchest":
                        return player.brimestonechest;
                    case "lchest":
                        return player.larranschest;
                    case "mbox":
                        return player.mbox;
                    case "tmbox":
                        return player.tmbox;
                    case "vmbox":
                        return player.vbox;
                    case "legg":
                        return player.legg;
                    case "pmbox":
                        return player.pvpbox;
                }
            case "OTHER":
                switch (arg) {
                    case "reaperchest":
                        return player.reaperchests;
                    case "cchest":
                        return player.crystalchest;
                    case "tchest":
                        return player.toxicchest;
                    case "bchest":
                        return player.brimestonechest;
                    case "lchest":
                        return player.larranschest;
                    case "mbox":
                        return player.mbox;
                    case "tmbox":
                        return player.tmbox;
                    case "vmbox":
                        return player.vbox;
                    case "legg":
                        return player.legg;
                    case "pmbox":
                        return player.pvpbox;
                }
                break;

        }
        return 0;
    }

    private void showCategory() {
        List<CollectionLog> logs = collectionLogs.stream().filter(log -> log.getCategory() == viewingCategory).collect(Collectors.toList());
        populateList(logs);
    }

    private void populateList(List<CollectionLog> logs) {
        for (int i = 25; i < 173; i += 3) {
            int index = (i - 25) / 3;
            if (index < logs.size()) {
                CollectionLog log = logs.get(index);
                player.getPackets().sendHideIComponent(INTERFACE_ID, i, false);
                player.getPackets().sendIComponentText(INTERFACE_ID, i + 1, log.toString());
            } else {
                player.getPackets().sendHideIComponent(INTERFACE_ID, i, true);
                player.getPackets().sendIComponentText(INTERFACE_ID, i + 1, "");
            }
        }
    }

    public boolean handleCollection(Item item) {
        boolean collected = false;
        for (CollectionLog log : collectionLogs) {
            if (log.requires(item)) {
                log.collect(item);
                collected = true;
            }
        }
        return collected;
    }

    public boolean addCollectionLog(CollectionLog log) {
        CollectionLog existing = getCollectionLog(log.getAliases());
        if (existing != null) {
            if (!existing.equals(log)) {
                collectionLogs.remove(existing);
                existing.setRequired(log.getRequired());
                existing.setName(log.getName());
                existing.setAliases(log.getAliases());
                existing.setCategory(log.getCategory());
                collectionLogs.add(existing);
                return true;
            }
        }
        return collectionLogs.add(log);
    }

    public CollectionLog getCollectionLog(List<String> aliases) {
        for (String alias : aliases) {
            CollectionLog collection = getCollectionLog(alias);
            if (collection != null) return collection;
        }
        return null;
    }

    public CollectionLog getCollectionLog(String name) {
         return collectionLogs.stream().filter(c -> c.getAliases().contains(name)).findFirst().orElse(null);
    }

    public enum Category {
        BOSSES,
        RAIDS,
        CLUES,
        OTHER
    }

}
