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

package com.shatteredpixel.yasd.general.levels;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.levels.painters.Painter;
import com.shatteredpixel.yasd.general.levels.painters.UnderwaterPainter;
import com.watabou.utils.Bundle;

public class UnderwaterLevel extends RegularLevel {
	{
		hasEntrance = false;

		hasExit = false;
	}

	public String tilesTex = Assets.TILES_HALLS;
	public String waterTex = Assets.WATER_HALLS;

	@Override
	public boolean[] liquid() {//All passable tiles are considered "liquid" due to being underwater.
		boolean[] liquid = new boolean[length()];
		for (int i = 0; i < liquid.length; i++) {
			liquid[i] = map[i].liquid || map[i].passable;
		}
		return liquid;
	}

	public UnderwaterLevel setParentLevel(Level level) {
		waterTex = level.waterTex();
		tilesTex = level.tilesTex();
		minScaleFactor = level.minScaleFactor;
		maxScaleFactor = level.maxScaleFactor;
		return this;
	}

	@Override
	protected Painter painter() {
		return new UnderwaterPainter();
	}

	@Override
	public String tilesTex() {
		if (waterTex.isEmpty()) {
			return Assets.TILES_HALLS;
		} else {
			return tilesTex;
		}
	}

	@Override
	public String waterTex() {
		if (waterTex.isEmpty()) {
			return Assets.WATER_HALLS;
		} else {
			return waterTex;
		}
	}

	@Override
	public String loadImg() {
		return waterTex();
	}

	private static final String TILE_TEX = "tile_tex";
	private static final String WATER_TEX = "water_tex";
	private static final String SCALE_FACTOR_MAX = "scalefactor-max";
	private static final String SCALE_FACTOR_MIN = "scalefactor-min";
	private static final String LIGHT_TILES = "light_tiles";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(TILE_TEX, tilesTex);
		bundle.put(WATER_TEX, waterTex);
		bundle.put(SCALE_FACTOR_MIN, minScaleFactor);
		bundle.put(SCALE_FACTOR_MAX, maxScaleFactor);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		tilesTex = bundle.getString(TILE_TEX);
		waterTex = bundle.getString(WATER_TEX);
		minScaleFactor = bundle.getInt(SCALE_FACTOR_MIN);
		maxScaleFactor = bundle.getInt(SCALE_FACTOR_MAX);
	}
}

