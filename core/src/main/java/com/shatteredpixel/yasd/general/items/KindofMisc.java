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

package com.shatteredpixel.yasd.general.items;

import com.shatteredpixel.yasd.general.Constants;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.shatteredpixel.yasd.general.windows.WndOptions;


public abstract class KindofMisc extends EquipableItem {

	private static final float TIME_TO_EQUIP = 1f;

	@Override
	public boolean doEquip(final Char ch) {
		final KindofMisc[] miscs = ch.belongings.miscs;
		if (!ch.belongings.canEquip(this)) {

			String[] miscNames = new String[miscs.length];
			for (int i = 0; i < miscs.length; i++) {
				miscNames[i] = miscs[i].toString();
			}

			GameScene.show(
					new WndOptions(Messages.get(KindofMisc.class, "unequip_title"),
							Messages.get(KindofMisc.class, "unequip_message"),
							miscNames) {

						@Override
						protected void onSelect(int index) {

							KindofMisc equipped = null;

							for (int i = 0; i < miscs.length; i++) {
								if (index == i) {
									equipped = miscs[i];
									break;
								}
							}
							if (equipped == null) return;

							int slot = Dungeon.quickslot.getSlot(KindofMisc.this);
							detach(ch.belongings.backpack);
							if (equipped.doUnequip(ch, true, false)) {
								doEquip(ch);
							} else {
								collect();
							}
							if (slot != -1) Dungeon.quickslot.setSlot(slot, KindofMisc.this);
						}
					});

			return false;

		} else {

			for (int i = 0; i < miscs.length; i++) {
				if (ch.belongings.canEquip(this, i)) {
					ch.belongings.miscs[i] = this;
					break;
				}
			}


			detach( ch.belongings.backpack );

			activate(ch);

			cursedKnown = true;
			if (cursed) {
				equipCursed(ch);
				GLog.n( Messages.get(this, "equip_cursed", this) );
			}

			ch.spendAndNext( TIME_TO_EQUIP );
			return true;

		}

	}

	@Override
	public boolean doUnequip(Char ch, boolean collect, boolean single) {
		if (super.doUnequip(ch, collect, single)){

			for (int i = 0; i < Constants.MISC_SLOTS; i++) {
				if (ch.belongings.miscs[i] == this) {
					ch.belongings.miscs[i] = null;
				}
			}

			return true;

		} else {

			return false;

		}
	}

	@Override
	public boolean isEquipped( Char owner ) {
		for (int i = 0; i < Constants.MISC_SLOTS; i++) {
			if (owner.belongings.miscs[i] == this) {
				return true;
			}
		}
		return false;
	}

}


