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
import com.shatteredpixel.yasd.general.MainGame;
import com.shatteredpixel.yasd.general.actors.buffs.Awareness;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.MindVision;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.yasd.general.scenes.InterlevelScene;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.utils.GLog;
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
		return actions;
	}

	private static final String AC_DEBUG = "debug";
	private static final String AC_MAP = "map";
	private static final String AC_TP = "tp";

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
				if (Dungeon.zPos < 1) {
					InterlevelScene.move(Dungeon.xPos, Dungeon.yPos, 1, "TEST", InterlevelScene.Mode.DESCEND);
				} else {
					InterlevelScene.move(Dungeon.xPos, Dungeon.yPos, 0, "TEST", InterlevelScene.Mode.DESCEND);
				}
				int i = 0;
				ArrayList<Integer> postions = new ArrayList<>();
				postions.add(0);
				postions.add(0);
				postions.add(0);
				MainGame.platform.promptTextInput("Choose depth to visit", "", 2, false, "SET", "NVM", new PlatformSupport.TextCallback() {
					@Override
					public void onSelect(boolean positive, String text) {
						for (char ch : text.toCharArray()) {

						}
					}
				});
				break;
		}
	}
}
