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

package com.shatteredpixel.yasd.general.levels.traps;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.blobs.Blob;
import com.shatteredpixel.yasd.general.actors.blobs.Electricity;
import com.shatteredpixel.yasd.general.actors.blobs.StormCloud;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.utils.BArray;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

public class StormTrap extends Trap {
	
	{
		color = YELLOW;
		shape = STARS;
	}
	
	@Override
	public void activate() {
		
		if (Dungeon.level.heroFOV[pos]){
			Sample.INSTANCE.play( Assets.Sounds.LIGHTNING );
		}
		
		PathFinder.buildDistanceMap( pos, BArray.not( Dungeon.level.solid(), null ), 2 );
		for (int i = 0; i < PathFinder.distance.length; i++) {
			if (PathFinder.distance[i] < Integer.MAX_VALUE) {
				GameScene.add(Blob.seed(i, 20, Electricity.class));
				GameScene.add(Blob.seed(i, 20, StormCloud.class));
			}
		}
	}
	
}
