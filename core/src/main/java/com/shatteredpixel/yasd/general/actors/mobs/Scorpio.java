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
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Cripple;
import com.shatteredpixel.yasd.general.actors.buffs.Light;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.food.MysteryMeat;
import com.shatteredpixel.yasd.general.items.potions.PotionOfHealing;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.sprites.AcidicSprite;
import com.shatteredpixel.yasd.general.sprites.ScorpioSprite;
import com.watabou.utils.Random;

public class Scorpio extends RangedMob {
	
	{
		spriteClass = ScorpioSprite.class;
		
		HP = HT = 95;
		defenseSkill = 29;
		viewDistance = Light.DISTANCE;
		
		EXP = 14;
		maxLvl = 25;

		baseSpeed = 0.8f;

		loot = new  PotionOfHealing();
		lootChance = 0.2f;

		properties.add(Property.DEMONIC);
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 26, 36 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 40;
	}
	
	@Override
	public int drRoll(Element element) {
		return Random.NormalIntRange(0, 20);
	}

	@Override
	public boolean canHit(Char enemy) {
		return new  Ballistica( pos, enemy.pos, Ballistica.PROJECTILE).collisionPos == enemy.pos;
	}

	@Override
	public boolean fleesAtMelee() {
		return true;
	}

	@Override
	public int defenseProc(Char enemy, int damage, Element element) {
		damage = super.defenseProc(enemy, damage, element);
		if (Random.Int( 2 ) == 0) {
			Buff.prolong( enemy, Cripple.class, Cripple.DURATION );
		}

		return damage;
	}

	@Override
	public int attackProc( Char enemy, int damage ) {
		damage = super.attackProc( enemy, damage );
		if (Random.Int( 2 ) == 0) {
			Buff.prolong( enemy, Cripple.class, Cripple.DURATION );
		}
		
		return damage;
	}
	
	@Override
	protected Item createLoot() {
		//(9-count) / 9 chance of getting healing, otherwise mystery meat
		if (Random.Float() < ((9f - Dungeon.LimitedDrops.SCORPIO_HP.count) / 9f)) {
			Dungeon.LimitedDrops.SCORPIO_HP.count++;
			return (Item)loot;
		} else {
			return new  MysteryMeat();
		}
	}

	public static class Acidic extends Scorpio {

		{
			spriteClass = AcidicSprite.class;

			properties.add(Property.ACIDIC);
		}

		@Override
		public Element elementalType() {
			return Element.ACID;
		}

		/*@Override
		public int defenseProc(Char enemy, int damage, Element element) {

			Buff.affect(enemy, Ooze.class).set(20f);

			return super.defenseProc( enemy, damage, element);
		}*/

	}
	
}
