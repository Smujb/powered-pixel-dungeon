/*
 *
 *   Pixel Dungeon
 *   Copyright (C) 2012-2015 Oleg Dolya
 *
 *   Shattered Pixel Dungeon
 *   Copyright (C) 2014-2019 Evan Debenham
 *
 *   Yet Another Shattered Dungeon
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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;

public class YASDAction extends GameAction {

	//--Existing actions from GameAction
	public static final GameAction NONE  = GameAction.NONE;

	public static final GameAction BACK  = GameAction.BACK;
	public static final GameAction MENU  = GameAction.MENU;
	//--

	protected YASDAction( String name ){
		super( name );
	}

	public static final GameAction HERO_INFO   = new YASDAction("hero_info");
	public static final GameAction JOURNAL     = new YASDAction("journal");

	public static final GameAction WAIT        = new YASDAction("wait");
	public static final GameAction SEARCH      = new YASDAction("search");

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

	private static final LinkedHashMap<Integer, GameAction> defaultBindings = new LinkedHashMap<>();
	static {
		defaultBindings.put( Input.Keys.BACK,        YASDAction.BACK );
		defaultBindings.put( Input.Keys.MENU,        YASDAction.MENU );

		defaultBindings.put( Input.Keys.H,           YASDAction.HERO_INFO );
		defaultBindings.put( Input.Keys.J,           YASDAction.JOURNAL );

		defaultBindings.put( Input.Keys.SPACE,       YASDAction.WAIT );
		defaultBindings.put( Input.Keys.S,           YASDAction.SEARCH );

		defaultBindings.put( Input.Keys.I,           YASDAction.INVENTORY );
		defaultBindings.put( Input.Keys.Q,           YASDAction.QUICKSLOT_1 );
		defaultBindings.put( Input.Keys.W,           YASDAction.QUICKSLOT_2 );
		defaultBindings.put( Input.Keys.E,           YASDAction.QUICKSLOT_3 );
		defaultBindings.put( Input.Keys.R,           YASDAction.QUICKSLOT_4 );

		defaultBindings.put( Input.Keys.A,           YASDAction.TAG_ATTACK );
		defaultBindings.put( Input.Keys.TAB,         YASDAction.TAG_DANGER );
		defaultBindings.put( Input.Keys.D,           YASDAction.TAG_ACTION );
		defaultBindings.put( Input.Keys.ENTER,       YASDAction.TAG_LOOT );
		defaultBindings.put( Input.Keys.T,           YASDAction.TAG_RESUME );

		defaultBindings.put( Input.Keys.PLUS,        YASDAction.ZOOM_IN );
		defaultBindings.put( Input.Keys.EQUALS,      YASDAction.ZOOM_IN );
		defaultBindings.put( Input.Keys.MINUS,       YASDAction.ZOOM_OUT );

		defaultBindings.put( Input.Keys.UP,          YASDAction.N );
		defaultBindings.put( Input.Keys.RIGHT,       YASDAction.E );
		defaultBindings.put( Input.Keys.DOWN,        YASDAction.S );
		defaultBindings.put( Input.Keys.LEFT,        YASDAction.W );

		defaultBindings.put( Input.Keys.NUMPAD_5,    YASDAction.WAIT );
		defaultBindings.put( Input.Keys.NUMPAD_8,    YASDAction.N );
		defaultBindings.put( Input.Keys.NUMPAD_9,    YASDAction.NE );
		defaultBindings.put( Input.Keys.NUMPAD_6,    YASDAction.E );
		defaultBindings.put( Input.Keys.NUMPAD_3,    YASDAction.SE );
		defaultBindings.put( Input.Keys.NUMPAD_2,    YASDAction.S );
		defaultBindings.put( Input.Keys.NUMPAD_1,    YASDAction.SW );
		defaultBindings.put( Input.Keys.NUMPAD_4,    YASDAction.W );
		defaultBindings.put( Input.Keys.NUMPAD_7,    YASDAction.NW );
	}

	@NotNull
	@Contract(" -> new")
	public static LinkedHashMap<Integer, GameAction> getDefaults() {
		return new LinkedHashMap(defaultBindings);
	}

	//TODO save functionality for changed keys
	public static void initialize(){
		KeyBindings.setAllBindings(getDefaults());

	}

	//file name? perhaps

	public static void loadBindings(){

	}

	public static void saveBindings(){

	}
}
