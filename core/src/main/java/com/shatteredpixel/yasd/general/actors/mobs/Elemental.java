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

package com.shatteredpixel.yasd.general.actors.mobs;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Burning;
import com.shatteredpixel.yasd.general.actors.buffs.Chill;
import com.shatteredpixel.yasd.general.actors.buffs.Frost;
import com.shatteredpixel.yasd.general.actors.buffs.Wet;
import com.shatteredpixel.yasd.general.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.yasd.general.sprites.ElementalSprite;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

public class Elemental extends Mob {

	{
		spriteClass = ElementalSprite.class;
		
		HP = HT = 65;
		defenseSkill = 24;
		
		EXP = 10;
		maxLvl = 20;
		
		flying = true;
		
		loot = Reflection.newInstance( PotionOfLiquidFlame.class );
		lootChance = 0.1f;
		
		properties.add(Property.FIERY);
	}

	@Override
	public Element elementalType() {
		return Element.FIRE;
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 16, 28 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 30;
	}
	
	@Override
	public int drRoll(Element element) {
		if (element.isMagical() && element != Element.WATER) {
			return Random.NormalIntRange(2, 8);
		} else {
			return Random.NormalIntRange(0, 5);
		}
	}

	@Override
	public int attackProc( Char enemy, int damage ) {
		damage = super.attackProc( enemy, damage );
		if (Random.Int( 2 ) == 0) {
			Buff.affect( enemy, Burning.class ).reignite( enemy );
		}
		
		return damage;
	}
	
	@Override
	public void add( Buff buff ) {
		if (buff instanceof Frost || buff instanceof Chill || buff instanceof Wet) {
				if (Dungeon.level.liquid()[this.pos])
					damage( Random.NormalIntRange( HT / 2, HT ), buff, Element.WATER, true);
				else
					damage( Random.NormalIntRange( 1, HT * 2 / 3 ), buff, Element.WATER, true );
		} else {
			super.add( buff );
		}
	}
	
}
