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

package com.shatteredpixel.yasd.actors.blobs;

import com.shatteredpixel.yasd.Dungeon;
import com.shatteredpixel.yasd.actors.Actor;
import com.shatteredpixel.yasd.actors.Char;
import com.shatteredpixel.yasd.actors.buffs.Buff;
import com.shatteredpixel.yasd.actors.buffs.Wet;
import com.shatteredpixel.yasd.effects.BlobEmitter;
import com.shatteredpixel.yasd.effects.Speck;
import com.shatteredpixel.yasd.levels.Level;
import com.shatteredpixel.yasd.levels.Terrain;
import com.shatteredpixel.yasd.messages.Messages;
import com.shatteredpixel.yasd.scenes.GameScene;

public class StormCloud extends Blob {
	
	@Override
	protected void evolve() {
		super.evolve();
		
		int cell;
		Char ch;
		for (int i = area.left; i < area.right; i++){
			for (int j = area.top; j < area.bottom; j++){
				cell = i + j*Dungeon.level.width();
				if (off[cell] > 0) {
					int terr = Dungeon.level.map[cell];
					if (terr == Terrain.EMPTY || terr == Terrain.GRASS ||
							terr == Terrain.EMBERS || terr == Terrain.EMPTY_SP ||
							terr == Terrain.HIGH_GRASS || terr == Terrain.FURROWED_GRASS
							|| terr == Terrain.EMPTY_DECO) {
						Level.set(cell, Terrain.WATER);
						GameScene.updateMap(cell);
					} else if (terr == Terrain.SECRET_TRAP || terr == Terrain.TRAP || terr == Terrain.INACTIVE_TRAP) {
						Level.set(cell, Terrain.WATER);
						Dungeon.level.traps.remove(cell);
						GameScene.updateMap(cell);
					}
					if (cur[cell] > 0 && (ch = Actor.findChar( cell )) != null) {
						if (!ch.isImmune(this.getClass())) {

							Buff.prolong(ch, Wet.class, 3f);

						}
					}
				}
			}
		}
	}
	
	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );
		emitter.pour( Speck.factory( Speck.STORM ), 0.4f );
	}
	
	@Override
	public String tileDesc() {
		return Messages.get(this, "desc");
	}
	
}
