/*
 *
 *  * Pixel Dungeon
 *  * Copyright (C) 2012-2015 Oleg Dolya
 *  *
 *  * Shattered Pixel Dungeon
 *  * Copyright (C) 2014-2019 Evan Debenham
 *  *
 *  * Powered Pixel Dungeon
 *  * Copyright (C) 2014-2020 Samuel Braithwaite
 *  *
 *  * This program is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 *
 */

package com.shatteredpixel.yasd.general.items.weapon.melee;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.messages.Messages;

public class Fist extends MeleeWeapon {

	{
		hitSound = Assets.Sounds.HIT_CRUSH;
		hitSoundPitch = 1.2f;

		tier = 1;
		DLY = 0.5f; //2x speed
		
		bones = false;

	}

	@Override
	public String desc() {
		if (tier < 3) {
			return Messages.get(Gloves.class, "desc");
		} else if (tier < 5) {
			return Messages.get(Knuckles.class, "desc");
		} else {
			return Messages.get(Gauntlet.class, "desc");
		}
	}

	@Override
	public int defenseFactor( Char owner ) {
		return tier;	//(tier) extra defence
	}

	//Placeholders for tiers.
	private static class Gloves extends MeleeWeapon {}

	private static class Knuckles extends MeleeWeapon {}

	private static class Gauntlet extends MeleeWeapon {}

}
