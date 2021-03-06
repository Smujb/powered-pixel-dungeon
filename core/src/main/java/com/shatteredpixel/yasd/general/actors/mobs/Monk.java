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

package com.shatteredpixel.yasd.general.actors.mobs;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.Imp;
import com.shatteredpixel.yasd.general.items.food.Food;
import com.shatteredpixel.yasd.general.items.food.Pasty;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.MonkSprite;
import com.shatteredpixel.yasd.general.sprites.SeniorSprite;
import com.shatteredpixel.yasd.general.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Monk extends Mob {

	{
		spriteClass = MonkSprite.class;


		healthFactor = 0.8f;
		damageFactor = 0.7f;
		attackDelay = 0.5f;

        attackDelay = 0.5f;

		loot = new Food();
		lootChance = 0.083f;

		properties.add(Property.UNDEAD);
	}

	@Override
	public void rollToDropLoot() {
		Imp.Quest.process(this);

		super.rollToDropLoot();
	}


	protected float focusCooldown = 0;

	protected boolean act() {
		boolean result = super.act();
		if (buff(Focus.class) == null && state == HUNTING && focusCooldown <= 0) {
			Buff.affect(this, Focus.class);
		}
		return result;
	}

	@Override
	public void spend(float time) {
		focusCooldown -= time;
		super.spend(time);
	}

	@Override
	public void move(int step) {
		// moving reduces cooldown by an additional 0.67, giving a total reduction of 1.67f.
		// basically monks will become focused notably faster if you kite them.
		focusCooldown -= 0.67f;
		super.move(step);
	}

	@Override
	public int defenseSkill(Char enemy) {
		if (buff(Focus.class) != null && paralysed == 0 && state != SLEEPING){
			return INFINITE_EVASION;
		}
		return super.defenseSkill( enemy );
	}

	@Override
	public String defenseVerb() {
		Focus f = buff(Focus.class);
		if (f == null) {
			return super.defenseVerb();
		} else {
			f.detach();
			Sample.INSTANCE.play( Assets.Sounds.HIT_PARRY, 1, Random.Float(0.96f, 1.05f));
			focusCooldown = Random.NormalFloat(6, 7);
			return Messages.get(this, "parried");
		}
	}


	private static String FOCUS_COOLDOWN = "focus_cooldown";


	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( FOCUS_COOLDOWN, focusCooldown );
	}

	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		focusCooldown = bundle.getInt( FOCUS_COOLDOWN );
	}

	public static class Focus extends Buff {

		{
			type = buffType.POSITIVE;
			announced = true;
		}

		@Override
		public int icon() {
			return BuffIndicator.MIND_VISION;
		}

		@Override
		public void tintIcon(Image icon) {
			icon.hardlight(0.25f, 1.5f, 1f);
		}

		@Override
		public String toString() {
			return Messages.get(this, "name");
		}

		@Override
		public String desc() {
			return Messages.get(this, "desc");
		}
	}

	public static class Senior extends Monk {

		{
			spriteClass = SeniorSprite.class;
			damageFactor = 0.8f;

			loot = new Pasty();
			lootChance = 0.2f;
		}

		@Override
		public void move( int step ) {
			// on top of the existing move bonus, senior monks get a further 1.66 cooldown reduction
			// for a total of 3.33, double the normal 1.67 for regular monks
			focusCooldown -= 1.66f;
			super.move(step);
		}

	}
}
