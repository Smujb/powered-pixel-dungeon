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

import com.shatteredpixel.yasd.general.Constants;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.GameSettings;
import com.shatteredpixel.yasd.general.MainGame;
import com.shatteredpixel.yasd.general.actors.buffs.Awareness;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.MindVision;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.InterlevelScene;
import com.shatteredpixel.yasd.general.scenes.PixelScene;
import com.shatteredpixel.yasd.general.sprites.ItemSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.ui.OptionSlider;
import com.shatteredpixel.yasd.general.ui.RedButton;
import com.shatteredpixel.yasd.general.ui.RenderedTextBlock;
import com.shatteredpixel.yasd.general.ui.Window;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.shatteredpixel.yasd.general.windows.IconTitle;
import com.watabou.noosa.Camera;
import com.watabou.utils.PlatformSupport;

import java.util.ArrayList;

public class MagicMap extends Item {

	{
		image = ItemSpriteSheet.ARTIFACT_SPELLBOOK;

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

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions =  super.actions(hero);
		actions.add(AC_DEBUG);
		actions.add(AC_MAP);
		actions.add(AC_TP);
		actions.add(AC_TEST);
		actions.add(AC_KILL);
		actions.add(AC_ITEM);
		return actions;
	}

	private static final String AC_DEBUG = "debug";
	private static final String AC_MAP = "map";
	private static final String AC_TP = "tp";
	private static final String AC_TEST = "test";
	private static final String AC_ITEM = "item";
	private static final String AC_KILL = "kill";


	private static final int BTN_HEIGHT	= 20;
	private static final float GAP		= 2;
	private static final String BASE_NAME = "com.shatteredpixel.yasd.general.items.";
	private static int getWidth() {
		return (int) (Camera.main.width*0.8f);
	}

	public static class WndGetItem extends Window {

		private IconTitle titlebar;
		private final Item[] items = new Item[1];
		private final int[] amounts = new int[1];
		private final int[] levels = new int[1];
		private RenderedTextBlock message;

		private static String name(Item item) {
			return item.getClass().getName();
		}

		public WndGetItem(Item item) {
			super();

			items[0] = item;
			amounts[0] = 1;
			levels[0] = 0;

			titlebar = new IconTitle();
			titlebar.icon(new ItemSprite(items[0].image(), null));
			titlebar.label(Messages.titleCase(items[0].name()));
			titlebar.setRect(0, 0, getWidth(), 0);
			add(titlebar);

			message = PixelScene.renderTextBlock("Choose an item (currently " + name(items[0]) + ")", 6);
			message.maxWidth(getWidth());
			message.setPos(0, titlebar.bottom() + GAP);
			add(message);

			final WndGetItem window = this;

			RedButton btnChoose = new RedButton( "Input Item" ) {
				@Override
				protected void onClick() {
					MainGame.platform.promptTextInput("Enter id of an item you want: ", name(items[0]).replace(BASE_NAME, ""), Integer.MAX_VALUE, false, "CHOOSE", "CANCEL", new PlatformSupport.TextCallback() {

						@Override
						public void onSelect(boolean positive, String text) {
							if (positive) {
								try {
									items[0] = (Item) Class.forName(BASE_NAME + text).newInstance();
									GLog.p("Successfully fetched item.");
								} catch (ClassNotFoundException e) {
									e.printStackTrace();
								} catch (IllegalAccessException e) {
									e.printStackTrace();
								} catch (InstantiationException e) {
									e.printStackTrace();
								}
								window.update();
							}
						}
					});
				}
			};
			btnChoose.setRect(0, message.bottom() + GAP, getWidth(), BTN_HEIGHT);
			add( btnChoose );

			OptionSlider quantitySlider = new OptionSlider("Choose quantity",
					"0", "25", 0, 25) {
				@Override
				protected void onChange() {
					amounts[0] = getSelectedValue();
					update();
				}
			};
			quantitySlider.setSelectedValue(Dungeon.yPos);
			quantitySlider.setRect(0, btnChoose.bottom() + GAP, getWidth(), BTN_HEIGHT);
			add(quantitySlider);

			OptionSlider levelSlider = new OptionSlider("Choose upgrade level",
					"0", "" + Constants.UPGRADE_LIMIT, 0, Constants.UPGRADE_LIMIT) {
				@Override
				protected void onChange() {
					levels[0] = getSelectedValue();
					update();
				}
			};
			levelSlider.setSelectedValue(Dungeon.yPos);
			levelSlider.setRect(0, quantitySlider.bottom() + GAP, getWidth(), BTN_HEIGHT);
			add(levelSlider);

			RedButton btnGo = new RedButton( "Collect" ) {
				@Override
				protected void onClick() {
					if (items[0].quantity(amounts[0]).level(levels[0]).collect()) {
						GLog.p("Successfully added item " + item.name() + " to entity #" + Dungeon.hero.id() + "'s backpack.");
					}
				}
			};
			btnGo.setRect(getWidth()/2, levelSlider.bottom() + GAP, getWidth()/2, BTN_HEIGHT);
			add( btnGo );

			resize(getWidth(), (int) btnGo.bottom());

		}

		@Override
		public synchronized void update() {
			super.update();
			titlebar.icon(new ItemSprite(items[0].image(), null));
			titlebar.label(Messages.titleCase(items[0].name()));
			titlebar.setRect(0, 0, getWidth(), 0);

			message = PixelScene.renderTextBlock("Choose an item (currently " + name(items[0]) + ")", 6);
			message.maxWidth(getWidth());
			message.setPos(0, titlebar.bottom() + GAP);
		}
	}

	public static class WndChooseDepth extends Window {

		public WndChooseDepth(Item item) {
			super();
			IconTitle titlebar = new IconTitle();
			titlebar.icon(new ItemSprite(item.image() , null));
			titlebar.label(Messages.titleCase(item.name()));
			titlebar.setRect(0, 0, getWidth(), 0);
			add( titlebar );

			RenderedTextBlock message = PixelScene.renderTextBlock( "Choose xPos, yPos and zPos:", 6 );
			message.maxWidth(getWidth());
			message.setPos(0, titlebar.bottom() + GAP);
			add( message );

			int[] position = new int[] {Dungeon.xPos, Dungeon.yPos, Dungeon.zPos};

			OptionSlider xPosSlider = new OptionSlider("Choose xPos (Currently: " + Dungeon.xPos + ", target: " + position[0] + ")",
					"0", "" + Constants.MAX_X, 0, Constants.MAX_X) {
				@Override
				protected void onChange() {
					position[0] = getSelectedValue();
					update();
				}
			};
			xPosSlider.setSelectedValue(GameSettings.brightness());
			xPosSlider.setRect(0, message.bottom() + GAP, getWidth(), BTN_HEIGHT);
			add(xPosSlider);

			OptionSlider yPosSlider = new OptionSlider("Choose yPos (Currently: " + Dungeon.yPos + ", target: " + position[1] + ")",
					"0", "" + Constants.MAX_Y, 0, Constants.MAX_Y) {
				@Override
				protected void onChange() {
					position[1] = getSelectedValue();
					update();
				}
			};
			yPosSlider.setSelectedValue(Dungeon.yPos);
			yPosSlider.setRect(0, xPosSlider.bottom() + GAP, getWidth(), BTN_HEIGHT);
			add(yPosSlider);

			OptionSlider zPosSlider = new OptionSlider("Choose zPos (Currently: " + Dungeon.zPos + ", target: " + position[2] + ")",
					"0", "" + Constants.MAX_Z, 0, Constants.MAX_Z) {
				@Override
				protected void onChange() {
					position[2] = getSelectedValue();
					update();
				}
			};
			zPosSlider.setSelectedValue(Dungeon.yPos);
			zPosSlider.setRect(0, yPosSlider.bottom() + GAP, getWidth(), BTN_HEIGHT);
			add(zPosSlider);

			RedButton btnCancel = new RedButton( "CANCEL" ) {
				@Override
				protected void onClick() {
					hide();
				}
			};
			btnCancel.setRect(0, zPosSlider.bottom() + GAP, getWidth()/2, BTN_HEIGHT);
			add( btnCancel );

			RedButton btnGo = new RedButton( "GO" ) {
				@Override
				protected void onClick() {
					InterlevelScene.move(position[0], position[1], position[2], "TEST", InterlevelScene.Mode.DESCEND);
				}
			};
			btnGo.setRect(getWidth()/2, zPosSlider.bottom() + GAP, getWidth()/2, BTN_HEIGHT);
			add( btnGo );

			resize(getWidth(), (int) btnGo.bottom());
		}
	}

	@Override
	public void execute(Hero hero, String action) {
		super.execute(hero, action);
		switch (action) {
			case AC_DEBUG:
				GLog.p("Position in Dungeon (X, Y, Z): " + Dungeon.xPos + ", " + Dungeon.yPos + ", " + Dungeon.zPos + ".");
				int[] coords = Dungeon.level.posToXY(hero.pos);
				GLog.p("Position in map (X, Y): " + coords[0] + ", " + coords[1] + ".");
				break;
			case AC_MAP:
				new ScrollOfMagicMapping().doRead();
				Buff.affect(hero, Awareness.class, Awareness.DURATION*5);
				Buff.affect(hero, MindVision.class, MindVision.DURATION);
				break;
			case AC_TP:
				new ScrollOfTeleportation().empoweredRead();
				break;
			case AC_TEST:
				MainGame.scene().addToFront(new WndChooseDepth(this));
				break;
			case AC_ITEM:
				MainGame.scene().addToFront(new WndGetItem(this));
				break;
			case AC_KILL:
				for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
					mob.die(this);
					GLog.i("All ded");
				}
				break;
		}
	}
}
