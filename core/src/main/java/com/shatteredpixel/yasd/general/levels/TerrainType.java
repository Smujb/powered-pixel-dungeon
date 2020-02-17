package com.shatteredpixel.yasd.general.levels;

public enum TerrainType {
    CHASM {
        @Override
        public void setup() {
            AVOID = true;
            PIT = true;
        }
    },
    EMPTY {
        @Override
        public void setup() {
            PASSABLE = true;
        }
    },
    GRASS {
        @Override
        public void setup() {
            PASSABLE = true;
            FLAMABLE = true;
        }
    },
    EMPTY_WELL {
        @Override
        public void setup() {
            EMPTY.setup();
        }
    },
    WATER {
        @Override
        public void setup() {
            PASSABLE = true;
            LIQUID = true;
        }
    },
    WALL {
        @Override
        public void setup() {
            LOS_BLOCKING = true;
            SOLID = true;
        }
    },
    DOOR {
        @Override
        public void setup() {
            PASSABLE = true;
            LOS_BLOCKING = true;
            FLAMABLE = true;
            SOLID = true;
        }
    },
    OPEN_DOOR {
        @Override
        public void setup() {
            PASSABLE = true;
            FLAMABLE = true;
        }
    },
    ENTRANCE {
        @Override
        public void setup() {
            EMPTY.setup();
        }
    },
    EXIT {
        @Override
        public void setup() {
            EMPTY.setup();
        }
    },
    EMBERS {
        @Override
        public void setup() {
            EMPTY.setup();
        }
    },
    DRY {
        @Override
        public void setup() {
            EMPTY.setup();
        }
    },
    LOCKED_DOOR {
        @Override
        public void setup() {
            LOS_BLOCKING = true;
            SOLID = true;
        }
    },
    PEDESTAL {
        @Override
        public void setup() {
            EMPTY.setup();
        }
    },
    WALL_DECO {
        @Override
        public void setup() {
            WALL.setup();
        }
    },
    BARRICADE {
        @Override
        public void setup() {
            FLAMABLE = true;
            SOLID = true;
            LOS_BLOCKING = true;
        }
    },
    EMPTY_SP {
        @Override
        public void setup() {
            EMPTY.setup();
        }
    },
    HIGH_GRASS {
        @Override
        public void setup() {
            PASSABLE = true;
            LOS_BLOCKING = true;
            FLAMABLE = true;
        }
    },
    FURROWED_GRASS {
        @Override
        public void setup() {
            HIGH_GRASS.setup();
        }
    },
    SECRET_DOOR {
        @Override
        public void setup() {
            WALL.setup();
            SECRET = true;
        }
    },
    TRAP {
        @Override
        public void setup() {
            AVOID = true;
        }
    },
    SECRET_TRAP {
        @Override
        public void setup() {
            EMPTY.setup();
            SECRET = true;
        }
    },
    INACTIVE_TRAP {
        @Override
        public void setup() {
            EMPTY.setup();
        }
    },
    EMPTY_DECO {
        @Override
        public void setup() {
            EMPTY.setup();
        }
    },
    LOCKED_EXIT {
        @Override
        public void setup() {
            SOLID = true;
        }
    },
    UNLOCKED_EXIT {
        @Override
        public void setup() {
            PASSABLE = true;
        }
    },
    SIGN {
        @Override
        public void setup() {
            PASSABLE = true;
            FLAMABLE = true;
        }
    },
    WELL {
        @Override
        public void setup() {
            AVOID = true;
        }
    },
    STATUE {
        @Override
        public void setup() {
            SOLID = true;
        }
    },
    STATUE_SP {
        @Override
        public void setup() {
            STATUE.setup();
        }
    },
    BOOKSHELF {
        @Override
        public void setup() {
            BARRICADE.setup();
        }
    },
    ALCHEMY {
        @Override
        public void setup() {
            SOLID = true;
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
}
