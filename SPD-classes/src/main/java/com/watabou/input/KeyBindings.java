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

package com.watabou.input;

import com.badlogic.gdx.Input;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class KeyBindings {

	private static LinkedHashMap<Integer, GameAction> bindings = new LinkedHashMap<>();

	public static void addKeyBinding(int keyCode, GameAction action){
		bindings.put(keyCode, action);
	}

	public static void clearKeyBindings(){
		bindings.clear();
	}

	public static LinkedHashMap<Integer, GameAction> getAllBindings(){
		return new LinkedHashMap<>(bindings);
	}

	public static void setAllBindings(LinkedHashMap<Integer, GameAction> newBindings){
		bindings = new LinkedHashMap<>(newBindings);
	}

	public static boolean acceptUnbound = false;

	public static boolean isKeyBound(int keyCode){
		if (keyCode <= 0 || keyCode > 255){
			return false;
		}
		return acceptUnbound || bindings.containsKey( keyCode );
	}

	public static GameAction getActionForKey(KeyEvent event){
		return bindings.get( event.code );
	}

	public static ArrayList<Integer> getKeysForAction(GameAction action ){
		ArrayList<Integer> result = new ArrayList<>();
		for( int i : bindings.keySet()){
			if (bindings.get(i) == action){
				result.add(i);
			}
		}
		return result;
	}

	public static String getKeyName( int keyCode ){
		if (keyCode == Input.Keys.UNKNOWN){
			return "None";
		} else if (keyCode == Input.Keys.PLUS){
			return "+";
		} else {
			return Input.Keys.toString(keyCode);
		}
	}

}
