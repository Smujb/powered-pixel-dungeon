/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.yasd.items.weapon.melee;

import com.shatteredpixel.yasd.actors.Char;
import com.shatteredpixel.yasd.actors.hero.Hero;
import com.shatteredpixel.yasd.actors.mobs.Mob;
import com.shatteredpixel.yasd.messages.Messages;
import com.shatteredpixel.yasd.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Sneak extends MeleeWeapon {

	{
		image = ItemSpriteSheet.ASSASSINS_BLADE;

		tier = 1;
		damageMultiplier = 0.80f;
	}


	@Override
	public int damageRoll(Char owner) {
		if (owner instanceof Hero) {
			Hero hero = (Hero)owner;
			Char enemy = hero.enemy();
			if (enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero)) {
				//deals 50% toward max to max on surprise, instead of min to max.
				int diff = max() - min();
				int damage = augment.damageFactor(Random.NormalIntRange(
						min() + Math.round(diff*0.75f),
						max()));
				int exStr = hero.STR() - STRReq();
				if (exStr > 0) {
					damage += Random.IntRange(0, exStr);
				}
				return damage;
			}
		}
		return super.damageRoll(owner);
	}

	@Override
	public int image() {
		if (tier < 2) {
			return ItemSpriteSheet.DAGGER;
		} else if (tier < 3) {
			return ItemSpriteSheet.DIRK;
		} else {
			return ItemSpriteSheet.ASSASSINS_BLADE;
		}
	}

	@Override
	public String desc() {
		if (tier < 2) {
			return Messages.get(Dagger.class, "desc");
		} else if (tier < 3) {
			return Messages.get(Dirk.class, "desc");
		} else {
			return Messages.get(AssassinsBlade.class, "desc");
		}
	}

	@Override
	public String name() {
		if (tier < 2) {
			return Messages.get(Dagger.class, "name");
		} else if (tier < 3) {
			return Messages.get(Dirk.class, "name");
		} else {
			return Messages.get(AssassinsBlade.class, "name");
		}
	}

	private static class Dirk extends MeleeWeapon {}
	private static class Dagger extends MeleeWeapon {}
	private static class AssassinsBlade extends MeleeWeapon {}

}