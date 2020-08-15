/*
 *
 *  * Pixel Dungeon
 *  * Copyright (C) 2012-2015 Oleg Dolya
 *  *
 *  * Shattered Pixel Dungeon
 *  * Copyright (C) 2014-2019 Evan Debenham
 *  *
 *  * Powered Pixel Dungeon
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

package com.shatteredpixel.yasd.general.items;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.utils.BArray;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

abstract public class KindOfWeapon extends KindofMisc {

	public String hitSound = Assets.Sounds.HIT;
	public float hitSoundPitch = 1f;

	public enum Property {
		DUAL_HANDED {
			@Override
			public boolean canApply(KindOfWeapon weapon) {
				return !weapon.properties.contains(SINGLE_HANDED);
			}
		},
		SINGLE_HANDED {
			@Override
			public boolean canApply(KindOfWeapon weapon) {
				return !weapon.properties.contains(DUAL_HANDED);
			}
		},
		BLUNT,
		CANT_SURPRISE_ATTK {
			@Override
			public boolean canApply(KindOfWeapon weapon) {
				return !weapon.properties.contains(SURPRISE_ATTK_BENEFIT);
			}
		},
		SURPRISE_ATTK_BENEFIT {
			@Override
			public boolean canApply(KindOfWeapon weapon) {
				return !weapon.properties.contains(CANT_SURPRISE_ATTK);
			}
		};

		public boolean canApply(KindOfWeapon weapon) {
			return true;
		}

		public float importance() {
			switch (this) {
				default:
					return 1.3f;
				case BLUNT:
					return 1.4f;
				case SURPRISE_ATTK_BENEFIT:
					return 1.8f;
			}
		}

		public String displayName() {
			return Messages.get(KindOfWeapon.class, name());
		}
	}

	public ArrayList<Property> properties = new ArrayList<>();

	public boolean breaksArmor(Char owner) {
		return properties.contains(Property.BLUNT);
	}

	public int min(){
		return min(level());
	}

	public int max(){
		return max(level());
	}

	abstract public int min(float lvl);
	abstract public int max(float lvl);

	public int damageRoll( Char owner ) {
		return Random.NormalIntRange(min(), max());
	}
	
	public float accuracyFactor( Char owner ) {
		return 1f;
	}
	
	public float speedFactor( Char owner ) {
		return 1f;
	}

	public int reachFactor( Char owner ){
		return 1;
	}
	
	public boolean canReach( Char owner, int target){
		if (Dungeon.level.distance( owner.pos, target ) > reachFactor(owner)){
			return false;
		} else {
			boolean[] passable = BArray.not(Dungeon.level.solid(), null);
			for (Char ch : Actor.chars()) {
				if (ch != owner) passable[ch.pos] = false;
			}
			
			PathFinder.buildDistanceMap(target, passable, reachFactor(owner));
			
			return PathFinder.distance[owner.pos] <= reachFactor(owner);
		}
	}

	public int defenseFactor( Char owner ) {
		return 0;
	}
	
	public int proc( Char attacker, Char defender, int damage ) {
		return damage;
	}

	public void hitSound( float pitch ){
		Sample.INSTANCE.play(hitSound, 1, pitch * hitSoundPitch);
	}
}
