/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.yasd.general.levels;

public enum Terrain {

	NONE {
		@Override
		public void setup() {

		}
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
	},
	FURROWED_GRASS {
		@Override
		public void setup() {
			passable = true;
			losBlocking = true;
			flamable = true;
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

	public boolean passable = false;
	public boolean losBlocking = false;
	public boolean flamable = false;
	public boolean secret = false;
	public boolean solid = false;
	public boolean avoid = false;
	public boolean liquid = false;
	public boolean pit = false;

	Terrain() {
		setup();
	}

	public abstract void setup();



	public Terrain discover() {
		return this;
	}
}


	/*public static final int CHASM			= 0;
	public static final int EMPTY			= 1;
	public static final int GRASS			= 2;
	public static final int EMPTY_WELL		= 3;
	public static final int WALL			= 4;
	public static final int DOOR			= 5;
	public static final int OPEN_DOOR		= 6;
	public static final int ENTRANCE		= 7;
	public static final int EXIT			= 8;
	public static final int EMBERS			= 9;
	public static final int LOCKED_DOOR		= 10;
	public static final int PEDESTAL		= 11;
	public static final int WALL_DECO		= 12;
	public static final int BARRICADE		= 13;
	public static final int EMPTY_SP		= 14;
	public static final int HIGH_GRASS		= 15;
	public static final int FURROWED_GRASS	= 30;
	public static final int DRY 			= 31;
	public static final int WALL_ANCIENT	= 32;
	public static final int WALL_MODERN	    = 33;

	public static final int WALL_ANCIENT_PLACEHOLDER= 34;//Placeholders if they aren't visible
	public static final int WALL_MODERN_PLACEHOLDER = 35;

	public static final int SECRET_DOOR	    = 16;
	public static final int SECRET_TRAP     = 17;
	public static final int TRAP            = 18;
	public static final int INACTIVE_TRAP   = 19;

	public static final int EMPTY_DECO		= 20;
	public static final int LOCKED_EXIT		= 21;
	public static final int UNLOCKED_EXIT	= 22;
	public static final int SIGN			= 23;
	public static final int WELL			= 24;
	public static final int STATUE			= 25;
	public static final int STATUE_SP		= 26;
	public static final int BOOKSHELF		= 27;
	public static final int ALCHEMY			= 28;

	public static final int WATER		    = 29;
	
	public static final int passable		= 0x01;
	public static final int losBlocking	= 0x02;
	public static final int flamable		= 0x04;
	public static final int secret			= 0x08;
	public static final int solid			= 0x10;
	public static final int avoid			= 0x20;
	public static final int liquid			= 0x40;
	public static final int pit				= 0x80;
	
	public static final int[] flags = new int[256];
	static {
		flags[CHASM]		= avoid	| pit;
		flags[EMPTY]		= passable;
		flags[GRASS]		= passable | flamable;
		flags[EMPTY_WELL]	= passable;
		flags[WATER]		= passable | liquid;
		flags[WALL]			= losBlocking | solid;
		flags[DOOR]			= passable | losBlocking | flamable | solid;
		flags[OPEN_DOOR]	= passable | flamable;
		flags[ENTRANCE]		= passable | solid;
		flags[EXIT]			= passable;
		flags[EMBERS]		= passable;
		flags[DRY]          = passable;
		flags[LOCKED_DOOR]	= losBlocking | solid;
		flags[PEDESTAL]		= passable;
		flags[WALL_DECO]	= flags[WALL];
		flags[BARRICADE]	= flamable | solid | losBlocking;
		flags[EMPTY_SP]		= flags[EMPTY];
		flags[HIGH_GRASS]	= passable | losBlocking | flamable;
		flags[FURROWED_GRASS]= flags[HIGH_GRASS];

		flags[SECRET_DOOR]  = flags[WALL]  | secret;
		flags[SECRET_TRAP]  = flags[EMPTY] | secret;
		flags[TRAP]         = avoid;
		flags[INACTIVE_TRAP]= flags[EMPTY];

		flags[EMPTY_DECO]	= flags[EMPTY];
		flags[LOCKED_EXIT]	= solid;
		flags[UNLOCKED_EXIT]= passable;
		flags[SIGN]			= passable | flamable;
		flags[WELL]			= avoid;
		flags[STATUE]		= solid;
		flags[STATUE_SP]	= flags[STATUE];
		flags[BOOKSHELF]	= flags[BARRICADE];
		flags[ALCHEMY]		= solid;
		flags[WALL_ANCIENT] = flags[WALL_MODERN] = flags[WALL];
		flags[WALL_ANCIENT_PLACEHOLDER] = flags[WALL_MODERN_PLACEHOLDER] = flags[EMPTY];
	}

	public static int discover( int terr ) {
		switch (terr) {
		case SECRET_DOOR:
			return DOOR;
		case SECRET_TRAP:
			return TRAP;
		default:
			return terr;
		}
	}

	public static void swapLevelState(Level level, Level.State state) {
		for (int i = 0; i < level.map.length; i++) {
			Dungeon.level.set(i, swapTerrainState(level.map[i], state));
		}
		GameScene.updateMap();
		Dungeon.observe();
	}

	public static int swapTerrainState(int terr, Level.State state) {
		if (state == Level.State.FUTURE) {
			switch (terr) {
				case WATER:
					terr = DRY;
					break;
				case WALL_ANCIENT:
					terr = WALL_ANCIENT_PLACEHOLDER;
					break;
				case WALL_MODERN_PLACEHOLDER:
					terr = WALL_MODERN;
					break;
			}
		} else if (state == Level.State.REGULAR) {
			switch (terr) {
				case DRY:
					terr = WATER;
					break;
				case WALL_ANCIENT:
					terr = WALL_ANCIENT_PLACEHOLDER;
					break;
				case WALL_MODERN:
					terr = WALL_MODERN_PLACEHOLDER;
					break;
			}
		} else if (state == Level.State.PAST) {
			switch (terr) {
				case DRY:
					terr = WATER;
					break;
				case WALL_ANCIENT_PLACEHOLDER:
					terr = WALL_ANCIENT;
					break;
				case WALL_MODERN:
					terr = WALL_MODERN_PLACEHOLDER;
					break;
			}
		}
		return terr;
	}

	//removes signs, places floors instead
	public static int[] convertTilesFrom0_6_0b(int[] map){
		for (int i = 0; i < map.length; i++){
			if (map[i] == 23){
				map[i] = 1;
			}
		}
		return map;
	}

}*/
