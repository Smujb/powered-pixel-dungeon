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

package com.shatteredpixel.yasd.general.items.weapon.enchantments;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.FlavourBuff;
import com.shatteredpixel.yasd.general.items.weapon.Weapon;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSprite;
import com.shatteredpixel.yasd.general.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Blocking extends Weapon.Enchantment {
	
	private static ItemSprite.Glowing BLUE = new ItemSprite.Glowing( 0x0000FF );
	
	@Override
	public int proc(Weapon weapon, Char attacker, Char defender, int damage) {
		
		int level = Math.max( 0, weapon.level() );
		
		Buff.prolong(attacker, BlockBuff.class, 2 + level/2).setBlocking(level + 1);
		
		return damage;
	}
	
	@Override
	public ItemSprite.Glowing glowing() {
		return BLUE;
	}
	
	public static class BlockBuff extends FlavourBuff {
		
		private int blocking = 0;
		
		public void setBlocking( int blocking ){
			this.blocking = blocking;
		}
		
		public int blockingRoll(){
			return Random.NormalIntRange(0, blocking);
		}
		
		@Override
		public int icon() {
			return BuffIndicator.ARMOR;
		}
		
		@Override
		public void tintIcon(Image icon) {
			icon.tint(0, 0.5f, 1, 0.5f);
		}
		
		@Override
		public String toString() {
			return Messages.get(this, "name");
		}
		
		@Override
		public String desc() {
			return Messages.get(this, "desc", blocking, dispTurns());
		}
		
		private static final String BLOCKING = "blocking";
		
		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(BLOCKING, blocking);
		}
		
		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			blocking = bundle.getInt(BLOCKING);
		}
	
	}
}
