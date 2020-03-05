/*
 *
 *   Pixel Dungeon
 *   Copyright (C) 2012-2015 Oleg Dolya
 *
 *   Shattered Pixel Dungeon
 *   Copyright (C) 2014-2019 Evan Debenham
 *
 *   Yet Another Shattered Dungeon
 *   Copyright (C) 2014-2020 Samuel Braithwaite
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 *
 */

package com.shatteredpixel.yasd.general.actors.buffs;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.ui.AirBar;
import com.watabou.utils.Bundle;

public class LimitedAir extends Buff {

	public static final String DURATION_KEY = "duration";

	public static final float DURATION = 25f;

	public float duration = DURATION;

	@Override
	public boolean attachTo(Char target) {
		if (target == Dungeon.hero) {
			AirBar.assignChar(Dungeon.hero);
		}
		return super.attachTo(target);
	}

	public float percentage() {
		return duration/DURATION;
	}

	public void reset() {
		duration = DURATION;
	}

	private static boolean needed(Char ch) {
		return Dungeon.underwater() && !ch.properties().contains(Char.Property.WATERY);
	}

	@Override
	public boolean act() {
		if (duration > 0) {
			duration--;
		} else {
			target.damage(target.HT/5, Element.WATER, true);
		}
		spend( TICK );
		return true;
	}

	public static void updateBuff(Char ch) {
		LimitedAir air = ch.buff(LimitedAir.class);
		if (LimitedAir.needed(ch) & air == null) {
			Buff.affect(ch, LimitedAir.class).reset();
		} else if (!LimitedAir.needed(ch) && air != null) {
			air.detach();
		}
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(DURATION_KEY, duration);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		duration = bundle.getFloat(DURATION_KEY);
	}
}
