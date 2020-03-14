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
		return actions;
	}

	private static final String AC_DEBUG = "debug";
	private static final String AC_MAP = "map";
	private static final String AC_TP = "tp";
	private static final String AC_TEST = "test";
	private static final String AC_KILL = "kill";

	public static class WndChooseDepth extends Window {

		private static int getWidth() {
			return MainGame.width/2;
		}
		private static final int BTN_HEIGHT	= 20;
		private static final float GAP		= 2;

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

			OptionSlider xPosSlider = new OptionSlider("Choose xPos",
					"0", "" + Constants.MAX_X, 0, Constants.MAX_X) {
				@Override
				protected void onChange() {
					position[0] = getSelectedValue();
				}
			};
			xPosSlider.setSelectedValue(GameSettings.brightness());
			xPosSlider.setRect(0, message.bottom() + GAP, getWidth(), BTN_HEIGHT);
			add(xPosSlider);

			OptionSlider yPosSlider = new OptionSlider("Choose yPos",
					"0", "" + Constants.MAX_Y, 0, Constants.MAX_Y) {
				@Override
				protected void onChange() {
					position[1] = getSelectedValue();
				}
			};
			yPosSlider.setSelectedValue(Dungeon.yPos);
			yPosSlider.setRect(0, xPosSlider.bottom() + GAP, getWidth(), BTN_HEIGHT);
			add(yPosSlider);

			OptionSlider zPosSlider = new OptionSlider("Choose zPos",
					"0", "" + Constants.MAX_Z, 0, Constants.MAX_Z) {
				@Override
				protected void onChange() {
					position[2] = getSelectedValue();
				}
			};
			zPosSlider.setSelectedValue(Dungeon.yPos);
			zPosSlider.setRect(0, yPosSlider.bottom() + GAP, getWidth(), BTN_HEIGHT);
			add(zPosSlider);

			RedButton btnGo = new RedButton( "GO" ) {
				@Override
				protected void onClick() {
					InterlevelScene.move(position[0], position[1], position[2], "TEST", InterlevelScene.Mode.DESCEND);
				}
			};
			btnGo.setRect(0, zPosSlider.bottom() + GAP, getWidth()/2, BTN_HEIGHT);
			add( btnGo );

			RedButton btnCancel = new RedButton( "CANCEL" ) {
				@Override
				protected void onClick() {
					hide();
				}
			};
			btnCancel.setRect(getWidth()/2, zPosSlider.bottom() + GAP, getWidth()/2, BTN_HEIGHT);
			add( btnCancel );

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
			case AC_KILL:
				for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
					mob.die(this);
					GLog.i("All ded");
				}
				break;
		}
	}
}
