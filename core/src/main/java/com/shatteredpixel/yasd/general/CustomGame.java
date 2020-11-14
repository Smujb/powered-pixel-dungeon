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

import com.badlogic.gdx.utils.ArrayMap;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.FileUtils;

import java.io.IOException;

public class CustomGame implements Bundlable {
    //Custom mode modifiers are stored twice: Globally and locally
    //Global custom modifiers are default settings when starting a custom run, and are changed/displayed by custom mode window
    //Local custom modifiers are not allowed to be modified during a game, and are copied from the globals at the start of a new game (see Dungeon.customGame)

    public static final String CUSTOM_MODIFIERS_FILE = "global_custom_modifiers.dat";

    //Initially set to null so the game knows to load the modifiers from the bundle
    private static ArrayMap<Modifier, Float> cachedGlobals = null;

    public enum Modifier {
        //TODO more modifiers
        MOB_DAMAGE_FACTOR,
        MOB_HP_FACTOR;
        private float floatValue = 1f;

        public float getLocal() {
            //Don't let this function be called outside of a context where the hero exists or there will be buggy results (most likely getGlobal should have been used)
            if (Dungeon.hero == null) throw new RuntimeException("Attempted to access local modifiers from a global context");
            return floatValue;
        }

        public float getGlobal() {
            //No checks are done for this (because they can't really be) but do not use this to actually apply the modifier, or the player can modify one run from the start scene of another
            return getGlobals().get(this);
        }

        public void setGlobalValue(float value) {
            //Sets the *global* value. Local values are set during setupLocals and copied from globals.
            getGlobals().put(this, value);
            storeGlobals();
        }
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        for (Modifier modifier : Modifier.values()) {
            bundle.put(modifier.toString(), modifier.floatValue);
        }
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        for (Modifier modifier : Modifier.values()) {
            modifier.floatValue = bundle.getFloat(modifier.toString());
        }
    }

    //Copy global modifiers into local per-run modifiers
    public CustomGame setupLocals() {
        for (Modifier modifier : Modifier.values()) {
            modifier.floatValue = getGlobals().get(modifier);
        }
        return this;
    }

    //Gets the global modifiers from the bundle and caches them in an array to increase performance.
    private static ArrayMap<Modifier, Float> getGlobals() {

        if (cachedGlobals != null) {
            return cachedGlobals;
        } else {
            cachedGlobals = new ArrayMap<>();
        }

        try {
            Bundle bundle = FileUtils.bundleFromFile(CUSTOM_MODIFIERS_FILE);
            for (Modifier modifier : Modifier.values()) {
                if (bundle.contains(modifier.toString())) {
                    modifier.floatValue = bundle.getFloat(modifier.toString());
                }
            }
            return cachedGlobals;
        } catch (IOException e) {
            PPDGame.reportException(e);
            return new ArrayMap<>();
        }
    }

    //Stores the current global modifier cache to the file
    private static void storeGlobals() {

        Bundle bundle = new Bundle();
        for (Modifier modifier : Modifier.values()) bundle.put( modifier.toString(), modifier.floatValue );

        try {
            FileUtils.bundleToFile(CUSTOM_MODIFIERS_FILE, bundle );
        } catch (IOException e) {
            PPDGame.reportException(e);
        }
    }
}
