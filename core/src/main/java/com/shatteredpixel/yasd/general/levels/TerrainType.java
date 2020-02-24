package com.shatteredpixel.yasd.general.levels;

public enum TerrainType {
    CHASM {
        @Override
        public void setup() {
            AVOID = true;
            PIT = true;
            value = 0;
        }
    },
    EMPTY {
        @Override
        public void setup() {
            PASSABLE = true;
            value = 1;
        }
    },
    GRASS {
        @Override
        public void setup() {
            PASSABLE = true;
            FLAMABLE = true;
            value = 2;
        }
    },
    EMPTY_WELL {
        @Override
        public void setup() {
            EMPTY.setup();
            value = 3;
        }
    },
    WATER {
        @Override
        public void setup() {
            PASSABLE = true;
            LIQUID = true;
            value = 29;
        }
    },
    WALL {
        @Override
        public void setup() {
            LOS_BLOCKING = true;
            SOLID = true;
            value = 4;
        }
    },
    DOOR {
        @Override
        public void setup() {
            PASSABLE = true;
            LOS_BLOCKING = true;
            FLAMABLE = true;
            SOLID = true;
            value = 5;
        }
    },
    OPEN_DOOR {
        @Override
        public void setup() {
            PASSABLE = true;
            FLAMABLE = true;
            value = 6;
        }
    },
    ENTRANCE {
        @Override
        public void setup() {
            EMPTY.setup();
            value = 7;
        }
    },
    EXIT {
        @Override
        public void setup() {
            EMPTY.setup();
            value = 8;
        }
    },
    EMBERS {
        @Override
        public void setup() {
            EMPTY.setup();
            value = 9;
        }
    },
    DRY {
        @Override
        public void setup() {
            EMPTY.setup();
            value = 31;
        }
    },
    LOCKED_DOOR {
        @Override
        public void setup() {
            LOS_BLOCKING = true;
            SOLID = true;
            value = 10;
        }
    },
    PEDESTAL {
        @Override
        public void setup() {
            EMPTY.setup();
            value = 11;
        }
    },
    WALL_DECO {
        @Override
        public void setup() {
            WALL.setup();
            value = 12;
        }
    },
    BARRICADE {
        @Override
        public void setup() {
            FLAMABLE = true;
            SOLID = true;
            LOS_BLOCKING = true;
            value = 13;
        }
    },
    EMPTY_SP {
        @Override
        public void setup() {
            EMPTY.setup();
            value = 14;
        }
    },
    HIGH_GRASS {
        @Override
        public void setup() {
            PASSABLE = true;
            LOS_BLOCKING = true;
            FLAMABLE = true;
            value = 15;
        }
    },
    FURROWED_GRASS {
        @Override
        public void setup() {
            HIGH_GRASS.setup();
            value = 30;
        }
    },
    SECRET_DOOR {
        @Override
        public void setup() {
            WALL.setup();
            SECRET = true;
            value = 16;
        }
    },
    TRAP {
        @Override
        public void setup() {
            AVOID = true;
            value = 18;
        }
    },
    SECRET_TRAP {
        @Override
        public void setup() {
            EMPTY.setup();
            SECRET = true;
            value = 17;
        }
    },
    INACTIVE_TRAP {
        @Override
        public void setup() {
            EMPTY.setup();
            value = 19;
        }
    },
    EMPTY_DECO {
        @Override
        public void setup() {
            EMPTY.setup();
            value = 20;
        }
    },
    LOCKED_EXIT {
        @Override
        public void setup() {
            SOLID = true;
            value = 21;
        }
    },
    UNLOCKED_EXIT {
        @Override
        public void setup() {
            PASSABLE = true;
            value = 22;
        }
    },
    SIGN {
        @Override
        public void setup() {
            PASSABLE = true;
            FLAMABLE = true;
            value = 23;
        }
    },
    WELL {
        @Override
        public void setup() {
            AVOID = true;
            value = 24;
        }
    },
    STATUE {
        @Override
        public void setup() {
            SOLID = true;
            value = 25;
        }
    },
    STATUE_SP {
        @Override
        public void setup() {
            STATUE.setup();
            value = 26;
        }
    },
    BOOKSHELF {
        @Override
        public void setup() {
            BARRICADE.setup();
            value = 27;
        }
    },
    ALCHEMY {
        @Override
        public void setup() {
            SOLID = true;
            value = 28;
        }
    };

    public boolean PASSABLE		    = false;
    public boolean LOS_BLOCKING	    = false;
    public boolean FLAMABLE		    = false;
    public boolean SECRET			= false;
    public boolean SOLID			= false;
    public boolean AVOID			= false;
    public boolean LIQUID			= false;
    public boolean PIT				= false;
    public int value = -1;

    TerrainType() {
        setup();
    }

    public abstract void setup();

    public TerrainType discover() {
        switch (this) {
            case SECRET_DOOR:
                return DOOR;
            case SECRET_TRAP:
                return TRAP;
            default:
                return this;
        }
    }

    public static int[] values(TerrainType[] values) {
        int[] returnValues = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            returnValues[i] = values[i].value;
        }
        return returnValues;
    }
}
