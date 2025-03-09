package com.rs.utility.tools;

import com.alex.store.Index;
import com.alex.store.Store;
import com.rs.cache.Cache;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ModelDumper {
    public ModelDumper() {
    }

    public static void main(String[] args) throws IOException {
        Cache.STORE = new Store("C:/Users/Corey/Desktop/data/", false);
        Index index = Cache.STORE.getIndexes()[53];
        System.out.println(index.getLastArchiveId());

        for(int i = 0; i < index.getLastArchiveId(); ++i) {
            byte[] data = index.getFile(i);
            if (data != null) {
                writeFile(data, "C:/Users/Corey/Desktop/900/" + i + ".png");
            }
        }

    }

    public static void writeFile(byte[] data, String fileName) throws IOException {
        OutputStream out = new FileOutputStream(fileName);
        out.write(data);
        out.close();
    }
}