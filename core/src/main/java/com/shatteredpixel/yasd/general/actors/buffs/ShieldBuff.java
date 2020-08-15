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

package com.shatteredpixel.yasd.general.actors.buffs;

import com.shatteredpixel.yasd.general.actors.Char;
import com.watabou.utils.Bundle;

import org.jetbrains.annotations.NotNull;

public abstract class ShieldBuff extends Buff {
	
	private int shielding;

	protected int shieldCap() {
		return -1;
	}
	
	@Override
	public boolean attachTo(@NotNull Char target) {
		if (super.attachTo(target)) {
			target.needsShieldUpdate = true;
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void detach() {
		target.needsShieldUpdate = true;
		super.detach();
	}

	private void fixShieldOverflow() {
		if (shieldCap() > 0 && shielding > shieldCap()) {
			shielding = shieldCap();
		} else if (shielding < 0) {
			shielding = 0;
			onZeroShield();
		}
	}
	
	public int shielding(){
		return shielding;
	}
	
	public void setShield( int shield ) {
		this.shielding = shield;
		fixShieldOverflow();
		if (target != null) target.needsShieldUpdate = true;
	}
	
	public final void incShield(){
		incShield(1);
	}
	
	public void incShield( int amt ){
		shielding += amt;
		fixShieldOverflow();
		if (target != null) target.needsShieldUpdate = true;
	}
	
	public final void decShield(){
		decShield(1);
	}
	
	public void decShield( int amt ){
		shielding -= amt;
		fixShieldOverflow();
		if (target != null) target.needsShieldUpdate = true;
	}
	
	//returns the amount of damage leftover
	public int absorbDamage(int dmg, Char.DamageSrc src){
		if (shielding >= dmg){
			shielding -= dmg;
			dmg = 0;
		} else {
			dmg -= shielding;
			shielding = 0;
		}
		if (shielding == 0){
			onZeroShield();
		}
		if (target != null) target.needsShieldUpdate = true;
		return dmg;
	}

	protected void onZeroShield() {
		detach();
	}
	
	private static final String SHIELDING = "shielding";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( SHIELDING, shielding);
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		shielding = bundle.getInt( SHIELDING );
	}
	
}
