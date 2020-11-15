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

    //Initially set to null so the game knows to load the modifiers/toggles from the bundle
    private static ArrayMap<Modifier, Float> cachedGlobalModifiers = null;
    private static ArrayMap<Toggle, Boolean> cachedGlobalToggles = null;


    //Locally stored values on a per-run basis
    private final ArrayMap<Modifier, Float> localModifiers = new ArrayMap<>();
    private final ArrayMap<Toggle, Boolean> localToggles = new ArrayMap<>();

    public enum Modifier {
        //TODO more modifiers
        HERO_HP_FACTOR {
            {
                positive = true;
            }
        },
        MOB_DAMAGE_FACTOR,
        MOB_HP_FACTOR;

        //If the modifier is positive (eg increases hero HP) then the game needs to know this when calculating how much easier/harder you have made the game
        protected boolean positive = false;

        public float getLocal() {
            if (Dungeon.customGame == null) throw new RuntimeException("Attempted to access local modifiers from a global context");
            return Dungeon.customGame.localModifiers.get(this, 1f);
        }

        public float getGlobal() {
            //No checks are done for this (because they can't really be) but do not use this to actually apply the modifier, or the player can modify one run from the start scene of another
            return getGlobalModifiers().get(this, 1f);
        }

        public void setGlobalValue(float value) {
            //Sets the *global* value. Local values are set during setupLocals and copied from globals.
            getGlobalModifiers().put(this, value);
            storeGlobals();
        }
    }

    public enum Toggle {
        //TODO endless mode
        ENDLESS;

        protected float difficultyFactor = 1f;

        public boolean getLocal() {
            if (Dungeon.customGame == null) throw new RuntimeException("Attempted to access local modifiers from a global context");
            return Dungeon.customGame.localToggles.get(this, false);
        }

        public boolean getGlobal() {
            //No checks are done for this (because they can't really be) but do not use this to actually apply the modifier, or the player can modify one run from the start scene of another
            return getGlobalToggles().get(this, false);
        }

        public void setGlobalValue(boolean value) {
            //Sets the *global* value. Local values are set during setupLocals and copied from globals.
            getGlobalToggles().put(this, value);
            storeGlobals();
        }
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        for (Modifier modifier : Modifier.values()) {
            localModifiers.put(modifier, bundle.getFloat(modifier.toString()));
        }
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        for (Modifier modifier : Modifier.values()) {
            bundle.put(modifier.toString(), localModifiers.get(modifier, 1f));
        }
    }

    //Copy global modifiers into local per-run modifiers, used when starting a custom run
    public CustomGame setupLocals() {
        for (Modifier modifier : Modifier.values()) {
            localModifiers.put(modifier, getGlobalModifiers().get(modifier, 1f));
        }
        for (Toggle toggle : Toggle.values()) {
            localToggles.put(toggle, getGlobalToggles().get(toggle, false));
        }
        return this;
    }

    //Reset local values, used when starting a new non-custom run
    public CustomGame resetLocals() {
        for (Modifier modifier : Modifier.values()) {
            localModifiers.put(modifier, 1f);
        }
        for (Toggle toggle : Toggle.values()) {
            localToggles.put(toggle, false);
        }
        return this;
    }

    public float calcTotalDifficultyFactor() {
        float factor = 1f;
        for (Modifier modifier : Modifier.values()) {
            if (modifier.positive) {
                factor *= 1/localModifiers.get(modifier, 1f);
            } else {
                factor *=  localModifiers.get(modifier, 1f);
            }
        }
        for (Toggle toggle : Toggle.values()) {
            if (localToggles.get(toggle, false)) {
                factor *= toggle.difficultyFactor;
            }
        }
        return factor;
    }

    //Gets the global modifiers from the bundle and caches them in an array to increase performance.
    private static ArrayMap<Modifier, Float> getGlobalModifiers() {

        if (cachedGlobalModifiers != null) {
            return cachedGlobalModifiers;
        } else {
            cachedGlobalModifiers = new ArrayMap<>();
        }

        try {
            Bundle bundle = FileUtils.bundleFromFile(CUSTOM_MODIFIERS_FILE);
            for (Modifier modifier : Modifier.values()) {
                if (bundle.contains(modifier.toString())) {
                    cachedGlobalModifiers.put(modifier, bundle.getFloat(modifier.toString()));
                }
            }
            return cachedGlobalModifiers;
        } catch (IOException e) {
            PPDGame.reportException(e);
            return new ArrayMap<>();
        }
    }

    //FIXME a lot of copy paste from previous function, surely there is a better way?
    //Gets the global toggles from the bundle and caches them in an array to increase performance.
    private static ArrayMap<Toggle, Boolean> getGlobalToggles() {

        if (cachedGlobalToggles != null) {
            return cachedGlobalToggles;
        } else {
            cachedGlobalToggles = new ArrayMap<>();
        }

        try {
            Bundle bundle = FileUtils.bundleFromFile(CUSTOM_MODIFIERS_FILE);
            for (Toggle toggle : Toggle.values()) {
                if (bundle.contains(toggle.toString())) {
                    cachedGlobalToggles.put(toggle, bundle.getBoolean(toggle.toString()));
                }
            }
            return cachedGlobalToggles;
        } catch (IOException e) {
            PPDGame.reportException(e);
            return new ArrayMap<>();
        }
    }

    //Stores the current global modifier cache and global toggle cache to the file
    private static void storeGlobals() {

        ArrayMap<Modifier, Float> modifiers = getGlobalModifiers();
        ArrayMap<Toggle, Boolean> toggles = getGlobalToggles();

        Bundle bundle = new Bundle();
        for (Modifier modifier : Modifier.values()) bundle.put( modifier.toString(), modifiers.get(modifier, 1f) );
        for (Toggle toggle : Toggle.values()) bundle.put( toggle.toString(), toggles.get(toggle, false) );

        try {
            FileUtils.bundleToFile(CUSTOM_MODIFIERS_FILE, bundle );
        } catch (IOException e) {
            PPDGame.reportException(e);
        }
    }
}
