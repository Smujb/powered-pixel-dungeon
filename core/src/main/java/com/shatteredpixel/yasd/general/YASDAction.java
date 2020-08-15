/*
 *
 *   Pixel Dungeon
 *   Copyright (C) 2012-2015 Oleg Dolya
 *
 *   Shattered Pixel Dungeon
 *   Copyright (C) 2014-2019 Evan Debenham
 *
 *   Powered Pixel Dungeon
 *   Copyright (C) 2014-2020 Samuel Braithwaite
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 *
 */

package com.shatteredpixel.yasd.general;

import com.badlogic.gdx.Input;
import com.watabou.input.GameAction;
import com.watabou.input.KeyBindings;
import com.watabou.utils.Bundle;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.FileUtils;

import org.jetbrains.annotations.Contract;

import java.io.IOException;
import java.util.LinkedHashMap;

public class YASDAction extends GameAction {

	//--Existing actions from GameAction
	public static final GameAction NONE  = GameAction.NONE;
	public static final GameAction BACK  = GameAction.BACK;
	//--

	protected YASDAction( String name ){
		super( name );
	}

	public static final GameAction HERO_INFO   = new YASDAction("hero_info");
	public static final GameAction JOURNAL     = new YASDAction("journal");

	public static final GameAction WAIT        = new YASDAction("wait");
	public static final GameAction SEARCH      = new YASDAction("search");
	public static final GameAction REST        = new YASDAction("rest");

	public static final GameAction INVENTORY   = new YASDAction("inventory");
	public static final GameAction QUICKSLOT_1 = new YASDAction("quickslot_1");
	public static final GameAction QUICKSLOT_2 = new YASDAction("quickslot_2");
	public static final GameAction QUICKSLOT_3 = new YASDAction("quickslot_3");
	public static final GameAction QUICKSLOT_4 = new YASDAction("quickslot_4");

	public static final GameAction TAG_ATTACK  = new YASDAction("tag_attack");
	public static final GameAction TAG_DANGER  = new YASDAction("tag_danger");
	public static final GameAction TAG_ACTION  = new YASDAction("tag_action");
	public static final GameAction TAG_LOOT    = new YASDAction("tag_loot");
	public static final GameAction TAG_RESUME  = new YASDAction("tag_resume");

	public static final GameAction ZOOM_IN     = new YASDAction("zoom_in");
	public static final GameAction ZOOM_OUT    = new YASDAction("zoom_out");

	public static final GameAction N           = new YASDAction("n");
	public static final GameAction E           = new YASDAction("e");
	public static final GameAction S           = new YASDAction("s");
	public static final GameAction W           = new YASDAction("w");
	public static final GameAction NE          = new YASDAction("ne");
	public static final GameAction SE          = new YASDAction("se");
	public static final GameAction SW          = new YASDAction("sw");
	public static final GameAction NW          = new YASDAction("nw");

	private static final LinkedHashMap<Integer, GameAction> defaultBindingsDesktop = new LinkedHashMap<>();
	private static final LinkedHashMap<Integer, GameAction> defaultBindingsAndroid = new LinkedHashMap<>();
	static {
		defaultBindingsDesktop.put( Input.Keys.ESCAPE,      YASDAction.BACK );
		defaultBindingsDesktop.put( Input.Keys.BACKSPACE,   YASDAction.BACK );

		defaultBindingsDesktop.put( Input.Keys.H,           YASDAction.HERO_INFO );
		defaultBindingsDesktop.put( Input.Keys.J,           YASDAction.JOURNAL );

		defaultBindingsDesktop.put( Input.Keys.SPACE,       YASDAction.WAIT );
		defaultBindingsDesktop.put( Input.Keys.S,           YASDAction.SEARCH );
		defaultBindingsDesktop.put( Input.Keys.Z,           YASDAction.REST );

		defaultBindingsDesktop.put( Input.Keys.I,           YASDAction.INVENTORY );
		defaultBindingsDesktop.put( Input.Keys.Q,           YASDAction.QUICKSLOT_1 );
		defaultBindingsDesktop.put( Input.Keys.W,           YASDAction.QUICKSLOT_2 );
		defaultBindingsDesktop.put( Input.Keys.E,           YASDAction.QUICKSLOT_3 );
		defaultBindingsDesktop.put( Input.Keys.R,           YASDAction.QUICKSLOT_4 );

		defaultBindingsDesktop.put( Input.Keys.A,           YASDAction.TAG_ATTACK );
		defaultBindingsDesktop.put( Input.Keys.TAB,         YASDAction.TAG_DANGER );
		defaultBindingsDesktop.put( Input.Keys.D,           YASDAction.TAG_ACTION );
		defaultBindingsDesktop.put( Input.Keys.ENTER,       YASDAction.TAG_LOOT );
		defaultBindingsDesktop.put( Input.Keys.T,           YASDAction.TAG_RESUME );

		defaultBindingsDesktop.put( Input.Keys.PLUS,        YASDAction.ZOOM_IN );
		defaultBindingsDesktop.put( Input.Keys.EQUALS,      YASDAction.ZOOM_IN );
		defaultBindingsDesktop.put( Input.Keys.MINUS,       YASDAction.ZOOM_OUT );

		defaultBindingsDesktop.put( Input.Keys.UP,          YASDAction.N );
		defaultBindingsDesktop.put( Input.Keys.RIGHT,       YASDAction.E );
		defaultBindingsDesktop.put( Input.Keys.DOWN,        YASDAction.S );
		defaultBindingsDesktop.put( Input.Keys.LEFT,        YASDAction.W );

		defaultBindingsDesktop.put( Input.Keys.NUMPAD_5,    YASDAction.WAIT );
		defaultBindingsDesktop.put( Input.Keys.NUMPAD_8,    YASDAction.N );
		defaultBindingsDesktop.put( Input.Keys.NUMPAD_9,    YASDAction.NE );
		defaultBindingsDesktop.put( Input.Keys.NUMPAD_6,    YASDAction.E );
		defaultBindingsDesktop.put( Input.Keys.NUMPAD_3,    YASDAction.SE );
		defaultBindingsDesktop.put( Input.Keys.NUMPAD_2,    YASDAction.S );
		defaultBindingsDesktop.put( Input.Keys.NUMPAD_1,    YASDAction.SW );
		defaultBindingsDesktop.put( Input.Keys.NUMPAD_4,    YASDAction.W );
		defaultBindingsDesktop.put( Input.Keys.NUMPAD_7,    YASDAction.NW );

		//defaultBindingsAndroid.put( Input.Keys.VOLUME_UP, YASDAction.TAG_DANGER );
		//defaultBindingsAndroid.put( Input.Keys.VOLUME_DOWN, YASDAction.SEARCH );
	}


	@Contract(" -> new")
	public static LinkedHashMap<Integer, GameAction> getDefaults() {
		return DeviceCompat.isDesktop() ? new LinkedHashMap<>(defaultBindingsDesktop) : new LinkedHashMap<>(defaultBindingsAndroid);
	}

	//hard bindings for android devices
	static {
		KeyBindings.addHardBinding( Input.Keys.BACK, YASDAction.BACK );
		KeyBindings.addHardBinding( Input.Keys.MENU, YASDAction.INVENTORY );
	}

	//we only save/loads keys which differ from the default configuration.
	private static final String BINDINGS_FILE = "keybinds.dat";

	public static void loadBindings(){
		try {
			Bundle b = FileUtils.bundleFromFile(BINDINGS_FILE);

			Bundle firstKeys = b.getBundle("first_keys");
			Bundle secondKeys = b.getBundle("second_keys");

			LinkedHashMap<Integer, GameAction> defaults = getDefaults();
			LinkedHashMap<Integer, GameAction> custom = new LinkedHashMap<>();

			for (GameAction a : allActions()) {
				if (firstKeys.contains(a.name())) {
					if (firstKeys.getInt(a.name()) == 0){
						for (int i : defaults.keySet()){
							if (defaults.get(i) == a){
								defaults.remove(i);
								break;
							}
						}
					} else {
						custom.put(firstKeys.getInt(a.name()), a);
						defaults.remove(firstKeys.getInt(a.name()));
					}
				}

				//we store any custom second keys in defaults for the moment to preserve order
				//incase the 2nd key is custom but the first one isn't
				if (secondKeys.contains(a.name())) {
					if (secondKeys.getInt(a.name()) == 0){
						int last = 0;
						for (int i : defaults.keySet()){
							if (defaults.get(i) == a){
								last = i;
							}
						}
						defaults.remove(last);
					} else {
						defaults.remove(secondKeys.getInt(a.name()));
						defaults.put(secondKeys.getInt(a.name()), a);
					}
				}

			}

			//now merge them and store
			for( int i : defaults.keySet()){
				if (i != 0) {
					custom.put(i, defaults.get(i));
				}
			}

			KeyBindings.setAllBindings(custom);

		} catch (Exception e){
			KeyBindings.setAllBindings(getDefaults());
		}
	}

	public static void saveBindings(){
		Bundle b = new Bundle();

		Bundle firstKeys = new Bundle();
		Bundle secondKeys = new Bundle();

		for (GameAction a : allActions()){
			int firstCur = 0;
			int secondCur = 0;
			int firstDef = 0;
			int secondDef = 0;

			for (int i : defaultBindingsDesktop.keySet()){
				if (defaultBindingsDesktop.get(i) == a){
					if(firstDef == 0){
						firstDef = i;
					} else {
						secondDef = i;
					}
				}
			}

			LinkedHashMap<Integer, GameAction> curBindings = KeyBindings.getAllBindings();
			for (int i : curBindings.keySet()){
				if (curBindings.get(i) == a){
					if(firstCur == 0){
						firstCur = i;
					} else {
						secondCur = i;
					}
				}
			}

			if (firstCur != firstDef){
				firstKeys.put(a.name(), firstCur);
			}
			if (secondCur != secondDef){
				secondKeys.put(a.name(), secondCur);
			}

		}

		b.put("first_keys", firstKeys);
		b.put("second_keys", secondKeys);

		try {
			FileUtils.bundleToFile(BINDINGS_FILE, b);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
