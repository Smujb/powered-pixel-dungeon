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

package com.shatteredpixel.yasd.general.items.weapon.melee;

import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class Magical extends MeleeWeapon {

	{
		image = ItemSpriteSheet.RUNIC_BLADE;

		tier = 1;
	}

	@Override
	public String desc() {
		return Messages.get(RunicBlade.class, "desc");
	}

	@Override
	public String name() {
		return Messages.get(RunicBlade.class, "name");
	}

	@Override
	public int max(float lvl) {
		return  (int) (5*(tier) +
				Math.round(lvl*(tier*2.5)));
	}

	private static class RunicBlade extends MeleeWeapon {}
}
