package com.rs.cache.loaders;

import com.rs.Constants;
import com.rs.cache.Cache;
import com.rs.network.io.InputStream;
import com.rs.utility.Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ClientScriptMap {

    private int length;
    private char aChar6337;
    private char aChar6345;
    private String defaultStringValue;
    private int defaultIntValue;
    private HashMap<Integer, Object> values;

    private static final ConcurrentHashMap<Integer, ClientScriptMap> interfaceScripts = new ConcurrentHashMap<Integer, ClientScriptMap>();

    public static void main(String[] args) throws IOException {
        // Cache.STORE = new Store("C:/.jagex_cache_32/runescape/");
        Cache.init();
    /*	ClientScriptMap names = ClientScriptMap.getMap(1345);
		ClientScriptMap hint1 = ClientScriptMap.getMap(952);
		ClientScriptMap hint2 = ClientScriptMap.getMap(1349);
		System.out.println(hint1);
		System.out.println(hint2);
		for (Object v : names.values.values()) {
			int key = (int) ClientScriptMap.getMap(1345).getKeyForValue(v);
			int id = ClientScriptMap.getMap(1351).getIntValue(key);

			/*
			 * String text = hint.getStringValue(key);
			 * if(text.equals("automatically.")) System.out.println(id);
			 */
		/*	String hint = hint1.getValues().containsKey((long) key) ? hint1
					.getStringValue(key) : hint2.getStringValue(key);

			System.out.println(id + ", " + v + "; " + hint + ", ");
		}*/
    }

	
	 /* int musicIndex = (int)
	  InterfaceScript.getInterfaceScript(1345).getKeyForValue
	  ("Astea Frostweb"); int id =
	 * InterfaceScript.getInterfaceScript(1351).getIntValue(musicIndex);
	 * System.out.println(id);
	 */
    //

    public static final ClientScriptMap getMap(int scriptId) {
        ClientScriptMap script = interfaceScripts.get(scriptId);
        if (script != null)
            return script;
        byte[] data = Cache.STORE.getIndexes()[17].getFile(
                scriptId >>> 8, scriptId & 0xff);
        script = new ClientScriptMap();
        if (data != null)
            script.readValueLoop(new InputStream(data));
        interfaceScripts.put(scriptId, script);
        return script;
    }

    public static ClientScriptMap getOSRSMap(int id) {
        return getMap(id + Constants.OSRS_ENUM_OFFSET);
    }

    public HashMap<Integer, Object> getValues() {
        return values;
    }

    public Object getValue(long key) {
        if (values == null)
            return null;
        return values.get(key);
    }

    public int getKeyForValue(Object value) {
        for (Integer key : values.keySet()) {
            if (values.get(key).equals(value))
                return key;
        }
        return -1;
    }

    public Map<Integer, Integer> ints() {
        Map<Integer, Integer> map = new HashMap<>(length);
        for (int i = 0; i < length; i++) {
            Object value = values.get(i);
            if (value instanceof Integer)
                map.put(i, (Integer) value);
        }
        return map;
    }

    public int getSize() {
        if (values == null)
            return 0;
        return values.size();
    }

    public int getIntValue(int key) {
        if (values == null)
            return defaultIntValue;
        Object value = values.get(key);
        if (!(value instanceof Integer))
            return defaultIntValue;
        return (Integer) value;
    }

    public String getStringValue(int key) {
        if (values == null)
            return defaultStringValue;
        Object value = values.get(key);
        if (!(value instanceof String))
            return defaultStringValue;
        return (String) value;
    }

    private void readValueLoop(InputStream stream) {
        for (; ; ) {
            int opcode = stream.readUnsignedByte();
            if (opcode == 0)
                break;
            readValues(stream, opcode);
        }
    }

    private void readValues(InputStream stream, int opcode) {
        if (opcode == 1)
            aChar6337 = Utils.method2782((byte) stream.readByte());
        else if (opcode == 2)
            aChar6345 = Utils.method2782((byte) stream.readByte());
        else if (opcode == 3)
            defaultStringValue = stream.readString();
        else if (opcode == 4)
            defaultIntValue = stream.readInt();
        else if (opcode == 5 || opcode == 6 || opcode == 7 || opcode == 8) {
            int length = stream.readUnsignedShort();
            int loop = opcode == 7 || opcode == 8 ? stream.readUnsignedShort()
                    : length;
            values = new HashMap<Integer, Object>(Utils.getHashMapSize(length));
            for (int i = 0; i < loop; i++) {
                int key = opcode == 7 || opcode == 8 ? stream
                        .readUnsignedShort() : stream.readInt();
                Object value = opcode == 5 || opcode == 7 ? stream.readString()
                        : stream.readInt();
                values.put(key, value);
            }
        }
    }

    private ClientScriptMap() {
        defaultStringValue = "null";
    }

    public int getDefaultIntValue() {
        return defaultIntValue;
    }
}
