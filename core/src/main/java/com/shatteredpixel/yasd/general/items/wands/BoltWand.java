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

package com.shatteredpixel.yasd.general.items.wands;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class BoltWand extends NormalWand {

	private boolean stopChars = true;
	private boolean stopTerrain = true;
	private boolean stopTarget = false;

	private static final String STOP_CHARS = "stop_chars";
	private static final String STOP_TERRAIN = "stop_terrain";
	private static final String STOP_TARGET = "stop_target";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(STOP_CHARS, stopChars);
		bundle.put(STOP_TERRAIN, stopTerrain);
		bundle.put(STOP_TARGET, stopTarget);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		stopChars = bundle.getBoolean(STOP_CHARS);
		stopTerrain = bundle.getBoolean(STOP_TERRAIN);
		stopTarget = bundle.getBoolean(STOP_TARGET);
	}

	@Override
	protected float getDamageMultiplier() {
		float multiplier = super.getDamageMultiplier();
		if (!stopChars) {
			multiplier *= 0.9f;
		}
		if (!stopTerrain) {
			multiplier *= 0.8f;
		}
		return multiplier;
	}

	@Override
	public String statsDesc() {
		String desc = "";
		if (!stopChars | !stopTerrain | stopTarget) {
			if (!stopChars) {
				desc += Messages.get(this, STOP_CHARS) + "\n";
			}
			if (!stopTerrain) {
				desc += Messages.get(this, STOP_TERRAIN) + "\n";
			}
			if (stopTarget) {
				desc += Messages.get(this, STOP_TARGET) + "\n";
			}
			desc += "\n";
		}
		return desc + Messages.get(this, "stats_desc", chargesPerCast(), min(), max());
	}

	@Override
	protected NormalWand initStats() {
		collisionProperties = Ballistica.WONT_STOP;
		if (Random.Int(3) == 0) {
			stopChars = false;
		}

		if (Random.Int(3) == 0) {
			stopTerrain = false;
		}

		if (Random.Int(3) == 0) {
			stopTarget = true;
		}
		initProps();
		return super.initStats();
	}

	private void initProps() {
		collisionProperties = Ballistica.WONT_STOP;
		if (stopTerrain) {
			collisionProperties = collisionProperties | Ballistica.STOP_TERRAIN;
		}
		if (stopChars) {
			collisionProperties = collisionProperties | Ballistica.STOP_CHARS;
		}
		if (stopTarget) {
			collisionProperties = collisionProperties | Ballistica.STOP_TARGET;
		}
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

					Sample.INSTANCE.play(Assets.Sounds.MISS);
				}

			} else {
				Dungeon.level.pressCell(bolt.collisionPos);
			}
		}
	}
}
