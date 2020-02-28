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
import com.shatteredpixel.yasd.general.items.Generator;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.potions.PotionOfHealing;
import com.shatteredpixel.yasd.general.items.weapon.enchantments.Grim;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.sprites.WarlockSprite;
import com.watabou.utils.Random;

public class Warlock extends RangedMob {
	
	{
		spriteClass = WarlockSprite.class;
		
		HP = HT = 80;
		defenseSkill = 21;
		
		EXP = 11;
		maxLvl = 21;

		DLY = 2f;
		
		loot = Generator.Category.POTION;
		lootChance = 0.83f;

		properties.add(Property.UNDEAD);
	}

	@Override
	public Element elementalType() {
		return Element.DARK;
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 16, 24 );
	}

	@Override
	public int attackSkill( Char target ) {
		return 30;
	}
	
	@Override
	public int drRoll(Element element) {
		return Random.NormalIntRange(0, 8);
	}

	@Override
	public boolean canHit(Char enemy) {
		return new  Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
	}

	@Override
	public boolean fleesAtMelee() {
		return false;
	}
	
	//used so resistances can differentiate between melee and magical attacks
	public static class DarkBolt extends MagicalDamage{}

	@Override
	public MagicalDamage magicalSrc() {
		return new  DarkBolt();
	}

	@Override
	public Item createLoot(){
		Item loot = super.createLoot();

		if (loot instanceof PotionOfHealing){

			//count/10 chance of not dropping potion
			if (Random.Float() < ((8f - Dungeon.LimitedDrops.WARLOCK_HP.count) / 8f)){
				Dungeon.LimitedDrops.WARLOCK_HP.count++;
			} else {
				return null;
			}

		}

		return loot;
	}

	{
		resistances.add( Grim.class );
	}
}
