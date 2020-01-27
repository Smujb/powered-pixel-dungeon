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

package com.shatteredpixel.yasd.levels.rooms.standard;

import com.shatteredpixel.yasd.Constants;
import com.shatteredpixel.yasd.Dungeon;
import com.shatteredpixel.yasd.levels.CavesBossLevel;
import com.shatteredpixel.yasd.levels.CavesLevel;
import com.shatteredpixel.yasd.levels.CityBossLevel;
import com.shatteredpixel.yasd.levels.CityLevel;
import com.shatteredpixel.yasd.levels.HallsBossLevel;
import com.shatteredpixel.yasd.levels.HallsLevel;
import com.shatteredpixel.yasd.levels.NewPrisonBossLevel;
import com.shatteredpixel.yasd.levels.PrisonLevel;
import com.shatteredpixel.yasd.levels.SewerBossLevel;
import com.shatteredpixel.yasd.levels.SewerLevel;
import com.shatteredpixel.yasd.levels.rooms.Room;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public abstract class StandardRoom extends Room {
	
	public enum SizeCategory {
		
		NORMAL(4, 10, 1),
		LARGE(10, 14, 2),
		GIANT(14, 18, 3);
		
		public final int minDim, maxDim;
		public final int roomValue;
		
		SizeCategory(int min, int max, int val){
			minDim = min;
			maxDim = max;
			roomValue = val;
		}
		
		public int connectionWeight(){
			return roomValue*roomValue;
		}
		
	}
	
	public SizeCategory sizeCat;
	{ setSizeCat(); }
	
	//Note that if a room wishes to allow itself to be forced to a certain size category,
	//but would (effectively) never roll that size category, consider using Float.MIN_VALUE
	public float[] sizeCatProbs(){
		//always normal by default
		return new float[]{1, 0, 0};
	}
	
	public boolean setSizeCat(){
		return setSizeCat(0, SizeCategory.values().length-1);
	}
	
	//assumes room value is always ordinal+1
	public boolean setSizeCat( int maxRoomValue ){
		return setSizeCat(0, maxRoomValue-1);
	}
	
	//returns false if size cannot be set
	public boolean setSizeCat( int minOrdinal, int maxOrdinal ) {
		float[] probs = sizeCatProbs();
		SizeCategory[] categories = SizeCategory.values();
		
		if (probs.length != categories.length) return false;
		
		for (int i = 0; i < minOrdinal; i++)                    probs[i] = 0;
		for (int i = maxOrdinal+1; i < categories.length; i++)  probs[i] = 0;
		
		int ordinal = Random.chances(probs);
		
		if (ordinal != -1){
			sizeCat = categories[ordinal];
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public int minWidth() { return sizeCat.minDim; }
	public int maxWidth() { return sizeCat.maxDim; }
	
	@Override
	public int minHeight() { return sizeCat.minDim; }
	public int maxHeight() { return sizeCat.maxDim; }
	
	//FIXME this is a very messy way of handing variable standard rooms
	private static ArrayList<Class<?extends StandardRoom>> rooms = new ArrayList<>();
	static {
		rooms.add(EmptyRoom.class);


		rooms.add(SewerPipeRoom.class);
		rooms.add(RingRoom.class);

		rooms.add(SegmentedRoom.class);
		rooms.add(StatuesRoom.class);

		rooms.add(CaveRoom.class);
		rooms.add(CirclePitRoom.class);

		rooms.add(HallwayRoom.class);
		rooms.add(PillarsRoom.class);

		rooms.add(RuinsRoom.class);
		rooms.add(SkullsRoom.class);


		rooms.add(PlantsRoom.class);
		rooms.add(AquariumRoom.class);
		rooms.add(PlatformRoom.class);
		rooms.add(BurnedRoom.class);
		rooms.add(FissureRoom.class);
		rooms.add(GrassyGraveRoom.class);
		rooms.add(StripedRoom.class);
		rooms.add(StudyRoom.class);
		rooms.add(SuspiciousChestRoom.class);
		rooms.add(MinefieldRoom.class);
	}
	
	private static float[][] chances = new float[Constants.NUM_FLOORS + 1][];
	static {
		float [] sewerChances      = new float[]{20,  15,5, 0,0, 0,0, 0,0, 0,0,    1,1,1,1,1,1,1,1,1,1};
		float [] prisonChances     = new float[]{20,  0,0, 15,5, 0,0, 0,0, 0,0,    1,1,1,1,1,1,1,1,1,1};
		float [] cavesChances      = new float[]{20,  0,0, 0,0, 15,5, 0,0, 0,0,    1,1,1,1,1,1,1,1,1,1};
		float [] cityChances       = new float[]{20,  0,0, 0,0, 0,0, 15,5, 0,0,    1,1,1,1,1,1,1,1,1,1};
		float [] hallsChances      = new float[]{20,  0,0, 0,0, 0,0, 0,0, 15,5,    1,1,1,1,1,1,1,1,1,1};
		float [] restrictedChances = new float[]{20,  15,5, 0,0, 0,0, 0,0, 0,0,    0,0,0,0,0,0,0,0,0,0};
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
		
		/*chances[6] =  new float[]{20,  0,0, 15,5, 0,0, 0,0, 0,0,    1,1,1,1,1,1,1,1,1,1};
		chances[10] = chances[9] = chances[8] = chances[7] = chances[6];
		
		chances[11] = new float[]{20,  0,0, 0,0, 15,5, 0,0, 0,0,    1,1,1,1,1,1,1,1,1,1};
		chances[15] = chances[14] = chances[13] = chances[12] = chances[11];

		chances[16] = new float[]{20,  0,0, 0,0, 0,0, 15,5, 0,0,    1,1,1,1,1,1,1,1,1,1};
		chances[20] = chances[19] = chances[18] = chances[17] = chances[16];
		
		chances[21] = chances[5];
		
		chances[22] = new float[]{20,  0,0, 0,0, 0,0, 0,0, 15,5,    1,1,1,1,1,1,1,1,1,1};
		chances[26] = chances[25] = chances[24] = chances[23] = chances[22];*/
	}
	
	
	public static StandardRoom createRoom(){
		return Reflection.newInstance(rooms.get(Random.chances(chances[Dungeon.depth])));
	}
	
}
