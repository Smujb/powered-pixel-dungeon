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

package com.shatteredpixel.yasd.general.levels.rooms.standard;

import com.shatteredpixel.yasd.general.Badges;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.items.journal.GuidePage;
import com.shatteredpixel.yasd.general.journal.Document;
import com.shatteredpixel.yasd.general.levels.Level;
import com.shatteredpixel.yasd.general.levels.Terrain;
import com.shatteredpixel.yasd.general.levels.painters.Painter;
import com.shatteredpixel.yasd.general.levels.rooms.Room;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class EntranceRoom extends StandardRoom {
	
	@Override
	public int minWidth() {
		return Math.max(super.minWidth(), 5);
	}
	
	@Override
	public int minHeight() {
		return Math.max(super.minHeight(), 5);
	}

	public void paint( Level level ) {
		
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, Terrain.EMPTY );
		
		for (Room.Door door : connected.values()) {
			door.set( Room.Door.Type.REGULAR );
		}

		do {
			level.entrance = level.pointToCell(random(2));
		} while (level.findMob(level.entrance) != null);

		if (!level.hasEntrance) {
			Painter.set(level, level.entrance, Terrain.EMPTY);
		} else {
			Painter.set(level, level.entrance, Terrain.ENTRANCE);
		}

		if (Dungeon.depth == 1 && !Document.ADVENTURERS_GUIDE.hasPage(Document.GUIDE_INTRO_PAGE)){
			int pos;
			do {
				//can't be on bottom row of tiles
				pos = level.pointToCell(new Point( Random.IntRange( left + 1, right - 1 ),
						Random.IntRange( top + 1, bottom - 2 )));
			} while (pos == level.entrance || level.findMob(level.entrance) != null);
			GuidePage p = new GuidePage();
			p.page(Document.GUIDE_INTRO_PAGE);
			level.drop( p, pos );
		}

		if (Dungeon.depth == 2){
			if (!Badges.isUnlocked(Badges.Badge.BOSS_SLAIN_1)){
				for (Room.Door door : connected.values()) {
					door.set( Door.Type.HIDDEN );
				}
			}

			if (!Document.ADVENTURERS_GUIDE.hasPage(Document.GUIDE_SEARCH_PAGE)){
				int pos;
				do {
					//can't be on bottom row of tiles
					pos = level.pointToCell(new Point( Random.IntRange( left + 1, right - 1 ),
							Random.IntRange( top + 1, bottom - 2 )));
				} while (pos == level.entrance || level.findMob(level.entrance) != null);
				GuidePage p = new GuidePage();
				p.page(Document.GUIDE_SEARCH_PAGE);
				level.drop( p, pos );
			}

		}

	}
	
}
