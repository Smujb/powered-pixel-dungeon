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

import com.shatteredpixel.yasd.actors.BelongingsHolder;
import com.shatteredpixel.yasd.actors.hero.Hero;
import com.shatteredpixel.yasd.messages.Messages;
import com.shatteredpixel.yasd.scenes.GameScene;
import com.shatteredpixel.yasd.utils.GLog;
import com.shatteredpixel.yasd.windows.WndOptions;


public abstract class KindofMisc extends EquipableItem {

	private static final float TIME_TO_EQUIP = 1f;

	@Override
	public boolean doEquip(final Hero hero) {

		if (hero.belongings.miscs[0] != null && hero.belongings.miscs[1] != null && hero.belongings.miscs[2] != null && hero.belongings.miscs[3] != null && hero.belongings.miscs[4] != null) {

			final KindofMisc m1 = hero.belongings.miscs[0];
			final KindofMisc m2 = hero.belongings.miscs[1];
			final KindofMisc m3 = hero.belongings.miscs[2];
			final KindofMisc m4 = hero.belongings.miscs[3];
			final KindofMisc m5 = hero.belongings.miscs[4];

			GameScene.show(
					new WndOptions(Messages.get(KindofMisc.class, "unequip_title"),
							Messages.get(KindofMisc.class, "unequip_message"),
							Messages.titleCase(m1.toString()),
							Messages.titleCase(m2.toString()),
							Messages.titleCase(m3.toString()),
							Messages.titleCase(m4.toString()),
							Messages.titleCase(m5.toString())) {

						@Override
						protected void onSelect(int index) {

							KindofMisc equipped;

							if (index == 0) {
								equipped = m1;
							} else if (index == 1) {
								equipped = m2;
							} else if (index == 2) {
								equipped = m3;
							} else if (index == 3) {
								equipped = m4;
							} else  {
								equipped = m5;
							}
							//temporarily give 1 extra backpack spot to support swapping with a full inventory
							hero.belongings.backpack.size++;
							if (equipped.doUnequip(hero, true, false)) {
								//fully re-execute rather than just call doEquip as we want to preserve quickslot
								execute(hero, AC_EQUIP);
							}
							hero.belongings.backpack.size--;
						}
					});

			return false;

		} else {

			if (hero.belongings.miscs[0] == null) {
				hero.belongings.miscs[0] = this;
			} else if (hero.belongings.miscs[1] == null) {
				hero.belongings.miscs[1] = this;
			} else if (hero.belongings.miscs[2] == null) {
				hero.belongings.miscs[2] = this;
			} else if (hero.belongings.miscs[3] == null) {
				hero.belongings.miscs[3] = this;
			}  else if (hero.belongings.miscs[4] == null) {
				hero.belongings.miscs[4] = this;
			}

			detach( hero.belongings.backpack );

			activate( hero );

			cursedKnown = true;
			if (cursed) {
				equipCursed( hero );
				GLog.n( Messages.get(this, "equip_cursed", this) );
			}

			hero.spendAndNext( TIME_TO_EQUIP );
			return true;

		}

	}

	@Override
	public boolean doUnequip(BelongingsHolder hero, boolean collect, boolean single) {
		if (super.doUnequip(hero, collect, single)){

			if (hero.belongings.miscs[0] == this) {
				hero.belongings.miscs[0] = null;
			} else if (hero.belongings.miscs[1] == this) {
				hero.belongings.miscs[1] = null;
			} else if (hero.belongings.miscs[2] == this) {
				hero.belongings.miscs[2] = null;
			} else if (hero.belongings.miscs[3] == this) {
				hero.belongings.miscs[3] = null;
			} else if (hero.belongings.miscs[4] == this) {
				hero.belongings.miscs[4] = null;
			}

			return true;

		} else {

			return false;

		}
	}

	@Override
	public boolean isEquipped(BelongingsHolder owner ) {
		return owner.belongings.miscs[0] == this || owner.belongings.miscs[1] == this || owner.belongings.miscs[2] == this || owner.belongings.miscs[3] == this || owner.belongings.miscs[4] == this;
	}

}


