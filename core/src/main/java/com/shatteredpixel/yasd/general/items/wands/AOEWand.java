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

package com.shatteredpixel.yasd.general.items.wands;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.effects.MagicMissile;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class AOEWand extends NormalWand {

	private static final int BASE_RANGE = 5;
	private int range = BASE_RANGE;

	//the actual affected cells
	private HashSet<Integer> affectedCells;
	//the cells to trace fire shots to, for visual effects.
	private HashSet<Integer> visualCells;
	private int direction = 0;

	@Override
	protected NormalWand initStats() {
		range = Random.IntRange(3, 10);
		return super.initStats();
	}

	@Override
	protected float getDamageMultiplier() {
		float multiplier = super.getDamageMultiplier();
		if (range > BASE_RANGE) {
			multiplier *= Math.pow(0.95, range-BASE_RANGE);
		}
		return multiplier;
	}

	@Override
	public void onZap(Ballistica bolt) {
		ArrayList<Char> affectedChars = new ArrayList<>();
		for( int cell : affectedCells.toArray(new Integer[0]) ){

			//ignore caster cell
			if (cell == bolt.sourcePos){
				continue;
			}

			affectCell(bolt.sourcePos, cell);

			Char ch = Actor.findChar( cell );
			if (ch != null) {
				affectedChars.add(ch);
			}
		}

		for ( Char ch : affectedChars.toArray(new Char[0]) ){
			processSoulMark(ch, chargesPerCast());
			hit(ch);
		}
	}

	private int left(int direction){
		return direction == 0 ? 7 : direction-1;
	}

	private int right(int direction){
		return direction == 7 ? 0 : direction+1;
	}

	private void spreadBlast(int cell, float strength){
		if (strength >= 0 && (Dungeon.level.passable(cell) || Dungeon.level.flammable(cell))){
			affectedCells.add(cell);
			if (strength >= 1.5f) {
				visualCells.remove(cell);
				spreadBlast(cell + PathFinder.CIRCLE8[left(direction)], strength - 1.5f);
				spreadBlast(cell + PathFinder.CIRCLE8[direction], strength - 1.5f);
				spreadBlast(cell + PathFinder.CIRCLE8[right(direction)], strength - 1.5f);
			} else {
				visualCells.add(cell);
			}
		} else if (!Dungeon.level.passable(cell))
			visualCells.add(cell);
	}

	@Override
	public String statsDesc() {
		return Messages.get(this, "stats_desc", range, chargesPerCast(), min(), max());
	}

	@Override
	protected void fx( Ballistica bolt, Callback callback ) {
		//need to perform spread logic here so we can determine what cells to put flames in.
		affectedCells = new HashSet<>();
		visualCells = new HashSet<>();

		// 4/6/8 distance
		int maxDist = range;
		int dist = Math.min(bolt.dist, maxDist);

		for (int i = 0; i < PathFinder.CIRCLE8.length; i++){
			if (bolt.sourcePos+PathFinder.CIRCLE8[i] == bolt.path.get(1)){
				direction = i;
				break;
			}
		}

		float strength = maxDist;
		for (int c : bolt.subPath(1, dist)) {
			strength--; //as we start at dist 1, not 0.
			affectedCells.add(c);
			if (strength > 1) {
				spreadBlast(c + PathFinder.CIRCLE8[left(direction)], strength - 1);
				spreadBlast(c + PathFinder.CIRCLE8[direction], strength - 1);
				spreadBlast(c + PathFinder.CIRCLE8[right(direction)], strength - 1);
			} else {
				visualCells.add(c);
			}
		}

		//going to call this one manually
		visualCells.remove(bolt.path.get(dist));

		for (int cell : visualCells){

			//this way we only get the cells at the tip, much better performance.
			((MagicMissile)curUser.sprite.parent.recycle( MagicMissile.class )).reset(
					getMagicMissileFX(),
					curUser.sprite,
					cell,
					null
			);
		}
		element.FX(curUser, bolt.path.get(dist/2), callback);
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}

	private int getMagicMissileFX() {
		int FX = MagicMissile.MAGIC_MISSILE;
		switch (element) {
			case PHYSICAL:
				FX = MagicMissile.PLASMA_BOLT;
				break;
			case MAGICAL:
				break;
			case EARTH:
				FX = MagicMissile.EARTH;
				break;
			case GRASS:
				FX = MagicMissile.FOLIAGE;
				break;
			case STONE:
				FX = MagicMissile.BONE;
				break;
			case SHARP:
				FX = MagicMissile.SLICE;
				break;
			case FIRE:
				FX = MagicMissile.FIRE_CONE;
				break;
			case DESTRUCTION:
				FX = MagicMissile.DISINT;
				break;
			case ACID:
				FX = MagicMissile.ACID;
				break;
			case DRAIN:
				FX =  MagicMissile.BEACON;
				break;
			case WATER:
				FX = MagicMissile.WATER_CONE;
				break;
			case COLD:
				FX = MagicMissile.FROST;
				break;
			case TOXIC:
				FX = MagicMissile.TOXIC_VENT;
				break;
			case CONFUSION:
				FX = MagicMissile.ELMO;
				break;
			case AIR:
				FX = MagicMissile.RAINBOW;
				break;
			case SHOCK:
				FX = MagicMissile.SHOCK_BALL;
				break;
			case SPIRIT:
				FX = MagicMissile.SHADOW;
				break;
		}
		return FX;
	}
}
