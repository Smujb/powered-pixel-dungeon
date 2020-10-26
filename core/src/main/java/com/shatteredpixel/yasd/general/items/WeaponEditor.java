/*
 *
 *   Pixel Dungeon
 *   Copyright (C) 2012-2015 Oleg Dolya
 *
 *   Shattered Pixel Dungeon
 *   Copyright (C) 2014-2019 Evan Debenham
 *
 *   Powered Pixel Dungeon
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
import com.shatteredpixel.yasd.general.items.weapon.melee.RandomMeleeWeapon;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.scenes.PixelScene;
import com.shatteredpixel.yasd.general.sprites.ItemSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.ui.CheckBox;
import com.shatteredpixel.yasd.general.ui.OptionSlider;
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

		unique = true;
		cursed = false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public boolean isUpgradable() {
		return false;
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
					if (!(item instanceof RandomMeleeWeapon)) {
						return;
					}
					Game.scene().addToFront(new WndEditWeapon(((RandomMeleeWeapon)item)));
				}
			}, WndBag.Mode.WEAPON, Messages.get(this, "select_weapon"));
		} else if (action.equals(AC_DESTROY)) {
			GameScene.selectItem(new WndBag.Listener() {
				@Override
				public void onSelect(Item item) {
					if (!(item instanceof MeleeWeapon)) {
						return;
					}
					convertToScrap(hero, ((MeleeWeapon)item));
				}
			}, WndBag.Mode.WEAPON, Messages.get(this, "select_weapon"));
		}
	}

	private static int amountOfScrap(Char ch) {
		Scrap scrap = ch.belongings.getItem(Scrap.class);
		if (scrap != null) {
			return scrap.quantity;
		}
		return 0;
	}

	private static int amountOfScrap(MeleeWeapon weapon) {
		int amount = 3;
		amount += weapon.tier * (weapon.level() + 1);
		amount *= weapon.degradedPercent();
		if (weapon.cursed) {
			amount /= 2;
		} else if (weapon.isIdentified()) {
			amount *= 1.5f;
		}
		return Math.max(1, amount);
	}

	private static boolean spendScrap(@NotNull Char ch, int amount) {
		Scrap scrap = ch.belongings.getItem(Scrap.class);
		if (scrap != null && amount <= scrap.quantity) {
			scrap.quantity(scrap.quantity-amount);
			if (scrap.quantity <= 0) {
				scrap.detach(ch.belongings.backpack);
			}
			GLog.p(Messages.get(WeaponEditor.class, "spent_scrap"), amount);
			return true;
		} else {
			if (scrap == null || scrap.quantity == 0) {
				GLog.n(Messages.get(WeaponEditor.class, "no_scrap"));
			} else {
				GLog.n(Messages.get(WeaponEditor.class, "not_enough"), amountOfScrap(ch), amount);
			}
			return false;
		}
	}

	private static void collectScrap(Char ch, int amount) {
		Scrap scrap = (Scrap) new Scrap().quantity(amount);
		if (!scrap.collect(ch.belongings.backpack, ch)) {
			Dungeon.level.drop(scrap, ch.pos).sprite.drop();
		}
	}

	private static void convertToScrap(Char ch, MeleeWeapon weapon) {

		if (ch.belongings.backpack.contains(weapon)) {
			weapon.detach(ch.belongings.backpack);
		} else {
			for (int i = 0; i < ch.belongings.miscs.length; i++) {
				if (ch.belongings.miscs[i] == weapon) {
					ch.belongings.miscs[i] = null;
					break;
				}
			}
		}
		int amount = amountOfScrap(weapon);
		collectScrap(ch, amount);
		GLog.p(Messages.get(WeaponEditor.class, "gained_scrap", weapon.name(), amount));
	}

	public static class Scrap extends Item {

		{
			image = ItemSpriteSheet.DUST;

			stackable = true;

			cursed = false;
		}

		@Override
		public boolean isIdentified() {
			return true;
		}

		@Override
		public boolean isUpgradable() {
			return false;
		}
	}

	private static class WndEditWeapon extends Window {

		RenderedTextBlock message;
		private ArrayList<Weapon.Property> properties;
		private float degradeFactor;
		private float accuracyFactor;
		private int reach;
		private float attackDelay;
		private float defenseMultiplier;

		private WndEditWeapon(RandomMeleeWeapon weapon) {
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

			int bottom = (int) message.bottom() + GAP;
			for (KindOfWeapon.Property property : KindOfWeapon.Property.values()) {
				CheckBox propBox = new CheckBox(property.displayName()) {
					@Override
					protected void onClick() {
						super.onClick();
						if (properties.contains(property)) {
							properties.remove(property);
						} else {
							properties.add(property);
						}
						WndEditWeapon.this.update();
					}
				};
				propBox.checked(properties.contains(property));
				propBox.setRect(0, bottom, WIDTH, BTN_HEIGHT*2/3f);
				add(propBox);
				bottom = (int) propBox.bottom() + GAP;
			}

			OptionSlider degradeFactorSlider = new StatSlider(Messages.get(this, "degrade_factor")) {
				@Override
				protected void onChange() {
					degradeFactor = getSelectedValue()/10f;
					WndEditWeapon.this.update();
				}
			};
			degradeFactorSlider.setSelectedValue((int) (degradeFactor*10));
			degradeFactorSlider.setRect(0, bottom, WIDTH, BTN_HEIGHT);
			add(degradeFactorSlider);

			bottom = (int) degradeFactorSlider.bottom();

			OptionSlider accuracyFactorSlider = new StatSlider(Messages.get(this, "acc_factor")) {
				@Override
				protected void onChange() {
					accuracyFactor = getSelectedValue()/10f;
					WndEditWeapon.this.update();
				}
			};
			accuracyFactorSlider.setSelectedValue((int) (accuracyFactor*10));
			accuracyFactorSlider.setRect(0, bottom, WIDTH, BTN_HEIGHT);
			add(accuracyFactorSlider);

			bottom = (int) accuracyFactorSlider.bottom();

			OptionSlider attackDelaySlider = new StatSlider(Messages.get(this, "attack_delay")) {
				@Override
				protected void onChange() {
					attackDelay = getSelectedValue()/10f;
					WndEditWeapon.this.update();
				}
			};
			attackDelaySlider.setSelectedValue((int) (attackDelay*10));
			attackDelaySlider.setRect(0, bottom, WIDTH, BTN_HEIGHT);
			add(attackDelaySlider);

			bottom = (int) attackDelaySlider.bottom();

			OptionSlider reachSlider = new OptionSlider(Messages.get(this, "reach"), "1", "3", 1, 3) {
				@Override
				protected void onChange() {
					reach = getSelectedValue();
					WndEditWeapon.this.update();
				}
			};
			reachSlider.setSelectedValue(reach);
			reachSlider.setRect(0, bottom, WIDTH, BTN_HEIGHT);
			add(reachSlider);

			bottom = (int) reachSlider.bottom();

			OptionSlider defenseSlider = new OptionSlider(Messages.get(this, "defense"), "0", "100", 0, 10) {
				@Override
				protected void onChange() {
					defenseMultiplier = getSelectedValue()*10f;
					WndEditWeapon.this.update();
				}
			};
			defenseSlider.setSelectedValue((int) (defenseMultiplier/10f));
			defenseSlider.setRect(0, bottom, WIDTH, BTN_HEIGHT);
			add(defenseSlider);

			bottom = (int) defenseSlider.bottom();

			RedButton btnApply = new RedButton(Messages.get(this, "apply")) {
				@Override
				protected void onClick() {
					if (spendScrap(Dungeon.hero, calcCost())) {
						copyTo(weapon);
						if (!Dungeon.hero.belongings.contains(weapon)) {
							weapon.collect();
						}
						hide();
					}
				}
			};
			btnApply.setRect(0, bottom, WIDTH, BTN_HEIGHT);
			add( btnApply );

			bottom = (int) btnApply.bottom();

			resize(WIDTH, bottom);
		}

		private int calcCost() {
			return amountOfScrap(copyTo(new RandomMeleeWeapon())) * 2;
		}

		@Override
		public synchronized void update() {
			message.text(Messages.get(this, "body", calcCost()));
		}

		private MeleeWeapon copyTo(RandomMeleeWeapon weapon) {
			weapon.properties = properties;
			weapon.degradeFactor = degradeFactor;
			weapon.ACC = accuracyFactor;
			weapon.RCH = reach;
			weapon.DLY = attackDelay;
			weapon.defenseMultiplier = defenseMultiplier;
			weapon.matchProfile();
			return weapon;
		}
	}

	private abstract static class StatSlider extends OptionSlider {

		private StatSlider(String title) {
			super(title, "50%", "200%", 5, 20);
		}
	}
}
