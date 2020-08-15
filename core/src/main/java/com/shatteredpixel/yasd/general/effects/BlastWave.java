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

package com.shatteredpixel.yasd.general.effects;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.tiles.DungeonTilemap;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;

public class BlastWave extends Image {

	private static final float TIME_TO_FADE = 0.2f;

	private float time;

	public BlastWave(){
		super(Effects.get(Effects.Type.RIPPLE));
		origin.set(width / 2, height / 2);
	}

	public void reset(int pos) {
		revive();

		x = (pos % Dungeon.level.width()) * DungeonTilemap.SIZE + (DungeonTilemap.SIZE - width) / 2;
		y = (pos / Dungeon.level.width()) * DungeonTilemap.SIZE + (DungeonTilemap.SIZE - height) / 2;

		time = TIME_TO_FADE;
	}

	@Override
	public void update() {
		super.update();

		if ((time -= Game.elapsed) <= 0) {
			kill();
		} else {
			float p = time / TIME_TO_FADE;
			alpha(p);
			scale.y = scale.x = (1-p)*3;
		}
	}

	public static void blast(int pos) {
		Group parent = Dungeon.hero.sprite.parent;
		BlastWave b = (BlastWave) parent.recycle(BlastWave.class);
		parent.bringToFront(b);
		b.reset(pos);
	}

}
