package com.rs.utility.tools;

import com.google.common.collect.Lists;
import com.google.gson.*;
import com.rs.cache.Cache;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.world.entity.player.Rarity;
import javafx.util.Pair;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import org.w3c.dom.*;

import javax.annotation.Nullable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * NOT USED, not enough wiki data to continue
 *
 * @author ReverendDread
 * Created 3/13/2021 at 11:30 AM
 * @project 718---Server
 */
public class RsWikiDropsDumper {

//    private static final String DUMP_DIRECTORY = "C:\\Users\\Andrew\\Desktop\\drop_table_dumps\\";
//    private static final String FILE_DIRECTORY = "C:\\Users\\Andrew\\Desktop\\runescapewiki-latest-pages-articles-2012-11-19.xml";
//
//    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
//    private static String tableName = "";
//    private static boolean inDropTable = false;
//    private static final List<NPCDropTable> npcDropTables = Lists.newArrayList();
//
//    @SneakyThrows
//    public static void main(String[] args) {
//        File wikiFile = new File(FILE_DIRECTORY);
//        if (!wikiFile.exists())
//            throw new IllegalStateException("File doesn't exist.");
//        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//        DocumentBuilder builder = dbf.newDocumentBuilder();
//        Document document = builder.parse(wikiFile);
//        NodeList pages = document.getElementsByTagName("title");
//
//        Cache.init();
//
//        //cache all the item def
//        for (int i = 0; i < 30_000; i++) {
//            ItemDefinitions.forId(i);
//        }
//
////        int id = 13447; //bandos 6260
////        NPCDefinitions npcDefinitions = NPCDefinitions.getNPCDefinitions(id);
////        NPCDropTable table = getNPCDropTable(id, npcDefinitions.name, pages);
////        System.out.println(GSON.toJson(table));
//
//        for (int id = 13000; id < 14000; id++) {
//            NPCDefinitions definitions = NPCDefinitions.getNPCDefinitions(id);
//            String formattedName = cleanForWindowsString(definitions.getName());
//            if (!formattedName.trim().isEmpty()) {
//                System.out.println("dumping table for id: " + id + ", " + formattedName);
//                NPCDropTable table = getNPCDropTable(id, definitions.name, pages);
//                Files.write(Paths.get(DUMP_DIRECTORY + formattedName + ".json"), GSON.toJson(table).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
//            }
//        }
//    }
//
//    private static Pair<Integer, NPCDropTable> getNPCDropTableById(int id) {
//        for (int index = 0; index < npcDropTables.size(); index++) {
//            NPCDropTable table = npcDropTables.get(index);
//            if (table.getIds().contains(id)) {
//                return new Pair<>(index, table);
//            }
//        }
//        return new Pair<>(-1, null);
//    }
//
//    private static int getWeightForRarity(String rarity) {
//        switch (rarity.toLowerCase()) {
//            case "common":
//            case "varies":
//                return Rarity.COMMON.rate;
//            case "uncommon":
//                return Rarity.UNCOMMON.rate;
//            case "rare":
//                return Rarity.RARE.rate;
//            case "very rare":
//                return Rarity.VERY_RARE.rate;
//            default:
//                return 1;
//        }
//    }
//
//    private static Pair<Pair<Integer, Integer>, Boolean> extractMinMax(String s) {
//        if (!s.contains("-")) { //static amount
//            int amount = Integer.parseInt(s.replace("-", "").replace("(noted)", "").replace(" ", ""));
//            return new Pair<>(new Pair<>(amount, amount), s.contains("(noted)"));
//        }
//        String regex = "([0-9]+)"; //only digits
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(s);
//        int min = -1, max = 0;
//        while (matcher.find()) {
//            if (min == -1)
//                min = Integer.parseInt(matcher.group());
//            else
//                max = Integer.parseInt(matcher.group());
//        }
//        return new Pair<>(new Pair<>(min, max), s.contains("(noted)"));
//    }
//
//    private static String getPageTextContentByTitle(NodeList pages, String title) {
//        for (int i = 0; i < pages.getLength(); i++) {
//            Node node = pages.item(i);
//            String content = node.getTextContent();
//            if (content.equalsIgnoreCase(title)) {
//                Node parent = node.getParentNode();
//                return parent.getTextContent();
//            }
//        }
//        return null;
//    }
//
//    private static String extractVariable(String content, String variable) {
//        String clean = content.replace("DropsLine", "").replace("{{", "").replace("}}", "").replace(",", "").toLowerCase().substring(1);
//        int variableStart = clean.indexOf(variable + "=");
//        boolean hasSpaceStart = false;
//        if (variableStart == -1) {
//            variableStart = clean.indexOf(variable + " =");
//            hasSpaceStart = true;
//        }
//        String s = clean.substring(variableStart);
//        int begin = s.indexOf("=");
//        int end = s.indexOf('|');
//        String extracted;
//        if (end == -1)
//            extracted = s.substring(begin + (hasSpaceStart ? 2 : 1));
//        else
//            extracted = s.substring(begin + (hasSpaceStart ? 2 : 1), end);
//        return extracted;
//    }
//
//    private static NPCDropTable getNPCDropTable(int id, String name, NodeList pages) {
//        Pair<Integer, NPCDropTable> pair = getNPCDropTableById(id);
//        NPCDropTable table = pair.getValue();
//        if (table == null) {
//            table = new NPCDropTable();
//            table.getIds().add(id);
//            String content = getPageTextContentByTitle(pages, name);
//            if (content != null) {
//                String[] lines = content.split("\n");
//                //Stream.of(lines).forEach(System.out::println);
//                int idx = 0;
//                for (String l : lines) {
//                    if (!inDropTable && l.contains("DropsTableHead")) {
//                        tableName = lines[idx - 1].replace("=", "");
//                        inDropTable = true;
//                    } else {
//                        if (inDropTable && tableName != null) {
//                            try {
//                                Optional<ItemDropTable> dropTable = table.getItemDropTables().stream().filter(t -> t.getName().equalsIgnoreCase(tableName)).findFirst();
//                                if (l.contains("|}")) {
//                                    //dropTable.ifPresent(t -> /* set weight */);
//                                    inDropTable = false;
//                                }
//                                if (!l.equalsIgnoreCase("|}")) { //read a drop and add it to the table
//                                    String itemName = extractVariable(l, "name");
//                                    String itemQuantities = extractVariable(l, "quantity");
//                                    String itemRarity = extractVariable(l, "rarity");
//                                    Pair<Pair<Integer, Integer>, Boolean> minMaxPair = extractMinMax(itemQuantities);
//                                    if (tableName.equalsIgnoreCase("100% Drop")) {
//                                        table.getGuaranteed().add(new ItemDrop(getItemIdByName(itemName, itemName.contains("(noted)")), minMaxPair.getKey().getKey(), minMaxPair.getKey().getValue()));
//                                    } else {
//                                        if (!dropTable.isPresent()) { //add a new table
//                                            table.getItemDropTables().add(new ItemDropTable(tableName));
//                                        }
//                                        dropTable.ifPresent(t -> t.getDrops().add(new VariableItemDrop(getItemIdByName(itemName, itemName.contains("(noted)")), minMaxPair.getKey().getKey(), minMaxPair.getKey().getValue(), getWeightForRarity(itemRarity))));
//                                    }
//                                }
//                            } catch (Exception ex) {}
//                        }
//                    }
//                    idx++;
//                }
//            }
//            npcDropTables.add(table);
//        } else {
//            //replace the table with another id added.
//            table.getIds().add(id);
//            npcDropTables.set(pair.getKey(), table);
//            System.out.println("merge table for npc " + table.getIds());
//        }
//        return table;
//    }
//
//    private static int getItemIdByName(String name, boolean noted) {
//        ItemDefinitions definition = ItemDefinitions.forName(name);
//        if (definition == null)
//            throw new IllegalStateException("Can't find item definition for name " + name + ".");
//        return noted ? definition.certId : definition.id;
//    }
//
//    private static String cleanForWindowsString(String string) {
//        return string.
//        replace("?", "").
//        replace(":", "").
//        replace("<", "").
//        replace(">", "").
//        replace("|", "").
//        replace("*", "").
//        replace("\\", "").
//        replace("/", "");
//    }
//
//    @Getter
//    protected static class NPCDropTable {
//
//        private final List<Integer> ids = new ArrayList<>();
//        private final List<ItemDrop> guaranteed = new ArrayList<>();
//        private final List<ItemDropTable> itemDropTables = new ArrayList<>();
//
//        public int getTotalRarity() {
//            int total = 0;
//            for (ItemDropTable table : itemDropTables) {
//                total += table.getTotalRarity();
//            }
//            return total;
//        }
//
//    }
//
//    @Getter @AllArgsConstructor
//    protected static class ItemDropTable {
//
//        private String name;
//        private final List<ItemDrop> drops = new ArrayList<>();
//
//        public int getTotalRarity() {
//            int total = 0;
//            for (ItemDrop drop : drops) {
//                if (drop instanceof VariableItemDrop) {
//                    total += ((VariableItemDrop) drop).rarity;
//                }
//            }
//            return total;
//        }
//
//    }
//
//    @Getter @AllArgsConstructor
//    protected static class ItemDrop {
//
//        private int id;
//        private int min, max;
//
//    }
//
//    @Getter
//    protected static class VariableItemDrop extends ItemDrop {
//
//        private int rarity;
//
//        public VariableItemDrop(int id, int min, int max, int rarity) {
//            super(id, min, max);
//            this.rarity = rarity;
//        }
//
//    }
//
//    @AllArgsConstructor @Getter
//    protected enum Rarity {
//
//        COMMON(8), //8
//        UNCOMMON(32), //32
//        RARE(128), //128
//        VERY_RARE(256); //256
//
//        private int rate;
//    }

}
