/*
 *
 *  * Pixel Dungeon
 *  * Copyright (C) 2012-2015 Oleg Dolya
 *  *
 *  * Shattered Pixel Dungeon
 *  * Copyright (C) 2014-2019 Evan Debenham
 *  *
 *  * Yet Another Shattered Dungeon
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

package com.shatteredpixel.yasd.general.levels.rooms.connection;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.levels.Level;
import com.shatteredpixel.yasd.general.levels.rooms.Room;
import com.watabou.utils.Point;

public abstract class ConnectionRoom extends Room {
	
	@Override
	public int minWidth() { return 3; }
	public int maxWidth() { return 10; }
	
	@Override
	public int minHeight() { return 3; }
	public int maxHeight() { return 10; }
	
	@Override
	public int minConnections(int direction) {
		if (direction == ALL)   return 2;
		else                    return 0;
	}
	
	@Override
	public boolean canPlaceTrap(Point p) {
		//traps cannot appear in connection rooms on floor 1
		return super.canPlaceTrap(p) && Dungeon.depth > 1;
	}

	/*private static ArrayList<Class<?extends ConnectionRoom>> rooms = new ArrayList<>();
	static {
		rooms.add(TunnelRoom.class);
		rooms.add(BridgeRoom.class);
		
		rooms.add(PerimeterRoom.class);
		rooms.add(WalkwayRoom.class);
		
		rooms.add(RingTunnelRoom.class);
		rooms.add(RingBridgeRoom.class);

		rooms.add(NonHiddenMazeConnectionRoom.class);
	}
	
	private static float[][] chances = new float[Constants.NUM_FLOORS + 1][];
	static {
		float [] sewerChances      = new float[]{20, 1,    0, 2,       2, 1, 1};
		float [] prisonChances     = new float[]{0, 0,     22, 3,      0, 0, 1};
		float [] cavesChances      = new float[]{12, 0,    0, 5,       5, 3, 1};
		float [] cityChances       = new float[]{0, 0,     18, 3,      3, 1, 1};
		float [] hallsChances      = new float[]{15, 4,    0, 2,       3, 2, 1};
		float [] restrictedChances = new float[]{20, 0,    0, 0,       0, 0, 0};
		for (int i = 0; i <= Constants.NUM_FLOORS; i++) {//Default to Sewer levelgen.
			chances[i] = restrictedChances;
		}
		for (int i = 0; i < Constants.CHAPTER_LENGTH*5; i++) {
			if (Dungeon.depth <= Constants.CHAPTER_LENGTH) {
				chances[i] = sewerChances;
			} else if (Dungeon.depth <= Constants.CHAPTER_LENGTH*2) {
				chances[i] = prisonChances;
			} else if (Dungeon.depth <= Constants.CHAPTER_LENGTH*3) {
				chances[i] = cavesChances;
			} else if (Dungeon.depth <= Constants.CHAPTER_LENGTH*4) {
				chances[i] = cityChances;
			} else if (Dungeon.depth <= Constants.CHAPTER_LENGTH*5) {
				chances[i] = hallsChances;
			}
		}
		chances[Constants.CHAPTER_LENGTH] = restrictedChances;//First boss
		chances[Constants.CHAPTER_LENGTH*4+1]= restrictedChances;//Floor after 4th boss
		/*chances[1] =  new float[]{20, 1,    0, 2,       2, 1};
		chances[4] =  chances[3] = chances[2] = chances[1];
		chances[5] =  new float[]{20, 0,    0, 0,       0, 0};
		
		chances[6] =  new float[]{0, 0,     22, 3,      0, 0};
		chances[10] = chances[9] = chances[8] = chances[7] = chances[6];
		
		chances[11] = new float[]{12, 0,    0, 5,       5, 3};
		chances[15] = chances[14] = chances[13] = chances[12] = chances[11];
		
		chances[16] = new float[]{0, 0,     18, 3,      3, 1};
		chances[20] = chances[19] = chances[18] = chances[17] = chances[16];
		
		chances[21] = chances[5];
		
		chances[22] = new float[]{15, 4,    0, 2,       3, 2};
		chances[26] = chances[25] = chances[24] = chances[23] = chances[22];
	}*/
	
	public static ConnectionRoom createRoom(Level level) {
		//return Reflection.newInstance(rooms.get(Random.chances(chances[Dungeon.depth])));
		return level.randomConnectionRoom();
	}
}
