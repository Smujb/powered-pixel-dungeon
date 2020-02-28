/*
 *
 *  * Pixel Dungeon
 *  * Copyright (C) 2012-2015 Oleg Dolya
 *  *
 *  * Shattered Pixel Dungeon
 *  * Copyright (C) 2014-2019 Evan Debenham
 *  *
 *  * Yet Another Shattered Dungeon
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

package com.shatteredpixel.yasd.general.actors.mobs;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Chill;
import com.shatteredpixel.yasd.general.actors.buffs.Frost;
import com.shatteredpixel.yasd.general.items.quest.Embers;
import com.shatteredpixel.yasd.general.sprites.NewbornElementalSprite;

public class NewbornElemental extends Elemental {

	{
		spriteClass = NewbornElementalSprite.class;

		HT = 65;
		HP = HT/2; //32

		defenseSkill = 14;

		EXP = 7;

		properties.add(Property.MINIBOSS);
	}

	@Override
	public void add(Buff buff) {
		if (buff instanceof Frost || buff instanceof Chill) {
			die(buff);
		} else {
			super.add(buff);
		}
	}

	@Override
	public void die(Object cause) {
		super.die(cause);
		Dungeon.level.drop( new  Embers(), pos ).sprite.drop();
	}
}
