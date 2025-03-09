package com.rs.game.world.entity.player.content.dialogue;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.dialogue.impl.SpinningD;
import com.rs.utility.Logger;



public final class DialogueHandler {

	private static final HashMap<Object, Class<Dialogue>> handledDialogues = new HashMap<Object, Class<Dialogue>>();
	
	@SuppressWarnings("rawtypes")
	private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
	    List<Class> classes = new ArrayList<Class>();
	    if (!directory.exists()) {
	        return classes;
	    }
	    File[] files = directory.listFiles();
	    for (File file : files) {
	        if (file.isDirectory()) {
	            assert !file.getName().contains(".");
	            classes.addAll(findClasses(file, packageName + "." + file.getName()));
	        } else if (file.getName().endsWith(".class")) {
	            classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
	        }
	    }
	    return classes;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static final void init() {
		String fileLoc = "/com/rs/game/world/entity/player/content/dialogue/impl";
		String packageDir = "com.rs.game.world.entity.player.content.dialogue.impl";
		try {
			final File f = new File(DialogueHandler.class.getProtectionDomain().getCodeSource().getLocation().getPath() + File.separator + fileLoc);
			List<Class> files = findClasses(f , packageDir);
			for (Class<Dialogue> c : files) {
				if (Dialogue.class.isAssignableFrom(c)) {
					handledDialogues.put(c.getSimpleName(), (Class<Dialogue>) Class.forName(c.getCanonicalName()));
				}
			}
			System.out.println("[DialogueHandler] Loaded " + handledDialogues.size() + " dialogues.");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static final void reload() {
		handledDialogues.clear();
		init();
	}

	public static final Dialogue getDialogue(Object key) {
		if (key instanceof Dialogue)
			return (Dialogue) key;
		Class<Dialogue> classD = handledDialogues.get(key);
		if (classD == null)
			return null;
		try {
			return classD.newInstance();
		} catch (Throwable e) {
			Logger.handle(e);
		}
		return null;
	}

	private DialogueHandler() {

	}
}
