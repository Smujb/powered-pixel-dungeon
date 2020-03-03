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
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.blobs.WellWater;
import com.shatteredpixel.yasd.general.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.yasd.general.levels.features.Door;
import com.shatteredpixel.yasd.general.levels.features.HighGrass;
import com.shatteredpixel.yasd.general.levels.traps.Trap;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.plants.Swiftthistle;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.noosa.audio.Sample;

public enum Terrain {

	NONE {
		@Override
		public void setup() {}
	},
	CHASM {
		@Override
		public void setup() {
			avoid = true;
			pit = true;
		}
	},
	EMPTY {
		@Override
		public void setup() {
			passable = true;
		}
	},
	GRASS {
		@Override
		public void setup() {
			passable = true;
			flamable = true;
		}
	},
	EMPTY_WELL {
		@Override
		public void setup() {
			passable = true;
		}
	},
	WATER {
		@Override
		public void setup() {
			passable = true;
			liquid = true;
		}
	},
	DEEP_WATER {
		@Override
		public void setup() {
			passable = true;
			liquid = true;
		}
	},
	WALL {
		@Override
		public void setup() {
			losBlocking = true;
			solid = true;
		}
	},
	DOOR {
		@Override
		public void setup() {
			passable = true;
			losBlocking = true;
			flamable = true;
			solid = true;
		}

		@Override
		public void press(int cell, boolean hard) {
			Door.enter(cell);
		}
	},
	OPEN_DOOR {
		@Override
		public void setup() {
			passable = true;
			flamable = true;
		}
	},
	ENTRANCE {
		@Override
		public void setup() {
			passable = true;
		}
	},
	EXIT {
		@Override
		public void setup() {
			passable = true;
		}
	},
	EMBERS {
		@Override
		public void setup() {
			passable = true;
		}
	},
	DRY {
		@Override
		public void setup() {
			passable = true;
		}
	},
	LOCKED_DOOR {
		@Override
		public void setup() {
			losBlocking = true;
			solid = true;
		}
	},
	PEDESTAL {
		@Override
		public void setup() {
			passable = true;
		}
	},
	WALL_DECO {
		@Override
		public void setup() {
			losBlocking = true;
			solid = true;
		}
	},
	BARRICADE {
		@Override
		public void setup() {
			flamable = true;
			solid = true;
			losBlocking = true;
		}
	},
	EMPTY_SP {
		@Override
		public void setup() {
			passable = true;
		}
	},
	HIGH_GRASS {
		@Override
		public void setup() {
			passable = true;
			losBlocking = true;
			flamable = true;
		}

		@Override
		public void press(int cell, boolean hard) {
			HighGrass.trample(Dungeon.level, cell);
		}
	},
	FURROWED_GRASS {
		@Override
		public void setup() {
			passable = true;
			losBlocking = true;
			flamable = true;
		}

		@Override
		public void press(int cell, boolean hard) {
			HIGH_GRASS.press(cell, hard);
		}
	},
	SECRET_DOOR {
		@Override
		public void setup() {
			losBlocking = true;
			solid = true;
			secret = true;
		}

		@Override
		public Terrain discover() {
			return DOOR;
		}
	},
	TRAP {
		@Override
		public void setup() {
			avoid = true;
		}

		@Override
		public void press(int cell, boolean hard) {
			Trap trap = Dungeon.level.traps.get(cell);
			triggerTrap(cell, trap);
		}
	},
	SECRET_TRAP {
		@Override
		public void setup() {
			passable = true;
			secret = true;
		}

		@Override
		public Terrain discover() {
			return TRAP;
		}

		@Override
		public void press(int cell, boolean hard) {
			if (hard) {
				Trap trap = Dungeon.level.traps.get(cell);
				GLog.i(Messages.get(Level.class, "hidden_trap", trap.name));
				triggerTrap(cell, trap);
			}
		}
	},
	INACTIVE_TRAP {
		@Override
		public void setup() {
			passable = true;
		}
	},
	EMPTY_DECO {
		@Override
		public void setup() {
			passable = true;
		}
	},
	LOCKED_EXIT {
		@Override
		public void setup() {
			solid = true;
		}
	},
	UNLOCKED_EXIT {
		@Override
		public void setup() {
			passable = true;
		}
	},
	SIGN {
		@Override
		public void setup() {
			passable = true;
			flamable = true;
		}
	},
	WELL {
		@Override
		public void setup() {
			avoid = true;
		}

		@Override
		public void press(int cell, boolean hard) {
			WellWater.affectCell(cell);
		}
	},
	STATUE {
		@Override
		public void setup() {
			solid = true;
		}
	},
	STATUE_SP {
		@Override
		public void setup() {
			solid = true;
		}
	},
	BOOKSHELF {
		@Override
		public void setup() {
			flamable = true;
			solid = true;
			losBlocking = true;
		}
	},
	ALCHEMY {
		@Override
		public void setup() {
			solid = true;
		}
	};

	public boolean passable 	= false;
	public boolean losBlocking  = false;
	public boolean flamable 	= false;
	public boolean secret 		= false;
	public boolean solid 		= false;
	public boolean avoid 		= false;
	public boolean liquid 		= false;
	public boolean pit 			= false;

	Terrain() {
		setup();
	}

	public abstract void setup();

	public Terrain discover() {
		return this;
	}

	protected void triggerTrap(int cell, Trap trap) {
		if (trap != null) {

			TimekeepersHourglass.timeFreeze timeFreeze =
					Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);

			Swiftthistle.TimeBubble bubble =
					Dungeon.hero.buff(Swiftthistle.TimeBubble.class);

			if (bubble != null){

				Sample.INSTANCE.play(Assets.SND_TRAP);

				Dungeon.level.discover(cell);

				bubble.setDelayedPress(cell);

			} else if (timeFreeze != null){

				Sample.INSTANCE.play(Assets.SND_TRAP);

				Dungeon.level.discover(cell);

				timeFreeze.setDelayedPress(cell);

			} else {

				if (Dungeon.hero.pos == cell) {
					Dungeon.hero.interrupt();
				}

				trap.trigger();

			}
		}
	}

	public void press(int cell) {
		press(cell, false);
	}

	public void press(int cell, boolean hard) {

	}
}