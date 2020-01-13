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

package com.shatteredpixel.yasd.sprites;

import com.shatteredpixel.yasd.Assets;
import com.shatteredpixel.yasd.actors.mobs.Warlock;
import com.shatteredpixel.yasd.effects.MagicMissile;
import com.watabou.noosa.Group;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class BurningFistSprite extends MagicalMobSprite {
	
	public BurningFistSprite() {
		super();
		
		texture( Assets.BURNING );
		
		TextureFilm frames = new TextureFilm( texture, 24, 17 );
		
		idle = new Animation( 2, true );
		idle.frames( frames, 0, 0, 1 );
		
		run = new Animation( 3, true );
		run.frames( frames, 0, 1 );
		
		attack = new Animation( 8, false );
		attack.frames( frames, 0, 5, 6 );
		
		die = new Animation( 10, false );
		die.frames( frames, 0, 2, 3, 4 );

		zap = attack.clone();
		
		play( idle );
	}

	@Override
	public void FX(Group group, int cell, Callback c) {
		MagicMissile.boltFromChar( group,
				MagicMissile.FIRE_CONE,
				this,
				cell,
				c);
	}
}
