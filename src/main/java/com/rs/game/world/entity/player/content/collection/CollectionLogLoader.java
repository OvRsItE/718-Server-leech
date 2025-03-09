package com.rs.game.world.entity.player.content.collection;

import com.google.gson.*;
import com.rs.game.item.Item;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CollectionLogLoader {

    private static final String DIR = "./data/collections";
    private static final Path PATH = Paths.get(DIR);
    private static final Gson GSON = new Gson();
    public static final List<CollectionLog> COLLECTION_LOG_CACHE = new ArrayList<>();

    public static void load() throws IOException {
        File folder = Paths.get(DIR).toFile();
        if (!folder.exists() && !folder.mkdir())
            throw new IllegalStateException("Can't create folder for collection logs!");

        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                CollectionLog log = GSON.fromJson(new FileReader(file.getPath()), CollectionLog.class);
                COLLECTION_LOG_CACHE.add(log);
            }
        }
    }

    private static <T> List<T> toList(JsonArray array, Class<T> t) {
        List<T> list = new ArrayList<>();
        if (array != null) {
            for (int i = 0; i < array.size(); i++) {
                list.add(GSON.fromJson(array, t));
            }
        }
        return list;
    }

}
