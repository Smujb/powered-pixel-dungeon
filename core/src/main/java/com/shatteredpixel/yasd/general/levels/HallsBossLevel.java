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
import com.shatteredpixel.yasd.general.Bones;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.actors.mobs.Yog;
import com.shatteredpixel.yasd.general.effects.CellEmitter;
import com.shatteredpixel.yasd.general.effects.particles.FlameParticle;
import com.shatteredpixel.yasd.general.items.Heap;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.keys.SkeletonKey;
import com.shatteredpixel.yasd.general.levels.interactive.Entrance;
import com.shatteredpixel.yasd.general.levels.interactive.Exit;
import com.shatteredpixel.yasd.general.levels.painters.Painter;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.watabou.noosa.Group;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import org.jetbrains.annotations.NotNull;

import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.EMPTY;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.EMPTY_DECO;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.EMPTY_SP;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.ENTRANCE;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.LOCKED_EXIT;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.WALL;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.WALL_DECO;
import static com.shatteredpixel.yasd.general.levels.terrain.Terrain.WATER;

public class HallsBossLevel extends Level {
	
	{
		color1 = 0x801500;
		color2 = 0xa68521;
		
		viewDistance = Math.min(4, viewDistance);
	}

	@Override
	public int getScaleFactor() {
		return new HallsLevel().getScaleFactor();
	}

	private static final int WIDTH = 32;
	private static final int HEIGHT = 32;

	private static final int ROOM_LEFT		= WIDTH / 2 - 1;
	private static final int ROOM_RIGHT		= WIDTH / 2 + 1;
	private static final int ROOM_TOP		= HEIGHT / 2 - 1;
	private static final int ROOM_BOTTOM	= HEIGHT / 2 + 1;
	
	private Entrance stairs = null;
	private boolean enteredArena = false;
	private boolean keyDropped = false;
	
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
	
	private static final String STAIRS	= "stairs";
	private static final String ENTERED	= "entered";
	private static final String DROPPED	= "droppped";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( STAIRS, stairs );
		bundle.put( ENTERED, enteredArena );
		bundle.put( DROPPED, keyDropped );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		stairs = (Entrance) bundle.get( STAIRS );
		enteredArena = bundle.getBoolean( ENTERED );
		keyDropped = bundle.getBoolean( DROPPED );
	}
	
	@Override
	protected boolean build() {
		
		setSize(32, 32);
		
		for (int i = 0; i < 5; i++) {
			
			int top = Random.IntRange(2, ROOM_TOP - 1);
			int bottom = Random.IntRange(ROOM_BOTTOM + 1, 22);
			Painter.fill(this, 2 + i * 4, top, 4, bottom - top + 1, EMPTY);
			
			if (i == 2) {
				int exit = (i * 4 + 3) + (top - 1) * width();
				interactiveAreas.add(new Exit().setPos(this, exit));
			}
			
			for (int j = 0; j < 4; j++) {
				if (Random.Int(2) == 0) {
					int y = Random.IntRange(top + 1, bottom - 1);
					map[i * 4 + j + y * width()] = WALL_DECO;
				}
			}
		}
		
		map[getExit().getPos(this)] = LOCKED_EXIT;
		
		Painter.fill(this, ROOM_LEFT - 1, ROOM_TOP - 1,
				ROOM_RIGHT - ROOM_LEFT + 3, ROOM_BOTTOM - ROOM_TOP + 3, WALL);
		Painter.fill(this, ROOM_LEFT, ROOM_TOP,
				ROOM_RIGHT - ROOM_LEFT + 1, ROOM_BOTTOM - ROOM_TOP + 1, EMPTY);
		
		int entrance = Random.Int(ROOM_LEFT + 1, ROOM_RIGHT - 1) +
				Random.Int(ROOM_TOP + 1, ROOM_BOTTOM - 1) * width();
		interactiveAreas.add(new Entrance().setPos(this, entrance));
		map[getEntrance().getPos(this)] = ENTRANCE;
		
		boolean[] patch = Patch.generate(width, height, 0.30f, 6, true);
		for (int i = 0; i < length(); i++) {
			if (map[i] == EMPTY && patch[i]) {
				map[i] = WATER;
			}
		}
		
		for (int i = 0; i < length(); i++) {
			if (map[i] == EMPTY && Random.Int(10) == 0) {
				map[i] = EMPTY_DECO;
			}
		}
		
		return true;
	}
	
	@Override
	protected void createMobs() {
	}
	
	public Actor respawner() {
		return null;
	}
	
	@Override
	protected void createItems() {
		Item item = Bones.get();
		if (item != null) {
			int pos;
			do {
				pos = Random.IntRange( ROOM_LEFT, ROOM_RIGHT ) + Random.IntRange( ROOM_TOP + 1, ROOM_BOTTOM ) * width();
			} while (pos == getEntrance().getPos(this));
			drop( item, pos ).setHauntedIfCursed().type = Heap.Type.REMAINS;
		}
	}
	
	@Override
	public int randomRespawnCell(Char ch) {
		int pos = getEntrance() == null ? stairs.getPos(this) : getEntrance().getPos(this);
		int cell;
		do {
			cell = pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
		} while (!passable(cell)
				|| (Char.hasProp(ch, Char.Property.LARGE) && !openSpace[cell])
				|| Actor.findChar(cell) != null);
		return cell;
	}
	
	@Override
	public void occupyCell(@NotNull Char ch ) {
		
		super.occupyCell( ch );
		
		if (!enteredArena && ch == Dungeon.hero && ch.pos != getEntrancePos()) {
			
			enteredArena = true;
			seal();
			
			for (int i=ROOM_LEFT-1; i <= ROOM_RIGHT + 1; i++) {
				doMagic( (ROOM_TOP - 1) * width() + i );
				doMagic( (ROOM_BOTTOM + 1) * width() + i );
			}
			for (int i=ROOM_TOP; i < ROOM_BOTTOM + 1; i++) {
				doMagic( i * width() + ROOM_LEFT - 1 );
				doMagic( i * width() + ROOM_RIGHT + 1 );
			}
			doMagic( getEntrance().getPos(this) );
			GameScene.updateMap();

			Dungeon.observe();
			
			Yog boss = Mob.create(Yog.class, this);
			do {
				boss.pos = Random.Int( length() );
			} while (
				!passable(boss.pos) ||
				heroFOV[boss.pos]);
			GameScene.add( boss );
			boss.spawnFists();
			
			stairs = getEntrance();
			interactiveAreas.remove(stairs);
			//entrance = -1;
		}
	}
	
	private void doMagic( int cell ) {
		set( cell, EMPTY_SP );
		CellEmitter.get( cell ).start( FlameParticle.FACTORY, 0.1f, 3 );
	}
	
	@Override
	public Heap drop( Item item, int cell ) {
		
		if (!keyDropped && item instanceof SkeletonKey) {
			keyDropped = true;
			unseal();
			
			//entrance = stairs;

			interactiveAreas.add(stairs);

			set( getEntrance().getPos(this), ENTRANCE );
			GameScene.updateMap( getEntrance().getPos(this) );
		}
		
		return super.drop( item, cell );
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
	public String tileDesc(@NotNull Terrain tile) {
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
		HallsLevel.addHallsVisuals( this, visuals );
		return visuals;
	}
}
