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

public class PPDAction extends GameAction {

	//--Existing actions from GameAction
	public static final GameAction NONE  = GameAction.NONE;
	public static final GameAction BACK  = GameAction.BACK;
	//--

	protected PPDAction(String name ){
		super( name );
	}

	public static final GameAction HERO_INFO   = new PPDAction("hero_info");
	public static final GameAction JOURNAL     = new PPDAction("journal");

	public static final GameAction WAIT        = new PPDAction("wait");
	public static final GameAction SEARCH      = new PPDAction("search");
	public static final GameAction REST        = new PPDAction("rest");

	public static final GameAction INVENTORY   = new PPDAction("inventory");
	public static final GameAction QUICKSLOT_1 = new PPDAction("quickslot_1");
	public static final GameAction QUICKSLOT_2 = new PPDAction("quickslot_2");
	public static final GameAction QUICKSLOT_3 = new PPDAction("quickslot_3");
	public static final GameAction QUICKSLOT_4 = new PPDAction("quickslot_4");

	public static final GameAction TAG_ATTACK  = new PPDAction("tag_attack");
	public static final GameAction TAG_DANGER  = new PPDAction("tag_danger");
	public static final GameAction TAG_ACTION  = new PPDAction("tag_action");
	public static final GameAction TAG_LOOT    = new PPDAction("tag_loot");
	public static final GameAction TAG_RESUME  = new PPDAction("tag_resume");

	public static final GameAction ZOOM_IN     = new PPDAction("zoom_in");
	public static final GameAction ZOOM_OUT    = new PPDAction("zoom_out");

	public static final GameAction N           = new PPDAction("n");
	public static final GameAction E           = new PPDAction("e");
	public static final GameAction S           = new PPDAction("s");
	public static final GameAction W           = new PPDAction("w");
	public static final GameAction NE          = new PPDAction("ne");
	public static final GameAction SE          = new PPDAction("se");
	public static final GameAction SW          = new PPDAction("sw");
	public static final GameAction NW          = new PPDAction("nw");

	private static final LinkedHashMap<Integer, GameAction> defaultBindingsDesktop = new LinkedHashMap<>();
	private static final LinkedHashMap<Integer, GameAction> defaultBindingsAndroid = new LinkedHashMap<>();
	static {
		defaultBindingsDesktop.put( Input.Keys.ESCAPE,      PPDAction.BACK );
		defaultBindingsDesktop.put( Input.Keys.BACKSPACE,   PPDAction.BACK );

		defaultBindingsDesktop.put( Input.Keys.H,           PPDAction.HERO_INFO );
		defaultBindingsDesktop.put( Input.Keys.J,           PPDAction.JOURNAL );

		defaultBindingsDesktop.put( Input.Keys.SPACE,       PPDAction.WAIT );
		defaultBindingsDesktop.put( Input.Keys.S,           PPDAction.SEARCH );
		defaultBindingsDesktop.put( Input.Keys.Z,           PPDAction.REST );

		defaultBindingsDesktop.put( Input.Keys.I,           PPDAction.INVENTORY );
		defaultBindingsDesktop.put( Input.Keys.Q,           PPDAction.QUICKSLOT_1 );
		defaultBindingsDesktop.put( Input.Keys.W,           PPDAction.QUICKSLOT_2 );
		defaultBindingsDesktop.put( Input.Keys.E,           PPDAction.QUICKSLOT_3 );
		defaultBindingsDesktop.put( Input.Keys.R,           PPDAction.QUICKSLOT_4 );

		defaultBindingsDesktop.put( Input.Keys.A,           PPDAction.TAG_ATTACK );
		defaultBindingsDesktop.put( Input.Keys.TAB,         PPDAction.TAG_DANGER );
		defaultBindingsDesktop.put( Input.Keys.D,           PPDAction.TAG_ACTION );
		defaultBindingsDesktop.put( Input.Keys.ENTER,       PPDAction.TAG_LOOT );
		defaultBindingsDesktop.put( Input.Keys.T,           PPDAction.TAG_RESUME );

		defaultBindingsDesktop.put( Input.Keys.PLUS,        PPDAction.ZOOM_IN );
		defaultBindingsDesktop.put( Input.Keys.EQUALS,      PPDAction.ZOOM_IN );
		defaultBindingsDesktop.put( Input.Keys.MINUS,       PPDAction.ZOOM_OUT );

		defaultBindingsDesktop.put( Input.Keys.UP,          PPDAction.N );
		defaultBindingsDesktop.put( Input.Keys.RIGHT,       PPDAction.E );
		defaultBindingsDesktop.put( Input.Keys.DOWN,        PPDAction.S );
		defaultBindingsDesktop.put( Input.Keys.LEFT,        PPDAction.W );

		defaultBindingsDesktop.put( Input.Keys.NUMPAD_5,    PPDAction.WAIT );
		defaultBindingsDesktop.put( Input.Keys.NUMPAD_8,    PPDAction.N );
		defaultBindingsDesktop.put( Input.Keys.NUMPAD_9,    PPDAction.NE );
		defaultBindingsDesktop.put( Input.Keys.NUMPAD_6,    PPDAction.E );
		defaultBindingsDesktop.put( Input.Keys.NUMPAD_3,    PPDAction.SE );
		defaultBindingsDesktop.put( Input.Keys.NUMPAD_2,    PPDAction.S );
		defaultBindingsDesktop.put( Input.Keys.NUMPAD_1,    PPDAction.SW );
		defaultBindingsDesktop.put( Input.Keys.NUMPAD_4,    PPDAction.W );
		defaultBindingsDesktop.put( Input.Keys.NUMPAD_7,    PPDAction.NW );

		//defaultBindingsAndroid.put( Input.Keys.VOLUME_UP, YASDAction.TAG_DANGER );
		//defaultBindingsAndroid.put( Input.Keys.VOLUME_DOWN, YASDAction.SEARCH );
	}


	@Contract(" -> new")
	public static LinkedHashMap<Integer, GameAction> getDefaults() {
		return DeviceCompat.isDesktop() ? new LinkedHashMap<>(defaultBindingsDesktop) : new LinkedHashMap<>(defaultBindingsAndroid);
	}

	//hard bindings for android devices
	static {
		KeyBindings.addHardBinding( Input.Keys.BACK, PPDAction.BACK );
		KeyBindings.addHardBinding( Input.Keys.MENU, PPDAction.INVENTORY );
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
