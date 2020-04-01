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

package com.shatteredpixel.yasd.general;

import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.shatteredpixel.yasd.general.levels.Level;
import com.shatteredpixel.yasd.general.levels.Terrain;

public class MapHandler {

	private static final String TILES_LAYER = "tiles";
	private static final String MOBS_LAYER = "mobs";
	private static final String ITEMS_LAYER = "items";

	private static TiledMapTileLayer tiles;
	private static MapObjects mobs;
	private static MapObjects items;

	private static void loadMap(Map map) {
		tiles = (TiledMapTileLayer) map.getLayers().get(TILES_LAYER);
		mobs = map.getLayers().get(MOBS_LAYER).getObjects();
		items = map.getLayers().get(ITEMS_LAYER).getObjects();
	}

	public static boolean build(Level level, String mapName) {
		loadMap(new TmxMapLoader().load(mapName));
		int width = tiles.getWidth();
		int height = tiles.getHeight();
		level.setSize(width, height);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Terrain toSet;
				int pos = level.XY(x, y);
				TiledMapTileLayer.Cell cell = tiles.getCell(x, y);
				if (cell == null) {
					toSet = Terrain.CHASM;
				} else {
					TiledMapTile tile = cell.getTile();
					int tileId = tile.getId();
					toSet = mapToTerrain(tileId);
					//Ideally levels should only have one exit and entrance, so only one of these tiles should be placed. If more are used, only the last one will be the real exit/entrance.
					if (toSet == Terrain.ENTRANCE) {
						level.entrance = pos;
					} else if (toSet == Terrain.EXIT || toSet == Terrain.LOCKED_EXIT || toSet == Terrain.UNLOCKED_EXIT) {
						level.exit = pos;
					}
				}
				level.map[pos] = toSet;
			}
		}
		return true;
	}

	private static Terrain mapToTerrain(int tile) {
		switch (tile) {
			case 0:
				return Terrain.CHASM;
			case 1:
				return Terrain.EMPTY;
			case 2:
				return Terrain.EMPTY_DECO;
			case 3:
				return Terrain.GRASS;
			case 4:
				return Terrain.EMBERS;
			case 5:
				return Terrain.EMPTY_SP;
			case 6:
				return Terrain.WATER;
			case 7:
				return Terrain.ENTRANCE;
			case 8:
				return Terrain.EXIT;
			case 9:
				return Terrain.EMPTY_WELL;
			case 10:
				return Terrain.WELL;
			case 11:
				return Terrain.PEDESTAL;
			case 13:
				return Terrain.WALL;
			case 14:
				return Terrain.WALL_DECO;
			case 15:
				return Terrain.BOOKSHELF;
			case 16:
				return Terrain.CRACKED_WALL;
			case 17:
				return Terrain.BARRICADE;
			case 19:
				return Terrain.DOOR;
			case 21:
				return Terrain.LOCKED_DOOR;
			case 22:
				return Terrain.UNLOCKED_EXIT;
			case 23:
				return Terrain.LOCKED_EXIT;
			case 24:
				return Terrain.BRONZE_LOCKED_DOOR;
			case 25:
				return Terrain.SIGN;
			case 26:
				return Terrain.STATUE;
			case 27:
				return Terrain.STATUE_SP;
			case 28:
				return Terrain.ALCHEMY;
			case 29:
				return Terrain.HIGH_GRASS;
			case 30:
				return Terrain.FURROWED_GRASS;

		}
		return Terrain.CHASM;
	}
}
