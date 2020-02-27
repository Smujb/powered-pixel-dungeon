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
import com.shatteredpixel.yasd.general.actors.buffs.Barrier;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Paralysis;
import com.shatteredpixel.yasd.general.actors.buffs.Terror;
import com.shatteredpixel.yasd.general.items.Gold;
import com.shatteredpixel.yasd.general.items.weapon.enchantments.Grim;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.BruteSprite;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Brute extends Mob {
	
	{
		spriteClass = BruteSprite.class;
		
		HP = HT = 50;
		defenseSkill = 18;
		
		EXP = 8;
		maxLvl = 16;
		
		loot = Gold.class;
		lootChance = 0.5f;
	}
	
	private boolean enraged = false;
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		enraged = HP < HT / 4;
	}
	
	@Override
	public int damageRoll() {
		return enraged ?
			Random.NormalIntRange( 20, 45 ) :
			Random.NormalIntRange( 10, 26 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 23;
	}
	
	@Override
	public int drRoll(Element element) {
		return Random.NormalIntRange(0, 8);
	}
	
	@Override
	public void damage(int dmg, Object src, Element element, boolean ignoresDefense) {
		super.damage( dmg, src, element, ignoresDefense);
		
		if (isAlive() && !enraged && HP < HT / 4) {
			enraged = true;
			spend( TICK );
			if (Dungeon.level.heroFOV[pos]) {
				sprite.showStatus( CharSprite.NEGATIVE, Messages.get(this, "enraged") );
			}
			Buff.affect(this, Barrier.class).setShield(HT/2);
			HP = 1;
		}
	}
	
	{
		immunities.add( Terror.class );
		immunities.add( Paralysis.class );
		immunities.add( Grim.class);
	}
}
