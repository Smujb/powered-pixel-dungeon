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

import com.shatteredpixel.yasd.general.Badges;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.Statistics;
import com.shatteredpixel.yasd.general.items.food.MysteryMeat;
import com.shatteredpixel.yasd.general.sprites.PiranhaSprite;

import org.jetbrains.annotations.NotNull;

public class Piranha extends WaterMob {
	
	{
		spriteClass = PiranhaSprite.class;

		healthFactor = 1.5f;
		accuracyFactor = 2f;
		damageFactor = 1.5f;

		baseSpeed = 2f;
		
		EXP = 0;

		loot = MysteryMeat.class;
		lootChance = 1f;

		state = SLEEPING;
	}
	
	/*public Piranha() {
		super();
		
		HP = HT = 10 + Dungeon.getScaleFactor() * 5;
		defenseSkill = 10 + Dungeon.getScaleFactor() * 2;
	}*/
	
	@Override
	protected boolean act() {
		
		if (!Dungeon.level.liquid()[pos]) {
			die( new DamageSrc(Element.EARTH, null) );
			sprite.killAndErase();
			return true;
		} else {
			return super.act();
		}
	}
	
	/*@Override
	public int damageRoll() {
		return Random.NormalIntRange( Dungeon.getScaleFactor(), 4 + Dungeon.getScaleFactor() * 2 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 20 + Dungeon.getScaleFactor() * 2;
	}
	
	@Override
	public int drRoll(Element element) {
		return Random.NormalIntRange(0, Dungeon.getScaleFactor());
	}*/
	
	@Override
	public void die(@NotNull DamageSrc cause ) {
		super.die( cause );
		
		Statistics.piranhasKilled++;
		Badges.validatePiranhasKilled();
	}
	
	@Override
	public boolean reset() {
		return true;
	}
	

}
