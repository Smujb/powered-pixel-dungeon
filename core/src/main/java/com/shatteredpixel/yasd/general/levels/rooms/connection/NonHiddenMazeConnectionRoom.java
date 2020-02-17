package com.shatteredpixel.yasd.general.levels.rooms.connection;

import com.shatteredpixel.yasd.general.levels.Level;

public class NonHiddenMazeConnectionRoom extends MazeConnectionRoom {
    @Override
    public void paint(Level level) {
        super.paint(level);
        for (Door door : connected.values()) {
            door.set( Door.Type.REGULAR );
        }
    }
}
