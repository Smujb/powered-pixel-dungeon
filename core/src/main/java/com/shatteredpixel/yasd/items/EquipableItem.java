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

package com.shatteredpixel.yasd.items;

import com.shatteredpixel.yasd.Assets;
import com.shatteredpixel.yasd.Dungeon;
import com.shatteredpixel.yasd.actors.Actor;
import com.shatteredpixel.yasd.actors.Char;
import com.shatteredpixel.yasd.actors.Char;
import com.shatteredpixel.yasd.actors.buffs.MagicImmune;
import com.shatteredpixel.yasd.actors.hero.Hero;
import com.shatteredpixel.yasd.effects.particles.ShadowParticle;
import com.shatteredpixel.yasd.messages.Messages;
import com.shatteredpixel.yasd.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public abstract class EquipableItem extends Item {

	public static final String AC_EQUIP = "EQUIP";
	public static final String AC_UNEQUIP = "UNEQUIP";

	{
		bones = true;
		defaultAction = AC_EQUIP;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(isEquipped(hero) ? AC_UNEQUIP : AC_EQUIP);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {

		super.execute(hero, action);

		if (action.equals(AC_EQUIP)) {
			//In addition to equipping itself, item reassigns itself to the quickslot
			//This is a special case as the item is being removed from inventory, but is staying with the hero.
			if (isEquipped(hero)) {
				execute(hero, AC_UNEQUIP);
			} else {
				int slot = Dungeon.quickslot.getSlot(this);
				doEquip(hero);
				if (slot != -1) {
					Dungeon.quickslot.setSlot(slot, this);
					updateQuickslot();
				}
			}
		} else if (action.equals(AC_UNEQUIP)) {
			doUnequip(hero, true);
		}
	}

	@Override
	public void doDrop(Hero hero) {
		if (!isEquipped(hero) || doUnequip(hero, false, false)) {
			super.doDrop(hero);
		}
	}

	@Override
	public void cast(final Char user, int dst) {
		if (isEquipped(user)) {
			if (quantity == 1 && !this.doUnequip(user, false, false)) {
				return;
			}
		}

		super.cast(user, dst);
	}

	public static void equipCursed(Char hero) {
		hero.sprite.emitter().burst(ShadowParticle.CURSE, 6);
		Sample.INSTANCE.play(Assets.SND_CURSED);
	}

	protected float time2equip(Char hero) {
		return 1;
	}

	public abstract boolean doEquip(Hero hero);

	public boolean doUnequip(Char hero, boolean collect, boolean single) {

		if (cursed && hero.buff(MagicImmune.class) == null) {
			GLog.w(Messages.get(EquipableItem.class, "unequip_cursed"));
			if (hero instanceof Hero) {
				((Hero) hero).loseMorale(2f);
			}

			return false;
		}

		if (single) {
			hero.spendAndNext(time2equip(hero));
		} else {
			if (hero instanceof Hero)
				hero.spend(time2equip(hero));
		}

		if (!collect || !collect(hero.belongings.backpack)) {
			onDetach();
			Dungeon.quickslot.clearItem(this);
			updateQuickslot();
			if (collect) Dungeon.level.drop(this, hero.pos);
		}

		return true;
	}

	final public boolean doUnequip(Hero hero, boolean collect) {
		return doUnequip(hero, collect, true);
	}

	public void activate(Char ch) {
		userID = ch.id();
		curUser = ch;
	}

	@Override
	public boolean collect() {
		curUser = (Char) Actor.findById(userID);
		return super.collect();
	}

	private int userID = 0;
	private static final String USERID =       "userID";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put( USERID, userID );
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		userID = bundle.getInt( USERID );
	}
}
