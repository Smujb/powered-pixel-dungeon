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

	public static void initDefaults() {

		KeyBindings.addKeyBinding( Input.Keys.BACK,        YASDAction.BACK );
		KeyBindings.addKeyBinding( Input.Keys.MENU,        YASDAction.MENU );

		KeyBindings.addKeyBinding( Input.Keys.H,           YASDAction.HERO_INFO );
		KeyBindings.addKeyBinding( Input.Keys.J,           YASDAction.JOURNAL );

		KeyBindings.addKeyBinding( Input.Keys.SPACE,       YASDAction.WAIT );
		KeyBindings.addKeyBinding( Input.Keys.S,           YASDAction.SEARCH );

		KeyBindings.addKeyBinding( Input.Keys.I,           YASDAction.INVENTORY );
		KeyBindings.addKeyBinding( Input.Keys.Q,           YASDAction.QUICKSLOT_1 );
		KeyBindings.addKeyBinding( Input.Keys.W,           YASDAction.QUICKSLOT_2 );
		KeyBindings.addKeyBinding( Input.Keys.E,           YASDAction.QUICKSLOT_3 );
		KeyBindings.addKeyBinding( Input.Keys.R,           YASDAction.QUICKSLOT_4 );

		KeyBindings.addKeyBinding( Input.Keys.A,           YASDAction.TAG_ATTACK );
		KeyBindings.addKeyBinding( Input.Keys.TAB,         YASDAction.TAG_DANGER );
		KeyBindings.addKeyBinding( Input.Keys.D,           YASDAction.TAG_ACTION );
		KeyBindings.addKeyBinding( Input.Keys.ENTER,       YASDAction.TAG_LOOT );
		KeyBindings.addKeyBinding( Input.Keys.T,           YASDAction.TAG_RESUME );

		KeyBindings.addKeyBinding( Input.Keys.PLUS,        YASDAction.ZOOM_IN );
		KeyBindings.addKeyBinding( Input.Keys.EQUALS,      YASDAction.ZOOM_IN );
		KeyBindings.addKeyBinding( Input.Keys.MINUS,       YASDAction.ZOOM_OUT );

		KeyBindings.addKeyBinding( Input.Keys.UP,          YASDAction.N );
		KeyBindings.addKeyBinding( Input.Keys.RIGHT,       YASDAction.E );
		KeyBindings.addKeyBinding( Input.Keys.DOWN,        YASDAction.S );
		KeyBindings.addKeyBinding( Input.Keys.LEFT,        YASDAction.W );

		KeyBindings.addKeyBinding( Input.Keys.NUMPAD_5,    YASDAction.WAIT );
		KeyBindings.addKeyBinding( Input.Keys.NUMPAD_8,    YASDAction.N );
		KeyBindings.addKeyBinding( Input.Keys.NUMPAD_9,    YASDAction.NE );
		KeyBindings.addKeyBinding( Input.Keys.NUMPAD_6,    YASDAction.E );
		KeyBindings.addKeyBinding( Input.Keys.NUMPAD_3,    YASDAction.SE );
		KeyBindings.addKeyBinding( Input.Keys.NUMPAD_2,    YASDAction.S );
		KeyBindings.addKeyBinding( Input.Keys.NUMPAD_1,    YASDAction.SW );
		KeyBindings.addKeyBinding( Input.Keys.NUMPAD_4,    YASDAction.W );
		KeyBindings.addKeyBinding( Input.Keys.NUMPAD_7,    YASDAction.NW );

	}

	//file name? perhaps

	public static void loadBindings(){

	}

	public static void saveBindings(){

	}
}
