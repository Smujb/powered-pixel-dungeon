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

package com.shatteredpixel.yasd.general.items.weapon.melee;

import com.shatteredpixel.yasd.general.items.KindOfWeapon;
import com.shatteredpixel.yasd.general.items.randomiser.Randomisable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class RandomMeleeWeapon extends MeleeWeapon implements Randomisable {


    protected String desc = null;

    @Override
    public String desc() {
        return desc == null ? "" : desc;
    }

    public void setName(String name) {
        this.name = name;
    }

    private WeaponProfile profile = WeaponProfile.NONE;


    @Override
    public RandomMeleeWeapon resetStats() {
        DLY = 1f;
        ACC = 1f;
        degradeFactor = 1f;
        defenseMultiplier = 0f;
        RCH = 1;
        properties = new ArrayList<>();
        return this;
    }

    //Generates stats for the weapon.
    @Override
    public RandomMeleeWeapon rollStats() {
        resetStats();
        KindOfWeapon.Property[] basicProps = Property.values();
        int nProps = basicProps.length + 5;
        int maxProps = 0;
        while (maxProps < 4 && Random.Float() < (0.75f * Math.pow(0.5, maxProps))) {
            maxProps++;
        }
        boolean[] propertiesEnabled = new boolean[nProps];
        for (int i = 0; i < maxProps; i++) {
            int index = Random.Int(nProps);
            if (!propertiesEnabled[index]) {
                propertiesEnabled[index] = true;
            }
        }

        for (int i = 0; i < basicProps.length; i++) {
            Property property = basicProps[i];
            if (propertiesEnabled[i] && property.canApply(this)) {
                properties.add(property);
            }
        }

        for (int i = basicProps.length; i < nProps; i++) {
            if (propertiesEnabled[i]) {
                switch (i - basicProps.length) {
                    case 0:
                        DLY = Randomisable.randomStat();
                        break;
                    case 1:
                        ACC = Randomisable.randomStat();
                        break;
                    case 2:
                        degradeFactor = Randomisable.randomStat();
                        break;
                    case 3:
                        defenseMultiplier = Random.NormalFloat(0, 1);
                        break;
                    case 4:
                        RCH = Random.NormalIntRange(1, 3);
                        break;
                }
            }
        }

        return matchProfile();
    }

    @Contract(" -> this")
    @Override
    public RandomMeleeWeapon matchProfile() {
        //Weapons that are only very slightly different from the basic weapon get it's image and description.
        float closestMatch = 1.1f;
        WeaponProfile closestMatchProfile = WeaponProfile.NONE;
        //Shuffle list first in case two are tied for first place, to give all an equal chance. Randomness is fine as the image variable is stored in bundles, so it won't change for an individual weapon.
        ArrayList<WeaponProfile> profiles = new ArrayList<>(Arrays.asList(WeaponProfile.values()));
        Collections.shuffle(profiles);
        for (WeaponProfile profile : profiles) {
            float importance = profile.match(this);
            if (importance > closestMatch) {
                closestMatch = importance;
                closestMatchProfile = profile;
            }
        }
        closestMatchProfile.copy(this);
        profile = closestMatchProfile;
        return this;
    }


    private static final String DELAY = "delay";
    private static final String DEGRADE = "degrade-factor";
    private static final String ACCURACY = "accuracy";
    private static final String DEFENSEFACTOR = "defense-factor";
    private static final String REACH = "reach";
    private static final String PROPERTIES = "props";
    private static final String PROPERTIES_AMT = "num-props";
    private static final String PROFILE = "profile";
    private static final String SOUND = "sound";
    private static final String PITCH = "pitch";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(DELAY, DLY);
        bundle.put(DEGRADE, degradeFactor);
        bundle.put(ACCURACY, ACC);
        bundle.put(DEFENSEFACTOR, defenseMultiplier);
        bundle.put(REACH, RCH);
        bundle.put(PROPERTIES_AMT, properties.size());
        bundle.put(PROFILE, profile);
        bundle.put(SOUND, hitSound);
        bundle.put(PITCH, hitSoundPitch);
        for (int i = 0; i < properties.size(); i++) {
            bundle.put(PROPERTIES + i, properties.get(i));
        }
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        DLY = bundle.getFloat(DELAY);
        degradeFactor = bundle.getFloat(DEGRADE);
        ACC = bundle.getFloat(ACCURACY);
        defenseMultiplier = bundle.getFloat(DEFENSEFACTOR);
        RCH = bundle.getInt(REACH);
        hitSound = bundle.getString(SOUND);
        hitSoundPitch = bundle.getFloat(PITCH);
        int numProps = bundle.getInt(PROPERTIES_AMT);
        for (int i = 0; i < numProps; i++) {
            properties.add(bundle.getEnum(PROPERTIES + i, Property.class));
        }

        if (bundle.contains(PROFILE)) {
            profile = bundle.getEnum(PROFILE, WeaponProfile.class);
            profile.copy(this);
        } else {
            matchProfile();
        }
    }
}
