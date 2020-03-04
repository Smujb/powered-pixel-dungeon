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
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.effects.particles.ShaftParticle;
import com.shatteredpixel.yasd.general.tiles.DungeonTilemap;
import com.watabou.noosa.Group;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.PointF;

import java.util.ArrayList;

public class UnderwaterLevel extends Level {
	{
		hasEntrance = false;

		hasExit = false;
	}

	public String tilesTex = Assets.TILES_HALLS;
	public String waterTex = Assets.WATER_HALLS;

	private int _width = -1;
	private int _height = -1;

	private ArrayList<Integer> lightLocations = new ArrayList<>();

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
		_height = level.height();
		_width = level.width();
		lightLocations = level.getTileLocations(Terrain.DEEP_WATER);
		return this;
	}

	@Override
	protected boolean build() {
		setSize(_width, _height);
		map = Level.basicMap(length());
		entrance = lightLocations.get(0);
		return true;
	}

	@Override
	protected void createMobs() {

	}

	@Override
	protected void createItems() {

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

	@Override
	public Group addVisuals() {
		Group visuals = super.addVisuals();
		for (int i=0; i < length(); i++) {
			if (lightLocations.contains(i)) {
				visuals.add(new Light(i));
			}
		}
		return visuals;
	}

	private static final String TILE_TEX = "tile_tex";
	private static final String WATER_TEX = "water_tex";
	private static final String SCALE_FACTOR_MAX = "scalefactor-max";
	private static final String SCALE_FACTOR_MIN = "scalefactor-min";
	private static final String LIGHT_TILE = "light_tile";
	private static final String LIGHT_TILE_AMT = "light_tiles_num";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(TILE_TEX, tilesTex);
		bundle.put(WATER_TEX, waterTex);
		bundle.put(SCALE_FACTOR_MIN, minScaleFactor);
		bundle.put(SCALE_FACTOR_MAX, maxScaleFactor);
		bundle.put(LIGHT_TILE_AMT, lightLocations.size());
		for (int i = 0; i < lightLocations.size(); i++) {
			bundle.put(LIGHT_TILE+i, lightLocations.get(i));
		}
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		tilesTex = bundle.getString(TILE_TEX);
		waterTex = bundle.getString(WATER_TEX);
		minScaleFactor = bundle.getInt(SCALE_FACTOR_MIN);
		maxScaleFactor = bundle.getInt(SCALE_FACTOR_MAX);
		int num = bundle.getInt(LIGHT_TILE_AMT);
		for (int i = 0; i < num; i++) {
			lightLocations.add(bundle.getInt(LIGHT_TILE+i));
		}
	}

	static class Light extends Emitter {

		private int pos;

		private static final Emitter.Factory factory = new Factory() {

			@Override
			public void emit( Emitter emitter, int index, float x, float y ) {
				ShaftParticle p = (ShaftParticle)emitter.recycle( ShaftParticle.class );
				p.reset( x, y );
			}
		};

		public Light( int pos ) {
			super();

			this.pos = pos;

			PointF p = DungeonTilemap.tileCenterToWorld( pos );
			pos( p.x - 6, p.y - 4, 12, 12 );

			pour( factory, 0.05f );
		}

		@Override
		public void update() {
			if (visible = (pos < Dungeon.level.heroFOV.length && Dungeon.level.heroFOV[pos])) {
				super.update();
			}
		}
	}
}

