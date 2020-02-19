package com.shatteredpixel.yasd;

import com.shatteredpixel.yasd.general.GameSettings;
import com.shatteredpixel.yasd.general.MainGame;
import com.shatteredpixel.yasd.general.messages.Messages;

import java.lang.reflect.InvocationTargetException;

public enum ModHandler {
	NONE(0),
	YASD(1),
	TEST(2),
	CURSED(3);

	ModHandler(int value) {
		this.value = value;
	}

	private int value;

	public int getValue() {
		return value;
	}

	public static ModHandler mod() {
		int mod = GameSettings.mod();
		switch (mod) {
			default:
				return NONE;
			case 1:
				return YASD;
			case 2:
				return TEST;
			case 3:
				return CURSED;
		}
	}

	public static String modPackage() {
		return mod().getPackage();
	}

	@Override
	public String toString() {
		switch (this) {
			case NONE: default:
				return "general";
			case YASD:
				return "yasd";
			case TEST:
				return "test";
			case CURSED:
				return "cursed";
		}
	}

	public String getPackage() {
		String base = "com.shatteredpixel.yasd.";
		base += this.toString();
		return base + ".";
	}
	public static String getName() {
		return mod().displayName();
	}

	public String displayName() {
		return Messages.get(ModHandler.replaceClass(MainGame.class), this.toString());
	}

	//Note that it can only replace the class in ...yasd.general. not anywhere else. It's only possible to do this also if the replacement extends the original.

	public static  <T> Class<T> replaceClass(Class<T> cl) {
		String className = cl.getName();
		className = className.replace(NONE.getPackage(), modPackage() + "_");
		try {
			return (Class<T>) Class.forName(className);
		} catch (Exception e) {
			return cl;
		}
	}

	public static  <T> T newObject(Class<T> cl, Object...args) {
		cl = replaceClass(cl);
		// build matching class args
		Class<?>[] classArgs = new Class[args.length];
		for (int i = 0; i < args.length; i++) {
			classArgs[i] = args[i].getClass();
		}

		T result = null;
		try {
			if (args.length == 0) {
				result = cl.newInstance();
			} else {
				result = cl.getConstructor(classArgs).newInstance(args);
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

		assert result != null;

		return result;
	}
}
