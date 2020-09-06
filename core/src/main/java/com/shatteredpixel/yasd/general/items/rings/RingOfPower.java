/*
 *
 *  * Pixel Dungeon
 *  * Copyright (C) 2012-2015 Oleg Dolya
 *  *
 *  * Shattered Pixel Dungeon
 *  * Copyright (C) 2014-2019 Evan Debenham
 *  *
 *  * Powered Pixel Dungeon
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

package com.shatteredpixel.yasd.general.items.rings;


import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class RingOfPower extends Ring {

	{
		icon = ItemSpriteSheet.Icons.RING_POWER;
	}

	@Override
	public boolean doEquip(Char ch) {
		if (super.doEquip(ch)){
			ch.updateHT( false );
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean doUnequip(Char ch, boolean collect, boolean single) {
		if (super.doUnequip(ch, collect, single)){
			ch.updateHT( false );
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Item upgrade() {
		super.upgrade();
		updateTargetHT();
		return this;
	}

	@Override
	public Item level(int value) {
		super.level(value);
		updateTargetHT();
		return this;
	}
	
	private void updateTargetHT(){
		if (buff != null && buff.target != null) {
			buff.target.updateHT( false );
		}
	}

	@Override
	protected RingBuff buff( ) {
		return new PowerBuff();
	}

	public String statsInfo() {
		if (isIdentified()){
			return Messages.get(this, "stats", soloBonus() * 2);
		} else {
			return Messages.get(this, "typical_stats", 2);
		}
	}

	public static int powerBonus(Char target ){
		return 2 * getBonus(target, PowerBuff.class);
	}

	public class PowerBuff extends RingBuff {}
}

