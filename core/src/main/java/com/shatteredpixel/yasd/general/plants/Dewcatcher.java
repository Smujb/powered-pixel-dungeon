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

package com.shatteredpixel.yasd.general.plants;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.items.Dewdrop;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Dewcatcher extends Plant{

	{
		image = 13;
	}

	@Override
	public void activate( Char ch ) {

		int nDrops = Random.NormalIntRange(3, 6);

		ArrayList<Integer> candidates = new ArrayList<>();
		for (int i : PathFinder.NEIGHBOURS8){
			if (Dungeon.level.passable(pos+i)
					&& pos+i != Dungeon.level.getEntrancePos()
					&& pos+i != Dungeon.level.getExitPos()) {
				candidates.add(pos+i);
			}
		}

		for (int i = 0; i < nDrops && !candidates.isEmpty(); i++){
			Integer c = Random.element(candidates);
			Dungeon.level.drop(new Dewdrop(), c).sprite.drop(pos);
			candidates.remove(c);
		}

	}

	//seed is never dropped, only care about plant class
	public static class Seed extends Plant.Seed {
		{
			plantClass = Dewcatcher.class;
		}
	}
}
