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
import com.shatteredpixel.yasd.general.actors.buffs.Amok;
import com.shatteredpixel.yasd.general.actors.buffs.Terror;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.Imp;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.KindofMisc;
import com.shatteredpixel.yasd.general.items.food.Food;
import com.shatteredpixel.yasd.general.items.weapon.melee.Fist;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.MonkSprite;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Monk extends Mob {
	
	{
		spriteClass = MonkSprite.class;
		
		HP = HT = 70;
		defenseSkill = 34;
		
		EXP = 11;
		maxLvl = 21;

		DLY = 0.5f;
		
		loot = new  Food();
		lootChance = 0.083f;

		properties.add(Property.UNDEAD);
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 14, 30 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return defenseSkill;
	}
	
	@Override
	public int drRoll(Element element) {
		return Random.NormalIntRange(0, 2);
	}
	
	@Override
	public void rollToDropLoot() {
		Imp.Quest.process( this );
		
		super.rollToDropLoot();
	}

	private int hitsToDisarm = 0;
	
	@Override
	public int attackProc( Char enemy, int damage ) {
		damage = super.attackProc( enemy, damage );
		
		if (enemy.hasBelongings()) {
			int index = Random.Int(enemy.belongings.miscs.length);
			KindofMisc item = enemy.belongings.miscs[index];
			
			if ((item != null)
					&& !(item instanceof Fist)
					&& !item.cursed) {
				if (hitsToDisarm == 0) hitsToDisarm = Random.NormalIntRange(4, 8);

				if (--hitsToDisarm == 0) {
					enemy.belongings.miscs[index] = null;
					Dungeon.quickslot.convertToPlaceholder(item);
					Item.updateQuickslot();
					Dungeon.level.drop(item, enemy.pos).sprite.drop();
					if (enemy == Dungeon.hero) {
						GLog.w(Messages.get(this, "disarm", item.name()));
					}
				}
			}
		}
		
		return damage;
	}
	
	{
		immunities.add( Amok.class );
		immunities.add( Terror.class );
	}

	private static String DISARMHITS = "hitsToDisarm";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(DISARMHITS, hitsToDisarm);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		hitsToDisarm = bundle.getInt(DISARMHITS);
	}
}
