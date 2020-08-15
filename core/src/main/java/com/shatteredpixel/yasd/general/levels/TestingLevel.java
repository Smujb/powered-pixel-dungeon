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

package com.shatteredpixel.yasd.general.levels;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.actors.mobs.Rat;
import com.shatteredpixel.yasd.general.levels.painters.Painter;
import com.shatteredpixel.yasd.general.levels.painters.SewerPainter;
import com.shatteredpixel.yasd.general.levels.traps.AlarmTrap;
import com.shatteredpixel.yasd.general.levels.traps.ChillingTrap;
import com.shatteredpixel.yasd.general.levels.traps.ConfusionTrap;
import com.shatteredpixel.yasd.general.levels.traps.FlockTrap;
import com.shatteredpixel.yasd.general.levels.traps.OozeTrap;
import com.shatteredpixel.yasd.general.levels.traps.ShockingTrap;
import com.shatteredpixel.yasd.general.levels.traps.SummoningTrap;
import com.shatteredpixel.yasd.general.levels.traps.TeleportationTrap;
import com.shatteredpixel.yasd.general.levels.traps.ToxicTrap;
import com.shatteredpixel.yasd.general.levels.traps.WornDartTrap;
import com.watabou.utils.Random;

public class TestingLevel extends RegularLevel {

	@Override
	public int nMobs() {
		return 50;
	}

	@Override
	protected int standardRooms() {
		//5 to 7, average 5.57
		return 20 * (5+ Random.chances(new float[]{4, 2, 1}));
	}

	@Override
	protected int specialRooms() {
		//1 to 3, average 1.67
		return 5 * (1+Random.chances(new float[]{4, 4, 2}));
	}

	@Override
	protected Painter painter() {
		return new SewerPainter()
				.setWater(feeling == Feeling.WATER ? 0.85f : 0.30f, 5)
				.setGrass(feeling == Feeling.GRASS ? 0.80f : 0.20f, 4)
				.setTraps(nTraps(), trapClasses(), trapChances());
	}

	@Override
	public String tilesTex() {
		return Assets.Environment.TILES_SEWERS;
	}

	@Override
	public String waterTex() {
		return Assets.Environment.WATER_SEWERS;
	}

	@Override
	public String loadImg() {
		return Assets.Interfaces.LOADING_SEWERS;
	}

	@Override
	protected Class<?>[] trapClasses() {
		return new Class<?>[]{
				ChillingTrap.class, ShockingTrap.class, ToxicTrap.class, WornDartTrap.class,
				AlarmTrap.class, OozeTrap.class,
				ConfusionTrap.class, FlockTrap.class, SummoningTrap.class, TeleportationTrap.class };
	}

	@Override
	protected float[] trapChances() {
		return new float[]{
				4, 4, 4, 4,
				2, 2,
				1, 1, 1, 1};
	}

	@Override
	public Class<?>[] mobClasses() {
		return new Class[]{Rat.class};
	}

	@Override
	public float[] mobChances() {
		return new float[]{1};
	}

	protected float[] connectionRoomChances() {
		return new float[]{
				20,
				1,
				0,
				2,
				2,
				1,
				1};
	}

	@Override
	protected float[] standardRoomChances() {
		return new float[]{20,  15,5, 15,5, 15,5, 15,5, 15,5,    1,1,1,1,1,1,1,1,1,1};
	}
}
