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

package com.shatteredpixel.yasd.general.items.wands;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class BoltWand extends NormalWand {

	@Override
	protected NormalWand initStats() {
		collisionProperties = Ballistica.WONT_STOP;
		if (Random.Int(3) != 0) {
			collisionProperties = collisionProperties | Ballistica.STOP_CHARS;
		}

		if (Random.Int(3) != 0) {
			collisionProperties = collisionProperties | Ballistica.STOP_TARGET;
		}

		if (Random.Int(3) != 0) {
			collisionProperties = collisionProperties | Ballistica.STOP_TERRAIN;
		}
		return super.initStats();
	}

	@Override
	public void onZap(Ballistica bolt) {

		for (int cell : bolt.path) {
			affectCell(bolt.sourcePos, cell);
			Char ch = Actor.findChar(cell);
			if (ch != null && ch != curUser) {
				if (element.isMagical() || Char.hit(curUser, ch)) {
					processSoulMark(ch, chargesPerCast());
					hit(ch);

					ch.sprite.burst(0xFFFFFFFF, level() / 2 + 2);

				} else {
					String defense = ch.defenseVerb();
					ch.sprite.showStatus(CharSprite.NEUTRAL, defense);

					Sample.INSTANCE.play(Assets.SND_MISS);
				}

			} else {
				Dungeon.level.pressCell(bolt.collisionPos);
			}
		}
	}
}
