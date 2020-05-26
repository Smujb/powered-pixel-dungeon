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

import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.Profile;

public enum ArmorProfile implements Profile {
	NONE,
	STEALTH_INCREASE,
	STEALTH_DECREASE,
	SPEED_INCREASE,
	SPEED_DECREASE,
	EVASION_INCREASE,
	EVASION_DECREASE,
	PHYSICAL,
	MAGICAL;

	@Override
	public float match(Item item) {
		Armor armor = ((Armor)item);
		switch (this) {
			default:
				return 1f;
			case STEALTH_INCREASE:
				return armor.STE;
			case STEALTH_DECREASE:
				return 1/armor.STE;
			case SPEED_INCREASE:
				return armor.speedFactor;
			case SPEED_DECREASE:
				return 1/armor.speedFactor;
			case EVASION_INCREASE:
				return armor.EVA;
			case EVASION_DECREASE:
				return 1/armor.EVA;
			case PHYSICAL:
				return armor.DRfactor;
			case MAGICAL:
				return armor.magicalDRFactor;
		}
	}

	@Override
	public Item copy(Item item) {
		return item;
	}
}
