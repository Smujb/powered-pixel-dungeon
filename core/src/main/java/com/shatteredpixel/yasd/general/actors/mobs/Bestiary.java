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

package com.shatteredpixel.yasd.general.actors.mobs;

import com.watabou.utils.Random;

public class Bestiary {

	public static Class<? extends Mob> swapMobAlt( Class<? extends Mob> mob ) {
		Class<? extends Mob> cl = mob;
		if (Random.Int( 5 ) == 0) {
			if (cl == Rat.class) {
				cl = Albino.class;
			} else if (cl == Slime.class) {
				cl = CausticSlime.class;
			} else if (cl == Thief.class) {
				cl = Bandit.class;
			} else if (cl == Brute.class) {
				cl = Shielded.class;
			} else if (cl == Monk.class) {
				cl = Senior.class;
			} else if (cl == Scorpio.class) {
				cl = Acidic.class;
			}
		}
		return cl;
	}

	/*public static Mob getMob() {
		Mob mob;
		Class<? extends Mob> mobClass = getMobClass(Dungeon.depth, Dungeon.path);
		mobClass = swapMobAlt(mobClass);
		do {
			mob = Reflection.newInstance(mobClass);
		} while (mob == null);
		switch (Dungeon.difficulty) {
			case 1://Easy = -25% max HP
				mob.HP = mob.HT*=0.75f;
				break;
			case 2: default://Medium = regular max HP
				break;
			case 3://Hard = +25% max HP
				mob.HP = mob.HT*=1.25f;
				break;
		}
		return mob;
	}

	private static Class<? extends Mob> getMobClass( int depth, int path ) {
		if (path == 0) {
			if (depth == Constants.CHAPTER_LENGTH) {
				return CausticSlime.class;
			}
			if (depth < Constants.CHAPTER_LENGTH) {
				return getSewerMobClass(depth % Constants.CHAPTER_LENGTH);
			} else if (depth <= Constants.CHAPTER_LENGTH * 2) {
				return getPrisonMobClass(depth % Constants.CHAPTER_LENGTH);
			} else if (depth <= Constants.CHAPTER_LENGTH * 3) {
				return getCavesMobClass(depth % Constants.CHAPTER_LENGTH);
			} else if (depth <= Constants.CHAPTER_LENGTH * 4) {
				return getCityMobClass(depth % Constants.CHAPTER_LENGTH);
			} else if (depth <= Constants.CHAPTER_LENGTH * 5) {
				return getHallsMobClass(depth % Constants.CHAPTER_LENGTH);
			}
		} else if (path == 1) {
			switch (Random.Int(4)) {
				case 0:
					return Warlock.class;
				case 1:
					return Eye.class;
				case 2:
					return Shaman.class;
			}
		}
		return Wraith.class;
	}

	private static Class<? extends Mob> getSewerMobClass(int depth) {
		float[] chances = new float[] {
				4 - depth,
				1 ,
				depth <= 4 ? 0 : 2,
				2,
				3,
				depth <= 2 ? 0 : 3};
		if (depth == 1) {
			chances = new float[] { 3, 1, 0, 0, 0 };
		} else if (depth == 6 || depth == 0) {
			return CausticSlime.class;
		}
		ArrayList<Class<? extends Mob>> mobs = new ArrayList<>(Arrays.asList(
				Rat.class,
				Snake.class,
				Slime.class,
				Gnoll.class,
				Thief.class,
				Crab.class
		));
		return mobs.get(Random.chances(chances));
	}

	private static Class<? extends Mob> getPrisonMobClass(int depth) {
		float[] chances = new float[] {
				3 - depth,
				1,
				depth/2f,
				depth == 1 ? 0 : 2,
				depth <= 2 ? 0 : 3};
		ArrayList<Class<? extends Mob>> mobs = new  ArrayList<>(Arrays.asList(
				Skeleton.class,
				Swarm.class,
				Shaman.class,
				Guard.class,
				Necromancer.class
		));
		return mobs.get(Random.chances(chances));
	}

	private static Class<? extends Mob> getCavesMobClass(int depth) {
		float[] chances = new float[]{
				4 - depth,
				depth == 1 ? 1 : 3,
				depth <= 4 ? 0 : 2,
				depth == 1 ? 0 : 2
		};
		ArrayList<Class<? extends Mob>> mobs = new  ArrayList<>(Arrays.asList(
				Bat.class,
				Brute.class,
				Spinner.class,
				Shaman.class
		));
		return mobs.get(Random.chances(chances));
	}

	private static Class<? extends Mob> getCityMobClass(int depth) {
		float[] chances = new float[]{
				5 - depth,
				5 - depth,
				1 + depth,
				depth <= 3 ? 0 : 4
		};
		ArrayList<Class<? extends Mob>> mobs = new  ArrayList<>(Arrays.asList(
				Warlock.class,
				Elemental.class,
				Monk.class,
				Golem.class
		));
		return mobs.get(Random.chances(chances));
	}

	private static Class<? extends Mob> getHallsMobClass(int depth) {
		float[] chances = new float[] {
				3,
				depth >= 3 ? 0 : 4,
				3
		};
		ArrayList<Class<? extends Mob>> mobs = new ArrayList<>(Arrays.asList(
				Eye.class,
				Scorpio.class,
				Succubus.class
		));
		return mobs.get(Random.chances(chances));
	}*/
}
