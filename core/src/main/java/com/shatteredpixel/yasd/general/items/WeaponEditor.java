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

package com.shatteredpixel.yasd.general.items;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.items.weapon.Weapon;
import com.shatteredpixel.yasd.general.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.scenes.PixelScene;
import com.shatteredpixel.yasd.general.sprites.ItemSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.ui.RedButton;
import com.shatteredpixel.yasd.general.ui.RenderedTextBlock;
import com.shatteredpixel.yasd.general.ui.Window;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.shatteredpixel.yasd.general.windows.IconTitle;
import com.shatteredpixel.yasd.general.windows.WndBag;
import com.watabou.noosa.Game;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class WeaponEditor extends Item {
	{
		image = ItemSpriteSheet.KIT;
	}

	private static final String AC_APPLY = "apply";
	private static final String AC_DESTROY = "destroy";

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_APPLY);
		actions.add(AC_DESTROY);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {
		super.execute(hero, action);
		if (action.equals(AC_APPLY)) {
			GameScene.selectItem(new WndBag.Listener() {
				@Override
				public void onSelect(Item item) {
					Game.scene().addToFront(new WndEditWeapon(((MeleeWeapon)item)));
				}
			}, WndBag.Mode.WEAPON, Messages.get(this, "select_weapon"));
		}
	}

	public static int amountOfScrap(Char ch) {
		Scrap scrap = ch.belongings.getItem(Scrap.class);
		if (scrap != null) {
			return scrap.quantity;
		}
		return 0;
	}

	public static int amountOfScrap(MeleeWeapon weapon) {
		int amount = 0;
		amount += weapon.tier * (weapon.level() + 1);
		return amount;
	}

	public static boolean spendScrap(@NotNull Char ch, int amount) {
		Scrap scrap = ch.belongings.getItem(Scrap.class);
		if (scrap != null && amount <= scrap.quantity) {
			scrap.quantity(scrap.quantity-amount);
			return true;
		} else {
			GLog.n(Messages.get(WeaponEditor.class, "not_enough"));
			return false;
		}
	}

	public static void collectScrap(Char ch, int amount) {
		new Scrap().quantity(amount).collect(ch.belongings.backpack, ch);
	}

	public static void convertToScrap(Char ch, MeleeWeapon weapon) {
		weapon.detach(ch.belongings.backpack);
		collectScrap(ch, amountOfScrap(weapon));
	}

	public static class Scrap extends Item {
		{
			image = ItemSpriteSheet.THROWING_STONE;

			stackable = true;
		}
	}

	public static class WndEditWeapon extends Window {

		RenderedTextBlock message;
		private ArrayList<Weapon.Property> properties;
		private float degradeFactor;
		private float accuracyFactor;
		private int reach;
		private float attackDelay;
		private float defenseMultiplier;

		public WndEditWeapon(MeleeWeapon weapon) {
			properties = weapon.properties;
			degradeFactor = weapon.degradeFactor;
			accuracyFactor = weapon.ACC;
			reach = weapon.RCH;
			attackDelay = weapon.DLY;
			defenseMultiplier = weapon.defenseMultiplier;
			IconTitle titlebar = new IconTitle();

			titlebar.icon(new ItemSprite(weapon.image(), null));
			titlebar.label(Messages.titleCase(weapon.name()));
			titlebar.setRect(0, 0, Window.WIDTH, 0);
			add(titlebar);

			message = PixelScene.renderTextBlock(Messages.get(this, "body", calcCost()), 6);
			message.maxWidth(WIDTH);
			message.setPos(0, titlebar.bottom() + GAP);
			add(message);

			RedButton btnChoose = new RedButton(Messages.get(this, "apply")) {
				@Override
				protected void onClick() {
					copyTo(weapon);
					spendScrap(Dungeon.hero, amountOfScrap(weapon));
					weapon.collect();
					hide();
				}
			};
			btnChoose.setRect(0, message.bottom() + GAP, WIDTH, BTN_HEIGHT);
			add( btnChoose );
		}

		private int calcCost() {
			return 0;
		}

		@Override
		public synchronized void update() {
			message.text(Messages.get(this, "body", calcCost()));
		}

		private void copyTo(MeleeWeapon weapon) {
			weapon.properties = properties;
			weapon.degradeFactor = degradeFactor;
			weapon.ACC = accuracyFactor;
			weapon.RCH = reach;
			weapon.DLY = attackDelay;
			weapon.defenseMultiplier = defenseMultiplier;
		}
	}
}
