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
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

import org.jetbrains.annotations.NotNull;

public enum ArmorProfile implements Profile {
	NONE {
		{
			image = ItemSpriteSheet.ARMOR_DISC;
			appearance = 4;
		}
	},
	STEALTH_INCREASE {
		{
			image = ItemSpriteSheet.ARMOR_HIDE;
			appearance = 2;
		}
	},
	STEALTH_DECREASE {
		{
			image = ItemSpriteSheet.ARMOR_BANDED;
			appearance = 5;
		}
	},
	SPEED_INCREASE {
		{
			image = ItemSpriteSheet.ARMOR_LEATHER;
			appearance = 2;
		}
	},
	SPEED_DECREASE {
		{
			image = ItemSpriteSheet.ARMOR_WARRIOR;
			appearance = 5;
		}
	},
	EVASION_INCREASE {
		{
			image = ItemSpriteSheet.ARMOR_DISC;
			appearance = 4;
		}
	},
	EVASION_DECREASE {
		{
			image = ItemSpriteSheet.ARMOR_MAIL;
			appearance = 3;
		}
	},
	PHYSICAL {
		{
			image = ItemSpriteSheet.ARMOR_PLATE;
			appearance = 5;
		}
	},
	MAGICAL {
		{
			image = ItemSpriteSheet.ARMOR_HOLDER;
			appearance = 3;
		}
	};

	public int image;
	public int appearance;

	public String armorName() {
		return Messages.get(ArmorProfile.class, name() +  "_name");
	}

	public String armorDesc() {
		return Messages.get(ArmorProfile.class, name() +  "_desc");
	}

	@Override
	public float match(@NotNull Item item) {
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
				if (armor.physicalResist > 1.2f) {
					return armor.physicalResist;
				} else {
					return 1f;
				}
			case MAGICAL:
				if (armor.magicalResist > 1.2f) {
					return armor.magicalResist;
				} else {
					return 1f;
				}
		}
	}

	@Override
	public Item copy(@NotNull Item item) {
		Armor armor = ((Armor)item);
		armor.image = image;
		armor.desc = armorDesc();
		armor.setName(armorName());
		armor.appearance = appearance;
		return armor;
	}
}
