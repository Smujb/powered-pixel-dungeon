package com.shatteredpixel.yasd;

import com.shatteredpixel.yasd.general.MainGame;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

import java.lang.reflect.InvocationTargetException;

public enum ModHandler implements Bundlable {
	NONE,
	YASD,
	TEST,
	CURSED;

	public static ModHandler mod = YASD;

	private static String MOD = "mod";

	@Override
	public void restoreFromBundle(Bundle bundle) {
		bundle.put(MOD, (Enum<?>) mod);
	}

	@Override
	public void storeInBundle(Bundle bundle) {
		mod = bundle.getEnum(MOD, ModHandler.class);
	}

	public String getPackage() {
		String base = "com.shatteredpixel.yasd.";
		switch (this) {
			case NONE: default:
				return base + "general.";
			case YASD:
				return base + "yasd.";
			case TEST:
				return base + "test.";
			case CURSED:
				return base + "cursed.";
		}
	}

	//Note that it can only replace the class in ...yasd.general. not anywhere else. It's only possible to do this also if the replacement extends the original.
	public static  <T> Class<T> replaceType(Class<T> cl) {
		return ModHandler.mod.replaceClass(cl);
	}

	public <T> Class<T> replaceClass(Class<T> cl) {
		String className = cl.getName();
		className = className.replace(NONE.getPackage(), mod.getPackage() + "_");
		try {
			return (Class<T>) Class.forName(className);
		} catch (Exception e) {
			return cl;
		}
	}

	public static <T> T createObject(Class<T> cl, Object...args) {
		return ModHandler.mod.newObject(cl, args);
	}

	public <T> T newObject(Class<T> cl, Object...args) {
		cl = replaceClass(cl);
		Class[] placeholder = new Class[args.length];
		try {
			if (args.length == 0) {
				return cl.newInstance();
			} else {
				return cl.getDeclaredConstructor(placeholder).newInstance(args);
			}
		} catch (InstantiationException e) {
			MainGame.reportException(e);
		} catch (IllegalAccessException e) {
			MainGame.reportException(e);
		} catch (NoSuchMethodException e) {
			MainGame.reportException(e);
		} catch (InvocationTargetException e) {
			MainGame.reportException(e);
		}
		return null;
	}
}
