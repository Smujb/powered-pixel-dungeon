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

package com.shatteredpixel.yasd.general.items.wands;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.effects.MagicMissile;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.mechanics.ConeAOE;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class AOEWand extends NormalWand {

	private static final int BASE_RANGE = 5;
	private int range = BASE_RANGE;


	ConeAOE cone;

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
		//10% less damage than an equal bolt wand.
		return multiplier * 0.9f;
	}

	@Override
	public void onZap(Ballistica bolt) {
		ArrayList<Char> affectedChars = new ArrayList<>();
		for( int cell : cone.cells ){

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

	@Override
	public String statsDesc() {
		return Messages.get(this, "stats_desc", range, chargesPerCast(), min(), max());
	}

	@Override
	protected void fx( Ballistica bolt, Callback callback ) {
		//need to perform spread logic here so we can determine what cells to put vfx in.

		// 4/6/8 distance
		int maxDist = range;
		int dist = Math.min(bolt.dist, maxDist);

		cone = new ConeAOE( bolt.sourcePos, bolt.path.get(dist),
				maxDist,
				30 + 20*chargesPerCast(),
				collisionProperties);

		//cast to cells at the tip, rather than all cells, better performance.
		for (Ballistica ray : cone.rays){

			//this way we only get the cells at the tip, much better performance.
			((MagicMissile)curUser.sprite.parent.recycle( MagicMissile.class )).reset(
					getMagicMissileFX(),
					curUser.sprite,
					ray.path.get(ray.dist),
					null
			);
		}
		element.FX(curUser, bolt.path.get(dist/2), callback);
		Sample.INSTANCE.play( Assets.Sounds.ZAP );
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
