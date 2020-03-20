/*
 *
 *   Pixel Dungeon
 *   Copyright (C) 2012-2015 Oleg Dolya
 *
 *   Shattered Pixel Dungeon
 *   Copyright (C) 2014-2019 Evan Debenham
 *
 *   Yet Another Shattered Dungeon
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

package com.shatteredpixel.yasd.general.levels.rooms;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.items.keys.IronKey;
import com.shatteredpixel.yasd.general.items.potions.PotionOfLevitation;
import com.shatteredpixel.yasd.general.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.yasd.general.levels.Level;
import com.shatteredpixel.yasd.general.levels.RegularLevel;
import com.shatteredpixel.yasd.general.levels.Terrain;
import com.shatteredpixel.yasd.general.levels.rooms.special.SpecialRoom;
import com.watabou.utils.Random;

public abstract class LockedRoom extends SpecialRoom {
	@Override
	public void paint(Level level) {
		paintRoom(level);
		if (level instanceof RegularLevel) {
			RegularLevel regularLevel = ((RegularLevel)level);
			if (regularLevel.hasPitRoom()) {
				entrance().set(Door.Type.REGULAR);
			} else {

				switch (Random.Int(10)) {
					case 0: default:
						setKeyDoor(level);
						break;
					case 1:
						setBarricadeDoor(level);
						break;
					//case 2:
					//	setChasmDoor(level);
					//	break;
				}

			}
		} else {
			setKeyDoor(level);
		}
	}

	private void setChasmDoor(Level level) {
		level.map[level.XY(entrance().x, entrance().y)] = Terrain.CHASM;
		level.addItemToSpawn(new PotionOfLevitation());
	}

	private void setBarricadeDoor(Level level) {
		entrance().set(Door.Type.BARRICADE);
		level.addItemToSpawn(new PotionOfLiquidFlame());
	}

	private void setKeyDoor(Level level) {
		entrance().set(Door.Type.LOCKED);
		level.addItemToSpawn(new IronKey(Dungeon.xPos, Dungeon.yPos, Dungeon.zPos));
	}

	public abstract void paintRoom(Level level);
}
