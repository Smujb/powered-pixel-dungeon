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

import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.KindOfWeapon;
import com.shatteredpixel.yasd.general.items.Profile;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

import org.jetbrains.annotations.NotNull;

public enum WeaponProfile implements Profile {
	NONE {
		{
			image = ItemSpriteSheet.SWORD;
		}
	},
	SLOW {
		{
			image = ItemSpriteSheet.GLAIVE;
		}
	},
	FAST {
		{
			image = ItemSpriteSheet.GAUNTLETS;
		}
	},
	ACCURATE {
		{
			image = ItemSpriteSheet.HAND_AXE;
		}
	},
	INACCURATE {
		{
			image = ItemSpriteSheet.FLAIL;
		}
	},
	WEAK {
		{
			image = ItemSpriteSheet.WORN_SHORTSWORD;
		}
	},
	STRONG {
		{
			image = ItemSpriteSheet.GREATSWORD;
		}
	},
	BLOCKING {
		{
			image = ItemSpriteSheet.GREATSHIELD;
		}
	},
	REACH {
		{
			image = ItemSpriteSheet.WHIP;
		}
	},
	DUAL_HANDED {
		{
			image = ItemSpriteSheet.GREATAXE;
		}
	},
	BLUNT {
		{
			image = ItemSpriteSheet.WAR_HAMMER;
		}
	},
	CANT_SURPRISE_ATTK {
		{
			image = ItemSpriteSheet.FLAIL;
		}
	},
	SURPRISE_ATTK_BENEFIT {
		{
			image = ItemSpriteSheet.ASSASSINS_BLADE;
		}
	};

	@Override
	public Item copy(@NotNull Item item) {
		MeleeWeapon weapon = ((MeleeWeapon)item);
		weapon.image = image;
		weapon.desc = weaponDesc();
		weapon.setName(weaponName());
		return weapon;
	}

	public int image;

	public String weaponName() {
		return Messages.get(WeaponProfile.class, name() +  "_name");
	}

	public String weaponDesc() {
		return Messages.get(WeaponProfile.class, name() +  "_desc");
	}

	@Override
	public float match(Item item) {
		MeleeWeapon weapon = ((MeleeWeapon)item);
		switch (this) {
			default:
				return 1f;
			case SLOW:
				return weapon.DLY;
			case FAST:
				return 1/weapon.DLY;
			case ACCURATE:
				return weapon.ACC;
			case INACCURATE:
				return 1/weapon.ACC;
			case WEAK:
				return weapon.degradeFactor;
			case STRONG:
				return 1/weapon.degradeFactor;
			case BLOCKING:
				return 1f + weapon.defenseMultiplier;
			case REACH:
				return (float) (Math.pow(1.5f, weapon.RCH-1));
			case DUAL_HANDED:
				if (weapon.properties.contains(KindOfWeapon.Property.DUAL_HANDED)) {
					return KindOfWeapon.Property.DUAL_HANDED.importance();
				} else {
					return 1f;
				}
			case BLUNT:
				if (weapon.properties.contains(KindOfWeapon.Property.BLUNT)) {
					return KindOfWeapon.Property.BLUNT.importance();
				} else {
					return 1f;
				}
			case CANT_SURPRISE_ATTK:
				if (weapon.properties.contains(KindOfWeapon.Property.CANT_SURPRISE_ATTK)) {
					return KindOfWeapon.Property.CANT_SURPRISE_ATTK.importance();
				} else {
					return 1f;
				}
			case SURPRISE_ATTK_BENEFIT:
				if (weapon.properties.contains(KindOfWeapon.Property.SURPRISE_ATTK_BENEFIT)) {
					return KindOfWeapon.Property.SURPRISE_ATTK_BENEFIT.importance();
				} else {
					return 1f;
				}
		}
	}
}
