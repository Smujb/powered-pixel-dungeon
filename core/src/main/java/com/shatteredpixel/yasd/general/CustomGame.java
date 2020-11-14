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

    public static final String CUSTOM_MODIFIERS_FILE = "global_custom_modifiers.dat";

    private static ArrayMap<Modifier, Float> cachedGlobals = null;

    public enum Modifier {
        MOB_DAMAGE_FACTOR,
        MOB_HP_FACTOR;
        private float floatValue = 1f;

        public float getLocal() {
            return floatValue;
        }

        public float getGlobal() {
            return getGlobals().get(this);
        }

        public void setValue(float value) {
            floatValue = value;
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

    public CustomGame setupLocals() {
        for (Modifier modifier : Modifier.values()) {
            modifier.floatValue = getGlobals().get(modifier);
        }
        return this;
    }

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
