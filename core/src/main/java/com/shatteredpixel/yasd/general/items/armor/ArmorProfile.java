/*
 *
 *   Pixel Dungeon
 *   Copyright (C) 2012-2015 Oleg Dolya
 *
 *   Shattered Pixel Dungeon
 *   Copyright (C) 2014-2019 Evan Debenham
 *
 *   Powered Pixel Dungeon
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
import com.shatteredpixel.yasd.general.items.randomiser.Profile;
import com.shatteredpixel.yasd.general.items.randomiser.Randomisable;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

import org.jetbrains.annotations.NotNull;

public enum ArmorProfile implements Profile {
	NONE {
		{
			image = ItemSpriteSheet.Armors.DISC;
			appearance = 4;
		}
	},
	STEALTH_INCREASE {
		{
			image = ItemSpriteSheet.Armors.HIDE;
			appearance = 2;
		}
	},
	STEALTH_DECREASE {
		{
			image = ItemSpriteSheet.Armors.BANDED;
			appearance = 5;
		}
	},
	SPEED_INCREASE {
		{
			image = ItemSpriteSheet.Armors.LEATHER;
			appearance = 2;
		}
	},
	SPEED_DECREASE {
		{
			image = ItemSpriteSheet.Armors.WARRIOR;
			appearance = 5;
		}
	},
	EVASION_INCREASE {
		{
			image = ItemSpriteSheet.Armors.LIGHT;
			appearance = 4;
		}
	},
	EVASION_DECREASE {
		{
			image = ItemSpriteSheet.Armors.MAIL;
			appearance = 3;
		}
	},
	PHYSICAL {
		{
			image = ItemSpriteSheet.Armors.PLATE;
			appearance = 5;
		}
	},
	MAGICAL {
		{
			image = ItemSpriteSheet.Armors.ETHEREAL;
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
	public float match(@NotNull Randomisable item) {
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
				if (armor.physicalDamageFactor < 0.8f) {
					return 1/armor.physicalDamageFactor;
				} else {
					return 1f;
				}
			case MAGICAL:
				if (armor.magicDamageFactor < 0.8f) {
					return 1/armor.magicDamageFactor;
				} else {
					return 1f;
				}
		}
	}

	@Override
	public Item copy(@NotNull Randomisable item) {
		RandomArmor armor = ((RandomArmor)item);
		armor.image = image;
		armor.desc = armorDesc();
		armor.setName(armorName());
		armor.appearance = appearance;
		return armor;
	}
}
