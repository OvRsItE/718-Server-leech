package com.rs.utility.tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rs.game.world.entity.npc.Drop;
import com.rs.utility.NPCDrops;
import com.rs.utility.NPCDrops.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;

/**
 * @author ReverendDread
 * Created 3/15/2021 at 2:20 AM
 * @project 718---Server
 */
public class DropTableFormatConverter {

    private static final String DUMP_DIRECTORY = "C:\\Users\\Andrew\\Desktop\\drop_table_converts\\";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void main(String[] args) throws IOException {
        NPCDrops.init();
        for (Map.Entry<Integer, Drop[]> dropTable : NPCDrops.getDropMap().entrySet()) {
            encode_table(dropTable.getKey(), dropTable.getValue());
        }
    }

    private static void encode_table(int id, Drop[] drops) throws IOException {
        NPCDrops.ItemDropTable table = new NPCDrops.ItemDropTable();
        for (Drop drop : drops) {
            if (drop.getRate() == 100)
                table.getGuaranteed().add(new NPCDrops.StaticDropItem(drop.getItemId(), drop.getMinAmount(), drop.getMaxAmount()));
            else
                table.getDrops().add(new NPCDrops.VariableDropItem(drop.getItemId(), drop.getMinAmount(), drop.getMaxAmount(), drop.getRate()));
        }
        Files.write(Paths.get(DUMP_DIRECTORY + id + ".json"), GSON.toJson(table).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
    }

}
