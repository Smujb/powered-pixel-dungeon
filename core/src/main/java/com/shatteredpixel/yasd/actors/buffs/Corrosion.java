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

package com.shatteredpixel.yasd.actors.buffs;

import com.shatteredpixel.yasd.Dungeon;
import com.shatteredpixel.yasd.actors.hero.Hero;
import com.shatteredpixel.yasd.items.Item;
import com.shatteredpixel.yasd.messages.Messages;
import com.shatteredpixel.yasd.ui.BuffIndicator;
import com.shatteredpixel.yasd.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Corrosion extends Buff implements Hero.Doom {

	private float damage = 1;
	protected float left;

	private static final String DAMAGE	= "damage";
	private static final String LEFT	= "left";

	{
		type = buffType.NEGATIVE;
		announced = true;
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( DAMAGE, damage );
		bundle.put( LEFT, left );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		damage = bundle.getFloat( DAMAGE );
		left = bundle.getFloat( LEFT );
	}

	public void set(float duration, int damage) {
		this.left = Math.max(duration, left);
		if (this.damage < damage) this.damage = damage;
	}
	
	@Override
	public int icon() {
		return BuffIndicator.POISON;
	}
	
	@Override
	public void tintIcon(Image icon) {
		icon.hardlight(1f, 0.5f, 0f);
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}
	
	@Override
	public String heroMessage() {
		return Messages.get(this, "heromsg");
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", dispTurns(left), (int)damage);
	}

	@Override
	public boolean act() {
		if (target.isAlive()) {
			target.damage((int)damage, this);
			if (target.belongings != null) {
				int index = Random.Int(5);
				Item item = target.belongings.miscs[index];
				if (item != null && item.canDegrade()) {
					item.use(damage*10);
				}
			}
			if (damage < (Dungeon.depth/2f)+2) {
				damage++;
			} else {
				damage += 0.5f;
			}
			
			spend( TICK );
			if ((left -= TICK) <= 0) {
				detach();
			}
		} else {
			detach();
		}

		return true;
	}
	
	@Override
	public void onDeath() {
		Dungeon.fail( getClass() );
		GLog.n(Messages.get(this, "ondeath"));
	}

}
