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

package com.shatteredpixel.yasd.general.items.armor;

import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class RandomArmor extends Armor {

    protected String desc = null;


    @Override
    public String desc() {
        return desc;
    }

    public void setName(String name) {
        this.name = name;
    }

    private static float randomStat() {
        int num = Random.Int(5, 20);
        return num/10f;
    }

    private void resetStats() {
        EVA = 1f;
        STE = 1f;
        speedFactor = 1f;
        magicDamageFactor = 1f;
        physicalDamageFactor = 1f;
        regenFactor = 1f;
    }

    public RandomArmor rollStats() {
        resetStats();
        final int nProps = 6;
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

        for (int i = 0; i < nProps; i++) {
            if (propertiesEnabled[i]) {
                switch (i) {
                    case 0:
                        EVA = randomStat();
                        break;
                    case 1:
                        STE = randomStat();
                        break;
                    case 2:
                        speedFactor = randomStat();
                        break;
                    case 3:
                        regenFactor = randomStat();
                        break;
                    case 4:
                        physicalDamageFactor = Random.NormalFloat(0.5f, 1);
                        break;
                    case 5:
                        magicDamageFactor = Random.NormalFloat(0.5f, 1);
                        break;
                }
            }
        }
        return matchProfile();
    }

    private ArmorProfile profile = ArmorProfile.NONE;

    @Contract(" -> this")
    public RandomArmor matchProfile() {
        //Weapons that are only very slightly different from the basic weapon get it's image and description.
        float closestMatch = 1.1f;
        ArmorProfile closestMatchProfile = ArmorProfile.NONE;
        //Shuffle list first in case two are tied for first place, to give all an equal chance. Randomness is fine as the image variable is stored in bundles, so it won't change for an individual weapon.
        ArrayList<ArmorProfile> profiles = new ArrayList<>(Arrays.asList(ArmorProfile.values()));
        Collections.shuffle(profiles);
        for (ArmorProfile profile : profiles) {
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

    private static final String STEALTH = "stealth";
    private static final String EVASION = "evasion";
    private static final String SPEED = "speed";
    private static final String MAGICAL_DR = "magic-dr";
    private static final String PHYSICAL_DR = "phys-dr";
    private static final String APPEARANCE = "appearance";
    private static final String REGEN_FACTOR = "regen-factor";
    private static final String PROFILE = "profile";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(STEALTH, STE);
        bundle.put(EVASION, EVA);
        bundle.put(SPEED, speedFactor);
        bundle.put(MAGICAL_DR, magicDamageFactor);
        bundle.put(PHYSICAL_DR, physicalDamageFactor);
        bundle.put(PROFILE, profile);
        bundle.put(APPEARANCE, appearance);
        bundle.put(REGEN_FACTOR, regenFactor);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        STE = bundle.getFloat(STEALTH);
        EVA = bundle.getFloat(EVASION);
        speedFactor = bundle.getFloat(SPEED);
        magicDamageFactor = bundle.getFloat(MAGICAL_DR);
        physicalDamageFactor = bundle.getFloat(PHYSICAL_DR);
        appearance = bundle.getInt(APPEARANCE);
        regenFactor = bundle.getFloat(REGEN_FACTOR);
        if (bundle.contains(PROFILE)) {
            profile = bundle.getEnum(PROFILE, ArmorProfile.class);
            profile.copy(this);
        } else {
            matchProfile();
        }
    }
}
