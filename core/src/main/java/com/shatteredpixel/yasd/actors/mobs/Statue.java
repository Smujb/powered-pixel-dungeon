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

package com.shatteredpixel.yasd.actors.mobs;

import com.shatteredpixel.yasd.Dungeon;
import com.shatteredpixel.yasd.actors.BelongingsHolder;
import com.shatteredpixel.yasd.actors.Char;
import com.shatteredpixel.yasd.items.Generator;
import com.shatteredpixel.yasd.items.KindofMisc;
import com.shatteredpixel.yasd.items.weapon.Weapon;
import com.shatteredpixel.yasd.items.weapon.Weapon.Enchantment;
import com.shatteredpixel.yasd.items.weapon.enchantments.Grim;
import com.shatteredpixel.yasd.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.yasd.journal.Notes;
import com.shatteredpixel.yasd.messages.Messages;
import com.shatteredpixel.yasd.sprites.StatueSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Statue extends BelongingsHolder {
	
	{
		spriteClass = StatueSprite.class;

		EXP = 0;
		state = PASSIVE;
		
		properties.add(Property.INORGANIC);

		STR = Integer.MAX_VALUE;
	}

	
	public Statue() {
		super();
		
		belongings.miscs[0] = newItem();
		belongings.miscs[1] = newItem();

		
		HP = HT = 20 + Dungeon.depth * 5;
		defenseSkill = 4 + Dungeon.depth;
	}

	public KindofMisc newItem() {
		int type = Random.Int(3);
		KindofMisc item;
		switch (type) {
			default:
				item = ((KindofMisc)Generator.random(Generator.Category.WEAPON));
				break;
			case 1:
				item = ((KindofMisc)Generator.random(Generator.Category.RING));
				break;
			case 2:
				item = ((KindofMisc)Generator.random(Generator.Category.ARMOR));
				break;
			case 3:
				item = ((KindofMisc)Generator.random(Generator.Category.WAND));
				break;
		}

		item.level(0);
		item.cursed = false;
		return item;
	}
	
	private static final String WEAPON	= "getWeapons";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
	}
	
	@Override
	protected boolean act() {
		if (Dungeon.level.heroFOV[pos]) {
			Notes.add( Notes.Landmark.STATUE );
		}
		return super.act();
	}
	
	@Override
	public void damage( int dmg, Object src ) {

		if (state == PASSIVE) {
			state = HUNTING;
			return;
		}
		super.damage( dmg, src );
	}
	
	@Override
	public void beckon( int cell ) {
		// Do nothing
	}
	
	@Override
	public void die( Object cause ) {
		for (int i=0; i < belongings.miscs.length; i++) {
			if (belongings.miscs[i] != null) {
				Dungeon.level.drop(belongings.miscs[i].identify(), pos).sprite.drop();
			}
		}
		super.die( cause );
	}
	
	@Override
	public void destroy() {
		Notes.remove( Notes.Landmark.STATUE );
		super.destroy();
	}
	
	@Override
	public boolean reset() {
		state = PASSIVE;
		return true;
	}

	@Override
	public String description() {
		String description = super.description() + "_";
		for (int i=0; i < belongings.miscs.length; i++) {
			if (belongings.miscs[i] != null) {
				description += (belongings.miscs[i].name()) + "_ \n\n_";
			}
		}
		return description + "_";
	}
	
	{
		resistances.add(Grim.class);
	}
	
}
