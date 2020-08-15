/*
 *
 *  * Pixel Dungeon
 *  * Copyright (C) 2012-2015 Oleg Dolya
 *  *
 *  * Shattered Pixel Dungeon
 *  * Copyright (C) 2014-2019 Evan Debenham
 *  *
 *  * Powered Pixel Dungeon
 *  * Copyright (C) 2014-2020 Samuel Braithwaite
 *  *
 *  * This program is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 *
 */

package com.shatteredpixel.yasd.general.levels.rooms.sewerboss;

import com.shatteredpixel.yasd.general.levels.Level;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;
import com.shatteredpixel.yasd.general.levels.interactive.Entrance;
import com.shatteredpixel.yasd.general.levels.painters.Painter;
import com.shatteredpixel.yasd.general.levels.rooms.Room;
import com.shatteredpixel.yasd.general.levels.rooms.standard.EntranceRoom;

public class SewerBossEntranceRoom extends EntranceRoom {
	
	@Override
	public int minHeight() {
		return 6;
	}
	
	@Override
	public int minWidth() {
		return 8;
	}
	
	public void paint(Level level ) {
		
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, Terrain.EMPTY );
		
		Painter.fill( level, left+1, top+1, width()-2, 1, Terrain.WALL_DECO);
		Painter.fill( level, left+1, top+2, width()-2, 1, Terrain.WATER);
		
		do {
			//level.entrance = level.pointToCell(random(3));
			level.interactiveAreas.add(new Entrance().setPos(level, level.pointToCell(random(3))));
		} while (level.findMob(level.getEntrancePos()) != null);
		Painter.set( level, level.getEntrancePos(), Terrain.ENTRANCE );
		
		for (Room.Door door : connected.values()) {
			door.set( Room.Door.Type.REGULAR );
			
			if (door.y == top || door.y == top+1){
				Painter.drawInside( level, this, door, 1, Terrain.WATER);
			}
		}
		
	}
	
}
