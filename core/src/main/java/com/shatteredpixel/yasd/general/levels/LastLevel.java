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

package com.shatteredpixel.yasd.general.levels;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.items.Amulet;
import com.shatteredpixel.yasd.general.levels.interactive.Entrance;
import com.shatteredpixel.yasd.general.levels.painters.Painter;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.watabou.noosa.Group;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.Arrays;

import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.CHASM;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.EMPTY;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.EMPTY_DECO;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.ENTRANCE;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.PEDESTAL;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.STATUE_SP;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.WALL;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.WATER;

public class LastLevel extends Level {

	{
		color1 = 0x801500;
		color2 = 0xa68521;

		minScaleFactor = 25;
		maxScaleFactor = -1;

		hasExit = false;
	}

	private int pedestal;

	@Override
	public boolean passable(int pos) {
		return super.passable(pos) & !pit(pos);
	}

	@Override
	public String tilesTex() {
		return Assets.TILES_HALLS;
	}

	@Override
	public String waterTex() {
		return Assets.WATER_HALLS;
	}

	@Override
	public String loadImg() {
		return Assets.LOADING_HALLS;
	}

	@Override
	protected boolean build() {
		
		setSize(16, 64);
		Arrays.fill( map, CHASM );

		int mid = width/2;

		Painter.fill( this, 0, height-1, width, 1, WALL );
		Painter.fill( this, mid - 1, 10, 3, (height-11), EMPTY);
		Painter.fill( this, mid - 2, height - 3, 5, 1, EMPTY);
		Painter.fill( this, mid - 3, height - 2, 7, 1, EMPTY);

		Painter.fill( this, mid - 2, 9, 5, 7, EMPTY);
		Painter.fill( this, mid - 3, 10, 7, 5, EMPTY);

		//entrance = (height-2) * width() + mid;
		interactiveAreas.add(new Entrance().setPos(this, (height-2) * width() + mid));
		map[getEntrance().getPos(this)] = ENTRANCE;

		pedestal = 12*(width()) + mid;
		map[pedestal] = PEDESTAL;
		map[pedestal-1-width()] = map[pedestal+1-width()] = map[pedestal-1+width()] = map[pedestal+1+width()] = STATUE_SP;

		//exit = pedestal;
		//interactiveAreas.add(new Exit().setPos(this, pedestal));

		int pos = pedestal;

		map[pos-width()] = map[pos-1] = map[pos+1] = map[pos-2] = map[pos+2] = WATER;
		pos+=width();
		map[pos] = map[pos-2] = map[pos+2] = map[pos-3] = map[pos+3] = WATER;
		pos+=width();
		map[pos-3] = map[pos-2] = map[pos-1] = map[pos] = map[pos+1] = map[pos+2] = map[pos+3] = WATER;
		pos+=width();
		map[pos-2] = map[pos+2] = WATER;
		
		for (int i=0; i < length(); i++) {
			if (map[i] == EMPTY && Random.Int( 10 ) == 0) {
				map[i] = EMPTY_DECO;
			}
		}

		feeling = Feeling.NONE;

		return true;
	}

	@Override
	//Acts as if the exit is on the pedestal even though the level has no exit
	public int getExitPos() {
		return pedestal;
	}

	@Override
	public Mob createMob() {
		return null;
	}
	
	@Override
	protected void createMobs() {
	}

	public Actor respawner() {
		return null;
	}

	@Override
	protected void createItems() {
		drop( new Amulet(), pedestal );
	}

	@Override
	public int randomRespawnCell() {
		int cell;
		do {
			cell = getEntrance().getPos(this) + PathFinder.NEIGHBOURS8[Random.Int(8)];
		} while (!passable(cell) || Actor.findChar(cell) != null);
		return cell;
	}

	@Override
	public String tileName( Terrain tile ) {
		switch (tile) {
			case WATER:
				return Messages.get(HallsLevel.class, "water_name");
			case GRASS:
				return Messages.get(HallsLevel.class, "grass_name");
			case HIGH_GRASS:
				return Messages.get(HallsLevel.class, "high_grass_name");
			case STATUE:
			case STATUE_SP:
				return Messages.get(HallsLevel.class, "statue_name");
			default:
				return super.tileName( tile );
		}
	}

	@Override
	public String tileDesc(Terrain tile) {
		switch (tile) {
			case WATER:
				return Messages.get(HallsLevel.class, "water_desc");
			case STATUE:
			case STATUE_SP:
				return Messages.get(HallsLevel.class, "statue_desc");
			case BOOKSHELF:
				return Messages.get(HallsLevel.class, "bookshelf_desc");
			default:
				return super.tileDesc( tile );
		}
	}

	@Override
	public Group addVisuals () {
		super.addVisuals();
		HallsLevel.addHallsVisuals(this, visuals);
		return visuals;
	}
}
