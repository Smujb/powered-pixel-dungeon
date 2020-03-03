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
import com.shatteredpixel.yasd.general.actors.buffs.Ooze;
import com.shatteredpixel.yasd.general.items.Generator;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.quest.GooBlob;
import com.shatteredpixel.yasd.general.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.yasd.general.sprites.CausticSlimeSprite;
import com.shatteredpixel.yasd.general.sprites.SlimeSprite;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

public class Slime extends Mob {
	
	{
		spriteClass = SlimeSprite.class;
		
		//HP = HT = 30;
		healthFactor = 2f;
		//defenseSkill = 5;
		
		EXP = 4;
		maxLvl = 9;
		
		lootChance = 0.1f;
	}
	
	/*@Override
	public int damageRoll() {
		return Random.NormalIntRange( 1, 8 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 10;
	}*/
	
	@Override
	public void damage(int dmg, Object src, Element element, boolean ignoresDefense) {
		if (dmg >= 5){
			//takes 5/6/7/8/9/10 dmg at 5/7/10/14/19/25 incoming dmg
			dmg = 4 + (int)(Math.sqrt(8*(dmg - 4) + 1) - 1)/2;
		}
		super.damage(dmg, src, element, ignoresDefense);
	}
	
	@Override
	protected Item createLoot() {
		Generator.Category c = Generator.Category.WEP_T2;
		MeleeWeapon w = (MeleeWeapon) Reflection.newInstance(c.classes[Random.chances(c.probs)]);
		w.random();
		w.level(0);
		return w;
	}

	public static class CausticSlime extends Slime {

		{
			spriteClass = CausticSlimeSprite.class;

			properties.add(Property.ACIDIC);
		}

		@Override
		public int attackProc(Char enemy, int damage ) {
			if (Random.Int( 2 ) == 0) {
				Buff.affect( enemy, Ooze.class ).set( 20f );
				enemy.sprite.burst( 0x000000, 5 );
			}

			return super.attackProc( enemy, damage );
		}

		@Override
		public void rollToDropLoot() {
			if (Dungeon.hero.lvl > Dungeon.getScaleFactor() + 3) return;

			super.rollToDropLoot();

			int ofs;
			do {
				ofs = PathFinder.NEIGHBOURS8[Random.Int(8)];
			} while (!Dungeon.level.passable()[pos + ofs]);
			Dungeon.level.drop( Reflection.newInstance(  GooBlob.class ), pos + ofs ).sprite.drop( pos );
		}
	}
}